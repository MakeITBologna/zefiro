/**
 * Workflow Module
 * @author Alba Quarto
 */
angular.module('workflow', [])

	.constant("workflowFormBlacklist", [
		"cm_owner",
		"cm_name",
		//PACKAGE
		"bpm_packageContains",
		//TASK
		"bpm_taskId",
		"bpm_description",
		"bpm_startDate",
		"bpm_completionDate",
		"bpm_dueDate",
		"bpm_priority",
		"bpm_percentComplete",
		"bpm_pooledActors",
		"bpm_comment",
		"bpm_status",
		//WORKFLOW TASK
		"bpm_context",
		"bpm_outcome",
		"bpm_completedItems",
		"bpm_packageActionGroup",
		"bpm_packageItemActionGroup",
		"bpm_hiddenTransitions",
		"bpm_reassignable",
		"bpm_package",
		//ACTIVITY OUTCOME TASK
		"bpm_outcomePropertyName",
		//START TASK
		"bpm_workflowDescription",
		"bpm_workflowDueDate",
		"bpm_workflowPriority",
		"bpm_sendEMailNotifications",
		//WORKFLOW DEFINITION
		"bpm_definitionName",
		"bpm_engineId",
		"bpm_definitionDeployed",
		"bpm_packageActionGroup",
		"bpm_packageItemActionGroup",
		//ASPECTS
		"bpm_assignee",
		"bpm_assignees",
		"bpm_groupAssignee",
		"bpm_groupAssignees",
		"bpm_isSystemPackage",
		"bpm_workflowDefinitionId",
		"bpm_workflowDefinitionName",
		"bpm_workflowInstanceId",
		"bpm_endAutomatically",
	])

	.constant("workflowAssigneeAspects", [
		"bpm_assignee",
		"bpm_assignees",
		"bpm_groupAssignee",
		"bpm_groupAssignees",
	])

	.constant("taskState", {
		"CLAIMED": "claimed",
		"UNCLAIMED": "unclaimed",
		"DELEGATED": "delegated",
		"RESOLVED": "resolved",
		"COMPLETED": "completed"
	})

	.constant('AUTHORITY_TYPE', {
		'GROUP': 'group',
		'PERSON': 'person'
	})

	.constant("processFieldName")

	.constant("OUTCOME_PROPERTY_NAME", "bpm_outcomePropertyName")
	
	.constant("WORKFLOW_DEFINITION_PROPERTIES", {
		"URL": "url",
		"TITLE": "title"
	})
	
	.constant("WORKFLOW_INSTANCE_PROPERTIES", {
		"URL": "url",
		"DEFINITION_URL": "definitionUrl",
		"START_DATE": "startDate",
		"END_DATE": "endDate",
		"MESSAGE":"message",
		"DUE_DATE":"dueDate",
		"PRIORITY":"priority"
	})

	.constant('deadlineTMessage',['jbMessages', function(jbMessages){
		return{
			"1": jbMessages.task.expired,
			"2": jbMessages.task.expiring
		}
	}])
	
	.constant('WF_VARIABLE_SCOPE', {
		"LOCAL": "local",
		"GLOBAL": "global"
	})



	/**
	 * Utility service for workflow
	 * @author Alba Quarto
	 */
	.factory('jbWorkflowUtil', ['AUTHORITY_TYPE','WF_VARIABLE_SCOPE', 'jbMessages', function (AUTHORITY_TYPE, WF_VARIABLE_SCOPE, jbMessages) {
		return {
			decodeType: function (type) {
				switch (type) {
					case "d:text": return "STRING";
					case "d:int": return "INTEGER";
					case "d:long": return "INTEGER";
					case "d:float": return "DECIMAL";
					case "d:double": return "DECIMAL";
					case "d:boolean": return "BOOLEAN";
					case "d:date": return "DATE";
					case "d:datetime": return "DATETIME";
					default: return "STRING";
				}
			},
			/**
			 * @param acepted: "PROCESS_NAME" | "STARTED_AT" | "DUE_AT" | "PRIORITY" | "DEADLINE_PROX".Default return given argument.
			 * */
			taskFieldName: function (field) {
				switch (field) {
					case "PROCESS_NAME": return "processName";
					case "STARTED_AT": return "startedAt";
					case "DUE_AT": return "dueAt";
					case "PRIORITY": return "priority";
					case "DEADLINE_PROX": return "deadlineProximity";
					case "VARIABLES": return "variables";
					case "STATE": return "state";
					case "ASSIGNEE": return "assignee";
					default: return field;
				}

			},
			processFieldName: function (field) {
				switch (field) {
					case "PROCESS_DEFINITION_ID": return "processDefinitionId";
					case "PROCESS_DEFINITION_KEY": return "processDefinitionKey";
					case "VARIABLES": return "variables";
					default: return field;

				}
			},
			getVoidVariable: function (name, type) {
				return {
					scope: WF_VARIABLE_SCOPE.LOCAL,
					name: name,
					type: type,
					value: null

				}
			},
			getAssigneeType: function (assigneeAspect) {
				switch (assigneeAspect) {
					case "bpm_assignee": return { type: AUTHORITY_TYPE.PERSON, many: false };
					case "bpm_assignees": return { type: AUTHORITY_TYPE.PERSON, many: true };
					case "bpm_groupAssignee": return { type: AUTHORITY_TYPE.GROUP, many: false };
					case "bpm_groupAssignees": return { type: AUTHORITY_TYPE.GROUP, many: true };
				}
			}, 
			sanitizeId: function (id){
				var sanitizedId = "";
				if(id){
					var parts = id.split("$");
					sanitizedId =  parts[parts.length -1];
				}
				return sanitizedId;
		    }, getPriorityValues: function(){
		    	return	{
					"1": jbMessages.high_f,
					"2": jbMessages.medium_f,
					"3": jbMessages.low_f,
				}
		    }, getStatusTranslateValues: function (){	
		    	return {
		    		"Not Yet Started": jbMessages.workflow.status.NYS,
		    		"In Progress": jbMessages.workflow.status.inProgress,
		    		"On Hold": jbMessages.workflow.status.onHold,
		    		"Cancelled": jbMessages.workflow.status.cancelled,
		    		"Completed": jbMessages.workflow.status.completed
		    	}
		    } 
		}
	}])
