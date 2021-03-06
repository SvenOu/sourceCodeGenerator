Ext.application({
    name: '${{packageName}}',
    appFolder: 'src',
    controllers: [
        $tp-repeat(components-suffixNotIncludeEnd~,){{
        '$(moduleName).$(name)Controller'
        }}
	],
    models: [
        $tp-repeat(components-suffixNotIncludeEnd~,){{
        '$(moduleName).$(name)Model'
        }}
	],
    stores: [
        $tp-repeat(components-suffixNotIncludeEnd~,){{
        '$(moduleName).$(name)Store'
        }}
    ],
    views: [
        $tp-repeat(components-suffixNotIncludeEnd~,){{
        '$(moduleName).$(name-upCaseFirst)Grid'
        }}
	],
	launch: function() {
        var me = this;
        ne.callParent();
    }
}); 
