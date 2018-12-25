Ext.define('CGT.controller.MainController', {
	extend : 'Ext.app.Controller',
	refs: [
	    {ref: 'sidebarDataView', selector: 'sidebarDataView'},
	    {ref: 'commonMainContainer', selector: 'commonMainContainer'},
	    {ref: 'homePanel', selector: 'homePanel'},
	    {ref: 'javaPanel', selector: 'javaPanel'},
	    {ref: 'javaScriptPanel', selector: 'javaScriptPanel'},
	    {ref: 'sqlPanel', selector: 'sqlPanel'},
    ],
    init: function(application) {
   	this.control({
           'sidebarDataView': {
               afterrender: this.dataViewSelectFirstItem,
               itemclick: this.sidebarDataViewItemClick
           },
           // 'useredit button[action=save]': {
           //     click: this.updateUser
           // }
       });
    },
    dataViewSelectFirstItem: function(){
        this.getSidebarDataView().getSelectionModel().selectRange(0, 0);
    },
    sidebarDataViewItemClick: function (dataView, record, item, index, e, eOpts) {

        var functionCode = record.get('functionCode'),
            mainConrainer = this.getCommonMainContainer();

        if (functionCode === 'home') {
            mainConrainer.getLayout().setActiveItem(this.getHomePanel());
        }

        if (functionCode === 'sql') {
            mainConrainer.getLayout().setActiveItem(this.getJavaPanel());
        }

        if (functionCode === 'java') {
            mainConrainer.getLayout().setActiveItem(this.getJavaScriptPanel());
        }

        if (functionCode === 'javascript') {
            mainConrainer.getLayout().setActiveItem(this.getSqlPanel());
        }
    }
});