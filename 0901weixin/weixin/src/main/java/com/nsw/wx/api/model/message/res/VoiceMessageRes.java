package com.nsw.wx.api.model.message.res;

import com.nsw.wx.api.common.MessageUtils;

public class VoiceMessageRes extends BaseMessageRes{
	private Voice Voice;

	
	public String getXmlStr(){
		MessageUtils m = new MessageUtils();
		return m.getXmlForBean(this);
	}


	public Voice getVoice() {
		return Voice;
	}


	public void setVoice(Voice voice) {
		Voice = voice;
	}
}
