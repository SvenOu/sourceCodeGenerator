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
    	'MainController',
    	'HomeController'
	],
    models: [
		'main.SideDataView',
		'sql.CodeView',
	],
    stores: [
        'main.SideDataView',
        'sql.CodeView',
    ],
    views: [
    	// comman
		'common.HeaderPanel',
		'common.MainContainer',
		'common.SidebarDataView',
		'common.UserContainer',
		'common.Viewport',
		'common.SqlDataBaseConfigWindow',

		//  main
        'main.HomePanel',
        'main.JavaPanel',
        'main.JavaScriptPanel',
        'main.SqlPanel'
	],
    //remove the loading-indicator after EXTAPP has been launched
	launch: function() {
		var me = this;

		/************************ test **********************/
        var userName = Ext.util.Cookies.get("m_userName"),
            password = Ext.util.Cookies.get("m_password");
        app.user = {
            userId: userName,
            password: password
		};
        if(Ext.isEmpty(userName)){
            Ext.Msg.show({
                title: 'Message',
                msg: 'please login !',
                buttons: Ext.MessageBox.OK,
                fn: function () {
                    window.location.href = '../login.html';
                }
            });
			return;
		}
		Ext.Ajax.on('beforerequest', function (conn, options, eOpts) {
			if(!options.params){
                options.params = {};
			}
            options.params.userId = app.user.userId;
        });
        /************************ end test **********************/
// 		Ext.Ajax.request({
// 			type : "GET",
// 			dataType : 'json',
// 			url : "..'+ app.API_PREFIX +'/security/getUserAccess",
// 		    success: function(response){
// 		    	var responseText = Ext.JSON.decode(response.responseText);
// 		    	if(responseText.success == true){
// 					app.user = responseText.data;
					Ext.create('CGT.view.common.Viewport');
					Ext.get('loading').fadeOut({
						remove: true
					});
					me.initExtJSConfig();
// 		    	}else {
// //		    		window.location.href = '../login.html';
// 		    	}
// 		    },
// 		    failure: function() {
// //		    	window.location.href = '../login.html';
// 		    }
// 		});
//		Ext.Ajax.on('requestcomplete',me.checkLogin);
				
	},
	initExtJSConfig:function(){
        //fix the panel.tool.tooltip width bug on chrome
    	delete Ext.tip.Tip.prototype.minWidth;
		if(Ext.isIE10) { 
		    Ext.supports.Direct2DBug = true;
		}
	}
}); 
