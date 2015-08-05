(function() {
	'use strict';

	angular.module('devops-dashboard').controller('featureConfigController',
			featureConfigController);

	featureConfigController.$inject = [ 'modalData', '$modalInstance',
			'collectorData'];

	function featureConfigController(modalData, $modalInstance, collectorData) {
		/* jshint validthis:true */
		var ctrl = this;
		var widgetConfig = modalData.widgetConfig;

		// public variables
		ctrl.toolsDropdownPlaceholder = 'Loading Scope Owners...';
		ctrl.toolsDropdownDisabled = true;

		// public variables
		ctrl.submitted = false;
		ctrl.teamId = widgetConfig.options.teamId;
		ctrl.teamName = widgetConfig.options.teamName;
		ctrl.collectorItemId = null;
		ctrl.collectors = [];
		//ctrl.selectedIndex = 0;

		// public methods
		ctrl.scopeOwners = [];
		ctrl.submit = submitForm;

		// Request collector items
		collectorData.itemsByType('scopeowner').then(processCollectorItemsResponse);
		// Request collectors
        collectorData.collectorsByType('scopeowner').then(processCollectorsResponse);
        
		function processCollectorItemsResponse(data, currentCollectorItemId) {
			var scopeOwners = [];
			//var selectedIndex = data.selectedIndex;
			var featureCollector = modalData.dashboard.application.components[0].collectorItems.ScopeOwner;
			var featureCollectorId = featureCollector ? featureCollector[0].id
					: null;

			scopeOwners = getScopeOwners(data, featureCollectorId);

			function getScopeOwners(data, currentCollectorItemId) {

				for ( var x = 0; x < data.length; x++) {
					var obj = data[x];
					var item = {
						value : obj.id,
						teamId : obj.options.teamId,
						teamName : obj.collector.name + ' - ' + obj.description
					};

					scopeOwners.push(item);

					if (currentCollectorItemId !== null && item.value == currentCollectorItemId) {
						ctrl.selectedIndex = x;
					}
				}

				ctrl.scopeOwners = scopeOwners;
				ctrl.toolsDropdownPlaceholder = 'Select a scope owner';
				ctrl.toolsDropdownDisabled = false;
				if ((ctrl.selectedIndex === undefined) || (ctrl.selectedIndex === null)) {
					ctrl.collectorItemId = '';
				} else {
					ctrl.valid = true;
					ctrl.collectorItemId = ctrl.scopeOwners[ctrl.selectedIndex];
				}
			}
		}
		
		function processCollectorsResponse(data) {
        	ctrl.collectors = data;
        }

		function submitForm(valid, data) {
			ctrl.submitted = true;
			if (valid && ctrl.collectors.length) {
				createCollectorItem(data).then(processCollectorItemResponse);
			}
		}

		function createCollectorItem(data) {
			var item = {
				// TODO - Remove hard-coded versionone reference when mulitple
				// scm collectors become available
				collectorId : _.findWhere(ctrl.collectors, {
					name : "VersionOne"
				}).id,
				options : {
					data : data
				}
			};
			return collectorData.createCollectorItem(item);
		}

		function processCollectorItemResponse(response) {
			var postObj = {
				name : 'feature',
				options : {
					id : widgetConfig.options.id,
					teamName : ctrl.collectorItemId.teamName,
					teamId : ctrl.collectorItemId.teamId
				},

				Id : modalData.dashboard.application.components[0].id,
				collectorItemId : ctrl.collectorItemId.value
			};
			// pass this new config to the modal closing so it's saved
			$modalInstance.close(postObj);
		}
	}
})();
