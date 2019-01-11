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
                text: 'Edit',
                align: 'center',
                width: 70,
                items: [{
                    icon: 'images/table_edit.png',
                    tooltip: 'edit this row',
                    handler: function (view, rowIndex, colIndex, item, e, record, row) {
                        this.fireEvent('editBtnClick', view, rowIndex, colIndex, item, e, record, row);
                    },
                    getClass: function(v, metadata, r, rowIndex, colIndex, store){
                        if(r.get('type') === 'sqlite'){
                            metadata.css = 'hide-cell';
                        }
                    }
                }]
            },
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
    }
});