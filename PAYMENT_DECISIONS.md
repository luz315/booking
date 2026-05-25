# 플래시 세일 예약 시스템 기술적 설계 결정사항

## 🎯 요구사항
- **결제 수단**: 신용카드, Y페이, Y포인트 지원
- **복합 결제**: (신용카드 + 포인트) 또는 (Y페이 + 포인트) 가능
- **제약 조건**: 신용카드와 Y페이는 혼용 불가
- **타임아웃**: 결제 시작 후 20분 내 미완료 시 자동 취소
- **확장성**: 새로운 결제 수단 추가 시 최소 변경

## 🏗️ 설계 원칙

### 1. Strategy Pattern 적용
```java
// 결제 수단별 처리 전략 분리
public interface PaymentProcessor {
    PaymentType getSupportedType();
    PaymentResult process(PaymentMethod paymentMethod, String transactionId);
    void cancel(PaymentMethod paymentMethod, String transactionId, String externalTransactionId);
}

// 구체적인 프로세서 구현 (예시)
@Component
public class CreditCardProcessor implements PaymentProcessor {
    @Override
    public PaymentType getSupportedType() { return PaymentType.CREDIT_CARD; }
    // 신용카드 결제 로직 구현...
}
```

### 2. 복합 결제 검증 로직
```java
public class PaymentValidationService {
    public void validatePaymentMethods(List<PaymentMethod> methods, Money totalAmount) {
        // 신용카드 + Y페이 혼용 불가
        if (containsBoth(CREDIT_CARD, Y_PAY)) {
            throw new PaymentInvalidInputException("신용카드와 Y페이는 함께 사용할 수 없습니다");
        }
        
        // 포인트 단독 사용 불가
        if (onlyContains(Y_POINT)) {
            throw new PaymentInvalidInputException("포인트는 다른 결제 수단과 함께 사용해야 합니다");
        }
    }
}
```

### 3. 원자적 복합 결제 처리
```java
public class CompositePaymentService {
    public void processPayment(Payment payment) {
        List<ProcessedPayment> processedPayments = new ArrayList<>();
        
        try {
            // 순차 처리 (실패 시 이전 결제들 자동 롤백)
            for (PaymentMethod method : payment.getPaymentMethods()) {
                PaymentResult result = processor.process(method, payment.getId());
                processedPayments.add(new ProcessedPayment(processor, method, result));
            }
            payment.markAsProcessed();
            
        } catch (Exception e) {
            // 실패 시 롤백
            rollbackProcessedPayments(processedPayments, payment.getId());
            payment.markAsFailed(e.getMessage());
            throw e;
        }
    }
}
```

## 🕐 타임아웃 관리

### 1. 스케줄러 기반 타임아웃 처리
```java
@Scheduled(fixedDelay = 60_000) // 1분마다 실행
public void processTimeoutPayments() {
    LocalDateTime boundary = LocalDateTime.now().minusMinutes(20);
    List<Payment> timeoutPayments = paymentRepository.findTimeoutPayments(PENDING, boundary);
    
    for (Payment payment : timeoutPayments) {
        payment.markAsTimeout();
        cancelRelatedBookingAndReleaseStock(payment);
    }
}
```

### 2. 연관 데이터 정리
- **예약 취소**: 타임아웃 시 관련 예약 자동 실패 처리
- **재고 복구**: Redis 및 DB 재고 원상복구
- **알림**: 사용자에게 타임아웃 알림 (선택적)

## 🔧 확장성 보장 방안

### 1. 새로운 결제 수단 추가 시
```java
// 1단계: 새로운 PaymentType 추가
public enum PaymentType {
    CREDIT_CARD, Y_PAY, Y_POINT, 
    NAVER_PAY  // 새로운 결제 수단
}

// 2단계: PaymentMethod 구체 클래스 추가
public class NaverPayPayment extends PaymentMethod {
    // 네이버페이 특화 로직
}

// 3단계: PaymentProcessor 구현체 추가
@Component
public class NaverPayProcessor implements PaymentProcessor {
    @Override
    public PaymentType getSupportedType() { return PaymentType.NAVER_PAY; }
    // 네이버페이 결제 처리 로직 구현
}

// 4단계: 필요시 검증 규칙 추가
// PaymentValidationService에서 새로운 조합 규칙 설정
```

### 2. 변경 최소화 전략
- **기존 코드 수정 없음**: 새 프로세서만 추가하면 자동 등록
- **설정 기반 검증**: 복합 결제 규칙을 설정으로 관리 가능
- **이벤트 기반 확장**: 결제 완료/실패 시 이벤트 발행으로 추가 로직 처리

## 🏛️ 아키텍처 레이어

### Domain Layer
```
payment/domain/
├── model/
│   ├── Payment.java              # 결제 애그리거트 루트
│   ├── PaymentMethod.java        # 결제 수단 추상 클래스
│   ├── PaymentStatus.java        # 결제 상태
│   └── [PaymentType]Payment.java # 구체 결제 수단 클래스들
├── service/
│   ├── PaymentValidationService.java  # 복합 결제 검증
│   ├── PaymentProcessor.java          # 결제 처리 전략 인터페이스
│   └── CompositePaymentService.java   # 복합 결제 오케스트레이션
└── exception/
    └── Payment[Type]Exception.java
```

### Application Layer
```
payment/application/
├── usecase/
│   ├── ProcessPaymentUseCase.java
│   └── CancelPaymentUseCase.java
└── port/out/
    ├── PaymentRepository.java
    └── PaymentGateway.java
```

### Infrastructure Layer
```
payment/infra/
├── processor/
│   ├── CreditCardProcessor.java     # 신용카드 처리기 구현
│   ├── YPayProcessor.java           # Y페이 처리기 구현
│   └── YPointProcessor.java         # Y포인트 처리기 구현
└── scheduler/
    └── PaymentTimeoutScheduler.java # 타임아웃 처리 스케줄러
```

## 💡 핵심 설계 원칙

1. **개방-폐쇄 원칙**: 새로운 결제 수단 추가에는 개방, 기존 코드 수정에는 폐쇄
2. **단일 책임 원칙**: 각 PaymentProcessor는 하나의 결제 수단만 담당
3. **의존성 역전**: 고수준 모듈(CompositePaymentService)이 저수준 모듈(구체 프로세서)에 의존하지 않음
4. **원자성**: 복합 결제는 모두 성공 또는 모두 실패 (부분 성공 없음)
5. **멱등성**: 동일한 결제 요청은 동일한 결과 보장

이 설계를 통해 새로운 결제 수단이 추가되어도 **기존 Booking API의 비즈니스 로직은 전혀 수정할 필요가 없으며**, PaymentProcessor 구현체만 추가하면 자동으로 시스템에 통합됩니다.