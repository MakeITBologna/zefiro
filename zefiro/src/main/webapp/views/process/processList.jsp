<!-- Process List Page
  -- @author Alba Quarto
  -->
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
        <fmt:message key="jsp.process.myProcesses.label" />
      </h1>
    </div>

    <div>
      <div class="panel jb-header-bar">

        <!-- #####  Process list ##### -->
        <table ng-init="initList()" ng-table="processTable" class="table table-condensed ng-table-responsive workflow-list" show-filter="false"
          show-group="isGroupHeaderRowVisible">
          <tbody>

            <!-- #####  Group header ##### -->
            <tr class="ng-table-group group-header" ng-repeat-start="($g_index, group) in $groups">
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

            <!-- #####  Process Row ##### -->
            <tr ng-hide="group.$hideRows" ng-repeat="($index, row) in group.data track by row.id" ng-dblclick="startEdit($g_index, $index)"
              ng-repeat-end>

              <!-- #####  Icons column ##### -->
              <td class="">
                <h5 class="text-center jb-text-primary">
                  <i class="fa fa-bars"></i>
                </h5>
              </td>

              <!-- #####  Details Column ##### -->
              <td>
                <h5 class="jb-clickable">
                  <a ng-click="startEdit($g_index, $index)">
                    <span ng-if="row.businessKey">{{row.businessKey}}</span>
                    <span ng-else>(Nessun Messaggio)</span>
                  </a>
                </h5>
                <h6>
                  <span>
                    <strong>
                      <fmt:message key="jsp.process.startedAt" />: </strong>
                  </span>
                  <span>{{row.startedAt | date: '${localePatternTimestamp}'}}</span>
                </h6>
                <h6 ng-if="row.endedAt">
                  <span>
                    <strong>
                      <fmt:message key="jsp.process.endedAt" />: </strong>
                  </span>
                  <span>{{row.endedAt | date: '${localePatternTimestamp}'}}</span>
                </h6>
              </td>
            </tr>
          </tbody>
        </table>
        <!-- #####  End Process List ##### -->

      </div>
    </div>

  </div>