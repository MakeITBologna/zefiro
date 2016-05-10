package it.makeit.zefiro.dao;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;


/**
 *
 * @author MAKE IT
 */
public class TypePropertyBean {
    private String queryName;

    private String propertyType;

    private Object defaultValue;

    private String choices;

    private String linkType;

    private Boolean queryable;
    
    private String displayName;

    private String description;

    private Boolean required;
    
	public TypePropertyBean() {
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

    public void setPropertyType(String pStrType) {
    	propertyType = pStrType;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object pDefaultValue) {
        defaultValue = pDefaultValue;
    }

    public String getChoices() {
        return choices;
    }

    public void setChoices(String pStrChoices) {
        choices = pStrChoices;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String pStrLinkType) {
        linkType = pStrLinkType;
    }

    public Boolean getQueryable() {
        return queryable;
    }

    public void setQueryable(Boolean pBlnQueryable) {
        queryable = pBlnQueryable;
    }
	
	public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String pStrLabel) {
        displayName = pStrLabel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String pStrDescription) {
        description = pStrDescription;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean pBlnRequired) {
        required = pBlnRequired;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
