<%@ include file="/include/directive.jsp" %>
<thead>
<tr>
  <th ng-if="jbUtil.isEmptyObject(documentType)" class="text-center sortable" ng-class="{'sort-asc': documentTable.isSortBy('typeName', 'asc'), 'sort-desc': documentTable.isSortBy('typeName', 'desc')}"
  	ng-click="documentTable.sorting('typeName', documentTable.isSortBy('typeName', 'desc') ? 'asc' : 'desc')">
  <div class="sort-indicator"><fmt:message key="jsp.document.type.title"/></div>
  </th>
  
  <th class="text-center sortable" ng-class="{'sort-asc': documentTable.isSortBy('description', 'asc'), 'sort-desc': documentTable.isSortBy('description', 'desc')}"
  	ng-click="documentTable.sorting('description', documentTable.isSortBy('description', 'desc') ? 'asc' : 'desc')">
  <div class="sort-indicator"><fmt:message key="jsp.document.description.title"/></div>
  </th>
  
  <th ng-if="jbUtil.isEmptyObject(documentType)" class="text-center sortable" ng-class="{'sort-asc': documentTable.isSortBy('createdBy', 'asc'), 'sort-desc': documentTable.isSortBy('createdBy', 'desc')}"
  	ng-click="documentTable.sorting('createdBy', documentTable.isSortBy('createdBy', 'desc') ? 'asc' : 'desc')">
  <div class="sort-indicator"><fmt:message key="jsp.document.createdBy.title"/></div>
  </th>
  
  <th class="text-center sortable" ng-class="{'sort-asc': documentTable.isSortBy('created', 'asc'), 'sort-desc': documentTable.isSortBy('created', 'desc')}"
  	ng-click="documentTable.sorting('created', documentTable.isSortBy('created', 'desc') ? 'asc' : 'desc')">
  <div class="sort-indicator"><fmt:message key="jsp.document.created.title"/></div>
  </th>
  
  <th class="text-center sortable" ng-repeat="p in documentType.propertyList" ng-if="p.queryable"
    ng-class="{'sort-asc': documentTable.isSortBy('\''+p.queryName+'\'', 'asc'), 'sort-desc': documentTable.isSortBy('\''+p.queryName+'\'', 'desc')}"
    ng-click="documentTable.sorting('\''+p.queryName+'\'', documentTable.isSortBy('\''+p.queryName+'\'', 'desc') ? 'asc' : 'desc');">
  <div class="sort-indicator">{{p.displayName}}</div>
  </th>
  
  <th></th>
</tr>
</thead>