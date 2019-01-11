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
        m_password: '',
        m_dataSourceId: ''
	},
    layout: {
        type: 'vbox',
		align: 'stretch'
	},
    initComponent:function(){
        var me = this;
        me.items = [
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
        ];
        me.bbar = {
            layout: {
                type: 'hbox',
                    pack: 'end'
            },
            items: [
                {
                    xtype: 'button',
                    name: 'saveBtn',
                    text: 'save '
                }
            ]
        }
        me.on('afterrender',me.selfAfterRender, me);
        me.callParent();
	},
    selfAfterRender: function (win) {
	    var me = this, type = this.down('displayfield[name=type]'),
            exampleUrl = this.down('displayfield[name=exampleUrl]'),
            username = this.down('textfield[name=username]'),
            password = this.down('textfield[name=password]'),
            url = this.down('textfield[name=url]');

        type.setValue(me.contentValues.m_type);
        exampleUrl.setValue(me.contentValues.m_exampleUrl);
        url.emptyText = [me.contentValues.m_urlEmptyText];

        if(!Ext.isEmpty(me.contentValues.m_url)){
            url.setValue(me.contentValues.m_url);
        }
        if(!Ext.isEmpty(me.contentValues.username)){
            username.setValue(me.contentValues.username);
        }
        if(!Ext.isEmpty(me.contentValues.password)){
            password.setValue(me.contentValues.password);
        }
    }
});