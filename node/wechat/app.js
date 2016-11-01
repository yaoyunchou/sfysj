/**
 * Created by yao on 2016/10/23.
 */
"use strict";
var Koa = require('koa');
var wechat = require('./wechat/g');
var path = require('path');
var util = require('./libs/util');
var wechat_file = path.join(__dirname,'./config/wechat.txt');

var config = {
	wechat: {
		appID: 'wx70ae2cf92f03f04a',
		appSecret: '11c20988538deadbc2df9ce8a0834140',
		token: 'yaoyunchou',
		getAccessToken:function(){
			return util.readFileAsync(wechat_file);
		},
		saveAccessToken:function(data){
			data = JSON.stringify(data);
			return util.writeFileAsync(wechat_file,data);
		}
	}
};
var app = new Koa();
app.use(wechat(config.wechat));

app.listen(80);

console.log('Listening:80');