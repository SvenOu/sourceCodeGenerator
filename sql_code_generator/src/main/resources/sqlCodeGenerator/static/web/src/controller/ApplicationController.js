Ext.define('CGT.controller.ApplicationController', {
	extend : 'Ext.app.Controller',
	refs: [
        {ref: 'userNameLabel', selector: 'headerPanel label[name=userName]'}
	],
    init: function(application) {
   	this.control({
           'headerPanel': {
               afterrender: this.headerPanelAfterRender
           }
       });
    },
    headerPanelAfterRender: function (panel) {
	    this.getUserNameLabel().setText(app.user.username);
    }
});