angular.module('process', ['ngResource', 'ui.bootstrap', 'ngTable', 'angular.filter'])


	.constant('NEW_PROCESS_DEFAULT_WHITELIST', [
		"bpm_workflowDescription",
		"bpm_workflowDueDate",
		"bpm_workflowPriority",
//		"bpm_sendEMailNotifications",
//		"bpm_percentComplete",
		"bpm_comment",
//		"bpm_status",
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
				}, addItems: {
					url: 'a/Process/processes/:id/items',
					method: 'POST',
					isArray: true
				}, getWorkflowInstances: {
					url: 'a/Process/workflowInstances',
					method: 'GET',
					isArray: true
				},  getWorkflowInstance: {
					url: 'a/Process/workflowInstances/:id',
					method: 'GET',
				}, getcompletedWorkflow: {
				 	url: 'a/Process/completedWorkflow/:id',
					method: 'GET',
					isArray: true
				}, getWorkflowDefinitions: {
					url: 'a/Process/workflowDefinitions',
					method: 'GET',
					isArray: true
				}
			});
	}])

	.controller('ProcessController', ['$scope', 'ProcessResource', 'NgTableParams', 'jbMessages', 'jbUtil', 'jbWorkflowUtil','WORKFLOW_DEFINITION_PROPERTIES', 'WORKFLOW_INSTANCE_PROPERTIES', 'deadlineTMessage',
		function ($scope, ProcessResource, NgTableParams, jbMessages, jbUtil, jbWorkflowUtil, WORKFLOW_DEFINITION_PROPERTIES, WORKFLOW_INSTANCE_PROPERTIES, deadlineTMessage) {

			$scope.jbctrl = $scope;
			$scope.jbMessages=jbMessages;
			$scope.WORKFLOW_INSTANCE_PROPERTIES = WORKFLOW_INSTANCE_PROPERTIES;
			$scope.WORKFLOW_DEFINITION_PROPERTIES = WORKFLOW_DEFINITION_PROPERTIES;
			$scope.jbWorkflowUtil = jbWorkflowUtil;
			
			$scope.processes = {};
			$scope.processTable = new NgTableParams();
			$scope.isGroupHeaderRowVisible = false;
			$scope.definitionsMap = {};
			$scope.processCompletedTable = new NgTableParams();
			$scope.table = $scope.processTable;
			//Ricerca documenti a partire dalla form di ricerca
			$scope.initList = function () {
				var definitionsPromise = ProcessResource.getWorkflowDefinitions({}, function(){
					console.log("----definitionsPromise", definitionsPromise);
										var processesPromise = ProcessResource.getWorkflowInstances({}, function(){
						console.log("----processPromise", processesPromise);
						var completedWorkflowPromise = ProcessResource.getcompletedWorkflow({}, function(){
							cleanListData();
							for(var i = 0; i<definitionsPromise.length; i++){
								$scope.definitionsMap[definitionsPromise[i][$scope.WORKFLOW_DEFINITION_PROPERTIES.URL]] = definitionsPromise[i];
							}
							
							console.log("----completedWorkflowPromise", completedWorkflowPromise);
							$scope.processTable.settings({ dataset: processesPromise });
							$scope.processCompletedTable.settings({ dataset: completedWorkflowPromise });
							$scope.table = $scope.processTable;//$scope.listIsInit = true;	
						})
					})
				})
			}
		
			cleanListData = function(){
				$scope.processTable = new NgTableParams({ group: $scope.WORKFLOW_INSTANCE_PROPERTIES.DEFINITION_URL }, {
					counts: [], groupOptions: {
						isExpanded: false
					}
				});
				$scope.processCompletedTable = new NgTableParams({ group: $scope.WORKFLOW_INSTANCE_PROPERTIES.DEFINITION_URL }, {
					counts: [], groupOptions: {
						isExpanded: false
					}
				});
				$scope.table = $scope.processTable;
				$scope.definitionsMap = {};
			}
			
			//EDIT PROCESS
			$scope.editingProcessId =  null;
			$scope.showDetail = false;
			$scope.processDetail = {};
			$scope.processTasksList = new NgTableParams({},{counts: []});
			$scope.viewDetail = function(group_i, row_i, table){
				 cleanDetail();
				 $scope.editingProcessId =  table.data[group_i].data[row_i].id;
				 processPromise = ProcessResource.getWorkflowInstance({id:  $scope.editingProcessId}, function(){
					 console.log("------processPromise data" , processPromise);
					 $scope.processDetail = processPromise;
					 $scope.breadcrumbs.push($scope.processDetail);
					 $scope.processTasksList.settings({ dataset: sortTasks(processPromise.tasks)});
					 
					 $scope.showDetail = true;
				 })
			}
			
			$scope.COMPLETITION_TIMES = "completitionTimes";
			$scope.DEADLINE_PROXIMITY = "deadlineProximity";
			
			sortTasks = function(tasks){
				var tasks = tasks || [];
				for(task in tasks){
					var dueDate = tasks[task].properties.bpm_dueDate;
					var completitionDate = tasks[task].properties.bpm_completionDate ;
					tasks[task][$scope.DEADLINE_PROXIMITY]= jbUtil.deadlineProximity(dueDate);
					if(completitionDate){
						tasks[task][$scope.COMPLETITION_TIMES] = jbUtil.deadlineProximity(dueDate, completitionDate);
					}
				}
				var sortingTask = tasks.sort(function (a, b){
					var a_endDate =a.properties.bpm_completionDate;
					var b_endDate =b.properties.bpm_completionDate;
					
					if(a_endDate && !b_endDate || a_endDate < b_endDate){
						return 1;
					}
					
					if(!a_endDate && b_endDate || a_endDate > b_endDate){
						return -1;
					}
					
					if(a_endDate && b_endDate || a_endDate == b_endDate){
						return 0;
					}
				});
				return sortingTask;
			}
			
			$scope.deadlineProximity = function (date) {
				return jbUtil.deadlineProximity(date);
			}
			
			cleanDetail = function(form){
				$scope.editingProcessId =  null;
				$scope.processDetail = {};
				$scope.processTasksList = new NgTableParams({},{counts: []});
			}
			
			$scope.closeDetail = function(){
				cleanDetail();
				$scope.showDetail = false;
			}
			
			//NEW PROCESS
			$scope.startNewProcess = false;
			$scope.breadcrumbs = [];
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
			
			//BC
			$scope.gotoBreadcrumb = function (i,  func) {
				
				if (i < 0) {
					$scope.breadcrumbs = [];
					if(func){
						func();
					}
					return;
				}
				if(func){
					func();
				}
				var shortDocument = $scope.breadcrumbs[i];
				$scope.breadCrumbIndex = i - 1;
				if (i == 0) {
					$scope.startEdit(shortDocument.rownum);
				} else {
					$scope.breadcrumbs = $scope.breadcrumbs.slice(0, i);
				}
			}
			
			//CONSTANTS
			$scope.deadlineTMessage = {
					"1": jbMessages.task.active + ". " + jbMessages.task.expired,
					"2": jbMessages.task.active ,
					"3": jbMessages.task.active ,
				}
			
			$scope.completitionTMessage = {
					"1": jbMessages.task.completedLate,
					"2": jbMessages.task.completedInTime,
					"3": jbMessages.task.completedInTime
			}
			
			$scope.taskPriority  = {
				"1": jbMessages.priorityHigh,
				"2": jbMessages.priorityMedium,
				"3": jbMessages.priorityLow
			}
			
			$scope.processPriority = $scope.jbWorkflowUtil.getPriorityValues();
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
					console.log("----formPromise",formPromise)
					buildStartForm(formPromise);
				});
			}

			$scope.addingAssignee = null;
			$scope.addedAssignee = [];
			$scope.updatedVariables = {};
			$scope.assigneeType = null;
			$scope.assigneeMany;
			selectedItemsMap = {};
			$scope.selectedItems = [];
			$scope.mandatoryAssignee = false;
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
				$scope.mandatoryAssignee = false;
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
					$scope.updatedVariables[modelName].defaultValue = model.defaultValue;
					if (assigneeAspect) {
						if ($scope.addAssignee !== null) {
							throw new Error("Ambiguità sul tipo di assegnatrio")
						}
						var auth = jbWorkflowUtil.getAssigneeType(model.name);
						$scope.assigneeType = auth.type;
						$scope.assigneeMany = auth.many;
						$scope.addingAssignee = model;
					} else {
						if(model.name === "bpm_workflowPriority"){
							model.shownValues = $scope.jbWorkflowUtil.getPriorityValues();
						} else if(model.name === "bpm_status"){
							model.shownValues = $scope.jbWorkflowUtil.getStatusTranslateValues();
						}
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
			
			$scope.showAssigneeError  = false;
			$scope.validateForm = function(form){
				var valid = jbValidate.checkForm(form);
				if(valid){
					if($scope.addingAssignee && $scope.addingAssignee.required && $scope.addedAssignee.length===0){
						valid = false;
						$scope.showAssigneeError=true;
					}
				}
				return valid;
			}

			$scope.startProcess = function (form) {
				if(!$scope.validateForm(form)){
					return;
				}
				var form = form;
				var process = {};
				process[jbWorkflowUtil.processFieldName('PROCESS_DEFINITION_ID')] = $scope.selectedDefinition.id;
				process[jbWorkflowUtil.processFieldName('PROCESS_DEFINITION_KEY')] = $scope.selectedDefinition.key;
				var assigneeName = $scope.addingAssignee ? $scope.addingAssignee.name : null;
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
					if ($scope.selectedItems.length === 0) {
						$scope.$emit('StartedNewProcess', false);
						$scope.back(form);
						return started;
					}
					var items = [];
					items.id = started.id;
					for (item in $scope.selectedItems) {
						var id = $scope.selectedItems[item].id.split(';')[0];
						items.push({ id: id });
					}
					addItemsProcmise = ProcessResource.addItems(items, function (items) {
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
				if (!selectedItemsMap[item.id]) {
					selectedItemsMap[item.id] = item;
					$scope.selectedItems.push(item);
				}
			});

			$scope.$on('DocumentBack', function () {
				$scope.selectDocument = false;
			})

			$scope.addItem = function () {
				$scope.selectDocument = true;
			}

			$scope.removeItem = function (item) {
				delete selectedItemsMap[item.id];
				$scope.selectedItems.splice($scope.selectedItems.indexOf(item), 1);
			}

		}])
