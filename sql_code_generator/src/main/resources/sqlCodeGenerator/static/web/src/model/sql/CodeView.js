Ext.define('CGT.model.sql.CodeView',{
    extend:'Ext.data.Model',
    fields: [
        { name: 'text', type: 'string', mapping: 'name'},
        { name: 'path', type: 'string'},
        { name: 'parent', type: 'string'},
        { name: 'leaf', type: 'boolean' },
        { name: 'children'}
    ]
});