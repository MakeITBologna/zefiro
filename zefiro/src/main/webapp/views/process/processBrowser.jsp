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
	<!--  <div ng-repeat="(processesGroup,documentTable) in processes"> --> 
		<div class="row jb-toolbar">
			<div class="col-sm-12">
				<h4>{{processGroup}}</h4>
			</div>
		</div>
		
		<table ng-table="documentTable" class="table table-condensed table-bordered table-striped ng-table-responsive" show-filter="false">
		 <tbody>
		  <tr class="ng-table-group" ng-repeat-start="group in $groups">
          	<td colspan="3">
            	<a href="" ng-click="group.$hideRows = !group.$hideRows">
              		<span class="glyphicon" ng-class="{ 'glyphicon-chevron-right': group.$hideRows, 'glyphicon-chevron-down': !group.$hideRows }"></span>
              		<strong>{{ group.value }}</strong>
            	</a>
          	</td>
          </tr>
		  <tr ng-repeat="row in $group.data track by $index" ng-dblclick="!relation && startEdit($index)" ng-repeat-end>
		  	<td>
		  	<div>{{row.name}}</div>
		  	<div>{{row.startUserId}}</div>
		  </tr>
		 </tbody>
		</table>
	<!-- 	</div>  -->
		
	 </div>
	</div>
   </div>
  		
  
  
  
</div>
</div>
  	