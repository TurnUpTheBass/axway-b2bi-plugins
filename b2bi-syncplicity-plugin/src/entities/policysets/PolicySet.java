package entities.policysets;

import java.io.Serializable;



import entities.Group;
import entities.policies.Policy;

@SuppressWarnings("serial")
public class PolicySet implements Serializable {
	public String Id;

	public String Name;

	public int Priority;

	public boolean IsDefault;

	public Policy Policy;

	public Group[] Groups;

	public EntitySetType EntitySetType = entities.policysets.EntitySetType
			.values()[0];

	public String CopiedFromSetId;
}