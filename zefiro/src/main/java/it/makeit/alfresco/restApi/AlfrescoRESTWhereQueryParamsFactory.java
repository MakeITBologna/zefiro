package it.makeit.alfresco.restApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Alba Quarto
 */
public class AlfrescoRESTWhereQueryParamsFactory {
	private List<Map<String, AlfrescoParamPredicate>> values;

	public AlfrescoRESTWhereQueryParamsFactory() {
		values = new ArrayList<Map<String, AlfrescoParamPredicate>>();
	}

	public AlfrescoRESTWhereQueryParamsFactory and() {
		values.add(addVoidValueMap(AlfrescoWhereOperatorEnum.AND));
		return this;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map<String, AlfrescoParamPredicate> addVoidValueMap(AlfrescoWhereOperatorEnum pEnum) {
		Map<String, AlfrescoParamPredicate> map = new HashMap();
		List value = new ArrayList();
		value.add("");
		map.put("", new AlfrescoParamPredicate(pEnum, value));
		values.add(map);

		return map;
	}

	public String build() {
		String query = "(";

		for (Map<String, AlfrescoParamPredicate> value : values) {
			for (Entry<String, AlfrescoParamPredicate> field : value.entrySet()) {
				query = query.concat(field.getKey());
				String listValue = "";
				Object[] fieldValues = field.getValue().getValues().toArray();
				for (int i = 0; i < fieldValues.length; i++) {
					if (i > 0) {
						listValue = listValue.concat(",");
					}
					listValue = listValue.concat(fieldValues[i].toString());
				}
				String fieldValue = field.getValue().getOperator().getOperator().replace("%s", listValue);

				query = query.concat(fieldValue);
			}
		}

		query = query.concat(")");
		return query;
	}

	public AlfrescoRESTWhereQueryParamsFactory and(List<Map<String, AlfrescoParamPredicate>> pValues) {
		for (int i = 0; i < pValues.size(); i++) {
			this.values.add(pValues.get(i));
			if (i < pValues.size() - 1) {
				and();
			}
		}
		return this;
	}

	public AlfrescoRESTWhereQueryParamsFactory add(String pField, AlfrescoParamPredicate pValue) {
		Map<String, AlfrescoParamPredicate> value = new HashMap<String, AlfrescoParamPredicate>();
		value.put(pField, pValue);
		this.values.add(value);
		return this;
	}
}
