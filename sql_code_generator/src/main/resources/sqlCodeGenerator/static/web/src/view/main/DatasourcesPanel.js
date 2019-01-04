Ext.define('CGT.view.main.DatasourcesPanel', {
	extend: 'Ext.panel.Panel',
	alias: ['widget.datasourcesPanel'],
	layout: {
		type: 'hbox',
		align: 'stretch'
	},
	items: [
        {
            xtype: 'datasourcegrid',
            name : 'dataSourceGrid',
            flex: 1,
            store: Ext.create('CGT.store.generator.Datasource')
        },
        {
        	xtype: 'container',
			name: 'dbFilesTreePanelContainer',
            cls: 'db-files-code-tree-panel-container',
			width: 200,
            layout: {
                type: 'fit'
            },
            items: [
                {
                    cls: 'code-tree-panel',
                    autoScroll: true,
                    name: 'dbFilesTreePanel',
                    xtype: 'treepanel',
                    store: Ext.create('CGT.store.generator.CodeView'),
                    root: {
                        expanded: true,
                        text: "DB Files"
                    }
                }
            ]
        }
	]
});