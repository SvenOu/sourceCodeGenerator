Ext.define('CGT.store.sql.CodeView',{
    extend:'Ext.data.TreeStore',
    model:'CGT.model.sql.CodeView',
    autoLoad: false,
    proxy: {
        type: 'ajax',
        // url: app.API_PREFIX +'/getCodeFileInfo',
        url: '#',
        reader: {
            type: 'json',
            root:'children'
        }
    }
});