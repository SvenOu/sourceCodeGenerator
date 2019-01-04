Ext.define('CGT.controller.MainController', {
	extend : 'Ext.app.Controller',
	refs: [
	    {ref: 'sidebarDataView', selector: 'sidebarDataView'},
	    {ref: 'commonMainContainer', selector: 'commonMainContainer'},
	    {ref: 'generatorPanel', selector: 'generatorPanel'},
	    {ref: 'datasourcesPanel', selector: 'datasourcesPanel'},
	    {ref: 'dataSourceGrid', selector: 'datasourcesPanel grid[name=dataSourceGrid]'},
	    {ref: 'templatesPanel', selector: 'templatesPanel'},
	    {ref: 'testPanel', selector: 'testPanel'}
    ],
    init: function(application) {
   	this.control({
           'sidebarDataView': {
               afterrender: this.dataViewSelectFirstItem,
               itemclick: this.sidebarDataViewItemClick
           },
           'datasourcesPanel': {
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

        var functionCode = record.get('functionCode'),
            mainConrainer = this.getCommonMainContainer();

        if (functionCode === 'generator') {
            mainConrainer.getLayout().setActiveItem(this.getGeneratorPanel());
        }

        if (functionCode === 'templates') {
            mainConrainer.getLayout().setActiveItem(this.getTemplatesPanel());
        }

        if (functionCode === 'dataSources') {
            mainConrainer.getLayout().setActiveItem(this.getDatasourcesPanel());
        }

        if (functionCode === 'test') {
            mainConrainer.getLayout().setActiveItem(this.getTestPanel());
        }
    }
});