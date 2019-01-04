Ext.define('CGT.store.generator.Datasource',{
	extend:'Ext.data.Store',
	model:'CGT.model.generator.Datasource',
	autoLoad: false,
	proxy:{
		type:'ajax',
        actionMethods: {
            create: 'POST',
            read: 'POST',
            update: 'POST',
            destroy: 'POST'
        },
		url: app.API_PREFIX + '/getDatasource',
		reader:{
			type:'json',
			root:'data'
		}
	}
});