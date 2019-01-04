Ext.define('CGT.view.main.GeneratorPanel', {
	extend: 'Ext.panel.Panel',
	alias: ['widget.generatorPanel'],
    layout: {
        type: 'fit'
    },
    initComponent:function(){
        var me = this;
        me.codeViewStore = Ext.create('CGT.store.generator.CodeView');
        me.items = [
            {
                xtype: 'container',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                items: [
                    {
                        xtype: 'container',
                        layout: {
                            type: 'hbox'
                        },
                        items: [
                            {
                                name: 'sqliteExampleBtn',
                                xtype: 'button',
                                margin: 10,
                                text: 'get example sqlite android code'
                            },
                            {
                                name: 'sqlServerExampleBtn',
                                xtype: 'button',
                                margin: 10,
                                text: 'get example sql server 2005 server code'
                            }
                        ]
                    },
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
                                name: 'codeTreePanel',
                                xtype: 'treepanel',
                                store: me.codeViewStore,
                                root: {
                                    expanded: true,
                                    text: "Generate Codes"
                                }
                            },
                            {
                                xtype: 'panel',
                                tbar: [
                                    {
                                        xtype: 'displayfield',
                                        labelWidth: 35,
                                        name: 'codeType',
                                        fieldLabel: 'type'
                                    },
                                    '->',
                                    {
                                        xtype: 'button',
                                        name: 'downloadCurrentFileBtn',
                                        text: 'download current file'
                                    },
                                    {
                                        xtype: 'button',
                                        name: 'downloadAllFileBtn',
                                        disabled: true,
                                        text: 'download all file as zip'
                                    }
                                ],
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
            }
        ];
        me.callParent(arguments);
    }
});