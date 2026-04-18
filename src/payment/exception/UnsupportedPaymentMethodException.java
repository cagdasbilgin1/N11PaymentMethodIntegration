package payment.exception;

public final class UnsupportedPaymentMethodException extends RuntimeException {

    public UnsupportedPaymentMethodException(String paymentMethodCode) {
        super("Unsupported payment method: " + paymentMethodCode);
    }
}
