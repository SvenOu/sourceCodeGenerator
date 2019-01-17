Ext.define('CGT.model.generator.CodeView',{
    extend:'Ext.data.TreeModel',
    idProperty: 'id',
    fields: [
        { name: 'text', type: 'string', mapping: 'name'},
        { name: 'path', type: 'string'},
        { name: 'parent', type: 'string'},
        { name: 'leaf', type: 'boolean' },
        { name: 'dir', type: 'boolean' },
        { name: 'children'},
        { name: 'icon', convert: function (v, rec) {
            if(rec.get('dir')){
                return 'images/icon_folder.png';
            }
            return 'images/icon_file.png';
         }},
        { name: 'id', convert: function (v, rec) {
            return rec.get('path').replace(/\//g,'-');
         }}
    ]
});