package it.makeit.zefiro.dao;

import it.makeit.alfresco.workflow.model.Task;
import it.makeit.zefiro.DecodedFieldNote;
import it.makeit.zefiro.DecodedFieldNote.DecodingType;

public class TaskComplete extends Task implements BaseData {
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

	@DecodedFieldNote(decodingType = DecodingType.PROCESS, value = "name")
	public void setProcessName(String processName) {
		this.processName = processName;
	}

	@DecodedFieldNote(decodingType = DecodingType.PROCESS, value = "title")
	public void setProcessTitle(String processTitle) {
		this.processTitle = processTitle;
	}

	@DecodedFieldNote(decodingType = DecodingType.PROCESS, value = "description")
	public void setProcessDescription(String processDescription) {
		this.processDescription = processDescription;
	}

	@DecodedFieldNote(decodingType = DecodingType.PROCESS, value = "businessKey")
	public void setProcessBusinessKey(String processBusinessKey) {
		this.processBusinessKey = processBusinessKey;
	}

	@DecodedFieldNote(decodingType = DecodingType.PROCESS, value = "startUserId")
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

	@DecodedFieldNote(decodingType = DecodingType.PROCESS, value = "startUserFirstName")
	public void setStartUserFirstName(String startUserFirstName) {
		this.startUserFirstName = startUserFirstName;
	}

	@DecodedFieldNote(decodingType = DecodingType.PROCESS, value = "startUserLastName")
	public void setStartUserLastName(String startUserLastName) {
		this.startUserLastName = startUserLastName;
	}

	@DecodedFieldNote(decodingType = DecodingType.ASSIGNEE, value = "firstName")
	public void setAssigneeFirstName(String assigneeFirstName) {
		this.assigneeFirstName = assigneeFirstName;
	}

	@DecodedFieldNote(decodingType = DecodingType.ASSIGNEE, value = "lastName")
	public void setAssigneeLastName(String assigneeLastName) {
		this.assigneeLastName = assigneeLastName;
	}

	@DecodedFieldNote(decodingType = DecodingType.OWNER, value = "firstName")
	public void setOwnerFirstName(String ownerFirstName) {
		this.ownerFirstName = ownerFirstName;
	}

	@DecodedFieldNote(decodingType = DecodingType.OWNER, value = "lastName")
	public void setOwnerLastName(String ownerLastName) {
		this.ownerLastName = ownerLastName;
	}
}
