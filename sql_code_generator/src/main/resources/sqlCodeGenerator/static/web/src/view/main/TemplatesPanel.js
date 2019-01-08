Ext.define('CGT.view.main.TemplatesPanel', {
	extend: 'Ext.panel.Panel',
	alias: ['widget.templatesPanel'],
    // custom attr
    contentValues: {
        m_mode: 'default', //  default, choose
        m_chooseFrom: null,
        m_lastChooseVal: null,
        m_callBack: null
    },
    initComponent:function() {
        var me = this;
        me.items= [
            {
                name: 'backBtn',
                xtype: 'button',
                margin: 10,
                text: 'back',
                hidden : true
            },
            {
                xtype: 'templategrid',
                name : 'templateGrid',
                flex: 1,
                store: Ext.create('CGT.store.generator.Template')
            }
        ];
        me.callParent();
    }
});