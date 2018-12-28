Ext.define('CGT.view.common.Viewport',{
    extend: 'Ext.container.Viewport', 
    alias: ['widget.commonViewport'],
    layout:{
		type: 'border',
		padding: 0
	},
	cls: 'common-viewport',
    initComponent : function() {
    	var me=this;
        me.gotoMaster(me);
        this.callParent(arguments);
    },
    gotoDefaultLocation: function(me){
        app.method.toastMsg('Warning', 'Not found!');
    },
    gotoMaster: function(me){
        me.sidebar = Ext.create('Ext.panel.Panel',{
            region: 'west',
            title: 'Menu',
            cls: 'left-slide-data-menu',
            border: false,
            animCollapse: true,
            collapsible: true,
            split: true,
            width: 110,
            items:[
                Ext.create('CGT.view.common.SidebarDataView',{name: 'commonSidebarDataView'})
            ]
        });
        Ext.apply(me,{
			items:[ 
				Ext.create('CGT.view.common.HeaderPanel',{name: 'headerPanel'}),
				Ext.create('CGT.view.common.MainContainer',{name: 'mainContainer'}),
				me.sidebar
			]
        });     
		me.on('afterrender',me.createLoading,me);	
    },
    createLoading: function(thisViewport, eOpts){
    	thisViewport.loading = new Ext.LoadMask(Ext.get(this.getEl()),{
            msg : 'Loading...',
            removeMask : true
		});
	},
    gotoClient: function(me){
    	Ext.apply(me,{
    		layout:{
    			type: 'fit'
    		},
			items:[ 
				Ext.create('CGT.view.client.MainContainer',{name: 'clientMainContainer'})
			]
        });
    }
});