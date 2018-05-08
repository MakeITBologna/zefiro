package it.makeit.alfresco.workflow.model;

import java.util.List;

public class Task {

	private String id;
	private String processId;
	private String processDefinitionId;
	private String activityDefinitionId;
	private String name;
	private String description;
	private String dueAt;
	private String startedAt;
	private String endedAt;
	private String durationInMs;
	private String priority;
	private String owner;
	private String assignee;
	private String formResourceKey;
	private String state;
	private List<Variable> variables;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getActivityDefinitionId() {
		return activityDefinitionId;
	}

	public void setActivityDefinitionId(String activityDefinitionId) {
		this.activityDefinitionId = activityDefinitionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDueAt() {
		return dueAt;
	}

	public void setDueAt(String dueAt) {
		this.dueAt = dueAt;
	}

	public String getStartedAt() {
		return startedAt;
	}

	public void setStartedAt(String startedAt) {
		this.startedAt = startedAt;
	}

	public String getEndedAt() {
		return endedAt;
	}

	public void setEndedAt(String endedAt) {
		this.endedAt = endedAt;
	}

	public String getDurationInMs() {
		return durationInMs;
	}

	public void setDurationInMs(String durationInMs) {
		this.durationInMs = durationInMs;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getFormResourceKey() {
		return formResourceKey;
	}

	public void setFormResourceKey(String formResourceKey) {
		this.formResourceKey = formResourceKey;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<Variable> getVariables() {
		return variables;
	}

	public void setVariables(List<Variable> variables) {
		this.variables = variables;
	}
}
