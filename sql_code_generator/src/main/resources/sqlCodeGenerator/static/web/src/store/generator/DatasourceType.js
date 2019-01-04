Ext.define('CGT.store.generator.DatasourceType',{
	extend:'Ext.data.Store',
	model:'CGT.model.generator.DatasourceType',
	autoLoad: false,
	proxy:{
		type:'ajax',
		url: app.API_PREFIX + '/getAllDataSourceTypes',
		reader:{
			type:'json',
			root:'data'
		}
	}
});