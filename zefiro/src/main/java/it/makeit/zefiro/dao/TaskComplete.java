package it.makeit.zefiro.dao;

import it.makeit.alfresco.workflow.model.Task;

public class TaskComplete extends Task {
	private String processName;
	private String processTitle;
	private String processDescription;
	private String processBusinessKey;
	private String ProcessStartUserId;
	private String startUserFirstName;
	private String startUserLastName;
	private String assigneeFirstName;
	private String assigneeLastName;
	private String ownerFirstName;
	private String ownerLastName;

	public String getProcessName() {
		return processName;
	}

	public String getProcessTitle() {
		return processTitle;
	}

	public String getProcessDescription() {
		return processDescription;
	}

	public String getProcessBusinessKey() {
		return processBusinessKey;
	}

	public String getProcessStartUserId() {
		return ProcessStartUserId;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public void setProcessTitle(String processTitle) {
		this.processTitle = processTitle;
	}

	public void setProcessDescription(String processDescription) {
		this.processDescription = processDescription;
	}

	public void setProcessBusinessKey(String processBusinessKey) {
		this.processBusinessKey = processBusinessKey;
	}

	public void setProcessStartUserId(String processStartUserId) {
		ProcessStartUserId = processStartUserId;
	}

	public String getStartUserFirstName() {
		return startUserFirstName;
	}

	public String getStartUserLastName() {
		return startUserLastName;
	}

	public String getAssigneeFirstName() {
		return assigneeFirstName;
	}

	public String getAssigneeLastName() {
		return assigneeLastName;
	}

	public String getOwnerFirstName() {
		return ownerFirstName;
	}

	public String getOwnerLastName() {
		return ownerLastName;
	}

	public void setStartUserFirstName(String startUserFirstName) {
		this.startUserFirstName = startUserFirstName;
	}

	public void setStartUserLastName(String startUserLastName) {
		this.startUserLastName = startUserLastName;
	}

	public void setAssigneeFirstName(String assigneeFirstName) {
		this.assigneeFirstName = assigneeFirstName;
	}

	public void setAssigneeLastName(String assigneeLastName) {
		this.assigneeLastName = assigneeLastName;
	}

	public void setOwnerFirstName(String ownerFirstName) {
		this.ownerFirstName = ownerFirstName;
	}

	public void setOwnerLastName(String ownerLastName) {
		this.ownerLastName = ownerLastName;
	}
}
