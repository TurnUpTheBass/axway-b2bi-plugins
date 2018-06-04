package entities;

import java.io.Serializable;
import java.util.Date;



@SuppressWarnings("serial")
public class SubscriptionPlan implements Serializable {
	public SubscriptionType Type = SubscriptionType.values()[0];

	public Boolean PremiumSupport = null;

	public BillingInterval BillingInterval = entities.BillingInterval.values()[0];

	public String PromotionalCode;

	public BillingInfo BillingInfo;

	public Date RenewalDateUtc = null;
}