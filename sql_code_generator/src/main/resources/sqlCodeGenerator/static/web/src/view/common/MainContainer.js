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
    	me.templateDetailPanel = Ext.create('CGT.view.main.TemplateDetailPanel');
    	me.androidFileManagerPanel = Ext.create('CGT.view.main.AndroidFileManagerPanel');
        Ext.apply(me,{
        	items: [
				me.generatorPanel,
				me.templatesPanel,
				me.datasourcesPanel,
				me.templateDetailPanel,
				me.androidFileManagerPanel
        	]
        });
        me.callParent(arguments);
    }
});
