<!-- Task List Page
  -- @author Alba Quarto
  -->
<%@ include file="/include/directive.jsp" %>
  <div class="container">

    <div class="page-header jb-header-bar">
      <div class="pull-right">
        <button ng-hide="relation" type="button" class="btn btn-success" title="<fmt:message key=" jsp.process.new " />" ng-click="startProcess()">
          <i class="fa fa-plus"></i>
          <fmt:message key="jsp.process.new" />
        </button>
      </div>
      <h1>
        <fmt:message key="jsp.task.myTask.label" />
      </h1>
    </div>

    <div>
      <div class="panel jb-header-bar">

        <!-- ##### Sorting dropdown ##### -->
        <div class="panel-body jb-panel-body">
          <form name="sortingForm" class="form-horizontal" novalidate>

            <div class="row jb-form-group">
              <label class="control-label col-sm-4">
                <strong class="control jb-button-label">
                  <fmt:message key="jsp.orderBy" />
                </strong>
              </label>
              <div class="btn-group col-sm-8 jb-button-dropdown" uib-dropdown is-open="status.isopen" id="sortingSelect" style="display: -webkit-box;">
                <button id="sortingSelect" type="button" class="btn dropdown-toggle btn-default jb-full-area" uib-dropdown-toggle ng-disabled="disabled"
                  aria-expanded="false">
                  <span class="pull-left">{{sortingSelectTitle}}</span>
                  <span class="pull-right">
                    <i class="fa fa-caret-down fa-sm"></i>
                  </span>
                </button>
                <!-- #####  Dropdown ##### -->
                <ul class="dropdown-menu jb-full-area jb-button-dropdown" uib-dropdown-menu role="menu" aria-labelledby="single-button">
                  <li class="dropdown-header jb-dropdown-header" ng-repeat-start="gr in sortingSelectData">
                    <span class="text">{{gr.title}}</span>
                  </li>
                  <li ng-repeat="item in gr.items" data-original-index="0" data-optgroup="1" class="selected jb-dropdown-subitems" ng-repeat-end>
                    <a tabindex="0" class="opt jb-action" data-tokens="null" role="option" aria-disabled="false" aria-selected="true" ng-click="sortingTaskList(gr, item)">
                      <span class="text">{{item.title}}</span>
                    </a>
                  </li>
                </ul>
              </div>
            </div>
          </form>
        </div>
        <!-- #####  End sorting dropdown ##### -->

        <!-- #####  Task list ##### -->
        <table ng-init="initList()" ng-table="taskTable" class="table table-condensed ng-table-responsive workflow-list" show-filter="false"
          show-group="isGroupHeaderRowVisible">
          <tbody>

            <!-- #####  Group header ##### -->
            <tr class="ng-table-group group-header" ng-repeat-start="($g_index, group) in $groups">
              <td colspan="3">
                <h4>
                  <a ng-click="group.$hideRows = !group.$hideRows">
                    <span class="glyphicon jb-clickable" ng-class="{ 'glyphicon-chevron-right': group.$hideRows, 'glyphicon-chevron-down': !group.$hideRows }"></span>
                    <span ng-if="groupType['PRIORITY']">{{ taskPriority[group.value] }}</span>
                    <span ng-else-if="groupType['DEADLINE_PROX']">{{ deadlineProxVal[group.value] }}</span>
                    <span ng-else>{{ group.value }}</span>
                    <span>({{group.data.length}})</span>
                  </a>
                </h4>
              </td>
            </tr>

            <!-- #####  Task Row ##### -->
            <tr ng-hide="group.$hideRows" ng-repeat="($index, row) in group.data track by row.id" ng-dblclick="startEdit($g_index, $index)"
              ng-repeat-end>

              <!-- #####  Icons column ##### -->
              <td class="">
                <h5 class="text-center jb-fa-md" ng-class="(row.deadlineProximity <= 2)  ? 'jb-text-danger' : 'jb-text-success'">
                  <i class="fa fa-clock-o" uib-tooltip="{{deadlineTMessag[row.deadlineProximity]}}" tooltip-placement="right-top" tooltip-class="jb-tooltip"></i>
                </h5>
                <h5 class="text-center jb-fa-md" ng-class="{'jb-text-success': row.priority==3, 'jb-text-warning': row.priority==2, 'jb-text-danger': row.priority==1}">
                  <i class="fa fa-exclamation-circle" uib-tooltip="{{taskPriority[row.priority]}}" tooltip-placement="right-top" tooltip-class="jb-tooltip"></i>
                </h5>
                <h5 class="text-center jb-fa-md" ng-class="(row.assignee) ? 'jb-text-primary' : ''">
                  <i ng-class="(!row.candidates || row.candidates.length==0)? 'fa fa-user' : 'fa fa-users'" uib-tooltip="{{assignedTMessage(row)}}"
                    tooltip-placement="right-top" tooltip-class="jb-tooltip"></i>
                </h5>
              </td>

              <!-- #####  Details Column ##### -->
              <td>
                <h5 class="jb-clickable">
                  <a ng-click="startEdit($g_index, $index)">
                    <span ng-if="row.processBusinessKey">{{row.processBusinessKey}}</span>
                    <span ng-else>(Nessun Messaggio)</span>
                  </a>
                </h5>
                <h6>
                  <span>
                    <span>
                      <strong>
                        <fmt:message key="jsp.task.task" />: </strong>
                    </span>
                    <span>{{row.name}}</span>
                    <span>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;</span>
                    <strong>
                      <fmt:message key="jsp.process.process" />: </strong>
                  </span>
                  <span>{{row.processId}}</span>
                </h6>
                <h6>
                  <span>
                    <strong>
                      <fmt:message key="jsp.assignedAt" />: </strong>
                  </span>
                  <span>{{row.startedAt | date: '${localePatternTimestamp}'}}</span>
                  <span ng-if="row.dueAt">
                    <span>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;</span>
                    <span>
                      <strong>
                        <fmt:message key="jsp.process.dueAt" />&nbsp;
                        <fmt:message key="jsp.at" />: </strong>
                    </span>
                    <span>{{row.dueAt | date: '${localePatternTimestamp}'}}</span>
                  </span>
                </h6>
                <h6>
                  <span>
                    <strong>
                      <fmt:message key="jsp.startedFrom" />: </strong>
                  </span>
                  <span>{{row.startUserFirstName}}</span>
                  <span> </span>
                  <span ng-if="row.startUserLastName">{{row.startUserLastName}}</span>
                  <span>&nbsp;&nbsp;&nbsp;</span>
                  <span>
                    <strong>
                      <fmt:message key="jsp.at" />: </strong>
                  </span>
                  <span>{{row.processStartedAt | date: '${localePatternTimestamp}'}}</span>
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
        <!-- #####  End Task List ##### -->

      </div>
    </div>
  </div>