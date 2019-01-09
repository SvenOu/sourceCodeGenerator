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
                    tbar: [
                        {
                            xtype: 'button',
                            name: 'resetDefTplBtn',
                            text: 'reset default templates'
                        },
                        '->'
                    ],
                    width: 350,
                    cls: 'code-tree-panel',
                    autoScroll: true,
                    name: 'templatesTreePanel',
                    xtype: 'treepanel',
                    // custom attr
                    contentValues: {
                        m_selectRecord: null
                    },
                    store: Ext.create('CGT.store.generator.CodeView'),
                    root: {
                        expanded: true,
                        text: "Template Codes"
                    }
                },
                {
                    xtype: 'codeeditor',
                    name: 'detailCodeEditor',
                    contentValues: {
                        m_editorId: 'detailCodeEditor',
                        m_mode: 'java'
                    },
                    flex:1,
                    tbar: [
                        {
                            xtype: 'button',
                            name: 'saveCodeBtn',
                            margin: '0 50 0 30 ',
                            text: 'save'
                        },
                        {
                            xtype: 'button',
                            name: 'reloadCodeBtn',
                            text: 'reload'
                        },
                        {
                            name: 'showDocBtn',
                            xtype: 'button',
                            margin: '0 0 0 30',
                            text: 'show document'
                        },
                        '->'
                    ]
                }
            ]
        }
    ]
});