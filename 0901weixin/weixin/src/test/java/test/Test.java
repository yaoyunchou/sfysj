package test;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Test {
	public static  void  main(String []args){
		String accessToken="XvCqKAmqf_SzbDyD88BJJDA_whezXMaqMXKx-trRPG--l7TTcVSAyRNFrmZwozZ6330JaVulHj-p7ZQnFO8JVSd4BQ2ufyu0yZYphw1TstgKFWfAIATOF";
		String url= "https://api.weixin.qq.com/cgi-bin/groups/members/batchupdate?access_token=_zbmfxX3-TOyuCL5cmOQUIBDyYJ9avJxiMKdUXAsCIN-znjTsavD-3qrab5-Wq2yyZ4snSdxzSePndB90YAMWCiMz_0sIT3rshKq17X23gswvrpxpIB4PZfP9O18-2GHFRBgAJAWIS";
		if(url.indexOf("access_token=") != -1){
			url = url.substring(0,url.indexOf("access_token="));
			System.out.print(url+"access_token="+accessToken);
		}
	}
}
