package test;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class HttpFileTest {
	public static void sendGet(String url , Parameter param) 
	 {
	  StringBuffer p=new StringBuffer();
	  boolean first=true;
	  for(Map.Entry<String, String> pair : param.parameters.entrySet()){
	   if(first) first=false;
	   else p.append('&');
	   p.append(pair.getKey());
	   p.append('=');
	   p.append(pair.getValue());
	  }
	  try
	  {
	   String urlName = url + "?" + p.toString();
	   URL realUrl = new URL(urlName);
	   URLConnection conn = realUrl.openConnection();
	   conn.connect(); 
	   //获取所有响应头字段
	   Map<String,List<String>> map = conn.getHeaderFields();
	   for (String key : map.keySet())
	   {
	   System.out.println(key + "--->" + map.get(key));
	   }
	   
	   Scanner scanner=new Scanner(conn.getInputStream(),"gb2312");//以gb2312格式接收，避免中文乱码
	   StringBuffer str=new StringBuffer();
	   while(scanner.hasNextLine()) {
	           str.append(scanner.nextLine());
	           str.append("/n");
	   }
	   
	   System.out.println(str.toString());
	   
	  }catch(Exception e){
	  // log.error(e.getMessage());
	  }
	 }
	 
	 public static void sendPost(String url,Parameter param)
	 {
		 
		 param = new Parameter();
		 param.addParam("media_id", "b727VYJUEw-WW3TqWVGhzSutpyO0INcpf8TP9jPnEPY");
		 
		 
	  PrintWriter out = null;
	  String result = "";
	  try{
	   URL realUrl = new URL(url);
	   URLConnection conn = realUrl.openConnection();
	   //发送POST请求设置
	   conn.setDoOutput(true);
	   conn.setDoInput(true);
	   out = new PrintWriter(conn.getOutputStream());
	   
	   boolean first=true;
	   for(Map.Entry<String, String> pair : param.parameters.entrySet()){
	    if(first) first=false;
	    else out.print('&');
	    out.print(pair.getKey());
	    out.print('=');
	    out.print(pair.getValue());
	   }
	   out.flush();
	   
	   Scanner scanner=new Scanner(conn.getInputStream(),"gb2312");//以gb2312格式接收
	   StringBuffer str=new StringBuffer();
	   while(scanner.hasNextLine()) {
	           str.append(scanner.nextLine());
	           str.append("/n");
	   }
	   
	   System.out.println(str.toString());
	   
	   if (out != null){
	    out.close();
	   }
	  }catch(Exception e){
	   //log.error(e.getMessage());
	  }
	  System.out.println(result);
	 }
	 
	 public static boolean getBinaryFile(String url){
	  try{
	   URL u = new URL(url);
	   URLConnection uc = u.openConnection();
	   String contentType = uc.getContentType();
	   int contentLength = uc.getContentLength();
	 
	   if (contentType.startsWith("text/") || (contentLength == -1))
	   {
	  //  log.error("getBinaryFile-->not a binary file");
	    return false;
	   }
	 
	   InputStream raw = uc.getInputStream();
	   InputStream in = new BufferedInputStream(raw);
	   byte[] data = new byte[contentLength];
	   int bytesRead = 0;
	   int offset = 0;
	   while (offset < contentLength)
	   {
	    bytesRead = in.read(data,offset,data.length-offset);
	    if(bytesRead == -1) break;
	    offset +=bytesRead;
	   }
	   in.close();
	 
	   if (offset != contentLength)
	   {
	    return false;
	   }
	 
	   String fileName = u.getFile();
	   String filePath = "D://";
	   fileName = fileName.substring(fileName.lastIndexOf('/')+1);
	   FileOutputStream fout = new FileOutputStream(filePath+fileName);
	   fout.write(data);
	   fout.flush();
	   fout.close();
	  }catch(Exception e){
	 //  log.error(e.getMessage());
	   return false;
	  }
	  return true;
	 }
	 
	 
	 public Parameter getParameter(){
	  return new Parameter();
	 }
	 
	 
	 
	 public static class Parameter{
	  private Map<String,String> parameters=new HashMap<String,String>();
	  private  Parameter(){
	   parameters=new HashMap<String,String>();
	  }
	  public boolean addParam(String name,String value){
	   if(parameters!=null){
	    parameters.put(name, value);
	    return true;
	   }else{
	    return false;
	   }
	  }
	  public boolean delParam(String name){
	   if(parameters!=null){
	    if(parameters.remove(name)==null){
	     return false;
	    }else{
	     return true;
	    }
	   }else{
	    return false;
	   }
	  }
	 }
	 
	 public static void main(String[] args) {
		 sendPost("https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=Y_fhDabgeb3pjWzq6nf2m7Q3W_MfAIJxtFVxacvE-eMUC1hsKw5R5xyTO3DyTP21Ut425JI2kGnURJ_ToxkGFbBtw4MAH8kiRWSZWqezyI1zep23HaD3xZObOUvJcxOvSHGcAGDJYX", new Parameter());
	}
	 
	 
}
