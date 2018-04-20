/**
 * documentType resource module
 * 
 */
angular.module('documentType', ['ngResource', 'ui.bootstrap', 'ngTable'])

.constant("mioPropertyBlacklist", [
                                
"alfcmis:nodeRef",
"cm:author",
"cm:description",
"cm:lastThumbnailModification",
"cm:title",
"cmis:isImmutable",
"cmis:versionLabel",
"cmis:objectTypeId",
"cmis:description",
"cmis:createdBy",
"cmis:checkinComment",
"cmis:creationDate",
"cmis:isMajorVersion",
"cmis:contentStreamFileName",
"cmis:name",
"cmis:isLatestVersion",
"cmis:lastModificationDate",
"cmis:contentStreamLength",
"cmis:objectId",
"cmis:lastModifiedBy",
"cmis:secondaryObjectTypeIds",
"cmis:contentStreamId",
"cmis:contentStreamMimeType",
"cmis:changeToken",
"cmis:isPrivateWorkingCopy",
"cmis:versionSeriesCheckedOutBy",
"cmis:isVersionSeriesCheckedOut",
"cmis:versionSeriesId",
"cmis:isLatestMajorVersion",
"cmis:versionSeriesCheckedOutId",
"cmis:baseTypeId",
"exif:dateTimeOriginal",
"exif:exposureTime",
"exif:fNumber",
"exif:flash",
"exif:focalLength" ,
"exif:isoSpeedRatings",
"exif:manufacturer",
"exif:model" ,
"exif:orientation" ,
"exif:pixelXDimension",
"exif:pixelYDimension" ,
"exif:resolutionUnit" ,
"exif:software",
"exif:xResolution" ,
"exif:yResolution" 

])

.factory('DocumentTypeResource', ['$resource', function($resource) {
	return $resource('a/DocumentType/:id', {}, {
		update: {
			method:'PUT'
		},
		getRelations: {
			method:'GET',
			url:'a/DocumentType/:id/relation',
			isArray: true
		}
	});
}])

.controller('DocumentTypeController', ['$scope', 'DocumentTypeResource', 'NgTableParams', 'jbMessages', 'jbPatterns', 'jbValidate', 'jbUtil',
function($scope, DocumentTypeResource, NgTableParams, jbMessages, jbPatterns, jbValidate, jbUtil) {
	
	$scope.jbMessages = jbMessages;
	$scope.jbPatterns = jbPatterns;
	$scope.jbValidate = jbValidate;
	
	$scope.currentRownum;
	$scope.editing = false;
	$scope.documentTypeList = [];
	$scope.documentTypeTemplate = {};
	$scope.documentTypeEditing = {};

	$scope.documentTypeTable = new NgTableParams({}, {});

	$scope.search = function() {
		$scope.documentTypeList = [];
		var documentTypePromise = DocumentTypeResource.query($scope.documentTypeTemplate, function() {
			$scope.documentTypeList = documentTypePromise;
			$scope.documentTypeTable.settings({dataset: $scope.documentTypeList});
		});
		return documentTypePromise;
	}
	
	$scope.clearSearch = function(form) {
		jbValidate.clearForm(form);
		$scope.documentTypeTemplate = {

		};
	}
	
	$scope.deleteRow = function(i) {
		var j = jbUtil.getModelIndex($scope.documentTypeTable, i);
		$scope.documentTypeEditing = {};
		$scope.documentTypeEditing.id = $scope.documentTypeList[j].id;
		DocumentTypeResource.delete($scope.documentTypeEditing, function() {
			$scope.documentTypeList.splice(j, 1);
			$scope.documentTypeTable.reload();
		});
	}
	
	$scope.startInsert = function() {
		$scope.currentRownum = null;
		$scope.documentTypeEditing = {};
		$scope.editing = true;
	}
	
	$scope.startEdit = function(key, i) {
		$scope.currentRownum = jbUtil.getModelIndex($scope.documentTypeTable, i);;
		$scope.documentTypeEditing = {};
		$scope.documentTypeEditing.id = key;
		var documentTypePromise = DocumentTypeResource.get($scope.documentTypeEditing, function() {
			$scope.documentTypeEditing = documentTypePromise;
			$scope.editing = true;
		});
	}
	
	$scope.startDuplicate = function(key, i) {
		$scope.startEdit(key, i);
		$scope.currentRownum = null;
	}
	
	$scope.saveDetail = function(form) {
		if ($scope.currentRownum != null) {
			DocumentTypeResource
				.update($scope.documentTypeEditing)
				.$promise
				.then(function(data) {
					angular.extend($scope.documentTypeList[$scope.currentRownum], data);
					$scope.closeDetail(form);
					$scope.documentTypeTable.reload();
				});
		} else {
			DocumentTypeResource.save($scope.documentTypeEditing)
				.$promise
				.then(function(data) {
					$scope.documentTypeList.unshift(data);
					$scope.closeDetail(form);
					$scope.documentTypeTable.reload();
				});
		}
	}
	
	$scope.clearDetail = function(form) {
		jbValidate.clearForm(form);
		$scope.documentTypeEditing = {
			id: null,
			name: null,
			properties: null
		};
	}
	
	$scope.closeDetail = function(form) {
		$scope.editing = false;
		if (form) $scope.clearDetail(form);
	}
	
}]);
