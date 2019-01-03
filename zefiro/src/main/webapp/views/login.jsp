<%@ include file="/include/directive.jsp" %>
<div class="container">
   <form name="jbLoginForm" class="form-login jb-border" ng-keyup="$event.keyCode == 13 ? (jbValidate.checkForm(jbLoginForm) && login()) : null" novalidate>
   	<input type="hidden" name="action" value="login" />
     <h2 class="form-login-heading bg-primary"><fmt:message key="jsp.login.title" /></h2>
     <div class="login-wrap">
     	<div class="form-group" ng-class="jbValidate.getClass(jbLoginForm.username)">
         <input type="text" name="username" ng-model="credentials.username" required class="form-control" placeholder="<fmt:message key="jsp.login.username" />" autofocus>
        </div>
        <div class="form-group" ng-class="jbValidate.getClass(jbLoginForm.password)">
         <input type="password" name="password" ng-model="credentials.password" required class="form-control" placeholder="<fmt:message key="jsp.login.password" />">
        </div>
        <div class="form-group">
         <button class="btn btn-primary btn-block" name="login" type="button" ng-click="jbValidate.checkForm(jbLoginForm) && login()" promise-btn="loginPromise"><i class="fa fa-lock"></i> <fmt:message key="jsp.login.submit"/></button>
        </div>
        <div class="alert alert-danger" role="alert" ng-if="loginError()">
         <fmt:message key="jsp.login.credential.error.label"/>
        </div>
     </div>
   </form>
</div>
