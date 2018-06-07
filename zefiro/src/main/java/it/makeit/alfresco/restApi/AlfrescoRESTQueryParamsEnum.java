package it.makeit.alfresco.restApi;

/**
 * @author Alba Quarto
 */
public enum AlfrescoRESTQueryParamsEnum implements RESTQueryParams {
	MAX_ITEMS("maxItems"), ORDER_BY("orderBy"), PROPERTIES("properties"), SKIP_COUNT("skipCount"), WHERE(
			"where"), SELECT("select"), FIELDS("fields"), TERM("term"), EXCLUDE("exclude");

	private String name;

	AlfrescoRESTQueryParamsEnum(String pName) {
		name = pName;
	}

	@Override
	public String getName() {
		return name;
	}
}
