/**
 *  main module
 */
angular.module('main', [
	'ngRoute',
	'ngResource',
	'ngCookies',
	'ui.bootstrap',
	'ui.bootstrap.datetimepicker',
	'angular-confirm',
	'angularPromiseButtons',
	'elif',
	'jbLocale',
	'documentType',
	'document',
	'workflow',
	'process',
	'task',
	'authority',
	'applicationState'
])


	//Contiene provider e costanti
	.config(['$routeProvider', '$httpProvider', 'uibDatepickerPopupConfig', 'uiDatetimePickerConfig', 'jbMessages',
		function ($routeProvider, $httpProvider, uibDatepickerPopupConfig, uiDatetimePickerConfig, jbMessages) {
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
						reset: function (jbAuthFactory) {
							jbAuthFactory.removeUser();
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
			
			$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';

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
	.run(['$confirmModalDefaults', 'jbMessages', function ($confirmModalDefaults, jbMessages) {
		$confirmModalDefaults.defaultLabels.title = jbMessages.confirmTitle;
		$confirmModalDefaults.defaultLabels.ok = jbMessages.ok;
		$confirmModalDefaults.defaultLabels.cancel = jbMessages.cancel;
	}])

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//FACTORY
	//ResponseErrorHanler
	.factory('responseErrorHandler', ['$q', '$location', '$rootScope','jbAuthFactory', function responseErrorHandler($q, $location, $rootScope, jbAuthFactory) {
		return {
			'responseError': function (response) {
				switch (response.status) {	
					case 403:
						var rdata = response.data;
						if (rdata.username) { // login fallito
							jbAuthFactory.storeUser({ username: rdata.username })
							$location.url('/login', true);
						} else if (rdata.notLoggedIn) { // non autenticato
							jbAuthFactory.storeUser(rdata );
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
	.factory('jbUtil', function () {
		return {
			isEmptyObject: function (obj) {
				if (obj instanceof Array) {
					return obj.length === 0;
				}

				for (var p in obj) {
					return false;
				}

				return true;
			},
			
			getModelIndex: function (table, i) {
				return i + (table.page() - 1) * table.count();
			},
			//Converte un intero che rappresenta i permessi (valori 0 - 15) in un oggetto con 4 campi booleani(get,post,put,del)
			permissionsInElemIntToObj: function (permissions) {
				var lObj = {};
				if (permissions == null) {
					lObj.get = false;
					lObj.post = false;
					lObj.put = false;
					lObj.del = false;
				} else {

					lObj.del = ((permissions & 1) > 0);
					lObj.put = ((permissions & 2) > 0);
					lObj.post = ((permissions & 4) > 0);
					lObj.get = ((permissions & 8) > 0);
				}
				return lObj;
			},
			//Converte l'oggetto con 4 campi booleani(get,post,put,del) che rappresenta i permessi in un intero (valori 0 - 15)
			permissionsInElemObjToInt: function (permissions) {
				var lInt = 0;;
				if (permissions.get) { lInt += 8; }
				if (permissions.post) { lInt += 4; }
				if (permissions.put) { lInt += 2; }
				if (permissions.del) { lInt += 1; }
				return lInt;
			},
			findRowWithKey: function (array, key, value) {
				for (var i = 0; i < array.length; i++) {
					if (array[i][key] === value)
						return i;
				}
			},
			sanitize: function (name) {
				if (name == null) {
					return null;
				}
				return name.replace(":", "_");
			},
			toArray: function (obj) {
				if (obj instanceof Array) {
					return obj;
				}

				var result = [];
				for (var p in obj) {
					result.push(angular.extend({}, obj[p]));
				}

				return result;'E'
			},
			contains: function (container, element) {
				for (var i in container) {
					if (container[i] == element) {
						return true;
					}
				}
				return false;
			},
			findByProperty: function (container, property, value) {
				for (var i in container) {
					if (container[i][property] == value) {
						return container[i];
					}
				}
				return null;
			},
			stringToDate: function (string) {
				var R_ISO8601_STR = /^(\d{4})-?(\d\d)-?(\d\d)(?:T(\d\d)(?::?(\d\d)(?::?(\d\d)(?:\.(\d+))?)?)?(Z|([+-])(\d\d):?(\d\d))?)?$/;
				// 1        2       3         4          5          6          7          8  9     10      11
				var match;
				if (!(match = string.match(R_ISO8601_STR))) {
					return null;
				}
				var toInt = function (str) {
					return parseInt(str, 10);
				}
				var date = new Date(0),
					tzHour = 0,
					tzMin = 0,
					dateSetter = match[8] ? date.setUTCFullYear : date.setFullYear,
					timeSetter = match[8] ? date.setUTCHours : date.setHours;
				if (match[9]) {
					tzHour = toInt(match[9] + match[10]);
					tzMin = toInt(match[9] + match[11]);
				}

				dateSetter.call(date, toInt(match[1]), toInt(match[2]) - 1, toInt(match[3]));
				var h = toInt(match[4] || 0) - tzHour;
				var m = toInt(match[5] || 0) - tzMin;
				var s = toInt(match[6] || 0);
				var ms = Math.round(parseFloat('0.' + (match[7] || 0)) * 1000);
				timeSetter.call(date, h, m, s, ms);
				return date;
			},
			deadlineProximity: function (date, proximityDate) {
				if (!date) {
					return 3;
				}
				var parsedDate = this.stringToDate(date);
				if (!parsedDate) {
					return 3;
				}
				var now = new Date();
				if(proximityDate){
					now = this.stringToDate(proximityDate);
				}
				var parsedDateSum = parsedDate.getTime();
				var nowSum = now.getTime();
				var day = 86400000;

				var dlProx = 3;
				if (nowSum > (parsedDateSum - day)) {
					dlProx = 2;
					if (nowSum > (parsedDateSum)) {
						dlProx = 1;
					}
				}
				return dlProx;
			},
			b64EncodeUnicode: function (toEncode) {
				return btoa(encodeURIComponent(toEncode).replace(/%([0-9A-F]{2})/g,
				        function toSolidBytes(match, p1) {
				            return String.fromCharCode('0x' + p1);
				    }));
			}
		}
	})

	//jbValidate, contiene funzioni di validazione input
	.factory('jbValidate', function () {
		return {
			getClass: function (el) {
				if (el.$valid && el.$dirty) {
					return 'has-success';
				} else if (el.$invalid && el.$$parentForm.$submitted) {
					return 'has-error';
				} else
					return '';
			},
			checkForm: function (form) {
				form.$setSubmitted();
				return form.$valid;
			},
			clearForm: function (form) {
				form.$setDirty(false);
				form.$setPristine(true);
			},
			showMessage: function (el) {
				return (el) && el.$invalid && el.$$parentForm.$submitted;
			}
		}
	})
	//jbValidate, contiene funzioni di validazione input // factory('jbAuthFactory', ['$cookies', function ($cookies) {..}
	.factory('jbAuthFactory', ['$cookies', function ($cookies) {
		var storedUserLabel = "jbuser"; 
		
		return {
			getUser: function () {
				return $cookies.getObject(storedUserLabel);
			},
			storeUser: function(jbuser){ 
				$cookies.putObject(storedUserLabel, { idUser: jbuser.idUser, username: jbuser.username, enabled: jbuser.enabled, fullName: jbuser.fullName, 
					process: jbuser.parametersMap.process, readOnly: jbuser.parametersMap.readOnly });
			},
			removeUser(){
				$cookies.remove(storedUserLabel);
			}
			
		}
	}])
	
	.constant("customConfiguration", {})

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//CONTROLLER

	//Logincontroller
	.controller('LoginController', [ '$scope', '$http', '$location', 'jbValidate', 'jbAuthFactory', 'jbUtil', 'customConfiguration',
		function ( $scope, $http, $location, jbValidate, jbAuthFactory, jbUtil, customConfiguration) {
		
		$scope.jbValidate = jbValidate;

		$scope.credentials = {};
		$scope.credentials.rootFolder = null;
		$scope.login = function () {
			 var headers =  {Authorization : "Basic "
			        + jbUtil.b64EncodeUnicode($scope.credentials.username+ ":" +$scope.credentials.password+ ":" +$scope.credentials.rootFolder)
			    };
			    
			$scope.loginPromise =
				$http.get('a/Login',{headers: headers} )
					.then(function (response) {
						var jbuser = response.data;
						jbAuthFactory.storeUser(jbuser);
						$http.get('a/customConfiguration/searchProperties')
						.then(function (response) {
							customConfiguration.value = response.data;				
							$location.url('/home', true);							
						});
						
					});
	
		};

	}])

	//MainController
	.controller('MainController', [ '$scope', '$http', 'jbAuthFactory', function ( $scope, $http, jbAuthFactory) {
		
		$scope.rootFoldersConfiguration = null;

		$http.get('a/customConfiguration/rootFolders').then(function (response) {
			$scope.rootFoldersConfiguration = response.data;
			console.log($scope.rootFoldersConfiguration);
		});
		
		$scope.serverMessageVisible = false;
		$scope.serverMessageString = null;

		$scope.showServerMessage = function (string) {
			$scope.serverMessageString = string;
			$scope.serverMessageVisible = true;
		}

		$scope.hideServerMessage = function () {
			$scope.serverMessageVisible = false;
		}

		$scope.getUser = function () {
			return jbAuthFactory.getUser();
		};

		$scope.isUserLogged = function () {
			var user = $scope.getUser();
			return !!user && !!user.enabled;
		};
		$scope.isReadOnly = function () {var user = $scope.getUser();return user.readOnly; };
		
		$scope.loginError = function () {
			var user = $scope.getUser();
			//return !!user && !!user.enabled && !!user.username;
			return !!user && user.enabled === null && user.username === null;
		};

		$scope.logout = function () {
			$http
				.get("a/Login", { params: { action: 'logout' } })
				.then(function (response) {
					$scope.getUser().enabled = 0;
					jbAuthFactory.removeUser();
				});
		};

		// gestisce lo stato dei popup dei datepicker
		$scope.calendarPopups = [];
		$scope.openCalendar = function (calendarName) {
			$scope.calendarPopups[calendarName] = true;
		};
		
		  $scope.$on('$routeChangeStart', function (scope, next, current) {
		        //if (next && next.$$route && next.$$route.controller == "LoginController") 
	        	if ($scope.getUser() && $scope.getUser().enabled == 1){
		            console.log("BACK");
	        	}
		    });

	}])

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Directive

	//jbPrint, direttiva per la stampa
	.directive('jbPrint', function () {
		return {
			restrict: 'A',
			require: 'form',
			scope: { jbPrint: '@' },
			link: function (scope, elem, attrs, formCtrl) { //funzione di direttiva
				formCtrl.print = function (fmt) {
					if (fmt == 'pdf' || fmt == 'xls') {
						elem.attr('action', scope.jbPrint + '/' + fmt);
						elem.attr('target', '_blank'); //apre una nuova pagina
						elem[0].submit(); //submit dom
					}
					else {
						elem.attr('action', scope.jbPrint);
						elem.attr('target', '_blank'); //apre una nuova pagina
						elem[0].submit(); //submit dom
					}
				};
			}
		};
	})

	// validazione per uguaglianza
	.directive('equals', function () {
		return {
			restrict: 'A', // only activate on element attribute
			require: '?ngModel', // get a hold of NgModelController
			link: function (scope, elem, attrs, ngModel) {
				if (!ngModel) return; // do nothing if no ng-model
				// watch own value and re-validate on change
				scope.$watch(attrs.ngModel, function () {
					validate();
				});
				// observe the other value and re-validate on change
				attrs.$observe('equals', function (val) {
					validate();
				});

				var validate = function () {
					var val1 = ngModel.$viewValue;
					var val2 = attrs.equals;
					ngModel.$setValidity('equals', !val1 || !val2 || val1 === val2);
				};
			}
		}
	})

	.directive('fileDropzone', function ($sce) { // Al momento non usato
		return {
			restrict: 'A',
			scope: {
				file: '=',
				fileName: '='
			},
			link: function (scope, element, attrs) {
				var checkSize, isTypeValid, processDragOverOrEnter, validMimeTypes;
				processDragOverOrEnter = function (event) {
					if (event != null) {
						event.preventDefault();
					}
					event.dataTransfer.effectAllowed = 'copy';
					return false;
				};
				validMimeTypes = attrs.fileDropzone;
				checkSize = function (size) {
					var _ref;
					if (((_ref = attrs.maxFileSize) === (void 0) || _ref === '') || (size / 1024) / 1024 < attrs.maxFileSize) {
						return true;
					} else {
						alert("File must be smaller than " + attrs.maxFileSize + " MB");
						return false;
					}
				};
				isTypeValid = function (type) {
					if ((validMimeTypes === (void 0) || validMimeTypes === '') || validMimeTypes.indexOf(type) > -1) {
						return true;
					} else {
						alert("Invalid file type.  File must be one of following types " + validMimeTypes);
						return false;
					}
				};
				element.bind('dragover', processDragOverOrEnter);
				element.bind('dragenter', processDragOverOrEnter);
				return element.bind('drop', function (event) {
					var file, name, reader, size, type;
					if (event != null) {
						event.preventDefault();
					}
					reader = new FileReader();
					reader.onload = function (evt) {
						if (checkSize(size) && isTypeValid(type)) {
							return scope.$apply(function () {
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
		return function (url) {
			return $sce.trustAsHtml(url);
		};

	}]).filter('yesOrNo',function () {
		return function (input) {
			return input ? 'yes' : 'no';
		};

	});