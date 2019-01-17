app = {
	context:{
		imageScenarioChanges: {}
	},
	setting: {
		PAGE_SIZE: 30
		,MODE: 'Test'
		,DATETIME_DATA_FORMAT: 'Y-m-d H:i:s'
		,DATE_FORMAT: 'M d, Y'
		,DATETIME_FORMAT: 'M d, Y H:i:s'
	},
	user: '',
    API_PREFIX: '/api/controller/sqlCode',
    SECURITY_API_PREFIX: '/api/controller/security'
};

Ext.apply(app, {
	method: {
        CommonResponseEequest: function(option){
            /*
	        var option = {
	            url: '',
	            params: {},
        		callback: function (responseText) {},
        		method: 'POST',
                successMsg: '',
                errorMsg: '',
                loadingPanel: null,
                scope: null
			};
			*/
        	if(Ext.isEmpty(option.successMsg)){
                option.successMsg = 'success';
			}
        	if(Ext.isEmpty(option.errorMsg)){
                option.errorMsg = 'fail';
			}
        	if(Ext.isEmpty(option.method)){
                option.method = 'GET';
			}
			var ajaxOption = {
                method: option.method,
                params: option.params,
                url: option.url,
                success: function(response){
                    if(option.loadingPanel){
                        option.loadingPanel.setLoading(false);
                    }
                    var responseText = Ext.JSON.decode(response.responseText);
                    if(responseText){
                        if(responseText.success){
                            app.method.toastMsg('Message', option.successMsg);
                        }else {
                            if(Ext.isEmpty(responseText.errorCode)){
                                app.method.toastMsg('Message', option.errorMsg);
							}else {
                                app.method.toastMsg('Message', responseText.errorCode);
							}
                        }
                    }
                    option.callback(responseText);
                },
                failure: function(response){
                    app.method.toastMsg('Message', option.errorMsg);
                    if(option.loadingPanel){
                        option.loadingPanel.setLoading(false);
                    }
                }
            };
        	if(!Ext.isEmpty(ajaxOption.scope)){
                ajaxOption.scope = option.scope;
			}
            if(option.loadingPanel){
                option.loadingPanel.setLoading(true);
            }
            Ext.Ajax.request(ajaxOption);
		},
		columnsRenderer: {
			addtooltip: function(value, metaData){
				metaData.tdAttr = 'data-qwidth="300" data-qtip="'+value+'"';
		        return value;
		    }
			,date: function(value){
				if (!value) return '';

				if (!Ext.isDate(value)){
					value = Ext.Date.parse(value, app.setting.DATETIME_DATA_FORMAT);
				}

				return Ext.Date.format(value, app.setting.DATE_FORMAT);
			}
			,datetime: function(value){
				if (!value) return '';

				if (!Ext.isDate(value)){
					value = Ext.Date.parse(value, app.setting.DATETIME_DATA_FORMAT);
				}

				return Ext.Date.format(value, app.setting.DATETIME_FORMAT);
			}
		}
		,fontColor: function(value, color){
		    	return '<span style="color:' + color + '">' + value + '</span>';
		    }
		,printLog: function( val ){
			if( app.setting.MODE == 'PRD' || Ext.isIE8m ) return;
			console.log(val);
		}
		,comingSoon: function(){
			Ext.Msg.show({
	             title:'Warning',
	             msg: 'Coming soon...',
	             buttons: Ext.Msg.OK,
	             icon: Ext.Msg.INFO
	        });
		}
        ,createMsgBox: function(t, s){
            return '<div class="msg"><h3>' + t + '</h3><p>' + s + '</p></div>';
        }
        ,toastMsg : function(title, format){
            if(!app.msgCt){
                app.msgCt = Ext.DomHelper.insertFirst(document.body, {id:'app-msg-div'}, true);
            }
            var s = Ext.String.format.apply(String, Array.prototype.slice.call(arguments, 1));
            var m = Ext.DomHelper.append(app.msgCt, app.method.createMsgBox(title, s), true);
            m.hide();
            m.slideIn('t').ghost("t", { delay: 2000, remove: true});
        }
	}
});