<%-- @author Alba Quarto --%>
<%@ include file="/include/directive.jsp"%>
<div class="container">
	<div class="page-header jb-header-bar">
		<div class="pull-right">
			<button ng-hide="relation" type="button" class="btn btn-success" title="<fmt:message key=" jsp.process.new "/>" ng-click="startProcess()">
				<i class="fa fa-plus"></i>
				<fmt:message key="jsp.process.new" />
			</button>
		</div>
		<h1>
			<fmt:message key="jsp.myProcesses.label" />
		</h1>
	</div>




	<div class="jb-header-bar">

		<div class="panel-body jb-panel-body">

			<form name="jbSearchWorkflowInstance" class="form-horizontal well" novalidate jb-print="a/Document/print">
				<div class="row jb-form-group">

					

					<label class="col-sm-1 control-label" for="jbSearchWorkflowInstance" title="<fmt:message key="jsp.process.date.title"/>"><fmt:message key="jsp.process.date.label" /></label>
					<div class="col-sm-2">
						<div class="input-group">
							<span class="input-group-addon"><fmt:message key="jsp.from.label" /></span> <input id="jbSearchWorkflowInstance-completed-from" class="form-control" title="<fmt:message key="jsp.process.completed.title"/> <fmt:message key="jsp.from.label"/>" type="text" name="completed-from" ng-model="processFilter['completed-from']" uib-datepicker-popup="${localePatternDate}" is-open="calendarPopups['completed-from']" /> <span class="input-group-btn">
								<button type="button" class="btn btn-default" ng-click="openCalendar('completed-from')">
									<i class="fa fa-calendar"></i>
								</button>
							</span>
						</div>
					</div>

					<div class="col-sm-2">
						<div class="input-group">
							<span class="input-group-addon"><fmt:message key="jsp.to.label" /></span> <input id="jbSearchFormDocument-completed-TO" class="form-control" title="<fmt:message key="jsp.document.completed.title"/> <fmt:message key="jsp.to.label"/>" type="text" name="cmis:creationDate|LE" ng-model="processFilter['completed-to']" uib-datepicker-popup="${localePatternDate}" is-open="calendarPopups['completed-to']" /> <span class="input-group-btn">
								<button type="button" class="btn btn-default" ng-click="openCalendar('completed-to')">
									<i class="fa fa-calendar"></i>
								</button>
							</span>
						</div>
					</div>

					<div class="col-sm-1"></div>

					<div class="row jb-form-group" align="center">
						<button type="submit" class="btn btn-primary" ng-click="jbValidate.checkForm(jbSearchWorkflowInstance) && search()" promise-btn>
							<i class="fa fa-search"></i>
							<fmt:message key="jsp.query.submit" />
						</button>
						<button type="button" class="btn btn-default" ng-click="clearSearch(jbSearchWorkflowInstance)">
							<i class="fa fa-eraser"></i>
							<fmt:message key="jsp.query.reset" />
						</button>
					</div>
				</div>
			</form>

		</div>


		<div ng-init="initList()" class="panel ">

			<uib-tabset active="active" justified="true"> <uib-tab index="0" heading="<fmt:message key="jsp.active.label" />" select="jbctrl.table = jbctrl.processTable">
			<div ng-if="jbctrl.table === jbctrl.processTable" ng-include="'views/process/processListBody.jsp'"></div>
			</uib-tab> <uib-tab index="1" heading="<fmt:message key="jsp.completed.label" />" select="jbctrl.table = jbctrl.processCompletedTable">
			<div ng-show="jbctrl.table === jbctrl.processCompletedTable" ng-include="'views/process/processListBody.jsp'"></div>
			</uib-tab> </uib-tabset>

		</div>
	</div>

</div>