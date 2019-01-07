Ext.define('CGT.store.generator.Template',{
	extend:'Ext.data.Store',
	model:'CGT.model.generator.Template',
	autoLoad: false,
	proxy:{
		type:'ajax',
		url: app.API_PREFIX + '/getAllTemplate',
		reader:{
			type:'json',
			root:'data'
		}
	}
});