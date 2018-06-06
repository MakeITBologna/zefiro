package it.makeit.alfresco.workflow.model;

import java.util.Date;
import java.util.Map;

public class WorkflowProcess {

	private String id;
	private String processDefinitionId;
	private String businessKey;
	private Date startedAt;
	private Date endedAt;
	private Long durationInMs;
	private String startActivityDefinitionId;
	private String endActivityDefinitionId;
	private String startUserId;
	private String deleteReason;

	// only for post
	private Map<String, Object> variables;
	// only for post
	private String processDefinitionKey;

	public String getProcessDefinitionKey() {
		return processDefinitionKey;
	}

	public void setProcessDefinitionKey(String processDefinitionKey) {
		this.processDefinitionKey = processDefinitionKey;
	}

	public Map<String, Object> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, Object> variables) {
		this.variables = variables;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public Date getStartedAt() {
		return startedAt;
	}

	public void setStartedAt(Date startedAt) {
		this.startedAt = startedAt;
	}

	public Date getEndedAt() {
		return endedAt;
	}

	public void setEndedAt(Date endedAt) {
		this.endedAt = endedAt;
	}

	public Long getDurationInMs() {
		return durationInMs;
	}

	public void setDurationInMs(Long durationInMs) {
		this.durationInMs = durationInMs;
	}

	public String getStartActivityDefinitionId() {
		return startActivityDefinitionId;
	}

	public void setStartActivityDefinitionId(String startActivityDefinitionId) {
		this.startActivityDefinitionId = startActivityDefinitionId;
	}

	public String getEndActivityDefinitionId() {
		return endActivityDefinitionId;
	}

	public void setEndActivityDefinitionId(String endActivityDefinitionId) {
		this.endActivityDefinitionId = endActivityDefinitionId;
	}

	public String getStartUserId() {
		return startUserId;
	}

	public void setStartUserId(String startUserId) {
		this.startUserId = startUserId;
	}

	public String getDeleteReason() {
		return deleteReason;
	}

	public void setDeleteReason(String deleteReason) {
		this.deleteReason = deleteReason;
	}
}
