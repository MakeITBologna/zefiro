<%@ include file="/include/directive.jsp" %>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

  <div class="container-fluid">
    <div class="row">
      <div class="page-header jb-page-header">
        <div class="pull-right">
          <button class="btn btn-default" type="button" ng-click="back(jbSearchFormDocument)">
            <i class="fa fa-times"></i>
            <fmt:message key="js.dialog.back" />
          </button>
        </div>
        <h1>
          <fmt:message key="jsp.selectDocument.label" />
        </h1>
      </div>
      <div id="query">
        <div class="panel">
          <div class="panel-body">
            <form name="jbSearchFormDocument" class="form-horizontal well" novalidate jb-print="a/Document/print">
              <input type="hidden" id="jbSearchFormDocument-propertyNames" name="propertyNames" value="{{documentTemplate.propertyNames}}"
              />
              <div class="row jb-form-group">

                <div class="col-sm-1"></div>

                <label class="col-sm-1 control-label" for="jbSearchFormDocument-type" title="<fmt:message key=" jsp.document.type.title
                  "/>">
                  <fmt:message key="jsp.document.type.label" />
                </label>
                <div class="col-sm-4">
                  <select ng-disabled="relation" class="form-control" id="jbSearchFormDocument-type" name="type" title="<fmt:message key="
                    jsp.document.type.title "/>" ng-model="documentTemplate.type" ng-change="typeChanged(jbSearchFormDocument)">
                    <option></option>
                    <option ng-repeat="t in userDocumentTypes" value="{{t.id}}">{{t.name}}</option>
                  </select>
                </div>

                <label class="col-sm-1 control-label" for="jbSearchFormDocument-description" title="<fmt:message key=" jsp.document.description.title
                  "/>">
                  <fmt:message key="jsp.document.description.label" />
                </label>
                <div class="col-sm-4">
                  <input class="form-control" id="jbSearchFormDocument-description" title="<fmt:message key=" jsp.document.description.title
                    "/>" type="text" name="description" ng-model="documentTemplate['cmis:description']" />
                </div>

                <div class="col-sm-1"></div>

              </div>

              <div class="row jb-form-group">

                <div class="col-sm-1"></div>

                <label class="col-sm-1 control-label" for="jbSearchFormDocument-createdBy" title="<fmt:message key=" jsp.document.createdBy.title
                  "/>">
                  <fmt:message key="jsp.document.createdBy.label" />
                </label>
                <div class="col-sm-4">
                  <input class="form-control" id="jbSearchFormDocument-createdBy" title="<fmt:message key=" jsp.document.createdBy.title
                    "/>" type="text" name="cmis:createdBy" ng-model="documentTemplate['cmis:createdBy']" />
                </div>

                <label class="col-sm-1 control-label" for="jbSearchFormDocument-created-FROM" title="<fmt:message key=" jsp.document.created.title
                  "/>">
                  <fmt:message key="jsp.document.created.label" />
                </label>
                <div class="col-sm-2">
                  <div class="input-group">
                    <span class="input-group-addon">
                      <fmt:message key="jsp.from.label" />
                    </span>
                    <input id="jbSearchFormDocument-created" class="form-control" title="<fmt:message key=" jsp.document.created.title
                      "/> <fmt:message key="jsp.from.label "/>" type="text" name="cmis:creationDate|GE" ng-model="documentTemplate['cmis:creationDate|GE']"
                      uib-datepicker-popup="${localePatternDate}" is-open="calendarPopups['jbSearchFormDocument-created-FROM']"
                    />
                    <span class="input-group-btn">
                      <button type="button" class="btn btn-default" ng-click="openCalendar('jbSearchFormDocument-created-FROM')">
                        <i class="fa fa-calendar"></i>
                      </button>
                    </span>
                  </div>
                </div>

                <div class="col-sm-2">
                  <div class="input-group">
                    <span class="input-group-addon">
                      <fmt:message key="jsp.to.label" />
                    </span>
                    <input id="jbSearchFormDocument-created-TO" class="form-control" title="<fmt:message key=" jsp.document.created.title
                      "/> <fmt:message key="jsp.to.label "/>" type="text" name="cmis:creationDate|LE" ng-model="documentTemplate['cmis:creationDate|LE']"
                      uib-datepicker-popup="${localePatternDate}" is-open="calendarPopups['jbSearchFormDocument-created-TO']"
                    />
                    <span class="input-group-btn">
                      <button type="button" class="btn btn-default" ng-click="openCalendar('jbSearchFormDocument-created-TO')">
                        <i class="fa fa-calendar"></i>
                      </button>
                    </span>
                  </div>
                </div>

                <div class="col-sm-1"></div>

              </div>

              <div class="row jb-form-group" ng-repeat="r in searchMatrix">

                <div class="col-sm-1"></div>

                <any ng-repeat="p in r" ng-switch="p.propertyType">

                  <div ng-switch-when="INTEGER">
                    <label class="col-sm-1 control-label" for="jbSearchFormDocument-{{jbUtil.sanitize(p.queryName)}}-FROM" title="{{p.description}}">{{p.displayName}}</label>
                    <div class="col-sm-2">
                      <div class="input-group">
                        <span class="input-group-addon">
                          <fmt:message key="jsp.from.label" />
                        </span>
                        <input class="form-control" id="jbSearchFormDocument-{{jbUtil.sanitize(p.queryName)}}-FROM" title="{{p.description}} <fmt:message key="
                          jsp.from.label "/>" type="text" name="{{p.queryName}}|GE" ng-model="documentTemplate[p.queryName+'|GE']"
                          ng-pattern="jbPatterns.number(0)" />
                      </div>
                    </div>

                    <div class="col-sm-2">
                      <div class="input-group">
                        <span class="input-group-addon">
                          <fmt:message key="jsp.to.label" />
                        </span>
                        <input class="form-control" id="jbSearchFormDocument-{{jbUtil.sanitize(p.queryName)}}-TO" title="{{p.description}} <fmt:message key="
                          jsp.to.label "/>" type="text" name="{{p.queryName}}|LE" ng-model="documentTemplate[p.queryName+'|LE']"
                          ng-pattern="jbPatterns.number(0)" />
                      </div>
                    </div>
                  </div>

                  <div ng-switch-when="DECIMAL">
                    <label class="col-sm-1 control-label" for="jbSearchFormDocument-{{jbUtil.sanitize(p.queryName)}}-FROM" title="{{p.description}}">{{p.displayName}}</label>
                    <div class="col-sm-2">
                      <div class="input-group">
                        <span class="input-group-addon">
                          <fmt:message key="jsp.from.label" />
                        </span>
                        <input class="form-control" id="jbSearchFormDocument-{{jbUtil.sanitize(p.queryName)}}-FROM" title="{{p.description}} <fmt:message key="
                          jsp.from.label "/>" type="text" name="{{p.queryName}}|GE" ng-model="documentTemplate[p.queryName+'|GE']"
                          ng-pattern="jbPatterns.number(2)" />
                      </div>
                    </div>

                    <div class="col-sm-2">
                      <div class="input-group">
                        <span class="input-group-addon">
                          <fmt:message key="jsp.to.label" />
                        </span>
                        <input class="form-control" id="jbSearchFormDocument-{{jbUtil.sanitize(p.queryName)}}-TO" title="{{p.description}} <fmt:message key="
                          jsp.to.label "/>" type="text" name="{{p.queryName}}|LE" ng-model="documentTemplate[p.queryName+'|LE']"
                          ng-pattern="jbPatterns.number(2)" />
                      </div>
                    </div>
                  </div>

                  <div ng-switch-when="DATETIME">
                    <label class="col-sm-1 control-label" for="jbSearchFormDocument-{jbUtil.sanitize(p.queryName)}}-FROM" title="{{p.description}}">{{p.displayName}}</label>
                    <div class="col-sm-2">
                      <div class="input-group">
                        <span class="input-group-addon">
                          <fmt:message key="jsp.from.label" />
                        </span>
                        <input id="jbSearchFormDocument-{{jbUtil.sanitize(p.queryName)}}-FROM" class="form-control" title="{{p.displayName}}" <fmt:message
                          key="jsp.from.label" />" type="text" name="{{p.queryName}}|GE" ng-model="documentTemplate[p.queryName+'|GE']" uib-datepicker-popup="${localePatternDate}"
                        is-open="calendarPopups['jbSearchFormDocument-'+jbUtil.sanitize(p.queryName)+'-FROM']"/>
                        <span class="input-group-btn">
                          <button type="button" class="btn btn-default" ng-click="openCalendar('jbSearchFormDocument-'+jbUtil.sanitize(p.queryName)+'-FROM')">
                            <i class="fa fa-calendar"></i>
                          </button>
                        </span>
                      </div>
                    </div>

                    <div class="col-sm-2">
                      <div class="input-group">
                        <span class="input-group-addon">
                          <fmt:message key="jsp.to.label" />
                        </span>
                        <input id="jbSearchFormDocument-{{jbUtil.sanitize(p.queryName)}}-TO" class="form-control" title="<fmt:message key=" jsp.document.{{p.queryName}}to.title
                          "/> <fmt:message key="jsp.to.label "/>" type="text" name="{{p.queryName}}|LE" ng-model="documentTemplate[p.queryName+'|LE']"
                          uib-datepicker-popup="${localePatternDate}" is-open="calendarPopups['jbSearchFormDocument-'+jbUtil.sanitize(p.queryName)+'-TO']"
                        />
                        <span class="input-group-btn">
                          <button type="button" class="btn btn-default" ng-click="openCalendar('jbSearchFormDocument-'+jbUtil.sanitize(p.queryName)+'-TO')">
                            <i class="fa fa-calendar"></i>
                          </button>
                        </span>
                      </div>
                    </div>
                  </div>

                  <div ng-switch-when="BOOLEAN">
                    <label class="col-sm-1 control-label" for="jbSearchFormDocument-{{jbUtil.sanitize(p.queryName)}}" title="{{p.description}}">{{p.displayName}}</label>
                    <div class="col-sm-4">
                      <select class="form-control" id="jbSearchFormDocument-{{jbUtil.sanitize(p.queryName)}}" title="{{p.description}}" name="{{p.queryName}}"
                        ng-model="documentTemplate[p.queryName]" jb-boolean>
                        <option></option>
                        <option value="true">
                          <fmt:message key="jsp.boolean.1" />
                        </option>
                        <option value="false">
                          <fmt:message key="jsp.boolean.0" />
                        </option>
                      </select>
                    </div>
                  </div>

                  <div ng-switch-when="STRING">
                    <label class="col-sm-1 control-label" for="jbSearchFormDocument-{{jbUtil.sanitize(p.queryName)}}" title="{{p.description}}">{{p.displayName}}</label>
                    <div class="col-sm-4">
                      <input ng-if="jbUtil.isEmptyObject(p.choices)" class="form-control" id="jbSearchFormDocument-{{jbUtil.sanitize(p.queryName)}}"
                        title="{{p.description}}" type="text" name="{{p.queryName}}" ng-model="documentTemplate[p.queryName]"
                      />
                      <select ng-if="!jbUtil.isEmptyObject(p.choices)" class="form-control" id="jbSearchFormDocument-{{jbUtil.sanitize(p.queryName)}}"
                        title="{{p.description}}" name="{{p.queryName}}" ng-model="documentTemplate[p.queryName]">
                        <option></option>
                        <option ng-repeat="c in p.choices" value="{{c}}">{{c}}</option>
                      </select>
                    </div>
                  </div>

                </any>
                <div class="col-sm-1"></div>
              </div>

              <div class="row form-group">

                <div class="col-sm-1"></div>

                <label class="col-sm-1 control-label" for="jbSearchFormDocument-contains" title="<fmt:message key=" jsp.document.content.title
                  "/>">
                  <fmt:message key="jsp.document.content.label" />
                </label>
                <div class="col-sm-9">
                  <input class="form-control" id="jbSearchFormDocument-contains" title="<fmt:message key=" jsp.document.content.title "/>"
                    type="text" name="contains" ng-model="documentTemplate.contains" />
                </div>

                <div class="col-sm-1"></div>

              </div>

              <div class="row jb-form-group" align="center">
                <button type="submit" class="btn btn-primary" ng-click="jbValidate.checkForm(jbSearchFormDocument) && search()" promise-btn>
                  <i class="fa fa-search"></i>
                  <fmt:message key="jsp.query.submit" />
                </button>
                <button type="button" class="btn btn-default" ng-click="clearSearch(jbSearchFormDocument)">
                  <i class="fa fa-eraser"></i>
                  <fmt:message key="jsp.query.reset" />
                </button>
              </div>

            </form>
          </div>
        </div>
      </div>
      <%-- query finish --%>
        <%-- results start --%>
          <div>
            <div class="panel">
              <div class="panel-body">

                <div class="row jb-toolbar">
                  <div class="col-sm-3">
                    <h4>{{documentTable.total()}}
                      <fmt:message key="jsp.document.label" />
                      <any ng-if="!jbUtil.isEmptyObject(documentType)">( {{documentType.name}} )</any>
                    </h4>
                  </div>
                  <div class="col-sm-9 "></div>
                </div>

                <table ng-table="documentTable" template-header="views/document/documentHeaders.jsp" class="table table-condensed table-bordered table-striped ng-table-responsive"
                  show-filter="false">
                  <tbody>
                    <%-- ng-dblclick definisce il comportamento all'utilizzo di doppio click sugli elementi. !relation permette di disattivare questa funzionalià nella modalità di selezione delle relazioni per i documenti --%>
                      <tr ng-repeat="row in $data track by $index" ng-dblclick="!relation && startEdit($index)">

                        <td ng-if="jbUtil.isEmptyObject(documentType)" sortable="'typeName'">{{row.typeName}}</td>
                        <td sortable="'description'">
                          <a ng-href="a/Document/{{row.id}}/content" target="_blank">{{row.description}}</a>
                        </td>
                        <td ng-if="jbUtil.isEmptyObject(documentType)" sortable="'createdBy'">{{row.createdBy}}</td>
                        <td class="text-right" sortable="'created'">{{row.created | date: '${localePatternDate}'}}</td>

                        <td ng-repeat-start="p in documentType.propertyList" ng-if="p.queryable && p.propertyType == 'INTEGER'" class="text-right"
                          sortable="'\''+p.queryName+'\''">{{row[p.queryName] | number:0}}</td>
                        <td ng-if="p.queryable && p.propertyType == 'DECIMAL'" class="text-right" sortable="'\''+p.queryName+'\''">{{row[p.queryName] | number:2}}</td>
                        <td ng-if="p.queryable && p.propertyType == 'DATETIME'" class="text-right" sortable="'\''+p.queryName+'\''">{{row[p.queryName] | date: '${localePatternDate}'}}</td>
                        <td ng-if="p.queryable && p.propertyType == 'BOOLEAN' && row[p.queryName]" sortable="'\''+p.queryName+'\''">
                          <fmt:message key="jsp.boolean.1" />
                        </td>
                        <td ng-if="p.queryable && p.propertyType == 'BOOLEAN' && !row[p.queryName]" sortable="'\''+p.queryName+'\''">
                          <fmt:message key="jsp.boolean.0" />
                        </td>
                        <td ng-repeat-end ng-if="p.queryable && p.propertyType == 'STRING'" sortable="'\''+p.queryName+'\''">
                          <a ng-if="p.linkType" href ng-click="showDocument(row[p.queryName])">{{row[p.queryName].split('|')[1]}}</a>
                          <any ng-if="!p.linkType">{{row[p.queryName]}}</any>
                        </td>

                        <td>
                          <div>
                            <button type="button" class="btn btn-primary btn-xs" ng-click="selectDocument(row) && clearAllSearch(jbSearchFormDocument)">
                              <i class="fa fa-plus fa-lg"></i>
                            </button>
                          </div>
                        </td>

                      </tr>

                  </tbody>
                  <tfoot ng-if="!jbUtil.isEmptyObject(summaries)">
                    <tr>
                      <td class="text-right" colspan="2">
                        <strong>
                          <fmt:message key="jsp.totals.label" />
                        </strong>
                      </td>
                      <td class="text-right" ng-repeat-start="p in documentType.propertyList" ng-if="p.queryable && p.propertyType == 'INTEGER'">
                        <strong>{{summaries[p.queryName] | number:0}}</strong>
                      </td>
                      <td class="text-right" ng-if="p.queryable && p.propertyType == 'DECIMAL'">
                        <strong>{{summaries[p.queryName] | number:2}}</strong>
                      </td>
                      <td ng-repeat-end ng-if="p.queryable && !isNumeric(p.propertyType)"></td>
                      <td></td>
                    </tr>
                  </tfoot>
                </table>
              </div>
            </div>
          </div>
          <%-- results finish --%>
    </div>
  </div>