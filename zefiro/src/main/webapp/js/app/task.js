angular.module('task', ['ngResource', 'ui.bootstrap', 'ngTable', 'angular.filter'])

.factory('TaskResource', ['$resource', '$log', function($resource, $log) {
	$log.log("ProcessResource", $resource);
	return $resource('a/Process/processes/:id', {id:'@id'},
			{
				startedProcesses: {
					url:'a/Process/startedProcesses',
					method: 'GET'
				}
			});
}])

.controller('TaskController', ['$scope', 'TaskResource', 'NgTableParams', '$log',
	function($scope, ProcessResource,  NgTableParams, $log) {

	$scope.taskes = {};
	$scope.documentTable = new NgTableParams({count: 25, group: "processName"}, {});
	
	//Ricerca documenti a partire dalla form di ricerca
	$scope.search = function() {
		var documentPromise = ProcessResource.query($scope.documentTemplate, function() {
			$log.log(documentPromise)
			$scope.documentTable.settings({dataset: documentPromise});
		});
		return documentPromise;
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