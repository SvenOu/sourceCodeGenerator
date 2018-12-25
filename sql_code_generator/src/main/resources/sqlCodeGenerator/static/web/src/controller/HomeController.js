Ext.define('CGT.controller.HomeController', {
	extend : 'Ext.app.Controller',
	refs: [
	    {ref: 'sqliteExampleBtn', selector: 'homePanel button[name=sqliteExampleBtn]'},
	    {ref: 'sqlServerExampleBtn', selector: 'homePanel button[name=sqlServerExampleBtn]'},
	    {ref: 'codeTreePanel', selector: 'homePanel treepanel[name=codeTreePanel]'},
	    {ref: 'codeSourcePanel', selector: 'homePanel panel[name=codeSourcePanel]'},
	    {ref: 'downloadCurrentFileBtn', selector: 'homePanel button[name=downloadCurrentFileBtn]'},
	    {ref: 'downloadAllFileBtn', selector: 'homePanel button[name=downloadAllFileBtn]'},
	    {ref: 'codeType', selector: 'homePanel displayfield[name=codeType]'},
	    {ref: 'sqlDataBaseConfigWindow', selector: 'sqldatabaseconfigwindow'},
	    {ref: 'winSqlType', selector: 'sqldatabaseconfigwindow displayfield[name=type]'},
	    {ref: 'winExampleUrl', selector: 'sqldatabaseconfigwindow displayfield[name=exampleUrl]'},
	    {ref: 'winUrl', selector: 'sqldatabaseconfigwindow textfield[name=url]'},
	    {ref: 'winUsername', selector: 'sqldatabaseconfigwindow textfield[name=username]'},
	    {ref: 'winPassword', selector: 'sqldatabaseconfigwindow textfield[name=password]'},
	    {ref: 'winGenerateSourceBtn', selector: 'sqldatabaseconfigwindow button[name=generateSourceBtn]'},
	    // {ref: 'homePanel', selector: 'homePanel'},
	    // {ref: 'javaPanel', selector: 'javaPanel'},
	    // {ref: 'javaScriptPanel', selector: 'javaScriptPanel'},
	    // {ref: 'sqlPanel', selector: 'sqlPanel'},
    ],
    init: function(application) {
   	this.control({
           'homePanel button[name=sqliteExampleBtn]': {
               click: this.sqliteExampleBtnClick
           },
           'homePanel button[name=sqlServerExampleBtn]': {
               click: this.sqlServerExampleBtnClick
           },
           'homePanel treepanel[name=codeTreePanel]': {
               select: this.codeTreePanelItemSelect
           },
           'homePanel button[name=downloadCurrentFileBtn]': {
               click: this.downloadCurrentFileBtnClick
           },
           'homePanel button[name=downloadAllFileBtn]': {
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
            var url = '../..'+ app.API_PREFIX +'/downloadAllFile?' + Ext.urlEncode({
                userId: app.user.userId,
                type: type
            });
            window.open(url, '_blank');
        }
    },
    downloadCurrentFileBtnClick: function(btn, e, eOpts){
	    var me = this, codePath = this.getCodeSourcePanel().m_codePath;
	    if(!Ext.isEmpty(codePath) && "#" !== codePath){
            var url = '../..'+ app.API_PREFIX +'/downloadSourcesFile?' + Ext.urlEncode({
                userId: app.user.userId,
                path: codePath
            });
            window.open(url, '_blank');
        }
    },
    codeTreePanelItemSelect: function (treePanel, record, index, eOpts){
        var me = this, url = '../..'+ app.API_PREFIX +'/getSourceFileCode';
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

        // var me = this, codeTreeStore = this.getCodeTreePanel().store,
        //     type = 'sqlServer2005', downloadAllFileBtn = this.getDownloadAllFileBtn();
        // me.getCodeType().setValue(type);
        // codeTreeStore.getProxy().url = "../..'+ app.API_PREFIX +'/getCodeFileInfo";
        // codeTreeStore.load({
        //     params: {type: type},
        //     callback: function (records, operation, success) {
        //         if(Ext.isEmpty(records)){
        //             downloadAllFileBtn.disable();
        //         }else {
        //             downloadAllFileBtn.enable();
        //         }
        //     }
        // });
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
        codeTreeStore.getProxy().url = '../..'+ app.API_PREFIX +'/getCodeFileInfo';

        if(!me.dbConfigWinFormValid()){
            return;
        }
        codeTreeStore.load({
            params:{
                type: contentValus.m_type,
                url: contentValus.m_url,
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
    dbConfigWinFormValid: function () {
        var me = this;
        if(me.getWinUrl().isVisible() &&!me.getWinUrl().isValid()){
            Ext.Msg.show({
                title: 'Message',
                msg: 'url require not empty'
            });
            return false;
        }
        if(me.getWinUsername().isVisible() &&!me.getWinUsername().isValid()){
            Ext.Msg.show({
                title: 'Message',
                msg: 'user name require not empty'
            });
            return false;
        }
        if(me.getWinPassword().isVisible() &&!me.getWinPassword().isValid()){
            Ext.Msg.show({
                title: 'Message',
                msg: 'password require not empty'
            });
            return false;
        }
        return true;
    }
});