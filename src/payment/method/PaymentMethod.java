package payment.method;

import payment.model.PaymentRequest;
import payment.model.PaymentResult;

public interface PaymentMethod {

    String code();

    PaymentResult pay(PaymentRequest request);
}
