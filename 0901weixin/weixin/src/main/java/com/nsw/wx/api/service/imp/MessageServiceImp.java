package com.nsw.wx.api.service.imp;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.nsw.wx.api.model.News;
import com.nsw.wx.api.model.WeiXinApiUrl;
import com.nsw.wx.api.model.mass.ImageMass;
import com.nsw.wx.api.model.mass.ImageText;
import com.nsw.wx.api.model.mass.Mass;
import com.nsw.wx.api.model.mass.TextMass;
import com.nsw.wx.api.model.mass.VedioMass;
import com.nsw.wx.api.model.mass.VoiceMass;
import com.nsw.wx.api.service.MessageService;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.api.util.JSONHelper;

public class MessageServiceImp extends BasicServiceImp implements MessageService{

	@Override
	public JSONObject massImageTextByGroup(ImageText imageText,String accessToken,String appId,String openId) {
		
		if(openId!=null){//群发预览
			String url = WeiXinApiUrl.preViewMass_Post.replace(Constants.ACCESSTOKEN, accessToken);
			String jsonStr = JSONHelper.bean2json(imageText);
			JSONObject preView = JSONObject.fromObject(jsonStr);
			preView.put("touser", openId);
			if(preView.containsKey("filter")){
				preView.remove("filter");
			}
			return postMethodResult(url, preView.toString(),appId);
		}else{
			String url = WeiXinApiUrl.sendAll_Post.replace(Constants.ACCESSTOKEN, accessToken);
			String jsonStr = JSONHelper.bean2json(imageText);
			return postMethodResult(url, jsonStr,appId);
		}
	
	}

	@Override
	public JSONObject massMsgByGroup(String jsonStr, String accessToken, String appId,String openId) {
		if(openId!=null){
			String url = WeiXinApiUrl.preViewMass_Post.replace(Constants.ACCESSTOKEN, accessToken);
			JSONObject preView = JSONObject.fromObject(jsonStr);
			preView.put("touser", openId);
			if(preView.containsKey("filter")){
				preView.remove("filter");
			}
			return postMethodResult(url, preView.toString(),appId);
		}else{
			String url = WeiXinApiUrl.sendAll_Post.replace(Constants.ACCESSTOKEN, accessToken);
			return postMethodResult(url, jsonStr,appId);
		}

	}

	@Override
	public JSONObject massTextByGroup(TextMass textMass, String accessToken,String appId,String openId) {
		if(openId!=null){//群发预览
			String url = WeiXinApiUrl.preViewMass_Post.replace(Constants.ACCESSTOKEN, accessToken);
			String jsonStr = JSONHelper.bean2json(textMass);
			JSONObject preView = JSONObject.fromObject(jsonStr);
			preView.put("touser", openId);
			if(preView.containsKey("filter")){
				preView.remove("filter");
			}
			return postMethodResult(url, preView.toString(),appId);
		}else{
			String url = WeiXinApiUrl.sendAll_Post.replace(Constants.ACCESSTOKEN, accessToken);
			String jsonStr = JSONHelper.bean2json(textMass);
			return postMethodResult(url, jsonStr,appId);
		}
		
	
	}

	@Override
	public JSONObject massVoiceByGroup(VoiceMass voiceMass, String accessToken,String appId) {
		String url = WeiXinApiUrl.sendAll_Post.replace(Constants.ACCESSTOKEN, accessToken);
		String jsonStr = JSONHelper.bean2json(voiceMass);
		return postMethodResult(url, jsonStr,appId);
	}

	@Override
	public JSONObject massVedioByGroup(VedioMass vedioMass, String accessToken,String appId) {
		String url = WeiXinApiUrl.sendAll_Post.replace(Constants.ACCESSTOKEN, accessToken);
		String jsonStr = JSONHelper.bean2json(vedioMass);
		return postMethodResult(url, jsonStr,appId);
	}

	@Override
	public JSONObject getMassStatus(Mass mass, String accessToken,String appId) {
		String url = WeiXinApiUrl.queryMassState_Post.replace(Constants.ACCESSTOKEN, accessToken);
		String jsonStr = JSONHelper.bean2json(mass);
		return postMethodResult(url, jsonStr,appId);
	}

	@Override
	public JSONObject uploadNews(News news, String accessToken,String appId) {
		String url = WeiXinApiUrl.uploadNews_Post.replace(Constants.ACCESSTOKEN, accessToken);
		JSONObject json = JSONObject.fromObject(news);
		JSONArray arr = json.getJSONArray("articles");
		for(int i=0;i<arr.size();i++ ){
			JSONObject j = arr.getJSONObject(i);
			if(j.containsKey("url")){
				j.remove("url");
			}
			arr.set(i, j);
		}
		json.put("articles", arr);
//		String jsonStr = JSONHelper.bean2json(news);
		return postMethodResult(url, json.toString(),appId);
	}

	@Override
	public JSONObject massImageByGroup(ImageMass imageMass, String accessToken,String appId,String openId) {
		if(openId!=null){//群发预览
			String url = WeiXinApiUrl.preViewMass_Post.replace(Constants.ACCESSTOKEN, accessToken);
			String jsonStr = JSONHelper.bean2json(imageMass);
			JSONObject preView = JSONObject.fromObject(jsonStr);
			preView.put("touser", openId);
			if(preView.containsKey("filter")){
				preView.remove("filter");
			}
			return postMethodResult(url, preView.toString(),appId);
		}else{
			String url = WeiXinApiUrl.sendAll_Post.replace(Constants.ACCESSTOKEN, accessToken);
			String jsonStr = JSONHelper.bean2json(imageMass);
			return postMethodResult(url, jsonStr,appId);
		}
		
	
	}

	@Override
	public JSONObject delMass(String msg_id, String accessToken,String appId) {
		String url = WeiXinApiUrl.deleteMass_Post.replace(Constants.ACCESSTOKEN, accessToken);
		JSONObject json = new JSONObject();
		json.put("msg_id", msg_id);
		return postMethodResult(url, json.toString(),appId);
	}

}
