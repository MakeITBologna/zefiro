angular.module('newProcess',[])

.constant("workflowAssigneeAspects", [
	"bpm_assignee",
	"bpm_assignees",
	"bpm_groupAssignee",
	"bpm_groupAssignees",
])

.constant("defaultWhiteList", [
	"bpm_workflowDescription",
	"bpm_workflowDueDate",
	"bpm_workflowPriority",
	"bpm_sendEMailNotifications",
	"bpm_percentComplete",
	"bpm_comment",
	"bpm_status",
])

.controller('NewProcessController', ['$scope', 'ProcessResource', 'workflowAssigneeAspects', 'defaultWhiteList', function($scope, ProcessResource,workflowAssigneeAspects,defaultWhiteList){

	$scope.back = function(){
		$scope.$emit('StartedNewProcess', false);
	}
	
	$scope.selectedType = false;	

	$scope.choiseSelectTitle = "";
	$scope.choiseSelectData =  [];
	
	$scope.initStartNewProcess = function(){
		var definitionsPromise = ProcessResource.processDefinitions(function() {
			console.log("------definitionsPromise",definitionsPromise)
			$scope.choiseSelectData =  definitionsPromise;
			})
	}
	
	$scope.groupType = {};
	$scope.choise = function (item) {
		$scope.choiseSelectTitle =  item.name ? item.name : item.key;
		startEditProcess(item);
	}
	
	$scope.currentTypeForm = [];
	startEditProcess = function(definition){
		$scope.selectedType=null;
		formPromise = ProcessResource.startForm({id: definition.id}, function() {
			console.log("-----formPromise", formPromise);
			buildStartForm(formPromise);
		});
	}
	
	$scope.addAssignee = null;
	$scope.updatedVariables = {};
	buildStartForm = function (formModel, fieldWhiteList) {
		//create the form and the relative variable map
		$scope.updatedVariables = {};
		$scope.currentTypeForm = [];
		$scope.addAssignee = null;
		for (var i = 0; i < formModel.length; i++) {
			var model = formModel[i];
			var modelName = model.name;
			assigneeAspect = workflowAssigneeAspects.includes(modelName);
			var whiteList = fieldWhiteList ? fieldWhiteList :defaultWhiteList;
			if (workflowFormBlacklist.includes(modelName) && !assigneeAspect && !whiteList.includes(modelName)) {
				continue;
			}
			$scope.updatedVariables[modelName] = null;
			if(assigneeAspect){
				$scope.addAssignee = assigneeAspect
			} else {
				$scope.currentTypeForm.push(model)
			}
		}
		$scope.selectedType = item;
	}

}])
