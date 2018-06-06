angular.module('authority', ['ngResource', 'ui.bootstrap', 'ngTable', 'angular.filter'])

	.factory('AuthorityResource', ['$resource', function ($resource) {
		return $resource('', { id: '@id' }, {
			getUsers: {
				isArray: true,
				url: 'a/Authority/users/:charSeq',
				method: 'GET'
			},
			getGroups: {
				isArray: true,
				url: 'a/Authority/groups/:charSeq',
				method: 'GET'
			}
		});
	}])


	.controller('AuthorityController', ['$scope', '$uibModalInstance', 'AUTHORITY_TYPE', 'AuthorityResource', 'jbValidate', 'jbMessages', 'title', 'authType', 'authMany', 'authArray',
		function ($scope, $uibModalInstance, AUTHORITY_TYPE, AuthorityResource, jbValidate, jbMessages, title, authType, authMany, authArray) {

			$scope.jbMessages = jbMessages;
			$scope.jbValidate = jbValidate;
			modalInstance = $uibModalInstance;
			$scope.title = title;
			authArray = authArray;
			$scope.searchAuth;
			$scope.authType = authType;
			$scope.authMany = authMany;
			$scope.AUTHORITY_TYPE = AUTHORITY_TYPE;
			$scope.foundAuthorities = [];
			$scope.selectedAuthorities = [];
			$scope.selectedMap = {};
			$scope.selectedMessage = jbMessages.authority.anySelected;
			$scope.searchMessage = jbMessages.authority.searchAuthority;

			$scope.back = function () {
				$scope.$emit('authorityModalBack');
				modalInstance.dismiss();
			}

			$scope.removeSelected = function (selected) {
				var id = $scope.getAuthorityId(selected);
				delete $scope.selectedMap[id];
				$scope.selectedAuthorities.splice($scope.selectedAuthorities.indexOf(selected), 1);
			}

			$scope.getAuthorityId = function (auth) {
				return ($scope.authType === AUTHORITY_TYPE.PERSON) ? auth.id : auth.fullName;
			}

			$scope.selectAuthority = function (auth) {
				var id = $scope.getAuthorityId(auth);
				if (!$scope.selectedMap[id]) {
					$scope.selectedMap[id] = auth;
					$scope.selectedAuthorities.push(auth);
				}
				return true;
			}

			$scope.searchAuthority = function (form) {
				if (!jbValidate.checkForm(form)) { return; }
				$scope.foundAuthorities = [];
				var authorityPromise;
				var resolveFunction = function () {
					if (authorityPromise.length === 0) {
						$scope.searchMessage = $scope.jbMessages.authority.anyFound;
					}
					$scope.foundAuthorities = authorityPromise;
				}
				if ($scope.authType === AUTHORITY_TYPE.PERSON) {
					authorityPromise = AuthorityResource.getUsers({ charSeq: $scope.searchAuth }, resolveFunction);
				} else if ($scope.authType === AUTHORITY_TYPE.GROUP) {
					authorityPromise = AuthorityResource.getGroups({ charSeq: $scope.searchAuth }, resolveFunction);
				}
			}

			$scope.saveSelected = function () {
				while (authArray.length > 0) {
					authArray.pop();
				}
				for (var i = 0; i < $scope.selectedAuthorities.length; i++) {
					if (!authArray.includes($scope.selectedAuthorities[i])) {
						authArray.push($scope.selectedAuthorities[i]);
					}
				}
				$scope.back();
			}

			$scope.preSelectedAutority = function () {
				for (var i = 0; i < authArray.length; i++) {
					$scope.selectAuthority(authArray[i]);
				}
			}

			$scope.validate = function (form) {
				$scope.jbValidate.showMessage(form);
			}

			$scope.getAuthorityDisplayName = function (authority) {
				if ($scope.authType === AUTHORITY_TYPE.PERSON) {
					var displayName = authority.firstName;
					if (authority.lastName) {
						displayName += " " + authority.lastName;
					}
					displayName += " (" + authority.id + ")";
					return displayName;
				} else if ($scope.authType === AUTHORITY_TYPE.GROUP) {
					return authority.displayName;
				}
			}

			$scope.getAuthorityDisplayName2 = function (found) {
				return $scope.getAuthorityDisplayName(found);
			}
		}])
