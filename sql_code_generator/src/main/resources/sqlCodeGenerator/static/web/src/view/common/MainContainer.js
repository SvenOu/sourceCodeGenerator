Ext.define('CGT.view.common.MainContainer', {
	extend: 'Ext.container.Container',
	alias: ['widget.commonMainContainer'],
	region: 'center',
	layout: {
		type:'card'
	},
    cls: 'common-main-container',
    padding: 5,
    initComponent : function() {
    	var me=this;
    	me.homePanel = Ext.create('CGT.view.main.HomePanel');
    	me.sqlPanel = Ext.create('CGT.view.main.SqlPanel');
    	me.javaPanel = Ext.create('CGT.view.main.JavaPanel');
    	me.javaScriptPanel = Ext.create('CGT.view.main.JavaScriptPanel');
        Ext.apply(me,{
        	items: [
				me.homePanel,
				me.sqlPanel,
				me.javaPanel,
				me.javaScriptPanel
        	]
        });
        me.callParent(arguments);
    }
});
