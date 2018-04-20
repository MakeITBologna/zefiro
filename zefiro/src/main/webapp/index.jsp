<!DOCTYPE html>
<%@ include file="/include/directive.jsp" %>
<html ng-app="main" ng-controller="MainController">

<head>

<title><fmt:message key="jsp.index.title"/></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta name="cache-control" content="no-cache" />
<!-- Per evitare il flash del ricaricamento pagina in IE -->
<!--[if IE]>
<meta http-equiv="Page-Enter" content="blendTrans(Duration=0)" />
<meta http-equiv="Page-Exit" content="blendTrans(Duration=0)" />
<![endif]-->

<!-- Per piena compatibilitÓ con IE11 - Edge -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet" href="css/bootstrap.min.css" />
<link rel="stylesheet" href="css/font-awesome.min.css" />
<link rel="stylesheet" href="css/ng-table-custom.css" />
<link rel="stylesheet" href="css/dashgum/style.css" />
<link rel="stylesheet" href="css/dashgum/style-responsive.css" />
<link rel="stylesheet" href="css/promise-buttons.css" />
<link rel="stylesheet" href="css/jbrick.css" />

<script type="text/javascript" src="js/angular/angular.js?version=${version}"></script>
<script type="text/javascript" src="js/angular/angular-route.js?version=${version}"></script>
<script type="text/javascript" src="js/angular/angular-resource.js?version=${version}"></script>
<script type="text/javascript" src="js/angular/angular-cookies.js?version=${version}"></script>
<script type="text/javascript" src="js/angular/angular-animate.js?version=${version}"></script>
<script type="text/javascript" src="js/angular/ui-bootstrap-tpls.js?version=${version}"></script>
<script type="text/javascript" src="js/angular/angular-confirm.js?version=${version}"></script>
<script type="text/javascript" src="js/angular/datetime-picker.js?version=${version}"></script>
<script type="text/javascript" src="js/angular/ng-table.js?version=${version}"></script>
<script type="text/javascript" src="js/angular/ngRemoteValidate.js"></script>
<script type="text/javascript" src="js/angular/angular-promise-buttons.js?version=${version}"></script>
<script type="text/javascript" src="js/angular/i18n/angular-locale-${language}.js?version=${version}"></script>
<script type="text/javascript" src="js/angular/i18n/jb-locale-${language}.js?version=${version}"></script>
<script type="text/javascript" src="js/angular/angular-filter.js?version=${version}"></script>
<script type="text/javascript" src="js/app/main.js?version=${version}"></script>
<script type="text/javascript" src="js/app/documentType.js?version=${version}"></script>
<script type="text/javascript" src="js/app/document.js?version=${version}"></script>

</head>

<body>
<!--header start-->
<nav class="navbar navbar-inverse" ng-if="isUserLogged()">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand">
        <img alt="Logo" src="images/zefiro.png" style="height: 100%">
      </a>
    </div>
    <ul class="nav navbar-nav">
    	<li><a href="#home"><fmt:message key="jsp.document.label"/></a></li>
   	</ul>
   	<ul class="nav navbar-nav navbar-right">
     	<li class="dropdown" uib-dropdown>
          <a class="dropdown-toggle" href uib-dropdown-toggle>{{getUser().fullName}}<span class="caret"></span></a>
          <ul class="dropdown-menu" uib-dropdown-menu>
            <li><a href="#login">Logout</a></li>
          </ul>
        </li>
     </ul>
  </div>
</nav>
<!--header end-->

<!--content start-->
<div ng-show="serverMessageVisible">
	<p><h3 class="alert alert-danger" role="alert"> {{serverMessageString}} </h3></p>
</div>
<div ng-view></div>
<!--content end-->

</body>
</html>