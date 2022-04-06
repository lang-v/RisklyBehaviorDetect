const ck = {
    setCookie(name, value, exdays) {
        //name  :表示cookie的名称，比如token
        //value  :表示cookie的值
        //exdays  :表示cookie的有效时间
        var d = new Date();
        d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
        var expires = "expires=" + d.toGMTString();
        document.cookie = name + "=" + value + "; " + expires;
    },
    getCookie(name) {
        let ret, m;
        if (typeof name === 'string' && name !== '') {
            if ((m = String(document.cookie).match(
                new RegExp('(?:^| )' + name + '(?:(?:=([^;]*))|;|$)')))) {
                ret = m[1] ? decodeURIComponent(m[1]) : ''
            }
        }
        console.log(name,ret)
        return ret
    },

    refreshCookies(globalProperties) {
        globalProperties.$userinfo.userid = ck.getCookie('userid')
        globalProperties.$userinfo.username = ck.getCookie('username')
        globalProperties.$userinfo.token = ck.getCookie('token')
        globalProperties.$userinfo.hasLogin = ck.getCookie('hasLogin')
        // globalProperties.$userinfo.username = this.getCookie('username');
        // globalProperties.$userinfo.token = this.getCookie('token');
    },

    saveCookies(userid,username, token, hasLogin) {
        this.setCookie('userid', userid, 10)
        this.setCookie('username', username, 10)
        this.setCookie('token', token, 10)
        this.setCookie('hasLogin', hasLogin, 10)
        // this.refreshCookies()
    },

    clearCookies() {
        this.setCookie('userid', '', 999)
        this.setCookie('username', 'None', 999)
        this.setCookie('token', '', 999)
        this.setCookie('hasLogin', false, 999)
    }
}
export default ck