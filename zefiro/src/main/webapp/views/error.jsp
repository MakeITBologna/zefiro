<%@ include file="/include/directive.jsp" %>

<div class="container">

<div class="page-header alert-danger" style="padding: 10px">
<h1><fmt:message key="jsp.error.title"/></h1>
<br>
<h4>{{$root.lastException.message}}</h4>
</div>

<h4>Stack trace</h4>
<hr>
<ul>
<li ng-repeat="t in $root.lastException.stackTrace">at {{t.className}}.{{t.methodName}} ( {{t.fileName}}: {{t.lineNumber}} )</li>
</ul>

<div ng-if="$root.lastException.cause">
<br>
<h4 class="alert-danger">Cause: {{$root.lastException.cause.message}}</h4>

<h5>Stack trace</h5>
<hr>
<ul>
<li ng-repeat="t1 in $root.lastException.cause.stackTrace">at {{t1.className}}.{{t1.methodName}} ( {{t1.fileName}}: {{t1.lineNumber}} )</li>
</ul>
</div>

<div ng-if="$root.lastException.cause.cause">
<br>
<h4 class="alert-danger">Nested Cause: {{$root.lastException.cause.message}}</h4>

<h5>Stack trace</h5>
<hr>
<ul>
<li ng-repeat="t2 in $root.lastException.cause.cause.stackTrace">at {{t2.className}}.{{t2.methodName}} ( {{t2.fileName}}: {{t2.lineNumber}} )</li>
</ul>
</div>

</div>