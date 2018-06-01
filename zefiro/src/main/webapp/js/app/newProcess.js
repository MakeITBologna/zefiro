angular.module('newProcess', [])

	.constant('NEW_PROCESS_DEFAULT_WHITELIST', [
		"bpm_workflowDescription",
		"bpm_workflowDueDate",
		"bpm_workflowPriority",
		"bpm_sendEMailNotifications",
		"bpm_percentComplete",
		"bpm_comment",
		"bpm_status",
	])

	.controller('NewProcessController', ['$scope', 'ProcessResource', 'workflowAssigneeAspects', 'NEW_PROCESS_DEFAULT_WHITELIST', 'workflowFormBlacklist', 'jbWorkflowUtil', 'jbValidate', '$uibModal', 'AUTHORITY_TYPE', 'jbMessages',
		function ($scope, ProcessResource, workflowAssigneeAspects, NEW_PROCESS_DEFAULT_WHITELIST, workflowFormBlacklist, jbWorkflowUtil, jbValidate, $uibModal, AUTHORITY_TYPE, jbMessages) {

			$scope.jbValidate = jbValidate;

			$scope.back = function (form) {
				$scope.$emit('NewProcessBack', false);
				cleanController(form)
			}


			$scope.$on('AuthorityBack', function (event) {
				if (userModal) {
					userModal.dismiss();
					userModal = undefined;
				}
			})

			$scope.$parent.$on('StartNewProcess', function () {
				$scope.initStartNewProcess();
			})

			cleanController = function (form) {
				if (form) {
					jbValidate.clearForm(form);
				}
				$scope.selectedType = false;
				$scope.choiseSelectTitle = "";
				$scope.choiseSelectData = [];
				$scope.selectedDefinition = {};
				$scope.groupType = {};
				$scope.currentTypeForm = [];
				$scope.addingAssignee = null;
				$scope.addedAssignee = [];
				$scope.updatedVariables = {};

			}

			$scope.selectedType = false;

			$scope.choiseSelectTitle = "";
			$scope.choiseSelectData = [];

			$scope.initStartNewProcess = function () {
				var definitionsPromise = ProcessResource.processDefinitions(function () {
					console.log("------definitionsPromise", definitionsPromise)
					$scope.choiseSelectData = definitionsPromise;
				})
			}

			$scope.groupType = {};
			$scope.choise = function (item) {
				$scope.choiseSelectTitle = item.name ? item.name : item.key;
				$scope.selectedDefinition = item;
				startEditProcess(item);
			}

			$scope.currentTypeForm = [];
			startEditProcess = function (definition) {
				$scope.selectedType = null;
				formPromise = ProcessResource.startForm({ id: definition.id }, function () {
					console.log("-----formPromise", formPromise);
					buildStartForm(formPromise);
				});
			}

			$scope.addingAssignee = null;
			$scope.addedAssignee = [];
			$scope.updatedVariables = {};
			$scope.assigneeType = null;
			$scope.assigneeMany = false;
			buildStartForm = function (formModel, fieldWhiteList) {
				//create the form and the relative variable map
				$scope.updatedVariables = {};
				$scope.currentTypeForm = [];
				$scope.addAssignee = null;
				$scope.addingAssignee =null;
				$scope.addedAssignee = [];
				$scope.assigneeType = null;
				$scope.assigneeMany = false;
				for (var i = 0; i < formModel.length; i++) {
					var model = formModel[i];
					var modelName = model.name;
					assigneeAspect = workflowAssigneeAspects.includes(modelName);
					var whiteList = fieldWhiteList ? fieldWhiteList : NEW_PROCESS_DEFAULT_WHITELIST;
					if (workflowFormBlacklist.includes(modelName) && !assigneeAspect && !whiteList.includes(modelName)) {
						continue;
					}
					$scope.updatedVariables[modelName] = jbWorkflowUtil.getVoidVariable(modelName, model.dataType);
					if (assigneeAspect) {
						if ($scope.addAssignee !== null) {
							//TODO capire come mostrare a video l'errore
							throw new Error("Ambiguità sul tipo di assegnatrio")
						}
						var auth = jbWorkflowUtil.getAssigneeType(model.name);
						$scope.assigneeType = auth.type;
						$scope.assigneeMany = auth.many;
						$scope.addingAssignee = model;
					} else {
						$scope.currentTypeForm.push(model)
					}
				}
				$scope.selectedType = item;
			}

			$scope.selectAssignee = function (parent) {
				angular.element(document).find("script");
				open('lg', parent)
			}

			var userModal;
			open = function (size) {
				var name = $scope.addingAssignee.name;
				var auth = jbWorkflowUtil.getAssigneeType(name);
				var authType = auth.type;
				var authMany = auth.many;
				userModal = $uibModal.open({
					animation: false,
					templateUrl: 'views/process/processSelectAssignee.jsp',
					controller: 'AuthorityController',
					size: size,
					scope: $scope,
					resolve: {
						title: function () { return jbMessages.workflow.select[name] || jbMessages.workflow.select.assignee },
						authType: function () { return $scope.assigneeType },
						authArray: function () { return $scope.addedAssignee },
						authMany: function () { return $scope.assigneeMany }
					}
				});

			}

			$scope.startProcess = function (form) {
				var form = form;
				var process = {};
				process[jbWorkflowUtil.processFieldName('PROCESS_DEFINITION_ID')] = $scope.selectedDefinition.id;
				process[jbWorkflowUtil.processFieldName('PROCESS_DEFINITION_KEY')] = $scope.selectedDefinition.key;
				var assigneeName = $scope.addingAssignee.name;
				if (assigneeName) {
					var auth = jbWorkflowUtil.getAssigneeType(assigneeName);
					var authMany = auth.many;
					if (authMany) {
						$scope.updatedVariables[assigneeName].value = []
						var assigneeValue = $scope.updatedVariables[assigneeName].value
						for (assignee in $scope.addedAssignee) {
							if(auth.type === AUTHORITY_TYPE.PERSON){
								assigneeValue.push($scope.addedAssignee[assignee].id);
							} else if (auth.type === AUTHORITY_TYPE.GROUP){
								assigneeValue.push($scope.addedAssignee[assignee].fullName);
							}
							
						}
					} else {
						if ($scope.addedAssignee.length === 1) {
							if(auth.type === AUTHORITY_TYPE.PERSON){
								$scope.updatedVariables[assigneeName].value = $scope.addedAssignee[0].id;
							} else if (auth.type === AUTHORITY_TYPE.GROUP){
								$scope.updatedVariables[assigneeName].value = $scope.addedAssignee[0].fullName;
							}
							
						}
					}
				}

				process.variables = {};
				for (variable in $scope.updatedVariables) {
					var updVar = $scope.updatedVariables[variable];
					if (updVar.value) {
						process.variables[updVar.name] = updVar.value;
					}
				}
				startProcessProcmise = ProcessResource.startProcess(process, function (started) {
					console.log("-------started Process", started);
					$scope.$emit('StartedNewProcess', false);
					$scope.back(form)
				})

			}

		}])
