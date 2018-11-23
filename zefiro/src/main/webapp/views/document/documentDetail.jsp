<!-- datail start -->
<%@ include file="/include/directive.jsp" %>
<html ng-app="main" ng-controller="MainController">
<div class="container-fluid">

	<ol class="breadcrumb">
		<li><a href ng-click="gotoDocumentBreadcrumb(-1, jbDetailFormDocument)"><fmt:message key="jsp.document.label"/></a></li>
		<li class="active" ng-show="currentRownum == null"><fmt:message key="jsp.document.insert"/></li>
		<li ng-repeat="d in documentBreadcrumbs">
		  <a ng-if="!$last" href ng-click="gotoDocumentBreadcrumb($index, jbDetailFormDocument)">{{d.description}}</a>
		  <span ng-if="$last">{{d.description}}</span>
		</li>
	</ol>
	<div class="page-header ">
		<div class="pull-right">
			<button ng-if="!isReadOnly()" class="btn btn-primary" type="button" ng-click="jbValidate.checkForm(jbDetailFormDocument) && saveDetail(jbDetailFormDocument)">
				<span ng-hide="currentRownum == null"><i class="fa fa-floppy-o"></i> <fmt:message key="jsp.detail.update.submit"/></span>
				<span ng-show="currentRownum == null"><i class="fa fa-floppy-o"></i> <fmt:message key="jsp.detail.insert.submit"/></span>
			</button>
			<button ng-hide="currentRownum == null" class="btn btn-default" type="button" ng-click="jbDetailFormDocument.print()"><i class="fa fa-download"></i> <fmt:message key="jsp.detail.download"/></button>
			<button class="btn btn-default" type="button" ng-click="gotoDocumentBreadcrumb(breadCrumbIndex-1, jbDetailFormDocument)"><i class="fa fa-times"></i> <fmt:message key="js.dialog.back"/></button>
		</div>
			<span ng-hide="currentRownum == null"><h1><b>{{documentEditing.description}}</b></h1></span>
			<span ng-show="currentRownum == null"><h1><b><fmt:message key="jsp.detail.insert.submit"/></b></h1></span>
	</div>
	
	<div class="row form-group">
		<!-- Preview -->
		<div class="col-sm-8">
			
			<div  ng-if="currentFileName != null" ng-bind-html="getDocumentObjectHTML() | trusted">
			
			</div>
		</div>
		<!-- Fine Preview -->
		<!-- Dettaglio -->
		<div class="col-sm-4">
			<!-- Informazioni di dettaglio -->
			<form name="jbDetailFormDocument" class="form-horizontal" novalidate jb-print="a/Document/{{documentEditing.id}}/content">
				<input ng-if="currentRownum != null" type="hidden" name="id" ng-model="documentEditing.id"/>
			
				<div ng-if="currentRownum == null" class="row jb-form-group">
					<label for="jbDetailFormDocument-file" class="control-label col-sm-4" title="<fmt:message key="jsp.document.file.title"/>"><fmt:message key="jsp.document.file.title"/></label>
					<div class="col-sm-8">
						<div class="input-group">
						 <input id="jbDetailFormDocument-file" class="form-control" title="<fmt:message key="jsp.document.name.title"/>" type="file" name="file" jb-upload="setCurrentFileName(newUrl, uploaded, userFilename)" />
						 <span class="input-group-btn">
						   <button class="btn btn-default" type="button" promise-btn="uploadPromise"><i class="fa fa-upload"></i> Upload</button>
						 </span>
						</div>
					</div>
				</div>
				
				<div class="row jb-form-group">
				  <label for="jbDetailFormDocument-name" class="control-label col-sm-4" title="<fmt:message key="jsp.document.name.title"/>"><fmt:message key="jsp.document.name.label" /></label>
				  <div class="col-sm-8">
				  <p ng-if="isReadOnly()" class="form-control-static">{{documentEditing.name}}</p>
				  	<input  ng-if="!isReadOnly()" ng-required="true" ng-if="!readOnly" id="jbDetailFormDocument-name" class="form-control" title="<fmt:message key="jsp.document.name.title"/>" type="text" name="name" ng-model="documentEditing.name" pattern="^\w+[\w\s\.-]*$"/>
				  	<label class="text-danger" ng-show="jbValidate.showMessage(jbDetailFormDocument.name)"><fmt:message key="js.validate.filename"/></label>
				  </div>
				</div>
				
				<div class="row jb-form-group">
				  <label for="jbDetailFormDocument-description" class="control-label col-sm-4" title="<fmt:message key="jsp.document.description.title"/>"><fmt:message key="jsp.document.description.label" /></label>
				  <div class="col-sm-8">
				   <p ng-if="isReadOnly()" class="form-control-static">{{documentEditing.description}}</p>
				  	<input ng-if="!isReadOnly()" ng-required="true" ng-if="!readOnly" id="jbDetailFormDocument-description" class="form-control" title="<fmt:message key="jsp.document.description.title"/>" type="text" name="description" ng-model="documentEditing.description"/>
				  	<label class="text-danger" ng-show="jbValidate.showMessage(jbDetailFormDocument.description)"><fmt:message key="js.validate.required"/></label>
				  </div>
				</div>
				
				<div  class="row jb-form-group">
				  <label for="jbDetailFormDocument-type" class="control-label col-sm-4" title="<fmt:message key="jsp.document.type.title"/>"><fmt:message key="jsp.document.type.label" /></label>
				  <div class="col-sm-8">
				  	<div ng-if="currentRownum != null"><p class="form-control-static">{{documentEditing.typeName}}</p></div>
				  	<div ng-if="currentRownum == null">
						<select ng-required="true" class="form-control" id="jbDetailFormDocument-type" name="type" title="<fmt:message key="jsp.document.type.title"/>" ng-model="documentEditing.type" ng-change="typeChangedInsert(jbDetailFormDocument)">
					  	  	<option></option>
					   		<option ng-repeat="t in userDocumentTypes" value="{{t.id}}">{{t.name}}</option>
					 	</select>
					</div>
					<label class="text-danger" ng-show="jbValidate.showMessage(jbDetailFormDocument.type)"><fmt:message key="js.validate.required"/></label>
				  </div>
				</div>	
				
				<div ng-if="currentRownum != null" class="row jb-form-group">
				  <label for="jbDetailFormDocument-version" class="control-label col-sm-4" title="<fmt:message key="jsp.document.version.title"/>"><fmt:message key="jsp.document.version.label" /></label>
				  <div class="col-sm-8">
				  <p class="form-control-static">{{documentEditing.version}}</p>
				  </div>
				</div>	
					
				<div ng-if="currentRownum != null" class="row jb-form-group">
				  <label for="jbDetailFormDocument-created" class="control-label col-sm-4" title="<fmt:message key="jsp.document.created.title"/>"><fmt:message key="jsp.document.created.label" /></label>
				  <div class="col-sm-8">
					<p class="form-control-static">{{documentEditing.created | date: '${localePatternTimestamp}'}}</p>  
				  </div>
				</div>
					
				<div ng-if="currentRownum != null" class="row jb-form-group">
				  <label for="jbDetailFormDocument-createdBy" class="control-label col-sm-4" title="<fmt:message key="jsp.document.createdBy.title"/>"><fmt:message key="jsp.document.createdBy.label" /></label>
				  <div class="col-sm-8">
				 	 <p class="form-control-static">{{documentEditing.createdBy}}</p>
				  </div>
				</div>
				
				<!-- Proprietà aggiuntive -->
				<div class="row jb-form-group" ng-repeat="p in documentTypeEdit.propertyList ">
				  <label for="jbDetailFormDocument-{{jbUtil.sanitize(p.queryName)}}" class="control-label col-sm-4" title="{{p.description}}">{{p.displayName}}</label>
				  <div class="col-sm-8">
					 
					  <div ng-if="p.propertyType == 'STRING'">
					  	<div ng-if="!readOnly" ng-class="jbValidate.getClass(jbDetailFormDocument[p.queryName])">
					  		
					  		<p ng-if="isReadOnly()" class="form-control-static">{{documentEditing.properties[p.queryName].value}}</p>
					  	    <input ng-required="p.required" ng-if="!isReadOnly() && jbUtil.isEmptyObject(p.choices) && !p.linkType" id="jbDetailFormDocument-{{jbUtil.sanitize(p.queryName)}}" class="form-control" title="{{p.description}}" type="text" name="{{p.queryName}}" ng-model="documentEditing.properties[p.queryName].value" />
					  	  	<select ng-required="p.required" ng-if="!isReadOnly() && !jbUtil.isEmptyObject(p.choices)" class="form-control" id="jbDetailFormDocument-{{jbUtil.sanitize(p.queryName)}}" title="{{p.description}}" name="{{p.queryName}}" ng-model="documentEditing.properties[p.queryName].value">
						        <option></option>
						    	<option ng-repeat="c in p.choices" value="{{c}}">{{c}}</option> 
						    	
						  	</select>
							<label class="text-danger" ng-show="jbValidate.showMessage(jbDetailFormDocument[p.queryName])"><fmt:message key="js.validate.required"/></label>
						</div>
						<div ng-if="readOnly || p.linkType">
							<p ng-if="!p.linkType" class="form-control-static">{{documentEditing.properties[p.queryName].value}}</p>
							<p ng-if="p.linkType" class="form-control-static">
								<a ng-click="showDocument(documentEditing.properties[p.queryName].value)">{{documentEditing.properties[queryName].value.split('|')[1]}}</a>
							</p>
						</div>
					  </div>
					  
					  <div ng-if="p.propertyType == 'INTEGER'">
					  	<div ng-if="!readOnly"  ng-class="jbValidate.getClass(jbDetailFormDocument[p.queryName])">
					  	<p ng-if="isReadOnly()" class="form-control-static">{{documentEditing.properties[p.queryName].value}}</p>
						  	<input ng-if="!isReadOnly()" ng-required="p.required" id="jbDetailFormDocument-{{jbUtil.sanitize(p.queryName)}}" class="form-control" title="{{p.description}}" type="text" name="{{p.queryName}}" ng-model="documentEditing.properties[p.queryName].value" ng-pattern="jbPatterns.number(0)" jb-number="0"/>
						 	<label class="text-danger" ng-show="jbValidate.showMessage(jbDetailFormDocument[p.queryName])"><fmt:message key="js.validate.number"/></label>
						</div>
						<div ng-if="readOnly">
							<p class="form-control-static">{{documentEditing.properties[p.queryName].value}}</p>
						</div>
					  	
					  </div>
					  
					  <div ng-if="p.propertyType == 'BOOLEAN'">
					  	<div ng-if="!readOnly">
					  		<p ng-if="isReadOnly()" class="form-control-static">{{documentEditing.properties[p.queryName].value |yesOrNo}}</p>
					  		 <select  ng-if="!isReadOnly()" ng-required="p.required" id="jbDetailFormDocument-{{jbUtil.sanitize(p.queryName)}}" class="form-control" title="{{p.description}}" name="{{p.queryName}}" ng-model="documentEditing.properties[p.queryName].value" jb-boolean>
					  			<option></option>
								<option value="true"><fmt:message key="jsp.boolean.1"/></option>
								<option value="false"><fmt:message key="jsp.boolean.0"/></option>
				  			</select> 
				  			<label class="text-danger" ng-show="jbValidate.showMessage(jbDetailFormDocument[p.queryName])"><fmt:message key="js.validate.required"/></label>
					  	</div>
				  		<div ng-if="readOnly">
							<p ng-if="documentEditing.properties[$index].value" class="form-control-static"><fmt:message key="jsp.boolean.1"/></p>
							<p ng-if="!documentEditing.properties[$index].value" class="form-control-static"><fmt:message key="jsp.boolean.0"/></p>
						</div>
					  </div>
					  
					  <div ng-if="p.propertyType == 'DECIMAL'" >
					  	<div ng-if="!readOnly" ng-class="jbValidate.getClass(jbDetailFormDocument[p.queryName])">
					  	<p ng-if="isReadOnly()" class="form-control-static">{{documentEditing.properties[p.queryName].value}}</p>
					  		<input ng-if="!isReadOnly()" ng-required="p.required" id="jbDetailFormDocument-{{jbUtil.sanitize(p.queryName)}}" class="form-control" title="{{p.description}}" type="text" name="{{p.queryName}}" ng-model="documentEditing.properties[p.queryName].value" ng-pattern="jbPatterns.number(2)" jb-number="2"/>
					  	<label class="text-danger" ng-show="jbValidate.showMessage(jbDetailFormDocument[p.queryName])"><fmt:message key="js.validate.number"/></label>
					  	</div>
					 	<div ng-if="readOnly">
							<p class="form-control-static">{{documentEditing.properties[p.queryName].value}}</p>
						</div>
					  </div>
					  
					  <div ng-if="p.propertyType == 'DATETIME'">
					  	<div class="input-group" ng-if="!readOnly">
					  		<p ng-if="isReadOnly()" class="form-control-static">{{documentEditing.properties[p.queryName].value | date: '${localePatternTimestamp}'}}</p>
					  		<input ng-if="!isReadOnly()" ng-required="p.required" id="jbDetailFormDocument-{{jbUtil.sanitize(p.queryName)}}" class="form-control" title="{{p.description}}" type="text" name="{{p.queryName}}" readonly datetime-picker="${localePatternTimestamp}" ng-model="documentEditing.properties[p.queryName].value" is-open="calendarPopups['jbDetailFormDocument-'+ jbUtil.sanitize(p.queryName)]" />
					  		<span ng-if="!isReadOnly()" class="input-group-btn">
	       						<button type="button" class="btn btn-default" ng-click="openCalendar('jbDetailFormDocument-'+ jbUtil.sanitize(p.queryName))"><i class="fa fa-calendar"></i></button>
	      					</span>
	      					<label class="text-danger" ng-show="jbValidate.showMessage(jbDetailFormDocument[p.queryName])"><fmt:message key="js.validate.required"/></label>
					  	</div>
					  	<div ng-if="readOnly">
							<p class="form-control-static">{{documentEditing.properties[p.queryName].value | date: '${localePatternTimestamp}'}}</p>
						</div>
					  </div>
					  
					  <div ng-if="p.propertyType == 'DATE'">
					  	<div class="input-group" ng-if="!readOnly">
					  		<p ng-if="isReadOnly()" class="form-control-static">{{documentEditing.properties[p.queryName].value | date: '${localePatternTimestamp}'}}</p>
					  		<input  ng-if="isReadOnly()" ng-required="p.required" id="jbDetailFormDocument-{{jbUtil.sanitize(p.queryName)}}" class="form-control" title="{{p.description}}" type="text" name="{{p.queryName}}" readonly uib-datepicker-popup="${localePatternDate}" ng-model="documentEditing.properties[p.queryName].value" is-open="calendarPopups['jbDetailFormDocument-'+ jbUtil.sanitize(p.queryName)]"/>
					  		<span class="input-group-btn">
	       						<button type="button" class="btn btn-default" ng-click="openCalendar('jbDetailFormDocument-'+ jbUtil.sanitize(p.queryName))"><i class="fa fa-calendar"></i></button>
	      					</span>
	      					<label class="text-danger" ng-show="jbValidate.showMessage(jbDetailFormDocument[p.queryName])"><fmt:message key="js.validate.required"/></label>
					  	</div>
					  	<div ng-if="readOnly">
							<p class="form-control-static">{{documentEditing.properties[p.queryName].value | date: '${localePatternTimestamp}'}}</p>
						</div>
					  </div>
				  	
				  </div>
			
				</div>
				<!-- Fine Proprietà aggiuntive -->
			 
			</form>
			<!-- Fine informazioni di dettaglio -->
			
			<!-- Versioni documento -->
			<br>
			<div ng-if="currentRownum != null" >
				<div class="row jb-toolbar" >
					<div class="col-sm-6">
						<h4><fmt:message key="jsp.document.version.sidemenu"/></h4>
					</div>
		  			<div  class="col-sm-6 ">
		  				<div  ng-if="!isReadOnly()" class="pull-right">
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
				<div class="row jb-toolbar">
					<div class="col-sm-6">
						<h4><fmt:message key="jsp.document.relationship"/></h4>
					</div>
		  		</div>
		  		<!-- Relazioni possibili -->
				<ul ng-repeat="rt in documentTypeEdit.relationTypes">
					<li>{{getRelationName(rt)}}
					<br>
					<table class="table table-hover">
					<tbody><!-- relazioni esistenti per il tipo -->
						<tr ng-repeat="r in documentEditing.relationships | filter:isRelationOfType(rt, 'source', documentEditing.id)">
							<td><a ng-click="showDocument({id: r.target.id, name: r.target.name, description: r.target.description})" href>{{r.target.description}}</a></td>
							<td>{{r.target.createdBy}}</td>
							<td ng-if="readOnly" ng-hide="readOnly" ng-click="delRelation(r.id)" confirm="{{jbMessages.confirmDeleteRel}}"><i class="fa fa-lg fa-times text-danger"></i></td>
						</tr>
						<tr ng-repeat="r in documentEditing.relationships | filter:isRelationOfType(rt, 'target', documentEditing.id)">
							<td><a ng-click="showDocument({id: r.source.id, name: r.source.name, description: r.source.description})" href>{{r.source.description}}</a></td>
							<td>{{r.source.createdBy}}</td>
							<td ng-if="readOnly" ng-hide="readOnly" ng-click="delRelation(r.id)" confirm="{{jbMessages.confirmDeleteRel}}"><i class="fa fa-lg fa-times text-danger"></i></td>
						</tr>
						<tr ng-if="readOnly && isTypeSpecific(rt, documentTypeEdit.id)">
							<td colspan="3">
								<button class="btn btn-success btn-sm" type="button" ng-click="startAddRelation(rt)"><i class="fa fa-plus"></i> <fmt:message key="jsp.document.addRelationship"/></button>
							</td>
						</tr>
					</tbody>
					</table>
					</li>
				</ul>
				<!-- Fine Documenti collegati -->
			</div>
		</div>
		<!-- Fine Dettaglio -->
	</div>
</div>
<!-- datail finish -->