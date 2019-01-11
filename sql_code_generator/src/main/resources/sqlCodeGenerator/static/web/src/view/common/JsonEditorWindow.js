Ext.define('CGT.view.common.JsonEditorWindow', {
	extend: 'Ext.window.Window',
	alias: ['widget.jsoneditorwindow'],
    bodyCls : 'common-json-editor',
    closeAction:'destroy',
    liveDrag : true,
    modal: false,
    width: 800,
    height: 600,
    // custom attr
    contentValues: {
        m_editorId: 'defaultJsonEditor'
    },
    m_jsonData: null,// code path
    initComponent: function(){
		var me = this;
        me.layout = {
            type: 'fit'
        };
        this.items = [
            {
                xtype: 'tabpanel',
                items: [
                    {
                        xtype: 'panel',
                        title: 'edit',
                        layout: {
                            type: 'vbox',
                            align: 'stretch'
                        },
                        defaults: {
                            flex: 1
                        },
                        items: [
                            {
                                xtype: 'codeeditor',
                                name: 'jsonEditing',
                                contentValues: {
                                    m_editorId: 'jsonEditing',
                                    m_mode: 'json'
                                }
                            },
                            {
                                xtype: 'codeeditor',
                                name: 'jsEditing',
                                contentValues: {
                                    m_editorId: 'jsEditing',
                                    m_mode: 'javascript'
                                }
                            }
                        ]
                    },
                    {
                        xtype: 'codeeditor',
                        title: 'preview',
                        name: 'jsonPreview',
                        contentValues: {
                            m_editorId: 'jsonPreview',
                            m_mode: 'json'
                        }
                    }
                ]
            }
        ];
        me.on('afterrender',me.selfAfterRender, me);
		me.callParent();
	},
    selfAfterRender: function (me) {
        // var editorElement = me.getEl().select("#" + me.contentValues.m_editorId).elements[0], mode  = "javascript";
        // me.editor = ace.edit(editorElement);
        // me.editor .setTheme("ace/theme/chrome");
        // if(!Ext.isEmpty(me.contentValues.m_mode)){
        //     mode = me.contentValues.m_mode;
        // }
        // me.editor .session.setMode("ace/mode/" + mode);
    }
});