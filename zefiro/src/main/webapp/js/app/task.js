/**
 * Task module
 * @author Alba Quarto
 */
angular.module('task', ['ngResource', 'ui.bootstrap', 'ngTable', 'angular.filter'])

.factory('TaskResource', ['$resource', function($resource) {
	return $resource('a/Task/:id', {id:'@id'},{
		getFormModel : {
			isArray: true,
			url:'a/Task/:id/formModel',
			method: 'GET'
		},
		getVariables: {
			isArray: true,
			url:'a/Task/:id/variables',
			method: 'GET'
		},
		getItems:  {
			isArray: true,
			url:'a/Task/:id/items',
			method: 'GET'
		}
	});
}])

.controller('TaskController', ['$scope', 'TaskResource', 'NgTableParams', 'jbMessages', 'jbWorkflowUtil', 'jbUtil', 'jbValidate',
	function($scope, TaskResource,  NgTableParams, jbMessages, jbWorkflowUtil, jbUtil, jbValidate) {

	$scope.taskTable = new NgTableParams({group: "processName"}, {counts: [],groupOptions: {
        isExpanded: false
    }});
	var TASK_LIST = 'taskList';
	
	$scope.jbMessages = jbMessages;
	$scope.jbWorkflowUtil = jbWorkflowUtil;
	$scope.jbUtil = jbUtil;
	$scope.jbValidate = jbValidate;
	
	$scope.isGroupHeaderRowVisible = false;
	$scope.editing = false;
	$scope.readOnly = false;
	$scope.currentRowNum = -1;
	$scope.currentGroupNum=-1;
	$scope.taskEditing = {};
	$scope.breadCrumbIndex = -1;
	$scope.breadcrumbs = [];
	$scope.currentTaskForm = [];
	$scope.currentTaskItems = [];
	$scope.updatedVariables = {}
	
	//Ricerca documenti a partire dalla form di ricerca
	$scope.search = function() {
		var taskPromise = TaskResource.query($scope.taskEditing, function() {
			console.log(taskPromise)
			$scope.taskTable.settings({dataset: taskPromise, });
		});
		
		return taskPromise;
	}
	
	$scope.startEdit = function(group_i, row_i) {
		console.log("Editing: "+ group_i+ " "+ row_i, $scope.taskTable);
		$scope.currentRowNum = row_i;
		$scope.currentGroupNum = group_i;
		$scope.breadCrumbIndex = 0;
		$scope.taskEditing = {};
		$scope.currentTaskForm = [];
		$scope.breadcrumbs = [];
		$scope.currentTaskItems = [];
		$scope.updatedVariables = {};
		$scope.taskEditing.id = $scope.taskTable.data[$scope.currentGroupNum].data[$scope.currentRowNum].id;
		var taskPromise = TaskResource.get($scope.taskEditing, function() {
			$scope.taskEditing = taskPromise;
			$scope.breadcrumbs.push({		
				id: $scope.taskEditing.id,
				name: $scope.taskEditing.name,
				description: $scope.taskEditing.description,
				businessKey: $scope.taskEditing.processBusinessKey,
				rownum: row_i,
				groupnum: group_i
			});
			var formPromise = TaskResource.getFormModel({id:$scope.taskEditing.id}, function() {
				console.log("-----formPromise", formPromise);
				$scope.currentTaskForm = formPromise;
				var itemsPromise = TaskResource.getItems({id:$scope.taskEditing.id}, function() {
					console.log("-----itemsPromise", itemsPromise);
					$scope.currentTaskItems = itemsPromise;
				})
				var variablesPromise = TaskResource.getVariables({id:$scope.taskEditing.id}, function(){
						console.log("-----varPromise", variablesPromise);
						variablesPromise.forEach(function(variable) {
							$scope.updatedVariables[variable.name] = variable;
						});
					$scope.editing = true;
				});
			});
		});
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
	
	$scope.deadlineProximity = function(date) {
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
		if(candidates && candidates.length > 0){
			msg = jbMessages.task.assignedUsers;
		}
		return msg;
	}
	
	$scope.getValidClass = function(el){
		if(el){
			return jbValidate.getClass(el);
		}
	}
	
	$scope.gotoTaskBreadcrumb = function(i, form) {
		if (i < 0) {
			$scope.breadcrumbs = [];
			$scope.closeDetail(form);
			return;
		}
		var shortDocument = $scope.breadcrumbs[i];
		$scope.breadCrumbIndex = i-1;
		if (i == 0) {
			$scope.startEdit(shortDocument.rownum);
		} else {
			$scope.breadcrumbs = $scope.breadcrumbs.slice(0, i);
			$scope.showDocument(shortDocument);
		}
	}
	
	//Chiude la pagina di dettaglio
	$scope.closeDetail = function(form) {
		$scope.editing = false;
		$scope.readOnly = false;
		if (form) $scope.clearDetail(form);
	}
	
	//Pulisce il form della pagina di dettaglio
	$scope.clearDetail = function(form) {
		jbValidate.clearForm(form);
		$scope.taskEditing = {};
	}
	
	//Gestione anteprima nel dettaglio
	$scope.getDocumentObjectHTML = function(document){
		console.log("--------documentHTML", document);
		return "<object data=\"" + "a/Document/" + document.id + "/preview" + "\" width=\"100%\" style=\"height: 100vh;\" ></object>";
	}
	
	
}])