/**
 * Task module
 * @author Alba Quarto
 */
angular.module('task', ['ngResource', 'ui.bootstrap', 'ngTable', 'angular.filter'])

	.factory('TaskResource', ['$resource', function ($resource) {
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
	}])

	.controller('TaskController', ['$scope', 'TaskResource', 'NgTableParams', 'jbMessages', 'jbWorkflowUtil', 'jbUtil', 'jbValidate',
		function ($scope, TaskResource, NgTableParams, jbMessages, jbWorkflowUtil, jbUtil, jbValidate) {

			//Utilities
			$scope.jbMessages = jbMessages;
			$scope.jbWorkflowUtil = jbWorkflowUtil;
			$scope.jbUtil = jbUtil;
			$scope.jbValidate = jbValidate;

			$scope.editing = false;
			$scope.readOnly = false;

			$scope.currentRowNum = -1;
			$scope.currentGroupNum = -1;
			$scope.taskEditing = {};
			$scope.breadCrumbIndex = -1;
			$scope.breadcrumbs = [];
			$scope.currentTaskForm = [];
			$scope.currentTaskItems = [];
			$scope.updatedVariables = {}
			$scope.orderCriteria = "";

			$scope.showDocument = false;

			$scope.tabhtml = "";

			$scope.startEdit = function (group_i, row_i) {
				console.log("Editing: " + group_i + " " + row_i, $scope.taskTable);
				$scope.currentRowNum = row_i;
				$scope.currentGroupNum = group_i;
				$scope.breadCrumbIndex = 0;
				$scope.taskEditing = {};
				$scope.currentTaskForm = [];
				$scope.breadcrumbs = [];
				$scope.currentTaskItems = [];
				$scope.updatedVariables = {};
				$scope.showDocument = false;
				$scope.taskEditing.id = $scope.taskTable.data[$scope.currentGroupNum].data[$scope.currentRowNum].id;
				var taskPromise = TaskResource.get($scope.taskEditing, function () {
					$scope.taskEditing = taskPromise;
					$scope.breadcrumbs.push({
						id: $scope.taskEditing.id,
						name: $scope.taskEditing.name,
						description: $scope.taskEditing.description,
						businessKey: $scope.taskEditing.processBusinessKey,
						rownum: row_i,
						groupnum: group_i
					});
					var formPromise = TaskResource.getFormModel({ id: $scope.taskEditing.id }, function () {
						console.log("-----formPromise", formPromise);
						$scope.currentTaskForm = formPromise;
						var itemsPromise = TaskResource.getItems({ id: $scope.taskEditing.id }, function () {
							console.log("-----itemsPromise", itemsPromise);
							$scope.currentTaskItems = itemsPromise;
							if ($scope.currentTaskItems.length > 0) {
								$scope.getDocumentObjectHTML($scope.currentTaskItems);
								$scope.showDocument = true;
							}
						})
						var variablesPromise = TaskResource.getVariables({ id: $scope.taskEditing.id }, function () {
							console.log("-----varPromise", variablesPromise);
							variablesPromise.forEach(function (variable) {
								$scope.updatedVariables[variable.name] = variable;
							});
							$scope.editing = true;
						});
					});
				});
			}

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
					$scope.showDocument(shortDocument);
				}
			}

			//Chiude la pagina di dettaglio
			$scope.closeDetail = function (form) {
				$scope.editing = false;
				$scope.readOnly = false;
				if (form) $scope.clearDetail(form);
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

			$scope.initList = function () {
				var taskPromise = TaskResource.query($scope.taskEditing, function () {
					console.log(taskPromise)
					for(var i = 0; i<taskPromise.length; i++){
						item = taskPromise[i];
						item.deadlineProximity = $scope.deadlineProximity(item[jbWorkflowUtil.taskFieldName("DUE_AT")]);
					}
					$scope.taskTable.settings({ dataset: taskPromise });
					sortingtaskTable(jbWorkflowUtil.taskFieldName("PROCESS_NAME"), ASC, jbWorkflowUtil.taskFieldName("DUE_AT"), ASC);
				});
				return taskPromise;
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
				if(group.value === jbWorkflowUtil.taskFieldName("PRIORITY")){
					$scope.groupType['PRIORITY'] = true;
				} else if (group.value === jbWorkflowUtil.taskFieldName("DEADLINE_PROX")){
					$scope.groupType['DEADLINE_PROX'] = true;
				}
				$scope.groupType[group.value] = true;
				sortingtaskTable(group.value,ASC, item.value, ASC);
			}
			
			sortingtaskTable = function(group, gDirection, item, iDirection){
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