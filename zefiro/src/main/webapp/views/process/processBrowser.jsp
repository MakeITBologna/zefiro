<%@ include file="/include/directive.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<div class="container-fluid">
<div class="row" ng-hide="editing || contentReplace">
  <div class="page-header jb-page-header">
    <div class="pull-right">
  		<button ng-hide="relation" type="button" class="btn btn-success" title="<fmt:message key="jsp.results.insert.user"/>" ng-click="startInsert()"><i class="fa fa-plus"></i> <fmt:message key="jsp.process.new"/></button>
  	  	<button ng-show="relation" type="button" class="btn btn-danger" title="<fmt:message key="js.dialog.annulla"/>" ng-click="closeAddRelation()"><i class="fa fa-times"></i> <fmt:message key="js.dialog.annulla"/></button>
  	</div>
  	<h1><fmt:message key="jsp.process.label"/></h1>
  </div>
</div>
</div>
  	