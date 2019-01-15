Ext.define('${{packageName}}.model.${{moduleName}}.${{name}}Model',{
    extend:'Ext.data.Model',
    fields: [
        $tp-repeat(fields-suffixNotIncludeEnd~,){{
        { name: '$(text)',  type: '$(type)'}
        }}
    ]
});
