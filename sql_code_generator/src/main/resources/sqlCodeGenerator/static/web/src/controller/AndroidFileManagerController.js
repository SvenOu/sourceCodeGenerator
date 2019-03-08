Ext.define('CGT.controller.AndroidFileManagerController', {
	extend : 'Ext.app.Controller',
	refs: [
        {ref: 'commonMainContainer', selector: 'commonMainContainer'},
	    {ref: 'androidFileManagerPanel', selector: 'androidfilemanagerpanel'},
	    {ref: 'packageName', selector: 'androidfilemanagerpanel textfield[name=packageName]'},
	    {ref: 'androidServerPort', selector: 'androidfilemanagerpanel textfield[name=androidServerPort]'},
	    {ref: 'ipAddress', selector: 'androidfilemanagerpanel textfield[name=ipAddress]'},
	    {ref: 'phonePath', selector: 'androidfilemanagerpanel displayfield[name=phonePath]'},
	    {ref: 'connectBtn', selector: 'androidfilemanagerpanel button[name=connectBtn]'},
	    {ref: 'codeTreePanel', selector: 'androidfilemanagerpanel filesystemtree[name=codeTreePanel]'},
        {ref: 'phoneFileCodeEditor', selector: 'androidfilemanagerpanel codeeditor[name=phoneFileCodeEditor]'},
        {ref: 'downloadAllFileBtn', selector: 'androidfilemanagerpanel button[name=downloadAllFileBtn]'},
        {ref: 'downloadCurrentFileBtn', selector: 'androidfilemanagerpanel button[name=downloadCurrentFileBtn]'},
        {ref: 'overwriteFile', selector: 'androidfilemanagerpanel filefield[name=overwriteFile]'},
        {ref: 'overwriteFileBtn', selector: 'androidfilemanagerpanel button[name=overwriteFileBtn]'},
        {ref: 'overwriteFileForm', selector: 'androidfilemanagerpanel form[name=overwriteFileForm]'},
    ],
    init: function(application) {
   	    this.control({
           'androidfilemanagerpanel': {
               afterrender: this.androidFileManagerPanelAfterRender
            },
            'androidfilemanagerpanel button[name=connectBtn]': {
                click: this.connectBtnClick
            },
            'androidfilemanagerpanel button[name=downloadCurrentFileBtn]': {
                click: this.downloadCurrentFileBtnClick
            },
            'androidfilemanagerpanel treepanel[name=codeTreePanel]': {
                select: this.codeTreePanelItemSelect
            },
            'androidfilemanagerpanel displayfield[name=phonePath]': {
                change: this.phonePathChange
            },
            'androidfilemanagerpanel button[name=downloadAllFileBtn]': {
                click: this.downloadAllFileBtnClick
            },
            'androidfilemanagerpanel button[name=overwriteFileBtn]': {
                click: this.overwriteFileBtnClick
            },
            'androidfilemanagerpanel filefield[name=overwriteFile]': {
                change: this.overwriteFileChange
            }
       });
    },
    overwriteFileChange: function(field, value, eOpts){
	    var me = this, cookieDb = CookieDb.getInstance();
        cookieDb.save("overwriteFile",  value);
    },
    overwriteFileBtnClick: function(btn, e, eOpts){
	    var me = this, overwriteFile = this.getOverwriteFile(),
            targetPath = btn.contentValues.m_selectedRec.path;
	    var phoneFileName = app.method.getFileNameFromPath(targetPath),
            localFileName = app.method.getFileNameFromPath(overwriteFile.getValue());

	    if(phoneFileName !== localFileName){
            Ext.Msg.alert('Message', 'Two file names are different and cannot be modified. <br />'+ phoneFileName + ', ' + localFileName);
        }else {
            Ext.Msg.confirm('Message', 'Do you want to overwrite ' + phoneFileName + '?', function(optional){
                if(optional ==='yes'){
                    me.uploadOverwriteFile(targetPath);
                }
            });
        }
    },
    uploadOverwriteFile: function (targetPath) {
        var me = this, overwriteFileForm =  this.getOverwriteFileForm(),
            overwriteFile = this.getOverwriteFile();

        if(Ext.isEmpty(overwriteFile.getValue())){
            app.method.toastMsg('Message', 'must select a file to overwrite.');
            return;
        }
        var mobileActionUrl = me.generateMobileActionUrl('/appfile/uploadAndReplaceFile?targetPath=' + targetPath);
        var params = {
            targetPath: targetPath,
            mobileActionUrl: mobileActionUrl
        };
        overwriteFileForm.setLoading(true);
        overwriteFileForm.getForm().submit({
            headers: {Accept: 'application/json;charset=UTF-8'},
            method: 'POST',
            url: app.API_PREFIX + '/uploadOverwriteFile',
            params: params,
            success: function(form, action) {
                app.method.toastMsg('Message', 'upload overwrite file success');
                overwriteFileForm.setLoading(false);
            },
            failure: function(form, action) {
                app.method.toastMsg('Warning', 'upload overwrite file fail');
                overwriteFileForm.setLoading(false);
            }
        });
    },
    downloadAllFileBtnClick: function(btn, e, eOpts){
        var mobileActionUrl = this.generateMobileActionUrl(
            '/appfile/downloadFile');
        window.open(mobileActionUrl, '_blank');
    },
    phonePathChange: function(field, newValue, oldValue, eOpts){
	    if(Ext.isEmpty(newValue)){
	        this.getDownloadCurrentFileBtn().disable();
        }else {
            this.getDownloadCurrentFileBtn().enable();
        }
    },
    codeTreePanelItemSelect: function (treePanel, record, index, eOpts){
	    var me = this, phonePath = this.getPhonePath();
	    if(!record.get('dir')){
            phonePath.setValue(record.get('path'));
        }
        var  url = app.API_PREFIX +'/doAndroidServerAction';
        var mobileActionUrl = me.generateMobileActionUrl('/appfile/getFileCode?path=' +
            record.get('path'));

        if(record.get('leaf') && !record.get('dir')){
            me.getOverwriteFileBtn().enable();
            me.getOverwriteFileBtn().contentValues.m_selectedRec = record.getData();
            var params = {
                mobileActionUrl: mobileActionUrl
            };
            me.getPhoneFileCodeEditor().setLoading(true);
            Ext.Ajax.request({
                method: 'GET',
                params: params,
                url: url,
                success: function(response){
                    me.getPhoneFileCodeEditor().setLoading(false);
                    if(response){
                        me.getPhoneFileCodeEditor().m_codePath = params.path;
                        me.getPhoneFileCodeEditor().setEditorText(response.responseText);
                        me.getPhoneFileCodeEditor().setReadOnly(true);
                    }
                },
                failure: function(response){
                    app.method.toastMsg('Message', 'getting record error!');
                },
                scope: me
            });
        }else {
            me.getOverwriteFileBtn().disable();
        }
    },
    downloadCurrentFileBtnClick: function(btn, e, eOpts){
        var me = this, phonePath = this.getPhonePath();
        if(!Ext.isEmpty(phonePath.getValue())){
            var mobileActionUrl = me.generateMobileActionUrl(
                '/appfile/downloadFile?path=' + phonePath.getValue());
            window.open(mobileActionUrl, '_blank');
        }
    },
    connectBtnClick: function(btn, e, eOpts){
        var me = this, downloadAllFileBtn = this.getDownloadAllFileBtn(),
            cookieDb = CookieDb.getInstance(),
            codeTreeStore = this.getCodeTreePanel().store,
            packageName = this.getPackageName(),
            androidServerPort = this.getAndroidServerPort(),
            ipAddress = this.getIpAddress();

        if(packageName.isVisible() && Ext.isEmpty(packageName.getValue())){
            app.method.toastMsg("Message","package name must not empty");
            return;
        }
        if(androidServerPort.isVisible() && Ext.isEmpty(androidServerPort.getValue())){
            app.method.toastMsg("Message","android server port must not empty");
            return;
        }
        if(ipAddress.isVisible() && Ext.isEmpty(ipAddress.getValue())){
            app.method.toastMsg("Message","ip address must not empty");
            return;
        }

        cookieDb.save("packageName", packageName.getValue());
        cookieDb.save("androidServerPort", androidServerPort.getValue());
        cookieDb.save("ipAddress", ipAddress.getValue());

        btn.setLoading(true);

        codeTreeStore.getProxy().url =  app.API_PREFIX +'/doAndroidServerAction';
        var mobileActionUrl = me.generateMobileActionUrl('/appfile/getFileInfo');
        downloadAllFileBtn.disable();
        codeTreeStore.load({
            params:{
                mobileActionUrl: mobileActionUrl,
            },
            callback: function (records, operation, success) {
                btn.setLoading(false);
                if(Ext.isEmpty(records)){
                    downloadAllFileBtn.disable();
                }else {
                    downloadAllFileBtn.enable();
                }
            }
        });
    },
    androidFileManagerPanelAfterRender: function(panel){
        var me = this, cookieDb = CookieDb.getInstance(),
            overwriteFile = this.getOverwriteFile(),
            packageName = this.getPackageName(),
            androidServerPort = this.getAndroidServerPort(),
            ipAddress = this.getIpAddress();

        packageName.setValue(cookieDb.find("packageName"));
        androidServerPort.setValue(cookieDb.find("androidServerPort"));
        ipAddress.setValue(cookieDb.find("ipAddress"));
    },
    generateMobileActionUrl: function (subUrl) {
        var packageName = this.getPackageName(),
            androidServerPort = this.getAndroidServerPort(),
            ipAddress = this.getIpAddress();
        var ipAddressVal = ipAddress.getValue();
        if( !ipAddressVal.startsWith('http')){
            ipAddressVal = 'http://' + ipAddressVal;
        }
        return ipAddressVal+ ":" +
            androidServerPort.getValue()+ "/"+
            packageName.getValue()+
            subUrl;
    }
});