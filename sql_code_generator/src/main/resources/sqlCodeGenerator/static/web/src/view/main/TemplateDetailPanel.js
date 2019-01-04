Ext.define('CGT.view.main.TemplateDetailPanel', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.templatedetailpanel',
    layout: {
        type: 'fit'
    },
    items: [
        {
            xtype: 'container',
            flex: 1,
            layout: {
                type: 'hbox',
                align: 'stretch'
            },
            border: true,
            items: [
                {
                    width: 350,
                    cls: 'code-tree-panel',
                    autoScroll: true,
                    name: 'templatesTreePanel',
                    xtype: 'treepanel',
                    store: Ext.create('CGT.store.generator.CodeView'),
                    root: {
                        expanded: true,
                        text: "Template Codes"
                    }
                },
                {
                    xtype: 'panel',
                    cls: 'code-panel',
                    flex:1,
                    padding: '0 10 0 10',
                    autoScroll: true,
                    m_codePath: '#',// 自定义属性
                    name: 'codeSourcePanel'
                }
            ]
        }
    ]
});