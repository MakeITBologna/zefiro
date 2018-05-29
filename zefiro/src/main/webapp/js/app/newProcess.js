angular.module('newProcess',[])

.constant("defaultWhiteList", [
	"bpm_workflowDescription",
	"bpm_workflowDueDate",
	"bpm_workflowPriority",
	"bpm_sendEMailNotifications",
	"bpm_percentComplete",
	"bpm_comment",
	"bpm_status",
])

.controller('NewProcessController', ['$scope', 'ProcessResource', 'workflowAssigneeAspects', 'defaultWhiteList', 'workflowFormBlacklist', 'jbWorkflowUtil','jbValidate','$uibModal',
	function($scope, ProcessResource,workflowAssigneeAspects,defaultWhiteList, workflowFormBlacklist, jbWorkflowUtil, jbValidate,$uibModal){

	$scope.back = function(form){
		$scope.$emit('NewProcessBack', false);
		cleanController(form)
	}
	
	
	$scope.$on('AuthorityBack', function(event){
		if(userModal){
			userModal.dismiss();
			userModal = undefined;
		}
	})
	
	$scope.$parent.$on('StartNewProcess', function() {
		$scope.initStartNewProcess();
	})
	
	$scope.startProcess = function(form){
		$scope.$emit('StartedNewProcess', false);
		cleanController(form);
	}
	
	cleanController = function(form){
		if(form) {
			jbValidate.clearForm(form);
		}
		$scope.selectedType = false;
		$scope.choiseSelectTitle = "";
		$scope.choiseSelectData =  [];
		$scope.groupType = {};
		$scope.currentTypeForm = [];
		$scope.addingAssignee = null;
		$scope.addedAssignee = [];
		$scope.updatedVariables = {};

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
	
	$scope.addingAssignee = null;
	$scope.addedAssignee = [];
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
			$scope.updatedVariables[modelName] = jbWorkflowUtil.getVoidVariable(modelName, model.dataType);
			if(assigneeAspect){
				if($scope.addAssignee!==null){
					//TODO capire come mostrare a video l'errore
					throw new Error("Ambiguità sul tipo di assegnatrio")
				} 
				$scope.addingAssignee = model;
			} else {
				$scope.currentTypeForm.push(model)
			}
		}
		$scope.selectedType = item;
	}
	
	$scope.selectAssignee = function(parent){
		angular.element(document).find("script");
		open('sm',parent)
	}
	
	var userModal;
	 open = function (size) {
		    userModal = $uibModal.open({
		      animation: true,
		      ariaLabelledBy: 'modal-title',
		      ariaDescribedBy: 'modal-body',
		      templateUrl: 'views/process/selectUser.jsp',
		      controller: 'AuthorityController',
		      size: size,
		      scope: $scope,
		      resolve: {
		    	  title: function () { return "TITOLO" },
		    	  authArray: function () {return $scope.addedAssignee}
		        }
		    });
		   
	 }

}])
