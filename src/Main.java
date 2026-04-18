import payment.method.CreditCardPayment;
import payment.method.PayPalPayment;
import payment.method.ApplePayPayment;
import payment.model.PaymentRequest;
import payment.model.PaymentResult;
import payment.service.PaymentMethodRegistry;
import payment.service.PaymentProcessor;

import java.math.BigDecimal;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        PaymentMethodRegistry registry = new PaymentMethodRegistry();
        registry.register(new CreditCardPayment());
        registry.register(new PayPalPayment());
        registry.register(new ApplePayPayment());

        PaymentProcessor paymentProcessor = new PaymentProcessor(registry);

        List<CheckoutScenario> checkoutScenarios = List.of(
                new CheckoutScenario(
                        "CREDIT_CARD",
                        new PaymentRequest("ORDER-1001", new BigDecimal("1599.90"), "TRY", "ayse@example.com")
                ),
                new CheckoutScenario(
                        "PAYPAL",
                        new PaymentRequest("ORDER-1002", new BigDecimal("499.50"), "TRY", "mehmet@example.com")
                ),
                new CheckoutScenario(
                        "APPLE_PAY",
                        new PaymentRequest("ORDER-1003", new BigDecimal("799.00"), "TRY", "zeynep@example.com")
                )
        );

        System.out.println("Available payment methods: " + registry.getAvailableCodes());
        System.out.println();

        for (CheckoutScenario checkoutScenario : checkoutScenarios) {
            PaymentResult result = paymentProcessor.process(
                    checkoutScenario.paymentMethodCode(),
                    checkoutScenario.paymentRequest()
            );
            printResult(result);
        }

        System.out.println("To add a new payment method, create a new PaymentMethod implementation and register it in the registry.");
    }

    private static void printResult(PaymentResult result) {
        System.out.printf(
                """
                Payment method: %s
                Order id: %s
                Status: %s
                Message: %s
                Transaction id: %s

                """,
                result.paymentMethodCode(),
                result.orderId(),
                result.success() ? "SUCCESS" : "FAILED",
                result.message(),
                result.transactionId()
        );
    }

    private record CheckoutScenario(String paymentMethodCode, PaymentRequest paymentRequest) {
    }
}
