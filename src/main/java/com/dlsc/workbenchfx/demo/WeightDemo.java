package com.dlsc.workbenchfx.demo;

import com.cg.core.util.Log;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortInvalidPortException;

import java.io.UnsupportedEncodingException;

public class WeightDemo {
	/**
	 * 串口名称
	 */
	private final String name;	
//	private final int readTimeout;//读取称重数据的超时时间，单位ms	
//	private final float minScale;
	/**
	 * 称重最小读数
	 */
	private final float minVal;	
	private byte[] preValue;
	private byte[] curValue;//当前值
	private int sameCount=0;
	private final int N;	
	/**
	 * 
	 * @param name 串口名称
	 * @param minValue 电子称最小读数,默认为0.005
	 * @param N 称重结果连续N次相同则视为稳定值,默认为3
	 */
	public WeightDemo(String name,Float minValue,Integer N) {
		super();
		this.name = name;		
//		this.minScale=minScale;
		this.minVal=minValue==null||minValue<0f?0.005f:minValue;
		this.N=N==null||N<=0?3:N;
		Log.debug("称重最小读数:"+minValue+";稳点因子:"+N);
	}
	private Float getAvg() throws InterruptedException, UnsupportedEncodingException {
		curValue=readPortData(16);
		if(curValue==null)
			return getAvg();
		//校验字节码有效性
		final int rIdx=curValue.length-1;
		int endIdx=rIdx;
		if(curValue[rIdx]!=0x00) {
			for(int i=0;i<rIdx;i++) {
				if(curValue[i]==0x00) {
					endIdx=i;
					break;
				}
			}
			if(endIdx==rIdx) {//未发现结束符,可能是零值
				Log.warn("16个字节中缺少0x00,忽略此处读数");
				return getAvg();
			}
			int dirtyLen=endIdx+1;
			Log.warn("发现脏数据，长度为:"+dirtyLen);
			//抛弃脏数据，重新读数，从endIdx之后补足脏数据位
			Log.info("正在补读脏数据");
			byte[] dirtyBytes=readPortData(dirtyLen);
			Log.info("已读脏数据:"+dirtyBytes.length);
			byte[] repair=new byte[16];
			int pos=0;
			for(int i=dirtyLen;i<curValue.length;i++) {
				repair[pos++]=curValue[i];
			}
			for(int i=0;i<dirtyBytes.length;i++) {
				repair[pos++]=dirtyBytes[i];
			}
			Log.info("已修复脏数据:"+pos);
			curValue=repair;
		}

		if(preValue==null) {
			sameCount=1;
			preValue=curValue;
			return getAvg();
		}else {
			boolean isSame=true;
			for(int i=0;i<6;i++) {
				int index=i+4;
				byte pre=preValue[index];
				byte cur=curValue[index];
				if(pre==cur) {
//					sameCount++;
				}else {
					isSame=false;
				}
			}
			if(isSame) {
				sameCount++;
			}else {
				sameCount=1;
			}
			preValue=curValue;
			if(sameCount<N)
				return getAvg();
			else {
				preValue=null;
				sameCount=0;
				int scaleValue=(curValue[4]-0x30)*10000+(curValue[5]-0x30)*1000
						+(curValue[7]-0x30)*100+(curValue[8]-0x30)*10
						+(curValue[9]-0x30);
				return scaleValue/1000.0f;
			}
				
		}
//		String portData="U 00.116kga S 00.116kgg S 00.116kgg S 00.116kgg S 00.116kgg S 00.116kgg S 00.116kgg S 00.116kgg S 00.116kgg S 00.116kgg ";		
//		parsePortData(portData);
	}
	/**
	 * 阻塞循环读数，直到读到大于一个刻度的值或者当前线程被终止
	 * @return 有效刻度值，若被打断则返回null
	 * @throws Exception 称重异常,例如连接失败等
	 */
	public Float getAvgValue() throws Exception{		
		try {
			long start=System.currentTimeMillis();
			while(true) {
				long t1=System.currentTimeMillis();
				Float avg=getAvg();				
				long t2=System.currentTimeMillis();
				Log.info("称重读数:"+avg+",耗时:"+(t2-t1)+"ms");
				if(avg==null||avg<=minVal)continue;//未放称重物体
				Log.info("称重最终结果:"+avg+",线程累计持续时长:"+(t2-start)+"ms");
				return avg;			
			}
		}catch (InterruptedException e) {
			return null;
		}finally {
			closeSerialPort();
		}
	}
	private void closeSerialPort() {
		if(serialPort!=null) {
			Log.info("关闭串口:"+serialPort.getDescriptivePortName());
			serialPort.closePort();
		}
	}
	private void newSerialPort() {
		if(serialPort==null) {
			try {
				serialPort=SerialPort.getCommPort(name);
				serialPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);
			}catch (SerialPortInvalidPortException e) {
				Log.error("",e);
				throw new IllegalArgumentException("串口名无效:"+name);
			}
		}
		if(!serialPort.isOpen()) {
			boolean isOpen=serialPort.openPort();
			if(!isOpen) {
				Log.warn("串口连接失败:"+name);
				throw new UnsupportedOperationException("串口连接失败:"+name);
			}
		}
	}
	private SerialPort serialPort;
	private void waitTime(long time) throws InterruptedException{
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			throw e;
		}
	}
	private byte[] readPortData(int readLen) throws InterruptedException, UnsupportedEncodingException {//加载一组串口数据
		newSerialPort();
		int len=0;
		int tryTime=0;
		int totalTime=0;
		int interval=10;
		int maxTryTime=1000/interval;
		int maxTotalTime=1000/interval;
		while(len<readLen) {//500ms内没有接收到数据，则判定串口异常
			if(tryTime>=maxTryTime) {//至少500ms内没有接收到数据，则判定串口异常
				throw new RuntimeException("串口驱动异常\n驱动不兼容或接头松动等");
			}
			if(totalTime>=maxTotalTime) {//1000ms内没有接收到一组数据，按超时处理
				throw new RuntimeException("串口读数不稳定\n可能是串口接头松动等");
			}
			waitTime(interval);
			len=serialPort.bytesAvailable();
			totalTime++;
			if(len==0) {
				tryTime++;
			}
		}
		byte[] readBuffer = new byte[readLen];//一组数据按16个字节计算
		int readBytes=serialPort.readBytes(readBuffer, readBuffer.length);
		Log.debug("称重接收字节数:"+readBytes);
//		log.debug("称重接收字节内容:"+new String(readBuffer, "US-ASCII"));
//		Log.info("称重字节:"+readBytes);
		if(readBytes<readLen) {
			Log.warn("称重接收字节数低于"+readLen+"，忽略处理，继续等待下一个数据");
			return readPortData(readLen);
		}	
		return readBuffer;
	}

	public static void main(String[] args) {
		Thread t=new Thread(()->{
			WeightDemo demo=new WeightDemo("COM5",0.005f,3);
			try {
				Float value=demo.getAvgValue();
				if(value==null)
					Log.info("称重被打断");
				else
					Log.info("称重结果:"+value);
			} catch (Exception e) {
				Log.info("称重异常:"+e.getMessage());
			}
		});
		t.start();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {			
			Log.error("",e);
		}
		t.interrupt();
    }
}

