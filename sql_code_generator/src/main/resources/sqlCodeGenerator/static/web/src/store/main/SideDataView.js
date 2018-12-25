Ext.define('CGT.store.main.SideDataView',{
    extend:'Ext.data.JsonStore',
    model:'CGT.model.main.SideDataView',
    data: [
        { src:'../web/images/icon_view.png', caption:'Home', functionCode:'home'},
        { src:'../web/images/icon_publish.png', caption:'Sql', functionCode:'sql'},
        { src:'../web/images/icon_create.png', caption:'Java', functionCode:'java'},
        { src:'../web/images/icon_report.png', caption:'JavaScript', functionCode:'javascript' }
    ]
});