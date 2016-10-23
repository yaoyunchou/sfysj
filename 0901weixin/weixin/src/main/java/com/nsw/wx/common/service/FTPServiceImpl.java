package com.nsw.wx.common.service;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.nsw.wx.common.model.Enterprise;
import com.nsw.wx.common.model.Site;
import com.nsw.wx.common.repository.SiteRepository;
import com.nsw.wx.common.util.Constants;
import com.nsw.wx.common.util.ContextUtil;
import com.nsw.wx.common.views.Message;

@Service("ftpcheckService")
@Transactional
public class FTPServiceImpl implements FTPService {

	Logger logger = Logger.getLogger(FTPServiceImpl.class);

	@Autowired
	private RedisTemplate redisTemplate;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private SiteRepository siteRepository;
	// FTP协议里面，规定文件名编码为iso-8859-1
	private static String SERVER_CHARSET = Constants.UPPER_ISO8859;
	private static String LOCAL_CHARSET = Constants.UPPER_UTF8;
	@Value("${tip.accountOverdue}")
	private String accountOverdue;

	@Override
	/**验证ftp连接
	 * 检查FTP服务是否正常
	 * 1.检查用户名密码正确
	 * 2.检查登陆权限
	 * 3.检查虚机磁盘大小
	 */
	public Message checkFtpUser(Map<String, Object> map) {

		FTPClient ftp = new FTPClient();
		Boolean flag = false;
		try {
			ftp = getFtpClient(map);
			int ReplyCode = ftp.getReplyCode();
			if (Constants.SUCCESSCODE == ReplyCode
					|| ReplyCode == Constants.COMMAND_OK) {
				flag = true;
				ftp.disconnect();
			} else {
				flag = false;
			}
		} catch (Exception e) {
			logger.error("ftp验证失败：", e);
			try {
				ftp.disconnect();
			} catch (Exception e1) {
				logger.error("ftp连接错误-关闭失败！", e1);
			}
			return new Message(flag, transferFtpErrorCode(ftp));
		}
		return new Message(flag, transferFtpErrorCode(ftp));
	}

	/**
	 * 将ftp errorcode转换成中文
	 * 
	 * @param ftp
	 * @return
	 */
	// 密码错误：530错误，Ftp 用户登入失败
	// 地址错误：
	// 无
	// 426错误 , 当上传文件大小为0的空文件时,系统会提示上传失败.实际该文件名在服务器已经创建了
	// 磁盘空间不足：452错误，原因: 当用户磁盘空间不够
	//
	private String transferFtpErrorCode(FTPClient ftp) {
		String hint = "ftp验证成功!";
		switch (ftp.getReplyCode()) {
		case 530:
			hint = accountOverdue;
			break;
		case 452:
			hint = "ftp磁盘空间不足！";
			break;
		case 200:
			hint = "命令执行正常结束";
			break;
		default:
			hint = accountOverdue;
		}
		return hint;
	}

	/**
	 * 获取ftp客户端连接
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public FTPClient getFtpClient(Map<String, Object> map) throws Exception {
		FTPClient ftp = new FTPClient();
		Site site = null;
		try {
			site = this.findSite((Long) map.get("enterprise_id"),
					Integer.parseInt(map.get("type") + ""));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (site != null) {
			String ftpUserName = site.getFtpUserName();
			String ftpPassword = site.getFtpPassword();
			String ftpIpAddress = site.getFtpIPAddress();
			int port = Integer.parseInt(site.getFtpPort());
			logger.info("----ftpUserName----" + ftpUserName
					+ "----ftpPassword---" + ftpPassword + ftpIpAddress
					+ "----" + port + "-----");
			ftp.setConnectTimeout(10000);
			ftp.connect(ftpIpAddress, port);
			if (FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
				if (ftp.login(ftpUserName, ftpPassword)) {
					ftp.setFileType(FTP.BINARY_FILE_TYPE);// 避免传递的图片失真
					ftp.setBufferSize(1024 * 1024 * 5);
					//ftp.setUseEPSVwithIPv4(true);
					// ftp.isRemoteVerificationEnabled();//=======================
					// 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（TypeConstant.UPPER_GBK）.
					if (FTPReply.isPositiveCompletion(ftp.sendCommand(
							"OPTS UTF8", "ON"))) {
						LOCAL_CHARSET = Constants.UPPER_UTF8;
					}
					ftp.setControlEncoding(LOCAL_CHARSET);
					// 设置被动模式ftpClient.setFileType(getTransforModule());
					ftp.enterLocalPassiveMode();
				}
			}
		}
		return ftp;
	}

	@Override
	public FTPClient connect(String path, String addr, int port,
			String username, String password) throws Exception {
		FTPClient ftp = new FTPClient();
		int reply;
		ftp.connect(addr, port);
		ftp.login(username, password);
		// 设置ftp缓冲区大小,不设置的话为1024
		ftp.setBufferSize(1024 * 1024 * 5);
		ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
		reply = ftp.getReplyCode();

		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
		}
		ftp.changeWorkingDirectory(path);
		return ftp;
	}

	/**
	 * 【功能描述：删除文件夹】 【功能详细描述：功能详细描述】
	 * 
	 * @param ftpClient
	 * @param ftpPath
	 * @return
	 * @throws IOException
	 */
	public void iterateDelete(FTPClient ftpClient, String ftpPath)
			throws IOException {
		if (ftpClient.changeWorkingDirectory(new String(ftpPath
				.getBytes(LOCAL_CHARSET), SERVER_CHARSET))) {
			FTPFile[] files = ftpClient.listFiles(new String(ftpPath
					.getBytes(LOCAL_CHARSET), SERVER_CHARSET));
			for (FTPFile f : files) {
				String path = ftpPath + File.separator + f.getName();
				if (f.isFile()) {
					logger.info("删除文件" + f.getName());
					// 是文件就删除文件
					ftpClient.deleteFile(new String(path
							.getBytes(LOCAL_CHARSET), SERVER_CHARSET));
				} else if (f.isDirectory()) {
					if (f.getName().endsWith(Constants.POINT)) {
						continue;
					}
					FTPFile[] flist = ftpClient.listFiles(new String(path
							.getBytes(LOCAL_CHARSET), SERVER_CHARSET));
					if (flist.length >= Constants.INT_ONE) {
						iterateDelete(ftpClient, path);
					}
					ftpClient.removeDirectory(new String(path
							.getBytes(LOCAL_CHARSET), SERVER_CHARSET));
				}
			}
		}
	}

	/**
	 * 删除除过temp之外的所有文件
	 * 
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 ***/
	public void deleteOldFile(FTPClient ftpClient)
			throws UnsupportedEncodingException, IOException {

		if (ftpClient.changeWorkingDirectory(new String("/"
				.getBytes(LOCAL_CHARSET), SERVER_CHARSET))) {
			FTPFile[] files = ftpClient.listFiles(new String("/"
					.getBytes(LOCAL_CHARSET), SERVER_CHARSET));
			for (FTPFile f : files) {
				String path = File.separator + f.getName();
				if (!f.getName().equals("temp")) {
					if (f.isFile()) {
						logger.info("删除文件" + f.getName());
						// 是文件就删除文件
						ftpClient.deleteFile(new String(path
								.getBytes(LOCAL_CHARSET), SERVER_CHARSET));
					} else if (f.isDirectory()) {
						if (f.getName().endsWith(Constants.POINT)) {
							continue;
						}
						FTPFile[] flist = ftpClient.listFiles(new String(path
								.getBytes(LOCAL_CHARSET), SERVER_CHARSET));
						if (flist.length >= Constants.INT_ONE) {
							iterateDelete(ftpClient, path);
						}
						ftpClient.removeDirectory(new String(path
								.getBytes(LOCAL_CHARSET), SERVER_CHARSET));
					}
				}
			}
		}

	}

	/**
	 * 上传文件到FTP
	 * 
	 * @throws IOException
	 * @ftp ： FTP链接
	 * @path ：上传文件夹路径
	 * @filename ：文件名称
	 * @in：上传文件流
	 */
	@Override
	public void ftpupload(FTPClient ftp, String path, String filename,
			BufferedInputStream in) throws Exception {
		createDirectory(ftp, path);
		try {
			ftp.changeWorkingDirectory(new String(path.getBytes(LOCAL_CHARSET),
					SERVER_CHARSET));
		} catch (Exception e) {
			e.printStackTrace();
		}
		ftp.setBufferSize(1024 * 1024 * 5);
		ftp.enterLocalPassiveMode();
		ftp.setConnectTimeout(1000 * 60);
		ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
		ftp.setSendBufferSize(1024 * 1024 * 5);
		ftp.setCharset(Charset.forName(LOCAL_CHARSET));
		logger.info("-->"
				+ new String(filename.getBytes(LOCAL_CHARSET), SERVER_CHARSET));
		if (in != null) {
			ftp.storeFile(new String(filename.getBytes(LOCAL_CHARSET),
					SERVER_CHARSET), in);
			if (in != null) {
				in.close();
			}
		}
	}

	/**
	 * 上传文件到FTP
	 * 
	 * @throws IOException
	 * @ftp ： FTP链接
	 * @from ：原来文件路径
	 * @to ：目标文件路径
	 * @isCover 是否覆盖拷贝
	 */
	@Override
	public void copy(FTPClient ftp, String from, String to, boolean isCover,
			String taskId) throws IOException {
		/*ftp.setBufferSize(1024 * 1024 * 5);
		FTPFile[] _file_s = ftp.listFiles(new String(from
				.getBytes(LOCAL_CHARSET), SERVER_CHARSET));
		for (FTPFile f : _file_s) {
			String path_f;
			String path_t;
			if (f.isDirectory()) {
				if (f.getName().endsWith(Constants.POINT)) {
					continue;
				}
				path_f = from + Constants.LEFT_SPRIT + f.getName();
				path_t = (null == to || to == Constants.LEFT_SPRIT || to == "") ? Constants.LEFT_SPRIT
						+ f.getName()
						: to + Constants.LEFT_SPRIT + f.getName();
				createDirectory(ftp, path_t);
				copy(ftp, path_f, path_t, isCover, taskId);
			} else {
				createDirectory(ftp, to);
				path_f = from + File.separator;
				path_t = to + File.separator;
				// 如果文件存在, 并且是覆盖拷贝
				if (isExistFile(ftp, path_t + f.getName())) {
					if (isCover) {
						logger.info("----文件名-----" + f.getName());
						PublishLog publishLog = (PublishLog) redisTemplate
								.opsForValue().get(taskId);
						List<String> list = publishLog.getFileName();
						if (list == null) {
							list = new ArrayList<String>();
						}
						list.add((list.size() + 1) + f.getName());
						publishLog.setFileName(list);
						;
						redisTemplate.opsForValue().set(taskId, publishLog);
						// 如果存在先删除
						ftp.deleteFile(new String((path_t + f.getName())
								.getBytes(LOCAL_CHARSET), SERVER_CHARSET));
					}
				}
				ftp.changeWorkingDirectory(from);
				fileCopy(
						ftp,
						new String((from + File.separator + f.getName())
								.getBytes(LOCAL_CHARSET), SERVER_CHARSET),
						new String((path_t + f.getName())
								.getBytes(LOCAL_CHARSET), SERVER_CHARSET));
			}

		}
*/
	}

	/**
	 * 复制文件
	 * 
	 * @param ftp
	 * @param from
	 * @param to
	 * @throws IOException
	 */
	public void fileCopy(FTPClient ftp, String from, String to)
			throws IOException {
		ftp.setBufferSize(1024 * 1024 * 5);
		ByteArrayOutputStream fos = new ByteArrayOutputStream();
		ftp.retrieveFile(from, fos);
		ByteArrayInputStream in = new ByteArrayInputStream(fos.toByteArray());
		ftp.storeFile(to, in);
		fos.close();
		in.close();
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param ftpClient
	 *            FTP链接句柄
	 * @param path
	 *            文件路径
	 * @return
	 */
	private boolean isExistFile(FTPClient ftpClient, String path) {
		boolean flag = false;
		File localfile = new File(path);
		if (!localfile.exists()) {
			flag = true;
		}
		return flag;
	}

	public double getDirSize(FTPClient ftpClient, String ftpPath)
			throws IOException {

		FTPFile[] files = ftpClient.listFiles(ftpPath);
		double size = 0;
		for (FTPFile f : files) {

			if (f.isFile()) {
				size += f.getSize();
			} else if (f.isDirectory()) {
				if (f.getName().endsWith(Constants.POINT)) {
					continue;
				}
				size += getDirSize(ftpClient,
						ftpPath + File.separator + f.getName());
			}
		}
		return size;
	}

	// 循环创建Ftp目录
	private void createDirectory(FTPClient ftpClient, String filePath) {
		String[] s = filePath.split(Constants.LEFT_SPRIT);
		String pathName = "";
		for (int i = 0; i < s.length; i++) {
			pathName = s[i] == null ? pathName : pathName
					+ Constants.LEFT_SPRIT + s[i];
			try {
				ftpClient.makeDirectory(new String(pathName
						.getBytes(LOCAL_CHARSET), SERVER_CHARSET));
			} catch (Exception e) {
				logger.error("ftp文件夹创建失败");
			}
		}
	}

	@Override
	public Site findSite(long enterpriseId, int type) {
		try {
			Enterprise e = new Enterprise();
			e.setId(enterpriseId);
			List<Site> sites = null;
			try {
				sites = siteRepository.findByEnterpriseAndType(e, type);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if (sites.size() > 0) {
				return sites.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error("Error", e);
			return null;
		}

	}

	public Site getSiteInfo(Long enterpriseId, String type) {
		int projType = 0;
		try {
			projType = Integer.parseInt(type);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		Site site = findSite(enterpriseId, projType);
		return site;
	}

	public Map getProjectInfo(Long userId) {
		
		Map user = null;
		Map projInfo = null;
		String querySql = "select * from user where id=" + userId;
		Query search = em.createNativeQuery(querySql);
		search.unwrap(SQLQuery.class).setResultTransformer(
				Transformers.ALIAS_TO_ENTITY_MAP);
		List rows = null;
		try {
			rows = search.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (rows != null && rows.size() > 0) {
			user = (Map) rows.get(0);
		}
		if (user != null && !user.isEmpty()
				&& user.get("enterprise_relation_id") != null) {
			querySql = "select * from project where projId = '"
					+ user.get("enterprise_relation_id") + "'";
			search = em.createNativeQuery(querySql);
			search.unwrap(SQLQuery.class).setResultTransformer(
					Transformers.ALIAS_TO_ENTITY_MAP);
			try {
				rows = search.getResultList();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (rows != null && rows.size() > 0) {
				projInfo = (Map) rows.get(0);
			}
		}
		return projInfo;
	}

	/**
	 * 项目右上角下拉框选择切换项目
	 **/
	public List<Map<String, Object>> listProjectInfo(Long enterpriseId) {
		/*Map map = null;
		String querySql = "select * from project where enterpriseId=?1";
		Query search = em.createNativeQuery(querySql);
		search.setParameter(1, enterpriseId);// site就是企业ID
		search.unwrap(SQLQuery.class).setResultTransformer(
				Transformers.ALIAS_TO_ENTITY_MAP);
		List rows = search.getResultList();
		if (rows != null && rows.size() > 0) {
			try {
				for (Object obj : rows) {
					((Map) obj).put("projName",EnumTypeUtil.ProjType.getProjName(((Map) obj).get("projectType") + ""));
					if (ContextUtil.getCurrentProject().equals(((Map) obj).get("projId"))) {
						((Map) obj).put("checked", true);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return rows;*/
		return null;
	}

	public Map getProjectType(String enterpriseId, String projectId) {
		Map map = null;
		String querySql = "select * from project where enterpriseId=?1 and projId =?2";
		Query search = em.createNativeQuery(querySql);
		search.setParameter(1, enterpriseId);
		search.setParameter(2, projectId);
		search.unwrap(SQLQuery.class).setResultTransformer(
				Transformers.ALIAS_TO_ENTITY_MAP);
		List rows = search.getResultList();
		if (rows != null && rows.size() > 0) {
			map = (Map) rows.get(0);
		}
		return map;
	}

	public void updateProject() {
		/*String enterpriseId = ContextUtil.getSite();
		String projectId = ContextUtil.getCurrentProject();
		String querySql = "update project set lastUpdTime=?1 where enterpriseId =?2 and projId=?3";
		Query query = em.createNativeQuery(querySql);
		query.setParameter(1, DateUtils.getCurrentTime());
		query.setParameter(2, enterpriseId);
		query.setParameter(3, projectId);
		query.executeUpdate();*/
	}

	// 命令=======================
	public void checkProject(FTPClient ftp, String tempPath, String projectPath) {

		try {
			FTPFile[] tempPathFiles = ftp.listFiles(tempPath);
			FTPFile[] projectFiles = ftp.listFiles(projectPath);
			// 比较 相同就删除
			for (int i = 0; i < tempPathFiles.length; i++) {
				for (int j = 0; j < projectFiles.length; j++) {
					if (tempPathFiles[i].getName().equalsIgnoreCase(
							projectFiles[j].getName())) {
						if (projectFiles[j].isFile()) {
							// ftp.deleteFile(new
							// String((projectPath+"/"+projectFiles[j].getName()).getBytes(LOCAL_CHARSET),
							// SERVER_CHARSET));
							ftp.sendCommand("CWD "
									+ new String(projectPath
											.getBytes(LOCAL_CHARSET),
											SERVER_CHARSET));
							ftp.sendCommand("PWD");
							ftp.sendCommand("DELE "
									+ new String(projectFiles[j].getName()
											.getBytes(LOCAL_CHARSET),
											SERVER_CHARSET));
						} else {
							iterateDeleteByCommond(
									ftp,
									new String(
											(projectPath + "/" + projectFiles[j]
													.getName())
													.getBytes(LOCAL_CHARSET),
											SERVER_CHARSET));
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void move(FTPClient ftp, String tempPath, String dirPath,
			String projectNames) throws Exception {
		/*logger.info(dirPath);
		int rep5 = ftp.sendCommand("CWD /");
		int rep4 = ftp.sendCommand("MKD " + dirPath);
		int rep1 = ftp.sendCommand("CWD " + tempPath + Constants.LEFT_SPRIT);
		if (rep1 == 250) {
			int rep2 = ftp.sendCommand("RNFR " + projectNames);
			if (350 == rep2) {
				int rep3 = ftp.sendCommand("RNTO " + dirPath
						+ Constants.LEFT_SPRIT + projectNames);

				int c = 0;
				while (rep3 != 250 && c < 3) {
					c++;
					rep3 = ftp.sendCommand("RNTO " + dirPath
							+ Constants.LEFT_SPRIT + projectNames);
				}
				if (rep3 != 250) {
					logger.info("RNTO " + dirPath + Constants.LEFT_SPRIT
							+ projectNames);
					throw new PublishException();
				}
			} else {
				logger.error("移出项目报错！错误命令为" + "RNFR " + projectNames);
				throw new PublishException();
			}
		} else {
			logger.error("进入文件夹报错！错误命令为" + "RNFR " + projectNames);
			throw new PublishException();
		}
*/
	}

	public void moveProjToRootPath(FTPClient ftp, String dirPath, String tempDir) {
		
/*
	//	String tempDir = "/temp"+Constants.LEFT_SPRIT+projId;
		createDirectory(ftp,dirPath);
		try {
			FTPFile[] proDirFiles = ftp.listFiles(tempDir);
			//temp/projId
			if (proDirFiles != null) {
				for (FTPFile ftpFile : proDirFiles) {
					if (!ftpFile.getName().equals(".")
							&& !ftpFile.getName().equals("..")) {

						int rep1 = ftp.sendCommand("CWD "
								+ new String(tempDir.getBytes(), "ISO-8859-1"));
						// temp/projId
						if (rep1 == 250) {
							ftp.sendCommand("PWD");
							int rep2 = ftp.sendCommand("RNFR "
									+ new String(ftpFile.getName().getBytes(),
											"ISO-8859-1"));
							if (350 == rep2) {
								int rep3 = ftp
										.sendCommand("RNTO "
												+ new String(
														(dirPath
																+ Constants.LEFT_SPRIT + ftpFile
																.getName())
																.getBytes(),
														"ISO-8859-1"));
								int c = 0;
								while (rep3 != 250 && c < 3) {
									c++;
									rep3 = ftp
											.sendCommand("RNTO "
													+ new String(
															(dirPath
																	+ Constants.LEFT_SPRIT + ftpFile
																	.getName())
																	.getBytes(),
															"ISO-8859-1"));
								}
								if (rep3 != 250) {
									logger.error("文件移出出错,出错文件为 RNTO " + dirPath
											+ Constants.LEFT_SPRIT
											+ ftpFile.getName());
									throw new PublishException();
								}
							} else {
								logger.error("文件标记出错,出错为RNFR "
										+ ftpFile.getName());
								throw new PublishException();
							}
						} else {
							// temp/projId
							logger.error("CWD " + tempDir);
							throw new PublishException();
						}
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/

	}

	public void iterateDeleteByCommond(FTPClient ftpClient, String ftpPath)
			throws IOException {

		ftpPath = ftpPath.replaceAll("//", "/");
		if (ftpClient.changeWorkingDirectory(new String(ftpPath
				.getBytes(LOCAL_CHARSET), SERVER_CHARSET))) {
			FTPFile[] files = ftpClient.listFiles(new String(ftpPath
					.getBytes(LOCAL_CHARSET), SERVER_CHARSET));
			if (files.length == 0) {
				ftpClient.removeDirectory(new String(ftpPath
						.getBytes(LOCAL_CHARSET), SERVER_CHARSET));
			}
			for (FTPFile f : files) {
				String path = ftpPath + File.separator + f.getName();
				if (f.isFile()) {
					// 是文件就删除文件
					ftpClient.sendCommand("CWD "
							+ new String(ftpPath.getBytes(LOCAL_CHARSET),
									SERVER_CHARSET));
					ftpClient.sendCommand("PWD");
					ftpClient.sendCommand("DELE "
							+ new String(f.getName().getBytes(LOCAL_CHARSET),
									SERVER_CHARSET));
					logger.info("删除文件"
							+ new String(f.getName().getBytes(LOCAL_CHARSET),
									SERVER_CHARSET));

				} else if (f.isDirectory()) {
					FTPFile[] flist = ftpClient.listFiles(new String(path
							.getBytes(LOCAL_CHARSET), SERVER_CHARSET));
					if (flist.length == 0) {
						ftpClient.removeDirectory(new String(path
								.getBytes(LOCAL_CHARSET), SERVER_CHARSET));
					} else {
						iterateDeleteByCommond(ftpClient,
								new String(path.getBytes(LOCAL_CHARSET),
										SERVER_CHARSET));
					}
					ftpClient.removeDirectory(new String(path
							.getBytes(LOCAL_CHARSET), SERVER_CHARSET));
				}
			}
			ftpClient.removeDirectory(new String(ftpPath
					.getBytes(LOCAL_CHARSET), SERVER_CHARSET));
		}
	}

	@Override
	public void updateAccessRecord(String projId, Long userId) {
		String querySql = "update user u set u.enterprise_relation_id='"+projId+"' where u.id ="+userId;//enterprise_relation_id保存的是企业项目ID
		Query query = em.createNativeQuery(querySql);
		query.executeUpdate();
	}

}
