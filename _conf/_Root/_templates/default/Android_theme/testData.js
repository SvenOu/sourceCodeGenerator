var text_default = 'default';
data = {
    textGroups: [],
    buttonGroups: [],
    buttonBackground: {
        'shape_default':[],
        'rectangle_corner': [],
        'rectangle': []
    }
};
var shapes = [
    'shape_'+ text_default,
    'rectangle_corner',
    'rectangle'
];

var textColors = [
    'textColors_' + text_default,
    'text_red',
    'text_white',
    'text_dark'
];

var colors = [
    'color_'+text_default,
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
    'font_family_'+ text_default,
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
// text
var text = 'TEXT';
for (var i = 0; i < textColors.length; i++) {
    var textColor = textColors[i];
    var textColorObj = {
        parent: text,
        name: text + '.' + textColor,
        textColor: textColor,
        fontFamilys: []
    };
    for (var j = 0; j < fontFamilys.length; j++) {
        var fontFamily = fontFamilys[j];
        var fontFamilyObj = {
            parent: textColorObj.name,
            name: textColorObj.name + '.' + fontFamily,
            fontFamily: fontFamily,
            fontSizes: []
        };
        for (var k = 0; k < fontSizes.length; k++) {
            var fontSize = fontSizes[k];
            var fontSizeObjUpperCase = {
                parent: fontFamilyObj.name,
                name: fontFamilyObj.name+ '.' + fontSize + '.UPPER_CASE',
                textAllCaps: true,
                fontSize: fontSize
            },fontSizeObjLowerCase = {
                parent: fontFamilyObj.name,
                name: fontFamilyObj.name+ '.' + fontSize,
                textAllCaps: false,
                fontSize: fontSize
            };
            fontFamilyObj.fontSizes.push(fontSizeObjLowerCase);
            fontFamilyObj.fontSizes.push(fontSizeObjUpperCase);
        }
        textColorObj.fontFamilys.push(fontFamilyObj);
    }
    data.textGroups.push(textColorObj);
}

// button
var buttonText = 'BUTTON';
for (var l = 0; l < shapes.length; l++) {
    var shape = shapes[l];
    var shapeObj = {
        parent: buttonText,
        name: buttonText + '.' + shape,
        shape: shape,
        colors: []
    };
    for (var m = 0; m < colors.length; m++) {
        var color = colors[m];
        var colorObj = {
            background: shapeObj.shape + '_'+ color + '_button',
            parent: shapeObj.name,
            name: shapeObj.name + '.' + color,
            color: color,
            textColors: []
        };

        var bgDrawalbleObj = {
            id: colorObj.background,
            shape: shapeObj.shape,
            color: colorObj.color
        };

        var contains = false;
        Ext.Array.each(data.buttonBackground[shapeObj.shape],
            function(obj, index) {
            if(obj.id === colorObj.background){
                contains = true;
                return false;
            }
        });
        if(!contains && colorObj.background.indexOf(text_default) === -1){
            data.buttonBackground[shapeObj.shape].push(bgDrawalbleObj);
        }

        for (var i = 0; i < textColors.length; i++) {
            var textColor = textColors[i];
            var textColorObj = {
                parent: colorObj.name,
                name: colorObj.name + '.' + textColor,
                textColor: textColor,
                fontFamilys: []
            };
            for (var j = 0; j < fontFamilys.length; j++) {
                var fontFamily = fontFamilys[j];
                var fontFamilyObj = {
                    parent: textColorObj.name,
                    name: textColorObj.name + '.' + fontFamily,
                    fontFamily: fontFamily,
                    textAllCaps: false
                },fontFamilyObjUpperCase = {
                    parent: textColorObj.name,
                    name: textColorObj.name + '.' + fontFamily+ '.UPPER_CASE',
                    fontFamily: fontFamily,
                    textAllCaps: true
                };
                textColorObj.fontFamilys.push(fontFamilyObj);
                textColorObj.fontFamilys.push(fontFamilyObjUpperCase);

                // fontSizes 太多引起文件太大所以去掉
                // var fontFamilyObj = {
                //     parent: textColorObj.name,
                //     name: textColorObj.name + '.' + fontFamily,
                //     fontFamily: fontFamily,
                //     fontSizes: []
                // };
                // for (var k = 0; k < fontSizes.length; k++) {
                //     var fontSize = fontSizes[k];
                //     var fontSizeObjUpperCase = {
                //         parent: fontFamilyObj.name,
                //         name: fontFamilyObj.name+ '.' + fontSize + '.UPPER_CASE',
                //         textAllCaps: true,
                //         fontSize: fontSize
                //     },fontSizeObjLowerCase = {
                //         parent: fontFamilyObj.name,
                //         name: fontFamilyObj.name+ '.' + fontSize,
                //         textAllCaps: false,
                //         fontSize: fontSize
                //     };
                //     fontFamilyObj.fontSizes.push(fontSizeObjLowerCase);
                //     fontFamilyObj.fontSizes.push(fontSizeObjUpperCase);
                // }
                // textColorObj.fontFamilys.push(fontFamilyObj);
            }
            colorObj.textColors.push(textColorObj);
        }
        shapeObj.colors.push(colorObj);
    }
    data.buttonGroups.push(shapeObj);
}

