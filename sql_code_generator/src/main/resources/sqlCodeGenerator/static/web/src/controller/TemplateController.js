Ext.define('CGT.controller.TemplateController', {
	extend : 'Ext.app.Controller',
	refs: [
        {ref: 'sidebarDataView', selector: 'sidebarDataView'},
        {ref: 'templatesPanel', selector: 'templatesPanel'},
        {ref: 'templateDetailPanel', selector: 'templatedetailpanel'},
        {ref: 'commonMainContainer', selector: 'commonMainContainer'},
        {ref: 'templatesTreePanel', selector: 'templatedetailpanel treepanel[name=templatesTreePanel]'},
        {ref: 'templateGrid', selector: 'templatesPanel templategrid[name=templateGrid]'},
        {ref: 'templatesPanelBackBtn', selector: 'templatesPanel button[name=backBtn]'},
        {ref: 'uploadTemplateBtn', selector: 'templatesPanel button[name=uploadTemplateBtn]'},
        {ref: 'uploadTemplateFileWindow', selector: 'uploadtemplatefilewindow'},
        {ref: 'uploadWinStartUploadBtn', selector: 'uploadtemplatefilewindow button[name=startUploadBtn]'},
        {ref: 'templateFolderFile', selector: 'uploadtemplatefilewindow filefield[name=templateFolderFile]'},
        {ref: 'templateFileForm', selector: 'uploadtemplatefilewindow form[name=templateFileForm]'},
        {ref: 'winTemplateName', selector: 'uploadtemplatefilewindow textfield[name=templateName]'},
        {ref: 'templatesTreePanel', selector: 'templatedetailpanel treepanel[name=templatesTreePanel]'}
	],
    init: function(application) {
        this.control({
               'templatesPanel templategrid[name=templateGrid]': {
                   afterrender: this.templateGridAfterRender,
                   cellclick: this.templateGridCellClick
               },
               'templatesPanel button[name=backBtn]': {
                   click: this.templatesPanelBackBtnClick
               },
               'templatesPanel button[name=uploadTemplateBtn]': {
                   click: this.uploadTemplateBtnClick
               },
               'uploadtemplatefilewindow button[name=startUploadBtn]': {
                   click: this.startUploadBtnClick
               }
        });
    },
    startUploadBtnClick: function(btn, e, eOpts){
        var me = this, templateFileForm =  this.getTemplateFileForm(),
            uploadTemplateFileWindow = this.getUploadTemplateFileWindow(),
            templateFolderFile = this.getTemplateFolderFile(),
            winTemplateName = this.getWinTemplateName();

        if(Ext.isEmpty(templateFolderFile.getValue())){
            app.method.toastMsg('Message', 'must select a templates Archive file.');
            return;
        }

        if(!templateFolderFile.validate()){
            app.method.toastMsg('Message', 'Only zip format file allowed.');
            return;
        }

        if(Ext.isEmpty(winTemplateName.getValue())){
            app.method.toastMsg('Message', 'template name must not empty.');
            return;
        }

        var params = {
            fileName: winTemplateName.getValue()
        };
        templateFileForm.setLoading(true);
        templateFileForm.getForm().submit({
            headers: {Accept: 'application/json;charset=UTF-8'},
            method: 'POST',
            url: app.API_PREFIX + '/uploadTemplateFile',
            params: params,
            success: function(form, action) {
                app.method.toastMsg('Message', 'upload template file success');
                me.getTemplateGrid().getStore().load();
                me.getTemplatesTreePanel().getStore().load();
                templateFileForm.setLoading(false);
                uploadTemplateFileWindow.close();

            },
            failure: function(form, action) {
                app.method.toastMsg('Warning', 'upload template file fail');
                templateFileForm.setLoading(false);
                uploadTemplateFileWindow.close();
            }
        });
    },
    uploadTemplateBtnClick: function(btn, e, eOpts){
        var uploadTplWin = Ext.create('CGT.view.common.UploadTemplateFileWindow',{
            contentValues :{
                m_folderPath: '',
                m_uploadFieldId: 'templateFileFieldId'
            }
        });
        uploadTplWin.show();
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
                // // path 多了反斜杠,需要slice去掉 todo
                // var nodeId = record.get('path').slice(0, -1);
                // var rec = codeTreePanel.getStore().getNodeById(nodeId);
                // // me.expandPath(rec.getPath());
                // codeTreePanel.getSelectionModel().select(rec);
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