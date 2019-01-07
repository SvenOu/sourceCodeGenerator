Ext.define('CGT.view.common.TemplateGrid', {
	extend: 'Ext.grid.Panel',
	alias: ['widget.templategrid'],
    bodyCls: 'main-common-template-grid',
    viewConfig: {
        enableTextSelection: true
    },
    columns: {
        defaults: {
            menuDisabled: true,
            sortable: true
        },
        items: [
            { text: 'data_source_id',  dataIndex: 'templateId', width: 190},
            { text: 'url',  dataIndex: 'path', flex: 1},
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
    }
});