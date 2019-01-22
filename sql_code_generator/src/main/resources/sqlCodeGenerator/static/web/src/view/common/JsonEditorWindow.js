Ext.define('CGT.view.common.JsonEditorWindow', {
	extend: 'Ext.window.Window',
	alias: ['widget.jsoneditorwindow'],
    bodyCls : 'common-json-editor',
    closeAction:'destroy',
    liveDrag : true,
    modal: false,
    width: 1024,
    height: 768,
    title: 'json data editor',
    // custom attr
    contentValues: {
        m_editorId: 'defaultJsonEditor'
    },
    m_jsonData: '',// code path
    m_dataSourceId: '',
    m_dataSourceName: '',
    m_defaultJsonData: {
	    title: "hello world",
	    content: "some text!"
    },
    me_defaultConvertJasonDataFunction : "function convertJasonData(data){\n" +
    "    // write your code\n" +
    "    return data;\n" +
    "}"
    ,
    initComponent: function(){
		var me = this;
        me.layout = {
            type: 'vbox',
            align: 'stretch'
        };
        this.items = [
            {
                xtype: 'codeeditor',
                name: 'jsonEditing',
                flex: 3,
                contentValues: {
                    m_editorId: 'jsonEditing',
                    m_mode: 'json'
                },
                bbar: [
                    '->',
                    {
                        xtype: 'textfield',
                        name: 'dataSourceName',
                        fieldLabel: 'dataSourceName',
                        labelWidth: 105,
                        allowBlank: false
                    },
                    {
                        name: 'appleChangeBtn',
                        xtype: 'button',
                        margin: '0 20 0 0',
                        text: 'save change and close'
                    },
                    {
                        name: 'resetBtn',
                        xtype: 'button',
                        margin: '0 20 0 70',
                        text: 'reset'
                    }
                ]
            },
            {
                xtype: 'codeeditor',
                name: 'jsEditing',
                flex: 2,
                contentValues: {
                    m_editorId: 'jsEditing',
                    m_mode: 'javascript'
                },
                bbar: [
                    '->',
                    {
                        name: 'executeJsBtn',
                        xtype: 'button',
                        margin: '0 20 0 0',
                        text: 'execute'
                    }
                ]
            }
        ];
        me.on('afterrender',me.selfAfterRender, me);
		me.callParent();
	},
    selfAfterRender: function (win) {
	    var me = this;
        me.jsonEditing = me.down('codeeditor[name=jsonEditing]');
        me.jsEditing = me.down('codeeditor[name=jsEditing]');
        me.resetBtn = me.down('button[name=resetBtn]');
        me.appleChangeBtn = me.down('button[name=appleChangeBtn]');
        me.executeJsBtn = me.down('button[name=executeJsBtn]');
        me.dataSourceName = me.down('textfield[name=dataSourceName]');

        if(Ext.isEmpty(me.m_jsonData)){
            me.m_jsonData = me.m_defaultJsonData;
        }
        me.dataSourceName.setValue(me.m_dataSourceName);
        me.setJsonText(me.m_jsonData);
        me.jsEditing.setEditorText(me.me_defaultConvertJasonDataFunction);

        me.resetBtn.on('click', me.resetBtnClick, me);
        me.executeJsBtn.on('click', me.executeJsBtnClick, me);
    },
    resetBtnClick: function(btn, e, eOpts){
	    var me = this;
        Ext.Msg.confirm('Message', 'Do you want to reset the json data?', function(optional){
            if(optional=='yes'){
                me.m_jsonData = me.m_defaultJsonData;
                me.setJsonText(me.m_jsonData);
            }
        });
    },
    executeJsBtnClick: function(btn, e, eOpts){
	    var me = this;
	    var fun = '(' + me.jsEditing.editor.getValue() + ')(this.m_jsonData)';
	    try {
            me.m_jsonData = eval(fun);
            me.setJsonText(me.m_jsonData);
        }catch (e) {
            app.method.toastMsg('Error', 'execute script error.');
        }
    },
    setJsonText: function (obj) {
        this.jsonEditing.setEditorText(JSON.stringify(obj, null, 4));
    },
    getJsonText: function () {
        return this.jsonEditing.editor.getValue();
    }
});