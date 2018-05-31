/**
 * Task module
 * @author Alba Quarto
 */
angular.module('task', ['workflow', 'ngResource', 'ui.bootstrap', 'ngTable', 'angular.filter'])

	.constant('TASK_DEFAULT_WHITE_LIST', [
		"bpm_comment",
		"bpm_status"
	])

	.factory('TaskResource', ['$resource', function ($resource) {
		return function (customHeaders) {
			return $resource('a/Task/:id', { id: '@id' }, {
				getFormModel: {
					isArray: true,
					url: 'a/Task/:id/formModel',
					method: 'GET'
				},
				getVariables: {
					isArray: true,
					url: 'a/Task/:id/variables',
					method: 'GET'
				},
				getItems: {
					isArray: true,
					url: 'a/Task/:id/items',
					method: 'GET'
				},
				updateTask: {
					url: 'a/Task/:id',
					method: 'PUT',
					headers: customHeaders
				},
				//ToDO spostare in documentresource
				getDocumentPreview: {
					url: 'a/Document/:id/preview',
					method: 'GET',
					responseType: 'arraybuffer',
					cache: false,
					transformResponse: function (data, header) {
						return {
							response: new Blob([data], { type: header("Content-Type") })
						};
					}
				}

			});
		}
	}])

	.controller('TaskController', ['$scope', 'TaskResource', 'NgTableParams', 'jbMessages', 'jbWorkflowUtil', 'jbUtil', 'jbValidate', 'workflowFormBlacklist', 'OUTCOME_PROPERTY_NAME', 'taskState', 'TASK_DEFAULT_WHITE_LIST', '$cookies',
		function ($scope, TaskResource, NgTableParams, jbMessages, jbWorkflowUtil, jbUtil, jbValidate, workflowFormBlacklist, OUTCOME_PROPERTY_NAME, taskState, TASK_DEFAULT_WHITE_LIST, $cookies) {
			$scope.testnestedcontroller = "task";
			//Utilities
			$scope.jbMessages = jbMessages;
			$scope.jbWorkflowUtil = jbWorkflowUtil;
			$scope.jbUtil = jbUtil;
			$scope.jbValidate = jbValidate;

			$scope.editing = false;
			$scope.readOnly = false;

			$scope.breadCrumbIndex = -1;
			$scope.breadcrumbs = [];
			$scope.orderCriteria = "";
	
			$scope.deadlineProximity = function (date) {
				if (!date) {
					return 3;
				}
				var parsedDate = jbUtil.stringToDate(date);
				if (!parsedDate) {
					return 3;
				}
				var now = new Date();
				var parsedDateSum = parsedDate.getTime();
				var nowSum = now.getTime();
				var day = 86400000;

				var dlProx = 3;
				if (nowSum > (parsedDateSum - day)) {
					dlProx = 2;
					if (nowSum > (parsedDateSum)) {
						dlProx = 1;
					}
				}
				return dlProx;
			}

			$scope.getValidClass = function (el) {
				if (el) {
					return jbValidate.getClass(el);
				}
			}

			$scope.gotoTaskBreadcrumb = function (i, form) {
				if (i < 0) {
					$scope.breadcrumbs = [];
					$scope.closeDetail(form);
					return;
				}
				var shortDocument = $scope.breadcrumbs[i];
				$scope.breadCrumbIndex = i - 1;
				if (i == 0) {
					$scope.startEdit(shortDocument.rownum);
				} else {
					$scope.breadcrumbs = $scope.breadcrumbs.slice(0, i);
				}
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

			//FORM TASK
			$scope.currentRowNum = -1;
			$scope.taskEditing = {};
			$scope.outcomeButtons = [];
			$scope.updatedVariables = {}
			$scope.currentTaskForm = [];
			$scope.currentTaskItems = [];
			SEPARATOR = ",";
			$scope.BPM_COMMENT = "bpm_comment";
			$scope.BPM_STATUS = "bpm_status";

			$scope.startEdit = function (group_i, row_i) {
				console.log("Editing: " + group_i + " " + row_i, $scope.taskTable);
				$scope.currentRowNum = row_i;
				var currentGroupNum = group_i;
				$scope.breadCrumbIndex = 0;
				$scope.taskEditing = {};
				$scope.currentTaskForm = [];
				$scope.breadcrumbs = [];
				$scope.currentTaskItems = [];
				$scope.updatedVariables = {};
				$scope.taskEditing.id = $scope.taskTable.data[currentGroupNum].data[$scope.currentRowNum].id;
				var taskPromise = TaskResource().get($scope.taskEditing, function () {
					$scope.taskEditing = taskPromise;
					$scope.breadcrumbs.push({
						id: $scope.taskEditing.id,
						name: $scope.taskEditing.name,
						description: $scope.taskEditing.description,
						businessKey: $scope.taskEditing.processBusinessKey,
						rownum: row_i,
						groupnum: group_i
					});
					var formPromise = TaskResource().getFormModel({ id: $scope.taskEditing.id }, function () {
						console.log("-----formPromise", formPromise);
						var variablesPromise = TaskResource().getVariables({ id: $scope.taskEditing.id }, function () {
							console.log("-----varPromise", variablesPromise);
							buildTaskForm(formPromise, variablesPromise);
							$scope.editing = true;
						});
						var itemsPromise = TaskResource().getItems({ id: $scope.taskEditing.id }, function () {
							console.log("-----itemsPromise", itemsPromise);
							$scope.currentTaskItems = itemsPromise;
							if ($scope.currentTaskItems.length > 0) {
								$scope.getDocumentObjectHTML($scope.currentTaskItems);
							}
						})
					});
				});
			}

			currentTaskVariables = {};

			buildTaskForm = function (formModel, variables) {
				//create map of current task variables
				currentTaskVariables = {};
				for (var i = 0; i < variables.length; i++) {
					var variable = variables[i];
					var variableName = variable.name;
					currentTaskVariables[variableName] = variable;
				}
				outcomeButton = "";
				if (currentTaskVariables[OUTCOME_PROPERTY_NAME]) {
					outcomeButton = jbUtil.sanitize(currentTaskVariables[OUTCOME_PROPERTY_NAME].value);
				}
				//create the form and the relative variable map
				$scope.updatedVariables = {};
				$scope.currentTaskForm = [];
				for (var i = 0; i < formModel.length; i++) {
					var model = formModel[i];
					var modelName = model.name;
					if (workflowFormBlacklist.includes(modelName) && !TASK_DEFAULT_WHITE_LIST.includes(modelName)) {
						continue;
					}
					var variable = currentTaskVariables[modelName];
					if (!variable) {
						currentTaskVariables[modelName] = jbWorkflowUtil.getVoidVariable(modelName, model.dataType);
						variable = currentTaskVariables[modelName];
					}
					$scope.updatedVariables[modelName] = variable;
					if (modelName === outcomeButton) {
						button = buildOutcomButton(model, currentTaskVariables[modelName]);
						if (button) {
							continue;
						}
					}
					$scope.currentTaskForm.push(model)
				}
			}

			buildOutcomButton = function (outcomeButton, variable) {
				value = outcomeButton.allowedValues;
				if (!outcomeButton || jbUtil.isEmptyObject(value)) {
					return false;
				}
				$scope.outcomeButtons = [];
				$scope.outcomeButtons.$$variable = variable;
				for (var i = 0; i < value.length; i++) {
					$scope.outcomeButtons.push(value[i]);
				}
				return true;
			}

			$scope.completeTask = function (jbTaskForm, outcome, outcomeValue) {
				if (outcome) {
					$scope.outcomeButtons.$$variable.value = outcomeValue;
				}
				var state = jbWorkflowUtil.taskFieldName("STATE");
				$scope.taskEditing[state] = taskState.COMPLETED;
				var updated = [state];
				$scope.saveTask(jbTaskForm, updated);
			}

			$scope.saveTask = function (jbTaskForm, updated) {
				variables = [];
				for (variable in $scope.updatedVariables) {
					console.log("----completeTask", variable)
					variables.push($scope.updatedVariables[variable]);
				}
				updated = updated || [];
				updated.push(jbWorkflowUtil.taskFieldName("VARIABLES"));
				$scope.taskEditing[jbWorkflowUtil.taskFieldName("VARIABLES")] = variables;
				updatePromise = TaskResource({ updated: updated }).updateTask($scope.taskEditing, function () {

					$scope.initList($scope.closeDetail);
				});
			}

			//Chiude la pagina di dettaglio
			$scope.closeDetail = function (form) {
				if (form) $scope.clearDetail(form);
				$scope.editing = false;
				$scope.readOnly = false;

			}

			//Pulisce il form della pagina di dettaglio
			$scope.clearDetail = function (form) {
				jbValidate.clearForm(form);
				$scope.taskEditing = {};
			}

			$scope.documenthtml = {};
			//Gestione anteprima nel dettaglio
			$scope.getDocumentObjectHTML = function (items) {
				for (var i = 0; i < items.length; i++) {
					$scope.documenthtml[items[i].id] = "<object data=\"" + "a/Document/" + items[i].id + "/preview" + "\" width=\"100%\" style=\"height: 100vh;\" ></object>";
				}
			}

			//LIST
			$scope.isGroupHeaderRowVisible = false;
			$scope.taskTable = new NgTableParams({ group: "processName" }, {
				counts: [], groupOptions: {
					isExpanded: false
				}
			});

			$scope.initList = function (resolveFunction) {
				var taskPromise = TaskResource().query({}, function () {
					console.log(taskPromise)
					for (var i = 0; i < taskPromise.length; i++) {
						item = taskPromise[i];
						item.deadlineProximity = $scope.deadlineProximity(item[jbWorkflowUtil.taskFieldName("DUE_AT")]);
					}
					$scope.taskTable.settings({ dataset: taskPromise });
					sortingtaskTable(jbWorkflowUtil.taskFieldName("PROCESS_NAME"), ASC, jbWorkflowUtil.taskFieldName("DUE_AT"), ASC);
					if (resolveFunction && typeof resolveFunction === "function") {
						resolveFunction();
					}
				});
			}

			$scope.sortingSelectTitle = "";
			ASC = 'asc';
			$scope.sortingSelectData = [{
				title: jbMessages.task.processType,
				value: jbWorkflowUtil.taskFieldName("PROCESS_NAME"),
				items: [{
					title: jbMessages.task.assignment,
					value: jbWorkflowUtil.taskFieldName("STARTED_AT"),
				}, {
					title: jbMessages.task.deadline,
					value: jbWorkflowUtil.taskFieldName("DUE_AT"),
				}, {
					title: jbMessages.task.priority,
					value: jbWorkflowUtil.taskFieldName("PRIORITY"),
				}]
			}, {
				title: jbMessages.task.priority,
				value: jbWorkflowUtil.taskFieldName("PRIORITY"),
				items: [{
					title: jbMessages.task.processType,
					value: jbWorkflowUtil.taskFieldName("PROCESS_NAME"),
				}, {
					title: jbMessages.task.assignment,
					value: jbWorkflowUtil.taskFieldName("STARTED_AT"),
				}, {
					title: jbMessages.task.deadline,
					value: jbWorkflowUtil.taskFieldName("DUE_AT")
				}]
			}, {
				title: jbMessages.task.deadlineProssimity,
				value: jbWorkflowUtil.taskFieldName("DEADLINE_PROX"),
				items: [{
					title: jbMessages.task.processType,
					value: jbWorkflowUtil.taskFieldName("PROCESS_NAME"),
				}, {
					title: jbMessages.task.assignment,
					value: jbWorkflowUtil.taskFieldName("STARTED_AT"),
				}]
			}];

			$scope.groupType = {};
			$scope.sortingTaskList = function (group, item) {
				$scope.sortingSelectTitle = group.title + " - " + item.title;
				$scope.groupType = {};
				if (group.value === jbWorkflowUtil.taskFieldName("PRIORITY")) {
					$scope.groupType['PRIORITY'] = true;
				} else if (group.value === jbWorkflowUtil.taskFieldName("DEADLINE_PROX")) {
					$scope.groupType['DEADLINE_PROX'] = true;
				}
				$scope.groupType[group.value] = true;
				sortingtaskTable(group.value, ASC, item.value, ASC);
			}

			sortingtaskTable = function (group, gDirection, item, iDirection) {
				$scope.taskTable.group(group, gDirection);
				$scope.taskTable.sorting(item, iDirection);
			}
			//END LIST

			//MESSAGES | STATIC STRING
			$scope.taskPriority = {
				"1": jbMessages.priorityHigh,
				"2": jbMessages.priorityMedium,
				"3": jbMessages.priorityLow
			}
			$scope.deadlineProxVal = {
				"1": jbMessages.expired,
				"2": jbMessages.maturing,
				"3": jbMessages.other
			}


			$scope.deadlineTMessag = {
				"1": jbMessages.task.expired,
				"2": jbMessages.task.expiring
			}

			$scope.assignedTMessage = function (task) {
				var msg = jbMessages.task.assignedUser;
				if (task.candidates && task.candidates.length > 0) {
					msg = jbMessages.task.assignedUsers;
					if (!task.assignee) {
						msg += ". " + jbMessages.task.unassigned;
					}
				}
				return msg;
			}
			//END TOOLTIP MESSAGES
		}])