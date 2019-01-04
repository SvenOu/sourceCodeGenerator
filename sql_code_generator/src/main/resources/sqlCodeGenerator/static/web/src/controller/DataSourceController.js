Ext.define('CGT.controller.DataSourceController', {
	extend : 'Ext.app.Controller',
	refs: [
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
        {ref: 'sqlRemoteConfigWindowAddBtn', selector: 'sqlremoteconfigwindow button[name=addBtn]'},
	],
    init: function(application) {
        this.control({
               'container[name=dbFilesTreePanelContainer]': {
                   afterrender: this.dbFilesTreePanelContainerAfterRender
               },
               'datasourcegrid[name=dataSourceGrid] actioncolumn': {
                   deleteBtnClick: this.dataSourceDeleteBtnClick
               },
               'datasourcegrid button[name=addDatasourceBtn]': {
                   click: this.addDatasourceBtnClick
               },
               'datasourcegrid button[name=refreshDbFiles]': {
                   click: this.refreshDbFiles
               },
               'sqlremoteconfigwindow button[name=addBtn]': {
                   click: this.sqlRemoteConfigWindowAddBtnClick
               },
               'sqlfileconfigwindow button[name=uploadDbFiles]': {
                   click: this.uploadDbFilesBtnClick
               },
               'datasourcegrid combobox[name=dataSourceType]': {
                   render: this.dataSourceTypeRender
               }
        });
    },
    refreshDbFilesTreePanelContainer: function () {
        var me = this, dbFilesTreePanel = this.getDbFilesTreePanel();
        var dbFilesTreePanelStore = dbFilesTreePanel.store;
        dbFilesTreePanelStore.getProxy().url =  app.API_PREFIX +'/getUserDbFilesInfo';
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
            type : "GET",
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

    sqlRemoteConfigWindowAddBtnClick: function(btn, e, eOpts){
        var me = this, sqlRemoteConfigWindow = me.getSqlRemoteConfigWindow();
        if(!me.remoteConfigWinFormValid()){
            return;
        }
        var params = {
            type: sqlRemoteConfigWindow.contentValus.m_type,
            url: me.getWinUrl().getValue(),
            userName: me.getWinUsername().getValue(),
            password: me.getWinPassword().getValue(),
        };
        sqlRemoteConfigWindow.setLoading(true);
        Ext.Ajax.request({
            type : "POST",
            dataType : 'json',
            params: params,
            url : app.API_PREFIX + '/addRemoteDbConfig',
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
	        type: sqlfileConfigWindow.contentValus.m_type
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
            sqlWindow.contentValus = {
                m_type: dbType,
                m_fileName: ''
            };
            sqlWindow.show();
        }else if(dbType == 'mssql'){
            var sqlWindow = Ext.create('CGT.view.common.SqlRemoteConfigWindow');
            sqlWindow.contentValus = {
                m_type: dbType,
                m_url: '',
                m_username: '',
                m_password: '',
                m_exampleUrl:'sql30.easternphoenix.com:1433/ChurchsYMTC',
                m_urlEmptyText:'a db url',
            };
            sqlWindow.show();
            me.getWinSqlType().setValue(sqlWindow.contentValus.m_type);
            me.getWinExampleUrl().setValue(sqlWindow.contentValus.m_exampleUrl);
            me.getWinUrl().emptyText = [sqlWindow.contentValus.m_urlEmptyText];
        }
    },
    headerPanelAfterRender: function (panel) {
	    this.getUserNameLabel().setText(app.user.username);
    }
});