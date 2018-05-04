package it.makeit.alfresco.restApi;

public enum AlfrescoWhereOperatorEnum {

	EQUAL("=%s"), GREATER(">%s"), LESS("<%s"), GREATER_EQUAL(">=%s"), LESS_EQUAL("<=%s"), BEETWEEN(
			" BETWEEN[%s, %s]"), MATCHES(" MATCHES %s"), IN(" IN [%s]"), AND(" AND "), OR(" OR ");
	String operator;

	AlfrescoWhereOperatorEnum(String pOperator) {
		operator = pOperator;
	}

	public String getOperator() {
		return operator;
	}
}
