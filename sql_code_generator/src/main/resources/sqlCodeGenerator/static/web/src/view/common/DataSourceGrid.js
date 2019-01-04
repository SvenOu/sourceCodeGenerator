Ext.define('CGT.view.common.DataSourceGrid', {
	extend: 'Ext.grid.Panel',
	alias: ['widget.datasourcegrid'],
    bodyCls: 'main-data-sources-grid',
    viewConfig: {
        enableTextSelection: true
    },
    columns: {
        defaults: {
            menuDisabled: true,
            sortable: true
        },
        items: [
            { text: 'data_source_id',  dataIndex: 'dataSourceId', width: 190},
            { text: 'type',  dataIndex: 'type'},
            { text: 'url',  dataIndex: 'url', flex: 1},
            { xtype : 'checkcolumn', text: 'lock',  dataIndex: 'lock',
                listeners: {
                    beforecheckchange: function (column, rowIndex, checked, eOpts) {
                        // return !column.up('grid').getStore().getAt(rowIndex).get('lock');
                        return false;
                    }
            }},
            {
                xtype:'actioncolumn',
                text: 'Delete',
                align: 'center',
                width: 70,
                items: [{
                    icon: 'images/delete.png',
                    tooltip: 'delete this row',
                    handler: function (view, rowIndex, colIndex, item, e, record, row) {
                        this.fireEvent('deleteBtnClick', view, rowIndex, colIndex, item, e, record, row);
                    }
                }]
            }
        ]
    },
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
});