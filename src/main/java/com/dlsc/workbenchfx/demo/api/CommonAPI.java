package com.dlsc.workbenchfx.demo.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cg.core.module.BizException;
import com.cg.core.module.RequestException;
import com.cg.core.util.Log;
import com.cg.core.util.RestUtil;
import com.dlsc.workbenchfx.demo.modules.query.model.ProductDetailInfo;

public class CommonAPI extends ServerAPI {
	/**
	 * 
	 * @param laserSql     laser查询条件
	 * @param isBoxUsed    laser是否已装箱
	 * @param isPalletUsed laser是否已装栈
	 * @param isShipping   laser是否已出货
	 * @param pageSize     分页大小,从1开始
	 * @param offset       分页起始索引,从0开始
	 * @return 产品详情
	 * @throws BizException 业务异常，包含网络异常
	 */
	public static List<ProductDetailInfo> getProductDetail(String laserSql, int pageSize, int offset)
			throws BizException {
		return getProductDetail(laserSql, pageSize, offset, 2000);
	}

	/**
	 * 
	 * @param laserSql     laser查询条件
	 * @param isBoxUsed    laser是否已装箱
	 * @param isPalletUsed laser是否已装栈
	 * @param isShipping   laser是否已出货
	 * @param pageSize     分页大小,从1开始
	 * @param offset       分页起始索引,从0开始
	 * @param respTimeout  响应超时时间(ms),实际查询包含了2个请求，极端情况下响应完成耗时接近respTimeout*2
	 * @return 产品详情
	 * @throws BizException 业务异常，包含网络异常
	 */
	public static List<ProductDetailInfo> getProductDetail(String laserSql, int pageSize, int offset, int respTimeout)
			throws BizException {
		try {
			String sql = "select * " + laserSql + " limit " + pageSize + " offset " + offset;
			final String api = "/common/query";
			// 1.产品信息
			Log.info("sql:" + sql);
			List<ProductDetailInfo> list = new RestUtil(getServer()).setReadTimeout(respTimeout)
					.post(api, Map.of("sql", sql)).getDataList(ProductDetailInfo.class, true);
			// 2.关联中箱信息
			final Set<String> boxCodeSet = new HashSet<>();
			for (ProductDetailInfo p : list) {
				String boxCode = p.getCartoonBoxCode();
				if (boxCode != null && !boxCode.isBlank()) {
					boxCodeSet.add(boxCode);
				}
			}
			if (!boxCodeSet.isEmpty()) {// 包含装箱的数据
				String bcStr = "'" + String.join("','", boxCodeSet) + "'";
				String boxAndLaserSql = "select c.create_date as boxDate,c.nick_name as cartoonBoxCode,p.nick_name as palletCode,p.create_date as palletDate from cartoon_box c left join pallet p on p.nick_name=c.pallet_code where c.nick_name in("
						+ bcStr + ")";
				Log.info("sql:" + boxAndLaserSql);
				List<ProductDetailInfo> anotherList = new RestUtil(getServer()).setReadTimeout(respTimeout)
						.post(api, Map.of("sql", boxAndLaserSql)).getDataList(ProductDetailInfo.class, true);
				Map<String, ProductDetailInfo> map = new HashMap<>();// boxCode->info
				for (ProductDetailInfo p : anotherList) {
					map.put(p.getCartoonBoxCode(), p);
				}
				for (ProductDetailInfo p : list) {
					String boxCode = p.getCartoonBoxCode();
					if (boxCode == null || boxCode.isBlank())
						continue;
					ProductDetailInfo ap = map.get(boxCode);
					if (ap == null)
						continue;// 未装箱
					p.setBoxDate(ap.getBoxDate());
					p.setPalletCode(ap.getPalletCode());
					p.setPalletDate(ap.getPalletDate());
				}
			}
			return list;
		} catch (RequestException e) {
			throw toBizException(getServer(), e);
		}
	}

	/**
	 * 
	 * @param laserSql laser查询条件
	 * @return laser总数
	 * @throws BizException 业务异常，包含网络异常
	 */
	public static Integer getProductDetailCount(String laserSql) throws BizException {
		return getProductDetailCount(laserSql, 2000);
	}

	/**
	 * 
	 * @param laserSql    laser查询条件
	 * @param respTimeout 响应超时时间
	 * @return laser总数
	 * @throws BizException 业务异常，包含网络异常
	 */
	public static Integer getProductDetailCount(String laserSql, int respTimeout) throws BizException {
		// from product_laser as s where 1 and s.used='是' and s.weight is not null and
		// s.weight<>'' and s.cartoon_box_code is not null and s.cartoon_box_code <>''
		// and s.cartoon_box_code in(select nick_name from cartoon_box where pallet_code
		// is not null and pallet_code<>'') and s.shipping_code is not null and
		// s.shipping_code <>''
		final String api = "/common/get";
		String sql = "select count(*) as C " + laserSql;
		Log.info("sql:" + sql);
		try {
			return new RestUtil(getServer()).setReadTimeout(respTimeout).post(api, Map.of("sql", sql))
					.getDataMap(Integer.class).get("C");
		} catch (RequestException e) {
			throw toBizException(getServer(), e);
		}
	}
}
