<%-- 
  -- Select authority/ies Template
  -- @author Alba Quarto 
  --%>
<%@ include file="/include/directive.jsp" %>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  
  <%-- ###### Header ###### --%>
  <div class="modal-header">
    <div class="pull-right">
      <button class="btn btn-default" type="button" ng-click="back()">
        <i class="fa fa fa-times"></i>
      </button>
    </div>
    <h3 class="modal-title" id="modal-title" style="color: inherit;">{{title}}
    </h3>
  </div>
  
  <div class="modal-body" id="modal-body">
  
    <%-- ###### Search Input ###### --%>
    <form name="jbSearchAuthForm" class="form-horizontal" novalidate>
      <div class="row input-group" style="display: table-footer-group">
        <input  class="form-control" ng-model="searchAuth" ng-minlength="2" title="searchAuth" required name="searchAuth" placeholder=" <fmt:message key="js.validate.minLenght"><fmt:param value="2" /></fmt:message>"  />
        <span class="input-group-btn">
          <button class="btn btn-default" type="button" ng-click="searchAuthority(jbSearchAuthForm)">
            <i class="fa fa-search"></i>
          </button>
        </span>
      </div>
      <label class="text-danger" ng-show="jbValidate.showMessage(jbSearchAuthForm.searchAuth)">
        <fmt:message key="js.validate.minLenght">
          <fmt:param value="2" />
        </fmt:message> 
      </label>
    </form>
    
    <%-- ###### Select Areas ###### --%>
    <div class="row">
    
      <%-- ###### Searhc results ###### --%>
      <div class="col-md-6">
        <ul class="jb-select-list jb-full-area form-control" uib-dropdown-menu role="menu" aria-labelledby="single-button">
          <li ng-if="foundAuthorities.length===0" data-original-index="0" data-optgroup="1">
            <div>
              <em>{{searchMessage}}</em>
            </div>
          </li>
          <li ng-repeat="found in foundAuthorities" data-original-index="0" data-optgroup="1">
            <div ng-if="authType === AUTHORITY_TYPE.PERSON">
              <span>{{found.firstName}}</span>
              <span ng-if="found.lastName">&nbsp;{{found.lastName}}</span>
              <span>&nbsp;({{found.id}})</span>
              <span ng-if="(!authMany && selectedAuthorities.length===0) || (authMany && !selectedMap[getAuthorityId(found)])" ng-click="selectAuthority(found)"
                class="pull-right">
                <i class="fa fa-plus-circle"></i>
              </span>
            </div>
            <div ng-if="authType === AUTHORITY_TYPE.GROUP">
              <span>{{found.displayName}}</span>
              <span ng-if="(!authMany && selectedAuthorities.length===0) || (authMany && !selectedMap[getAuthorityId(found)])" ng-click="selectAuthority(found)"
                class="pull-right">
                <i class="fa fa-plus-circle"></i>
              </span>
            </div>
          </li>
        </ul>
      </div>
      
      <%-- ###### Selected items ###### --%>
      <div class="col-md-6" ng-init="preSelectedAutority()">
        <ul class="jb-select-list jb-full-area col-md-6 form-control" uib-dropdown-menu role="menu" aria-labelledby="single-button">
          <li ng-if="selectedAuthorities.length===0" data-original-index="0" data-optgroup="1">
            <div>
              <em>{{selectedMessage}}
                <em>
            </div>
          </li>

          <li ng-repeat="selected in selectedAuthorities" data-original-index="0" data-optgroup="1">
            <div ng-if="authType === AUTHORITY_TYPE.PERSON">
              <span>{{selected.firstName}}</span>
              <span ng-if="selected.lastName">&nbsp;{{selected.lastName}}</span>
              <span>&nbsp;({{selected.id}})</span>
              <span class="pull-right" ng-click="removeSelected(selected)">
                <i class="fa fa-times-circle-o"></i>
              </span>
            </div>
            <div ng-if="authType === AUTHORITY_TYPE.GROUP">
              <span>{{selected.displayName}}</span>
              <span class="pull-right" ng-click="removeSelected(selected)">
                <i class="fa fa-times-circle-o"></i>
              </span>
            </div>
          </li>
        </ul>
      </div>
      
    </div>
    <%-- ###### End Select Areas ###### --%>

  </div>

  <div class="modal-footer">
    <div class="text-center">
      <button class="btn btn-primary" type="button" ng-click="saveSelected()">Salva</button>
    </div>
  </div>