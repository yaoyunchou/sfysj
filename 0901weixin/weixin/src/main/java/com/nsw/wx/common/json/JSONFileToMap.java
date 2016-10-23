package com.nsw.wx.common.json;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author 陈谨
 *
 * @author 2015年9月1日上午9:35:17
 */
public class JSONFileToMap {
	
	public static Map<String,Object> jsonToMap1(String jsonFileName){
		ObjectMapper mapper = new ObjectMapper();
		Map<String,Object> map =new HashMap<String,Object>();
		try {
			map = mapper.readValue(new File("../pccms/src/main/resources/"+jsonFileName+".json"), Map.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	
	public Map<String,Object> jsonToMap(String jsonFileName){
		ObjectMapper mapper = new ObjectMapper();
		Map<String,Object> map =new HashMap<String,Object>();
		try {
			
			
		//	System.out.println(this.getClass().getClassLoader().getResource("").getPath());
			map = mapper.readValue(new File("../pccms/src/test/resources/"+jsonFileName+".json"), Map.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	
	
	
	public Map<String,Object> jsonFileToMap(String jsonFileName){
		ObjectMapper mapper = new ObjectMapper();
		Map<String,Object> map =new HashMap<String,Object>();
		try {
			
			
		    String path = this.getClass().getClassLoader().getResource("").getPath();
			map = mapper.readValue(path+jsonFileName, Map.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	
	
	public List<Map<String,Object>> jsonFileToListMap(String jsonFileName){
		ObjectMapper mapper = new ObjectMapper();
		List<Map<String,Object>> map =new ArrayList<>();
		try {
			
		    String path = this.getClass().getClassLoader().getResource("").getPath();
			map = mapper.readValue(path+jsonFileName, List.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	
	public static void main(String[] args) {
		System.out.println();
	}
}
