<%@ include file="/include/directive.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<div ng-init="search()" hidden></div>
<div class="container-fluid">
<div class="row" ng-hide="editing || contentReplace">
  <div class="page-header jb-page-header">
    <div class="pull-right">
  		<button ng-hide="relation" type="button" class="btn btn-success" title="<fmt:message key="jsp.results.insert.user"/>" ng-click="search()"><i class="fa fa-plus"></i> <fmt:message key="jsp.process.new"/></button>
  	  	<button ng-show="relation" type="button" class="btn btn-danger" title="<fmt:message key="js.dialog.annulla"/>" ng-click="closeAddRelation()"><i class="fa fa-times"></i> <fmt:message key="js.dialog.annulla"/></button>
  	</div>
  	<h1><fmt:message key="jsp.process.label"/></h1>
  </div>
   <div>
	<div class="panel">
	  <div class="panel-body">

		<div class="row jb-toolbar">
			<div class="col-sm-12">
				<h4>{{processGroup}}</h4>
			</div>
		</div>
		
		<table ng-table="documentTable" class="table table-condensed table-bordered table-striped ng-table-responsive" show-filter="false" show-group="isGroupHeaderRowVisible">
		 <tbody>
		  <tr class="ng-table-group" ng-repeat-start="group in $groups">
          	<td colspan="3">
            	<a href="" ng-click="group.$hideRows = !group.$hideRows">
              		<span class="glyphicon" ng-class="{ 'glyphicon-chevron-right': group.$hideRows, 'glyphicon-chevron-down': !group.$hideRows }"></span>
              		<strong>{{ group.value }}</strong>
            	</a>
          	</td>
          </tr>
		  <tr ng-hide="group.$hideRows"  ng-repeat="row in group.data track by $index" ng-dblclick="!relation && startEdit($index)" ng-repeat-end>
		  	<td>
		 	 <div><span ng-if="row.processBusinessKey">{{row.processBusinessKeyprocessBusinessKey}}</span><span ng-else>(Nessun Messaggio)</span></div>
		  	 <div>
		  		<span ng-if="row.startedAt"><fmt:message key="jsp.process.process.startedAt"/>: {{row.startedAt  | date: '${localePatternDate}'}}</span>
		  	 </div>
		  	 <div ng-if="row.endedAt">	
		  		<span><fmt:message key="jsp.process.process.endedAt"/>:</span>
		  		<span >{{row.endedAt  | date: '${localePatternDate}'}}</span>
		  	 </div>
		  	 <div ng-if="row.description"><fmt:message key="jsp.description"/>: {{row.description}}</div>
		  	 <div ng-if="row.priority"><fmt:message key="jsp.priority"/>: {{decodePriority(row.priority)}}</div>
		  	</td>
		  	
		  	<td>
	         <div class="btn-group" uib-dropdown ng-show="row.id != null && !relation">
	          <button type="button" class="btn btn-primary btn-xs"><i class="fa fa-search fa-lg"></i></button>
	          <button type="button" class="btn btn-default btn-xs"><i class="fa fa-trash  fa-lg"></i></button>
		     </div>
	       </td>
		  </tr>
		 </tbody>
		 </table>
		
	 </div>
	</div>
   </div> 
</div>
</div>
  	