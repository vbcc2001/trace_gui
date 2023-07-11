package com.dlsc.workbenchfx.demo.modules.user.model;

import java.util.ArrayList;
import java.util.List;

import com.cg.core.module.BizException;
import com.cg.core.module.RequestException;
import com.cg.core.util.Log;
import com.cg.core.util.RestUtil;
import com.dlsc.workbenchfx.demo.api.ServerAPI;

public class UserInfoAPI extends ServerAPI{
	
	public static List<UserInfo> getAll() throws RequestException{
		try {
			return new RestUtil(getServer()).post("/userInfo/getAll").getDataList(UserInfo.class);
		}catch (BizException e) {
			Log.warn("/userInfo/getAll",e);
			return new ArrayList<>();
		}
	}
	
	public static UserInfo addUser(UserInfo user) throws RequestException {
		try {
			return new RestUtil(getServer()).post("/userInfo/add",user).getData(UserInfo.class);
		}catch (BizException e) {
			Log.warn("/userInfo/add",e);
			return null;
		}
	}
	public static UserInfo updateUser(UserInfo user) throws RequestException, BizException {
		return new RestUtil(getServer()).post("/userInfo/updateById",user).getData(UserInfo.class);
	}
	
}
