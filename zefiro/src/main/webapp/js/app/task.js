/**
 * Task module
 */
angular.module('task', ['ngResource', 'ui.bootstrap', 'ngTable', 'angular.filter'])

.factory('TaskResource', ['$resource', '$log', function($resource, $log) {
	$log.log("TaskResource", $resource);
	return $resource('a/Task/:id', {id:'@id'},{
		getFormModel : {
			isArray: true,
			url:'a/Task/:id/formModel',
			method: 'GET'
		}
	});
}])

.controller('TaskController', ['$scope', 'TaskResource', 'NgTableParams', 'jbMessages', 'jbWorkflowUtil', 'jbUtil', '$log',
	function($scope, TaskResource,  NgTableParams, jbMessages, jbWorkflowUtil, jbUtil, $log) {

	$scope.taskTable = new NgTableParams({group: "processName"}, {counts: [],groupOptions: {
        isExpanded: false
    }});
	$scope.isGroupHeaderRowVisible = false;
	$scope.jbMessages = jbMessages;
	$scope.jbWorkflowUtil = jbWorkflowUtil;
	$scope.editing = false;
	$scope.currentRownum = -1;
	$scope.breadCrumbIndex = -1;
	$scope.documentBreadcrumbs = [];
	$scope.documentEditing = {};
	
	//Ricerca documenti a partire dalla form di ricerca
	$scope.search = function() {
		var taskPromise = TaskResource.query($scope.taskEditing, function() {
			$log.log(taskPromise)
			$scope.taskTable.settings({dataset: taskPromise, });
		});
		
		return taskPromise;
	}
	
	$scope.startEdit = function(i) {
		$log.log("Editing: "+ i);
//		$scope.currentRownum = i;
//		$scope.breadCrumbIndex = 0;
//		$scope.taskEditing = {};
//		$scope.taskEditing.id = $scope.taskTable.data[$scope.currentRownum].id;
//		var taskPromise = $scope.search();
//		formModelPromise = TaskResource.getFormModel({id:currentTask.id}, function(){
//			$log.log("form-model", formModelPromise);
//			$scope.editing = true;
//			$scope.currentTask=currentTask;
//			$scope.currentTaskFormModel = formModelPromise;
//		})
//		
//		return formModelPromise;
	}
	
	$scope.decodePriority = function(priority) {
		var priorityBadge = "";
		switch(priority){
		case "1": priorityName = jbMessages.high; break;
 		case "2": priorityName = jbMessages.medium; break;
		case "3": priorityName = jbMessages.low; break;
		}
		
		return priorityName;
	}
	
	//TODO CANCELLARE ALLA FINE
	$scope.log = function(id){
		$log.log("log id: " +id);
	}
	
	$scope.deadlineProximity = function(date) {
		$log.log("deadlineProximity: ", date)
		if(!date){
			return 3;
		}
		var parsedDate = jbUtil.stringToDate(date);
		if(!parsedDate){
			return 3;
		}
		var now = new Date();
		var parsedDateSum = parsedDate.getTime();
		var nowSum = now.getTime();
		var day = 86400000;
		
		var dlProx = 3;
		if(nowSum > (parsedDateSum-day)){
			dlProx = 2;
			if(nowSum > (parsedDateSum)){
				dlProx = 1;
			}
		}
		return dlProx;
	}
	
	$scope.deadlineTMessag = function(date) {
		$log.log("deadlineTMessag: "+date)
		dlProx = $scope.deadlineProximity(date);
		var msg = "";
		switch(dlProx){
			case 1: msg = jbMessages.task.expired; break;
	 		case 2: msg = jbMessages.task.expiring; break;
		}
		
		return msg;
	}
	
	$scope.assignedTMessage = function(candidates){
		var msg = jbMessages.task.assignedUser;
		if(candidates && candidates.length >= 0){
			msg = jbMessages.task.assignedUsers;
		}
		return msg;
	}
}])