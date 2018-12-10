<!DOCTYPE html>
<%@ include file="/include/directive.jsp" %>
<html >

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta name="cache-control" content="no-cache" />
<!-- Per evitare il flash del ricaricamento pagina in IE -->
<!--[if IE]>
<meta http-equiv="Page-Enter" content="blendTrans(Duration=0)" />
<meta http-equiv="Page-Exit" content="blendTrans(Duration=0)" />
<![endif]-->

<!-- Per piena compatibilità con IE11 - Edge -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="shortcut icon" href="images/zefiro.ico">
<link rel="stylesheet" href="css/bootstrap.min.css" />
<link rel="stylesheet" href="css/font-awesome.min.css" />
<link rel="stylesheet" href="css/ng-table-custom.css" />
<link rel="stylesheet" href="css/dashgum/style.css" />
<link rel="stylesheet" href="css/dashgum/style-responsive.css" />
<link rel="stylesheet" href="css/promise-buttons.css" />
<link rel="stylesheet" href="css/jbrick.css?version=${version}" />

 
<script src="js/single-spa/angular.min.js"></script>
 <script src="js/single-spa/single-spa"></script>
  <script src="js/single-spa/single-spa-angularjs"></script>
  
  
<script type="text/javascript" src="js/app/main.js?version=${version}"></script>
<script type="text/javascript" src="js/app/documentType.js?version=${version}"></script>
<script type="text/javascript" src="js/app/document.js?version=${version}"></script>
<script type="text/javascript" src="js/app/workflow.js?version=${version}"></script>
<script type="text/javascript" src="js/app/process.js?version=${version}"></script>
<script type="text/javascript" src="js/app/task.js?version=${version}"></script>
<script type="text/javascript" src="js/app/authority.js?version=${version}"></script>
<script type="text/javascript" src="js/app/applicationState.js?version=${version}"></script>

<script type="text/javascript" src="js/app/single-spa.config.js"></script>





</head>

<body>
	<div id="zefiro-app" />

</body>

</html>