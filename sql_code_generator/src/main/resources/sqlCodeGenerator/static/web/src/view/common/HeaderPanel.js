Ext.define('CGT.view.common.HeaderPanel', {
	extend: 'Ext.panel.Panel',
	alias: ['widget.headerPanel'],
	id: 'headerpanel',
    height: 60,
    region: 'north',
    cls: 'header',
    title: 'Header',
    border: false,
    header: false,
    split: false,
    layout: {
        type: 'hbox',
        align: 'middle'
    },
	initComponent : function() {
		var me = this;
		Ext.apply(me,{
			items:[ 
				{
					cls: 'title-text',
					xtype: 'label',
                    text: 'Evolution!  no longer write duplicate code',
                    margin: '0 0 0 50'
				},
				{
					xtype: 'box',
					flex: 1
				},
				{
					xtype: 'button',
					icon: '../web/images/icon_logoff.png',
					name: 'logOffButton',
					margin: '0 5 0 10',
					text: 'Log Off',
					scope: me,
					handler: me.logOffBtnClick
				}
			]
        });
		this.callParent(arguments);
	},
	logOffBtnClick: function(button,e){
		window.location.href = '/j_spring_security_logout';
	}
});