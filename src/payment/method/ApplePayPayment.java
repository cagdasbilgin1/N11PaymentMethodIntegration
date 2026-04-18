package payment.method;

import payment.model.PaymentRequest;
import payment.model.PaymentResult;

import java.util.Locale;
import java.util.UUID;

public final class ApplePayPayment implements PaymentMethod {

    private static final String CODE = "APPLE_PAY";

    @Override
    public String code() {
        return CODE;
    }

    @Override
    public PaymentResult pay(PaymentRequest request) {
        String message = String.format(
                Locale.ROOT,
                "Payment of %.2f %s was completed with Apple Pay.",
                request.amount(),
                request.currency()
        );

        return new PaymentResult(
                CODE,
                request.orderId(),
                true,
                message,
                "AP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.ROOT)
        );
    }
}