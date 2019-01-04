Ext.define('CGT.store.main.SideDataView',{
    extend:'Ext.data.JsonStore',
    model:'CGT.model.main.SideDataView',
    data: [
        { src:'../web/images/icon_view.png', caption:'Generator', functionCode:'generator'},
        { src:'../web/images/icon_publish.png', caption:'Templates', functionCode:'templates'},
        { src:'../web/images/icon_create.png', caption:'Data sources', functionCode:'dataSources'},
        { src:'../web/images/icon_report.png', caption:'Templates Detail', functionCode:'templateDetail' }
    ]
});