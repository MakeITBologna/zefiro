package it.makeit.alfresco.restApi;

import java.util.ArrayList;
import java.util.List;

public class AlfrescoParamPredicate {

	private AlfrescoWhereOperatorEnum operator;
	private List<Object> values;

	public AlfrescoParamPredicate() {
	}

	public AlfrescoParamPredicate(AlfrescoWhereOperatorEnum pOperator, List<Object> pValue) {
		operator = pOperator;
		values = pValue;
	}

	public AlfrescoWhereOperatorEnum getOperator() {
		return operator;
	}

	public List<Object> getValues() {
		return values;
	}

	public void setOperator(AlfrescoWhereOperatorEnum operator) {
		this.operator = operator;
	}

	public void setValues(List<Object> value) {
		this.values = value;
	}

	public void addValue(Object value) {
		if (values == null) {
			values = new ArrayList<Object>();
		}
		values.add(value);
	}
}
