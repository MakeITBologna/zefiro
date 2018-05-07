angular.module('task', ['ngResource', 'ui.bootstrap', 'ngTable', 'angular.filter'])

.factory('TaskResource', ['$resource', '$log', function($resource, $log) {
	$log.log("ProcessResource", $resource);
	return $resource('a/Task/:id', {id:'@id'});
}])

.controller('TaskController', ['$scope', 'TaskResource', 'NgTableParams', 'jbMessages', '$log',
	function($scope, TaskResource,  NgTableParams, jbMessages, $log) {

	$scope.taskes = {};
	$scope.documentTable = new NgTableParams({count: 25, group: "processName"}, {});
	$scope.isGroupHeaderRowVisible = false;
	$scope.jbMessages = jbMessages;
	
	//Ricerca documenti a partire dalla form di ricerca
	$scope.search = function() {
		var documentPromise = TaskResource.query($scope.documentTemplate, function() {
			$log.log(documentPromise)
			$scope.documentTable.settings({dataset: documentPromise});
		});
		return documentPromise;
	}
	
	$scope.decodePriority = function(priority) {
		var priorityName = "";
		switch(priority){
		case "1": priorityName = jbMessages.high; break;
 		case "2": priorityName = jbMessages.medium; break;
		case "3": priorityName = jbMessages.low; break;
		}
		
		return priorityName;
	}
	
	
		//Ricerca documenti a partire dalla form di ricerca
		/*$scope.search = function() {
			var documentPromise = ProcessResource.query(null, function() {	
				var processes = {};
				let results = documentPromise;
				if(typeof(documentPromise.toJSON) === "function"){
					results = documentPromise.toJSON();
				}
				results.forEach((d)=> {
					var processName = d.name;
					if(!processes[processName]){
						 processes[processName] = [];
					}
					processes[processName].push(d);
				});
				for(key in processes){
					$scope.processes[key] = new NgTableParams({count: 25}, {});
					$scope.processes[key].settings({dataset: processes[key]});
				}
			//$scope.documentTable.settings({dataset: documentPromise});
			//	$scope.updateSummaries(documentPromise);
			});
			return documentPromise;
		}*/
	//Ricerca documenti a partire dalla form di ricerca
	/*$scope.search = function() {
		var documentPromise = ProcessResource.query(null, function() {	
			var processes = {};
			let results = documentPromise;
			if(typeof(documentPromise.toJSON) === "function"){
				results = documentPromise.toJSON();
			}
			results.forEach((d)=> {
				var processName = d.name;
				if(!processes[processName]){
					 processes[processName] = [];
				}
				processes[processName].push(d);
			});
			for(key in processes){
				$scope.processes[key] = new NgTableParams({count: 25}, {});
				$scope.processes[key].settings({dataset: processes[key]});
			}
		//$scope.documentTable.settings({dataset: documentPromise});
		//	$scope.updateSummaries(documentPromise);
		});
		return documentPromise;
	}*/
}])