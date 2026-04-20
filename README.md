# N11 Payment Method Integration

This is a small Java project built to show how a payment flow can be extended without breaking the existing structure.

The application currently supports:

- Credit Card
- PayPal
- Apple Pay

The main idea is simple: each payment method implements the same `PaymentMethod` interface. The app discovers these implementations with reflection, registers them through `PaymentMethodRegistry`, and then runs the payment flow through `PaymentProcessor`.

This keeps the example easy to extend while still showing SOLID principles, especially Open/Closed Principle and Single Responsibility Principle.

## Run

```bash
javac -d out $(find src -name '*.java')
java -cp out Main
```

When you run the app, it asks which payment method you want to use and prints a sample result for that choice.
