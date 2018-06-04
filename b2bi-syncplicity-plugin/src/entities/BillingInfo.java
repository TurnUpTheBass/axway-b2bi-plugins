package entities;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BillingInfo implements Serializable {
	public PaymentMethod PaymentMethod = entities.PaymentMethod.values()[0];

	public CreditCard CreditCard;
}