
package com.dlsc.workbenchfx.demo.zplTemplate;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.cg.core.module.BizException;
import com.cg.core.util.Log;

/**
 * ClazzUtils
 * @author ZENG.XIAO.YAN
 * @version 1.0
 */
public class ClazzUtils {
    private static final String CLASS_SUFFIX = ".class";
    private static final String CLASS_FILE_PREFIX = File.separator + "classes"  + File.separator;
    private static final String PACKAGE_SEPARATOR = ".";




    /**
     * 查找包下的所有类的名字
     * @param packageName
     * @param showChildPackageFlag 是否需要显示子包内容
     * @return List集合，内容为类的全名
     */
    public static List<String> getClazzName(String packageName, boolean showChildPackageFlag ) {
        List<String> result = new ArrayList<>();
        String suffixPath = packageName.replaceAll("\\.", "/");
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            Enumeration<URL> urls = loader.getResources(suffixPath);
            while(urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if(url != null) {
                    String protocol = url.getProtocol();
                    if("file".equals(protocol)) {
                        String path = url.getPath();
                        Log.info(path);
                        result.addAll(getAllClassNameByFile(new File(path), showChildPackageFlag));
                    } else if("jar".equals(protocol)) {
                        JarFile jarFile = null;
                        try{
                            jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
                        } catch(Exception e){
                            Log.error("",e);
                        }
                        if(jarFile != null) {
                            result.addAll(getAllClassNameByJar(jarFile, packageName, showChildPackageFlag));
                        }
                    }
                }
            }
        } catch (IOException e) {
            Log.error("",e);
        }
        return result;
    }


    /**
     * 递归获取所有class文件的名字
     * @param file 
     * @param flag	是否需要迭代遍历
     * @return List
     */
    private static List<String> getAllClassNameByFile(File file, boolean flag) {
        List<String> result =  new ArrayList<>();
        if(!file.exists()) {
            return result;
        }
        if(file.isFile()) {
            String path = file.getPath();
            // 注意：这里替换文件分割符要用replace。因为replaceAll里面的参数是正则表达式,而windows环境中File.separator="\\"的,因此会有问题
            if(path.endsWith(CLASS_SUFFIX)) {
                path = path.replace(CLASS_SUFFIX, "");
                // 从"/classes/"后面开始截取
                String clazzName = path.substring(path.indexOf(CLASS_FILE_PREFIX) + CLASS_FILE_PREFIX.length())
                    .replace(File.separator, PACKAGE_SEPARATOR);
                if(-1 == clazzName.indexOf("$")) {
                    result.add(clazzName);
                }
            }
            return result;

        } else {
            File[] listFiles = file.listFiles();
            if(listFiles != null && listFiles.length > 0) {
                for (File f : listFiles) {
                    if(flag) {
                        result.addAll(getAllClassNameByFile(f, flag));
                    } else {
                        if(f.isFile()){
                            String path = f.getPath();
                            if(path.endsWith(CLASS_SUFFIX)) {
                                path = path.replace(CLASS_SUFFIX, "");
                                // 从"/classes/"后面开始截取
                                String clazzName = path.substring(path.indexOf(CLASS_FILE_PREFIX) + CLASS_FILE_PREFIX.length())
                                    .replace(File.separator, PACKAGE_SEPARATOR);
                                if(-1 == clazzName.indexOf("$")) {
                                    result.add(clazzName);
                                }
                            }
                        }
                    }
                }
            } 
            return result;
        }
    }


    /**
     * 递归获取jar所有class文件的名字
     * @param jarFile 
     * @param packageName 包名
     * @param flag	是否需要迭代遍历
     * @return List
     */
    private static List<String> getAllClassNameByJar(JarFile jarFile, String packageName, boolean flag) {
        List<String> result =  new ArrayList<>();
        Enumeration<JarEntry> entries = jarFile.entries();
        while(entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String name = jarEntry.getName();
            // 判断是不是class文件
            if(name.endsWith(CLASS_SUFFIX)) {
                name = name.replace(CLASS_SUFFIX, "").replace("/", ".");
                if(flag) {
                    // 如果要子包的文件,那么就只要开头相同且不是内部类就ok
                    if(name.startsWith(packageName) && -1 == name.indexOf("$")) {
                        result.add(name);
                    }
                } else {
                    // 如果不要子包的文件,那么就必须保证最后一个"."之前的字符串和包名一样且不是内部类
                    if(packageName.equals(name.substring(0, name.lastIndexOf("."))) && -1 == name.indexOf("$")) {
                        result.add(name);
                    }
                }
            }
        }
        return result;
    }
    private static final Map<String, PrintTemplateInfo> templates=new HashMap<>();
    public static List<String> getAllPrintTemplateName(String clientType){
    	if(templates.isEmpty()) {
    		loadAllTemplateInfo();
    	}
    	List<String> list=new ArrayList<>();
    	templates.forEach((name,info)->{
    		if(info.isSupport(clientType)) {
    			list.add(name);
    		}
    	});
    	return list;
    }
    /**
     * 
     * @param name 模板名称
     * @return 永不为null
     * @throws BizException 找不到打印模板
     */
    public static PrintTemplateInfo getPrintTemplateByName(String name) throws BizException {
    	if(templates.isEmpty()) {
    		loadAllTemplateInfo();
    	}
    	PrintTemplateInfo info=templates.get(name);
    	if(info==null)
    		throw new BizException("打印模板不存在:"+name);
    	return info;    	    		
    }
    /**
     * @return 目标客户所有打印模板
     */
    private static void loadAllTemplateInfo(){
    	Class<?> root=ClazzUtils.class;
    	try {
    		URI uri=root.getResource(root.getSimpleName()+".class").toURI();
    		if("jar".equals(uri.getScheme())) {//jar包需要重构文件沙盒
    			FileSystems.newFileSystem(uri, Map.of());//单例模式,只有首次执行需要创建FileSystem实例
    		}
    		final String packName=root.getPackageName();
    		Path rootPath=Paths.get(uri);
    		List<String> classNames=new ArrayList<>();
    		Files.list(rootPath.getParent()).forEach(p->{
                var name = p.getFileName().toString().replaceFirst("\\..*", "");
                //过滤掉非TP开头的文件及文件夹
                if(name.indexOf("TP")==0){
                    classNames.add(name);
                }
    		});    		
    		for(String clzName:classNames) {
    			Class<?> clz=Class.forName(packName+"."+clzName);
    			if(TPInterface.class.isAssignableFrom(clz)&&clz!=TPInterface.class) {//PrintService实现类
    				PrintTemplate pt=clz.getDeclaredAnnotation(PrintTemplate.class);    				
        			if(pt!=null) {
        				final TPInterface service=(TPInterface) clz.getDeclaredConstructor().newInstance();
        				String[] pts=pt.value();
        				if(pts!=null&&pts.length>0) {
        					final PrintTemplateInfo info=new PrintTemplateInfo(service, pt);
        					for(String tpName:pt.value()) {
            					PrintTemplateInfo previous = templates.put(tpName, info);
            					if(previous!=null) {
            						Log.warn("Find more than one provider with print template:"+tpName+",and auto select "+clz.getSimpleName()+"now!");
            					}
            				}
        				}
        			}
    			}
    			
    		}    	
    	}catch (URISyntaxException e) {
			Log.error("Fail to get print template list", e);
		}catch (Throwable t) {
			Log.error("Fail to get print template list", t);
		}    	
    }
    
    public static List<String> getAllTemplateList(){
    	ClazzUtils.class.getPackage().getAnnotations();
        List<String> tempList = new ArrayList<String>();
        List<String> list = ClazzUtils.getClazzName("com.dlsc.workbenchfx.demo.zplTemplate", false);
        for (String name:list){
            String[] data = name.split("\\.");
            //Log.info(data.length);
            String tmpName = data[5];
            if(tmpName.indexOf("TP")==0){
                tempList.add(tmpName);
            }
        }
        return tempList;
    }

    public static void main(String[] args) {
        List<String> list = getAllTemplateList();
        for (String string : list) {
            Log.info(string);
        }
    }
}
