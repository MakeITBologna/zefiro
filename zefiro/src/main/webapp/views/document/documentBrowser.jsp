<%@ include file="/include/directive.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<div class="container-fluid" ng-if="isUserLogged()">
<div class="row" ng-hide="editing || contentReplace">
  <div class="page-header jb-page-header">
    <div class="pull-right" >
  		<button ng-hide="relation" ng-if="!isReadOnly()" type="button" class="btn btn-success" title="<fmt:message key="jsp.results.insert.user"/>" ng-click="startInsert()"><i class="fa fa-plus"></i> <fmt:message key="jsp.document.insert"/></button>
  	  	<button ng-show="relation" type="button" class="btn btn-danger" title="<fmt:message key="js.dialog.annulla"/>" ng-click="closeAddRelation()"><i class="fa fa-times"></i> <fmt:message key="js.dialog.annulla"/></button>
  	</div>
  	<h1><fmt:message key="jsp.document.label"/></h1>
  </div>  
  <div id="query">
	<div class="panel">
	  <div class="panel-body">
		<form name="jbSearchFormDocument" class="form-horizontal well" novalidate jb-print="a/Search/print">
		<input type="hidden" id="jbSearchFormDocument-propertyNames" name="propertyNames" value="{{documentTemplate.propertyNames}}" />				
		    <div class="row jb-form-group">
		    
		      <div class="col-sm-2"></div>
			  
			  <label class="col-sm-1 control-label" for="jbSearchFormDocument-type" title="<fmt:message key="jsp.document.type.title"/>"><fmt:message key="jsp.document.type.label" /></label>
			  <div class="col-sm-3">
				  <select  ng-disabled="relation" class="form-control" id="jbSearchFormDocument-type" name="type" title="<fmt:message key="jsp.document.type.title"/>" ng-model="documentTemplate.type" ng-change="typeChanged(jbSearchFormDocument)">
				    <option></option>
				    <option ng-repeat="t in userDocumentTypes" value="{{t.id}}">{{t.name}}</option>
				  </select>
			  </div>
			  
			  <label class="col-sm-1 control-label" for="jbSearchFormDocument-description" title="<fmt:message key="jsp.document.description.title"/>"><fmt:message key="jsp.document.description.label" /></label>
			  <div class="col-sm-3">
			  	<input class="form-control" id="jbSearchFormDocument-description" title="<fmt:message key="jsp.document.description.title"/>" type="text" name="description" ng-model="documentTemplate['cmis:description']"/>
			  </div>
			  
			  <div class="col-sm-2"></div>
			  
			</div>
			 
			<div class="jb-hr"></div>
			
			<div class="row jb-form-group" ng-repeat="r in searchMatrix">
			
			<div class="col-sm-2"></div>
			
			<any ng-repeat="p in r" ng-switch="p.propertyType">
			
			  <div ng-switch-when="INTEGER">
				<label class="col-sm-1 control-label" for="jbSearchFormDocument-{{jbUtil.sanitize(p.queryName)}}-FROM" title="{{p.description}}">{{p.displayName}}</label>
				<div class="col-sm-3">
					<div class="row">
						<div class="col-sm-6">
							<div class="input-group">
						  	    <span class="input-group-addon"><fmt:message key="jsp.from.label"/></span>
							    <input class="form-control" id="jbSearchFormDocument-{{jbUtil.sanitize(p.queryName)}}-FROM" title="{{p.description}} <fmt:message key="jsp.from.label"/>" type="text" name="{{p.queryName}}|GE" ng-model="documentTemplate[p.queryName+'|GE']" ng-pattern="jbPatterns.number(0)"/>
							</div>
						</div>
						<div class="col-sm-6">
					  	  <div class="input-group">
					  	    <span class="input-group-addon"><fmt:message key="jsp.to.label"/></span>
						    <input class="form-control" id="jbSearchFormDocument-{{jbUtil.sanitize(p.queryName)}}-TO" title="{{p.description}} <fmt:message key="jsp.to.label"/>" type="text" name="{{p.queryName}}|LE" ng-model="documentTemplate[p.queryName+'|LE']" ng-pattern="jbPatterns.number(0)"/>
						  </div>
						</div>
					</div>
				</div>
			  </div>
			
			  <div ng-switch-when="DECIMAL">
				<label class="col-sm-1 control-label" for="jbSearchFormDocument-{{jbUtil.sanitize(p.queryName)}}-FROM" title="{{p.description}}">{{p.displayName}}</label>
				<div class="col-sm-3">
					<div class="row">
						<div class="col-sm-6">
						  <div class="input-group">
					  	    <span class="input-group-addon"><fmt:message key="jsp.from.label"/></span>
						    <input class="form-control" id="jbSearchFormDocument-{{jbUtil.sanitize(p.queryName)}}-FROM" title="{{p.description}} <fmt:message key="jsp.from.label"/>" type="text" name="{{p.queryName}}|GE" ng-model="documentTemplate[p.queryName+'|GE']" ng-pattern="jbPatterns.number(2)"/>
						  </div>
						</div>
						<div class="col-sm-6">
						  <div class="input-group">
					  	    <span class="input-group-addon"><fmt:message key="jsp.to.label"/></span>
						    <input class="form-control" id="jbSearchFormDocument-{{jbUtil.sanitize(p.queryName)}}-TO" title="{{p.description}} <fmt:message key="jsp.to.label"/>" type="text" name="{{p.queryName}}|LE" ng-model="documentTemplate[p.queryName+'|LE']" ng-pattern="jbPatterns.number(2)"/>
						  </div>
						</div>
					</div>
				</div>
			  </div>
			
			  <div ng-switch-when="DATETIME">
			    <label class="col-sm-1 control-label" for="jbSearchFormDocument-{jbUtil.sanitize(p.queryName)}}-FROM" title="{{p.description}}">{{p.displayName}}</label>
				<div class="col-sm-3">
					<div class="row">
						<div class="col-sm-6">
					  	  <div class="input-group">
					  	    <span class="input-group-addon"><fmt:message key="jsp.from.label"/></span>
					        <input id="jbSearchFormDocument-{{jbUtil.sanitize(p.queryName)}}-FROM" class="form-control" title="{{p.displayName}} <fmt:message key="jsp.from.label"/>" type="text" name="{{p.queryName}}|GE" ng-model="documentTemplate[p.queryName+'|GE']" uib-datepicker-popup="${localePatternDate}" is-open="calendarPopups['jbSearchFormDocument-'+jbUtil.sanitize(p.queryName)+'-FROM']"/>
			                <span class="input-group-btn">
			                  <button type="button" class="btn btn-default" ng-click="openCalendar('jbSearchFormDocument-'+jbUtil.sanitize(p.queryName)+'-FROM')"><i class="fa fa-calendar"></i></button>
			                </span>
			              </div>
						</div>
						<div class="col-sm-6">
						  <div class="input-group">
			        			<span class="input-group-addon"><fmt:message key="jsp.to.label"/></span>
						        <input id="jbSearchFormDocument-{{jbUtil.sanitize(p.queryName)}}-TO" class="form-control" title="<fmt:message key="jsp.document.{{p.queryName}}to.title"/> <fmt:message key="jsp.to.label"/>" type="text" name="{{p.queryName}}|LE" ng-model="documentTemplate[p.queryName+'|LE']" uib-datepicker-popup="${localePatternDate}" is-open="calendarPopups['jbSearchFormDocument-'+jbUtil.sanitize(p.queryName)+'-TO']"/>
				                <span class="input-group-btn">
				                  <button type="button" class="btn btn-default" ng-click="openCalendar('jbSearchFormDocument-'+jbUtil.sanitize(p.queryName)+'-TO')"><i class="fa fa-calendar"></i></button>
				                </span>
			               </div>
						</div>
	              </div>
	            </div>
			  </div>
			  
			  <div ng-switch-when="BOOLEAN">
				<label class="col-sm-1 control-label" for="jbSearchFormDocument-{{jbUtil.sanitize(p.queryName)}}" title="{{p.description}}">{{p.displayName}}</label>
				<div class="col-sm-3">
				  <select class="form-control" id="jbSearchFormDocument-{{jbUtil.sanitize(p.queryName)}}" title="{{p.description}}" name="{{p.queryName}}" ng-model="documentTemplate[p.queryName]" jb-boolean>
				    <option></option>
				    <option value="true"><fmt:message key="jsp.boolean.1"/></option>
					<option value="false"><fmt:message key="jsp.boolean.0"/></option>
				  </select>
				</div>
			  </div>
			
			  <div ng-switch-when="STRING">
				<label class="col-sm-1 control-label" for="jbSearchFormDocument-{{jbUtil.sanitize(p.queryName)}}" title="{{p.description}}">{{p.displayName}}</label>
				<div class="col-sm-3">
				  <input ng-if="jbUtil.isEmptyObject(p.choices) && !p.suggestBox" class="form-control" id="jbSearchFormDocument-{{jbUtil.sanitize(p.queryName)}}" title="{{p.description}}" type="text" name="{{p.queryName}}" ng-model="documentTemplate[p.queryName]"/>
		    	  <input ng-if="jbUtil.isEmptyObject(p.choices) && p.suggestBox" ng-controller="TypeaheadCtrl" type="text" ng-model="documentTemplate[p.queryName]" uib-typeahead="suggestion for suggestion in getSuggestions($viewValue, documentTemplate.type, p.name)" 
		    		class="form-control" id="jbSearchFormDocument-{{jbUtil.sanitize(p.queryName)}}" title="{{p.description}}" type="text" name="{{p.queryName}}" ng-model="documentTemplate[p.queryName]" autocomplete="off"/>
				  <select ng-if="!jbUtil.isEmptyObject(p.choices)" class="form-control" id="jbSearchFormDocument-{{jbUtil.sanitize(p.queryName)}}" title="{{p.description}}" name="{{p.queryName}}" ng-model="documentTemplate[p.queryName]">
				    <option></option>
				    <option ng-repeat="c in p.choices" value="{{c}}">{{c}}</option>
				  </select>
				</div>
			  </div>
			
			</any>
			<div class="col-sm-2"></div>
			</div>
			
			<div class="row form-group">
			
			  <div class="col-sm-2"></div>
			  
			  <label class="col-sm-1 control-label" for="jbSearchFormDocument-contains" title="<fmt:message key="jsp.document.content.title"/>"><fmt:message key="jsp.document.content.label"/></label>
			  <div class="col-sm-7">
			  	<input class="form-control" id="jbSearchFormDocument-contains" title="<fmt:message key="jsp.document.content.title"/>" type="text" name="contains" ng-model="documentTemplate.contains"/>
			  </div>
			  
			  <div class="col-sm-2"></div>
			
			</div>
						
            <div class="row jb-form-group" align="center">
        	  <button type="submit" class="btn btn-primary" ng-click="jbValidate.checkForm(jbSearchFormDocument) && search()" promise-btn><i class="fa fa-search"></i> <fmt:message key="jsp.query.submit"/></button>
        	  <button type="button" class="btn btn-default" ng-click="clearSearch(jbSearchFormDocument)"><i class="fa fa-eraser"></i> <fmt:message key="jsp.query.reset"/></button>
        	</div>
        	
  		</form>
  	  </div>
  	</div>
  </div>
  <!-- query finish -->
  <!-- results start -->
  <div>
	<div class="panel">
	  <div class="panel-body">

		<div class="row jb-toolbar">
			<div class="col-sm-3">
				<h4>{{documentTable.total()}} <fmt:message key="jsp.document.label"/> <any ng-if="!jbUtil.isEmptyObject(documentType)">( {{documentType.name}} )</any></h4>
			</div>
  			<div class="col-sm-9 ">
  				<div class="pull-right">
					<!-- <button type="button" class="btn btn-default btn-sm" title="<fmt:message key="jsp.results.export.pdf"/>" ng-click="jbSearchFormDocument.print('pdf')"><i class="fa fa-file-pdf-o fa-lg"></i> <fmt:message key="jsp.results.export.pdf"/></button>-->
					<button ng-show="!relation" type="button" class="btn btn-default btn-sm" title="<fmt:message key="jsp.results.export.xls"/>" ng-click="jbSearchFormDocument.print('xls')"><i class="fa fa-file-excel-o fa-lg"></i> <fmt:message key="jsp.results.export.xls"/></button>
  				</div>
			</div>
  		</div>

	    <table ng-table="documentTable" template-header="views/document/documentHeaders.jsp" class="table table-condensed table-bordered table-striped ng-table-responsive" show-filter="false">
		<tbody>
		<!-- ng-dblclick definisce il comportamento all'utilizzo di doppio click sugli elementi. !relation permette di disattivare questa funzionaliï¿½ nella modalitï¿½ di selezione delle relazioni per i documenti -->
		<tr ng-repeat="row in $data track by $index" ng-dblclick="!relation && startEdit($index)">
		 
	      <td ng-if="jbUtil.isEmptyObject(documentType)" sortable="'typeName'">{{row.typeName}}</td>
	      <td sortable="'description'">
	        <span ng-if="row.properties['cmis:contentStreamId'] && row.properties['cmis:contentStreamId'].value !==null ">
	      	  <a ng-href="a/Document/{{row.id}}/content" target="_blank">{{row.description}}</a>
	        </span>
	        <span ng-else>{{row.description}}</span></td>
	      <td ng-if="jbUtil.isEmptyObject(documentType)" sortable="'createdBy'">{{row.createdBy}}</td>
          <td class="text-right" sortable="'created'">{{row.created | date: '${localePatternDate}'}}</td>

          <td ng-repeat-start="p in documentType.propertyList" ng-if="p.queryable && p.propertyType == 'INTEGER'" class="text-right" sortable="'\''+p.queryName+'\''">{{row[p.queryName] | number:0}}</td>
          <td ng-if="p.queryable && p.propertyType == 'DECIMAL'" class="text-right" sortable="'\''+p.queryName+'\''">{{row[p.queryName] | number:2}}</td>
          <td ng-if="p.queryable && p.propertyType == 'DATETIME'" class="text-right" sortable="'\''+p.queryName+'\''">{{row[p.queryName] | date: '${localePatternDate}'}}</td>
          <td ng-if="p.queryable && p.propertyType == 'BOOLEAN'" sortable="'\''+p.queryName+'\''"><span ng-if="row[p.queryName] === true"><fmt:message key="jsp.boolean.1"/></span><span ng-if="row[p.queryName] === false"><fmt:message key="jsp.boolean.0"/></span></td>
          <td ng-repeat-end ng-if="p.queryable && p.propertyType == 'STRING'" sortable="'\''+p.queryName+'\''" ng-class="p.statusBadge? 'jb-center':''">
            <a ng-if="p.linkType && !p.statusBadge" href ng-click="showDocument(row[p.queryName])">{{row[p.queryName].split('|')[1]}}</a>
            <any ng-if="!p.linkType && !p.statusBadge">{{row[p.queryName]}}</any>
            <span ng-if="p.statusBadge" class="label" ng-class="getBadgeClass(p.queryName, row[p.queryName])">{{row[p.queryName]}}</any>
          </td>
          
          <td>
<div class="btn-group" uib-dropdown ng-show="row.id != null && !relation">
	          <button type="button" class="btn btn-primary btn-xs" ng-click="startEdit($index)"><i class="fa fa-pencil-square-o fa-lg"></i></button>
	          <button type="button" class="btn btn-default btn-xs" uib-dropdown-toggle><i class="fa fa-bars fa-lg"></i></button>
			  <ul uib-dropdown-menu role="menu" class="dropdown-menu-right">
			    <li role="menuitem"><a href ng-click="startDuplicate($index)"><i class="fa fa-files-o fa-fw"></i> <fmt:message key="jsp.document.duplicate"/></a></li>
			    <li role="menuitem"><a href ng-click="deleteRow($index)" confirm="{{jbMessages.confirmDelete}}"><i class="fa fa-trash fa-fw"></i> <fmt:message key="jsp.document.delete"/></a></li>
			  </ul>
		    </div>
		    <!-- Modalità aggiunta relazioni -->
		    <div ng-show="relation">
		    	<button type="button" class="btn btn-primary btn-xs" ng-click="addRelation(row.id)"><i class="fa fa-plus fa-lg"></i></button>
		    </div>
	      </td>

		</tr>

		</tbody>
		<tfoot ng-if="!jbUtil.isEmptyObject(summaries)">
		<tr>
		  <td class="text-right" colspan="2"><strong><fmt:message key="jsp.totals.label"/></strong></td>
		  <td class="text-right" ng-repeat-start="p in documentType.propertyList" ng-if="p.queryable && p.propertyType == 'INTEGER'"><strong>{{summaries[p.queryName] | number:0}}</strong></td>
		  <td class="text-right" ng-if="p.queryable && p.propertyType == 'DECIMAL'"><strong>{{summaries[p.queryName] | number:2}}</strong></td>
		  <td ng-repeat-end ng-if="p.queryable && !isNumeric(p.propertyType)"></td>
		  <td></td>
		</tr>
		</tfoot>
		</table>
	  </div>
	</div>
  </div>
  <!-- results finish -->
</div>
	<div class="row" ng-include="'views/document/documentDetail.jsp'" ng-show="editing"></div>
	<div class="row" ng-include="'views/document/documentContentReplace.jsp'" ng-show="contentReplace"></div>

</div>
