package com.api;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.cg.core.module.BizException;
import com.cg.core.module.RequestException;
import com.cg.core.util.Log;
import com.dlsc.workbenchfx.demo.ModelUtil;
import com.dlsc.workbenchfx.demo.api.CommonAPI;
import com.dlsc.workbenchfx.demo.api.CounterAPI;
import com.dlsc.workbenchfx.demo.api.PalletAPI;
import com.dlsc.workbenchfx.demo.api.ShippingAPI;
import com.dlsc.workbenchfx.demo.global.GlobalConfig;
import com.dlsc.workbenchfx.demo.modules.palletSN.model.PalletSN;
import com.dlsc.workbenchfx.demo.modules.productLaser.model.ProductLaser;
import com.dlsc.workbenchfx.demo.modules.productLaser.model.ProductLaserAPI;
import com.dlsc.workbenchfx.demo.AESUtil;
import com.dlsc.workbenchfx.demo.modules.query.model.ProductDetailInfo;
import com.dlsc.workbenchfx.demo.modules.shipping.model.Shipping;

public class APITest {
	@BeforeAll
	public static void beforeAll() {
//		GlobalConfig.getInstance().setServerUrl("http://localhost:8082");
		GlobalConfig.getInstance().setServerUrl("http://xiaomi.upupcat.com");
	}	
	public static void main(String[] args) throws IOException {		
		GlobalConfig.getInstance().setServerUrl("http://localhost:8082");
		for(int i=0;i<210;i++) {
			new Thread(()->{				
				try {
					//据说4核8G内存单进程调度线程数800-1000，超过这个并发数之后，将会花费巨大的时间在CPU调度上(未验证)
					//单接口并发测试,30个并发基本无阻塞(200ms以内),超过则需要线程排队,后端单个线程处理150ms左右
					//实际情况要更复杂，后端可能会在同时处理其他接口请求(默认只能同时处理20个请求),但是客户端的线程密集度不会像这里模拟的这么夸张
					//客户端本身并发不会太高，更多的是多个客户端同时发起请求最终汇聚到同一个服务端形成并发
					long t1=System.currentTimeMillis();
					List<Integer> list=CounterAPI.getNum("hello", 1, true, 10);//本地测试平均耗时193ms
					long t2=System.currentTimeMillis();
					System.out.println(Thread.currentThread().getName()+":"+list+";耗时:"+(t2-t1)+"ms");
				} catch (Throwable e) {
					System.out.println(Thread.currentThread().getName()+":"+e.getMessage());
				}
			}, "thread-"+i).start();
		}
		try {
			Thread.sleep(50000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testGetProductDetail() throws RequestException, BizException {
		String laserSql="from product_laser as s where 1  and s.used='是'  and s.weight is not null and s.weight<>''  and exists(select id from cartoon_box c where c.nick_name=s.cartoon_box_code and exists(select id from pallet p where p.nick_name=c.pallet_code))  and s.shipping_code is not null and s.shipping_code <>''";
		List<ProductDetailInfo> list=CommonAPI.getProductDetail(laserSql, 10, 0);
		Map<String, PropertyDescriptor> map=ModelUtil.getPropMap(ProductDetailInfo.class);
		List<String> selectFields=Arrays.asList(
                "SNCode",
                "SKU",
                "code69",
                "productName",
                "productType",
                "productColor",
                "productId",
                "ecologicalChain",
                "factoryCode",
                "createDate",
                "used",
                "weight",
                "cartoonBoxCode",
                "boxDate",
                "palletCode",
                "palletDate",
                "shippingCode",
                "shippingDate",                    
                "creator");
		for (ProductDetailInfo info : list) {
			StringBuilder sb=new StringBuilder("{");
			for (String fieldName : selectFields) {
				PropertyDescriptor p = map.get(fieldName);
				if (p == null) {
					Log.warn("通用查询缺少表格字段:" + fieldName);
					continue;
				}						
				Object rs = ModelUtil.readValue(p, info);
				sb.append(fieldName).append(":").append(rs);
			}
			sb.append("}");
			System.out.println(sb.toString());
		}
	}
	@Test
	public void testGetProductDetailCount() throws RequestException, BizException {
		String laserSql="from product_laser as s where 1  and s.used='是'  and s.weight is not null and s.weight<>''  and exists(select id from cartoon_box c where c.nick_name=s.cartoon_box_code and exists(select id from pallet p where p.nick_name=c.pallet_code))  and s.shipping_code is not null and s.shipping_code <>''";
		System.out.println("产品总数:"+CommonAPI.getProductDetailCount(laserSql));
	}
	@Test
	public void testGetLaser() throws RequestException {
		ProductLaser laser=ProductLaserAPI.getProductLaser("41769/AABBBF3QM00001");
		System.out.println(laser);
	}
	@Test
	public void testPalletInputBox() {//栈板装箱事务接口
		/*
		select sncode,nick_name,pallet_code from cartoon_box where sncode in('Test','MZXHBHR7008CN2CAG9000001','MZXHBHR7008CN2CAG9000003');
		select * from pallet where sncode='test2';
		delete from pallet where sncode='test2';
		update cartoon_box set pallet_code=null where sncode in('Test','MZXHBHR7008CN2CAG9000001','MZXHBHR7008CN2CAG9000003');
		 */		
		PalletSN pallet=new PalletSN();
		pallet.setSNCode("test2");
		pallet.setNickName("test");
		pallet.setCreateDate("2023/03/14");
		List<String> boxCodes=List.of("MZXHBHR7008CN2CAG9000001","MZXHBHR7008CN2CAG9000003","Test");
		try {
			boolean isTrue=PalletAPI.inputBox(pallet, boxCodes);
			System.out.println("插入结果:"+isTrue);
		} catch (BizException e) {
			System.err.println(e.getMessage());
		}
		
	}
	@Test
	public void testPalletGetByNickName() throws RequestException {//小米
		List<PalletSN> list=PalletAPI.getByNickName("PBHR7008CN2C01000001AG9");
		System.out.println("查询结果:"+list.size());
	}
	@Test
	public void testGetPallet() throws RequestException {
		String nickName="PBHR6046CN33A000001CCC";
		nickName="PBHR6326CN33A000001BBB";
		PalletSN probe = new PalletSN();
        probe.setNickName(nickName);
        List<PalletSN> list = PalletAPI.getPallet(probe);
        Log.info("list size:"+list.size());
        Shipping shipping=ShippingAPI.byPallet(list.get(0).getNickName());
        String shippingCode=shipping.getShippingCode();
        
        Log.info("ShippingCode:"+shipping.getShippingCode()+";isEmpty?"+shippingCode.isEmpty());
	}
    @Test
    public void testRSA(){

        String base64PrivateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKAUZV+tjiNBKhlBZbKBnzeugpdYPhh5PbHanjV0aQ+LF7vetPYhbTiCVqA3a+Chmge44+prlqd3qQCYra6OYIe7oPVq4mETa1c/7IuSlKJgxC5wMqYKxYydb1eULkrs5IvvtNddx+9O/JlyM5sTPosgFHOzr4WqkVtQ71IkR+HrAgMBAAECgYAkQLo8kteP0GAyXAcmCAkA2Tql/8wASuTX9ITD4lsws/VqDKO64hMUKyBnJGX/91kkypCDNF5oCsdxZSJgV8owViYWZPnbvEcNqLtqgs7nj1UHuX9S5yYIPGN/mHL6OJJ7sosOd6rqdpg6JRRkAKUV+tmN/7Gh0+GFXM+ug6mgwQJBAO9/+CWpCAVoGxCA+YsTMb82fTOmGYMkZOAfQsvIV2v6DC8eJrSa+c0yCOTa3tirlCkhBfB08f8U2iEPS+Gu3bECQQCrG7O0gYmFL2RX1O+37ovyyHTbst4s4xbLW4jLzbSoimL235lCdIC+fllEEP96wPAiqo6dzmdH8KsGmVozsVRbAkB0ME8AZjp/9Pt8TDXD5LHzo8mlruUdnCBcIo5TMoRG2+3hRe1dHPonNCjgbdZCoyqjsWOiPfnQ2Brigvs7J4xhAkBGRiZUKC92x7QKbqXVgN9xYuq7oIanIM0nz/wq190uq0dh5Qtow7hshC/dSK3kmIEHe8z++tpoLWvQVgM538apAkBoSNfaTkDZhFavuiVl6L8cWCoDcJBItip8wKQhXwHp0O3HLg10OEd14M58ooNfpgt+8D8/8/2OOFaR0HzA+2Dm";
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
          String s = new String(keySpec.getFormat());

        System.out.println("====+++++++++++++++++====================>"+s);
    }


    @Test
    void testAES() {
        System.out.println("aes encrypt: "+AESUtil.encrypt("helloworld"));
    }
}

