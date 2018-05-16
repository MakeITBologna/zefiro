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
    <fmt:message key="jsp.process.label" />
  </h1>
</div>
<div>
  <div class="panel jb-header-bar">
  </div>
   <div class="panel-body">

      <!--<div class="row jb-toolbar">
   <div class="col-sm-12">
				<h4>{{processGroup}}</h4>
			</div> -->
    </div>
    <table ng-table="documentTable" class="table table-condensed ng-table-responsive workflow-list" show-filter="false" show-group="isGroupHeaderRowVisible">
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
            <h5 class="text-center jb-text-primary" >
          	  <i class="fa fa-bars"></i>
          	</h5>
          </td>
          <td>
            <h5  class="jb-clickable">
              <a ng-click="startEdit($index)">
                <span ng-if="row.businessKey">{{row.businessKey}}</span>
                <span ng-if="!row.businessKey">(Nessun Messaggio)</span>
              </a>
            </h5>
            <h6 ng-if="row.startedAt">
              <span>
                <strong><fmt:message key="jsp.process.startedAt" />: </strong>
              </span>
              <span>{{row.startedAt | date: '${localePatternTimestamp}'}}</span>
            </h6>
            <h6 ng-if="row.endedAt">
              <span>
                <strong><fmt:message key="jsp.process.endedAt" />: </strong>
              </span>
              <span>{{row.endedAt | date: '${localePatternTimestamp}'}}</span>
            </h6>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</div>