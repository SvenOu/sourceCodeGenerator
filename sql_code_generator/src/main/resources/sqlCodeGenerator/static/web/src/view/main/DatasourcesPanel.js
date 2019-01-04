Ext.define('CGT.view.main.DatasourcesPanel', {
	extend: 'Ext.panel.Panel',
	alias: ['widget.datasourcesPanel'],
	layout: {
		type: 'vbox',
		align: 'stretch'
	},
	items: [
		{
			xtype: 'datasourcegrid',
            name : 'dataSourceGrid',
            store: Ext.create('CGT.store.generator.Datasource')
		}
	]
});