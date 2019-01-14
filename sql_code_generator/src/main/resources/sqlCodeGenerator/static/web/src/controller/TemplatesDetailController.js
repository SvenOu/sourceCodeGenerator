Ext.define('CGT.controller.TemplatesDetailController', {
	extend : 'Ext.app.Controller',
	refs: [
        {ref: 'templateDetailPanel', selector: 'templatedetailpanel'},
        {ref: 'templatesTreePanel', selector: 'templatedetailpanel treepanel[name=templatesTreePanel]'},
        {ref: 'detailCodeEditor', selector: 'templatedetailpanel codeeditor[name=detailCodeEditor]'},
        {ref: 'saveCodeBtn', selector: 'codeeditor button[name=saveCodeBtn]'},
        {ref: 'reloadCodeBtn', selector: 'codeeditor button[name=reloadCodeBtn]'},
        {ref: 'resetDefTplBtn', selector: 'treepanel button[name=resetDefTplBtn]'},
        {ref: 'showDocBtn', selector: 'templatedetailpanel button[name=showDocBtn]'},
        {ref: 'deleteSelectedEdBtn', selector: 'templatedetailpanel button[name=deleteSelectedEdBtn]'},
        {ref: 'templateGrid', selector: 'templatesPanel templategrid[name=templateGrid]'},
        {ref: 'downloadAllTemplatesBtn', selector: 'templatedetailpanel button[name=downloadAllTemplatesBtn]'},
	],
    init: function(application) {
	    this.control({
           'templatedetailpanel': {
               afterrender: this.templatesTreePanelAfterRender
           },
            'templatedetailpanel treepanel[name=templatesTreePanel]': {
                select: this.codeTreePanelItemSelect
            },
            'codeeditor button[name=saveCodeBtn]': {
                click: this.saveCodeBtnClick
            },
            'codeeditor button[name=reloadCodeBtn]': {
                click: this.reloadCodeBtnClick
            },
            'treepanel button[name=resetDefTplBtn]': {
                click: this.resetDefTplBtnClick
            },
            'templatedetailpanel button[name=showDocBtn]': {
                click: this.showDocBtnClick
            },
            'templatedetailpanel button[name=deleteSelectedEdBtn]': {
                click: this.deleteSelectedEdBtnClick
            },
            'templatedetailpanel button[name=downloadAllTemplatesBtn]': {
                click: this.downloadAllTemplatesBtnClick
            }
	    });
    },
    downloadAllTemplatesBtnClick: function(btn, e, eOpts){
        var url = app.API_PREFIX +'/downloadAllTemplateFile';
        window.open(url, '_blank');
    },
    deleteSelectedEdBtnClick: function(btn, e, eOpts){
	    var me = this, templatesTreePanel = this.getTemplatesTreePanel();
	    var selectRec = templatesTreePanel.contentValues.m_selectRecord,
            url = app.API_PREFIX +'/deleteUserTemplate';

	    if(!selectRec){
            app.method.toastMsg('Message', 'must select a template root directory.');
	        return;
        }

        Ext.Msg.confirm('Warning', 'Delete this template file, the related records will also be deleted, do you want to delete?', function(optional){
            if(optional=='yes'){
                templatesTreePanel.setLoading(true);
                var params = {
                    path: selectRec.get('path')
                };
                Ext.Ajax.request({
                    method: 'POST',
                    params: params,
                    url: url,
                    success: function(response){
                        templatesTreePanel.setLoading(false);
                        var responseText = Ext.JSON.decode(response.responseText);
                        if(responseText){
                            if(responseText.success){
                                app.method.toastMsg('Message', 'delete template success.');
                            }else {
                                app.method.toastMsg('Message', responseText.errorCode);
                            }
                        }
                        me.refreshTreePanel();
                        me.getTemplateGrid().getStore().load();
                    },
                    failure: function(response){
                        app.method.toastMsg('Message', 'delete template error!');
                        templatesTreePanel.setLoading(false);
                    },
                    scope: me
                });
            }
        });
    },
    showDocBtnClick: function(btn, e, eOpts){
	    var me = this;
	    if(me.docWindow && me.docWindow.isVisible(true)){
	        return;
        }
        me.docWindow = Ext.create('CGT.view.common.DocWindow',{
            renderTo: Ext.getBody(),
        });
        me.docWindow.show();
    },
    resetDefTplBtnClick: function(btn, e, eOpts){
        var me = this, templateDetailPanel = this.getTemplateDetailPanel(),
            url = app.API_PREFIX +'/resetDefaultUserTemplate';
        var params = {};
        Ext.Msg.confirm('Message', 'Do you want to reset the default templates?', function(optional){
            if(optional==='yes'){
                templateDetailPanel.setLoading(true);
                Ext.Ajax.request({
                    method: 'POST',
                    params: params,
                    url: url,
                    success: function(response){
                        templateDetailPanel.setLoading(false);
                        var responseText = Ext.JSON.decode(response.responseText);
                        if(responseText){
                            if(responseText.success){
                                // me.refreshTreePanel();
                                var params = {path: this.getDetailCodeEditor().m_codePath};
                                if(!Ext.isEmpty(params.path)){
                                    me.doReloadFileCode(params);
                                }
                                app.method.toastMsg('Message', 'reset default templates file success.');
                            }else {
                                app.method.toastMsg('Message', responseText.errorCode);
                            }
                        }
                    },
                    failure: function(response){
                        app.method.toastMsg('Message', 'reset error!');
                    },
                    scope: me
                });
            }
        });
    },
    saveCodeBtnClick: function(btn, e, eOpts){
	    var me = this, detailCodeEditor = this.getDetailCodeEditor(),
            url = app.API_PREFIX +'/saveSourceFileCode';

        if(Ext.isEmpty(detailCodeEditor.m_codePath)){
            return;
        }
        me.getDetailCodeEditor().setLoading(true);
        var params = {
            path: detailCodeEditor.m_codePath,
            content: detailCodeEditor.editor.getValue()
        };
        Ext.Ajax.request({
            method: 'POST',
            params: params,
            url: url,
            success: function(response){
                me.getDetailCodeEditor().setLoading(false);
                var responseText = Ext.JSON.decode(response.responseText);
                if(responseText){
                    if(responseText.success){
                        app.method.toastMsg('Message', 'save file success.');
                    }else {
                        app.method.toastMsg('Message', responseText.errorCode);
                    }
                }
            },
            failure: function(response){
                app.method.toastMsg('Message', 'saving content error!');
            },
            scope: me
        });
    },
    reloadCodeBtnClick: function(btn, e, eOpts){
	    var me = this, detailCodeEditor = this.getDetailCodeEditor();
	    if(Ext.isEmpty(detailCodeEditor.m_codePath)){
	        return;
        }
        var params = {path: detailCodeEditor.m_codePath};
	    if(!Ext.isEmpty(params.path)){
            me.doReloadFileCode(params);
        }
    },
    doReloadFileCode: function (params) {
        var me = this, url = app.API_PREFIX +'/getSourceFileCode';
        me.getDetailCodeEditor().setLoading(true);
        Ext.Ajax.request({
            method: 'GET',
            params: params,
            url: url,
            success: function(response){
                me.getDetailCodeEditor().setLoading(false);
                if(response){
                    me.getDetailCodeEditor().m_codePath = params.path;
                    me.getDetailCodeEditor().setEditorText(response.responseText);
                }
            },
            failure: function(response){
                app.method.toastMsg('Message', 'getting data error!');
            },
            scope: me
        });
    },
    codeTreePanelItemSelect: function (treePanel, record, index, eOpts){
        var me = this;
        me.getTemplatesTreePanel().contentValues.m_selectRecord = record;
        if(record.get('leaf')){
            var params = {path: record.get('path')};
            me.doReloadFileCode(params);
        }
    },
    refreshTreePanel: function () {
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
    },
    templatesTreePanelAfterRender: function (panel) {
	    var me = this;
        var task = new Ext.util.DelayedTask(function(){
            me.refreshTreePanel();
        });
        task.delay(2000);
    }
});