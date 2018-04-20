package it.makeit.zefiro.dao;

import java.util.Date;

public class WorkFlowProcessComplete {
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

	private String name;
	private String title;
	private String description;
	private Integer version;

	public String getId() {
		return id;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public Date getStartedAt() {
		return startedAt;
	}

	public Date getEndedAt() {
		return endedAt;
	}

	public Long getDurationInMs() {
		return durationInMs;
	}

	public String getStartActivityDefinitionId() {
		return startActivityDefinitionId;
	}

	public String getEndActivityDefinitionId() {
		return endActivityDefinitionId;
	}

	public String getStartUserId() {
		return startUserId;
	}

	public String getDeleteReason() {
		return deleteReason;
	}

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

	public void setId(String id) {
		this.id = id;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public void setStartedAt(Date startedAt) {
		this.startedAt = startedAt;
	}

	public void setEndedAt(Date endedAt) {
		this.endedAt = endedAt;
	}

	public void setDurationInMs(Long durationInMs) {
		this.durationInMs = durationInMs;
	}

	public void setStartActivityDefinitionId(String startActivityDefinitionId) {
		this.startActivityDefinitionId = startActivityDefinitionId;
	}

	public void setEndActivityDefinitionId(String endActivityDefinitionId) {
		this.endActivityDefinitionId = endActivityDefinitionId;
	}

	public void setStartUserId(String startUserId) {
		this.startUserId = startUserId;
	}

	public void setDeleteReason(String deleteReason) {
		this.deleteReason = deleteReason;
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

}
