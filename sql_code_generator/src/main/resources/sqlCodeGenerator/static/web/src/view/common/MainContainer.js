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
    	me.generatorPanel = Ext.create('CGT.view.main.GeneratorPanel');
    	me.templatesPanel = Ext.create('CGT.view.main.TemplatesPanel');
    	me.datasourcesPanel = Ext.create('CGT.view.main.DatasourcesPanel');
    	me.testPanel = Ext.create('CGT.view.main.TestPanel');
        Ext.apply(me,{
        	items: [
				me.generatorPanel,
				me.templatesPanel,
				me.datasourcesPanel,
				me.testPanel
        	]
        });
        me.callParent(arguments);
    }
});
