<%@ include file="/include/directive.jsp" %>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <div class="modal-header">
    <h3 class="modal-title" id="modal-title">{{title}} <button class="btn btn-default pull-right" type="button" ng-click="back()">
      <i class="fa fa fa-times"></i>
    </button></h3>
  </div>
  <div class="modal-body" id="modal-body">
        <form name="jbNewProcessForm" class="form-horizontal" novalidate>
           <div class="row jb-form-group">
            <label for="jbNewProcessForm-p.name" class="control-label col-sm-4" title="{{p.name}}">{{p.title}}</label>
            <div class="col-sm-8">
            <input  class="form-control"/>
        </div>
        </div>
        </form>
  
    <ul>
      <li ng-repeat="item in $ctrl.items">
        <a href="#" ng-click="$event.preventDefault(); $ctrl.selected.item = item">{{ item }}</a>
      </li>
    </ul>
    Selected:
    <b>{{ $ctrl.selected.item }}</b>
  </div>
  <div class="modal-footer">
    <button class="btn btn-primary" type="button" ng-click="$ctrl.ok()">OK</button>
    <button class="btn btn-warning" type="button" ng-click="$ctrl.cancel()">Cancel</button>
  </div>