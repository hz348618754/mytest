package com.example.mytest;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Formatter;


/**
 * Created by 辉 on 2017/11/18 0018.
 */

public class ShowInfo extends Activity {
	TextView tv;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_info);

		tv = findViewById(R.id.show_info);
		tv.setText(getInfo()+getScreenSize()+getCpuName()+getMaxCPU()+getMinCPU()+getCurCPU()+getMacAddress()
					+getAvailMemory()+getTotalMemory());
	}

	//获取手机基本信息
	private String getInfo() {
		TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
				return null;
			}
		}
		String imei = telephonyManager.getDeviceId();
		String imsi = telephonyManager.getSubscriberId();
		String mtype = Build.MODEL;
		String mbrand = Build.BRAND;
		String mnumber = telephonyManager.getLine1Number();
		return "手机IMEI号："+imei+"\n手机IESI号："+imsi+"\n手机型号："+mtype+"\n手机品牌："+mbrand+"\n手机号码："+mnumber+"\n"
				;
	}

	//获取屏幕尺寸
	private String getScreenSize(){
		Point point = new Point();
		getWindowManager().getDefaultDisplay().getSize(point);
		String result = "屏幕分辨率为："+point.y+"*"+point.x;
		return  result+"\n";
	}

	//获取CPU品牌
	private String getCpuName(){
		String str1 = "/proc/cpuinfo";
		String str2 = "";
		String[] cpuInfo = {"",""};
		String[] arrayOfString;
		try{
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr,8192);
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			for(int i = 2;i<arrayOfString.length;i++){
				cpuInfo[0] = cpuInfo[0]+arrayOfString[i]+"";
			}
			localBufferedReader.close();
		}catch (IOException e){
			e.printStackTrace();
		}
		return "CPU型号："+cpuInfo[0]+"\n";
	}

	//获取CPU最大频率
	private String getMaxCPU(){
		final String MaxPath = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq";
		FileReader fr = null;
		BufferedReader br = null;
		double result = 0.0;
		try{
			fr = new FileReader(MaxPath);
			br = new BufferedReader(fr);
			result = (Double.parseDouble(br.readLine())/1000);
		}catch (Exception e){
			e.printStackTrace();
		}
		return "CPU最大频率为："+result+"MHz\n";
	}
	//获取CPU最小频率
	private String getMinCPU(){
		final String MinPath = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq";
		FileReader fr = null;
		BufferedReader br = null;
		long result = 0;
		try{
			fr = new FileReader(MinPath);
			br = new BufferedReader(fr);
			result = (Long.parseLong(br.readLine())/1000);
		}catch (Exception e){
			e.printStackTrace();
		}
		return "CPU最小频率为："+result+"MHz\n";
	}
	//获取CPU当前频率
	private String getCurCPU(){
		final String CurPath = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq";
		FileReader fr = null;
		BufferedReader br = null;
		double result = 0;
		try{
			fr = new FileReader(CurPath);
			br = new BufferedReader(fr);
			result = (Double.parseDouble(br.readLine())/1000);
		}catch (Exception e){
			e.printStackTrace();
		}
		return "当前CPU频率为："+result+"MHz\n";
	}

	//获取手机MAC地址
	private String getMacAddress(){
		String result = "";
		WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		result = wifiInfo.getMacAddress();
		return "手机MAC地址为："+result+"\n";
	}

	//获取手机当前可用内存大小
	private String getAvailMemory(){
		File path = Environment.getDataDirectory();
		StatFs statFs = new StatFs(path.getPath());
		long blockSize = statFs.getBlockSize();
		long availableBlocks = statFs.getAvailableBlocks();
		return "当前可用内存："+ android.text.format.Formatter.formatFileSize(ShowInfo.this,blockSize*availableBlocks)+"\n";
	}
	//获取手机总内存大小
	private String getTotalMemory(){
		String path = "/proc/meminfo";
		String str = "";
		try {
			FileReader localFileReader = new FileReader(path);
			BufferedReader localBufferedReader = new BufferedReader(localFileReader,8192);
			str = localBufferedReader.readLine();
			int start = str.indexOf(":");
			int end = str.indexOf("B");
			str =  str.substring(start+1,end+1).trim();
			localBufferedReader.close();
		}catch (Exception e){
			e.printStackTrace();
		}

		return "手机总内存空间为："+str+"\n";
	}
}
