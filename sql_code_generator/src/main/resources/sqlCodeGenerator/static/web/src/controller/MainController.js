Ext.define('CGT.controller.MainController', {
	extend : 'Ext.app.Controller',
	refs: [
	    {ref: 'headerPanel', selector: 'headerPanel'},
	    {ref: 'sidebarDataView', selector: 'sidebarDataView'},
	    {ref: 'commonMainContainer', selector: 'commonMainContainer'},
	    {ref: 'toggleSpaceBtn', selector: 'generatorPanel button[name=toggleSpaceBtn]'},
	    {ref: 'generatorPanelTbar', selector: 'generatorPanel container[name=generatorPanelTbar]'},
	    {ref: 'generatorPanel', selector: 'generatorPanel'},
	    {ref: 'datasourcesPanel', selector: 'datasourcespanel'},
	    {ref: 'dataSourceGrid', selector: 'datasourcespanel datasourcegrid[name=dataSourceGrid]'},
	    {ref: 'templatesPanel', selector: 'templatesPanel'},
	    {ref: 'templateDetailPanel', selector: 'templatedetailpanel'},
        {ref: 'generateCodeEditor', selector: 'generatorPanel codeeditor[name=generateCodeEditor]'},
        {ref: 'fileActionWindow', selector: 'fileactionwindow'},
        {ref: 'winDoActionBtn', selector: 'fileactionwindow button[name=doActionBtn]'},
        {ref: 'winFileName', selector: 'fileactionwindow textfield[name=fileName]'},
        {ref: 'androidFileManagerPanel', selector: 'androidfilemanagerpanel'}
    ],
    init: function(application) {
   	this.control({
           'sidebarDataView': {
               afterrender: this.dataViewSelectFirstItem,
               itemclick: this.sidebarDataViewItemClick
           },
           'datasourcespanel': {
               activate: this.datasourcesPanelActivate
           },
           'generatorPanel button[name=toggleSpaceBtn]': {
               click: this.toggleSpaceBtnClick
           },
           'filesystemtree': {
               treeContextMenuItemClick: this.fileSystemTreeContextMenuItemClick
           },
           'fileactionwindow button[name=doActionBtn]': {
               click: this.doActionBtnClick
           }
       });
    },
    doActionBtnClick: function(btn, e, eOpts){
        var me = this, fileActionWindow = this.getFileActionWindow();
        var winFileName = me.getWinFileName(),
            option = fileActionWindow.contentValues.m_option;
        if(!winFileName.validate()){
            app.method.toastMsg('Message', 'file name invalid !');
            return;
        }
        option.loadingPanel = btn;
        option.params.fileName = winFileName.getValue();
        option.callback = function (responseText) {
            fileActionWindow.close();
            var params = option.params, record = option.record;
            var newFilePath = record.get('path');
            if('new folder' ===  params.fileAction
                || 'new file' ===  params.fileAction
                ||'edit name' === params.fileAction){
                if(record.parentNode){
                    newFilePath = record.parentNode.get('path') + '/' + params.fileName;
                }
            }else if('new child folder' ===  params.fileAction
                || 'new child file' ===  params.fileAction){
                newFilePath = record.get('path') + '/' + params.fileName;
            }
            option.panel.reloadTreeWithExpanded(newFilePath);
        };
        app.method.CommonResponseEequest(option);
    },
    fileSystemTreeContextMenuItemClick: function(panel, action, menu, record){
        var me = this, url = app.API_PREFIX +'/doFileAction',
            params = {
                path: record.get('path'),
                fileAction: menu,
                fileName: ""
            };
        var option = {
            url: url,
            params: params,
            callback: null,
            method: 'POST',
            successMsg: menu + ' success.',
            errorMsg: menu + ' fail',
            loadingPanel: null,
            panel: panel,
            record:record,
            scope: me
        };
        if('delete' === menu){
            Ext.Msg.confirm('Message', 'Do you want to delete this file: <br>'+ params.path +'?', function(optional){
                if(optional === 'yes'){
                    option.callback = function (responseText) {
                        record.remove();
                    };
                    app.method.CommonResponseEequest(option);
                }
            });
        }else{
            var win = Ext.create('CGT.view.common.FileActionWindow',{
                contentValues: {
                    m_title: menu,
                    m_fileName: record.get('text'),
                    m_action: menu,
                    m_option: option
                }
            });
            win.show();
        }
    },
    toggleSpaceBtnClick: function(btn, e, eOpts){
        var headerPanel = this.getHeaderPanel(),
            generatorPanelTbar = this.getGeneratorPanelTbar(),
            generateCodeEditor = this.getGenerateCodeEditor();
        if(headerPanel.isVisible()|| generatorPanelTbar.isVisible()){
            headerPanel.setVisible(false);
            generatorPanelTbar.setVisible(false);
        }else {
            headerPanel.setVisible(true);
            generatorPanelTbar.setVisible(true);
        }
    },
    datasourcesPanelActivate: function(panel){
	    this.getDataSourceGrid().getStore().load();
    },
    dataViewSelectFirstItem: function(){
        this.getSidebarDataView().getSelectionModel().selectRange(0, 0);
    },
    sidebarDataViewItemClick: function (dataView, record, item, index, e, eOpts) {

        var functionCode = record.get('functionCode'), mainContainer = this.getCommonMainContainer();

        if (functionCode === 'generator') {
            mainContainer.getLayout().setActiveItem(this.getGeneratorPanel());
        }

        if (functionCode === 'templates') {
            mainContainer.getLayout().setActiveItem(this.getTemplatesPanel());
        }

        if (functionCode === 'dataSources') {
            mainContainer.getLayout().setActiveItem(this.getDatasourcesPanel());
        }

        if (functionCode === 'templateDetail') {
            mainContainer.getLayout().setActiveItem(this.getTemplateDetailPanel());
        }

        if (functionCode === 'androidAppFileManager') {
            mainContainer.getLayout().setActiveItem(this.getAndroidFileManagerPanel());
        }
    }
});