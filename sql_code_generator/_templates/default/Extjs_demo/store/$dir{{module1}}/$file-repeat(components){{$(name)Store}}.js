Ext.define('${{packageName}}.store.${{moduleName}}.${{name}}',{
    extend:'Ext.data.TreeStore',
    model:'${{packageName}}.model.${{moduleName}}.${{name}}',
    autoLoad: false,
    proxy: {
        type: 'ajax',
        url: '${{url}}',
        reader: {
            type: 'json',
            root:'${{root}}'
        }
    }
});