/**
 * Created by yao on 2016/10/29.
 */
"use strict";
var xml2js = require('xml2js');
var Promise = require('bluebird');
var _ = require('lodash');
exports.parseXMLAsync = function(xml){
	console.log('this is before xml2js'+xml);
	return new Promise(function (resolve,reject){
		xml2js.parseString(xml,{trim:true},function(err,content){
			if(err){
				reject(err);
			}else{
				resolve(content);
			}
		});
	});
};

var formatMessage = function formatMessage(result){
	var message = {};
	if(typeof  result === 'object'){
		var keys = Object.keys(result);
		_.forEach(keys,function(val){
			var item = result[val];
			var key = val;
			if(!(item instanceof Array)|| item.length === 0){
				return;
			}
			if(item.length ===1 ){
				var val = item[0];
				if(typeof  val === 'object'){
					message[key] = formatMessage(val);
				}else{
					message[key] = (val||'').trim();
				}
			}else{
				message[key] = [];
				_.forEach(item,function(val){
					message[key].push(formatMessage(item));
				});
			}
		});
	}
	return message;
};

exports.formatMessage = formatMessage;