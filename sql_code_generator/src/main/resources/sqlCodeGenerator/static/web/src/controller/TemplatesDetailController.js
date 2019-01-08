Ext.define('CGT.controller.TemplatesDetailController', {
	extend : 'Ext.app.Controller',
	refs: [
        {ref: 'templateDetailPanel', selector: 'templatedetailpanel'},
        {ref: 'templatesTreePanel', selector: 'templatedetailpanel treepanel[name=templatesTreePanel]'},
        {ref: 'codeSourcePanel', selector: 'templatedetailpanel panel[name=codeSourcePanel]'},
	],
    init: function(application) {
	    this.control({
           'templatedetailpanel': {
               afterrender: this.templatesTreePanelAfterRender
           },
            'templatedetailpanel treepanel[name=templatesTreePanel]': {
                select: this.codeTreePanelItemSelect
            },
	    });
    },
    codeTreePanelItemSelect: function (treePanel, record, index, eOpts){
        var me = this, url = app.API_PREFIX +'/getSourceFileCode';
        me.getTemplatesTreePanel().contentValues.m_selectRecord = record;
        if(record.get('leaf')){
            var params = {path: record.get('path')};
            me.getCodeSourcePanel().update("loading...");
            Ext.Ajax.request({
                method: 'GET',
                params: params,
                url: url,
                success: function(response){
                    if(response){
                        var html = Prism.highlight(response.responseText, Prism.languages.javascript, 'java');
                        me.getCodeSourcePanel().m_codePath = params.path;
                        me.getCodeSourcePanel().update('<pre><code class="language-java">'+html+'</code></pre>');
                    }
                },
                failure: function(response){
                    app.method.toastMsg('Message', 'getting record error!');
                },
                scope: me
            });
        }
    },
    templatesTreePanelAfterRender: function (panel) {
        var me = this, templatesTreePanel = this.getTemplatesTreePanel();
        var templatesTreePanelStore = templatesTreePanel.store;
        templatesTreePanelStore.getProxy().url =  app.API_PREFIX +'/getTemplateFilesInfo';
        templatesTreePanelStore.load({
            params:{},
            callback: function (records, operation, success) {
                // console.log(records);
                // templatesTreePanel.getRootNode().expand(true);
            }
        });
    }
});