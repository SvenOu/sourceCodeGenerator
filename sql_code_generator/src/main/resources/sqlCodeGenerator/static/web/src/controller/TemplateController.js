Ext.define('CGT.controller.TemplateController', {
	extend : 'Ext.app.Controller',
	refs: [
        {ref: 'sidebarDataView', selector: 'sidebarDataView'},
        {ref: 'templatesPanel', selector: 'templatesPanel'},
        {ref: 'templateDetailPanel', selector: 'templatedetailpanel'},
        {ref: 'commonMainContainer', selector: 'commonMainContainer'},
        {ref: 'templatesTreePanel', selector: 'templatedetailpanel treepanel[name=templatesTreePanel]'},
        {ref: 'templateGrid', selector: 'templatesPanel templategrid[name=templateGrid]'},
        {ref: 'templatesPanelBackBtn', selector: 'templatesPanel button[name=backBtn]'}
	],
    init: function(application) {
        this.control({
               'templatesPanel templategrid[name=templateGrid]': {
                   afterrender: this.templateGridAfterRender,
                   cellclick: this.templateGridCellClick
               },
               'templatesPanel button[name=backBtn]': {
                   click: this.templatesPanelBackBtnClick
               }
        });
    },
    templatesPanelBackBtnClick: function(btn, e, eOpts){
        var contentValues = this.getTemplatesPanel().contentValues,
            mainContainer = this.getCommonMainContainer();
        mainContainer.getLayout().setActiveItem(contentValues.m_chooseFrom);
        contentValues.m_mode = 'default';
        btn.setVisible(false);
    },
    templateGridAfterRender: function (panel) {
	    var me = this, templateGrid = this.getTemplateGrid();
        templateGrid.getStore().load();
    },
    templateGridCellClick: function (table, td, cellIndex, record, tr, rowIndex, e, eOpts) {
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
            if(cellIndex === 4){
                Ext.Msg.confirm('Message', 'Do you want to delete this template?', function(optional){
                    if(optional=='yes'){
                        me.preformDeleteTemplate(table, td, cellIndex, record, tr, rowIndex, e, eOpts);
                    }
                });
            }else if(cellIndex === 1){
                me.getSidebarDataView().getSelectionModel().select(3);
                mainContainer.getLayout().setActiveItem(templateDetailPanel);
                // path 多了反斜杠,需要slice去掉
                var nodeId = record.get('path').slice(0, -1);
                var rec = codeTreePanel.getStore().getNodeById(nodeId);
                codeTreePanel.getSelectionModel().select(rec);
            }
        }
    },
    preformDeleteTemplate: function (table, td, cellIndex, record, tr, rowIndex, e, eOpts) {
        var me = this;
        var params = {
            templateId: record.get('templateId')
        };
        Ext.Ajax.request({
            type : "POST",
            dataType : 'json',
            params: params,
            url : app.API_PREFIX + '/deleteCodeTemplate',
            success: function(response){
                var responseText = Ext.JSON.decode(response.responseText);
                if(responseText){
                    if(responseText.success){
                        app.method.toastMsg('Message', 'delete template success.');
                    }else {
                        app.method.toastMsg('Message', responseText.errorCode);
                    }
                }
                me.getTemplateGrid().getStore().load();
            },
            failure: function() {
                app.method.toastMsg('Message', 'delete template fail.');
            }
        });
    }
});