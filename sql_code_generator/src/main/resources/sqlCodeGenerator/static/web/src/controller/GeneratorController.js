Ext.define('CGT.controller.GeneratorController', {
	extend : 'Ext.app.Controller',
	refs: [
        {ref: 'commonMainContainer', selector: 'commonMainContainer'},
	    {ref: 'generatorPanel', selector: 'generatorPanel'},
	    {ref: 'dataSourcesPanel', selector: 'datasourcespanel'},
	    {ref: 'templatesPanel', selector: 'templatesPanel'},
	    {ref: 'packageName', selector: 'generatorPanel textfield[name=packageName]'},
	    {ref: 'selectDataSourceBtn', selector: 'generatorPanel button[name=selectDataSourceBtn]'},
	    {ref: 'selectedTemplate', selector: 'generatorPanel textfield[name=selectedTemplate]'},
	    {ref: 'selectedDataSource', selector: 'generatorPanel textfield[name=selectedDataSource]'},
	    {ref: 'selectTemplateBtn', selector: 'generatorPanel button[name=selectTemplateBtn]'},
	    {ref: 'generateCodeBtn', selector: 'generatorPanel button[name=generateCodeBtn]'},
	    {ref: 'codeTreePanel', selector: 'generatorPanel treepanel[name=codeTreePanel]'},
	    {ref: 'generateCodeEditor', selector: 'generatorPanel codeeditor[name=generateCodeEditor]'},
	    {ref: 'downloadCurrentFileBtn', selector: 'generatorPanel button[name=downloadCurrentFileBtn]'},
	    {ref: 'downloadAllFileBtn', selector: 'generatorPanel button[name=downloadAllFileBtn]'},
	    {ref: 'dataSourceId', selector: 'generatorPanel displayfield[name=dataSourceId]'},
        {ref: 'templatesPanelBackBtn', selector: 'templatesPanel button[name=backBtn]'},
        {ref: 'dataSourcesPanelBackBtn', selector: 'datasourcespanel button[name=backBtn]'},
        {ref: 'docWindow', selector: 'docwindow'},
        {ref: 'docCodeEditor', selector: 'docwindow codeeditor[name=docCodeEditor]'},
    ],
    init: function(application) {
   	this.control({
           'generatorPanel': {
               afterrender: this.generatorPanelAfterRender
           },
           'generatorPanel button[name=selectDataSourceBtn]': {
               click: this.selectDataSourceBtnClick
           },
           'generatorPanel button[name=clearGenerateCodeBtn]': {
               click: this.clearGenerateCodeBtnClick
           },
           'generatorPanel button[name=selectTemplateBtn]': {
               click: this.selectTemplateBtnClick
           },
           'generatorPanel button[name=generateCodeBtn]': {
               click: this.generateCodeBtnClick
           },
           'generatorPanel treepanel[name=codeTreePanel]': {
               select: this.codeTreePanelItemSelect
           },
           'generatorPanel button[name=downloadCurrentFileBtn]': {
               click: this.downloadCurrentFileBtnClick
           },
           'generatorPanel button[name=downloadAllFileBtn]': {
               click: this.downloadAllFileBtnClick
           },
           'docwindow': {
               afterrender: this.docWindowAfterRender
           }
       });
    },
    clearGenerateCodeBtnClick: function(btn, e, eOpts){
	    var me = this, codeTreePanel = this.getCodeTreePanel(),
            url = app.API_PREFIX +'/clearGenerateCode';
        Ext.Msg.confirm('Message', 'Do you want to delete all generate code?', function(optional){
            if(optional=='yes'){
                codeTreePanel.setLoading(true);
                var params = {};
                Ext.Ajax.request({
                    method: 'POST',
                    params: params,
                    url: url,
                    success: function(response){
                        codeTreePanel.setLoading(false);
                        var responseText = Ext.JSON.decode(response.responseText);
                        if(responseText){
                            if(responseText.success){
                                app.method.toastMsg('Message', 'clear generate code success.');
                            }else {
                                app.method.toastMsg('Message', responseText.errorCode);
                            }
                        }
                        me.doRefreshCodeTreePanel();
                    },
                    failure: function(response){
                        app.method.toastMsg('Message', 'delete fail!');
                        codeTreePanel.setLoading(false);
                    },
                    scope: me
                });
            }
        });
    },
    doRefreshCodeTreePanel: function () {
        var me = this, codeTreeStore = this.getCodeTreePanel().store,
            downloadAllFileBtn = this.getDownloadAllFileBtn();
        codeTreeStore.getProxy().url =  app.API_PREFIX +'/getUserGenerateRootCodeFileInfo';
        codeTreeStore.load({
            params:{},
            callback: function (records, operation, success) {
                if(Ext.isEmpty(records)){
                    downloadAllFileBtn.disable();
                }else {
                    downloadAllFileBtn.enable();
                }
            }
        });
    },
    generatorPanelAfterRender: function(panel){
	    this.doRefreshCodeTreePanel();
    },
    docWindowAfterRender: function(win){
        var me = this, docCodeEditor = this.getDocCodeEditor(),
            url = app.API_PREFIX +'/getDoucumentFile';
        var params = {};
        docCodeEditor.setLoading(true);
        Ext.Ajax.request({
            method: 'GET',
            params: params,
            url: url,
            success: function(response){
                docCodeEditor.setLoading(false);
                if(response){
                    docCodeEditor.m_codePath = params.path;
                    docCodeEditor.setEditorText(response.responseText);
                    docCodeEditor.setReadOnly(true);
                }
            },
            failure: function(response){
                app.method.toastMsg('Message', 'getting doc error!');
            },
            scope: me
        });
    },
    downloadAllFileBtnClick: function(btn, e, eOpts){
        var url = app.API_PREFIX +'/downloadAllFile';
        window.open(url, '_blank');
    },
    downloadCurrentFileBtnClick: function(btn, e, eOpts){
	    var me = this, codePath = this.getGenerateCodeEditor().m_codePath;
	    if(!Ext.isEmpty(codePath) && "#" !== codePath){
            var url = app.API_PREFIX +'/downloadSourcesFile?' + Ext.urlEncode({
                userId: app.user.userId,
                path: codePath
            });
            window.open(url, '_blank');
        }
    },
    codeTreePanelItemSelect: function (treePanel, record, index, eOpts){
        var me = this, url = app.API_PREFIX +'/getSourceFileCode';
        if(record.get('leaf') && !record.get('dir')){
            var params = {path: record.get('path')};
            me.getGenerateCodeEditor().setLoading(true);
            Ext.Ajax.request({
                method: 'GET',
                params: params,
                url: url,
                success: function(response){
                    me.getGenerateCodeEditor().setLoading(false);
                    if(response){
                        me.getGenerateCodeEditor().m_codePath = params.path;
                        me.getGenerateCodeEditor().setEditorText(response.responseText);
                        me.getGenerateCodeEditor().setReadOnly(true);
                    }
                },
                failure: function(response){
                    app.method.toastMsg('Message', 'getting record error!');
                },
                scope: me
            });
        }
    },
    generateCodeBtnClick: function (btn, e, eOpts) {
        var me = this, codeTreeStore = this.getCodeTreePanel().store,
            downloadAllFileBtn = this.getDownloadAllFileBtn(),
            tplContentValues = this.getTemplatesPanel().contentValues,
            dataSourcesContentValues = this.getDataSourcesPanel().contentValues;

        if(!me.generateConfigValid()){
            return;
        }
        me.getGenerateCodeEditor().clearData();
        me.getDataSourceId().setValue(dataSourcesContentValues.m_lastChooseVal.get('dataSourceId'));
        codeTreeStore.getProxy().url =  app.API_PREFIX +'/getCodeFileInfo';
        btn.setLoading('progressing...');
        codeTreeStore.load({
            params:{
                packageName: me.getPackageName().getValue(),
                dataSourceId: dataSourcesContentValues.m_lastChooseVal.get('dataSourceId'),
                templateId: tplContentValues.m_lastChooseVal.get('templateId')
            },
            callback: function (records, operation, success) {
                if(Ext.isEmpty(records)){
                    downloadAllFileBtn.disable();
                }else {
                    downloadAllFileBtn.enable();
                }
                btn.setLoading(false);
            }
        });
    },
    generateConfigValid: function () {
        var me = this;
        if(me.getSelectedDataSource().isVisible() && Ext.isEmpty(me.getSelectedDataSource().getValue())){
            app.method.toastMsg("Message","must select a dataSource");
            return false;
        }
        if(me.getSelectedTemplate().isVisible() && Ext.isEmpty(me.getSelectedTemplate().getValue())){
            app.method.toastMsg("Message","must select a template");
            return false;
        }
        if(me.getPackageName().isVisible() &&!me.getPackageName().isValid()){
            app.method.toastMsg("Message","package require not empty");
            return false;
        }
        return true;
    },
    selectTemplateBtnClick: function (btn, e, eOpts) {
        var me = this, templatePanel = this.getTemplatesPanel(),
            mainContainer = this.getCommonMainContainer(),
            templatesPanelBackBtn = this.getTemplatesPanelBackBtn(),
            selectedTemplate = this.getSelectedTemplate();
        templatePanel.contentValues.m_mode = 'choose';
        templatePanel.contentValues.m_chooseFrom = this.getGeneratorPanel();
        templatePanel.contentValues.m_callBack = function (contentValues) {
            var record =  contentValues.m_lastChooseVal;
            selectedTemplate.setFieldLabel(record.get('templateId'));
            selectedTemplate.setValue(record.get('path'));
            templatesPanelBackBtn.setVisible(false);
        };
        mainContainer.getLayout().setActiveItem(templatePanel);
        templatesPanelBackBtn.setVisible(true);
    },
    selectDataSourceBtnClick: function (btn, e, eOpts) {
        var me = this, dataSourcesPanel = this.getDataSourcesPanel(),
            mainContainer = this.getCommonMainContainer(),
            dataSourcesPanelBackBtn = this.getDataSourcesPanelBackBtn(),
            selectedDataSource = this.getSelectedDataSource();
        dataSourcesPanel.contentValues.m_mode = 'choose';
        dataSourcesPanel.contentValues.m_chooseFrom = this.getGeneratorPanel();
        dataSourcesPanel.contentValues.m_callBack = function (contentValues) {
            var record =  contentValues.m_lastChooseVal;
            selectedDataSource.setFieldLabel(record.get('dataSourceId'));
            selectedDataSource.setValue(record.get('url'));
            dataSourcesPanelBackBtn.setVisible(false);
        };
        mainContainer.getLayout().setActiveItem(dataSourcesPanel);
        dataSourcesPanelBackBtn.setVisible(true);
    }
});