/**
 * Created by yao on 2016/10/29.
 */
"use strict";
var sha1 = require('sha1');
var getRawBody = require('raw-body');
var Promise = require('bluebird');
var request = Promise.promisify(require('request'));
var util = require('./util');


var prefix = 'https://api.weixin.qq.com/cgi-bin/';
var api = {
	accessToken: prefix + 'token?grant_type=client_credential'
};

function Wechat(opts) {
	var that = this;
	this.appID = opts.appID;
	this.appSecret = opts.appSecret;
	this.getAccessToken = opts.getAccessToken;
	this.saveAccessToken = opts.saveAccessToken;

	this.getAccessToken().then(function (data) {
		try {
			data.JSON.parse(data);
		}
		catch (e) {
			return that.updateAccessToken();
		}
		if (this.isValidAccessToken(data)) {
			Promise.resolve(data);
		} else {
			return that.updateAccessToken();
		}
	}).then(function (data) {
		that.access_token = data.access_token;
		that.expires_in = data.expires_in;
		that.saveAccessToken(data);
	});
}
Wechat.prototype.isValidAccessToken = function (data) {
	if (!data || !data.access_token || !data.expires_in) {
		return false;
	}
	var access_token = data.access_token;
	var expires_in = data.expires_in;
	var now = new Date().getTime();
	if (now < expires_in) {
		return true;
	} else {
		return false;
	}
};
Wechat.prototype.updateAccessToken = function () {
	var appID = this.appID;
	var appSecret = this.appSecret;
	var url = api.accessToken + '&appid=' + appID + '&secret=' + appSecret;

	return new Promise(function (resolve, reject) {
		request({url: url, json: true}).then(function (response) {
			var data = response[1];
			var now = new Date().getTime();
			var expires_in = now + (data.expires_in - 20) * 1000;

			data.expires_in = expires_in;
			resolve(data);
		});
	});

};


module.exports = function (opts) {
	//var wechat = new Wechat(opts)
	return function *(next) {
		var that = this;
		console.log(this.query);
		var token = opts.token;
		var signature = this.query.signature;
		var timestamp = this.query.timestamp;
		var nonce = this.query.nonce;
		var echostr = this.query.echostr;
		var str = [token, timestamp, nonce].sort().join('');
		var sha = sha1(str);
		if (this.method === 'GET') {
			if (sha === signature) {
				this.body = echostr + '';
			} else {
				this.body = 'wrong';
			}
		}
		else if (this.method === 'POST') {
			if (sha !== signature) {
				this.body = 'wrong';
				//this.body = echostr + '';
				return false;
			}
			//else {
			//	this.body = 'wrong';
			//}
			var data = yield getRawBody(this.req, {
				length: this.length,
				limit: 'lmb',
				encoding: this.charset
			});
			var content = yield util.parseXMLAsync(data);
			console.log(content);
			var message = util.formatMessage(content.xml);
			console.log(message);

			if (message.MsgType === 'event'){
				if (message.Event === 'subscribe') {
					var now = new Date().getTime();
					that.status = 200;
					that.type = 'application/xml';
					that.body = ' <xml>' +
						'<ToUserName><![CDATA[' + message.FromUserName + ']]></ToUserName>' +
						'<FromUserName><![CDATA[' + message.ToUserName + ']]></FromUserName>' +
						'<CreateTime>' + now + '</CreateTime>' +
						'<MsgType><![CDATA[text]]></MsgType>' +
						'<Content><![CDATA[ 你好! 姚运筹]]></Content>' +
						'</xml>';
					console.log(that.body);
					return;
				}
			}
		}
	};
};
