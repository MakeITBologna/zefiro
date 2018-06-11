<%-- @author Alba Quarto --%>
<%@ include file="/include/directive.jsp" %>
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

    <div>
      <div ng-init="initList()" class="panel jb-header-bar">

		<uib-tabset active="active"  justified="true" >
          <uib-tab  index="0" heading="<fmt:message key="jsp.active.label" />" select="jbctrl.table = jbctrl.processTable">
            <div ng-if="jbctrl.table === jbctrl.processTable" ng-include="'views/process/processListBody.jsp'" ></div>
          </uib-tab>
          <uib-tab  index="1" heading="<fmt:message key="jsp.completed.label" />" select="jbctrl.table = jbctrl.processCompletedTable"> 
            <div ng-show="jbctrl.table === jbctrl.processCompletedTable" ng-include="'views/process/processListBody.jsp'" ></div>
          </uib-tab>
        </uib-tabset>
       
      </div>
    </div>

  </div>