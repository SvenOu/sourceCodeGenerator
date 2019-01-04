Ext.define('CGT.store.generator.CodeView',{
    extend:'Ext.data.TreeStore',
    model:'CGT.model.generator.CodeView',
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