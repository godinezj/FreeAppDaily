package com.isosoft.freeappdaily;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

public class PropertiesUtil {
	private static String TAG = PropertiesUtil.class.getSimpleName();
	public static Properties getProperties(Context ctx) {
		try {
			Resources res = ctx.getResources();
			AssetManager am = res.getAssets();
			InputStream in = am.open("freeappdaily.properties");
			Properties props = new Properties();
			props.load(in);
			return props;
		} catch (Exception e) {
			Log.e(TAG, "Error loading properties");
		}
		return null;
	}
	
	public static String getEula(Context ctx) {
		Resources res = ctx.getResources();
		AssetManager am = res.getAssets();
		StringBuffer eula = new StringBuffer();
		String line;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(am.open("EULA.txt")));
			while((line = reader.readLine()) != null){
				eula.append(line);
			}
			return eula.toString();
		} catch (IOException e) {
			Log.e(TAG, "Error loading EULA");
		}
		return EULA;
	}
	
	private static final String EULA = "End-User License Agreement for Free App Daily" 
+ "\nThis End-User License Agreement (EULA) is a legal agreement between you and the Author (Javier Godinez) for Free App Daily (the Software). By installing the software, you agree to be bounded by the terms of this EULA. If you do not agree to the terms of this EULA, do not install or use the software. Free App Daily is being distributed as Freeware for personal, non commercial use. You are NOT allowed to make a charge for distributing this Software. It may be distributed freely only on Google Play and the Amazon Appstore only. "
+ "\nSOFTWARE PRODUCT LICENSE"
+ " 1. GRANT OF LICENSE. This EULA grants you the following rights: Installation and Use. You may install and use the software. You may not reproduce or distribute copies of the software."
+ " 2. DESCRIPTION OF OTHER RIGHTS AND LIMITATIONS. Limitations on Reverse Engineering, Decompilation, Disassembly and change (add,delete or modify) the resources in the compiled the assembly. You may not reverse engineer, decompile, or disassemble the Software. The software is licensed as a single product. Its component parts may not be separated for use on more than one computer. You may permanently transfer all of your rights under this EULA, provided the recipient agrees to the terms of this EULA. The Author of this software may terminate this EULA if you fail to comply with the terms and conditions of this EULA. In such event, you must destroy all copies of the software and all of its component parts."
+ " 3. COPYRIGHT. All title and copyrights in and to the Software are owned by the Author of this Software. The Software is protected by copyright laws and international treaty provisions. Therefore, you must treat the software like any other copyrighted material. The licensed users can use all functions in the software."
+ "\nLIMITED WARRANTY"
+ " 1. NO WARRANTIES. The Author of this Software expressly disclaims any warranty for the software. The software and any related documentation is provided \"as is\" without warranty of any kind, either express or implied, including, without limitation, the implied warranties or merchantability, fitness for a particular purpose, or noninfringement. The entire risk arising out of use or performance of the software remains with you."
+ " 2. NO LIABILITY FOR DAMAGES. In no event shall the author of this Software be liable for any special, consequential, incidental or indirect damages whatsoever (including, without limitation, damages for loss of business profits, business interruption, loss of business information, or any other pecuniary loss) arising out of the use of or inability to use this product, even if the Author of this Software is aware of the possibility of such damages and known defects.";
}
