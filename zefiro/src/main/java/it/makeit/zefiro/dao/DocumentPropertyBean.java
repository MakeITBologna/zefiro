package it.makeit.zefiro.dao;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;


/**
 *
 * @author MAKE IT
 */
public class DocumentPropertyBean {
    private String queryName;

    private String propertyType;

    private Object value;
    
    public DocumentPropertyBean() {
    	
    }
    
	public DocumentPropertyBean(String pQueryName, String pPropertyType, Object pValue) {
		setQueryName(pQueryName);
		setPropertyType(pPropertyType);
		setValue(pValue);
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String pStrName) {
    	queryName = pStrName;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String pStrPropertyType) {
    	propertyType = pStrPropertyType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object pValue) {
        value = pValue;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
