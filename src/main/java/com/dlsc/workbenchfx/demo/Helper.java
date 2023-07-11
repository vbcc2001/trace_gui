package com.dlsc.workbenchfx.demo;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.alibaba.fastjson2.JSON;
import com.cg.core.module.RespEntity;
import com.cg.core.util.Log;
import com.cg.core.util.RestUtil;
import com.dlsc.workbenchfx.demo.global.GlobalConfig;

public class Helper{
    public Set<String> listFilesUsingJavaIO(String dir) {
        return Stream.of(new File(dir).listFiles())
            .filter(file -> !file.isDirectory())
            .map(File::getName)
            .collect(Collectors.toSet());
    }

    public static void main(String[] args) throws Exception {
        //Helper helper = new Helper();
        //String path = helper.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        //Log.info(path);
        //String postBody = """
        //{"files":"test.docx,test.py,test.zip",
        //    "dates":"2022-10-01 09:05:01,2022-10-17 10:01:59, 2022-10-20 21:21:21",
        //    "uri":"trace"}
        //    """;
        //Log.info(postBody);
        //String res = MainUtils.post(postBody, "http://helper.upupcat.com/patch_list");
        //Log.info(res);
        //test();
    }
    public static String getFileMd5(String filename) throws Exception{
        //Create checksum for this file
        File file = new File(filename);
        //Use MD5 algorithm
        MessageDigest md5Digest = MessageDigest.getInstance("MD5");
        //Get the checksum
        String checksum = getFileChecksum(md5Digest, file);
        //see checksum
        //Log.info(checksum);
        return checksum;
    }

    private static String getFileChecksum(MessageDigest digest, File file) throws IOException
    {
        //Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(file);

        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0; 

        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        };

        //close the stream; We don't need it now.
        fis.close();

        //Get the hash's bytes
        byte[] bytes = digest.digest();

        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        //return complete hash
        return sb.toString();
    }

    public static void updatePatch(boolean test) {

        Helper helper = new Helper();
        //String path = helper.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        String folder = MainUtils.getBasePath(Helper.class);       
        Log.debug(folder.toString());
        Set<String> files = helper.listFilesUsingJavaIO(folder.toString());
        List<String> fileList = List.copyOf(files);
        String patchs = "";
        String checksums = "";
        for (int i=0; i<fileList.size(); i++){
            String checksum = "000";
            try{
                checksum = getFileMd5(folder.toString()+"/"+fileList.get(i));
            }catch(Exception e){
                Log.error("",e);
            }
            if (i==fileList.size()-1){
                patchs +=  fileList.get(i);
                checksums += checksum;
                continue;
            }
            patchs +=  fileList.get(i) + ",";
            checksums += checksum + ",";
        }
        String postBody = """
        {"files":"%s",
            "checksums":"%s",
            "uri":"%s"}
            """;
        // postBody = String.format(postBody,patchs,checksums,"/trace/installer/"+testDir+"traceSys/app/");
        final String updateType=test?"test":"release";
        final String ver=TomlConfig.build().getVersion().getNumber().substring(0,2);
        postBody =  String.format(postBody,patchs,checksums,"/trace/installer/"+updateType+"/"+ver+"/front/traceSys/app/");       
        Log.debug(postBody);
        String res = MainUtils.post(postBody, "http://helper.upupcat.com/patch_list");
        String patch_list = JSON.parseObject(res).getJSONObject("patch_list").getString("new_files");
        String old_list = JSON.parseObject(res).getJSONObject("patch_list").getString("old_files");

        //先删除旧的jar
        Log.info("old_files:"+old_list);
        clearOldJar(old_list);
        if(patch_list==null || patch_list.isEmpty()) {
        	Log.info("当前版本已是最新版本，不需要更新");
        	return;
        } 
        String[] patchList = patch_list.split(",");
        Log.info("共"+patchList.length+"个文件需要更新");
        long t1=System.currentTimeMillis();
        for (int i=0; i<patchList.length; i++){
            String fileName = patchList[i].substring(patchList[i].lastIndexOf("/") + 1);
            if (!fileName.isEmpty()){
            	Log.info("更新: == "+patchList[i]+ "到 :=="+folder.toString()+"/"+fileName);
                helper.downloadFile(patchList[i],folder.toString()+"/"+fileName);
            }
        }
        long t2=System.currentTimeMillis();
        Log.info("更新完成,累计耗时"+(t2-t1)+"ms");
    }

    public static void updateServer(boolean test){
        final String updateType=test?"test":"release";
        String ver=GlobalConfig.getInstance().getServerVer().getServer();
        String uri = "/trace/installer/"+updateType+"/V3/backend/lib";
        Map<String,String> postBody =  new HashMap<String,String>();
        if(ver.startsWith("V")) {
        	ver=ver.substring(1);//TODO 不确定更新服务对version是否有格式要求，先兼容
        }
        postBody.put("server_version",ver);
        postBody.put("uri",uri);
        try{
            RespEntity res = new RestUtil("http://helper.upupcat.com").post("/patch_server",postBody);
            String url = res.getDataString();
            if(url==null || url.isEmpty()){
            Log.info("服务器不需要更新");
            }else{
            Log.info("服务器已经最新");
            }
        }catch(Exception e){
            Log.error("",e);
        }

    }

    public void downloadFile(String url, String path){
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
        		FileOutputStream fileOutputStream = new FileOutputStream(path);){
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            Log.error("下载异常",e);
        }
    }

    private static void clearOldJar(String oldFiles){
        //避免jar冲突,只保留最新版本
        String[] fileList = oldFiles.split(",");
        String folder = MainUtils.getBasePath(Helper.class);       
        for (int i=0; i<fileList.length; i++){
            if (!fileList[i].isEmpty()){
                String path = folder+"/"+fileList[i];
                //Log.info("path: "+path);
                File myObj = new File(path); 
                if (myObj.delete()) { 
                    Log.info("Deleted the file: " + myObj.getName());
                } else {
                    Log.info("Failed to delete the file.");
                }

            }
        }
    }


}
