package it.makeit.alfresco.webscriptsapi;

import it.makeit.alfresco.restApi.RESTQueryParams;

public enum AlfrescoWorkflowInstanceQueryParamsEnum implements RESTQueryParams {
	INITIATOR("initiator"), INCLUDE_TASKS("includeTasks");

	private String name;

	AlfrescoWorkflowInstanceQueryParamsEnum(String pName) {
		name = pName;
	}

	public String getName() {
		return name;
	}
}
