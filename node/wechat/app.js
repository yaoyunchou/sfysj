/**
 * Created by yao on 2016/10/23.
 */
"use strict";
var Koa = require('koa');
var sha1 = require('sha1');
var config = {
	wechat:{
		appID:'wx70ae2cf92f03f04a',
		appSecret:'11c20988538deadbc2df9ce8a0834140',
		token:'yaoyunchou'
	}
};
var app = new Koa();
app.use(function *(next){
	console.log(this.query);
	var token = config.wechat.token;
	var signature = this.query.signature;
	var timestamp = this.query.timestamp;
	var nonce = this.query.nonce;
	var echostr = this.query.echostr;
	var str = [token, timestamp, nonce].sort().join('');
	var sha = sha1(str);
	if(sha === signature){
		this.body = echostr + '';
	}else {
		this.body = 'wrong';
	}
});

app.listen(80);
console.log('Listening:80');