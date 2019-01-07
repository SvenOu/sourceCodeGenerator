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
                            type: 'hbox',
                            align: 'middle'
                        },
                        items: [
                            {
                                name: 'selectDataSourceBtn',
                                xtype: 'button',
                                margin: 10,
                                text: 'select a data source'
                            },
                            {
                                xtype: 'label',
                                padding: 5,
                                name: 'selectedDataSource',
                                flex: 1
                            }
                        ]
                    },
                    {
                        xtype: 'container',
                        layout: {
                            type: 'hbox',
                            align: 'middle'
                        },
                        items: [
                            {
                                name: 'selectTemplateBtn',
                                xtype: 'button',
                                margin: 10,
                                text: 'select a code template'
                            },
                            {
                                xtype: 'label',
                                padding: 5,
                                name: 'selectedTemplate',
                                flex: 1
                            }
                        ]
                    },
                    {
                        xtype: 'container',
                        layout: {
                            type: 'hbox',
                            align: 'middle'
                        },
                        items: [
                            {
                                xtype: 'textfield',
                                name: 'packageName',
                                allowBlank: false,
                                fieldLabel: 'package name',
                                margin: '0 0 0 10',
                                emptyText: 'input package name',
                                value: 'com.' + app.user.username
                            },
                            {
                                name: 'generateCodeBtn',
                                xtype: 'button',
                                margin: 10,
                                text: 'generate code'
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
                                        labelWidth: 110,
                                        name: 'dataSourceId',
                                        fieldLabel: 'dataSource Id'
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