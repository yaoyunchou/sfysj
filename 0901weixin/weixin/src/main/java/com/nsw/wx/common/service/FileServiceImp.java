package com.nsw.wx.common.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.csource.manager.FileOperationService;
import org.csource.manager.FileOperationServiceImpl;
import org.springframework.stereotype.Service;

import com.nsw.wx.common.util.ImageFactory;

@Service("fileService")
public class FileServiceImp implements FileService{
	
	private FileOperationService fileOperationService = new FileOperationServiceImpl();

	@Override
	public InputStream getStreamByUrl(String destUrl) {
		// TODO Auto-generated method stub
		 URL url = null;  
	        InputStream is =null;  
	        try {  
	            url = new URL(destUrl);  
	        } catch (MalformedURLException e) {  
	            e.printStackTrace();  
	        }  
	        try {  
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();//利用HttpURLConnection对象,我们可以从网络中获取网页数据.  
	            conn.setDoInput(true);  
	            conn.connect();  
	            is = conn.getInputStream(); //得到网络返回的输入流  
	            if(is!=null){
	                int fileSize = is.available();
					ImageFactory imageFactory = new ImageFactory();
					while(fileSize>1048576){
						is = imageFactory.imgCompressToScale(is, 0.5f, "jpg");
						fileSize = is.available();
					}
	            }
	        } catch (IOException e) {  
	            e.printStackTrace();  
	            return null;
	        }  
	        return is;  
	}

	@Override
	public String insertImageToDb(InputStream inputStream,String fileType) {
		// TODO Auto-generated method stub
		try {
			if(inputStream != null && fileType != null){
				String fileId = fileOperationService.uploadFile(IOUtils.toByteArray(inputStream), fileType, null);//保存到fastdfs
				if(fileId != null ){
					return fileId;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
