Ext.Loader.setConfig({enabled: true});  
Ext.require([ 
	'Ext.app.Application',
	'Ext.app.Controller' 
]);
Ext.application({  
    name: 'CGT',
    appFolder: 'src',
    controllers: [
    	'ApplicationController',
    	'DataSourceController',
    	'MainController',
    	'TemplatesDetailController',
    	'GeneratorController'
	],
    models: [
		'main.SideDataView',
		'generator.CodeView',
		'generator.Datasource',
		'generator.DatasourceType',
	],
    stores: [
        'main.SideDataView',
        'generator.CodeView',
        'generator.Datasource',
        'generator.DatasourceType',
    ],
    views: [
    	// comman
		'common.HeaderPanel',
		'common.MainContainer',
		'common.SidebarDataView',
		'common.Viewport',
		'common.SqlDataBaseConfigWindow',
		'common.SqlRemoteConfigWindow',
		'common.SqlFileConfigWindow',
		'common.DataSourceGrid',

		//  main
        'main.GeneratorPanel',
        'main.TemplatesPanel',
        'main.DatasourcesPanel',
        'main.TemplateDetailPanel'
	],
    //remove the loading-indicator after EXTAPP has been launched
	launch: function() {
		var me = this;
        /************************ end test **********************/
		Ext.Ajax.request({
			type : "GET",
			dataType : 'json',
			url : app.SECURITY_API_PREFIX + '/getCurrentUserDetails',
		    success: function(response){
		    	var responseText = Ext.JSON.decode(response.responseText);
		    	if(responseText.success == true){
					app.user = responseText.data;
					Ext.create('CGT.view.common.Viewport');
					Ext.get('loading').fadeOut({
						remove: true
					});
					me.initExtJSConfig();
		    	}else {
//		    		window.location.href = '../login.html';
		    	}
		    },
		    failure: function() {
//		    	window.location.href = '../login.html';
		    }
		});
	},
	initExtJSConfig:function(){
        //fix the panel.tool.tooltip width bug on chrome
    	delete Ext.tip.Tip.prototype.minWidth;
		if(Ext.isIE10) { 
		    Ext.supports.Direct2DBug = true;
		}
	}
}); 
