import payment.model.PaymentRequest;
import payment.model.PaymentResult;
import payment.service.PaymentMethodDiscovery;
import payment.service.PaymentMethodRegistry;
import payment.service.PaymentProcessor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final String PAYMENT_METHOD_PACKAGE = "payment.method";

    public static void main(String[] args) {
        PaymentMethodRegistry registry = new PaymentMethodRegistry();
        PaymentMethodDiscovery paymentMethodDiscovery = new PaymentMethodDiscovery();
        paymentMethodDiscovery.discover(PAYMENT_METHOD_PACKAGE).forEach(registry::register);

        PaymentProcessor paymentProcessor = new PaymentProcessor(registry);

        List<String> paymentMethodCodes = List.copyOf(registry.getAvailableCodes());
        Scanner scanner = new Scanner(System.in);

        System.out.println("Payment methods discovered with reflection: " + registry.getAvailableCodes());
        System.out.println();
        printPaymentMethodOptions(paymentMethodCodes);

        int selectedIndex;
        try {
            selectedIndex = readSelectedIndex(scanner, paymentMethodCodes.size());
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
            return;
        }

        CheckoutScenario checkoutScenario = new CheckoutScenario(
                paymentMethodCodes.get(selectedIndex),
                createSamplePaymentRequest(selectedIndex)
        );

        PaymentResult result = paymentProcessor.process(
                checkoutScenario.paymentMethodCode(),
                checkoutScenario.paymentRequest()
        );

        System.out.println();
        printResult(result);

        System.out.println("To add a new payment method, create a new PaymentMethod implementation in the payment.method package.");
    }

    private static void printPaymentMethodOptions(List<String> paymentMethodCodes) {
        System.out.println("Which payment method would you like to use?");

        for (int index = 0; index < paymentMethodCodes.size(); index++) {
            System.out.printf("%s [%d]%n", paymentMethodCodes.get(index), index + 1);
        }

        System.out.print("Your choice: ");
    }

    private static int readSelectedIndex(Scanner scanner, int optionCount) {
        String input = scanner.nextLine().trim();

        try {
            int selectedOption = Integer.parseInt(input);

            if (selectedOption < 1 || selectedOption > optionCount) {
                throw new IllegalArgumentException("Invalid selection. Please choose a number between 1 and " + optionCount + ".");
            }

            return selectedOption - 1;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Invalid selection. Please enter a number.");
        }
    }

    private static PaymentRequest createSamplePaymentRequest(int index) {
        return new PaymentRequest(
                "ORDER-" + (1001 + index),
                BigDecimal.valueOf(499 + (index * 250L)).setScale(2),
                "TRY",
                "customer" + (index + 1) + "@example.com"
        );
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
