package payment.service;

import payment.method.PaymentMethod;
import payment.model.PaymentRequest;
import payment.model.PaymentResult;

import java.util.Objects;

public final class PaymentProcessor {

    private final PaymentMethodRegistry paymentMethodRegistry;

    public PaymentProcessor(PaymentMethodRegistry paymentMethodRegistry) {
        this.paymentMethodRegistry = Objects.requireNonNull(
                paymentMethodRegistry,
                "paymentMethodRegistry cannot be null"
        );
    }

    public PaymentResult process(String paymentMethodCode, PaymentRequest paymentRequest) {
        Objects.requireNonNull(paymentRequest, "paymentRequest cannot be null");

        PaymentMethod paymentMethod = paymentMethodRegistry.getRequired(paymentMethodCode);
        return paymentMethod.pay(paymentRequest);
    }
}
