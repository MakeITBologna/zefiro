package it.makeit.alfresco.webscriptsapi;

import it.makeit.alfresco.restApi.RESTQueryParams;

public enum AlfrescoWorkflowInstanceQueryParamsEnum implements RESTQueryParams {
	INITIATOR("initiator"), INCLUDE_TASKS("includeTasks"), STATE("state"), 
	COMPLETED_BEFORE("completedBefore"), COMPLETED_AFTER("completedAfter"),
	STARTED_BEFORE("startedBefore"), STARTED_AFTER("startedAfter"),
	DUE_BEFORE("dueBefore"), DUE_AFTER("dueAfter")
	;

	private String name;

	AlfrescoWorkflowInstanceQueryParamsEnum(String pName) {
		name = pName;
	}

	@Override
	public String getName() {
		return name;
	}
}
