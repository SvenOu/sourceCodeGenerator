Ext.define('CGT.controller.TemplateController', {
	extend : 'Ext.app.Controller',
	refs: [
        {ref: 'templatesPanel', selector: 'templatesPanel'},
        {ref: 'commonMainContainer', selector: 'commonMainContainer'},
        {ref: 'templateGrid', selector: 'templatesPanel templategrid[name=templateGrid]'}
	],
    init: function(application) {
        this.control({
               'templatesPanel templategrid[name=templateGrid]': {
                   afterrender: this.templateGridAfterRender,
                   itemclick: this.templateGridItemClick
               }
        });
    },
    templateGridAfterRender: function (panel) {
	    var me = this, templateGrid = this.getTemplateGrid();
        templateGrid.getStore().load();
    },
    templateGridItemClick: function (table, record, item, index, e, eOpts) {
        var me = this, templatePanel = this.getTemplatesPanel(), mainContainer = this.getCommonMainContainer();
        var contentValues = templatePanel.contentValues;
        if(contentValues.m_mode === 'choose'){
            contentValues.m_lastChooseVal = record;
            contentValues.m_callBack(contentValues);
            mainContainer.getLayout().setActiveItem(contentValues.m_chooseFrom);
            contentValues.m_mode = 'default';
        }
    }
});