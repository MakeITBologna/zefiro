<%-- Task Detail Page
  -- @author Alba Quarto
  --%>
<%@ include file="/include/directive.jsp" %>
  <div class="container">
    <%-- ###### Breadcrumb ###### --%>
    <ol class="breadcrumb">
      <li>
        <a href ng-click="gotoBreadcrumb(-1, jbForm)">
          <fmt:message key="jsp.task.myTask.label" />
        </a>
      </li>
      <li ng-repeat="p in breadcrumbs">
        <a ng-if="!$last" href ng-click="gotoBreadcrumb($index, jbForm)">
          <span ng-if="p.message">{{p.message}}</span>
          <span ng-if="!p.message">(jbMessages.noMessage)</span>
          <span>({{p.title}})</span>
        </a>
        <span ng-if="$last">{{p.description}}</span>
      </li>
    </ol>

    <%-- ###### Title ####### --%>
    <div class="page-header ">
      <div class="pull-right">
        <button class="btn btn-default" type="button" ng-click="gotoBreadcrumb(breadCrumbIndex-1, jbForm)">
          <i class="fa fa-times"></i>
          <fmt:message key="js.dialog.back" />
        </button>
      </div>
      <span>
        <h2>
          <b ng-if="processDetail.message">{{processDetail.message}}</b>
          <b ng-if="!processDetail.message">{{jbMessages.noMessage}}</b>
          <small>&nbsp;{{processDetail.title}}</small>
        </h2>
      </span>
    </div>

	<%-- ###### Deatils ###### --%>
    <div ng-if="editingProcessId" class="panel jb-header-bar">
        
        <%-- #####  Task list ##### --%>
        <table ng-table="processTasksList" class="table table-condensed ng-table-responsive workflow-list" show-filter="false">
          <tbody>

            <%-- #####  Task Row ##### --%>
            <tr  ng-repeat="row in $data">

              <%-- #####  Icons column ##### --%>
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

              <%-- #####  Details Column ##### --%>
              <td>
                <!-- <h5 class="jb-clickable">
                  <a ng-click="startEdit($g_index, $index)">
                    <span ng-if="row.description">{{row.description}}</span>
                    <span ng-else>{{jbMessages.noMessage}}</span>
                  </a>
                </h5>-->
                <h6>
                  <span>
                    <span>
                      <strong>
                        <fmt:message key="jsp.task.task" />: </strong>
                    </span>
                    <span>{{row.title}}</span>
                    <span>&nbsp;({{row.description}})</span>
                  </span>
                </h6>
                <h6>
                  <span>
                    <strong>
                      <fmt:message key="jsp.assignedAt" />: </strong>
                  </span>
                  <span>{{row.properties.bpm_startDate | date: '${localePatternTimestamp}'}}</span>
                  <span ng-show="row.properties.bpm_dueDate">
                    <span>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;</span>
                    <span>
                      <strong>
                        <fmt:message key="jsp.process.dueAt" />&nbsp;
                        <fmt:message key="jsp.at" />: </strong>
                    </span>
                    <span>{{row.properties.bpm_dueDate | date: '${localePatternTimestamp}'}}</span>
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
                <h6 ng-show="row.properties.bpm_completionDate">
                  <span>
                    <strong>
                      <fmt:message key="jsp.process.endedAt" />: </strong>
                  </span>
                  <span>{{row.properties.bpm_completionDate | date: '${localePatternTimestamp}'}}</span>
                </h6>
              </td>
            </tr>
          </tbody>
        </table>
        <%-- #####  End Task List ##### --%>  
    </div>

  </div>