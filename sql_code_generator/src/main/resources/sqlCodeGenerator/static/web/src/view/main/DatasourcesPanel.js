Ext.define('CGT.view.main.DatasourcesPanel', {
	extend: 'Ext.panel.Panel',
	alias: ['widget.datasourcespanel'],
	layout: {
		type: 'hbox',
		align: 'stretch'
	},
    // custom attr
    contentValues: {
        m_mode: 'default', //  default, choose
        m_chooseFrom: null,
        m_lastChooseVal: null,
        m_callBack: null
    },
	items: [
        {
            xtype: 'datasourcegrid',
            name : 'dataSourceGrid',
            flex: 1,
            store: Ext.create('CGT.store.generator.Datasource'),
            tbar: {
                layout: 'hbox',
                items: [
                    {
                        xtype: 'container',
                        layout: {
                            type: 'hbox',
                            align: 'middle'
                        },
                        items: [
                            {
                                xtype: 'combobox',
                                name: 'dataSourceType',
                                fieldLabel: 'type',
                                store: Ext.create('CGT.store.generator.DatasourceType'),
                                labelWidth: 50,
                                queryMode: 'remote',
                                displayField: 'type',
                                valueField: 'type',
                            },
                            {
                                name: 'addDatasourceBtn',
                                xtype: 'button',
                                margin: 10,
                                text: 'add datasource'
                            },
                            {
                                name: 'refreshDbFiles',
                                xtype: 'button',
                                margin: 10,
                                text: 'refresh dataSource files'
                            }
                        ]
                    }
                ]
            }
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