data =
{
    module1: 'main',
    module2: 'test',
    packageName: 'CGT',
    components: [
        {
            name: 'template',
            packageName: 'CGT',
            moduleName: 'main',
            root: 'children',
            url: 'http://example.com/someRequest',
            fields: [
                { text: 'template_id', dataIndex: 'templateId', type: 'string'},
                { text: 'url', dataIndex: 'path', type: 'string'}
            ]
        }
    ]
};
for(var i =0; i<5; i++){
    var cmp = {
        name: 'template'+ i,
        packageName: 'CGT',
        moduleName: 'main',
        root: 'children',
        url: 'http://example.com/someRequest' + i,
        fields: [
            { text: 'template_id', dataIndex: 'templateId', type: 'string'},
            { text: 'url', dataIndex: 'path', type: 'string'}
        ]
    };
    data.components.push(cmp);
}
