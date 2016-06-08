<!-- datail start -->
<%@ include file="/include/directive.jsp" %>

<div class="container-fluid">

	<ol class="breadcrumb">
		<li><a href ng-click="closeContentReplace(jbContentReplaceFormDocument); gotoDocumentBreadcrumb(-1, jbDetailFormDocument)"><fmt:message key="jsp.document.label"/></a></li>
		<li class="active" ng-show="currentRownum == null"><fmt:message key="jsp.document.insert"/></li>
		<li ng-repeat="d in documentBreadcrumbs">
		  <a ng-if="!$last" href ng-click="gotoDocumentBreadcrumb($index, jbDetailFormDocument)">{{d.description}}</a>
		  <span ng-if="$last">{{d.description}}</span>
		</li>
	</ol>
	<div class="page-header ">
		<div class="pull-right">
			<button ng-if="!readOnly" class="btn btn-primary" type="button" ng-click="jbValidate.checkForm(jbContentReplaceFormDocument) && updateContent(jbContentReplaceFormDocument)">
				<span><i class="fa fa-floppy-o"></i> <fmt:message key="js.dialog.crea"/></span>
			</button>
			<button class="btn btn-default" type="button" ng-click="closeContentReplace(jbContentReplaceFormDocument,true)"><i class="fa fa-times"></i> <fmt:message key="js.dialog.annulla"/></button>
		</div>
			<span><h1><b>{{documentEditing.name}}</b></h1></span>
	</div>
	
	<div class="row form-group">
		<!-- Preview -->
		<div class="col-sm-8">
			
			<div ng-if="currentFileName != null" ng-bind-html="getDocumentObjectHTML() | trusted">
			
			</div>
		</div>
		<!-- Fine Preview -->
		<!-- Dettaglio -->
		<div class="col-sm-4">
			<!-- Informazioni di dettaglio -->
			<form name="jbContentReplaceFormDocument" class="form-horizontal" novalidate jb-print="a/Document/{{documentEditing.id}}/content">
				<input type="hidden" name="id" ng-model="documentEditing.id"/>
			
				<div class="row jb-form-group">
					<label for="jbContentReplaceFormDocument-file" class="control-label col-sm-4" title="<fmt:message key="jsp.document.file.title"/>"><fmt:message key="jsp.document.file.title"/></label>
					<div class="col-sm-8">
						<div class="input-group">
						 <input ng-required="true" id="jbContentReplaceFormDocument-file" class="form-control" title="<fmt:message key="jsp.document.file.title"/>" type="file" name="file" jb-upload="setCurrentFileName(newUrl, uploaded, userFilename)"/>
						 <span class="input-group-btn">
						   <button class="btn btn-default" type="button" ng-click=""><i class="fa fa-upload"></i> Upload</button>
						 </span>
						</div>
						<label class="text-danger" ng-show="jbValidate.showMessage(jbContentReplaceFormDocument.file)"><fmt:message key="js.validate.required"/></label>
					</div>
				</div>
				
				<div class="row jb-form-group">
				  <label for="jbContentReplaceFormDocument-comment" class="control-label col-sm-4" title="<fmt:message key="jsp.document.comment.title"/>"><fmt:message key="jsp.document.comment.label" /></label>
				  <div class="col-sm-8">
				  	<input ng-required="true" id="jbContentReplaceFormDocument-comment" class="form-control" title="<fmt:message key="jsp.document.comment.title"/>" type="text" name="comment" ng-model="documentContentReplace.comment"/>
				 	<label class="text-danger" ng-show="jbValidate.showMessage(jbContentReplaceFormDocument.comment)"><fmt:message key="js.validate.required"/></label>
				  </div>
				</div>
				
				<div  class="row jb-form-group">
				  <label for="jbContentReplaceFormDocument-version" class="control-label col-sm-4" title="<fmt:message key="jsp.document.version.title"/>"><fmt:message key="jsp.document.version.label" /></label>
				  <div class="col-sm-8">
					<select ng-required="true" class="form-control" id="jbContentReplaceFormDocument-version" name="version" title="<fmt:message key="jsp.document.version.title"/>" ng-model="documentContentReplace.version">
				  	  	<option value="MAJOR">Major</option>
				   		<option Value="MINOR">Minor</option>
				 	</select>
				 	<label class="text-danger" ng-show="jbValidate.showMessage(jbContentReplaceFormDocument.version)"><fmt:message key="js.validate.required"/></label>
				  </div>
				</div>	
			 
			</form>
			<!-- Fine informazioni di dettaglio -->
			
		</div>
		<!-- Fine Dettaglio -->
	</div>
</div>
<!-- datail finish -->