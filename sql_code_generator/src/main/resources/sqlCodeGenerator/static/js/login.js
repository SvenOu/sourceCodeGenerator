$(function(){
	
	$('#username').keydown(function(event){
		if (event.keyCode == 13){
			$('#password').focus();
		}
	});

    $('#password').hide();
	$('#password').keydown(function(event){
		if (event.keyCode == 13){
			login();
		}
	});

	$('#btn-login').click(function(event){
		login();
	});
	
	$('#btn-reset').click(function(event){
		$('#username').val('').focus();
		$('#password').val('');
	});
	
});

function login(){
	var userName = $('#username').val(),
		password = $('#password').val();

    $.cookie("m_userName", userName);
    $.cookie("m_password", password);
	// $("form").submit();
	window.location.href = "web/master.html";
//	$.ajax({
//		type : "POST",
//		dataType : 'json',
//		data : {
//			userId: userName,
//			password: password
//		},
//		url : "controller/security/login",
//		success : function(result){
//			if(result.success=='true'||result.success==true){
//				window.location.href = "web/master.html";
//			}else {
//				$().toastmessage('showWarningToast', 'Invalid user name or password!');
//				$('#username').focus();
//			}
//		}
//	});
};
