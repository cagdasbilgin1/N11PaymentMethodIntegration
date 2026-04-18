# N11 Payment Method Integration

This is a small Java project built to show how a payment flow can be extended without breaking the existing structure.

The application currently supports:

- Credit Card
- PayPal
- Apple Pay

The main idea is simple: each payment method implements the same `PaymentMethod` interface, and the system resolves the correct one through `PaymentMethodRegistry`. This keeps the payment flow easy to extend and fits well with SOLID, especially Open/Closed Principle and Single Responsibility Principle.

## Run

```bash
javac -d out $(find src -name '*.java')
java -cp out Main
```

When you run the app, it prints a sample payment result for all available payment methods.
