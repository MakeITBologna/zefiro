<%@ include file="/include/directive.jsp" %>
<div ng-init="search()" hidden></div>
<div class="page-header jb-header-bar">
  <div class="pull-right">
    <button ng-hide="relation" type="button" class="btn btn-success" title="<fmt:message key=" jsp.results.insert.user "/>" ng-click="search()">
      <i class="fa fa-plus"></i>
      <fmt:message key="jsp.process.new" />
    </button>
    <button ng-show="relation" type="button" class="btn btn-danger" title="<fmt:message key=" js.dialog.annulla "/>" ng-click="closeAddRelation()">
      <i class="fa fa-times"></i>
      <fmt:message key="js.dialog.annulla" />
    </button>
  </div>
  <h1>
    <fmt:message key="jsp.task.label" />
  </h1>
</div>
<div>
  <div class="panel jb-header-bar">
    <div class="panel-body">

      <!--<div class="row jb-toolbar">
   <div class="col-sm-12">
				<h4>{{processGroup}}</h4>
			</div> -->
    </div>
    <table ng-table="taskTable" class="table table-condensed ng-table-responsive workflow-list" show-filter="false" show-group="isGroupHeaderRowVisible">
      <tbody>
        <tr class="ng-table-group group-header" ng-repeat-start="group in $groups">
          <td colspan="3">
            <h4>
              <a ng-click="group.$hideRows = !group.$hideRows">
                <span class="glyphicon jb-clickable" ng-class="{ 'glyphicon-chevron-right': group.$hideRows, 'glyphicon-chevron-down': !group.$hideRows }"></span>
                <span>{{ group.value }}</span>
                <span>({{group.data.length}})</span>
              </a>
            </h4>
          </td>
        </tr>
        <tr ng-hide="group.$hideRows" ng-repeat="row in group.data track by $index" ng-dblclick="startEdit($index)" ng-repeat-end>
          <td class="">
            <h5 class="text-center" ng-class="(deadlineProximity(row.dueAt) <=2)  ? 'jb-text-danger' : 'jb-text-success'">
          	  <i class="fa fa-circle" uib-tooltip="{{deadlineTMessag(row.dueAt)}}" tooltip-placement="right-top" tooltip-class="jb-tooltip"></i>
          	</h5>
          	<h5 class="text-center jb-text-primary">
          	  <i ng-class="(!row.candidates || row.candidates.length==0)? 'fa fa-user' : 'fa fa-users'" uib-tooltip="{{assignedTMessage(row.candidates)}}" tooltip-placement="right-top"  tooltip-class="jb-tooltip"></i>
          	</h5>
          </td>
          <td>
            <h5  class="jb-clickable">
              <a ng-click="startEdit($index)">
                <span ng-if="row.processBusinessKey">{{row.processBusinessKey}}</span>
                <span ng-if="!row.processBusinessKey">(Nessun Messaggio)</span>
              </a>
            </h5>
            <h6 ng-if="row.description">
              <span>
                <strong><fmt:message key="jsp.description" />: </strong>
              </span>
              <span>{{row.description}}</span>
            </h6>
            <h6 ng-if="row.dueAt">
              <span>
                <strong><fmt:message key="jsp.process.process.dueAt" />: </strong>
              </span>
              <span>{{row.dueAt | date: '${localePatternTimestamp}'}}</span>
            </h6>
   <!-- <h6 ng-if="row.startedAt">
              <span>
                <strong><fmt:message key="jsp.process.process.startedAt" />: </strong>
              </span>
              <span>{{row.startedAt | date: '${localePatternTimestamp}'}}</span>
            </h6> -->         
             <h6>
              <span>
                <strong><fmt:message key="jsp.startedFrom" />: </strong>
              </span>
              <span>{{row.startUserFirstName}}</span>
              <span> </span>
              <span ng-if="row.startUserLastName">{{row.startUserLastName}}</span>
              <span>
                <strong><fmt:message key="jsp.at"/>: </strong>
              </span>
              <span>{{row.startedAt | date: '${localePatternTimestamp}'}}</span>
            </h6>
            <h6 ng-if="row.endedAt">
              <span>
                <strong><fmt:message key="jsp.process.process.endedAt" />: </strong>
              </span>
              <span>{{row.endedAt | date: '${localePatternTimestamp}'}}</span>
            </h6>
            <h6 ng-if="row.priority">
              <span>
                <strong><fmt:message key="jsp.priority" />: </strong>
              </span>
              <span class="label text-uppercase" ng-class="{'label-success': row.priority==3, 'label-warning': row.priority==2, 'label-danger': row.priority==1}">{{decodePriority(row.priority)}}</span>
            </h6>
           <h6 ng-if="!row.assignee">
              <span class="label label-default text-uppercase">
                <fmt:message key="jsp.unassigned" />
              </span>
            </h6>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</div>