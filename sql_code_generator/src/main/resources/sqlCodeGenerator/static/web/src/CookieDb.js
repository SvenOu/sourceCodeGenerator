
function CookieDb() {
    this.initialize.apply(this, arguments);
}
CookieDb.DEFAULT_EXDAYS = "30";

CookieDb.getInstance = function(){
    if(!this.instance){
        this.instance = new CookieDb();
    }
    return this.instance;
};

CookieDb.prototype.initialize = function () {
    this._defaultExdays = this.constructor.DEFAULT_EXDAYS;
};

CookieDb.prototype.find = function (key) {
    var name = key + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for(var i = 0; i <ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) === ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
};

CookieDb.prototype.save = function (key, val) {
    var d = new Date();
    d.setTime(d.getTime() + (this._defaultExdays * 24 * 60 * 60 * 1000));
    var expires = "expires="+ d.toUTCString();
    document.cookie = key + "=" + val + ";" + expires + ";path=/";
    return find(key);
};