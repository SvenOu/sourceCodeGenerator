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
        {ref: 'generateCodeEditor', selector: 'generatorPanel codeeditor[name=generateCodeEditor]'}
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
           }
       });
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
        generateCodeEditor.resize()
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
    }
});