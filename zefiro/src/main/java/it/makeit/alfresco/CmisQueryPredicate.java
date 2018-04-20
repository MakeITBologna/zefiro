package it.makeit.alfresco;

import java.util.Date;

public class CmisQueryPredicate<T> {

	public static enum Operator {
		EQ,
		LIKE,
		LE,
		GE,
		BETWEEN,
		IN_FOLDER,
		IN_TREE,
		CONTAINS;
	}

	private final ParamType paramType;
	private final Class<T> paramClass;

	private String propertyId;
	private Operator operator;
	private T value1;
	private T value2;

	private static CmisQueryPredicate<?> create(ParamType pParamType) {
		switch (pParamType) {
		case ID:
		case STRING:
		case PROPERTY:
		case TYPE:
		case CONTAINS:
		case IN_FOLDER:
		case IN_TREE:
			return new CmisQueryPredicate<String>(pParamType, String.class);

		case BOOLEAN:
			return new CmisQueryPredicate<Boolean>(pParamType, Boolean.class);

		case DATETIME:
			return new CmisQueryPredicate<Date>(pParamType, Date.class);

		case NUMBER:
			return new CmisQueryPredicate<Number>(pParamType, Number.class);

		default:
			throw new IllegalArgumentException("Tipo non supportato");
		}
	}

	private CmisQueryPredicate(ParamType paramType, Class<T> paramClass) {
		this.paramType = paramType;
		this.paramClass = paramClass;
	}

	public static CmisQueryPredicate<String> eqTo(String pPropertyId, String pValue) {
		@SuppressWarnings("unchecked")
		CmisQueryPredicate<String> lPredicate =
		        (CmisQueryPredicate<String>) CmisQueryPredicate.create(ParamType.STRING);
		lPredicate.setEq(pPropertyId, pValue);
		return lPredicate;
	}

	public static CmisQueryPredicate<Number> eqTo(String pPropertyId, Number pValue) {
		@SuppressWarnings("unchecked")
		CmisQueryPredicate<Number> lPredicate =
		        (CmisQueryPredicate<Number>) CmisQueryPredicate.create(ParamType.NUMBER);
		lPredicate.setEq(pPropertyId, pValue);
		return lPredicate;
	}

	public static CmisQueryPredicate<Boolean> eqTo(String pPropertyId, Boolean pValue) {
		@SuppressWarnings("unchecked")
		CmisQueryPredicate<Boolean> lPredicate =
		        (CmisQueryPredicate<Boolean>) CmisQueryPredicate.create(ParamType.BOOLEAN);
		lPredicate.setEq(pPropertyId, pValue);
		return lPredicate;
	}

	public static CmisQueryPredicate<Date> eqTo(String pPropertyId, Date pValue) {
		@SuppressWarnings("unchecked")
		CmisQueryPredicate<Date> lPredicate =
		        (CmisQueryPredicate<Date>) CmisQueryPredicate.create(ParamType.DATETIME);
		lPredicate.setEq(pPropertyId, pValue);
		return lPredicate;
	}

	public static CmisQueryPredicate<Number> between(String pPropertyIdId, Number pFrom, Number pTo) {
		@SuppressWarnings("unchecked")
		CmisQueryPredicate<Number> lPredicate =
		        (CmisQueryPredicate<Number>) CmisQueryPredicate.create(ParamType.NUMBER);
		lPredicate.setBetween(pPropertyIdId, pFrom, pTo);
		return lPredicate;
	}

	public static CmisQueryPredicate<Date> between(String pPropertyIdId, Date pFrom, Date pTo) {
		@SuppressWarnings("unchecked")
		CmisQueryPredicate<Date> lPredicate =
		        (CmisQueryPredicate<Date>) CmisQueryPredicate.create(ParamType.DATETIME);
		lPredicate.setBetween(pPropertyIdId, pFrom, pTo);
		return lPredicate;
	}

	public static CmisQueryPredicate<String> like(String pPropertyId, String pValue) {
		@SuppressWarnings("unchecked")
		CmisQueryPredicate<String> lPredicate =
		        (CmisQueryPredicate<String>) CmisQueryPredicate.create(ParamType.STRING);
		lPredicate.setLike(pPropertyId, pValue);;
		return lPredicate;
	}

	public static CmisQueryPredicate<String> inFolder(String pFolderId) {
		@SuppressWarnings("unchecked")
		CmisQueryPredicate<String> lPredicate =
		        (CmisQueryPredicate<String>) CmisQueryPredicate.create(ParamType.IN_FOLDER);
		lPredicate.setInFolder(pFolderId);
		return lPredicate;
	}

	public static CmisQueryPredicate<String> inTree(String pFolderId) {
		@SuppressWarnings("unchecked")
		CmisQueryPredicate<String> lPredicate =
		        (CmisQueryPredicate<String>) CmisQueryPredicate.create(ParamType.IN_TREE);
		lPredicate.setInTree(pFolderId);
		return lPredicate;
	}

	public static CmisQueryPredicate<String> contains(String pText) {
		@SuppressWarnings("unchecked")
		CmisQueryPredicate<String> lPredicate =
		        (CmisQueryPredicate<String>) CmisQueryPredicate.create(ParamType.CONTAINS);
		lPredicate.setContains(pText);
		return lPredicate;
	}

	private void set(String propertyId, Operator operator, T value1, T value2) {
		if ((operator == Operator.LIKE || operator == Operator.IN_FOLDER || operator == Operator.IN_TREE
		        || operator.equals(Operator.CONTAINS)) && !((value1 instanceof String) || (value2 instanceof String))) {
			throw new IllegalArgumentException("il predicato richiesto è valido solo per valori di tipo stringa");
		}

		this.propertyId = propertyId;
		this.operator = operator;
		this.value1 = value1;
		this.value2 = value2;
	}

	public void setEq(String propertyId, T value) {
		set(propertyId, Operator.EQ, value, null);
	}

	public void setGe(String propertyId, T value) {
		set(propertyId, Operator.GE, value, null);
	}

	public void setLe(String propertyId, T value) {
		set(propertyId, Operator.LE, value, null);
	}

	public void setBetween(String propertyId, T valueFrom, T valueTo) {
		set(propertyId, Operator.BETWEEN, valueFrom, valueTo);
	}

	@SuppressWarnings("unchecked")
	public void setLike(String propertyId, String value) {
		set(propertyId, Operator.LIKE, (T) value, null);
	}

	@SuppressWarnings("unchecked")
	public void setInFolder(String folderId) {
		set(null, Operator.IN_FOLDER, (T) folderId, null);
	}

	@SuppressWarnings("unchecked")
	public void setInTree(String folderId) {
		set(null, Operator.IN_TREE, (T) folderId, null);
	}

	@SuppressWarnings("unchecked")
	public void setContains(String text) {
		set(null, Operator.CONTAINS, (T) text, null);
	}

	public ParamType getParamType() {
		return paramType;
	}

	public Class<T> getParamClass() {
		return paramClass;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public T getValue() {
		return value1;
	}

	public T getValueFrom() {
		return value1;
	}

	public T getValueTo() {
		return value2;
	}
}
