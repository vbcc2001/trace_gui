package com.dlsc.workbenchfx.demo.global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cg.core.module.BizException;
import com.cg.core.module.RequestException;
import com.cg.core.module.SimpleElement;
import com.cg.core.util.Log;
import com.dlsc.workbenchfx.demo.Dictionary;
import com.dlsc.workbenchfx.demo.LocalCache;
import com.dlsc.workbenchfx.demo.ModelUtil;
import com.dlsc.workbenchfx.demo.TomlConfig;
import com.dlsc.workbenchfx.demo.TomlConfig.Version;
import com.dlsc.workbenchfx.demo.api.DictionaryAPI;
import com.dlsc.workbenchfx.demo.api.ServerAPI;
import com.dlsc.workbenchfx.demo.modules.productInfo.model.ProductInfo;
import com.dlsc.workbenchfx.demo.modules.productInfo.model.ProductInfoAPI;
import com.dlsc.workbenchfx.demo.modules.user.model.UserInfo;
import com.dlsc.workbenchfx.demo.modules.user.model.UserInfoAPI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
 * 全局配置项
 * @author LiangGuanHao
 *
 */
public class GlobalConfig {
	private final LocalCache localCache=LocalCache.getCache();
	private GlobalConfig() {
		String ct=localCache.getClientType();
		if(ct==null||ct.isBlank()||"old".equals(ct)||"S101".equals(ct)) {//旧文件缓存，忽略处理
			
		}else {//新文件缓存
			this.clientType=ct;
			setServerUrl(localCache.getServer(ct));			
		}
	}
	private String clientType;//客户端类型
	private String serverUrl;//当前正在使用的服务地址
	private Version serverVer;	
	private ObservableList<ProductInfo> productInfoList;//当前可用的产品模板
	private ObservableList<String> productNameList;
	/**
	 * 当前可用的产品颜色编码(目前素士专用,小米自定义颜色,不需要编码) name->code
	 */
	private Map<String, String> colorMap;
	private List<SimpleElement> colorEles;
	private List<String> colorNames;
 	private final Map<String, String> clientTypeMap=Map.of("xiaomi","小米","sushi","素士");
	
	private void loadAndCacheAllProductInfos() throws RequestException{
		try {
			List<ProductInfo> list=ProductInfoAPI.getAll(3);//连接超时自动重连最多3次
			productInfoList=FXCollections.observableArrayList(list);
			productNameList=FXCollections.observableArrayList(new ArrayList<>());
			flushProductNameList();
		} catch (RequestException e) {
			throw e;
		}
	}
	public void flushServerConfig(String ct,String serverUrl) throws RequestException {
		final String originCt=getClientType();
		final String originServerUrl=getServerUrl();
		final ObservableList<ProductInfo> originProductInfo=this.productInfoList;
		final Map<String, String> originColorMap=this.colorMap;
		final List<SimpleElement> originColorEles=this.colorEles;
		if(ct==null||ct.isBlank())ct=getClientType();
		if(serverUrl==null||serverUrl.isBlank())serverUrl=getServerUrl();
		try {
			setClientType(ct);
			setServerUrl(serverUrl);
			loadAndCacheAllProductInfos();
			loadAndCacheColorMap();
		}catch (RequestException e) {//异常回退
			setClientType(originCt);
			setServerUrl(originServerUrl);
			this.productInfoList=originProductInfo;
			this.colorMap=originColorMap;
			this.colorEles=originColorEles;
			this.flushProductNameList();
			throw e;
		}
	}
	/**
	 * 
	 * @return 所有产品模板(内存直接映射的单例,修改内存会影响到所有模块)
	 */
	public ObservableList<ProductInfo> getAllProductInfos(){
		return productInfoList;
	}
	/**
	 * 
	 * @return 在镭雕/彩盒等模块中下拉选择的产品显示名列表 
	 * @see #flushProductNameList()
	 */
	public ObservableList<String> getProductNames(){
		return productNameList;
	}
	public void addProductInfos(List<ProductInfo> list) {
		if(list==null)return;
		productInfoList.addAll(list);
		flushProductNameList();		
	}
	public void delProductInfo(Integer id) {
		if(id==null)return;		
		productInfoList.removeIf(info->{
			if(info.getId()==id) {				
				return true;
			}
			return false;
		});
		flushProductNameList();//重置productNameList
	}
	private void flushProductNameList() {
		productNameList.clear();		
		for(int i=0;i<productInfoList.size();i++) {
			final ProductInfo info=productInfoList.get(i);
			String productId=info.getProductId();
			if(productId!=null&&!productId.isBlank()) {
				productNameList.add(i+":\t"+productId+"\t"+info.getProductName());				
			}else {
				productNameList.add(i+":\t"+info.getProductName());
			}
		}
	}
	private void loadAndCacheColorMap() throws RequestException{
		try {
			List<Dictionary> list=DictionaryAPI.getByType("color", 3);			
			colorMap=new HashMap<>();
			colorEles=new ArrayList<>();
			colorNames=new ArrayList<>();
			colorMap.put("白色","WH");
			colorNames.add("白色");
			colorEles.add(new SimpleElement("WH", "白色"));
	        colorMap.put("棕色","BN");
	        colorNames.add("棕色");
	        colorEles.add(new SimpleElement("BN", "棕色"));
	        colorMap.put("绿色","GN");
	        colorNames.add("绿色");
	        colorEles.add(new SimpleElement("GN", "绿色"));
	        colorMap.put("黄色","YE");
	        colorNames.add("黄色");
	        colorEles.add(new SimpleElement("YE", "黄色"));
	        colorMap.put("灰色","GY");
	        colorNames.add("灰色");
	        colorEles.add(new SimpleElement("GY", "灰色"));
	        colorMap.put("粉红色","PK");
	        colorNames.add("粉红色");
	        colorEles.add(new SimpleElement("PK", "粉红色"));
	        colorMap.put("蓝色","BU");
	        colorNames.add("蓝色");
	        colorEles.add(new SimpleElement("BU", "蓝色"));
	        colorMap.put("红色","RD");
	        colorNames.add("红色");
	        colorEles.add(new SimpleElement("RD", "红色"));
	        colorMap.put("黑色","BK");
	        colorNames.add("黑色");
	        colorEles.add(new SimpleElement("BK", "黑色"));
	        colorMap.put("紫色","VT");
	        colorNames.add("紫色");
	        colorEles.add(new SimpleElement("VT", "紫色"));
	        colorMap.put("银色","SR");
	        colorNames.add("银色");
	        colorEles.add(new SimpleElement("SR", "银色"));
	        list.forEach(dic->{
	        	//由于历史原因,颜色编码字典在设计时code为中文,text为英文编码,因为颜色编码查询只有getByType,所以就不强制改回来了
	        	//产品入库时保存的是中文
	        	colorMap.put(dic.getCode(), dic.getText());//可覆盖默认颜色编码
	        	colorEles.add(new SimpleElement(dic.getText(), dic.getCode()));
	        	colorNames.add(dic.getCode());
	        });		       		       
		} catch (RequestException e) {
			throw e;
		}
	}
	public void putColor(String code,String name) {
		String lastCode=colorMap.put(name, code);
		if(lastCode==null) {//新增
			colorEles.add(new SimpleElement(code, name));
			colorNames.add(name);
		}else {//覆盖(同名颜色不同编码)
			colorEles.forEach(se->{
				if(se.getName().equals(name)) {
					se.setId(code);
				}
			});
		}
	}
	public String getColorCode(String colorText) {
		return colorMap.get(colorText);
	}
	public List<SimpleElement> getAllColorElements(){
		return colorEles;
	}
	public List<String> getAllColorNames(){
		return colorNames;
	}
	/**
	 * 用户列表
	 */
	private final Map<String, UserInfo> userMap=new HashMap<>();
	private final ObservableList<UserInfo> userList=FXCollections.observableArrayList(new ArrayList<>());
	private UserInfo currentUser;
	public String getClientType() {
		return clientType;
	}
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}	
	public String getServerUrl() {
		return serverUrl;
	}
	public void setServerUrl(String serverUrl) {
		if(!serverUrl.contains("://"))serverUrl="http://"+serverUrl;
		this.serverUrl = serverUrl;
	}
	public Version getServerVer() {
		return serverVer;
	}
	public void setServerVer(Version serverVer) {
		this.serverVer = serverVer;
	}	
	public ProductInfo getProductInfo(int id) {
		for(ProductInfo info:productInfoList) {
			if(info.getId()==id)
				return info;
		}
		return null;
	}	
	public Map<String, String> getColorMap() {
		return colorMap;
	}
	public void setColorMap(Map<String, String> colorMap) {
		this.colorMap = colorMap;
	}
	private static GlobalConfig inst;
	public final static GlobalConfig getInstance() {
		if(inst==null) {
			inst=new GlobalConfig();			
		}
		return inst;
	}
	public String getClientName(String clientType) {
		if(clientType==null)return null;
		return clientTypeMap.get(clientType);
	}
	public final List<SimpleElement> getAllClientType(){
		//可以扩展为在控制中心配置所有客户端类型    
		List<SimpleElement> list=new ArrayList<>();
		clientTypeMap.forEach((id,name)->{
			list.add(new SimpleElement(id, name));
		});
		return list;
	}
	/**
	 * 将初始配置持久化到本地缓存(clientType和serverUrl)
	 */
	public void persistInitConfig() {
		localCache.saveInitConfig(getClientType(), getServerUrl());
	}
	private boolean isUserInit=false;
	/**
	 * 查询并加载所有用户,项目登录/切换客户端版本/修改用户信息的时候自动刷新
	 * @throws RequestException 网络异常
	 */
	public void loadAllUser() throws RequestException {
		if(userMap.isEmpty()) {
			List<UserInfo> list=UserInfoAPI.getAll();			
			if(list.isEmpty()) {//没有账号，自动创建管理员账号
				UserInfo user = new UserInfo();
	            user.setUserName("admin");
	            user.setUserPasswd("88888888");
	            user.setColorBox("是");
	            user.setCartoonBox("是");
	            user.setPallet("是");
	            user.setShipping("是");
	            UserInfo admin=UserInfoAPI.addUser(user);
	            if(admin!=null) {
	            	Log.error("Fail to regist admin");
	            	userMap.put(admin.getUserName(), admin);
	            	userList.add(admin);
	            }	         
			}else {
				list.forEach(user->{
					userMap.put(user.getUserName(), user);
					userList.add(user);
				});
			}			
			isUserInit=true;
		}		
	}
	public void clearUserInfo() {
		userMap.clear();
		userList.clear();
		isUserInit=false;
	}
	/**
	 * 
	 * @param userName 用户名
	 * @param pwd 密码
	 * @return 从内存中拷贝的用户信息,永不为null
	 * @throws BizException 找不到用户或网络异常
	 */
	public UserInfo findUser(String userName,String pwd) throws BizException {
		if(!isUserInit) {//初始化用户信息
			try {
				loadAllUser();
			} catch (RequestException e) {
				throw ServerAPI.toBizException(getServerUrl(), e);
			}
		}
		if(userName==null||userName.isBlank()) {
			Log.debug("Bad user:"+userName);
			throw new BizException("用户名不能为空");
		}
		UserInfo info=userMap.get(userName);
		if(info==null)
			throw new BizException("用户/密码错误!");
		if(pwd==null||pwd.isBlank())
			throw new BizException("密码不能为空");
		if(!pwd.equals(info.getUserPasswd())) {
			Log.debug("Bad pwd:"+pwd+" for "+userName);
			throw new BizException("用户/密码错误!");
		}
		if(info.isFrozen())
			throw new BizException("用户已冻结");
		return ModelUtil.copyModel(info,new UserInfo());
	}
	/**
	 * 
	 * @return 所有用户列表(含已冻结用户)
	 */
	public ObservableList<UserInfo> getAllUsers(){
		return userList;
	}
	public UserInfo getUser(String userName) {
		return userMap.get(userName);
	}
	public void addUser(UserInfo user) {
		userMap.put(user.getUserName(), user);
		userList.add(user);
	}
	/**
	 * 
	 * @return 当前用户信息(拷贝版,修改无效)
	 */
	public UserInfo getCurrentUser() {
		return ModelUtil.copyModel(currentUser,new UserInfo());
	}
	public void setCurrentUser(UserInfo currentUser) {
		this.currentUser = currentUser;
	}
	/**
	 * 检查服务器参数是否合法并将合法参数保存到本地缓存
	 * @param ct 客户端类型
	 * @param serverUrl 服务地址
	 * @param 若参数合法是否替换全局缓存的服务器版本信息
	 * @return 若为null则表明正常,若为空则表明参数为空,否则返回异常信息
	 */
	public String checkClientTypeAndServer(String ct,String serverUrl,boolean changeServer) {
    	if(ct==null||ct.isBlank())return "";
    	if(serverUrl==null||serverUrl.isBlank())return "";
    	if(!serverUrl.contains("://"))serverUrl="http://"+serverUrl;
    	try {
			ServerAPI.checkClientType(serverUrl, ct);//检查客户端类型					
		} catch (BizException e) {
			return e.getMessage();
		}
    	try {//检查服务器版本是否兼容当前客户端版本
			Version serverVer=ServerAPI.getAndCheckServerVersion(serverUrl, TomlConfig.build().getVersion());
			if(changeServer)setServerVer(serverVer);						
		} catch (BizException e) {
			return e.getMessage();
		}
		return null;
    }
}
