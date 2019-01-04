Ext.define('CGT.model.generator.Datasource',{
    extend:'Ext.data.Model',
    fields: [
        { name: 'dataSourceId'},
        { name: 'type'},
        { name: 'url'},
        { name: 'lock' }
    ]
});