package com.demo.booking.booking.redis;

import com.demo.booking.booking.application.port.out.StockHoldPort;
import com.demo.booking.booking.domain.exception.StockHoldException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.io.ClassPathResource;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisStockHoldAdapter implements StockHoldPort {

    private static final long PAYMENT_TIMEOUT_SECONDS = 300L; // 5분
    private final RedisTemplate<String, String> redisTemplate;

    private static final DefaultRedisScript<String> HOLD_SCRIPT = createScript("redis-scripts/hold-stock.lua");
    private static final DefaultRedisScript<String> CANCEL_SCRIPT = createScript("redis-scripts/cancel-stock.lua");
    
    private static DefaultRedisScript<String> createScript(String scriptPath) {
        DefaultRedisScript<String> script = new DefaultRedisScript<>();
        script.setLocation(new ClassPathResource(scriptPath));
        script.setResultType(String.class);
        return script;
    }

    @Override
    public void holdStayDates(Long productId, Long userId, String idempotencyKey,
                                         LocalDate checkInDate, LocalDate checkOutDate) {
        try {
            List<String> keys = buildRedisKeys(productId, userId, idempotencyKey, checkInDate, checkOutDate);
            
            String result = redisTemplate.execute(
                    HOLD_SCRIPT,
                    keys,
                    String.valueOf(PAYMENT_TIMEOUT_SECONDS),
                    idempotencyKey
            );

            if (result == null) {
                throw new StockHoldException();
            }

            parseHoldResult(result);

        } catch (StockHoldException e) {
            throw e;
        } catch (Exception e) {
            log.error("Redis 재고 선점 실패: productId={}, userId={}", productId, userId, e);
            throw new StockHoldException();
        }
    }

    private void parseHoldResult(String result) {
        if (result.startsWith("SOLD_OUT:")) {
            String unavailableDateStr = result.substring("SOLD_OUT:".length());
            throw new StockHoldException(unavailableDateStr + "일 재고가 소진되었습니다");
        }

         switch (result) {
            case "SUCCESS" -> { return; }
            case "DUPLICATED" -> throw new StockHoldException("이미 예약을 선점했습니다");
            case "ALREADY_HELD" -> throw new StockHoldException("이미 처리된 요청입니다");
            default -> throw new StockHoldException();
        }
    }

    @Override  
    public void cancelStayDates(Long productId, Long userId, String idempotencyKey,
                               LocalDate checkInDate, LocalDate checkOutDate) {
        try {
            List<String> keys = buildRedisKeys(productId, userId, idempotencyKey, checkInDate, checkOutDate);
            
            String result = redisTemplate.execute(
                    CANCEL_SCRIPT,
                    keys,
                    idempotencyKey
            );
            
            log.info("재고 해제 완료: productId={}, userId={}, result={}", productId, userId, result);

        } catch (Exception e) {
            log.error("Redis 재고 해제 실패: productId={}, userId={}", productId, userId, e);
            // 해제 실패는 스케줄러가 나중에 처리하므로 예외를 던지지 않음
        }
    }

    @Override
    public int getCurrentStock(Long productId, LocalDate stayDate) {
        try {
            String stockKey = stockKey(productId, stayDate);
            String stockValue = redisTemplate.opsForValue().get(stockKey);
            return stockValue != null ? Integer.parseInt(stockValue) : 0;
        } catch (Exception e) {
            log.error("Redis 재고 조회 실패: productId={}, stayDate={}", productId, stayDate, e);
            return 0;
        }
    }

    private List<String> buildRedisKeys(Long productId, Long userId, String idempotencyKey,
                                       LocalDate checkInDate, LocalDate checkOutDate) {
        List<String> keys = new ArrayList<>();
        keys.add(userKey(productId, userId));
        keys.add(idempotencyKey(productId, idempotencyKey));

        List<LocalDate> stayDates = checkInDate.datesUntil(checkOutDate).toList();
        for (LocalDate stayDate : stayDates) {
            keys.add(stockKey(productId, stayDate));
        }
        
        return keys;
    }
    
    private String stockKey(Long productId, LocalDate stayDate) {
        return "flash-sale:{" + productId + "}:stock:" + stayDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
    
    private String userKey(Long productId, Long userId) {
        return "flash-sale:{" + productId + "}:user:" + userId;
    }
    
    private String idempotencyKey(Long productId, String key) {
        return "flash-sale:{" + productId + "}:idempotency:" + key;
    }
}