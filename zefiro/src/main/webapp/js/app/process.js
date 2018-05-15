angular.module('process', ['ngResource', 'ui.bootstrap', 'ngTable', 'angular.filter'])

.factory('ProcessResource', ['$resource', '$log', function($resource, $log) {
	$log.log("ProcessResource", $resource);
	return $resource('a/Process/processes:id', {id:'@id'},
			{
				startedProcesses: {
					url:'a/Process/startedProcesses',
					method: 'GET',
					isArray: true
				}
			});
}])

.controller('ProcessController', ['$scope', 'ProcessResource', 'NgTableParams', '$log',
	function($scope, ProcessResource,  NgTableParams, $log) {

	$scope.processes = {};
	$scope.documentTable = new NgTableParams({group: "name"},{counts: [],groupOptions: {
        isExpanded: false
    }});
	$scope.isGroupHeaderRowVisible = false;
	
	//Ricerca documenti a partire dalla form di ricerca
	$scope.search = function() {
		var documentPromise = ProcessResource.startedProcesses($scope.documentTemplate, function() {
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
