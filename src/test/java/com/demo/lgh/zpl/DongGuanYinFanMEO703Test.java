package com.demo.lgh.zpl;

/**
 * 江西乐丰标签打印Demo
 * @author LiangGuanHao
 *
 */
public class DongGuanYinFanMEO703Test {

	public static void main(String[] args) {
		//DongGuanYinFanMEO703Test.uniqueBoxCodeTest();
		//DongGuanYinFanMEO703Test.palletMark();
		//DongGuanYinFanMEO703Test.xiaoMiColorBox();
		//DongGuanYinFanMEO703Test.warehouse();
		DongGuanYinFanMEO703Test.xiaoMiBox();
	}
	//60*117
	public static void xiaoMiBox(){

		String productId="40088";
		String color="白色";
		String SKU="BHR6046CN";
		String PO="45000000";
		String createDate="20220606";
		String supplier="1223";
		String prodArea="广东省深圳市宝安区";
		String salesRegion="中国大陆";
		String productName="米家便携式冲牙器F300";
		String productSpec="176*141*56mm";
		String pkgSpec="64箱/托盘，15件/箱";
		String inspection="64箱/托盘，15件/箱";
		String boxGrossWeight="100";
		String boxNetWeight="85";
		String boxSize="43.8*29.5*20.5";
		String volume="300";
		String dstCity="深圳";
		String dstArea="宝安";
		var dpi = 300;
		boolean isPortrait= false;

//		var zpl =new DongGuanYinFanMEO703().xiaoMiBox(isPortrait,true, 300, productId,  color,  SKU,  PO,
//				 createDate,  supplier,  prodArea,  salesRegion,  productName,  productSpec,  pkgSpec,
//				 inspection,  boxGrossWeight,  boxNetWeight,  boxSize,  volume,  dstCity,  dstArea );
//		System.out.println(zpl.toZPL());
	}

	public static void warehouse(){
//		var zpl =new DongGuanYinFanMEO703().warehouse(false, true, 300, "深圳", "南山", "40089", "PBHR6046CN26A000001AGW");
//		System.out.println(zpl.toZPL());
	}

	public static void xiaoMiColorBox() {

		//彩盒
		var dpi = 300;
		var SNCode = "40088/SSAGBF2ZA00001";
		var sku = "BHR6046CN";
		var color =  "白色";
		var createDate = "20220606";
		var productName = "米家便携式冲牙器F300";
		var code69 = "6934177784828";
//		var zpl =new DongGuanYinFanMEO703().xiaoMiColorBox(true, dpi,SNCode,sku,color,createDate,productName, code69);
//		System.out.println(zpl.toZPL());
	}



	public static void uniqueBoxCodeTest() {
		var boxCode = "MZXHBHR7008CN2B000001AG9";
		var name = "小米电动冲牙器F330";
		var list = new String[]{
				"40088/SSAGBF2ZA00001",
				"40088/SSAGBF2ZA00002",
				"40088/SSAGBF2ZA00003",
				"40088/SSAGBF2ZA00004",
				"40088/SSAGBF2ZA00005",
				"40088/SSAGBF2ZA00006",
				"40088/SSAGBF2ZA00007",
				"40088/SSAGBF2ZA00008",
				"40088/SSAGBF2ZA00009",
				"40088/SSAGBF2ZA00010"
		};

//		var zpl = new DongGuanYinFanMEO703().uniqueBoxCode(false,600,boxCode,name,"白色",list);
//		System.out.println(zpl.toZPL());
	}
	public static void palletMark() {//栈板标签
		var dpi = 300;
		var po = "4500XXXXXX";
		var sku = "BHR6046CN";
		var id = "40088";
		var  createDate = "20220606";
		var  productName = "米家便携式冲牙器";
		var  productColor = "白色";
		var  pcs = "960 PCS/PLT";
		var  grossWeight = "89.1";
		var  palletNumber = "PBHR6046CN26A000001AGW";
		var boxSNs = new String[]{
//				"40088/SSAGBF2ZA00001","40088/SSAGBF2ZA00001","40088/SSAGBF2ZA00001","40088/SSAGBF2ZA00001","40088/SSAGBF2ZA00001","40088/SSAGBF2ZA00001",
//				"40088/SSAGBF2ZA00002","40088/SSAGBF2ZA00002","40088/SSAGBF2ZA00002","40088/SSAGBF2ZA00002","40088/SSAGBF2ZA00002","40088/SSAGBF2ZA00002",
//				"40088/SSAGBF2ZA00003","40088/SSAGBF2ZA00003","40088/SSAGBF2ZA00003","40088/SSAGBF2ZA00003","40088/SSAGBF2ZA00003","40088/SSAGBF2ZA00003",
//				"40088/SSAGBF2ZA00004","40088/SSAGBF2ZA00004","40088/SSAGBF2ZA00004","40088/SSAGBF2ZA00004","40088/SSAGBF2ZA00004","40088/SSAGBF2ZA00004",
//				"40088/SSAGBF2ZA00005","40088/SSAGBF2ZA00005","40088/SSAGBF2ZA00005","40088/SSAGBF2ZA00005","40088/SSAGBF2ZA00005","40088/SSAGBF2ZA00005",
//				"40088/SSAGBF2ZA00006","40088/SSAGBF2ZA00006","40088/SSAGBF2ZA00006","40088/SSAGBF2ZA00006","40088/SSAGBF2ZA00006","40088/SSAGBF2ZA00006",
//				"40088/SSAGBF2ZA00007","40088/SSAGBF2ZA00007","40088/SSAGBF2ZA00007","40088/SSAGBF2ZA00007","40088/SSAGBF2ZA00007","40088/SSAGBF2ZA00007",
//				"40088/SSAGBF2ZA00008","40088/SSAGBF2ZA00008","40088/SSAGBF2ZA00008","40088/SSAGBF2ZA00008","40088/SSAGBF2ZA00008","40088/SSAGBF2ZA00008",
//				"40088/SSAGBF2ZA00009","40088/SSAGBF2ZA00009","40088/SSAGBF2ZA00009","40088/SSAGBF2ZA00009","40088/SSAGBF2ZA00009","40088/SSAGBF2ZA00009",
				"40088/SSAGBF2ZA00010","40088/SSAGBF2ZA00010","40088/SSAGBF2ZA00010","40088/SSAGBF2ZA00010","40088/SSAGBF2ZA00010","40088/SSAGBF2ZA00010"
		};
		//测试接口升级了，懒得改测试代码了
		
//		var zpl =DongGuanYinFanMEO703.palletMark(false,true, dpi,po,sku,id,"20220606",productName,productColor,
//				pcs,grossWeight,palletNumber,boxSNs, 17, 4);
//
//		System.out.println(123);
//		System.out.println(zpl.toZPL());
	}
}
