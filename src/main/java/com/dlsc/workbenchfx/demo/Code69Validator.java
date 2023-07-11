package com.dlsc.workbenchfx.demo;
import com.cg.core.util.Log;
import com.dlsc.formsfx.model.validators.CustomValidator;

public class Code69Validator extends CustomValidator<String> {

    private Code69Validator(String errorMessage) {
        super(input -> check(input), errorMessage);
    }

    public static Code69Validator verify(String errorMessage) {
        return new Code69Validator(errorMessage);
    }

    private static boolean check(String code69){
        if(code69.length()!=13){
            Log.info("长度不对!"+code69.length());
            return false;
        }
        int[] code69Value = new int[code69.length()];
        for(int i=0; i<code69.length(); i++){
            code69Value[i] = Integer.valueOf(""+code69.charAt(i));
        }
        if(code69Value[12] != MainUtils.code69Verify(code69Value)){
            Log.info("校验不对!"+code69Value[12]+"=="+MainUtils.code69Verify(code69Value));
            return false;
        }
        return true;
    }

}

