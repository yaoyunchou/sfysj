package com.nsw.wx.common.util;

/**
 * @author wukang
 * @Copyright: www.nsw88.com Inc. All rights reserved.
 * @date 2014骞�鏈�鏃�涓嬪崍11:32:25
 * @Description:生成树工具类
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class BuildTreeUtil {

	private static Logger logger = Logger.getLogger(BuildTreeUtil.class);

	public BuildTreeUtil() {

	}

	/***
	 * @Description: 调用主入口，构建数据结构
	 * @param @param list
	 * @param @throws JsonGenerationException
	 * @param @throws JsonMappingException
	 * @param @throws IOException
	 * @return String
	 */
	public List<Map<String, Object>> build(List<Map<String, Object>> list) {
		logger.info("原始list" + list.toString());

		List<Map<String, Object>> nodeList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : list) {
			if (map.get("path") != null && !"".equals(map.get("path"))) {
				StringBuffer buffer = new StringBuffer(map.get("path")
						.toString());
				buffer.deleteCharAt(buffer.length() - 1).deleteCharAt(0);
				String[] node = buffer.toString().split(",");
				String lastNode = node[node.length - 1];
				map.put("tempPath", lastNode);
			}
			map.put("_id", map.get("_id") + "");
			nodeList.add(map);
		}
		BuildTreeUtil buildTreeUtil = new BuildTreeUtil(nodeList);
		return buildTreeUtil.buildTree();
	}

	List<Map<String, Object>> nodes = new ArrayList<Map<String, Object>>();

	public BuildTreeUtil(List<Map<String, Object>> nodes) {
		super();
		this.nodes = nodes;
	}

	/**
	 * @Description: 构建树形结构
	 * @param @return
	 * @return List<Map<String,Object>>
	 * @throws
	 */
	public List<Map<String, Object>> buildTree() {
		List<Map<String, Object>> treeNodes = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> rootNodes = getRootNodes();
		for (Map<String, Object> rootNode : rootNodes) {
			buildChildNodes(rootNode);
			rootNode.remove("tempPath");
			treeNodes.add(rootNode);
		}
		return treeNodes;
	}

	/**
	 * @Description: 递归子节点
	 * @param @param node
	 * @return void
	 * @throws
	 */
	public void buildChildNodes(Map<String, Object> node) {
		List<Map<String, Object>> children = getChildNodes(node);
		if (!children.isEmpty()) {
			for (Map<String, Object> child : children) {
				child.remove("tempPath");
				buildChildNodes(child);
			}
			node.put("children", children);
		}
	}

	/**
	 * @Description: 获取父节点下所有的子节点
	 * @param @param node
	 * @param @return
	 * @return List<Map<String,Object>>
	 * @throws
	 */
	public List<Map<String, Object>> getChildNodes(Map<String, Object> node) {
		List<Map<String, Object>> childNodes = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> n : nodes) {
			if (node.get("_id").equals(n.get("tempPath"))) {
				childNodes.add(n);
			}
		}
		return childNodes;
	}

	/**
	 * @Description: 判断是否为根节点
	 * @param @param node
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public boolean rootNode(Map<String, Object> node) {
		boolean isRootNode = true;
		for (Map<String, Object> n : nodes) {
			if ((n.get("_id").equals(node.get("tempPath")))) {
				isRootNode = false;
				break;
			}
		}
		return isRootNode;
	}

	/**
	 * @Description: 获取集合中所有的根节点
	 * @param @return
	 * @return List<Map<String,Object>>
	 * @throws
	 */
	public List<Map<String, Object>> getRootNodes() {
		List<Map<String, Object>> rootNodes = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> n : nodes) {
			if (rootNode(n)) {
				rootNodes.add(n);
			}
		}
		return rootNodes;
	}
}
