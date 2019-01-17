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
    	'TemplateController',
    	'GeneratorController'
	],
    models: [
		'main.SideDataView',
		'generator.CodeView',
		'generator.Datasource',
		'generator.DatasourceType',
		'generator.Template',
	],
    stores: [
        'main.SideDataView',
        'generator.CodeView',
        'generator.Datasource',
        'generator.DatasourceType',
        'generator.Template',
    ],
    views: [
    	// comman
		'common.HeaderPanel',
		'common.MainContainer',
		'common.SidebarDataView',
		'common.Viewport',
		'common.SqlRemoteConfigWindow',
		'common.SqlFileConfigWindow',
		'common.UploadTemplateFileWindow',
		'common.DataSourceGrid',
		'common.TemplateGrid',
		'common.CodeEditor',
		'common.JsonEditorWindow',
		'common.FileActionWindow',
		'common.DocWindow',
		'common.FileSystemTree',

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
                var responseText = {};
                try {
                     responseText = Ext.JSON.decode(response.responseText);
                }catch (e) {}

		    	if(responseText.success == true){
					app.user = responseText.data;
					Ext.create('CGT.view.common.Viewport');
					Ext.get('loading').fadeOut({
						remove: true
					});
					me.initExtJSConfig();
		    	}else {
		    		window.location.href = '/webs/security/login.html';
		    	}
		    },
		    failure: function() {
		    	window.location.href = '/webs/security/login.html';
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
