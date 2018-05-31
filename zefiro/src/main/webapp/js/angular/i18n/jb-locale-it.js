angular.module("jbLocale", ["ngLocale"])

.constant("jbMessages", {

"ok"    : "Ok",
"cancel": "Annulla",

"confirmTitle" : "Prego confermare",
"confirmDelete": "Eliminare il record ?",
"confirmDeleteRel": "Eliminare la relazione?",

"today": "Oggi",
"clear": "Pulisci",
"close": "Chiudi",
"now"  : "Adesso",
"date" : "Data",
"time" : "Ora",

"priorityHigh": "Priorità alta",
"priorityLow": "Priorità bassa",
"priorityMedium": "Priorità media",

"expired": "Scaduto",
"maturing": "In scadenza",
"other": "Altro",
	
"task" : {
	"expired": "Attività scaduta",
	"expiring": "Attività in scadenza",
	"assignedUser": "Attività assegnata ad un utente",
	"assignedUsers": "Attività assegnata ad un pool",
	"processType": "Tipo processo",
	"assignment": "Assegnazione",
	"deadline": "Scadenza",
	"priority": "Priorità",
	"deadlineProssimity": "In scadenza",
	"unassigned" : "Non presa in carico"
},

"authority" : { 
	"searchAuthority" : "Nessun elemento cercato",
	"anySelected": "Nessun elemento selezionato",
	"anyFound": "Nessun elemento trovato"
},

"workflow": {
	"select": {
		"assignee": "Seleziona l'assegnatario",
		"bpm_assignee": "Seleziona l'utente assegnatario",
		"bpm_assignees": "Seleziona gli utenti assegnatari",
		"bpm_groupAssignee": "Seleziona il grouppo assegnatario",
		"bpm_groupAssignees": "Seleziona i gruppi assegnatari"
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
