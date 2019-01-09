Ext.define('CGT.view.common.CodeEditor', {
	extend: 'Ext.panel.Panel',
	alias: ['widget.codeeditor'],
    layout: 'fit',
    bodyCls : 'code-panel-container',
    header: false,
    // custom attr
    contentValues: {
        m_editorId: 'defaultEditor',
		m_mode: 'javascript'
    },
    m_codePath: null,// code path
    initComponent: function(){
		var me = this;
        this.items = [
            {
                autoScroll: true,
                html: '<div class="code-editor" id = "'+ me.contentValues.m_editorId +'" />'
            }
        ];
        me.on('afterrender',me.selfAfterRender, me);
        me.on('resize',me.onResize, me);
		me.callParent();
	},
    selfAfterRender: function (me) {
        var editorElement = me.getEl().select("#" + me.contentValues.m_editorId).elements[0], mode  = "javascript";
        me.editor = ace.edit(editorElement);
        me.editor .setTheme("ace/theme/chrome");
        if(!Ext.isEmpty(me.contentValues.m_mode)){
            mode = me.contentValues.m_mode;
        }
        me.editor .session.setMode("ace/mode/" + mode);
    },
    setEditorText: function (text) {
	    var me = this;
        me.editor.setValue(text);
        me.editor.session.setValue(text);
    },
    setReadOnly: function(readOnly) {
        this.editor.setReadOnly(readOnly);
    },
    clearData: function () {
	    this.m_codePath = null;
	    this.setEditorText('');
    },
    onResize: function (panel, width, height, oldWidth, oldHeight, eOpts) {
        this.editor.resize();
    }
});