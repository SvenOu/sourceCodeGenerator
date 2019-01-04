Ext.define('CGT.model.generator.CodeView',{
    extend:'Ext.data.Model',
    fields: [
        { name: 'text', type: 'string', mapping: 'name'},
        { name: 'path', type: 'string'},
        { name: 'parent', type: 'string'},
        { name: 'leaf', type: 'boolean' },
        { name: 'children'}
    ]
});