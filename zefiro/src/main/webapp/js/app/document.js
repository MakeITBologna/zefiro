/**
 * document resource module
 * 
 */
angular.module('document', ['ngResource', 'ui.bootstrap', 'ngTable', 'documentType', 'angular.filter', 'applicationState'])

.factory('DocumentResource', ['$resource', function($resource) {
	return $resource('a/Document/:id', {id:'@id'}, {
		update: {method:'PUT'},
		updateContent: {
			url:'a/Document/:id/content',
			method: 'PUT'
		},
		getVersions: {
			isArray: true,
			url:'a/Document/:id/versions',
			method: 'GET'
		}
	});
}])

.factory('ItemResource', ['$resource', function($resource) {
	return $resource('a/Item/:id', {id:'@id'}, {
		update: {method:'PUT'}
	});
}])

.factory('RelationResource', ['$resource', function($resource) {
	return $resource('a/Relation/:id',{}, {});
}])

.factory('SearchResource',['$resource', function($resource) {
	return $resource('a/Search/');
}])

.controller('DocumentController', ['$scope', 'DocumentResource', 'DocumentTypeResource', 'ItemResource', 'RelationResource', 'SearchResource', 
	'NgTableParams', 'jbMessages', 'jbPatterns', 'jbValidate', 'jbUtil', 'mioPropertyBlacklist', 'customConfiguration',
function($scope, DocumentResource, DocumentTypeResource, ItemResource, RelationResource, SearchResource, NgTableParams, jbMessages, jbPatterns, 
		jbValidate, jbUtil, mioPropertyBlacklist, customConfiguration) {
	
	$scope.jbMessages = jbMessages;
	$scope.jbPatterns = jbPatterns;
	$scope.jbValidate = jbValidate;
	$scope.jbUtil = jbUtil;
	
	$scope.currentRownum = null;
	
	$scope.editing = false;
	$scope.contentReplace = false;
	$scope.readOnly = false;
	$scope.relation = false;
	
	$scope.searchMatrix = [];
	$scope.documentTemplate = {properties: []};
	$scope.documentEditing = {};
	$scope.documentContentReplace = {};
	$scope.documentVersions = {};
	$scope.summaries = {};
	$scope.documentType = {};
	$scope.documentTypeEdit = {};
	$scope.documentBreadcrumbs = [];
	
	$scope.breadCrumbIndex = -1;
	
    $scope.currentFileName = null;
    $scope.uploadedFileName = null;
    
	$scope.documentTable = new NgTableParams({count: 25}, {});
	$scope.userDocumentTypes = DocumentTypeResource.query($scope.getUser());
	
	$scope.isItem = false;
	$scope.customConfiguration = customConfiguration;
	$scope.customizedSearch = false;
	//-------------------------------------------------------------------------------------------------------------------------------------------------------------
	
	////////////////////////////////////////////////////////////////////////////////////////////
	//RICERCA
	
	//Cambio tipo di documento nella ricerca principale
	$scope.typeChanged = function(form) {
		$scope.summaries = {};
		
		var idType = $scope.documentTemplate.type;
		
		$scope.documentTable.settings({dataset: []});
		$scope.documentTable.reload();
		
		if (!idType) {
			$scope.clearSearch(form);
			$scope.documentType = {};
			$scope.documentTemplate.type = null;
			$scope.searchMatrix = [];
			return;
		}
		
		availableSearchProp = [];
		availableSearchCol = [];
		
		$scope.customConfiguration.value.map(function(d){
			if (d.type == idType){
				availableSearchProp = availableSearchProp.concat(d.searchField);
				availableSearchCol = availableSearchCol.concat(d.searchTableColumn);
			}
		});
		
		$scope.clearSearch(form);
		$scope.setDocumentType("search", idType);
		$scope.documentTemplate.type = idType;
	}
	
	//Ricerca documenti a partire dalla form di ricerca
	$scope.search = function() {
		$scope.documentTemplate.propertyNames = "";
		for( i in $scope.documentType.propertyList){
			$scope.documentTemplate.propertyNames += "," + $scope.documentType.propertyList[i].queryName;
		}
		if($scope.documentTemplate.propertyNames != "")
			$scope.documentTemplate.propertyNames = $scope.documentTemplate.propertyNames.substr(1);
		$scope.documentTable.settings({dataset: []});
		var documentPromise = SearchResource.query($scope.documentTemplate, function() {
			for (i in documentPromise) {
				var d = documentPromise[i];
				if (d.properties) {
					for (j in $scope.documentType.propertyList) {
						var p = $scope.documentType.propertyList[j];
						d[p.queryName] = $scope.getColumnValue(d, p.queryName);
					}
				}
			}
			$scope.documentTable.settings({dataset: documentPromise});
			$scope.updateSummaries(documentPromise);
		});
		return documentPromise;
	}
	
	//Pulisce il form di ricerca
	$scope.clearSearch = function(form) {
		jbValidate.clearForm(form);
		
		$scope.documentTemplate['cmis:description'] = null;
		$scope.documentTemplate['cmis:createdBy'] = null;
		$scope.documentTemplate['cmis:creationDate|GE'] = null;
		$scope.documentTemplate['cmis:creationDate|LE'] = null;		

		$scope.documentTemplate['contains'] = null;
		
		for (i in $scope.documentType.propertyList) {
			var p = $scope.documentType.propertyList[i];
			$scope.documentTemplate[p.queryName] = null;
			$scope.documentTemplate[p.queryName+'|GE'] = null;
			$scope.documentTemplate[p.queryName+'|LE'] = null;
		}
	}
	
	//Aggiorna i valori delle sommatorie nella tabella dei risultati di ricerca
	$scope.updateSummaries = function(documents) {
		$scope.summaries = {};
		for (var i in $scope.documentType.propertyList) {
			var p = $scope.documentType.propertyList[i];

			if ($scope.isNumeric(p.propertyType) && p.queryable) {
				$scope.summaries[p.queryName] = 0;
			}
		}
		
		for (var i = 0; i < documents.length; i++) {
			for (var j in $scope.documentType.propertyList) {
				var p = $scope.documentType.propertyList[j];
				if ($scope.isNumeric(p.propertyType) && p.queryable && documents[i].properties[p.queryName].value) {
					$scope.summaries[p.queryName] += documents[i].properties[p.queryName].value;
				}
			}
		}
	}
	
	//Gestisce la matrice utilizzata per creare il form di ricerca
	$scope.getSearchMatrix = function(aProperties, nColumns, availableSearchProp) {
		
		if (jbUtil.isEmptyObject(aProperties)) return;
		aProperties = availableSearchProp.length > 0? aProperties.filter(prop => availableSearchProp.includes(prop.name)) : aProperties;
		
		var r = [];
		var c = new Array();
		var i = 0;
		for (var j = 0; j < aProperties.length; j++) {
		//	if (aProperties[j].queryable) {
				if (i % nColumns == 0 && i > 0) {
						r.push(c);
						c = new Array();
				}
				
				c.push(aProperties[j]);
				i++;
			//}
		}

		if (c[0]) r.push(c);
		return r;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	//PAGINA DI DETTAGLIO
	
	//Cambio tipo di documento nella pagina di dettaglio
	$scope.typeChangedInsert = function(form) {
		var idType = $scope.documentEditing.type;
		var name = $scope.documentEditing.name;
		var description = $scope.documentEditing.description;

		$scope.clearDetail(form);
		$scope.documentEditing.type = idType;
		$scope.documentEditing.name = name;
		$scope.documentEditing.description = description;
		
		if (!idType) {
			$scope.documentEditing.type = null;
			$scope.documentTypeEdit = {};
			return;
		}
		
		$scope.setDocumentType("edit", idType);
	}
	
	//Apre la pagina di dettaglio per l'inserimento di un nuovo elemento
	$scope.startInsert = function() {
		$scope.currentRownum = null;
		$scope.breadCrumbIndex = 0;
		$scope.documentEditing = {};
		$scope.editing = true;
		$scope.readOnly = false;
		$scope.currentFileName = null;
		$scope.uploadedFileName = null;
		$scope.documentEditing.type = $scope.documentTemplate.type ;
		$scope.documentTypeEdit = {};
		if($scope.documentEditing.type)
			$scope.setDocumentType("edit", $scope.documentEditing.type);
	}
	
	
	$scope.startEdit = function(i, duplicate) {
		$scope.currentRownum = i;
		$scope.breadCrumbIndex = 0;
		$scope.documentEditing = {};
		$scope.documentEditing.id = $scope.documentTable.data[$scope.currentRownum].id;
		
		var baseType = $scope.documentTable.data[$scope.currentRownum].baseType;
		$scope.isItem = baseType === 'cmis:document'? false : true;
		
		var resource = $scope.isItem? ItemResource : DocumentResource;
		var documentPromise = resource.get($scope.documentEditing, function() {
			$scope.documentEditing = documentPromise;
			$scope.documentBreadcrumbs = [];
			$scope.documentBreadcrumbs.push({
				id: $scope.documentEditing.id,
				name: $scope.documentEditing.name,
				description: $scope.documentEditing.description,
				rownum: i
			});
			$scope.editing = true;
			$scope.readOnly = false;
			
			$scope.setDocumentType("edit", $scope.documentEditing.type);
			$scope.getHandledPropertyList($scope.documentEditing.properties);
			
			if (duplicate === true) {
				$scope.currentRownum = null;
				$scope.documentEditing.name = null;
				$scope.documentEditing.id = null;
				$scope.currentFileName = null;
				$scope.uploadedFileName = null;
			} else if (baseType == 'cmis:document'){
				$scope.currentFileName = "a/Document/" + $scope.documentEditing.id + "/preview";
			};
						
			if(baseType == 'cmis:document'){
				$scope.loadVersions($scope.documentEditing.id);
			};
		});

	}
	
	$scope.editItem = function (){
		var itemPromise = ItemResource.get($scope.documentEditing, function() {
			$scope.documentEditing = itemPromise;
			$scope.documentBreadcrumbs = [];
			$scope.documentBreadcrumbs.push({
				id: $scope.documentEditing.id,
				name: $scope.documentEditing.name,
				description: $scope.documentEditing.description,
				rownum: i
			});
			$scope.editing = true;
			$scope.readOnly = false;
			
			$scope.setDocumentType("edit", $scope.documentEditing.type);
			$scope.getHandledPropertyList($scope.documentEditing.properties);
			
			if (duplicate === true) {
				$scope.currentRownum = null;
				$scope.documentEditing.name = null;
				$scope.documentEditing.id = null;
				$scope.currentFileName = null;
				$scope.uploadedFileName = null;
			};
		});
	}
	
	//Carica in documentVersions le versioni del documento con id passato in ingresso alla funzione
	$scope.loadVersions = function(pId){
		DocumentResource.getVersions({id: pId})
		.$promise
		.then(function(data) {
			$scope.documentVersions = {};
			angular.extend($scope.documentVersions, data);
		});
	}
	
	//Modalità visualizzazione documento in sola lettura
	$scope.showDocument = function(shortDocument) {
		$scope.currentRownum = -1;
		$scope.documentEditing = {};
		$scope.documentEditing.id = shortDocument.id;
		
		var resource = shortDocument.baseType == "cmis:item"? ItemResource : DocumentResource;
		
		var documentPromise = resource.get($scope.documentEditing, function() {
			
			$scope.isItem = shortDocument.baseType === 'cmis:document'? false : true;
			
			$scope.documentEditing = documentPromise;
			$scope.documentBreadcrumbs.push(shortDocument);
			
			$scope.editing = true;
			$scope.readOnly = true;
			
			$scope.breadCrumbIndex++;
			$scope.setDocumentType("edit", $scope.documentEditing.type);
			
			if(!$scope.isItem) {
				$scope.loadVersions($scope.documentEditing.id);
				$scope.currentFileName = "a/Document/" + $scope.documentEditing.id + "/preview";
			}
			
			
		});
	}
	
	//Inserisce / modifica elemento
	$scope.saveDetail = function(form) {
		var resource = $scope.isItem? ItemResource : DocumentResource;

		if ($scope.currentRownum != null) {
			resource
				.update($scope.documentEditing)
				.$promise
				.then(function(data) {
					if (data && data.properties) {
						for (j in $scope.documentType.propertyList) {
							var p = $scope.documentType.propertyList[j];
							data[p.queryName] = $scope.getColumnValue(data, p.queryName);
						}
					}
					angular.extend($scope.documentTable.data[$scope.currentRownum], data);
					$scope.closeDetail(form);
					$scope.documentTable.reload();
				});
		} else {
			$scope.documentEditing.createdBy = $scope.getUser().fullName;
			$scope.documentEditing.created = Date.now();
			$scope.documentEditing.uploadedFileName = $scope.uploadedFileName;
			if($scope.documentEditing.properties == null){
				$scope.documentEditing.properties = {};
			}
			for(var i = 0; i < $scope.documentTypeEdit.propertyList.length ; i++){
				var p =  $scope.documentTypeEdit.propertyList[i];
				
				if ($scope.documentEditing.properties[p.queryName] == undefined) {
					$scope.documentEditing.properties[p.queryName] = {};
				}
				
				angular.extend($scope.documentEditing.properties[p.queryName], {
					queryName: p.queryName,
					propertyType: p.propertyType
				});
			}
			resource.save($scope.documentEditing)
				.$promise
				.then(function(data) {
					if($scope.documentTable.settings().dataset){
						$scope.documentTable.settings().dataset.unshift(data);
						$scope.documentTable.data.unshift(data);
					}
					$scope.closeDetail(form);
				});
		}
	}
	
	//Pulisce il form della pagina di dettaglio
	$scope.clearDetail = function(form) {
		jbValidate.clearForm(form);
		$scope.documentEditing = {
			id: null,
			name: null,
			description: null,
			type: null,
			version: null,
			created: null,
			createdBy: null,
			properties: null
		};
	}
	
	//Chiude la pagina di dettaglio
	$scope.closeDetail = function(form) {
		$scope.editing = false;
		$scope.readOnly = false;
		if (form) $scope.clearDetail(form);
	}
	
	//Gestione anteprima nel dettaglio
	$scope.getDocumentObjectHTML = function(){
		return "<object data=\"" + $scope.currentFileName + "\" width=\"100%\" style=\"height: 100vh;\" ></object>";
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////
	//ELIMINAZIONE  / DUPLICAZIONE
	
	$scope.deleteRow = function(i) {
		$scope.documentEditing = {};
		$scope.documentEditing.id = $scope.documentTable.data[i].id;
		DocumentResource.delete($scope.documentEditing, function() {
			var j = jbUtil.findRowWithKey($scope.documentTable.settings().dataset, 'id', $scope.documentEditing.id);
			$scope.documentTable.settings().dataset.splice(j, 1);
			$scope.documentTable.data.splice(i, 1);
			$scope.documentTable.reload();
		});
	}
	
	$scope.startDuplicate = function(i) {
		$scope.startEdit(i, true);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	//UTILITA'
	
	//Aggiorna la lista delle proprietà del tipo documento
	$scope.setDocumentType = function(context, id, callback){

		var documentTypePromise = DocumentTypeResource.get({id: id}, function() {
			
			angular.extend(documentTypePromise, {propertyList: $scope.getHandledPropertyList(documentTypePromise.properties)});
			
			var relationTypesPromise = DocumentTypeResource.getRelations({id: id}, function() {
				angular.extend(documentTypePromise, {relationTypes: relationTypesPromise});
				
				if (context == "search") {
					$scope.documentType = documentTypePromise;
					$scope.searchMatrix = $scope.getSearchMatrix($scope.documentType.propertyList, 2, availableSearchProp);
					$scope.documentType.propertyList = availableSearchCol.length > 0? $scope.documentType.propertyList.filter(prop => availableSearchCol.includes(prop.name)) : $scope.documentType.propertyList;
				}
				else {
					$scope.documentTypeEdit = documentTypePromise;
				}

				if (callback) callback();
			});
		});
	}
 
	//Gestione Breadcrumb
	$scope.gotoDocumentBreadcrumb = function(i, form) {
		if (i < 0) {
			$scope.documentBreadcrumbs = [];
			$scope.closeDetail(form);
			return;
		}
		var shortDocument = $scope.documentBreadcrumbs[i];
		$scope.breadCrumbIndex = i-1;
		if (i == 0) {
			$scope.startEdit(shortDocument.rownum);
		} else {
			$scope.documentBreadcrumbs = $scope.documentBreadcrumbs.slice(0, i);
			$scope.showDocument(shortDocument);
		}
	}
	
	$scope.getColumnValue = function(doc, propName) {
		var p = doc.properties[propName];
		
		if (p == undefined || p.value == undefined) {
			return null;
		}
		
		return p.value;
	}	
	
	$scope.isNumeric = function(type) {
		return (type == 'INTEGER' || type == 'DECIMAL');
	}
	
	$scope.setCurrentFileName = function(newFileName, pUploaded, pUserFilename){
		if(pUploaded === true){
			$scope.uploadedFileName = newFileName;
			$scope.documentEditing.name = pUserFilename;
		} else {
			$scope.currentFileName = newFileName;
		}
	}
	
	//Elimina dalla lista di proprietà passata gli elementi presenti nella blacklist
	$scope.getHandledPropertyList = function(allPropertiesObject){
		for (i in mioPropertyBlacklist) {
			delete allPropertiesObject[mioPropertyBlacklist[i]];
		}
		
		var propertyList = jbUtil.toArray(allPropertiesObject);
		propertyList.sort(function(p, s) {
			return (p.displayName < s.displayName)? -1 : 1;
		});
		
		return propertyList;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//NUOVA VERSIONE
	
	//Avvia la modalità per la creazione di una nuova versione
	$scope.startContentReplace = function(){
		$scope.contentReplace = true;
		$scope.editing = false;
		$scope.documentContentReplace = {};
	}
	
	//Crea una nuova versione
	$scope.updateContent = function(form) {
		DocumentResource
			.updateContent({id:$scope.documentEditing.id, name:$scope.documentContentReplace.comment , version: $scope.documentContentReplace.version , uploadedFileName: $scope.uploadedFileName})
			.$promise.then(function(data) {
				//Aggiorna le versioni, la pagina di modifica e la tabella di ricerca
				$scope.documentEditing.version = data.version;
				$scope.documentEditing.id = data.id;
				$scope.documentEditing.uploadedFileName = data.uploadedFileName;
				angular.extend($scope.documentTable.data[$scope.currentRownum], $scope.documentEditing);
				jbValidate.clearForm(form);
				$scope.contentReplace = false;
				$scope.startEdit($scope.currentRownum,false);
		});
	}
	
	//Chiude la modalità per la creazione di una nuova versione
	$scope.closeContentReplace = function(form,replace){
		$scope.contentReplace = false;
		$scope.editing = true;
		if(replace === true){
			$scope.currentFileName = "a/Document/" + $scope.documentEditing.id + "/preview";
		}
		if (form) jbValidate.clearForm(form);
	}
		
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//GESTIONE RELAZIONI
	
	// apre la pagina di ricerca per selezionare un documento e aggiungerlo alla relazione
	$scope.startAddRelation = function(relType, documentId){
		$scope.editing = false;
		$scope.relation = true;
		$scope.addingRelationType = relType;
		// salvo lo stato della pagina di ricerca
		$scope.searchStatus =  angular.extend({}, {
			documentTemplate: $scope.documentTemplate, 
			searchMatrix: $scope.searchMatrix,
			dataTable: $scope.documentTable
		});
		// determino il tipo del documento da aggiungere e il verso della relazione
		var fixedType = relType.allowedTargetTypes[0].id;
		$scope.relationSide = 'target';
		
		if (fixedType == $scope.documentTypeEdit.id) {
			fixedType = relType.allowedSourceTypes[0].id;
			$scope.relationSide = 'source';
		}
		
		// predispongo la pagina ricerca
		$scope.documentTable = new NgTableParams({count: 25}, {});
		$scope.documentTemplate = {properties: []};
		$scope.documentTemplate.type = fixedType;
		$scope.setDocumentType("search", fixedType);
	}
	
	//Aggiunge la relazione selezionata
	$scope.addRelation = function(selectedId){
		var source = selectedId;
		var target = $scope.documentEditing.id;
		if($scope.relationSide == 'target'){
			source = $scope.documentEditing.id;
			target = selectedId;
		}	
		RelationResource.save({typeId: $scope.addingRelationType.id, sourceId: source , targetId: target })
		.$promise
		.then(function(data) {
			$scope.closeAddRelation();
			$scope.startEdit($scope.currentRownum, false);
		}, function(data){
				if(data.status = 422){
					$scope.showServerMessage(data.data.message);
				}
		});
	}
	
	//Elimina la relazione
	$scope.delRelation = function(pId){
		RelationResource.delete({id : pId}, function(){
			$scope.startEdit($scope.currentRownum, false);
		});
	}
	
	//Chiude la modalità di aggiunta relazione
	$scope.closeAddRelation = function(){
		$scope.relation = false;
		$scope.editing = true;
		$scope.hideServerMessage();
		// ripristino lo stato della ricerca precedente
		$scope.documentTemplate = $scope.searchStatus.documentTemplate;
		$scope.searchMatrix = $scope.searchStatus.searchMatrix;
		//Senza la doppia assegnazione ci sono problemi di refresh 
		$scope.documentTable = $scope.searchStatus.dataTable;
		$scope.documentTable.reload();
		
		// reimposto il tipo e ricalcolo i totali
		if($scope.documentTemplate.type != null){
			$scope.setDocumentType("search", $scope.documentTemplate.type, function(){ 
				$scope.updateSummaries($scope.searchStatus.dataTable);
				$scope.searchStatus = {};
			});
		} else {
			$scope.documentType = {};
			$scope.updateSummaries($scope.searchStatus.dataTable);
			$scope.searchStatus = {};
		}
	}
	
	$scope.getRelationName = function(relType){

		if (relType.allowedSourceTypes[0].id == $scope.documentTypeEdit.id ||
				jbUtil.contains($scope.documentTypeEdit.aspects, relType.allowedSourceTypes[0].id)) {
			return relType.allowedTargetTypes[0].name;
		}
		
		if (relType.allowedTargetTypes[0].id == $scope.documentTypeEdit.id ||
				jbUtil.contains($scope.documentTypeEdit.aspects, relType.allowedTargetTypes[0].id)) {
			return relType.allowedSourceTypes[0].name;
		}

		return relType.name;
	}
	
	$scope.isTypeSpecific = function(relType){
		// se il tipo corrente prende parte alla relazione come source, controllo il target
		if (relType.allowedSourceTypes[0].id == $scope.documentTypeEdit.id ||
				jbUtil.contains($scope.documentTypeEdit.aspects, relType.allowedSourceTypes[0].id)) {
			return !relType.allowedTargetTypes[0].isSecondary;
		}
		
		// altrimenti controllo il source
		return !relType.allowedSourceTypes[0].isSecondary;
	}
	
	$scope.isRelationOfType = function(relationType, side, documentId){
		return function(relation) {
			
			console.log(relation);
			console.log(relationType);
			console.log(documentId);
			
			return relation.type.id == relationType.id && relation[side].id == documentId;
		};
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
	$scope.selectDocument = function(item){
		$scope.$emit('DocumentSelected',item);
		return item;
	}
	
	$scope.clearAllSearch = function(form){
		$scope.clearSearch(form);
		$scope.documentTable = new NgTableParams({count: 25}, {});
	}
	
	$scope.back = function(form){
		$scope.clearAllSearch(form);
		$scope.$emit('DocumentBack');
	}
}])

.directive('jbUpload', function ($http) {
    return {
    	scope: {jbUpload : "&"} ,
        restrict: 'A',
        link: function (scope, element, attr) {
        	var button = element.next().find("button");
            button.bind('click', function () {
            	var file = element[0].files[0];
            	var filename = (file)? file.name : null;
            	var previewable = filename != null && filename.match(/\.pdf$|\.jpg$|\.jpeg$|\.gif$|\.png$|\.tiff$|\.bmp$|\.svg$/i);
            	            	
                var formData = new FormData();
                formData.append('file', file);
                
                scope.jbUpload({
                	newUrl: (previewable)? URL.createObjectURL(file) : 'document/preview_unavailable.jpg',
                	uploaded: false,
                	userFilename: null
                });
                
                scope.$parent.uploadPromise = $http({
                    url: "a/File",
                    method: "POST",
                    data: formData,
                    headers: {'Content-Type': undefined}
                }).success(function (response) {
                	scope.jbUpload({
                		newUrl: "document/" + response.fileName,
                		uploaded :true,
                		userFilename: filename
                	});
                });
            });
        }
    };
});
