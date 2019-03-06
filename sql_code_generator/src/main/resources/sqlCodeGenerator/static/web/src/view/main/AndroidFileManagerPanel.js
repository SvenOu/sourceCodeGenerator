Ext.define('CGT.view.main.AndroidFileManagerPanel', {
    extend: 'Ext.panel.Panel',
    alias: ['widget.androidfilemanagerpanel'],
    cls: 'android-file-manager-panel',
    layout: {
        type: 'fit'
    },
    initComponent:function(){
        var me = this;
        me.codeViewStore = Ext.create('CGT.store.generator.CodeView');
        me.tbar = {
            xtype: 'container',
            name: 'androidFileManagerPanelTbar',
            cls: 'generator-panel-tbar',
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
                            xtype: 'textfield',
                            name: 'packageName',
                            allowBlank: false,
                            fieldLabel: 'package name',
                            margin: '20 0 0 10',
                            emptyText: 'input package name'
                        },
                        {
                            xtype: 'textfield',
                            name: 'androidServerPort',
                            allowBlank: false,
                            labelWidth: 125,
                            fieldLabel: 'android server port',
                            margin: '20 0 0 100',
                            emptyText: 'input server port'
                        }
                    ]
                }
            ]
        };
        me.items = [
            {
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                bbar: [
                    {
                        xtype: 'form',
                        name: 'overwriteFileForm',
                        flex: 1,
                        layout: 'hbox',
                        items: [
                            {
                                xtype: 'filefield',
                                name: 'overwriteFile',
                                allowBlank: false,
                                fieldLabel: 'Select a local file to overwrite the file on your phone',
                                labelAlign: 'top',
                                buttonText: 'select',
                                margin: '0 180 0 0',
                                clearOnSubmit: false,
                                flex: 1
                            },
                            {
                                xtype: 'button',
                                name: 'overwriteFileBtn',
                                text: 'overwrite selected file',
                                disabled: true,
                                margin: '0 50 0 0',
                                contentValues: {
                                    m_selectedRec: null
                                }
                            }
                        ]
                    }
                ],
                items: [
                    {
                        xtype: 'container',
                        layout: {
                            type: 'hbox',
                            align: 'middle'
                        },
                        items: [
                            {
                                xtype: 'textfield',
                                name: 'ipAddress',
                                allowBlank: false,
                                fieldLabel: 'ip address',
                                margin: '20 0 0 10',
                                width: 400,
                                emptyText: 'input android phone ip address'
                            },
                            {
                                name: 'connectBtn',
                                xtype: 'button',
                                margin: '20 0 0 50',
                                text: 'connectAndRefresh'
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
                                xtype: 'filesystemtree',
                                store: me.codeViewStore,
                                root: {
                                    expanded: true,
                                    text: "App Files",
                                    readonly: true
                                }
                            },
                            {
                                tbar: [
                                    {
                                        flex: 1,
                                        xtype: 'displayfield',
                                        cls: 'cls-field-auto-warp',
                                        labelWidth: 70,
                                        name: 'phonePath',
                                        fieldLabel: 'phone path'
                                    },
                                    {
                                        xtype: 'button',
                                        name: 'downloadCurrentFileBtn',
                                        disabled: true,
                                        text: 'download current file'
                                    },
                                    {
                                        xtype: 'button',
                                        name: 'downloadAllFileBtn',
                                        disabled: true,
                                        text: 'download all file as zip'
                                    }
                                ],
                                xtype: 'codeeditor',
                                name: 'phoneFileCodeEditor',
                                contentValues: {
                                    m_editorId: 'phoneFileCodeEditor',
                                    m_mode: 'java'
                                },
                                flex:1
                            }
                        ]
                    }
                ]
            }
        ];
        me.callParent(arguments);
    }
});