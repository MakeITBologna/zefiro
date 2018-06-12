<%-- Task Detail Page
  -- @author Alba Quarto
  --%>
<%@ include file="/include/directive.jsp" %>
  <div class="container">
    <%-- ###### Breadcrumb ###### --%>
    <ol class="breadcrumb">
      <li>
        <a href ng-click="gotoBreadcrumb(-1, closeDetail)">
          <fmt:message key="jsp.myProcesses.label" />
        </a>
      </li>
      <li ng-repeat="p in breadcrumbs">
        <a ng-if="!$last" href ng-click="gotoBreadcrumb($index)">
          <span ng-if="p.message">{{p.message}}</span>
          <span ng-if="!p.message">(jbMessages.noMessage)</span>
          <span>({{p.title}})</span>
        </a>
 		  <span ng-if="p.message">{{p.message}}</span>
          <span ng-if="!p.message">(jbMessages.noMessage)</span>      </li>
    </ol>

    <%-- ###### Title ####### --%>
    <div class="page-header ">
      <div class="pull-right">
        <button class="btn btn-default" type="button" ng-click="gotoBreadcrumb(breadCrumbIndex-1, closeDetail)">
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
    	<h6>
          <span>
            <span>
              <strong>
                <fmt:message key="jsp.process.label" />:</strong>
            </span>
            <span>{{jbWorkflowUtil.sanitizeId(processDetail.id)}}</span>
          </span>
          <span>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;</span>
          <span>{{processDetail.title}}</span>
          <span ng-if="processDetail.description">,&nbsp;{{processDetail.description}}</span>
        </h6>
        <h6>
          <span>
            <strong>
              <fmt:message key="jsp.started.label.m" />:&nbsp;<fmt:message key="jsp.at.label" /></strong>
          </span>
          <span>{{processDetail.startDate | date: '${localePatternTimestamp}'}}</span>
          <span ng-if="processDetail.dueDate">
            <span>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;</span>
            <span>
              <strong>
                <fmt:message key="jsp.due.label" />&nbsp;
                <fmt:message key="jsp.at.label" />:&nbsp;
              </strong>
            </span>
            <span>{{processDetail.dueDate | date: '${localePatternTimestamp}'}}</span>
          </span>       
        </h6>
        <h6 ng-if="processDetail.endDate">
           <span>
             <strong>
               <fmt:message key="jsp.ended.label" />&nbsp;
               <fmt:message key="jsp.at.label" />:&nbsp;</strong>
           </span>
           <span>{{processDetail.endDate | date: '${localePatternTimestamp}'}}</span>
        </h6>
         <h6>
           <span>
             <strong>
               <fmt:message key="jsp.priority" />:&nbsp;</strong>
               <span class="badge" ng-class="{'jb-success':processDetail.priority==3, 'jb-warning': processDetail.priority==2, 'jb-danger': processDetail.priority==1}">{{processPriority[processDetail.priority]}}</span>
           </span>
        </h6>
        
        <h5 class="jb-header-bar"><strong><fmt:message key="jsp.task.label" />:</strong></h5>
        <%-- #####  Task list ##### --%>
        <table ng-table="processTasksList" class="table table-condensed ng-table-responsive workflow-list" show-filter="false">
          <tbody>

            <%-- #####  Task Row ##### --%>
            <tr  ng-repeat="row in $data">

              <%-- #####  Icons column ##### --%>
              <td class="">
                <h5 ng-if="!row.properties.bpm_completionDate" class="text-center jb-fa-md" ng-class="(row[DEADLINE_PROXIMITY] <= 2)  ? 'jb-text-danger' : 'jb-text-success'">
                  <i class="fa fa-clock-o" uib-tooltip="{{deadlineTMessage[row[DEADLINE_PROXIMITY]]}}" tooltip-placement="right-top" tooltip-class="jb-tooltip"></i>
                </h5>
                 <h5 ng-if="row.properties.bpm_completionDate" class="text-center jb-fa-md" ng-class="(row[COMPLETITION_TIMES] === 1)  ? 'jb-text-danger' : 'jb-text-success'">
                  <i class="fa fa-check-circle" uib-tooltip="{{completitionTMessage[row[COMPLETITION_TIMES]]}}" tooltip-placement="right-top" tooltip-class="jb-tooltip"></i>
                </h5>
                <h5 class="text-center jb-fa-md" ng-class="{'jb-text-success': row.properties.bpm_priority==3, 'jb-text-warning': row.properties.bpm_priority==2, 'jb-text-danger': row.properties.bpm_priority==1}">
                  <i class="fa fa-exclamation-circle" uib-tooltip="{{taskPriority[row.properties.bpm_priority]}}" tooltip-placement="right-top" tooltip-class="jb-tooltip"></i>
                </h5>
              </td>

              <%-- #####  Details Column ##### --%>
              <td>
                <h6>
                  <span>
                    <span>
                      <strong>
                        <fmt:message key="jsp.task.label" />:&nbsp;</strong>
                    </span>
                    <span>{{row.title}}</span>
                    <span>&nbsp;({{row.description}})</span>
                  </span>
                </h6>
                <h6>
                  <span>
                    <strong>
                      <fmt:message key="jsp.started.label.f" />&nbsp;&nbsp;&nbsp;<fmt:message key="jsp.at.label" />:&nbsp;</strong>
                  </span>
                  <span>{{row.properties.bpm_startDate | date: '${localePatternTimestamp}'}}</span>
                  <span ng-show="row.properties.bpm_dueDate">
                    <span>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;</span>
                    <span>
                      <strong>
                        <fmt:message key="jsp.due.label" />&nbsp;
                        <fmt:message key="jsp.at.label" />:&nbsp;</strong>
                    </span>
                    <span>{{row.properties.bpm_dueDate | date: '${localePatternTimestamp}'}}</span>
                  </span>
                </h6>
                <h6 ng-if="!row.properties.bpm_completionDate && !row.pooled && row.owner">
                  <span>
                    <strong>
                      <fmt:message key="jsp.assigned.label.f" />&nbsp;<fmt:message key="jsp.to.label" />:&nbsp;</strong>
                  </span>
                  <span>{{row.owner.firstName}}</span>
                  <span ng-if="row.owner.lastName">&nbsp;{{row.owner.lastName}}</span>
                  <span>&nbsp;({{row.owner.userName}})</span>
                </h6>
                <h6 ng-if="!row.properties.bpm_completionDate && row.pooled">
                  <span ng-if="!row.owner">
                    <strong>
                      <span> <fmt:message key="jsp.task.notTaken" /></span>  
                    </strong>
                  </span>
                  <span ng-if="row.owner">
                    <strong>
                      <span><fmt:message key="jsp.task.taken" />&nbsp;<fmt:message key="jsp.from.label" />:&nbsp;</span>  
                    </strong>
                    <span>{{row.owner.firstName}}</span>
                    <span ng-if="row.owner.lastName">&nbsp;{{row.owner.lastName}}</span>
                    <span>&nbsp;({{row.owner.userName}})</span>
                  </span>
                </h6>
                <h6 ng-show="row.properties.bpm_completionDate">
                  <span>
                    <strong>
                      <fmt:message key="jsp.ended.label.f" />&nbsp;<fmt:message key="jsp.at.label" />:&nbsp;</strong>
                  </span>
                  <span>{{row.properties.bpm_completionDate | date: '${localePatternTimestamp}'}}</span>
                  <span ng-if="row.owner">
                    <span>&nbsp;&nbsp;&nbsp;</span>
                    <span>
                      <strong>
                        <fmt:message key="jsp.from.label" />:&nbsp;</strong>
                    </span>
                    <span>{{row.owner.firstName}}</span>
                    <span ng-if="row.owner.lastName">&nbsp;{{row.owner.lastName}}</span>
                    <span>&nbsp;({{row.owner.userName}})</span>
                  </span>
                </h6>
                <h6 ng-show="row.outcome">
                  <span>
                    <strong>
                      <fmt:message key="jsp.outcome.label" />:&nbsp;</strong>
                  </span>
                  <span>{{row.outcome}}</span>
                  <span ng-if="row.properties.bpm_comment">
                    <span>&nbsp;&nbsp;&nbsp;</span>
                    <span>
                      <strong>
                        <fmt:message key="jsp.with.label" />&nbsp;
                        <fmt:message key="jsp.comment.label" />:&nbsp;
                      </strong>
                    </span>
                    <span class="jb-text-italic">{{row.properties.bpm_comment}}</span>
                  </span>
                </h6>
              </td>
            </tr>
          </tbody>
        </table>
        <%-- #####  End Task List ##### --%>  
    </div>

  </div>