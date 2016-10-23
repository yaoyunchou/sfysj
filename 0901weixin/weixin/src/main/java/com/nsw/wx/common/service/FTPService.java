package com.nsw.wx.common.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.net.ftp.FTPClient;

import com.nsw.wx.common.model.Site;
import com.nsw.wx.common.views.Message;

/**
 * FTP传输接口
 * 
 * @author Administrator
 *
 */
public interface FTPService {

    /**
     * 验证ftp连接
     * 
     * @param map
     * @return
     * @throws Exception
     */
    Message checkFtpUser(Map<String, Object> map) throws Exception;

    /**
     * @param path
     *            上传到ftp服务器哪个路径下
     * @param addr
     *            地址
     * @param port
     *            端口号
     * @param username
     *            用户名
     * @param password
     *            密码
     * @return
     * @throws Exception
     */
    FTPClient connect(String path, String addr, int port, String username,
            String password) throws Exception;

    /**
     * 【功能描述：删除文件夹】 【功能详细描述：功能详细描述】
     * 
     * @param ftpClient
     * @param ftpPath
     * @return
     * @throws IOException
     */
    void iterateDelete(FTPClient ftpClient, String ftpPath) throws IOException;

    /**
     * FTP流上传文件
     * 
     * @param ftp
     * @param path
     *            路径
     * @param filename
     *            文件名
     * @param in
     *            输入流
     */
    void ftpupload(FTPClient ftp, String path, String filename, BufferedInputStream in)
            throws Exception;

    /**
     * FTP拷贝文件
     * 
     * @param ftp
     * @param path
     *            路径
     * @param filename
     *            文件名
     * @param in
     *            输入流
     */
    void copy(FTPClient ftp, String sourPath, String dirPath, boolean isCover, String taskId)
            throws IOException;

    /**
     * 获取远端FTP目录大小
     * 
     * @param ftp
     * @param sourPath
     */
    double getDirSize(FTPClient ftpClient, String ftpPath) throws IOException;

    /**
     * 获取ftp连接
     * 
     * @param map
     * @return
     * @throws Exception
     */
    public FTPClient getFtpClient(Map<String, Object> map) throws Exception;

    /**
     * 通过企业ID和类型查找site
     * 
     * @param enterpriseId
     * @param type
     * @return
     */
    public Site findSite(long enterpriseId, int type);
    
    
    public Site getSiteInfo(Long userId, String type);
    
    
    public Map getProjectInfo(Long userId);
    
 
  	public Map getProjectType(String enterpriseId, String projectId);
  	
  	public void updateProject();
  	
  	public void deleteOldFile(FTPClient ftpClient) throws UnsupportedEncodingException, IOException; 
  	
  	
  	 /**
     * 
    * @Description: 项目检查，查看当前ftp上是否有相同目录或文件
    * @param @param ftp
    * @param @param tempPath
    * @param @param dirPath   
    * @return void  
    * @throws
     */
    void checkProject(FTPClient ftp, String tempPath, String dirPath);
    
    
    /**
	 * FTP移动文件
	 */
   void move(FTPClient ftp, String tempPath, String dirPath, String projectNames) throws Exception;

   
   /**
    * 
   * @Description: 将项目里层所有目录和文件 移动到根目录 
   * @param @param ftp
   * @param @param dirPath
   * @param @param projectNames   
   * @return void  
   * @throws
    */
   void moveProjToRootPath(FTPClient ftp,String dirPath,String projectNames);
   
   
   
   /**
    * 
   * @Description: 获取企业的项目
   * @param @param ftp
   * @param @param dirPath
   * @param @param projectNames   
   * @return void  
   * @throws
    */
   public List<Map<String,Object>> listProjectInfo(Long enterpriseId);
   
   
   /**
    * 
   * @Description: 本次切换的项目更新导入据库保存，供下次登录默认取出上次的项目
   * @param @param ftp
   * @param @param dirPath
   * @param @param projectNames   
   * @return void  
   * @throws
    */
  public void updateAccessRecord(String projId, Long id);

}
