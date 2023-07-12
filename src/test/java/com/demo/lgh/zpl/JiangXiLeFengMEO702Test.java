package com.demo.lgh.zpl;

/**
 * 江西乐丰标签打印Demo
 * @author LiangGuanHao
 *
 */
public class JiangXiLeFengMEO702Test {

	public static void main(String[] args) {
		JiangXiLeFengMEO702Test.uniqueBoxCodeTest();
//		JiangXiLeFengMEO702Test.palletMark();
	}
	/** 中箱唯一码标签 110*60 */
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
				"40088/SSAGBF2ZA00010",
				"40088/SSAGBF2ZA00005",
				"40088/SSAGBF2ZA00006",
				"40088/SSAGBF2ZA00007",
				"40088/SSAGBF2ZA00008",
				"40088/SSAGBF2ZA00009"
		};

//		var zpl = new JiangXiLeFengMEO702().uniqueBoxCode(false,600,boxCode,name,"白色",list);
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

//		var zpl =new JiangXiLeFengMEO702().palletMark(false,true, dpi,po,sku,id,"20220606",productName,productColor,
//				pcs,grossWeight,palletNumber);
//		System.out.println(zpl.toZPL());
	}
}
