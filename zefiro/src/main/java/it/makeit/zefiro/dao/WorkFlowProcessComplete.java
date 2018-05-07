package it.makeit.zefiro.dao;

import it.makeit.alfresco.workflow.model.WorkflowProcess;

public class WorkFlowProcessComplete extends WorkflowProcess {
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

	public void setName(String name) {
		this.name = name;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getStartUserFirstName() {
		return startUserFirstName;
	}

	public String getStartUserLastName() {
		return startUserLastName;
	}

	public void setStartUserFirstName(String startUserFirstName) {
		this.startUserFirstName = startUserFirstName;
	}

	public void setStartUserLastName(String startUserLastName) {
		this.startUserLastName = startUserLastName;
	}

}
