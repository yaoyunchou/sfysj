package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import net.sf.json.JSONObject;

import com.nsw.wx.api.model.Media;
import com.nsw.wx.api.model.MediaListPara;
import com.nsw.wx.api.model.VideoInfo;
import com.nsw.wx.api.service.MaterialService;
import com.nsw.wx.api.service.imp.MaterialServiceImp;
import com.nsw.wx.api.util.AccessTokenHelper;
/**
 * 
* @author Liuzp
* @Copyright: www.nsw88.com Inc. All rights reserved. 
* @date 2015年10月23日 下午5:24:41
* @Description: 素材接口测试
 */
public class MaterialTest {
	public static void main(String[] args) throws FileNotFoundException {
		

		MaterialService mate = new MaterialServiceImp();
		
		//AccessTokenHelper.appid="wx030cc5e930d2ce1f";
		//AccessTokenHelper.appsecret="7d7d31d5c2f7e03aada4845fe7f4d376";
		//String accessToken = AccessTokenHelper.getAccessToken();
		
		File f = new File("d://111.jpg");
		
		FileInputStream fi = new FileInputStream(f);
		
		//上传永久文件  图片 语音 
	//	JSONObject object3 = mate.addMatter("Z24MGwJMia76VynZmmyQgYCWyIO4yLVMZu3_haf9yOKA8Bj2QczNXScXK6VYg4R1YzCufOajhYAyQb0nSMGkREcWlOVIj-XGfg9tQQdmg-b1Vy-L0c8vRswDjMvw0w57LVIiAKDBLY",fi,"123.jpg" ,null);
		//System.out.println(object3);
		
		JSONObject object4 = mate.uploadImageLogo("4pc6wWOwKamTQieGn5CJM0laM6jmWCGma1PzwCpXNXzQlONoj8dWSeHuOrACqC6bE7dwrbLDBsZngm_4cPCv59cM8uchLS9HOQj6spsTJXi414cAXBiUCHeN6oE6bkKRPPOcABAWWV",fi,"123.jpg",null );
		System.out.println(object4);
		
		//上传永久文件  视频   这里需要传入视频描述参数
		//VideoInfo info = new VideoInfo("视频标题", "视频描述");
		//JSONObject object4 = mate.addMatter(accessToken, "e://1.mp4",info);
		//System.out.println(object4);
		
		/**
		 * 成功返回示例
		 * 	{"media_id":"X_3Fh9bIR4fmhNShOPb-ZFqoeHGDeIrCtv9LTmzETVc","url":"https://mmbiz.qlogo.cn/mmbiz/WSrrI3icAJgNsJtevbj25oQ7DzartlssCiayXKKmQBQgJpZ3JMlAtU589ghGyvcznNWDacgFaqOGbUD4YAcRKTnw/0?wx_fmt=jpeg"}
			{"media_id":"X_3Fh9bIR4fmhNShOPb-ZCWcJj94g1NCaYcq_1MGetw"}

		 */
		
		/**
		 * 获取素材总数
		 */
		
		//FileInputStream in = new FileInputStream(new File("d:/111.jpg"));
		
		
	   //JSONObject json = mate.addMatter("Y_fhDabgeb3pjWzq6nf2m7Q3W_MfAIJxtFVxacvE-eMUC1hsKw5R5xyTO3DyTP21Ut425JI2kGnURJ_ToxkGFbBtw4MAH8kiRWSZWqezyI1zep23HaD3xZObOUvJcxOvSHGcAGDJYX", in,"232.jpg","image",null);
	 //  System.out.println(json);
	   //返回 ：{"voice_count":0,"video_count":2,"image_count":7,"news_count":0}
	   
	   /**
	    * 获取素材列表
	    */
	   //获取图片类型的素材 从0取起 总共最多取10个
	   //MediaListPara  paraList = new MediaListPara("image","0", "10");
	   
	  // JSONObject json8 = mate.getMatterList(accessToken, paraList);
	   //ystem.out.println(json8);
		
	}
	
	
}
