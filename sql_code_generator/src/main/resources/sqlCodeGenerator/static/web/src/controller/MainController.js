Ext.define('CGT.controller.MainController', {
	extend : 'Ext.app.Controller',
	refs: [
	    {ref: 'sidebarDataView', selector: 'sidebarDataView'},
	    {ref: 'commonMainContainer', selector: 'commonMainContainer'},
	    {ref: 'generatorPanel', selector: 'generatorPanel'},
	    {ref: 'datasourcesPanel', selector: 'datasourcespanel'},
	    {ref: 'dataSourceGrid', selector: 'datasourcespanel datasourcegrid[name=dataSourceGrid]'},
	    {ref: 'templatesPanel', selector: 'templatesPanel'},
	    {ref: 'templateDetailPanel', selector: 'templatedetailpanel'}
    ],
    init: function(application) {
   	this.control({
           'sidebarDataView': {
               afterrender: this.dataViewSelectFirstItem,
               itemclick: this.sidebarDataViewItemClick
           },
           'datasourcespanel': {
               activate: this.datasourcesPanelActivate
           }
       });
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