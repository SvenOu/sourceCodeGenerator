Ext.define('${{packageName}}.view.${{moduleName}}.${{name-upCaseFirst}}Grid', {
	extend: 'Ext.grid.Panel',
	alias: 'widget.${{name-lowerCaseALL}}grid',
    bodyCls: '${{moduleName}}-${{name-lowerCaseALL}}-grid',
    viewConfig: {
        enableTextSelection: true
    },
    columns: {
        defaults: {
            menuDisabled: true,
            sortable: true
        },
        items: [
            $tp-repeat(fields-suffixNotIncludeEnd~,){{
            { text: '$(text)',  dataIndex: '$(dataIndex)'}
            }}
        ]
    }
});
