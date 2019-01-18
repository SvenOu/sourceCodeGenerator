Ext.define('CGT.view.common.FileActionWindow',{
	extend:'Ext.window.Window',
	alias : 'widget.fileactionwindow',
	closeAction:'destroy',
	modal: false,
	cls: 'main-common-file-action-window',
	width: 600,
	padding: 15,
	liveDrag : true,
	// custom attr
	contentValues: {
        m_title: 'file action',
        m_action: '',
        m_fileName: '',
        m_option: null
	},
    layout: {
        type: 'fit'
	},
    initComponent:function(){
        var me = this;
        me.title = me.contentValues.m_title;
        me.items = [
            {
                xtype: 'textfield',
                name: 'fileName',
                fieldLabel: 'file name',
				labelWidth: 60,
                value: me.contentValues.m_fileName,
                allowBlank: false,
                emptyText: ''
            }
        ];
        me.bbar= {
            layout: {
                type: 'hbox',
                    pack: 'center'
            },
            items: [
                {
                    xtype: 'button',
                    name: 'doActionBtn',
                    text: me.contentValues.m_action
                }
            ]
        };
        me.callParent();
	}
});