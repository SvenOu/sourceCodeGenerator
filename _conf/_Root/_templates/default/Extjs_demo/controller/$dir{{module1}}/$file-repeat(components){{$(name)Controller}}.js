Ext.define('${{packageName}}.controller.${{name}}Controller', {
	extend : 'Ext.app.Controller',
	refs: [
        {ref: '${{name}}', selector: '${{name-lowerCaseALL}}grid'}
	],
    init: function(application) {
   	this.control({
           // 'headerPanel': {
           //     afterrender: this.headerPanelAfterRender
           // }
       });
    }
});
