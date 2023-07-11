package com.dlsc.workbenchfx.demo.modules.productLaser.model;

import java.util.Calendar;

import com.cg.core.module.BizException;
import com.cg.core.util.DateUtil;
import com.cg.core.util.MathUtil;
import com.dlsc.workbenchfx.demo.ModelUtil;
import com.dlsc.workbenchfx.demo.global.GlobalConfig;
import com.dlsc.workbenchfx.demo.modules.productInfo.model.ProductInfo;


public class ProductLaser extends ProductInfo implements Cloneable{
    private String createDate;  
    private String SNCode;
    private String used;//="否";
    private String streamCode;
    private String status;//="N"; // 退回返修R
    private String creator;//="";

    private String ecologicalChain; //生态链公司
    private String reserved;// = "F"; //预留位置
    private String weight; //重量
    private String shippingCode; //出货编码 发现漏了补回来.
    private String cartoonBoxCode;//中箱
    private String shippingDate;//出货日期

    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    public ProductLaser(String productPartNumber, 
            String productName, 
            String productType, //
            String productColor, 
            String createDate, 
            String factoryCode, 
            String streamCode, 
            String creator,
            String SNCode) {
        super(productPartNumber, productName, productType, productColor);
        this.createDate = createDate;
        this.factoryCode = factoryCode;
        this.SNCode = SNCode;
        this.streamCode = streamCode;
        this.creator = creator;
    }

    public String getCreateDate() {
        return createDate;
    }    

    public String getSNCode() {
        return SNCode;
    }

    public String getUsed() {
        return used;
    }

    public String getStreamCode() {
        return streamCode;
    }

    public String getStatus() {
        return status;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setSNCode(String sNCode) {
        SNCode = sNCode;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public void setStreamCode(String streamCode) {
        this.streamCode = streamCode;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getEcologicalChain() {
        return ecologicalChain;
    }

    public void setEcologicalChain(String ecologicalChain) {
        this.ecologicalChain = ecologicalChain;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public ProductLaser() {
		super();
	}
	/**
	 * 
	 * @param sn 未知来源的SN码(可能是第三方系统生成)
	 * @param clientType 客户端类型
	 * @param template 产品模板
	 * @return 完整的产品信息
	 * @throws BizException SN码不符合规范
	 */
	public static ProductLaser fromSN(String sn,final String clientType,ProductInfo template) throws BizException{
		if(template==null)
			throw new BizException("未选择产品信息");
		if(sn==null||sn.isBlank())
			throw new BizException("SN码不能为空");	
		switch(clientType) {
		case "xiaomi"://小米
			return fromXiaoMiSN(sn,template);
		case "sushi"://素士
			return fromSuShiSN(sn,template);
		default:
			throw new BizException("不支持的SN码规则模板:"+clientType);
		}
	}
	public static ProductLaser fromTemplate(String clientType,String streamCode,ProductInfo template) throws BizException {
		if(template==null)
			throw new BizException("未选择产品模板");
		if(streamCode==null||streamCode.isBlank())
			throw new BizException("自动生成流水失败");
		ProductLaser laser=new ProductLaser();
		ModelUtil.fillLaserValueFromInfo(laser, template);
		//生成SN码
		laser.setStreamCode(streamCode);
		
		return null;
	}
	public String toSNPrefix(final String clientType) throws BizException {
		switch(clientType) {
		case "xiaomi":
			return toXiaoMiSNPrefix();
		case "sushi":
			return toSushiSNPrefix();
		default:
			throw new BizException("不支持的SN码规则模板:"+clientType);
		}
	}	
	private String toXiaoMiSNPrefix() throws BizException {
		//12345/ABCDEF0W800001
		//1.商品ID,固定5位,来自于产品模板
		final String productId=getProductId();
		if(productId==null||productId.isBlank())
			throw new BizException("商品ID不能为空");
		if(productId.length()!=5)
			throw new BizException("商品ID必需5个字符:"+productId);
		final StringBuilder sn=new StringBuilder(16).append(productId).append("/");
		//2.生态链公司编码,2位数字+字母,不含I和O,来自于镭雕设置
		final String ec=getEcologicalChain();
		if(ec==null||ec.length()!=2)
			throw new BizException("生态链公司编码必需2个字符:"+ec);
		if(isBadCode(ec.charAt(0))||isBadCode(ec.charAt(1)))
			throw new BizException("生态链公司编码只能是大写字母A-Z和数字1-9(不含I和O):"+ec);
		sn.append(ec);
		//3.代工厂编码,2位数字+字母,不含I和O,来自于镭雕设置
		final String fc=getFactoryCode();
		if(fc==null||fc.length()!=3)
			throw new BizException("代工厂编码必需3个字符:"+fc);
		if(isBadCode(fc.charAt(0))||isBadCode(fc.charAt(1))||isBadCode(fc.charAt(2)))
    		throw new BizException("代工厂编码只能是大写字母A-Z和数字1-9(不含I和O):"+fc);
		//4.SN码预留位，目前只有F,和素士的产品属性N/R类似
		sn.append(fc).append("F");//预留位		
		//5.SN码生成日期,自动取当前日期
		final Calendar now=Calendar.getInstance();//日期编码取当前日期
		sn.append(now.get(Calendar.YEAR)%10);//年号取最后一位
		sn.append("NPQRSTUVWXYZ".toCharArray()[now.get(Calendar.MONTH)]);//月
		sn.append("123456789ABCDEFHJKMNPQRSTUVWXYZ".toCharArray()[now.get(Calendar.DAY_OF_MONTH)-1]);//日
		return sn.toString();
	}
	private static ProductLaser fromXiaoMiSN(String sn,ProductInfo template) throws BizException {
		//12345/ABCDEF0W800001 
		//productId+/+ecologicalChain(不在模板中配)+factoryCode(不在模板中配)+F+createDate+streamCode
    	if(sn.length()!=20)
    		throw new BizException("SN码长度必须为20:"+sn);    
    	int slashIdx=sn.indexOf("/");
    	if(slashIdx!=5)
    		throw new BizException("SN码斜线位置错误:"+sn);
    	final ProductLaser laser=new ProductLaser();
    	final String laserId=sn.substring(0,5);
    	if(!laserId.equals(template.getProductId()))
    		throw new BizException("SN码商品ID:"+laserId+"和选择的产品信息不同:"+template.getProductId());
    	laser.setProductId(laserId);//商品ID
    	final String ecologicalChain=sn.substring(6,8);    	
    	if(isBadCode(ecologicalChain.charAt(0))||isBadCode(ecologicalChain.charAt(1)))
    		throw new BizException("SN码生态链编码只能是大写字母A-Z和数字1-9(不含I和O):"+sn);
    	laser.setEcologicalChain(ecologicalChain);//生态链编码
    	final String factoryCode=sn.substring(8,11);
    	if(!factoryCode.equals(template.getFactoryCode()))
			throw new BizException("SN码包含的工厂代码"+factoryCode+"与设置值"+template.getFactoryCode()+"不一致");
    	if(isBadCode(factoryCode.charAt(0))||isBadCode(factoryCode.charAt(1))||isBadCode(factoryCode.charAt(2)))
    		throw new BizException("SN码代工厂编码只能是大写字母A-Z和数字1-9(不含I和O):"+sn);
    	laser.setReserved(String.valueOf(sn.charAt(11)));//预留位
    	//fix: 小米产品信息没有指定工厂编码
    	if(template.getFactoryCode()!=null&&!factoryCode.equals(template.getFactoryCode()))
			throw new BizException("SN码工厂编码:"+factoryCode+"与选择的产品信息不同:"+template.getFactoryCode());
    	laser.setFactoryCode(factoryCode);
    	laser.setCreateDate(DateUtil.now());//由唐博仕确认，彩盒标签生产日期和产品SN记录的日期不相干,此字段对应彩盒标签的生产日期
    	//暂不对SN码的日期编码合法性做校验
    	String streamCode=toDecimalStreamCode(sn.substring(15,20));//看小米文档疑似支持32进制的流水，但语焉不详，既定事实是，工厂以前生产的小米产品都是十进制流水    	
    	if(streamCode==null)
    		throw new BizException("SN码流水号只能由数字1-9组成:"+sn);    	
    	laser.setStreamCode(streamCode);
    	laser.setSNCode(sn);
    	ModelUtil.fillLaserValueFromInfo(laser, template);//从模板中补齐信息
    	
		return laser;
	}
	private String toSushiSNPrefix() throws BizException {		
		final StringBuilder sn=new StringBuilder(32);
		//1.产品型号 来自于产品模板
		if(this.productType==null||this.productType.isBlank())
			throw new BizException("未指定产品型号");
		sn.append(productType);
		//2.产品颜色编码,2位 来自于产品模板
		if(this.productColor==null||this.productColor.isBlank())
			throw new BizException("未指定产品颜色");
		String colorCode=GlobalConfig.getInstance().getColorCode(this.productColor);
		if(colorCode==null||colorCode.isBlank())
			throw new BizException("找不到"+productColor+"的颜色编码");
		if(colorCode.length()!=2)
			throw new BizException(productColor+"的颜色编码不是2位编码:"+colorCode);
		sn.append(colorCode);
		//3.代工厂编码,2位 来自于产品模板
		if(this.factoryCode==null||this.factoryCode.isBlank())
			throw new BizException("未指定代工厂编码");
		if(this.factoryCode.length()!=2)
			throw new BizException("代工厂编码不是2位编码:"+this.factoryCode);
		sn.append(this.factoryCode);		
		//4.编码日期,3位编码,自动生成
		Calendar now=Calendar.getInstance();
		int year=now.get(Calendar.YEAR);
		//4.1 素士目前提供的年份编码只到2030年 [2020,2030] 对应 [M,W]
		int yearIdx=year-2020;
		if(yearIdx<0||yearIdx>11)
			throw new BizException("素士目前提供的年份编码只支持2020~2030年");			
		sn.append("MNOPQRSTUVW".charAt(yearIdx));
		//4.2 月份编码
		int month=now.get(Calendar.MONTH);//0~11
		sn.append("EFGHIJKLMNOP".charAt(month));
		//4.3 日编码
		int day=now.get(Calendar.DAY_OF_MONTH);//1~31
		sn.append("123456789ABCDEFGHIJKLMNOPQRSTUV".charAt(day));
		//5.产品属性 N表示普通产品, R表示返工产品,实际应该是和小米的reserved同一个字段,历史遗留问题，不改了 
		if(this.status==null||this.status.isBlank())
			throw new BizException("未指定产品属性");
		if(!"N".equals(status)&&!"R".equals(status))
			throw new BizException("产品属性只能是N或者R");
		sn.append(this.status);
		return sn.toString();
	}
	private static ProductLaser fromSuShiSN(String sn,ProductInfo template) throws BizException {
		//MT1SROYONBN00147
		//productType+colorCode+factoryCode(不在模板中配)+N/R+streamCode
		final int len=sn.length();
		if(len<14)
    		throw new BizException("SN码长度至少要14位:"+sn);
		String streamCode=toDecimalStreamCode(sn.substring(len-5,len));//最后5位为流水号
		if(streamCode==null)
    		throw new BizException("SN码流水号只能由数字1-9组成:"+sn);		
		final ProductLaser laser=new ProductLaser();
		laser.setStreamCode(streamCode);
		char mark=sn.charAt(len-6);
		if(mark!='N'&&mark!='R')
			throw new BizException("SN码产品属性只能是N或者R:"+sn);
		laser.setStatus(new StringBuilder(mark).toString());//产品状态,N表示正常生产normal,R表示客退维修品repair
		laser.setCreateDate(DateUtil.now());//生产日期,SN码中的3位生产日期不知道有啥用，先不管
		int fcStart=len-11;
		final String factoryCode=sn.substring(fcStart, fcStart+2);
		if(!factoryCode.equals(template.getFactoryCode()))
			throw new BizException("SN码包含的工厂编码"+factoryCode+"与设置值"+template.getFactoryCode()+"不一致");
		if(!factoryCode.equals(template.getFactoryCode()))
			throw new BizException("SN码工厂编码:"+factoryCode+"与选择的产品信息不同:"+template.getFactoryCode());
		laser.setFactoryCode(factoryCode);//2位工厂编码
		int colorStart=len-13;
		final String colorCode=sn.substring(colorStart, colorStart+2);//2位颜色编码
		final String selectColorCode=GlobalConfig.getInstance().getColorCode(template.getProductColor());
		if(!colorCode.equals(selectColorCode))
			throw new BizException("SN码颜色编码:"+colorCode+"与选择的产品信息不同:"+selectColorCode);
		laser.setProductColor(template.getProductColor());//2位颜色编码
		final String productType=sn.substring(0, colorStart);
		if(!productType.equals(template.getProductType()))
			throw new BizException("SN码型号:"+productType+"与选择的产品信息不同:"+template.getProductType());
		laser.setProductType(productType);//产品型号编码
		laser.setSNCode(sn);
		//补齐产品信息
		ModelUtil.fillLaserValueFromInfo(laser, template);
		return laser;
	}
	private static String toDecimalStreamCode(String streamCode) throws BizException {
		if("00000".equals(streamCode))
    		throw new BizException("SN码流水号不能为0");
		Integer sc=MathUtil.toInteger(streamCode);
    	if(sc==null)//看小米文档疑似支持32进制的流水，但语焉不详，既定事实是，工厂以前生产的小米产品都是十进制流水
    		return null;
    	return sc.toString();
	}
	private static boolean isBadCode(char c) { //1-9 A-H J-N P-Z
		//生态链公司编码和工厂编码从AA开始编码,超出字母可用范围之后再启用数字1-9,禁止字母I和O,统一大写
		if(c<'1')return true;
		if(c<='9')return false;//1-9
		if(c<'A')return true;
		if(c<'I')return false;//A-H
		if(c=='I')return true;
		if(c<'O')return false;//J-N
		if(c<='Z')return false;//P-Z
		return true;
	}	
//	private static int getYear(char c) {//十年内取年分的最后一位数字，超过10年的不做追溯跟踪
//		int	y=LocalDate.now().getYear();
//		int r=y%10;//取余数
//		if(r==c)return y;
//		int fix=y/10;//取除数
//		if(r>c)return fix*10+c;  //比如为2，今年为2023,则结果应该为2022
//		return fix*10+c-10;	//比如为9,今年为2023,则结果应该为2019	2020+9-10
//	}

	public String getShippingCode() {
		return shippingCode;
	}

	public void setShippingCode(String shippingCode) {
		this.shippingCode = shippingCode;
	}

	public String getCartoonBoxCode() {
		return cartoonBoxCode;
	}

	public void setCartoonBoxCode(String cartoonBoxCode) {
		this.cartoonBoxCode = cartoonBoxCode;
	}

	public String getShippingDate() {
		return shippingDate;
	}

	public void setShippingDate(String shippingDate) {
		this.shippingDate = shippingDate;
	}	
}
