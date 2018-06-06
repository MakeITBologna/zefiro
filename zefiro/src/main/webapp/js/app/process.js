angular.module('process', ['ngResource', 'ui.bootstrap', 'ngTable', 'angular.filter'])


	.constant('NEW_PROCESS_DEFAULT_WHITELIST', [
		"bpm_workflowDescription",
		"bpm_workflowDueDate",
		"bpm_workflowPriority",
		"bpm_sendEMailNotifications",
		"bpm_percentComplete",
		"bpm_comment",
		"bpm_status",
	])

	.factory('ProcessResource', ['$resource', function ($resource) {
		return $resource('a/Process/processes/:id', { id: '@id' },
			{
				startedProcesses: {
					url: 'a/Process/startedProcesses',
					method: 'GET',
					isArray: true
				}, processDefinitions: {
					url: 'a/Process/definitions',
					method: 'GET',
					isArray: true
				}, startForm: {
					url: 'a/Process/definitions/:id/startForm',
					method: 'GET',
					isArray: true
				}, startProcess: {
					url: 'a/Process/processes/',
					method: 'POST',
					isArray: false
				}, addItems: {
					url: 'a/Process/processes/:id/items',
					method: 'POST',
					isArray: true
				}
			});
	}])

	.controller('ProcessController', ['$scope', 'ProcessResource', 'NgTableParams', '$log',
		function ($scope, ProcessResource, NgTableParams, $log) {

			$scope.processes = {};
			$scope.processTable = new NgTableParams({ group: "name" }, {
				counts: [], groupOptions: {
					isExpanded: false
				}
			});
			$scope.isGroupHeaderRowVisible = false;

			//Ricerca documenti a partire dalla form di ricerca
			$scope.initList = function () {
				var documentPromise = ProcessResource.startedProcesses($scope.documentTemplate, function () {
					$log.log(documentPromise)
					$scope.processTable.settings({ dataset: documentPromise });
				});
				return documentPromise;
			}
			//NEW PROCESS
			$scope.startNewProcess = false;
			$scope.startProcess = function () {
				$scope.$broadcast('StartNewProcess');
				$scope.startNewProcess = true;
			}

			$scope.$on('StartedNewProcess', function (event) {
				$scope.startNewProcess = false;
				$scope.initList();
			});

			$scope.$on('NewProcessBack', function (event) {
				$scope.startNewProcess = false;
			});


		}])

	.controller('NewProcessController', ['$scope', 'ProcessResource', 'workflowAssigneeAspects', 'NEW_PROCESS_DEFAULT_WHITELIST', 'workflowFormBlacklist', 'jbWorkflowUtil', 'jbValidate', '$uibModal', 'jbUtil', 'AUTHORITY_TYPE', 'jbMessages',
		function ($scope, ProcessResource, workflowAssigneeAspects, NEW_PROCESS_DEFAULT_WHITELIST, workflowFormBlacklist, jbWorkflowUtil, jbValidate, $uibModal, jbUtil, AUTHORITY_TYPE, jbMessages) {
			
			
			$scope.AUTHORITY_TYPE = AUTHORITY_TYPE;
			$scope.jbValidate = jbValidate;
			$scope.jbWorkflowUtil = jbWorkflowUtil;
			$scope.jbUtil = jbUtil;

			$scope.back = function (form) {
				$scope.$emit('NewProcessBack', false);
				cleanController(form)
			}

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
			$scope.assigneeMany;
			selectedItemsMap = {};
			$scope.selectedItems = []
			buildStartForm = function (formModel, fieldWhiteList) {
				//create the form and the relative variable map
				$scope.updatedVariables = {};
				$scope.currentTypeForm = [];
				$scope.addAssignee = null;
				$scope.addingAssignee = null;
				$scope.addedAssignee = [];
				$scope.assigneeType = null;
				$scope.assigneeMany;
				$scope.selectDocument = false;
				selectedItemsMap = {};
				$scope.selectedItems = []
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
				$scope.selectedType = true;
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
							if (auth.type === AUTHORITY_TYPE.PERSON) {
								assigneeValue.push($scope.addedAssignee[assignee].id);
							} else if (auth.type === AUTHORITY_TYPE.GROUP) {
								assigneeValue.push($scope.addedAssignee[assignee].fullName);
							}

						}
					} else {
						if ($scope.addedAssignee.length === 1) {
							if (auth.type === AUTHORITY_TYPE.PERSON) {
								$scope.updatedVariables[assigneeName].value = $scope.addedAssignee[0].id;
							} else if (auth.type === AUTHORITY_TYPE.GROUP) {
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
					if($scope.selectedItems.length===0){
						 $scope.$emit('StartedNewProcess', false);
						 $scope.back(form);
						 return started;
					}
					var items = [];
					items.id = started.id;
					for(item in $scope.selectedItems){
						var id = $scope.selectedItems[item].id.split(';')[0];
						items.push({id: id});
					}
					addItemsProcmise = ProcessResource.addItems(items, function (items){
						 $scope.$emit('StartedNewProcess', false);
						 $scope.back(form);
						 return started;
					});
				});
			}
			
			//Documents
			/**
			 * subscription at Documentontroller  selection event
			 */
			$scope.$on('DocumentSelected', function (event, item) {
				$scope.selectDocument = false;
				if(!selectedItemsMap[item.id]){
					selectedItemsMap[item.id]=item;
					$scope.selectedItems.push(item);
				}
			});
			
			$scope.$on('DocumentBack', function(){
				$scope.selectDocument = false;
			})

			$scope.addItem = function(){
				$scope.selectDocument = true;
			}
			
			$scope.removeItem = function(item){
				delete selectedItemsMap[item.id];
				$scope.selectedItems.splice($scope.selectedItems.indexOf(item), 1);
			}

		}])
