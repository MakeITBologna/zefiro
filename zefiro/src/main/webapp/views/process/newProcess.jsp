<!-- Task Detail Page
  -- @author Alba Quarto
  -->
<%@ include file="/include/directive.jsp" %>
  <div class="container-fluid">

    <div class="page-header">
      <div class="pull-right">
        <button class="btn btn-default" type="button" ng-click="back(jbNewProcessForm)">
          <i class="fa fa-times"></i>
          <fmt:message key="js.dialog.back" />
        </button>
      </div>
      <h1>
        <fmt:message key="jsp.process.newProcess" />
      </h1>
    </div>
        <!-- ##### Form ##### -->
        <form name="jbNewProcessForm" class="form-horizontal" >

          <!-- #####  Process Type choise dropdown ##### -->
          <div class="row jb-form-group">
            <label class="control-label col-sm-4">
              <strong class="control jb-button-label">
                <fmt:message key="jsp.newProcess.processType" />
              </strong>
            </label>
            <div class="btn-group col-sm-8 jb-button-dropdown" uib-dropdown is-open="status.isopen" id="sortingSelect" style="display: -webkit-box;">
              <button id="sortingSelect" type="button" class="btn dropdown-toggle btn-default jb-full-area" uib-dropdown-toggle ng-disabled="disabled"
                aria-expanded="false">
                <span class="pull-left">{{choiseSelectTitle}}</span>
                <span class="pull-right">
                  <i class="fa fa-caret-down fa-sm"></i>
                </span>
              </button>
              <!-- #####  Dropdown ##### -->
              <ul class="dropdown-menu jb-full-area jb-form-button-dropdown jb-button-dropdown" uib-dropdown-menu role="menu" aria-labelledby="single-button">
                <li ng-repeat="definition in choiseSelectData" data-original-index="0" data-optgroup="1" class="selected jb-dropdown-subitems"
                  ng-repeat-end>
                  <a id="choise_{{definition.key}}" tabindex="0" class="opt jb-action" data-tokens="null" role="option" aria-disabled="false"
                    aria-selected="true" ng-click="choise(definition)">
                    <div class="row">
                      <div class="col-sm-12" ng-if="definition.name">{{definition.name}}</div>
                      <div class="col-sm-12" ng-if="!definition.name">{{definition.key}}</div>
                      <div class="col-sm-12" ng-if="definition.description">{{definition.description}}</div>
                    </div>
                  </a>
                </li>
              </ul>
            </div>
          </div>
          <!-- #####  Process Type choise dropdown ##### -->

          <!-- ##### Process Properties ##### -->
          <!-- <div class="row jb-form-group">
            <label for="jbNewProcessForm-businessKey" class="control-label col-sm-4" title="{{p.name}}">{{p.title}}</label>
            <div class="col-sm-8">
              <div></div>
            </div>
          </div>

          <div class="row jb-form-group">
            <label for="jbNewProcessForm-businessKey" class="control-label col-sm-4" title="{{p.name}}">{{p.title}}</label>
            <div class="col-sm-8">
              <div>
                <input ng-if="jbUtil.isEmptyObject(p.allowedValues)" required id="jbNewProcessForm-businessKey" class="form-control"
                    title="{{businessKey}}" type="text" name="{{businessKey}}" ng-model="startedProcess[businessKey]" />
              </div>
            </div>
          </div>	-->
          <!-- ##### End Process Properties ##### -->

          <!-- ##### Variables ##### -->
          <div ng-if="selectedType" class="row jb-form-group" ng-repeat="(i, p) in currentTypeForm">
            <label for="jbNewProcessForm-p.name" class="control-label col-sm-4" title="{{p.name}}">{{p.title}}</label>
            <div class="col-sm-8">

              <!-- ##### STRING ##### -->
              <div ng-if="jbWorkflowUtil.decodeType(p.dataType) == 'STRING'">
                <div ng-if="!readOnly" ng-class="getValidClass(jbNewProcessForm[p.name])">
                  <input ng-if="jbUtil.isEmptyObject(p.allowedValues)" ng-required="p.required" id="jbNewProcessForm-{{p.name}}" class="form-control"
                    title="{{p.name}}" type="text" name="{{p.name}}" ng-model="updatedVariables[p.name].value" />
                  <ng-include src="'views/process/select.jsp'"></ng-include>
                  <label class="text-danger" ng-show="jbValidate.showMessag.e(jbNewProcessForm[p.name])">
                    <fmt:message key="js.validate.required" />
                  </label>
                </div>
                <div ng-if="readOnly || p.linkType">
                  <p ng-if="!p.linkType" class="form-control-static">{{updatedVariables[p.name].value}}</p>
                </div>
              </div>
              <!-- ##### END STRING ##### -->

              <!-- ##### INTEGER ##### -->
              <div ng-if="jbWorkflowUtil.decodeType(p.dataType) == 'INTEGER'">
                <div ng-if="!readOnly" ng-class="getValidClass(jbNewProcessForm[p.name])">
                  <input ng-if="jbUtil.isEmptyObject(p.allowedValues)" ng-required="p.required" id="jbNewProcessForm-{{p.name}}" class="form-control"
                    title="{{p.name}}" type="text" name="{{p.name}}" ng-model="updatedVariables[p.name].value" ng-pattern="jbPatterns.number(0)"
                    jb-number="0" />
                  <ng-include src="'views/process/select.jsp'"></ng-include>
                  <label class="text-danger" ng-show="jbValidate.showMessage(jbNewProcessForm[p.name])">
                    <fmt:message key="js.validate.number" />
                  </label>
                </div>
                <div ng-if="readOnly">
                  <p class="form-control-static">{{updatedVariables[p.name].value}}</p>
                </div>
              </div>
              <!-- ##### END INTEGER ##### -->

              <!-- ##### BOOLEAN ##### -->
              <div ng-if="jbWorkflowUtil.decodeType(p.dataType) == 'BOOLEAN'">
                <div ng-if="!readOnly">
                  <select ng-required="p.required" id="jbNewProcessForm-{{p.name}}" class="form-control" title="{{p.name}}" name="{{p.name}}"
                    ng-model="updatedVariables[p.name].value" jb-boolean>
                    <option></option>
                    <option value="true">
                      <fmt:message key="jsp.boolean.1" />
                    </option>
                    <option value="false">
                      <fmt:message key="jsp.boolean.0" />
                    </option>
                  </select>
                  <label class="text-danger" ng-show="jbValidate.showMessage(jbNewProcessForm[p.name])">
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
              <!-- ##### END BOOLEAN ##### -->

              <!-- ##### DECIMAL ##### -->
              <div ng-if="jbWorkflowUtil.decodeType(p.dataType) == 'DECIMAL'">
                <div ng-if="!readOnly" ng-class="getValidClass(jbNewProcessForm[p.name])">
                  <input ng-required="p.required" id="jbNewProcessForm-{{p.name}}" class="form-control" title="{{p.name}}" type="text" name="{{p.name}}"
                    ng-model="updatedVariables[p.name].value" ng-pattern="jbPatterns.number(2)" jb-number="2" />
                  <label class="text-danger" ng-show="jbValidate.showMessage(jbNewProcessForm[p.name])">
                    <fmt:message key="js.validate.number" />
                  </label>
                </div>
                <div ng-if="readOnly">
                  <p class="form-control-static">{{updatedVariables[p.name].value}}</p>
                </div>
              </div>
              <!-- ##### END DECIMAL ##### -->

              <!-- ##### DATETIME ##### -->
              <div ng-if="jbWorkflowUtil.decodeType(p.dataType) == 'DATETIME'">
                <div class="input-group" ng-if="!readOnly">
                  <input ng-required="p.required" id="jbNewProcessForm-{{p.name}}" class="form-control" title="{{p.name}}" type="text" name="{{p.name}}"
                    readonly uib-datepicker-popup="${localePatternTimestamp}" ng-model="updatedVariables[p.name].value" is-open="calendarPopups['jbNewProcessForm-'+ p.name]"
                  />
                  <span class="input-group-btn">
                    <button type="button" class="btn btn-default" ng-click="openCalendar('jbNewProcessForm-'+ p.name)">
                      <i class="fa fa-calendar"></i>
                    </button>
                  </span>
                  <label class="text-danger" ng-show="jbValidate.showMessage(jbNewProcessForm[p.name])">
                    <fmt:message key="js.validate.required" />
                  </label>
                </div>
                <div ng-if="readOnly">
                  <p class="form-control-static">{{updatedVariables[p.name].value | date: '${localePatternTimestamp}'}}</p>
                </div>
              </div>
              <!-- ##### END DATETIME ##### -->

              <!-- ##### DATE ##### -->
              <div ng-if="jbWorkflowUtil.decodeType(p.dataType) == 'DATE'">
                <div class="input-group" ng-if="!readOnly">
                  <input ng-required="p.required" id="jbNewProcessForm-{{p.name}}" class="form-control" title="{{p.name}}" type="text" name="{{p.name}}"
                    readonly uib-datepicker-popup="${localePatternDate}" ng-model="updatedVariables[p.name].value" is-open="calendarPopups['jbNewProcessForm-'+ p.name]"
                  />
                  <span class="input-group-btn">
                    <button type="button" class="btn btn-default" ng-click="openCalendar('jbNewProcessForm-'+ p.name)">
                      <i class="fa fa-calendar"></i>
                    </button>
                  </span>
                  <label class="text-danger" ng-show="jbValidate.showMessage(jbNewProcessForm[p.name])">
                    <fmt:message key="js.validate.required" />
                  </label>
                </div>
                <div ng-if="readOnly">
                  <p class="form-control-static">{{updatedVariables[p.name].value | date: '${localePatternDate}'}}</p>
                </div>
              </div>
              <!-- ##### END DATE ##### -->
            </div>

          </div>
          <!-- ##### End Variables ##### -->

          <!-- ###### Assignee Button ##### -->
          <div ng-if="addingAssignee" class="row jb-form-group">
            <label for="jbNewProcessForm-addingAssignee" class="control-label col-sm-4" title="addingAssignee">{{addingAssignee.title}}</label>
            <div class="col-sm-8">
              <div ng-repeat="assignee in addedAssignee">
                <p class="form-control-static">{{assignee.firstName}}
                  <span ng-if="assignee.lastName">&nbsp;{{assignee.lastName}}</span>
                </p>
              </div>
            </div>
          </div>
          <div ng-if="addingAssignee" class="pull-right">
            <button class="btn jb-btn-primary" type="button" ng-click="selectAssignee()">
              <fmt:message key="jsp.selectAssignee" />
            </button>
          </div>
          <!-- ###### End Assignee Button ##### -->

        </form>
        <!-- ##### End Form ##### -->
  </div>