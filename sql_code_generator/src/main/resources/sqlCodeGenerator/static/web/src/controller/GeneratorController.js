Ext.define('CGT.controller.GeneratorController', {
	extend : 'Ext.app.Controller',
	refs: [
        {ref: 'commonMainContainer', selector: 'commonMainContainer'},
	    {ref: 'generatorPanel', selector: 'generatorPanel'},
	    {ref: 'dataSourcesPanel', selector: 'datasourcespanel'},
	    {ref: 'templatesPanel', selector: 'templatesPanel'},
	    {ref: 'packageName', selector: 'generatorPanel textfield[name=packageName]'},
	    {ref: 'selectDataSourceBtn', selector: 'generatorPanel button[name=selectDataSourceBtn]'},
	    {ref: 'selectedTemplate', selector: 'generatorPanel label[name=selectedTemplate]'},
	    {ref: 'selectedDataSource', selector: 'generatorPanel label[name=selectedDataSource]'},
	    {ref: 'selectTemplateBtn', selector: 'generatorPanel button[name=selectTemplateBtn]'},
	    {ref: 'generateCodeBtn', selector: 'generatorPanel button[name=generateCodeBtn]'},
	    {ref: 'codeTreePanel', selector: 'generatorPanel treepanel[name=codeTreePanel]'},
	    {ref: 'codeSourcePanel', selector: 'generatorPanel panel[name=codeSourcePanel]'},
	    {ref: 'downloadCurrentFileBtn', selector: 'generatorPanel button[name=downloadCurrentFileBtn]'},
	    {ref: 'downloadAllFileBtn', selector: 'generatorPanel button[name=downloadAllFileBtn]'},
	    {ref: 'dataSourceId', selector: 'generatorPanel displayfield[name=dataSourceId]'},
	    {ref: 'sqlDataBaseConfigWindow', selector: 'sqldatabaseconfigwindow'},
	    {ref: 'winSqlType', selector: 'sqldatabaseconfigwindow displayfield[name=type]'},
	    {ref: 'winExampleUrl', selector: 'sqldatabaseconfigwindow displayfield[name=exampleUrl]'},
	    {ref: 'winUrl', selector: 'sqldatabaseconfigwindow textfield[name=url]'},
	    {ref: 'winUsername', selector: 'sqldatabaseconfigwindow textfield[name=username]'},
	    {ref: 'winPassword', selector: 'sqldatabaseconfigwindow textfield[name=password]'},
	    {ref: 'winGenerateSourceBtn', selector: 'sqldatabaseconfigwindow button[name=generateSourceBtn]'},
    ],
    init: function(application) {
   	this.control({
           'generatorPanel button[name=selectDataSourceBtn]': {
               click: this.selectDataSourceBtnClick
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
           'sqldatabaseconfigwindow button[name=generateSourceBtn]': {
               click: this.generateSourceBtnClick
           }
       });
    },
    downloadAllFileBtnClick: function(btn, e, eOpts){
	    var me = this, tplContentValues = this.getTemplatesPanel().contentValues,
            dataSourcesContentValues = this.getDataSourcesPanel().contentValues;

        var templateId = tplContentValues.m_lastChooseVal.get('templateId'),
            dataSourceId = dataSourcesContentValues.m_lastChooseVal.get('dataSourceId');
	    if(!Ext.isEmpty(dataSourceId)){
            var url = app.API_PREFIX +'/downloadAllFile?' + Ext.urlEncode({
                userId: app.user.userId,
                templateId: templateId,
                dataSourceId: dataSourceId
            });
            window.open(url, '_blank');
        }
    },
    downloadCurrentFileBtnClick: function(btn, e, eOpts){
	    var me = this, codePath = this.getCodeSourcePanel().m_codePath;
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
        if(record.get('leaf')){
            var params = {path: record.get('path')};
            me.getCodeSourcePanel().update("loading...");
            Ext.Ajax.request({
                method: 'GET',
                params: params,
                url: url,
                success: function(response){
                    if(response){
                        var html = Prism.highlight(response.responseText, Prism.languages.javascript, 'java');
                        me.getCodeSourcePanel().m_codePath = params.path;
                        me.getCodeSourcePanel().update('<pre><code class="language-java">'+html+'</code></pre>');
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

        me.getDataSourceId().setValue(dataSourcesContentValues.m_lastChooseVal.get('dataSourceId'));
        codeTreeStore.getProxy().url =  app.API_PREFIX +'/getCodeFileInfo';

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
            }
        });
    },
    generateConfigValid: function () {
        var me = this;
        if(me.getSelectedDataSource().isVisible() && Ext.isEmpty(me.getSelectedDataSource().text)){
            app.method.toastMsg("Message","must select a dataSource");
            return false;
        }
        if(me.getSelectedTemplate().isVisible() && Ext.isEmpty(me.getSelectedTemplate().text)){
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
            mainContainer = this.getCommonMainContainer();
        templatePanel.contentValues.m_mode = 'choose';
        templatePanel.contentValues.m_chooseFrom = this.getGeneratorPanel();
        templatePanel.contentValues.m_callBack = function (contentValues) {
            var record =  contentValues.m_lastChooseVal;
            me.getSelectedTemplate().setText(record.get('templateId') + ': ' + record.get('path'));
        };
        mainContainer.getLayout().setActiveItem(templatePanel);
    },
    selectDataSourceBtnClick: function (btn, e, eOpts) {
        var me = this, dataSourcesPanel = this.getDataSourcesPanel(),
            mainContainer = this.getCommonMainContainer();
        dataSourcesPanel.contentValues.m_mode = 'choose';
        dataSourcesPanel.contentValues.m_chooseFrom = this.getGeneratorPanel();
        dataSourcesPanel.contentValues.m_callBack = function (contentValues) {
            var record =  contentValues.m_lastChooseVal;
            me.getSelectedDataSource().setText(record.get('dataSourceId') + ': ' + record.get('url'));
        };
        mainContainer.getLayout().setActiveItem(dataSourcesPanel);
    },
    generateSourceBtnClick: function(btn, e, eOpts){

    }
});