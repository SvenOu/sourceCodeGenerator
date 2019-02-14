Ext.define('CGT.controller.DataSourceController', {
	extend : 'Ext.app.Controller',
	refs: [
        {ref: 'commonMainContainer', selector: 'commonMainContainer'},
        {ref: 'datasourcesPanel', selector: 'datasourcespanel'},
        {ref: 'dataSourceGrid', selector: 'datasourcegrid[name=dataSourceGrid]'},
        {ref: 'dataSourceType', selector: 'datasourcegrid combobox[name=dataSourceType]'},
        {ref: 'addDatasourceBtn', selector: 'datasourcegrid button[name=addDatasourceBtn]'},
        {ref: 'dbFilesTreePanelContainer', selector: 'container[name=dbFilesTreePanelContainer]'},
        {ref: 'dbFilesTreePanel', selector: 'treepanel[name=dbFilesTreePanel]'},
        {ref: 'sqlfileConfigWindow', selector: 'sqlfileconfigwindow'},
        {ref: 'sqlFileConfigForm', selector: 'sqlfileconfigwindow form[name=sqlFileConfigForm]'},
        {ref: 'dbFile', selector: 'sqlfileconfigwindow filefield[name=dbFile]'},
        {ref: 'uploadDbFiles', selector: 'sqlfileconfigwindow button[name=uploadDbFiles]'},
        {ref: 'sqlRemoteConfigWindow', selector: 'sqlremoteconfigwindow'},
        {ref: 'winSqlType', selector: 'sqlremoteconfigwindow displayfield[name=type]'},
        {ref: 'winExampleUrl', selector: 'sqlremoteconfigwindow displayfield[name=exampleUrl]'},
        {ref: 'winUrl', selector: 'sqlremoteconfigwindow textfield[name=url]'},
        {ref: 'winUsername', selector: 'sqlremoteconfigwindow textfield[name=username]'},
        {ref: 'winPassword', selector: 'sqlremoteconfigwindow textfield[name=password]'},
        {ref: 'sqlRemoteConfigWindowSaveBtn', selector: 'sqlremoteconfigwindow button[name=saveBtn]'},
        {ref: 'dataSourcesPanelBackBtn', selector: 'datasourcespanel button[name=backBtn]'},
        {ref: 'jsonEditorWindow', selector: 'jsoneditorwindow'},
        {ref: 'jsonEditorWindowAppleChangeBtn', selector: 'jsoneditorwindow button[name=appleChangeBtn]'},
        {ref: 'jsonEditorWindowDataSourceName', selector: 'jsoneditorwindow textfield[name=dataSourceName]'}
	],
    init: function(application) {
        this.control({
               'treepanel[name=dbFilesTreePanel]': {
                   select: this.dbFilesTreePanelSelect
               },
               'container[name=dbFilesTreePanelContainer]': {
                   afterrender: this.dbFilesTreePanelContainerAfterRender
               },
               'datasourcegrid[name=dataSourceGrid]': {
                   itemclick: this.dataSourceGridItemClick
               },
               'datasourcegrid[name=dataSourceGrid] actioncolumn': {
                   deleteBtnClick: this.dataSourceDeleteBtnClick,
                   editBtnClick: this.dataSourceEditBtnClickClick
               },
               'datasourcegrid button[name=addDatasourceBtn]': {
                   click: this.addDatasourceBtnClick
               },
               'datasourcegrid button[name=refreshDbFiles]': {
                   click: this.refreshDbFiles
               },
               'sqlremoteconfigwindow button[name=saveBtn]': {
                   click: this.sqlRemoteConfigWindowSaveBtnClick
               },
               'sqlfileconfigwindow button[name=uploadDbFiles]': {
                   click: this.uploadDbFilesBtnClick
               },
               'datasourcegrid combobox[name=dataSourceType]': {
                   render: this.dataSourceTypeRender
               },
               'datasourcespanel button[name=backBtn]': {
                   click: this.dataSourcesPanelBackBtnClick
               },
               'jsoneditorwindow button[name=appleChangeBtn]': {
                   click: this.jsonEditorWindowAppleChangeBtnClick
               }
        });
    },
    jsonEditorWindowAppleChangeBtnClick: function(btn, e, eOpts){
        var me = this, jsonEditorWindow = this.getJsonEditorWindow(),
            jsonEditorWindowDataSourceName = this.getJsonEditorWindowDataSourceName();
        var params = {
            type: 'custom_json',
            jsonData: jsonEditorWindow.getJsonText(),
            dataSourceName: jsonEditorWindowDataSourceName.getValue(),
            dataSourceId: jsonEditorWindow.m_dataSourceId
        };

        if(Ext.isEmpty(jsonEditorWindowDataSourceName.getValue())){
            app.method.toastMsg('Message', 'data source name is empty.');
            return;
        }
        try {
            JSON.parse(params.jsonData)
        }catch (e) {
            app.method.toastMsg('Message', 'json data is not valid format.');
            return;
        }

        jsonEditorWindow.setLoading(true);
        Ext.Ajax.request({
            type : "POST",
            dataType : 'json',
            jsonData: params,
            url : app.API_PREFIX + '/saveJsonDataSource',
            success: function(response){
                var responseText = Ext.JSON.decode(response.responseText);
                if(responseText){
                    if(responseText.success){
                        app.method.toastMsg('Message', 'save json datasource success.');
                    }else {
                        app.method.toastMsg('Message', responseText.errorCode);
                    }
                }
                me.getDataSourceGrid().getStore().load();
                jsonEditorWindow.setLoading(false);
                jsonEditorWindow.close();
            },
            failure: function() {
                app.method.toastMsg('Message', 'save json datasource fail.');
                jsonEditorWindow.setLoading(false);
            }
        });
    },
    dataSourcesPanelBackBtnClick: function(btn, e, eOpts){
        var contentValues = this.getDatasourcesPanel().contentValues,
            mainContainer = this.getCommonMainContainer();
        mainContainer.getLayout().setActiveItem(contentValues.m_chooseFrom);
        contentValues.m_mode = 'default';
        btn.setVisible(false);
    },
    dbFilesTreePanelSelect: function(treePanel, record, index, eOpts){
        this.getDbFilesTreePanel().contentValues.m_selectRecord = record;
    },
    dataSourceGridItemClick: function(table, record, item, index, e, eOpts){
        var me = this, datasourcesPanel = this.getDatasourcesPanel(), mainContainer = this.getCommonMainContainer();
        var contentValues = datasourcesPanel.contentValues;
        if(contentValues.m_mode === 'choose'){
            contentValues.m_lastChooseVal = record;
            contentValues.m_callBack(contentValues);
            mainContainer.getLayout().setActiveItem(contentValues.m_chooseFrom);
            contentValues.m_mode = 'default';
        }else if(contentValues.m_mode === 'default'){

        }
    },
    refreshDbFilesTreePanelContainer: function () {
        var me = this, dbFilesTreePanel = this.getDbFilesTreePanel();
        var dbFilesTreePanelStore = dbFilesTreePanel.store;
        dbFilesTreePanelStore.getProxy().url = app.API_PREFIX +'/getUserDbFilesInfo';
        dbFilesTreePanelStore.load({
            params:{},
            callback: function (records, operation, success) {
                // console.log(records);
            }
        });
    },
    dbFilesTreePanelContainerAfterRender: function(win){
        this.refreshDbFilesTreePanelContainer();
    },
    preformDeleteDataSource: function (view, rowIndex, colIndex, item, e, record, row) {
        var me = this;
        var params = {
            dataSourceId: record.get('dataSourceId')
        };
        Ext.Ajax.request({
            type : "POST",
            dataType : 'json',
            params: params,
            url : app.API_PREFIX + '/deleteDataSource',
            success: function(response){
                var responseText = Ext.JSON.decode(response.responseText);
                if(responseText){
                    if(responseText.success){
                        app.method.toastMsg('Message', 'delete datasource success.');
                    }else {
                        app.method.toastMsg('Message', responseText.errorCode);
                    }
                }
                me.getDataSourceGrid().getStore().load();
            },
            failure: function() {
                app.method.toastMsg('Message', 'delete datasource fail.');
            }
        });
    },
    dataSourceEditBtnClickClick: function(view, rowIndex, colIndex, item, e, record, row){
	    var me = this, type = record.get('type');
        if(type === 'custom_json'){
            if(me.jsonEditorWindow && me.jsonEditorWindow.isVisible(true)){
                me.jsonEditorWindow.close();
                return;
            }
            var jsonData = JSON.parse(record.get('jsonData'));
            me.jsonEditorWindow = Ext.create('CGT.view.common.JsonEditorWindow',{
                renderTo: Ext.getBody(),
                contentValues: {
                    m_editorId: 'jsonEditorWindow'
                },
                m_jsonData: jsonData,
                m_defaultJsonData: jsonData,
                m_dataSourceName: (record.get('url').split(':'))[1],
                m_dataSourceId: record.get('dataSourceId')
            });
            me.jsonEditorWindow .show();
        }else if(type === 'sqlite'){
            // console.log(type);
        }else if(type === 'mssql'){
            if(me.sqlRemoteConfigWindow && me.sqlRemoteConfigWindow.isVisible(true)){
                me.sqlRemoteConfigWindow.close();
                return;
            }
            me.sqlRemoteConfigWindow = Ext.create('CGT.view.common.SqlRemoteConfigWindow');
            me.sqlRemoteConfigWindow.contentValues = {
                m_type: record.get('type'),
                m_url: record.get('url').replace(/\s*jdbc:jtds:sqlserver:\/\/\s*/g,""),
                m_username: record.get('username'),
                m_password: record.get('password'),
                m_exampleUrl:'sql30.easternphoenix.com:1433/ChurchsYMTC',
                m_urlEmptyText:'a db url',
                m_dataSourceId: record.get('dataSourceId')
            };
            me.sqlRemoteConfigWindow.show();
        }
    },
    dataSourceDeleteBtnClick: function(view, rowIndex, colIndex, item, e, record, row){
	    var me = this;
        Ext.Msg.confirm('Message', 'Do you want to delete this dataSource?', function(optional){
            if(optional=='yes'){
                me.preformDeleteDataSource(view, rowIndex, colIndex, item, e, record, row);
            }
        });
    },
    dataSourceTypeRender: function(combo){
        combo.getStore().on('load',function(sto, records, successful, eOpts){
            if(records && records[0]){
                combo.setValue(records[0].data.type);
            }
        });
        combo.getStore().load();
    },

    sqlRemoteConfigWindowSaveBtnClick: function(btn, e, eOpts){
        var me = this, sqlRemoteConfigWindow = me.getSqlRemoteConfigWindow();
        if(!me.remoteConfigWinFormValid()){
            return;
        }
        var params = {
            type: sqlRemoteConfigWindow.contentValues.m_type,
            url: me.getWinUrl().getValue(),
            userName: me.getWinUsername().getValue(),
            password: me.getWinPassword().getValue(),
            dataSourceId: sqlRemoteConfigWindow.contentValues.m_dataSourceId
        };
        sqlRemoteConfigWindow.setLoading(true);
        Ext.Ajax.request({
            type : "POST",
            dataType : 'json',
            params: params,
            url : app.API_PREFIX + '/saveRemoteDbConfig',
            success: function(response){
                var responseText = Ext.JSON.decode(response.responseText);
                if(responseText.success === true){
                    app.method.toastMsg('Warning', 'add remote db config success');
                }else {
                    app.method.toastMsg('Warning', responseText.errorCode);
                }
                sqlRemoteConfigWindow.close();
                sqlRemoteConfigWindow.setLoading(false);
                me.getDataSourceGrid().getStore().load();
            },
            failure: function() {
                app.method.toastMsg('Warning', 'add remote db config fail');
                sqlRemoteConfigWindow.close();
                sqlRemoteConfigWindow.setLoading(false);
            }
        });

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
    },
    uploadDbFilesBtnClick: function (btn, e, eOpts) {
	    var me = this, sqlFileConfigForm =  this.getSqlFileConfigForm(),
            sqlfileConfigWindow = me.getSqlfileConfigWindow();

	    if(Ext.isEmpty(me.getDbFile().getValue())){
            app.method.toastMsg('Message', 'must select a db file.');
	        return;
        }
	    var params = {
	        type: sqlfileConfigWindow.contentValues.m_type
	    };
        sqlFileConfigForm.setLoading(true);
        sqlFileConfigForm.getForm().submit({
            headers: {Accept: 'application/json;charset=UTF-8'},
            method: 'POST',
            url: app.API_PREFIX + '/uploadDbFile',
            params: params,
            success: function(form, action) {
                app.method.toastMsg('Message', 'upload db file success');
                me.getDataSourceGrid().getStore().load();
                sqlFileConfigForm.setLoading(false);
                sqlfileConfigWindow.close();
                me.refreshDbFilesTreePanelContainer();
            },
            failure: function(form, action) {
                app.method.toastMsg('Warning', 'upload db file fail');
                sqlFileConfigForm.setLoading(false);
                sqlfileConfigWindow.close();
            }
        });
    },
    refreshDbFiles: function(btn, e, eOpts){
        this.refreshDbFilesTreePanelContainer();
    },
    addDatasourceBtnClick: function (btn, e, eOpts) {
	    var me = this, dataSourceType = this.getDataSourceType();
	    var dbType = dataSourceType.getValue();
	    if(Ext.isEmpty(dbType)){
            app.method.toastMsg("Message","must choose a datasource");
            return;
        }
        if(dbType == 'sqlite'){
            var sqlWindow = Ext.create('CGT.view.common.SqlFileConfigWindow');
            sqlWindow.contentValues = {
                m_type: dbType,
                m_fileName: ''
            };
            sqlWindow.show();
        }else if(dbType == 'mssql'){
            if(me.sqlRemoteConfigWindow && me.sqlRemoteConfigWindow.isVisible(true)){
                return;
            }
            me.sqlRemoteConfigWindow = Ext.create('CGT.view.common.SqlRemoteConfigWindow');
            me.sqlRemoteConfigWindow.contentValues = {
                m_type: dbType,
                m_url: '',
                m_username: '',
                m_password: '',
                m_exampleUrl:'sql30.easternphoenix.com:1433/ChurchsYMTC',
                m_urlEmptyText:'a db url',
            };
            me.sqlRemoteConfigWindow.show();
        }else if(dbType == 'custom_json'){
            if(me.jsonEditorWindow && me.jsonEditorWindow.isVisible(true)){
                return;
            }
            me.jsonEditorWindow = Ext.create('CGT.view.common.JsonEditorWindow',{
                renderTo: Ext.getBody(),
                contentValues: {
                    m_editorId: 'jsonEditorWindow'
                }
            });
            me.jsonEditorWindow .show();
        }
    },
    headerPanelAfterRender: function (panel) {
	    this.getUserNameLabel().setText(app.user.username);
    }
});