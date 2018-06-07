package it.makeit.alfresco.webscriptsapi.model;

import java.util.Date;
import java.util.List;

public class WorkflowInstance {
	private String id;
	private String url;
	private String name;
	private String title;
	private String description;
	private boolean isActive;
	private Date startDate;
	private int priority;
	private String message;
	private Date endDate;
	private Date dueDate;
	private String context;
	private PersonUser initiator;
	private String definitionUrl;
	private WorkflowDefinition definition;
	private List<TaskInstance> tasks;

	public String getId() {
		return id;
	}

	public String getUrl() {
		return url;
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

	public boolean isActive() {
		return isActive;
	}

	public Date getStartDate() {
		return startDate;
	}

	public int getPriority() {
		return priority;
	}

	public String getMessage() {
		return message;
	}

	public Date getEndDate() {
		return endDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public String getContext() {
		return context;
	}

	public PersonUser getInitiator() {
		return initiator;
	}

	public String getDefinitionUrl() {
		return definitionUrl;
	}

	public WorkflowDefinition getDefinition() {
		return definition;
	}

	public List<TaskInstance> getTasks() {
		return tasks;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public void setInitiator(PersonUser initiator) {
		this.initiator = initiator;
	}

	public void setDefinitionUrl(String definitionUrl) {
		this.definitionUrl = definitionUrl;
	}

	public void setDefinition(WorkflowDefinition definition) {
		this.definition = definition;
	}

	public void setTasks(List<TaskInstance> tasks) {
		this.tasks = tasks;
	}
}
