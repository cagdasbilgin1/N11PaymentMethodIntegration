# N11 Payment Method Integration

This project is a simple Java example that demonstrates how to add a new payment method to an existing payment flow while following SOLID principles.

The project includes:

- `CreditCardPayment` as the existing payment method
- `PayPalPayment` as the newly added payment method
- `PaymentMethod` as the shared extension point
- `PaymentProcessor` to orchestrate the payment flow
- `PaymentMethodRegistry` to register and resolve payment methods

## Design Approach

### Open/Closed Principle

The system is designed to be extended without changing the core payment flow.
To add a new payment method:

1. Create a new class that implements the `PaymentMethod` interface.
2. Register that class in the system through `PaymentMethodRegistry`.

This allows the system to grow without modifying `PaymentProcessor` or any of the existing payment method classes.

### Single Responsibility Principle

- `PaymentRequest` and `PaymentResult` only carry data.
- `PaymentProcessor` only orchestrates the payment flow.
- `PaymentMethodRegistry` only manages payment method registration and lookup.
- Each payment method class is responsible only for its own payment behavior.

### Dependency Inversion Principle

`PaymentProcessor` depends on the `PaymentMethod` abstraction instead of concrete payment classes.

## Project Structure

```text
src/
  Main.java
  payment/
    exception/
      UnsupportedPaymentMethodException.java
    method/
      PaymentMethod.java
      CreditCardPayment.java
      PayPalPayment.java
    model/
      PaymentRequest.java
      PaymentResult.java
    service/
      PaymentMethodRegistry.java
      PaymentProcessor.java
```

## Run

```bash
javac -d out $(find src -name '*.java')
java -cp out Main
```

## Expected Result

When the application runs, it prints a simple payment scenario for both the existing payment method and the newly added payment method.
