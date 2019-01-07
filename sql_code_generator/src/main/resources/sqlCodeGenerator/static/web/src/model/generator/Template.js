Ext.define('CGT.model.generator.Template',{
    extend:'Ext.data.Model',
    fields: [
        { name: 'templateId'},
        { name: 'path'},
        { name: 'lock'},
        { name: 'owner' }
    ]
});