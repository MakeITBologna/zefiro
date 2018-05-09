/**
 *  main module
 * 
 */
angular.module('main', [
  'ngRoute',
  'ngResource',
  'ngCookies',
  'ui.bootstrap',
  'ui.bootstrap.datetimepicker',
  'angular-confirm',
  'angularPromiseButtons',
  'jbLocale',
  'documentType',
  'document',
  'process',
  'task'
])

//Contiene provider e costanti
.config(['$routeProvider', '$httpProvider', 'uibDatepickerPopupConfig', 'uiDatetimePickerConfig', 'jbMessages',
function($routeProvider, $httpProvider, uibDatepickerPopupConfig, uiDatetimePickerConfig, jbMessages) {
	$routeProvider
	//Pagina home
	.when('/home', {
		templateUrl: 'views/document/documentBrowser.jsp',
		controller: 'DocumentController'
	})
	
	//Errore generico
	.when('/error', {
		templateUrl: 'views/error.jsp'
	})
	//Errore di accesso
	.when('/forbidden', {
		templateUrl: 'views/forbidden.jsp'
	})
	//Not Found
	.when('/notfound', {
		templateUrl: 'views/notfound.jsp'
	})
	//Pagina logout
	.when('/logout', {
		templateUrl: 'views/login.jsp',
		controller: 'LoginController'
	})
	//Pagina login
	.when('/login', {
		templateUrl: 'views/login.jsp',
		controller: 'LoginController',
		resolve: {
			reset: function ($cookies) {
				$cookies.remove("jbuser");
			}
		}
	})
	
	//Pagina processi avviati da te	
	.when('/process', {
		templateUrl: 'views/process/processBrowser.jsp',
		controller: 'ProcessController'
	})
	
	//Pagina attività assegante a te
	.when('/task', {
		templateUrl: 'views/process/taskBrowser.jsp',
		controller: 'TaskController'
	})

	//Ridireziona a login
	.otherwise({
		redirectTo: '/login'
	});

	$httpProvider.interceptors.push('responseErrorHandler');

	uibDatepickerPopupConfig.currentText = jbMessages.today;
	uibDatepickerPopupConfig.clearText = jbMessages.clear;
	uibDatepickerPopupConfig.closeText = jbMessages.close;

	uiDatetimePickerConfig.todayText = jbMessages.today;
	uiDatetimePickerConfig.clearText = jbMessages.clear;
	uiDatetimePickerConfig.closeText = jbMessages.close;
	uiDatetimePickerConfig.nowText = jbMessages.now;
	uiDatetimePickerConfig.dateText = jbMessages.date;
	uiDatetimePickerConfig.timeText = jbMessages.time;
}])

//Codice che deve esser eseguito per lanciare l'applicazione
.run(['$confirmModalDefaults', 'jbMessages', function($confirmModalDefaults, jbMessages) {
	$confirmModalDefaults.defaultLabels.title = jbMessages.confirmTitle;
	$confirmModalDefaults.defaultLabels.ok = jbMessages.ok;
	$confirmModalDefaults.defaultLabels.cancel = jbMessages.cancel;
}])

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//FACTORY
//ResponseErrorHanler
.factory('responseErrorHandler', ['$cookies', '$q', '$location', '$rootScope', function responseErrorHandler($cookies, $q, $location, $rootScope) {
	return {
		'responseError': function(response) {
			switch (response.status) {
			case 403:
				var rdata = response.data;
				if (rdata.username) { // login fallito
					$cookies.putObject('jbuser', {username : rdata.username});
					$location.url('/login', true);
				} else if (rdata.notLoggedIn) { // non autenticato
					$location.url('/login', true);
				} else { // accesso non consentito
					$location.url('/forbidden', true);
				}
				break;
			case 404:
				$location.url('/notfound', true);
				break;
			case 500:
				$rootScope.lastException = response.data;
				$location.url('/error', true);
				break;
			}
			return $q.reject(response);
		}
	};
}])

//jbUtil, contiene funzioni di utilità
.factory('jbUtil', function() {
	return {
		isEmptyObject: function(obj) {
			if (obj instanceof Array) {
				return obj.length == 0;
			}
			
			for (var p in obj) {
				return false;
			}
			
			return true;
		},
		getModelIndex: function(table, i) {
			return i + (table.page() - 1) * table.count();
		},
		//Converte un intero che rappresenta i permessi (valori 0 - 15) in un oggetto con 4 campi booleani(get,post,put,del)
		permissionsInElemIntToObj: function(permissions){
			var lObj = {};
			if(permissions == null ){
				lObj.get = false;
				lObj.post = false;
				lObj.put = false;
				lObj.del = false;
			}else{
				
				lObj.del = ((permissions & 1) > 0) ;
				lObj.put = ((permissions & 2) > 0);
				lObj.post = ((permissions & 4) > 0);
				lObj.get = ((permissions & 8) > 0);	
			}
			return lObj;
		},
		//Converte l'oggetto con 4 campi booleani(get,post,put,del) che rappresenta i permessi in un intero (valori 0 - 15)
		permissionsInElemObjToInt: function(permissions) {
			var lInt = 0;;
			if(permissions.get){ lInt += 8; }
			if(permissions.post){ lInt += 4; }
			if(permissions.put){ lInt += 2; }
			if(permissions.del){ lInt += 1; }
			return lInt;
		},
		findRowWithKey: function(array, key , value){
			for(var i=0 ; i< array.length ; i++){
				if(array[i][key] === value)
					return i;
			}
		},
		sanitize: function(name) {
			if (name == null) {
				return null;
			}
			return name.replace(":", "_");
		},
		toArray: function(obj) {
			if (obj instanceof Array) {
				return obj;
			}
			
			var result = [];
			for (var p in obj) {
				result.push(angular.extend({}, obj[p]));
			}
			
			return result;
		},
		contains: function(container, element) {
			for(var i in container) {
				if (container[i] == element) {
					return true;
				}
			}
			return false;
		},
		findByProperty: function(container, property, value) {
			for(var i in container) {
				if (container[i][property] == value) {
					return container[i];
				}
			}
			return null;
		}
	}
})

//jbValidate, contiene funzioni di validazione input
.factory('jbValidate', function() {
	return {
		getClass: function(el) {
			if (el.$valid && el.$dirty) {
				return 'has-success';
			} else if (el.$invalid && el.$$parentForm.$submitted) {
				return 'has-error';
			} else
				return '';
		},
		checkForm: function(form) {
			form.$setSubmitted();
			return form.$valid;
		},
		clearForm: function(form) {
			form.$setDirty(false);
			form.$setPristine(true);
		},
		showMessage: function(el){
			return (el) && el.$invalid && el.$$parentForm.$submitted;
		}
	}
})

.factory('jbWorkflowUtil', function() {
	return {
		decodeType: function(type){
			switch(type){
				case "d:text": return "STRING";
				case "d:int": return "INTEGER";
				case "d:long": return "INTEGER";
				case "d:float":return "DECIMAL";
				case "d:double":return "DECIMAL";
				case "d:boolean": return "BOOLEAN";
				case "d:date":  return "DATE";
				case "d:datetime": return "DATETIME";
			}
		}
	}
})

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//CONTROLLER

//Logincontroller
.controller('LoginController', ['$cookies', '$scope', '$http', '$location', 'jbValidate', function($cookies, $scope, $http, $location, jbValidate) {

	$scope.jbValidate = jbValidate;

	$scope.credentials = {};

	$scope.login = function() {
		$scope.loginPromise =
			$http
			.get('a/Login', {params: $scope.credentials})
			.then(function(response) {
				var jbuser = response.data;
				$cookies.putObject('jbuser', {idUser: jbuser.idUser, username : jbuser.username, enabled : jbuser.enabled, fullName : jbuser.fullName});
				$location.url('/home', true);
			});
	};

}])

//MainController
.controller('MainController', ['$cookies', '$scope', '$http', function($cookies, $scope, $http) {

	$scope.serverMessageVisible = false;
	$scope.serverMessageString = null;
	
	$scope.showServerMessage = function(string){
		$scope.serverMessageString = string;
		$scope.serverMessageVisible = true;
	}
	
	$scope.hideServerMessage = function(){
		$scope.serverMessageVisible = false;
	}
	
	$scope.getUser = function() {
		return $cookies.getObject("jbuser");
	};

	$scope.isUserLogged = function() {
		var user = $scope.getUser();
		return !!user && !!user.enabled;
	};

	$scope.loginError = function() {
		var user = $scope.getUser();
		return !!user && !user.enabled && !!user.username;
	};

	$scope.logout = function() {
		$http
		.get("a/Login", {params: {action: 'logout'}})
		.then(function(response) {
			$cookies.remove("jbuser");
	    });
	};

	// gestisce lo stato dei popup dei datepicker
	$scope.calendarPopups = [];
	$scope.openCalendar = function(calendarName) {
		$scope.calendarPopups[calendarName] = true;
	};
		
}])

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Directive

//jbPrint, direttiva per la stampa
.directive('jbPrint', function() {
	return {
		restrict: 'A',
		require:'form',
		scope: {jbPrint:'@'},
		link: function(scope, elem, attrs, formCtrl) { //funzione di direttiva
			formCtrl.print = function(fmt) {
				if (fmt == 'pdf' || fmt == 'xls') {
					elem.attr('action', scope.jbPrint+'/'+fmt);
					elem.attr('target', '_blank'); //apre una nuova pagina
					elem[0].submit(); //submit dom
				}
				else{
					elem.attr('action', scope.jbPrint);
					elem.attr('target', '_blank'); //apre una nuova pagina
					elem[0].submit(); //submit dom
				}
			};
		}
	};
})

// validazione per uguaglianza
.directive('equals', function() {
	  return {
	    restrict: 'A', // only activate on element attribute
	    require: '?ngModel', // get a hold of NgModelController
	    link: function(scope, elem, attrs, ngModel) 
	    {
	    	if(!ngModel) return; // do nothing if no ng-model
	    	// watch own value and re-validate on change
	    	scope.$watch(attrs.ngModel, function() {
	    		validate();
	    	});
	    	// observe the other value and re-validate on change
	    	attrs.$observe('equals', function (val) {
	    		validate();
	    	});
	
	    	var validate = function() {
	    		var val1 = ngModel.$viewValue;
	    		var val2 = attrs.equals;
	    		ngModel.$setValidity('equals', ! val1 || ! val2 || val1 === val2);
	    	};
	    }
	  }
})

.directive('fileDropzone', function($sce) { // Al momento non usato
    return {
      restrict: 'A',
      scope: {
        file: '=',
        fileName: '='
      },
      link: function(scope, element, attrs) {
        var checkSize, isTypeValid, processDragOverOrEnter, validMimeTypes;
        processDragOverOrEnter = function(event) {
          if (event != null) {
            event.preventDefault();
          }
          event.dataTransfer.effectAllowed = 'copy';
          return false;
        };
        validMimeTypes = attrs.fileDropzone;
        checkSize = function(size) {
          var _ref;
          if (((_ref = attrs.maxFileSize) === (void 0) || _ref === '') || (size / 1024) / 1024 < attrs.maxFileSize) {
            return true;
          } else {
            alert("File must be smaller than " + attrs.maxFileSize + " MB");
            return false;
          }
        };
        isTypeValid = function(type) {
          if ((validMimeTypes === (void 0) || validMimeTypes === '') || validMimeTypes.indexOf(type) > -1) {
            return true;
          } else {
            alert("Invalid file type.  File must be one of following types " + validMimeTypes);
            return false;
          }
        };
        element.bind('dragover', processDragOverOrEnter);
        element.bind('dragenter', processDragOverOrEnter);
        return element.bind('drop', function(event) {
          var file, name, reader, size, type;
          if (event != null) {
            event.preventDefault();
          }
          reader = new FileReader();
          reader.onload = function(evt) {
            if (checkSize(size) && isTypeValid(type)) {
              return scope.$apply(function() {
            	//var fileURL = URL.createObjectURL(evt.target.result);  
                //scope.file = $sce.trustAsResourceUrl(fileURL);
            	  scope.file = evt.target.result;
                if (angular.isString(scope.fileName)) {
                  return scope.fileName = name;
                }
              });
            }
          };
          file = event.dataTransfer.files[0];
          name = file.name;
          type = file.type;
          size = file.size;
          reader.readAsDataURL(file);
          return false;
        });
      }
    };
  })
  
  .filter('trusted', ['$sce', function ($sce) {
    return function(url) {
        return $sce.trustAsHtml(url);
    };

}]);



