Ext.define('CGT.controller.GeneratorController', {
	extend : 'Ext.app.Controller',
	refs: [
	    {ref: 'sqliteExampleBtn', selector: 'generatorPanel button[name=sqliteExampleBtn]'},
	    {ref: 'sqlServerExampleBtn', selector: 'generatorPanel button[name=sqlServerExampleBtn]'},
	    {ref: 'codeTreePanel', selector: 'generatorPanel treepanel[name=codeTreePanel]'},
	    {ref: 'codeSourcePanel', selector: 'generatorPanel panel[name=codeSourcePanel]'},
	    {ref: 'downloadCurrentFileBtn', selector: 'generatorPanel button[name=downloadCurrentFileBtn]'},
	    {ref: 'downloadAllFileBtn', selector: 'generatorPanel button[name=downloadAllFileBtn]'},
	    {ref: 'codeType', selector: 'generatorPanel displayfield[name=codeType]'},
	    {ref: 'sqlDataBaseConfigWindow', selector: 'sqldatabaseconfigwindow'},
	    {ref: 'winSqlType', selector: 'sqldatabaseconfigwindow displayfield[name=type]'},
	    {ref: 'winExampleUrl', selector: 'sqldatabaseconfigwindow displayfield[name=exampleUrl]'},
	    {ref: 'winUrl', selector: 'sqldatabaseconfigwindow textfield[name=url]'},
	    {ref: 'winUsername', selector: 'sqldatabaseconfigwindow textfield[name=username]'},
	    {ref: 'winPassword', selector: 'sqldatabaseconfigwindow textfield[name=password]'},
	    {ref: 'winGenerateSourceBtn', selector: 'sqldatabaseconfigwindow button[name=generateSourceBtn]'},
	    // {ref: 'generatorPanel', selector: 'generatorPanel'},
	    // {ref: 'templatesPanel', selector: 'templatesPanel'},
	    // {ref: 'datasourcesPanel', selector: 'datasourcesPanel'},
	    // {ref: 'testPanel', selector: 'testPanel'},
    ],
    init: function(application) {
   	this.control({
           'generatorPanel button[name=sqliteExampleBtn]': {
               click: this.sqliteExampleBtnClick
           },
           'generatorPanel button[name=sqlServerExampleBtn]': {
               click: this.sqlServerExampleBtnClick
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
	    var me = this, type = me.getCodeType().getValue();
	    if(!Ext.isEmpty(type)){
            var url = app.API_PREFIX +'/downloadAllFile?' + Ext.urlEncode({
                userId: app.user.userId,
                type: type
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
    sqlServerExampleBtnClick: function (btn, e, eOpts) {
        var me = this, codeTreeStore = this.getCodeTreePanel().store,
            type = 'sqlServer2005', downloadAllFileBtn = this.getDownloadAllFileBtn();

        var sqlWindow = Ext.create('CGT.view.common.SqlDataBaseConfigWindow');
        sqlWindow.contentValus = {
            m_type: type,
            m_url: '',
            m_username: '',
            m_password: '',
            m_exampleUrl:'jdbc:jtds:sqlserver://sql30.easternphoenix.com:1433/ChurchsYMTC',
            m_urlEmptyText:'jdbc:jtds:sqlserver:{sqlite file path}',
        };
        sqlWindow.show();
        me.getWinSqlType().setValue(sqlWindow.contentValus.m_type);
        me.getWinExampleUrl().setValue(sqlWindow.contentValus.m_exampleUrl);
        me.getWinUrl().emptyText = [sqlWindow.contentValus.m_urlEmptyText];
    },
    sqliteExampleBtnClick: function (btn, e, eOpts) {
        var me = this, codeTreeStore = this.getCodeTreePanel().store,
            type = 'sqlite', downloadAllFileBtn = this.getDownloadAllFileBtn();
        var sqlWindow = Ext.create('CGT.view.common.SqlDataBaseConfigWindow');
        sqlWindow.contentValus = {
            m_type: type,
            m_url: '',
            m_username: '',
            m_password: '',
            m_exampleUrl:'jdbc:sqlite:E:/IntenlliJ_IDEA_workspace/sourceCodeGenerator/sql_code_generator/db/dev_test1.sqlite',
            m_urlEmptyText:'jdbc:sqlite:{sqlite file path}',
        };
        sqlWindow.show();
        me.getWinSqlType().setValue(sqlWindow.contentValus.m_type);
        me.getWinExampleUrl().setValue(sqlWindow.contentValus.m_exampleUrl);
        me.getWinUrl().emptyText = [sqlWindow.contentValus.m_urlEmptyText];
        me.getWinUsername().setVisible(false);
        me.getWinPassword().setVisible(false);
    },
    generateSourceBtnClick: function(btn, e, eOpts){
	    var me = this, codeTreeStore = this.getCodeTreePanel().store,
            downloadAllFileBtn = this.getDownloadAllFileBtn(),
            win = btn.up('sqldatabaseconfigwindow');
        var contentValus = win.contentValus;
        contentValus.m_url = me.getWinUrl().getValue();
        contentValus.m_username = me.getWinUsername().getValue();
        contentValus.m_password = me.getWinPassword().getValue();
        me.getCodeType().setValue(contentValus.m_type);
        codeTreeStore.getProxy().url =  app.API_PREFIX +'/getCodeFileInfo';

        if(!me.remoteConfigWinFormValid()){
            return;
        }
        codeTreeStore.load({
            params:{
                type: contentValus.m_type,
                url: contentValus.m_url,
                packageName: "com.sv.test",
                username: contentValus.m_username,
                password: contentValus.m_password
            },
            callback: function (records, operation, success) {
                if(Ext.isEmpty(records)){
                    downloadAllFileBtn.disable();
                }else {
                    downloadAllFileBtn.enable();
                }
            }
        });
        win.close();
    },
    remoteConfigWinFormValid: function () {
        var me = this;
        if(me.getWinUrl().isVisible() &&!me.getWinUrl().isValid()){
            app.method.toastMsg("Message","url require not empty");
            return false;
        }
        if(me.getWinUsername().isVisible() &&!me.getWinUsername().isValid()){
            app.method.toastMsg("Message","user name require not empty");
            return false;
        }
        if(me.getWinPassword().isVisible() &&!me.getWinPassword().isValid()){
            app.method.toastMsg("Message","password require not empty");
            return false;
        }
        return true;
    }
});