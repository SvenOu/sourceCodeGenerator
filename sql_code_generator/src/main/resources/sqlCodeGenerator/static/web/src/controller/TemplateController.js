Ext.define('CGT.controller.TemplateController', {
	extend : 'Ext.app.Controller',
	refs: [
        {ref: 'sidebarDataView', selector: 'sidebarDataView'},
        {ref: 'templatesPanel', selector: 'templatesPanel'},
        {ref: 'templateDetailPanel', selector: 'templatedetailpanel'},
        {ref: 'commonMainContainer', selector: 'commonMainContainer'},
        {ref: 'templatesTreePanel', selector: 'templatedetailpanel treepanel[name=templatesTreePanel]'},
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
        var me = this, templatePanel = this.getTemplatesPanel(),
            codeTreePanel = this.getTemplatesTreePanel(),
            templateDetailPanel = this.getTemplateDetailPanel();
            mainContainer = this.getCommonMainContainer();
        var contentValues = templatePanel.contentValues;
        if(contentValues.m_mode === 'choose'){
            contentValues.m_lastChooseVal = record;
            contentValues.m_callBack(contentValues);
            mainContainer.getLayout().setActiveItem(contentValues.m_chooseFrom);
            contentValues.m_mode = 'default';
        }else{
            me.getSidebarDataView().getSelectionModel().select(3);
            mainContainer.getLayout().setActiveItem(templateDetailPanel);
            // path 多了反斜杠,需要slice去掉
            var nodeId = record.get('path').slice(0, -1);
            var rec = codeTreePanel.getStore().getNodeById(nodeId);
            codeTreePanel.getSelectionModel().select(rec);
        }
    }
});