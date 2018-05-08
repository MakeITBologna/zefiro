package it.makeit.zefiro.dao;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 *
 * @author MAKE IT
 */
public class RelationBean implements BaseData {

	private String id;

	private String typeId;

	private String sourceId;

	private String targetId;

	public RelationBean() {
	}

	public String getId() {
		return id;
	}

	public void setId(String pStrId) {
		id = pStrId;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String pStrTypeId) {
		typeId = pStrTypeId;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String pStrSourceId) {
		sourceId = pStrSourceId;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String pStrTargetId) {
		targetId = pStrTargetId;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
