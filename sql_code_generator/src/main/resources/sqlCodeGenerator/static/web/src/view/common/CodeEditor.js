Ext.define('CGT.view.common.CodeEditor', {
	extend: 'Ext.panel.Panel',
	alias: ['widget.codeeditor'],
    bodyCls : 'common-code-editor',
    header: false,
    // custom attr
    contentValues: {
        m_editorId: 'defaultEditor',
		m_mode: 'javascript'
    },
    m_codePath: null,// code path
    initComponent: function(){
		var me = this;
		me.layout = {
		   type: 'fit'
        };

        var editorModeCombo = {
            xtype: 'combo',
            fieldLabel: 'Editor Mode',
            labelWidth: 85,
            name: 'editorMode',
            queryMode: 'local',
            store: ['text', 'java', 'javascript', 'objectivec', 'json', 'xml'],
            displayField: 'division',
            value: me.contentValues.m_mode,
            autoSelect: true,
            editable: false,
            forceSelection: true,
            listeners: {
                select: me.onSelectEditorMode,
                scope: me
            }
        };

		if(Ext.isEmpty(me.bbar)){
            me.bbar = [editorModeCombo];
        }else{
            me.bbar.unshift(editorModeCombo);
        }

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
    onSelectEditorMode: function(combo, records, eOpts){
        this.editor.session.setMode("ace/mode/" + records[0].getData().field1);
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