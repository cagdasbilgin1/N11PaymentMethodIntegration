package payment.service;

import payment.exception.UnsupportedPaymentMethodException;
import payment.method.PaymentMethod;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class PaymentMethodRegistry {

    private final Map<String, PaymentMethod> paymentMethods = new LinkedHashMap<>();

    public void register(PaymentMethod paymentMethod) {
        Objects.requireNonNull(paymentMethod, "paymentMethod cannot be null");

        String normalizedCode = normalize(paymentMethod.code());
        if (paymentMethods.containsKey(normalizedCode)) {
            throw new IllegalArgumentException("Payment method already registered: " + normalizedCode);
        }

        paymentMethods.put(normalizedCode, paymentMethod);
    }

    public PaymentMethod getRequired(String paymentMethodCode) {
        String normalizedCode = normalize(paymentMethodCode);
        PaymentMethod paymentMethod = paymentMethods.get(normalizedCode);

        if (paymentMethod == null) {
            throw new UnsupportedPaymentMethodException(normalizedCode);
        }

        return paymentMethod;
    }

    public Set<String> getAvailableCodes() {
        return Collections.unmodifiableSet(paymentMethods.keySet());
    }

    private String normalize(String paymentMethodCode) {
        Objects.requireNonNull(paymentMethodCode, "paymentMethodCode cannot be null");

        if (paymentMethodCode.isBlank()) {
            throw new IllegalArgumentException("paymentMethodCode cannot be blank");
        }

        return paymentMethodCode.trim().toUpperCase(Locale.ROOT);
    }
}
