Ext.define('CGT.view.common.FileSystemTree', {
	extend: 'Ext.tree.Panel',
	alias: ['widget.filesystemtree'],
    bodyCls : 'common-file-system-tree',
    // custom attr
    contentValues: {
        m_treeId: 'defaultFileSystemTree',
		m_mode: 'write'// read, write
    },
    //just caches
    expandStatus: {},
    root: {
        expanded: true,
        dir: true,
        readonly: false,
        text: "root"
    },
    initComponent: function(){
		var me = this;

        if('undefined' === typeof(me.root.dir)){
            me.root.dir = true;
        }
        me.on('itemcontextmenu',me.selfItemContextMenu, me);
		me.callParent();
	},
    selfItemContextMenu: function(panel, record, item, index, e, eOpts){
	    if(!record.parentNode || this.root.readonly){
	        return false;
        }
        var me = this, menusArray = ['edit name', 'new folder', 'new file'];
        var items = [];
        if(record.get('dir')){
            menusArray.push('new child file');
            menusArray.push('new child folder');
        }
        menusArray.push('delete');

        Ext.Array.each(menusArray, function(menu, index, menusArraySelf) {
            items.push(Ext.create('Ext.Action', {
                m_record : record,
                text : menu,
                handler : function() {
                    me.fireEvent('treeContextMenuItemClick',
                        me, this, menu, this.m_record);
                }
            }));
        });
        var extMenu = Ext.create('Ext.menu.Menu', {
            items : items
        });
        extMenu.showAt(e.getXY());
        e.stopEvent();
    },
    reloadTreeWithExpanded: function (newFilePath) {
        var me = this;
        me.getStore().reload({
            callback: function (records, operation, success) {
                var node = null;
                Ext.Array.each(records, function(rec, index, recordsItSelf) {
                    node = rec.findChild('path', newFilePath, true)
                    if(node){
                        return false;
                    }
                });
                if(node){
                    me.expandPath(node.getPath());
                }
            }});
    }
});