package entities.policies;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MobileDataSyncLimit implements Serializable {
	public int TransferLimit;

	public int BillingCycleResetDay;
}