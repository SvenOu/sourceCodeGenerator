Ext.define('CGT.view.common.DocWindow',{
	extend:'Ext.window.Window',
	alias : 'widget.docwindow',
	closeAction:'destroy',
	title: 'Doucument for use',
	modal: false,
	cls: 'main-common-doc-window',
	width: 750,
	height: 400,
	padding: 0,
	liveDrag : true,
	// custom attr
	contentValues: {

	},
    layout: {
        type: 'fit'
	},
	items: [
		{
            xtype: 'codeeditor',
            name: 'docCodeEditor',
            contentValues: {
                m_editorId: 'docCodeEditor',
                m_mode: 'java'
            }
		}
	]
});