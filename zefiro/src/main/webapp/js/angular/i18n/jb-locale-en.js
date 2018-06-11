angular.module("jbLocale", ["ngLocale"])

.constant("jbMessages", {

"ok"    : "Ok",
"cancel": "Cancel",

"confirmTitle" : "Please confirm",
"confirmDelete": "Delete current record ?",
"confirmDeleteRel": "Delete current relatioship?",
"confirmSelection":"Are you sure you want to select",

"today": "Today",
"clear": "Clear",
"close": "Close",
"now"  : "Now",
"date" : "Date",
"time" : "Time",

"priorityHigh": "High priority",
"priorityLow": "Low priority",
"priorityMedium": "Medium priority",
"noMessage":"No message",

"high_f": "High",
"low_f": "Low",
"medium_f": "Medium",

"expired": "Expired",
"maturing": "Maturing",
"other": "Other",

"task" : {
	"expired": "Task expired",
	"expiring": "Task expirng",
		"assignedUser": "Task assigned to one user",
		"assignedUsers": "Task assigned to a pool",
		"processType": "Process type",
		"assignment": "Assignement date",
		"deadline": "Deadline",
		"priority": "Priority",
		"deadlineProssimity": "Maturing",
		"unassigned" : "Unssigned",
		"completedLate": "Task completed after the due date",
		"active": "Task active",
		"completedInTime": "Task completed in time"
},

"authority" : { 
	"searchAuthority" : "No items searched",
	"anySelected": "No items selected",
	"anyFound": "No items found"
},
"workflow": {
	"select": {
		"assignee": "Select the assignee",
		"bpm_assignee": "Select the assignee user",
		"bpm_assignees": "Select the assignee users",
		"bpm_groupAssignee": "Select the assignee group",
		"bpm_groupAssignees": "Select the assignee groups"
	}
}
})

.factory('jbPatterns', ['$locale', function($locale) {
	return {
		number: function(nd) {
			return (nd > 0)? "[+-]?\\d*(\\"+$locale.NUMBER_FORMATS.DECIMAL_SEP+"\\d{1,"+nd+"})?" : "[+-]?\\d*";
		}
	}
}])

.factory('jbFormat', ['$locale', function($locale) {
	
	var number = function (data, ndec) {
		if (data == undefined || isNaN(data)) {
			return '';
		}
		
		var d = (ndec)? ndec : 0;
		
		if (d < 0 || Math.round(d) != d) {
			throw new Error('Invalid formatting parameters');
		}
		
		var m = Math.pow(10, d);
		var r = Math.round(data * m) / m;
		
		return (r+'').replace('.', $locale.NUMBER_FORMATS.DECIMAL_SEP);
	};
	
	var toNumber = function (data) {
		return (data+'').replace($locale.NUMBER_FORMATS.DECIMAL_SEP,  '.') * 1;
	};
	
	var bool = function(data) {
		if (typeof(data) === 'boolean')
			return data + '';
 
		return '';
	}
	
	var toBool = function(data) {
		if(data == null || data == ''){
			return null;
		}
		return JSON.parse(data);
	}
	
	return {
		number: number,
		toNumber: toNumber,
		bool: bool,
		toBool: toBool
	}
}])

.directive('jbNumber', ['jbFormat', function (jbFormat) {
    return {
        restrict: 'A',
        require: 'ngModel',
        scope: {jbNumber:'@'},
        link: function(scope, element, attrs, ngModelCtrl) {
        	
        	// dal model alla view
        	ngModelCtrl.$formatters.push(function(data) {
                return jbFormat.number(data, scope.jbNumber);
            });
        	
        	// dalla view al model
        	ngModelCtrl.$parsers.push(function(data) {
        	    return jbFormat.toNumber(data);
        	});
        }
    };
}])

.directive('jbBoolean', ['jbFormat', function (jbFormat) {
    return {
        restrict: 'A',
        require: 'ngModel',
        scope: {jbType:'@'},
        link: function(scope, element, attrs, ngModelCtrl) {
        	
        	// dal model alla view
        	ngModelCtrl.$formatters.push(function(data) {
                return jbFormat.bool(data);
            });
        	
        	// dalla view al model
        	ngModelCtrl.$parsers.push(function(data) {
        	    return jbFormat.toBool(data);
        	});
        }
    };
}]);
