package it.makeit.alfresco.restApi;

import java.util.Collection;

public class AlfrescoParamPredicate {

	private AlfrescoWhereOperatorEnum operator;
	private Collection value;

	public AlfrescoParamPredicate() {
	}

	public AlfrescoParamPredicate(AlfrescoWhereOperatorEnum pOperator, Collection pValue) {
		operator = pOperator;
		value = pValue;
	}

	public AlfrescoWhereOperatorEnum getOperator() {
		return operator;
	}

	public Collection getValue() {
		return value;
	}

	public void setOperator(AlfrescoWhereOperatorEnum operator) {
		this.operator = operator;
	}

	public void setValue(Collection value) {
		this.value = value;
	}

}
