package it.makeit.zefiro.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 *
 * @author MAKE IT
 */
public class DocumentTypeBean implements BaseData {
	private String id;

	private String name;

	private Map<String, TypePropertyBean> properties;

	public DocumentTypeBean() {
	}

	public String getId() {
		return id;
	}

	public void setId(String pStrId) {
		id = pStrId;
	}

	public String getName() {
		return name;
	}

	public void setName(String pStrName) {
		name = pStrName;
	}

	public Map<String, TypePropertyBean> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, TypePropertyBean> pProperties) {
		if (pProperties == null) {
			throw new IllegalArgumentException();
		}
		properties = pProperties;
	}

	public void addProperties(List<TypePropertyBean> pProperties) {
		if (pProperties == null) {
			throw new IllegalArgumentException();
		}

		if (properties == null) {
			properties = new LinkedHashMap<String, TypePropertyBean>();
		}

		for (TypePropertyBean lTypePropertyBean : pProperties) {
			setProperty(lTypePropertyBean);
		}
	}

	public TypePropertyBean getProperty(String pPropertyName) {
		return properties.get(pPropertyName);
	}

	public void setProperty(TypePropertyBean pProperty) {
		if (pProperty == null || pProperty.getQueryName() == null) {
			throw new IllegalArgumentException();
		}
		properties.put(pProperty.getQueryName(), pProperty);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
