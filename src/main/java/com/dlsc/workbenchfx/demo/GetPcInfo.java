package com.dlsc.workbenchfx.demo;
 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import com.cg.core.util.Log;
 
public class GetPcInfo{
 
    public static void main(String[] args) throws IOException {
        // 获取CPU序列号
        getCpuInfo();
        
        // 获取硬盘序列号
        String sn = getSerialNumber("C");
        Log.info("硬盘序列号="+sn);
 
        // 获取主板序列号
        String cpuId = getMotherboardSN();
        Log.info("主板序列号"+cpuId);
    }
    
    // 获取CPU序列号
    @SuppressWarnings("resource")
    private static void getCpuInfo() throws IOException {
          Process process = Runtime.getRuntime().exec(
            new String[] { "wmic", "cpu", "get", "ProcessorId" });
          process.getOutputStream().close();
          Scanner sc = new Scanner(process.getInputStream());
          String property = sc.next();
          String serial = sc.next();
          Log.info(property + ": " + serial);
    }
    
    // 获取硬盘序列号
    // wmic diskdrive get SerialNumber
    public static String getSerialNumber(String drive) {
        String result = "";
        try {
            File file = File.createTempFile("realhowto",".vbs");
            file.deleteOnExit();
            FileWriter fw = new java.io.FileWriter(file);
            String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
                        +"Set colDrives = objFSO.Drives\n"
                        +"Set objDrive = colDrives.item(\"" + drive + "\")\n"
                        +"Wscript.Echo objDrive.SerialNumber";  // see note
            fw.write(vbs);
            fw.close();
            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
               result += line;
            }
            input.close();
        }
        catch(Exception e){
            Log.error("",e);
        }
        return result.trim();
    }
 
    // 获取主板序列号
    // 主板厂商：wmic BaseBoard get Manufacturer
    // 主板型号：wmic BaseBoard get Product
    public static String getMotherboardSN() {
        String result = "";
        try {
            File file = File.createTempFile("realhowto", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new java.io.FileWriter(file);
            String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
             + "Set colItems = objWMIService.ExecQuery _ \n"
             + "   (\"Select * from Win32_BaseBoard\") \n"
             + "For Each objItem in colItems \n"
             + "    Wscript.Echo objItem.SerialNumber \n"
             + "    exit for  ' do the first cpu only! \n" + "Next \n";
            fw.write(vbs);
            fw.close();
            Process p = Runtime.getRuntime().exec(
             "cscript //NoLogo " + file.getPath());
            BufferedReader input = new BufferedReader(new InputStreamReader(p
             .getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
            input.close();
        } catch (Exception e) {
            Log.error("",e);
        }
        
        return result.trim();
    }
}
