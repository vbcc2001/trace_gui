package com.dlsc.workbenchfx.demo;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cg.core.util.Log;
import com.dlsc.workbenchfx.demo.zplTemplate.ClazzUtils;




public class Example{
    public static final String DEST = "./target/barcodes.pdf";
    private Map<Integer,String> miSymbol = new HashMap<Integer,String>(){
        {
            for(int i=0; i<10;i++){
                put(i,i+"");
            }
            put(10,"A");
            put(11,"B");
            put(12,"C");
            put(13,"D");
            put(14,"E");
            put(15,"F");
            put(16,"H");
            put(17,"J");
            put(18,"K");
            put(19,"M");
            put(20,"N");
            put(21,"P");
            put(22,"Q");
            put(23,"R");
            put(24,"S");
            put(25,"T");
            put(26,"U");
            put(27,"V");
            put(28,"W");
            put(29,"X");
            put(30,"Y");
        }
    };



   
    public static void main(String[] args) throws Exception {
        //testme();
        //myCache.put(1, "testCache1");
        //myCache.put(2, "testCache2");
        List<String> list = ClazzUtils.getAllTemplateList();
        for (String string : list) {
            Log.info(string);
        }

    }

}
