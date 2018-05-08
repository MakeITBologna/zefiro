package it.makeit.zefiro.dao;

import it.makeit.alfresco.workflow.model.WorkflowProcess;
import it.makeit.zefiro.DecodedFieldNote;
import it.makeit.zefiro.DecodedFieldNote.DecodingType;

public class WorkFlowProcessComplete extends WorkflowProcess implements BaseData {
	private String name;
	private String title;
	private String description;
	private Integer version;
	private String startUserFirstName;
	private String startUserLastName;

	public String getName() {
		return name;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Integer getVersion() {
		return version;
	}

	@DecodedFieldNote(decodingType = DecodingType.DEFINITION, value = "name")
	public void setName(String name) {
		this.name = name;
	}

	@DecodedFieldNote(decodingType = DecodingType.DEFINITION, value = "title")
	public void setTitle(String title) {
		this.title = title;
	}

	@DecodedFieldNote(decodingType = DecodingType.DEFINITION, value = "description")
	public void setDescription(String description) {
		this.description = description;
	}

	@DecodedFieldNote(decodingType = DecodingType.DEFINITION, value = "version")
	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getStartUserFirstName() {
		return startUserFirstName;
	}

	public String getStartUserLastName() {
		return startUserLastName;
	}

	@DecodedFieldNote(decodingType = DecodingType.STARTER, value = "firstName")
	public void setStartUserFirstName(String startUserFirstName) {
		this.startUserFirstName = startUserFirstName;
	}

	@DecodedFieldNote(decodingType = DecodingType.STARTER, value = "lastName")
	public void setStartUserLastName(String startUserLastName) {
		this.startUserLastName = startUserLastName;
	}

}
