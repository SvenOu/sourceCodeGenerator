Ext.define('${{packageName}}.model.${{moduleName}}.${{name}}',{
    extend:'Ext.data.Model',
    fields: [
        $tp-repeat(fields-suffixNotIncludeEnd~,){{
        { name: '$(text)',  type: '$(type)'}
        }}
    ]
});