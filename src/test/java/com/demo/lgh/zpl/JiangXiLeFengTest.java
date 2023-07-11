package com.demo.lgh.zpl;
import java.awt.GraphicsEnvironment;

import org.junit.jupiter.api.Test;

/**
 * 江西乐丰标签打印Demo
 * @author LiangGuanHao
 *
 */
public class JiangXiLeFengTest {
	public static void main(String[] args) {
		GraphicsEnvironment grapEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fontNameList = grapEnv.getAvailableFontFamilyNames();

		for(String fontName : fontNameList){

			System.out.println(fontName);

		}
		StringBuilder sb=new StringBuilder();
		Integer i=-10;
		sb.append("^MD").append(i);
		System.out.println(sb);
	}
	
	@Test
	public void pallet() {//栈板标签 
//		System.out.println(new JiangXiLeFeng().palletMark(true,true, 300,"4500XXXXXX","BHR6046CN","40088","20220606","米家便携式冲牙器 白色","960 PCS/PLT","89.1","PBHR6046CN26A000001AGW"));
	}
	
	@Test
	public void colorBox() {//彩盒标签 42*28mm 600dpi
//		System.out.println(new JiangXiLeFeng().xiaoMiColorBox(true, 300, "40088/AFAGWF2SE00001", "BHR6046CN","白色","2022.11","米家便携式冲牙器","6934177784828"));
	}
	
	@Test
	public void printBoxWuLiu() {//中箱物流标签 117*60mm 300dpi
		//V3改过接口参数了，懒得改测试代码了 by lgh on 2023/02/25
//		System.out.println(JiangXiLeFeng.xiaoMiBox(true,300,
//				"40088","粉色","BHR6047CN","4500XXXXXX","20221125", "深圳素士科技股份有限公司", 
//				"广东省深圳市宝安区", "中国大陆", "米家便携式冲牙器", "176*141*56mm", "64 箱/托盘,15 件/箱", "01/20221125", "12.00kg",
//				"11.28kg", "43.8*29.5*20.5", "0.026", "深圳", "南山",false));
	}
	@Test
	public void printWarehouse() {//目的仓标签  100*70mm 203dpi
//		System.out.println(new JiangXiLeFeng().warehouse(false,true, 203, "深圳","南山","40088","20220630","PBHR6046CN26A000001AGW"));
	}
	@Test
	public void printBoxUN() {//中箱唯一码标签 110*60mm 300dpi
		String[] productSNs=new String[15];
		for(int i=0;i<15;i++) {
			if(i<10) {
				productSNs[i]="40088/CCBBBF2YN0000"+(i+1);
			}else {
				productSNs[i]="40088/CCBBBF2YN000"+(i+1);
			}
		}
//		String zpl=new JiangXiLeFeng().uniqueBoxCode(false, true, 300, "MZXHBHR6046CN2B000005BBB", productSNs).toZPL();
//		System.out.println(zpl);
	}
}
