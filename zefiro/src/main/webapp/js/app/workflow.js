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
	"bpm_endAutomatically"
])

.constant("taskState", {
	"CLAIMED": "claimed",
	"UNCLAIMED": "unclaimed",
	"DELEGATED": "delegated",
	"RESOLVED": "resolved",
	"COMPLETED": "completed"
})

.constant("OUTCOME_PROPERTY_NAME","bpm_outcomePropertyName")

/**
 * Utility service for workflow
 * @author Alba Quarto
 */
.factory('jbWorkflowUtil', function() {
	return {
		decodeType: function(type){
			switch(type){
				case "d:text": return "STRING";
				case "d:int": return "INTEGER";
				case "d:long": return "INTEGER";
				case "d:float":return "DECIMAL";
				case "d:double":return "DECIMAL";
				case "d:boolean": return "BOOLEAN";
				case "d:date":  return "DATE";
				case "d:datetime": return "DATETIME";
				default: return "STRING";
			}
		}, 
		/**
		 * @param acepted: "PROCESS_NAME" | "STARTED_AT" | "DUE_AT" | "PRIORITY" | "DEADLINE_PROX".Default return given argument.
		 * */
		taskFieldName: function(field) {
			switch(field) {
				case "PROCESS_NAME": return "processName";
				case "STARTED_AT": return "startedAt";
				case "DUE_AT": return "dueAt";
				case "PRIORITY": return "priority";
				case "DEADLINE_PROX": return "deadlineProximity";
				case "VARIABLES": return "variables";
				default: return field;
			}
			
		},
		getVoidVariable: function(name, type){
			return {
				 scope: "local",
                 name: name,
                 type: type,
                 value: null
				
			}
		}
	}
})
