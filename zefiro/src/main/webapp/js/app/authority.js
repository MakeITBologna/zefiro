angular.module('authority', ['ngResource', 'ui.bootstrap', 'ngTable', 'documentType', 'angular.filter'])


.controller('AuthorityController', ['$scope', '$uibModalInstance', 'title', 'authArray', function($scope, $uibModalInstance, title, authArray){
	modalInstance = $uibModalInstance;
	
	$scope.back = function(element){
		modalInstance.dismiss()
		//$scope.$emit('AuthorityBack', element);
	}
	
	$scope.title=title;

}])
