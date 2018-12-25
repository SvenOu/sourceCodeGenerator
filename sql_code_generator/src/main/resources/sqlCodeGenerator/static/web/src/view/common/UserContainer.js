Ext.define('CGT.view.common.UserContainer', {
	extend: 'Ext.container.Container',
    border: false,
    header: false,
    split: false,
    layout: {
        align: 'right',
        type: 'vbox'
    },
    initComponent : function() {
        var logoutBtn = Ext.create('Ext.Button', {
        	height: 32,
        	width: 80,
        	cls : 'btn-log-off',
            text: 'Log Off',
            id: 'logoff',
            scale: 'medium',
            handler: function() {
//                Ext.Ajax.request({
//                    url: '..'+ app.API_PREFIX +'/security/logoff',
//                    success: function(response){
//                        app.method.printLog(Ext.JSON.decode(response.responseText));
//                        if(Ext.JSON.decode(response.responseText).success == true){
//                            app.user = '';
//                            window.location.href = '../login.html';
//                        }
//                    }
//                });
            	window.location.href = '../j_spring_security_logout';
            }
        });
        this.items = [
            logoutBtn
        ];
        this.callParent(arguments);
    }
});