package payment.model;

import java.math.BigDecimal;
import java.util.Objects;

public record PaymentRequest(
        String orderId,
        BigDecimal amount,
        String currency,
        String customerEmail
) {

    public PaymentRequest {
        Objects.requireNonNull(orderId, "orderId cannot be null");
        Objects.requireNonNull(amount, "amount cannot be null");
        Objects.requireNonNull(currency, "currency cannot be null");
        Objects.requireNonNull(customerEmail, "customerEmail cannot be null");

        if (orderId.isBlank()) {
            throw new IllegalArgumentException("orderId cannot be blank");
        }

        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("amount must be greater than zero");
        }

        if (currency.isBlank()) {
            throw new IllegalArgumentException("currency cannot be blank");
        }

        if (customerEmail.isBlank()) {
            throw new IllegalArgumentException("customerEmail cannot be blank");
        }
    }
}
