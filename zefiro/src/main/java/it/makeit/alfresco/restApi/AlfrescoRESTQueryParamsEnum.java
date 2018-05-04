package it.makeit.alfresco.restApi;

/**
 * @author Alba Quarto
 */
public enum AlfrescoRESTQueryParamsEnum {
	MAX_ITEMS("maxItems"), ORDER_BY("orderBy"), PROPERTIES("properties"), SKIP_COUNT("skipCount"), WHERE(
			"where"), SELECT("select");

	private String name;

	AlfrescoRESTQueryParamsEnum(String pName) {
		name = pName;
	}

	public String getName() {
		return name;
	}
}
