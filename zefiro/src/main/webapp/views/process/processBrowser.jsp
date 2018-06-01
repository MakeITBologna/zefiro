<%-- @author Alba Quarto --%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <div class="container">
  <div class="row" ng-include="'views/process/processList.jsp'"  ng-hide="editing || startNewProcess"></div>
  <div class="row" ng-include="'views/process/newProcess.jsp'"  ng-controller="NewProcessController" ng-show="startNewProcess"></div>
</div>
  	