data = {
    buttonGroups: []
};

var shapes = [
    'rounded_corners',
    'rectangle'
];

var colors = [
    'light',
    'stable',
    'positive',
    'calm',
    'balanced',
    'energized',
    'assertive',
    'royal',
    'dark'
];
var fontFamilys = [
    'font_family_1',
    'font_family_2',
    'font_family_3',
    'font_family_4',
    'font_family_5'
];
var fontSizes = [
    'f1',
    'f2',
    'f3',
    'f4',
    'f5',
    'f6',
    'f7',
    'f8',
    'f9',
    'f10',
    'f11',
    'f12',
    'f13'
];
var buttonText = 'BUTTON';
for (var l = 0; l < shapes.length; l++) {
    var shape = shapes[l];
    var shapeObj = {
        background: shape +'_button',
        parent: buttonText,
        name: buttonText + '.' + shape,
        shape: shape,
        colors: []
    };
    for (var i = 0; i < colors.length; i++) {
        var color = colors[i];
        var colorObj = {
            parent: shapeObj.name,
            name: shapeObj.name + '.' + color,
            color: color,
            fontFamilys: []
        };
        for (var j = 0; j < fontFamilys.length; j++) {
            var fontFamily = fontFamilys[j];
            var fontFamilyObj = {
                parent: colorObj.name,
                name: colorObj.name + '.' + fontFamily,
                fontFamily: fontFamily,
                fontSizes: []
            };
            for (var k = 0; k < fontSizes.length; k++) {
                var fontSize = fontSizes[k];
                var fontSizeObjUpperCase = {
                    parent: fontFamilyObj.name,
                    name: fontFamilyObj.name+ '.' + fontSize + '.UPPER_CASE',
                    fontSize: fontSize
                },fontSizeObjLowerCase = {
                    parent: fontFamilyObj.name,
                    name: fontFamilyObj.name+ '.' + fontSize + '.LOWER_CASE',
                    fontSize: fontSize
                };
                fontFamilyObj.fontSizes.push(fontSizeObjUpperCase);
                fontFamilyObj.fontSizes.push(fontSizeObjLowerCase);
            }
            colorObj.fontFamilys.push(fontFamilyObj);
        }
        shapeObj.colors.push(colorObj);
    }
    data.buttonGroups.push(shapeObj);
}
