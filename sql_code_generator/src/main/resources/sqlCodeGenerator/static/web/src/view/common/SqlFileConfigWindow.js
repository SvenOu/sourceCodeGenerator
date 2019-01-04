Ext.define('CGT.view.common.SqlFileConfigWindow',{
	extend:'Ext.window.Window',
	alias : 'widget.sqlfileconfigwindow',
	closeAction:'destroy',
	title: 'Config file database',
	modal: true,
	cls: 'main-common-condif-database-window',
	width: 1000,
	// height: 700,
	padding: 15,
	liveDrag : true,
	// custom attr
	contentValus: {
        m_type: '',
        m_fileName: ''
	},
    layout: {
        type: 'fit'
	},
	items: [
		{
			xtype: 'form',
			name: 'sqlFileConfigForm',
            layout: {
                type: 'vbox',
                align: 'stretch'
            },
			items: [
                {
                    xtype: 'filefield',
                    name: 'dbFile',
                    allowBlank: false,
                    labelAlign: 'right',
                    fieldLabel: 'db file',
                    msgTarget: 'side',
                    labelWidth: 50,
                    flex: 1,
                    buttonText: 'Select a db file'
                }
			]
		}
	],
    bbar: {
		layout: {
			type: 'hbox',
			pack: 'center'
		},
		items: [
            {
                xtype: 'button',
				name: 'uploadDbFiles',
                text: 'upload'
            }
		]
	}
});