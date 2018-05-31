angular.module('process', ['ngResource', 'ui.bootstrap', 'ngTable', 'angular.filter'])

.factory('ProcessResource', ['$resource', function($resource) {
	return $resource('a/Process/processes/:id', {id:'@id'},
			{
				startedProcesses: {
					url:'a/Process/startedProcesses',
					method: 'GET',
					isArray: true
				}, processDefinitions: {
					url:'a/Process/definitions',
					method: 'GET',
					isArray: true
				}, startForm: {
					url:'a/Process/definitions/:id/startForm',
					method: 'GET',
					isArray: true
				}, startProcess: {
					url:'a/Process/processes/',
					method: 'POST',
					isArray: false
				}
			});
}])

.controller('ProcessController', ['$scope', 'ProcessResource', 'NgTableParams', '$log',
	function($scope, ProcessResource,  NgTableParams, $log) {

	$scope.processes = {};
	$scope.processTable = new NgTableParams({group: "name"},{counts: [],groupOptions: {
        isExpanded: false
    }});
	$scope.isGroupHeaderRowVisible = false;
	
	//Ricerca documenti a partire dalla form di ricerca
	$scope.initList = function() {
		var documentPromise = ProcessResource.startedProcesses($scope.documentTemplate, function() {
			$log.log(documentPromise)
			$scope.processTable.settings({dataset: documentPromise});
		});
		return documentPromise;
	}
	
	//NEW PROCESS
	$scope.startNewProcess=false;
	$scope.startProcess = function(){
		$scope.startNewProcess=true;
	}
	
	$scope.$on('StartedNewProcess', function(event){
		$scope.startNewProcess=false;
	});

}])