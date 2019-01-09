Ext.define('CGT.view.common.SidebarDataView', {
	extend: 'Ext.view.View',
    cls: 'sidebar-dataview',
    alias: ['widget.sidebarDataView'],
    selectedItemCls: 'function-button-cur',
    initComponent : function() {
		this.store = Ext.create('CGT.store.main.SideDataView');
		this.itemSelector = 'div.function-button';
    	this.tpl = new Ext.XTemplate(
		    '<tpl for=".">',
		        '<div id="{functionCode}" data-qwidth="300" data-qtip="{caption}" class="function-button">',
		        	'<div class="function-img"><img src="{src}" style="cursor: pointer;" /></div>',
		        '<p class="function-name">{caption}</p>',
		        '</div>',
		    '</tpl>'
    	);
        this.callParent(arguments);
    }
});