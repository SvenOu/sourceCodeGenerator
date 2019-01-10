Ext.define('CGT.view.common.UploadTemplateFileWindow',{
	extend:'Ext.window.Window',
	alias : 'widget.uploadtemplatefilewindow',
	closeAction:'destroy',
	title: 'Upload your templates folder',
	modal: true,
	cls: 'main-common-upload-template-window',
	width: 1000,
	// height: 700,
	padding: 15,
	liveDrag : true,
	// custom attr
	contentValues: {
        m_folderPath: null
	},
    layout: {
        type: 'fit'
	},
    initComponent:function(){
		var me = this;
        me.items = [
            {
                xtype: 'form',
                name: 'templateFileForm',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                items: [
                    {
                        xtype: 'filefield',
                        name: 'templateFolderFile',
                        allowBlank: false,
                        labelAlign: 'right',
                        fieldLabel: 'templates folder (zip file)',
                        msgTarget: 'side',
                        labelWidth: 175,
                        regex: /^.*\.(zip|Zip)$/,
                        regexText: 'Only zip format file allowed',
                        flex: 1,
                        buttonText: 'Select a template folder',
                        listeners: {
                            change: me.templateFolderFileFieldChange,
                            scope: me
                        }
                    }
                ]
            }
        ];
		me.callParent();
	},
    bbar: {
		layout: {
			type: 'hbox'
		},
		items: [
            {
                xtype: 'textfield',
                name: 'templateName',
                fieldLabel: 'template name',
                allowBlank: false,
                readOnly: true
            },
			{
				xtype: 'box',
				flex: 1
			},
            {
                xtype: 'button',
				name: 'startUploadBtn',
                text: 'start upload'
            }
        ]
	},
    templateFolderFileFieldChange: function (fileField, value, eOpts) {
		if(Ext.isEmpty(value)){
			return;
		}
        var fileRawName = value.replace(/^.*\\/, "");
        var fileName = fileRawName.substring(0, fileRawName.lastIndexOf('.'));
		this.down('textfield[name=templateName]').setValue(fileName);
    }
});