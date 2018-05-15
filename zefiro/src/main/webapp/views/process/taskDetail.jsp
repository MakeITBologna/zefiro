<!-- datail start -->
<%@ include file="/include/directive.jsp" %>

  <div class="container-fluid">

    <ol class="breadcrumb">
  		<li><a href ng-click="gotoTaskBreadcrumb(-1, jbTaskForm)"><fmt:message key="jsp.task.label"/></a></li>
		<li ng-repeat="p in breadcrumbs">
		  <a ng-if="!$last" href ng-click="gotoTaskBreadcrumb($index, jbTaskForm)">{{p.processBusinessKey}}</a>
		  <span ng-if="$last">{{p.description}}</span>
		</li>
    </ol>
    
    <div class="page-header ">
      <div class="pull-right">
        <button class="btn btn-default" type="button" ng-click="gotoTaskBreadcrumb(breadCrumbIndex-1, jbTaskForm)">
          <i class="fa fa-times"></i>
          <fmt:message key="js.dialog.back" />
        </button>
      </div>
      <span ng-hide="currentRowNum == null">
        <h2>
          <b>{{taskEditing.processBusinessKey}}</b>
        </h2>
        <h3>
        	{{taskEditing.description}}
        </h3>
      </span>
    </div>

    <div class="row form-group">
   
      <!-- Dettaglio -->
      <div class="col-sm-4">
        <!-- Informazioni di dettaglio -->
        <form name="jbTaskForm" class="form-horizontal" novalidate>
          <!-- 				<input ng-if="currentRownum != null" type="hidden" name="id" ng-model="documentEditing.id"/>

				<div ng-if="currentRownum == null" class="row jb-form-group">
					<label for="jbTaskForm-file" class="control-label col-sm-4" title="<fmt:message key="jsp.document.file.title"/>"><fmt:message key="jsp.document.file.title"/></label>
					<div class="col-sm-8">
						<div class="input-group">
						 <input id="jbTaskForm-file" class="form-control" title="<fmt:message key="jsp.document.name.title"/>" type="file" name="file" jb-upload="setCurrentFileName(newUrl, uploaded, userFilename)"/>
						 <span class="input-group-btn">
						   <button class="btn btn-default" type="button" promise-btn="uploadPromise"><i class="fa fa-upload"></i> Upload</button>
						 </span>
						</div>
					</div>
				</div>

				<div class="row jb-form-group">
				  <label for="jbTaskForm-name" class="control-label col-sm-4" title="<fmt:message key="jsp.document.name.title"/>"><fmt:message key="jsp.document.name.label" /></label>
				  <div class="col-sm-8">
				  	<input ng-required="true" ng-if="!readOnly" id="jbTaskForm-name" class="form-control" title="<fmt:message key="jsp.document.name.title"/>" type="text" name="name" ng-model="documentEditing.name" pattern="^\w+[\w\s\.-]*$"/>
				  	<label class="text-danger" ng-show="jbValidate.showMessage(jbTaskForm.name)"><fmt:message key="js.validate.filename"/></label>
				  	<p ng-if="readOnly" class="form-control-static">{{documentEditing.name}}</p>
				  </div>
				</div>

				<div class="row jb-form-group">
				  <label for="jbTaskForm-description" class="control-label col-sm-4" title="<fmt:message key="jsp.document.description.title"/>"><fmt:message key="jsp.document.description.label" /></label>
				  <div class="col-sm-8">
				  	<input ng-required="true" ng-if="!readOnly" id="jbTaskForm-description" class="form-control" title="<fmt:message key="jsp.document.description.title"/>" type="text" name="description" ng-model="documentEditing.description"/>
				  	<label class="text-danger" ng-show="jbValidate.showMessage(jbTaskForm.description)"><fmt:message key="js.validate.required"/></label>
				  	<p ng-if="readOnly" class="form-control-static">{{documentEditing.description}}</p>
				  </div>
				</div>

				<div  class="row jb-form-group">
				  <label for="jbTaskForm-type" class="control-label col-sm-4" title="<fmt:message key="jsp.document.type.title"/>"><fmt:message key="jsp.document.type.label" /></label>
				  <div class="col-sm-8">
				  	<div ng-if="currentRownum != null"><p class="form-control-static">{{documentEditing.typeName}}</p></div>
				  	<div ng-if="currentRownum == null">
						<select ng-required="true" class="form-control" id="jbTaskForm-type" name="type" title="<fmt:message key="jsp.document.type.title"/>" ng-model="documentEditing.type" ng-change="typeChangedInsert(jbTaskForm)">
					  	  	<option></option>
					   		<option ng-repeat="t in userDocumentTypes" value="{{t.id}}">{{t.name}}</option>
					 	</select>
					</div>
					<label class="text-danger" ng-show="jbValidate.showMessage(jbTaskForm.type)"><fmt:message key="js.validate.required"/></label>
				  </div>
				</div>

				<div ng-if="currentRownum != null" class="row jb-form-group">
				  <label for="jbTaskForm-version" class="control-label col-sm-4" title="<fmt:message key="jsp.document.version.title"/>"><fmt:message key="jsp.document.version.label" /></label>
				  <div class="col-sm-8">
				  <p class="form-control-static">{{documentEditing.version}}</p>
				  </div>
				</div>

				<div ng-if="currentRownum != null" class="row jb-form-group">
				  <label for="jbTaskForm-created" class="control-label col-sm-4" title="<fmt:message key="jsp.document.created.title"/>"><fmt:message key="jsp.document.created.label" /></label>
				  <div class="col-sm-8">
					<p class="form-control-static">{{documentEditing.created | date: '${localePatternTimestamp}'}}</p>
				  </div>
				</div>

				<div ng-if="currentRownum != null" class="row jb-form-group">
				  <label for="jbTaskForm-createdBy" class="control-label col-sm-4" title="<fmt:message key="jsp.document.createdBy.title"/>"><fmt:message key="jsp.document.createdBy.label" /></label>
				  <div class="col-sm-8">
				 	 <p class="form-control-static">{{documentEditing.createdBy}}</p>
				  </div>
				</div> -->
          <!-- Proprietà aggiuntive -->
          <div class="row jb-form-group" ng-repeat="(i, p) in currentTaskForm ">
            <label for="jbTaskForm-p.name" class="control-label col-sm-4" title="{{p.name}}">{{p.title}}</label>
            <div class="col-sm-8">

			  <!-- STRING -->
              <div ng-if="jbWorkflowUtil.decodeType(p.dataType) == 'STRING'">
                <div ng-if="!readOnly" ng-class="getValidClass(jbTaskForm[p.name])">
                  <input ng-if="jbUtil.isEmptyObject(p.allowedValues)" ng-required="p.required" id="jbTaskForm-{{p.name}}"
                    class="form-control" title="{{p.name}}" type="text" name="{{p.name}}" ng-model="updatedVariables[p.name].value"
                  />
                  <ng-include src="'views/process/select.html'"></ng-include>
                  <label class="text-danger" ng-show="jbValidate.showMessag.e(jbTaskForm[p.name])">
                    <fmt:message key="js.validate.required" />
                  </label>
                </div>
                <div ng-if="readOnly || p.linkType">
							<p ng-if="!p.linkType" class="form-control-static">{{updatedVariables[p.name].value}}</p>
							</div>
              </div>
              <!--END STRING -->
              
              <!-- INTEGER -->
              <div ng-if="jbWorkflowUtil.decodeType(p.dataType) == 'INTEGER'">
                <div ng-if="!readOnly" ng-class="getValidClass(jbTaskForm[p.name])">
                  <input ng-if="jbUtil.isEmptyObject(p.allowedValues)" ng-required="p.required" id="jbTaskForm-{{p.name}}" class="form-control" title="{{p.name}}"
                    type="text" name="{{p.name}}" ng-model="updatedVariables[p.name].value" ng-pattern="jbPatterns.number(0)"
                    jb-number="0" />
                  <ng-include src="'views/process/select.html'"></ng-include>
                  <label class="text-danger" ng-show="jbValidate.showMessage(jbTaskForm[p.name])">
                    <fmt:message key="js.validate.number" />
                  </label>
                </div>
                <div ng-if="readOnly">
                  <p class="form-control-static">{{updatedVariables[p.name].value}}</p>
                </div>
              </div>
              <!-- END INTEGER -->

			  <!-- BOOLEAN -->
              <div ng-if="jbWorkflowUtil.decodeType(p.dataType) == 'BOOLEAN'">
                <div ng-if="!readOnly">
                  <select ng-required="p.required" id="jbTaskForm-{{p.name}}" class="form-control" title="{{p.name}}"
                    name="{{p.name}}" ng-model="updatedVariables[p.name].value" jb-boolean>
                    <option></option>
                    <option value="true">
                      <fmt:message key="jsp.boolean.1" />
                    </option>
                    <option value="false">
                      <fmt:message key="jsp.boolean.0" />
                    </option>
                  </select>
                  <label class="text-danger" ng-show="jbValidate.showMessage(jbTaskForm[p.name])">
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
			  <!-- END BOOLEAN -->

              <!-- DECIMAL -->
              <div ng-if="jbWorkflowUtil.decodeType(p.dataType) == 'DECIMAL'">
                <div ng-if="!readOnly" ng-class="getValidClass(jbTaskForm[p.name])">
                  <input ng-required="p.required" id="jbTaskForm-{{p.name}}" class="form-control" title="{{p.name}}"
                    type="text" name="{{p.name}}" ng-model="updatedVariables[p.name].value" ng-pattern="jbPatterns.number(2)"
                    jb-number="2" />
                  <label class="text-danger" ng-show="jbValidate.showMessage(jbTaskForm[p.name])">
                    <fmt:message key="js.validate.number" />
                  </label>
                </div>
                <div ng-if="readOnly">
                  <p class="form-control-static">{{updatedVariables[p.name].value}}</p>
                </div>
              </div>
              <!-- END DECIMAL -->

			  <!-- DATETIME -->
              <div ng-if="jbWorkflowUtil.decodeType(p.dataType) == 'DATETIME'">
                <div class="input-group" ng-if="!readOnly">
                  <input ng-required="p.required" id="jbTaskForm-{{p.name}}" class="form-control" title="{{p.name}}"
                    type="text" name="{{p.name}}" readonly uib-datepicker-popup="${localePatternTimestamp}" ng-model="updatedVariables[p.name].value"
                    is-open="calendarPopups['jbTaskForm-'+ p.name]" />
                  <span class="input-group-btn">
                    <button type="button" class="btn btn-default" ng-click="openCalendar('jbTaskForm-'+ p.name)">
                      <i class="fa fa-calendar"></i>
                    </button>
                  </span>
                  <label class="text-danger" ng-show="jbValidate.showMessage(jbTaskForm[p.name])">
                    <fmt:message key="js.validate.required" />
                  </label>
                </div>
                <div ng-if="readOnly">
                  <p class="form-control-static">{{updatedVariables[p.name].value | date: '${localePatternTimestamp}'}}</p>
                </div>
              </div>
              <!-- END DATETIME -->

			  <!-- DATE -->
              <div ng-if="jbWorkflowUtil.decodeType(p.dataType) == 'DATE'">
                <div class="input-group" ng-if="!readOnly">
                  <input ng-required="p.required" id="jbTaskForm-{{p.name}}" class="form-control" title="{{p.name}}"
                    type="text" name="{{p.name}}" readonly uib-datepicker-popup="${localePatternDate}" ng-model="updatedVariables[p.name].value"
                    is-open="calendarPopups['jbTaskForm-'+ p.name]" />
                  <span class="input-group-btn">
                    <button type="button" class="btn btn-default" ng-click="openCalendar('jbTaskForm-'+ p.name)">
                      <i class="fa fa-calendar"></i>
                    </button>
                  </span>
                  <label class="text-danger" ng-show="jbValidate.showMessage(jbTaskForm[p.name])">
                    <fmt:message key="js.validate.required" />
                  </label>
                </div>
                <div ng-if="readOnly">
                  <p class="form-control-static">{{updatedVariables[p.name].value | date: '${localePatternDate}'}}</p>
                </div>
              </div>

            </div>
          </div>
          <!-- Fine Proprietà aggiuntive -->

        </form>
        <!-- Fine informazioni di dettaglio -->

        <!-- Versioni documento -->
        <!-- 			<br>
			<div ng-if="currentRownum != null" >
				<div class="row jb-toolbar">
					<div class="col-sm-6">
						<h4><fmt:message key="jsp.document.version.sidemenu"/></h4>
					</div>
		  			<div class="col-sm-6 " ng-if="!readOnly">
		  				<div class="pull-right">
		  					<button class="btn btn-success btn-sm" type="button" ng-click="startContentReplace()"><i class="fa fa-plus"></i> <fmt:message key="jsp.document.update"/></button>
		  				</div>
					</div>
		  		</div>

				<table class="table table-hover">
				<tbody>
					<tr ng-repeat="dv in documentVersions" ng-if="$index < 10">
						<td>
							<span ng-show="dv.isLatestVersion" class="label label-success">{{dv.version}}</span>
							<span ng-hide="dv.isLatestVersion" class="label label-default">{{dv.version}}</span>
						</td>
						<td><a ng-href="a/Document/{{dv.id}}/content" target="_blank">{{dv.lastModificationDate | date: '${localePatternTimestamp}'}}</a></td>
						<td>{{dv.lastModifiedBy}}</td>
					</tr>
				</tbody>
				</table>
				<!-- Fine Versioni documento -->

        <!-- Documenti collegati -->
        <!-- 		<div class="row jb-toolbar">
					<div class="col-sm-6">
						<h4><fmt:message key="jsp.document.relationship"/></h4>
					</div>
		  		</div>
		  		<!-- Relazioni possibili -->
        <!-- 			<ul ng-repeat="rt in documentTypeEdit.relationTypes">
					<li>{{getRelationName(rt)}}
					<br>
					<table class="table table-hover">
					<tbody><!-- relazioni esistenti per il tipo -->
        <!-- 						<tr ng-repeat="r in documentEditing.relationships | filter:isRelationOfType(rt, 'source', documentEditing.id)">
							<td><a ng-click="showDocument({id: r.target.id, name: r.target.name, description: r.target.description})" href>{{r.target.description}}</a></td>
							<td>{{r.target.createdBy}}</td>
							<td ng-hide="readOnly" ng-click="delRelation(r.id)" confirm="{{jbMessages.confirmDeleteRel}}"><i class="fa fa-lg fa-times text-danger"></i></td>
						</tr>
						<tr ng-repeat="r in documentEditing.relationships | filter:isRelationOfType(rt, 'target', documentEditing.id)">
							<td><a ng-click="showDocument({id: r.source.id, name: r.source.name, description: r.source.description})" href>{{r.source.description}}</a></td>
							<td>{{r.source.createdBy}}</td>
							<td ng-hide="readOnly" ng-click="delRelation(r.id)" confirm="{{jbMessages.confirmDeleteRel}}"><i class="fa fa-lg fa-times text-danger"></i></td>
						</tr>
						<tr ng-if="!readOnly && isTypeSpecific(rt, documentTypeEdit.id)">
							<td colspan="3">
								<button class="btn btn-success btn-sm" type="button" ng-click="startAddRelation(rt)"><i class="fa fa-plus"></i> <fmt:message key="jsp.document.addRelationship"/></button>
							</td>
						</tr>
					</tbody>
					</table>
					</li>
				</ul>
				<!-- Fine Documenti collegati -->
        <!-- 			</div>-->
      </div>
         <!-- Preview -->
    <div ng-if="currentTaskItems.length > 0" class="col-sm-8">
     	<uib-tabset active="active">
    <uib-tab index="$index" ng-repeat="tab in currentTaskItems" heading="{{tab.name}}" disable="tab.disabled">
    <h3>{{tab.name}}</h3>
        <div  ng-bind-html="getDocumentObjectHTML(tab) | trusted"></div>
    </uib-tab>
    </uib-tabset>
    
      </div> 
      <!-- Fine Preview -->
      <!-- Fine Dettaglio -->
    </div>
  </div>
  <!-- datail finish -->