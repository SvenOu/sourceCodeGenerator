$(function(){
    new LoginApplication().start();
});

function LoginApplication() {
    this.initialize.apply(this, arguments);
}
LoginApplication.prototype.initialize = function () {
    this.cookieDb = new CookieDb();
    $('#username').val(this.cookieDb._userId);
    $('#password').val(this.cookieDb._password);
};
LoginApplication.prototype.login = function(){
    $("form#login").submit();
    this.cookieDb.save(CookieDb.KEY_USER_ID, $('#username').val());
    this.cookieDb.save(CookieDb.KEY_PASSWORD, $('#password').val());
};
LoginApplication.prototype.register = function(){
    $("form#login").submit();
    this.cookieDb.save(CookieDb.KEY_USER_ID, $('#username').val());
    this.cookieDb.save(CookieDb.KEY_PASSWORD, $('#password').val());
};
LoginApplication.prototype.start = function(){
    var me = this;
    $('#username').keydown(function(event){
        if (event.which === 13){
            $('#password').focus();
        }
    });

    $('#password').keydown(function(event){
        if (event.which === 13){
            me.login();
        }
    });

    $('#btn-login').click(function(event){
        me.login();
    });

    $('#btn-register').click(function(event){
        me.register();
    });
};

function CookieDb() {
    this.initialize.apply(this, arguments);
}
CookieDb.KEY_USER_ID = "userId";
CookieDb.KEY_PASSWORD = "password";
CookieDb.DEFAULT_PASSWORD = "123456";
CookieDb.prototype.initialize = function () {
    this._password = this.constructor.DEFAULT_PASSWORD;

    var cacheUserId = this.find(this.constructor.KEY_USER_ID);
    this._userId = cacheUserId? cacheUserId: "";

    var cachePassword = this.find(this.constructor.KEY_PASSWORD);
    this._password = cachePassword? cachePassword: this.constructor.DEFAULT_PASSWORD;
};
CookieDb.prototype.find = function (key) {
    return $.cookie(key);
};
CookieDb.prototype.save = function (key, val) {
    return $.cookie(key, val);
};

