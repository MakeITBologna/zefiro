<%-- Start new Process Page
  -- @author Alba Quarto
  --%>
<%@ include file="/include/directive.jsp" %>
  <div class="container" ng-show="!selectDocument">

    <div class="page-header jb-header-bar">
      <div class="pull-right">
        <button class="btn btn-default" type="button" ng-click="back(jbForm)">
          <i class="fa fa-times"></i>
          <fmt:message key="js.dialog.back" />
        </button>
      </div>
      <h1>
        <fmt:message key="jsp.process.newProcess" />
      </h1>
    </div>

    <div>
      <div class="panel jb-header-bar">

        <div class="panel-body jb-panel-body">
          <%-- ##### Edit Form ##### --%>
          <form name="jbForm" class="form-horizontal">

            <%-- #####  Process Type choise dropdown ##### --%>
            <div class="row jb-form-group">
              <label class="control-label col-sm-4" style="padding-left: 0pX;">
                <fmt:message key="jsp.newProcess.processType" />
              </label>
              <div class="btn-group col-sm-8 jb-button-dropdown jb-form-control" uib-dropdown is-open="status.isopen" id="sortingSelect"
                style="display: -webkit-box;">
                <button id="sortingSelect" type="button" class="btn dropdown-toggle btn-default jb-full-area" uib-dropdown-toggle ng-disabled="disabled"
                  aria-expanded="false">
                  <span class="pull-left">{{choiseSelectTitle}}</span>
                  <span class="pull-right">
                    <i class="fa fa-caret-down fa-sm"></i>
                  </span>
                </button>
                <%-- #####  Dropdown ##### --%>
                <ul class="dropdown-menu jb-full-area  jb-button-dropdown" uib-dropdown-menu role="menu" aria-labelledby="single-button">
                  <li ng-repeat="definition in choiseSelectData" data-original-index="0" data-optgroup="1" class="selected jb-dropdown-subitems"
                    ng-repeat-end>
                    <a id="choise_{{definition.key}}" tabindex="0" class="opt jb-action" data-tokens="null" role="option" aria-disabled="false"
                      aria-selected="true" ng-click="choise(definition)">
                      <div class="row">
                        <div ng-if="definition.name" class="col-sm-12 jb-text-bold" >{{definition.name}}</div>
                        <div ng-if="!definition.name" class="col-sm-12 jb-text-bold">{{definition.key}}</div>
                        <div ng-if="definition.description" class="col-sm-12 jb-text-italic">{{definition.description}}</div>
                      </div>
                    </a>
                  </li>
                </ul>
              </div>
            </div>
            <%-- #####  Process Type choise dropdown ##### --%>

            <%-- ##### Process Properties ##### --%>
            <%-- <div class="row jb-form-group">
            <label for="jbForm-businessKey" class="control-label col-sm-4" title="{{p.name}}">{{p.title}}</label>
            <div class="col-sm-8">
              <div></div>
            </div>
          </div>

          <div class="row jb-form-group">
            <label for="jbForm-businessKey" class="control-label col-sm-4" title="{{p.name}}">{{p.title}}</label>
            <div class="col-sm-8">
              <div>
                <input ng-if="jbUtil.isEmptyObject(p.allowedValues)" required id="jbForm-businessKey" class="form-control"
                    title="{{businessKey}}" type="text" name="{{businessKey}}" ng-model="startedProcess[businessKey]" />
              </div>
            </div>
          </div>	--%>
            <%-- ##### End Process Properties ##### --%>

            <%-- ##### Variables ##### --%>
            <div ng-if="selectedType" class="row jb-form-group" ng-repeat="(i, p) in currentTypeForm">
              <label for="jbForm-p.name" class="control-label col-sm-4" title="{{p.name}}" style="padding-left: 0px;">{{p.title}}</label>
              <div class="col-sm-8 jb-form-control">

                <%-- ##### STRING ##### --%>
                <div ng-if="jbWorkflowUtil.decodeType(p.dataType) == 'STRING'">
                  <div ng-if="!readOnly" ng-class="getValidClass(jbForm[p.name])">
                    <input ng-if="jbUtil.isEmptyObject(p.allowedValues)" ng-required="p.required" id="jbForm-{{p.name}}" class="form-control"
                      title="{{p.name}}" type="text" name="{{p.name}}" ng-model="updatedVariables[p.name].value" />
                    <ng-include src="'views/process/select.jsp'"></ng-include>
                    <label class="text-danger" ng-show="jbValidate.showMessag.e(jbForm[p.name])">
                      <fmt:message key="js.validate.required" />
                    </label>
                  </div>
                  <div ng-if="readOnly || p.linkType">
                    <p ng-if="!p.linkType" class="form-control-static">{{updatedVariables[p.name].value}}</p>
                  </div>
                </div>
                <%-- ##### END STRING ##### --%>

                <%-- ##### INTEGER ##### --%>
                <div ng-if="jbWorkflowUtil.decodeType(p.dataType) == 'INTEGER'">
                  <div ng-if="!readOnly" ng-class="getValidClass(jbForm[p.name])">
                    <input ng-if="jbUtil.isEmptyObject(p.allowedValues)" ng-required="p.required" id="jbForm-{{p.name}}" class="form-control"
                      title="{{p.name}}" type="text" name="{{p.name}}" ng-model="updatedVariables[p.name].value" ng-pattern="jbPatterns.number(0)"
                      jb-number="0" />
                    <ng-include src="'views/process/select.jsp'"></ng-include>
                    <label class="text-danger" ng-show="jbValidate.showMessage(jbForm[p.name])">
                      <fmt:message key="js.validate.number" />
                    </label>
                  </div>
                  <div ng-if="readOnly">
                    <p class="form-control-static">{{updatedVariables[p.name].value}}</p>
                  </div>
                </div>
                <%-- ##### END INTEGER ##### --%>

                <%-- ##### BOOLEAN ##### --%>
                <div ng-if="jbWorkflowUtil.decodeType(p.dataType) == 'BOOLEAN'">
                  <div ng-if="!readOnly">
                    <select ng-required="p.required" id="jbForm-{{p.name}}" class="form-control" title="{{p.name}}" name="{{p.name}}"
                      ng-model="updatedVariables[p.name].value" jb-boolean>
                      <option></option>
                      <option value="true">
                        <fmt:message key="jsp.boolean.1" />
                      </option>
                      <option value="false">
                        <fmt:message key="jsp.boolean.0" />
                      </option>
                    </select>
                    <label class="text-danger" ng-show="jbValidate.showMessage(jbForm[p.name])">
                      <fmt:message key="js.validate.required" />
                    </label>
                  </div>
                  <div ng-if="readOnly">
                    <p ng-if="updatedVariables[p.name].value" class="form-control-static">
                      <fmt:message key="jsp.boolean.1" />
                    </p>
                    <p ng-if="updatedVariables[p.name].value" class="form-control-static">
                      <fmt:message key="jsp.boolean.0" />
                    </p>
                  </div>
                </div>
                <%-- ##### END BOOLEAN ##### --%>

                <%-- ##### DECIMAL ##### --%>
                <div ng-if="jbWorkflowUtil.decodeType(p.dataType) == 'DECIMAL'">
                  <div ng-if="!readOnly" ng-class="getValidClass(jbForm[p.name])">
                    <input ng-required="p.required" id="jbForm-{{p.name}}" class="form-control" title="{{p.name}}" type="text" name="{{p.name}}"
                      ng-model="updatedVariables[p.name].value" ng-pattern="jbPatterns.number(2)" jb-number="2" />
                    <label class="text-danger" ng-show="jbValidate.showMessage(jbForm[p.name])">
                      <fmt:message key="js.validate.number" />
                    </label>
                  </div>
                  <div ng-if="readOnly">
                    <p class="form-control-static">{{updatedVariables[p.name].value}}</p>
                  </div>
                </div>
                <%-- ##### END DECIMAL ##### --%>

                <%-- ##### DATETIME ##### --%>
                <div ng-if="jbWorkflowUtil.decodeType(p.dataType) == 'DATETIME'">
                  <div class="input-group" ng-if="!readOnly">
                    <input ng-required="p.required" id="jbForm-{{p.name}}" class="form-control" title="{{p.name}}" type="text" name="{{p.name}}"
                      readonly uib-datepicker-popup="${localePatternTimestamp}" ng-model="updatedVariables[p.name].value" is-open="calendarPopups['jbForm-'+ p.name]"
                    />
                    <span class="input-group-btn">
                      <button type="button" class="btn btn-default" ng-click="openCalendar('jbForm-'+ p.name)">
                        <i class="fa fa-calendar"></i>
                      </button>
                    </span>
                    <label class="text-danger" ng-show="jbValidate.showMessage(jbForm[p.name])">
                      <fmt:message key="js.validate.required" />
                    </label>
                  </div>
                  <div ng-if="readOnly">
                    <p class="form-control-static">{{updatedVariables[p.name].value | date: '${localePatternTimestamp}'}}</p>
                  </div>
                </div>
                <%-- ##### END DATETIME ##### --%>

                <%-- ##### DATE ##### --%>
                <div ng-if="jbWorkflowUtil.decodeType(p.dataType) == 'DATE'">
                  <div class="input-group" ng-if="!readOnly">
                    <input ng-required="p.required" id="jbForm-{{p.name}}" class="form-control" title="{{p.name}}" type="text" name="{{p.name}}"
                      readonly uib-datepicker-popup="${localePatternDate}" ng-model="updatedVariables[p.name].value" is-open="calendarPopups['jbForm-'+ p.name]"
                    />
                    <span class="input-group-btn">
                      <button type="button" class="btn btn-default" ng-click="openCalendar('jbForm-'+ p.name)">
                        <i class="fa fa-calendar"></i>
                      </button>
                    </span>
                    <label class="text-danger" ng-show="jbValidate.showMessage(jbForm[p.name])">
                      <fmt:message key="js.validate.required" />
                    </label>
                  </div>
                  <div ng-if="readOnly">
                    <p class="form-control-static">{{updatedVariables[p.name].value | date: '${localePatternDate}'}}</p>
                  </div>
                </div>
                <%-- ##### END DATE ##### --%>
              </div>

            </div>
            <%-- ##### End Variables ##### --%>

            <%-- ##### Adding Items ##### --%>
            <div ng-if="selectedType" class="row jb-form-group">
              <label for="jbForm-addingItems" class="control-label col-sm-4" title="addingItems" style="padding-left: 0px;">Items</label>
              <div class="col-sm-8 jb-form-control" style="display: inline-table;">
                <div class="form-control" readonly  style="display: inherit;">
                  <div ng-repeat="($index, item) in selectedItems">
                      <span>{{item.name}}</span>
                      <span ng-if="item.description">({{item.description}})</span>
                      <span class="pull-right" ng-click="removeItem(item)">
                		<i class="fa fa-times-circle-o"></i>
              		  </span>
                  </div>
                </div>
              </div>
            </div>
            <div ng-if="selectedType" class="row jb-form-group">
              <div class="text-right">
                <button class="btn jb-btn-primary" type="button" ng-click="addItem()">
                  <div><fmt:message key="jsp.newProcess.addItem" /></div>
                </button>
              </div>
            </div>
            <%-- ##### End Adding Items ##### --%>

            <%-- ###### Assignee Button ##### --%>
            <div ng-if="addingAssignee" class="row jb-form-group">
              <label for="jbForm-addingAssignee" class="control-label col-sm-4" title="addingAssignee" style="padding-left: 0px;">{{addingAssignee.title}}</label>
              <div class="col-sm-8 jb-form-control"  style="display: inline-table;">
                <div class="form-control" readonly  style="display: inherit;">
                  <span ng-repeat="($index, assignee) in addedAssignee">
                    <span ng-if="$index>0">,&nbsp;</span>
                    <span ng-if="assigneeType === AUTHORITY_TYPE.PERSON">
                      <span>{{assignee.firstName}}</span>
                      <span ng-if="assignee.lastName">&nbsp;{{assignee.lastName}}</span>
                      <span>({{assignee.id}})</span>
                    </span>
                    <span ng-if="assigneeType === AUTHORITY_TYPE.GROUP">
                      <span>{{assignee.displayName}}</span>
                    </span>
                  </span>
                </div>
              </div>
            </div>
            <div ng-if="addingAssignee" class="text-right row jb-form-control">
              <button class="btn jb-btn-primary" type="button" ng-click="selectAssignee()">
                <div ng-show="!$scope.assigneeMany"><fmt:message key="jsp.selectAssignee" /></div>
                <div ng-show="$scope.assigneeMany"><fmt:message key="jsp.selectAssignees" /></div>
              </button>
            </div>
            <%-- ###### End Assignee Button ##### --%>
          </form>

          <div ng-if="selectedType" class="row jb-form-control text-right navbar-btn ">
            <button class="btn btn-primary" type="button" ng-click="jbValidate.checkForm(jbForm) && startProcess(jbForm)">
              <span>
                <fmt:message key="jsp.process.start" />
              </span>
            </button>
          </div>
          <%-- ##### End Edit Form ##### --%>
        </div>
      </div>
    </div>
  </div>
  <div ng-show="selectDocument">
    <div class="row" ng-include="'views/process/addDocument.jsp'"  ng-controller="DocumentController" >
  </div>
