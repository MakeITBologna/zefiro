<%-- @author Alba Quarto --%>
<%@ include file="/include/directive.jsp" %>
   <table  ng-table="table" class="table table-condensed ng-table-responsive workflow-list" show-filter="false"
          show-group="isGroupHeaderRowVisible">
		<%-- #####  Process list ##### --%>
          <tbody>

            <%-- #####  Group header ##### --%>
            <tr class="ng-table-group group-header" ng-repeat-start="($g_index, group) in $groups">
              <td colspan="3">
                <h4>
                  <a ng-click="group.$hideRows = !group.$hideRows">
                    <span class="glyphicon jb-clickable" ng-class="{ 'glyphicon-chevron-right': group.$hideRows, 'glyphicon-chevron-down': !group.$hideRows }"></span>
                    <span>{{definitionsMap[group.value][WORKFLOW_DEFINITION_PROPERTIES.TITLE] }}</span> 
                    <span>({{group.data.length}})</span>
                  </a>
                </h4>
              </td>
            </tr>

            <%-- ##### Process Row ##### --%>
            <tr ng-hide="group.$hideRows" ng-repeat="($index, row) in group.data track by row.id" ng-dblclick="viewDetail($g_index, $index, table)"
              ng-repeat-end>

              <%-- #####  Icons column ##### --%>
              <td class="">
                <h5 class="text-center jb-text-primary">
                  <i class="fa fa-bars"></i>
                </h5>
                <h5  class="text-center">
                 <span  ng-class="{'jb-text-success':row.priority==3, 'jb-text-warning': row.priority==2, 'jb-text-danger':row.priority==1}">
                   <i class="fa fa-exclamation-circle" uib-tooltip="{{taskPriority[row.priority]}}" tooltip-placement="right-top" tooltip-class="jb-tooltip"></i>
                 </span>
                </h5>
              </td>

              <%-- #####  Details Column ##### --%>
              <td>
                <h5 class="jb-clickable">
                  <a ng-click="viewDetail($g_index, $index, table)">
                    <span ng-if="row[WORKFLOW_INSTANCE_PROPERTIES.MESSAGE]">{{row[WORKFLOW_INSTANCE_PROPERTIES.MESSAGE]}}</span>
                    <span ng-else>{{jbMessages.noMessage}}</span>
                  </a>
                </h5>
                <h6>
                  <span>
                    <strong>
                      <fmt:message key="jsp.started.label.m" />&nbsp;<fmt:message key="jsp.at.label" />: </strong>
                  </span>
                  <span>{{row[WORKFLOW_INSTANCE_PROPERTIES.START_DATE] | date: '${localePatternTimestamp}'}}</span>
                  <span ng-if="row[WORKFLOW_INSTANCE_PROPERTIES.DUE_DATE]">
                    <span>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;</span>
                    <span>
                      <strong>
                        <fmt:message key="jsp.due.label" />&nbsp;
                        <fmt:message key="jsp.at.label" />:&nbsp;</strong>
                    </span>
                    <span>{{row[WORKFLOW_INSTANCE_PROPERTIES.DUE_DATE] | date: '${localePatternTimestamp}'}}</span>
                  </span>
                </h6>
                <h6 ng-if="row[WORKFLOW_INSTANCE_PROPERTIES.END_DATE]">
                  <span>
                    <strong>
                      <fmt:message key="jsp.ended.label" /> <fmt:message key="jsp.at.label" />: </strong>
                  </span>
                  <span>{{row[WORKFLOW_INSTANCE_PROPERTIES.END_DATE] | date: '${localePatternTimestamp}'}}</span>
                </h6>
              </td>
            </tr>
          </tbody>
          </table>
        <%-- #####  End Process List ##### --%>