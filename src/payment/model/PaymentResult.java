package payment.model;

import java.util.Objects;

public record PaymentResult(
        String paymentMethodCode,
        String orderId,
        boolean success,
        String message,
        String transactionId
) {

    public PaymentResult {
        Objects.requireNonNull(paymentMethodCode, "paymentMethodCode cannot be null");
        Objects.requireNonNull(orderId, "orderId cannot be null");
        Objects.requireNonNull(message, "message cannot be null");
        Objects.requireNonNull(transactionId, "transactionId cannot be null");
    }
}
