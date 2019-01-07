Ext.define('CGT.view.common.SqlRemoteConfigWindow',{
	extend:'Ext.window.Window',
	alias : 'widget.sqlremoteconfigwindow',
	closeAction:'destroy',
	title: 'Config remote database',
	modal: true,
	cls: 'main-common-sql-remote-config-window',
	width: 1000,
	// height: 700,
	padding: 15,
	liveDrag : true,
	// custom attr
	contentValues: {
        m_type: '',
        m_url: '',
        m_username: '',
        m_password: ''
	},
    layout: {
        type: 'vbox',
		align: 'stretch'
	},
	items: [
		{
            xtype: 'displayfield',
            name: 'type',
            fieldLabel: 'type'
		},
		{
            xtype: 'displayfield',
			name: 'exampleUrl',
            fieldLabel: 'example url:'
		},
		{
            xtype: 'textfield',
            name: 'url',
            fieldLabel: 'url',
            allowBlank: false,
            emptyText: ''
        },
		{
            xtype: 'textfield',
            name: 'username',
            allowBlank: false,
            fieldLabel: 'username',
            emptyText: 'user name'
        },
		{
            xtype: 'textfield',
            name: 'password',
            allowBlank: false,
            fieldLabel: 'password',
            emptyText: 'password'
        }
	],
    bbar: {
		layout: {
			type: 'hbox',
			pack: 'end'
		},
		items: [
            {
                xtype: 'button',
				name: 'addBtn',
                text: 'add '
            }
		]
	}
});