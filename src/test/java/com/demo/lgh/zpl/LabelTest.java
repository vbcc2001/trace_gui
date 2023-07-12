package com.demo.lgh.zpl;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class LabelTest {
	
	@SuppressWarnings("unused")
	@Test
	public void printM13Pallet() throws IOException {//小米M13栈板标签打样(东莞因范)
		//标签尺寸117*60mm,可能为纵向打印
		boolean setTransPose=true;
		boolean isMock=false;
		int dpi=300;
		String PO="4500XXXXXX";
		String SKU="BHR7009CN";
		String id="45390";
		String createDate="20230111";
		String productName="米家智能颈部按摩仪";
		String pcs="216PCS/PLT";
		String grossWeight="XXXkg";
		String palletNumber="PBHR7009CN31A000001AG9";
		String[] boxSNs=new String[] {
			"45390/AFAG9F3NS00001",
			"45390/AFAG9F3NS00002",
			"45390/AFAG9F3NS00003",
			"45390/AFAG9F3NS00004",
			"45390/AFAG9F3NS00005",
			"45390/AFAG9F3NS00006"
		};//i*rows+j  i<cols,j<rows
		int rows=3;
		int cols=2;
//		ZPLLabel label=M13.palletMark(setTransPose, isMock, dpi, PO, SKU, id, createDate, productName, productName, pcs, grossWeight, palletNumber, boxSNs, rows, cols);
//		ZPLLabel label=new M13().palletMark(setTransPose, isMock, dpi, PO, SKU, id, createDate, productName, pcs, grossWeight, palletNumber);
//		System.out.println(label.toZPL());
//		ImageIO.write(label.toPNG(), "png", new File("target/m13_pallet.png"));
	}
}
