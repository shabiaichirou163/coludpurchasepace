package com.cloudpurchase.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

/**
 *  * 文件本地存取工具类，构造器接受一个上下文对象
 *
 * 主要用于将网络获取到的图片、文字存入本地
 *
 * 文件存储的位置：//mnt/sdcard/android/data/包名/catch
 *
 * IsSDcard()判断SD是否存在
 *
 * 需要注册SDCard读写权限
 *<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 *<uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
 *
 * @author Augustine
 * */
public class FileUtils {

	private Context ctx;//�������ȡ�����ļ�·������

	public FileUtils(Context ctx){
		this.ctx = ctx;
		/*Log.e("","SD���Ƿ����"+IsSDcard());
		Log.e("","�ڲ��洢���·��:---->>>");
		//���ж���Լ��������ɾ��->
		Log.e("",""+this.ctx.getCacheDir().getAbsolutePath());//data/data/����/catch
		Log.e("",""+this.ctx.getFilesDir().getAbsolutePath());//datat/data/����/file
		//�ⲿ�洢��ַ
		Log.e("",""+Environment.getExternalStorageDirectory().getAbsolutePath());//mnt/sdcard
		//�Ƽ�ʹ��
		Log.e("",""+this.ctx.getExternalCacheDir().getAbsolutePath());//mnt/sdcard/android/data/����/catch
		Log.e("",""+this.ctx.getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath());//mnt/sdcard/android/data/����/files/music*/
	}

	//判断SD卡是否存在��
	private boolean IsSDcard(){
		String state = Environment.getExternalStorageState();
		if(state.equals(Environment.MEDIA_MOUNTED)){
			return true;
		}
		return false;
	}

	/*
	储存一张图片
	 */
	public void saveBitmap(String name,Bitmap bitmap){
		if(name == null)
			return;
		if(bitmap == null)
			return;
		if(!IsSDcard())
			return;
		ByteArrayOutputStream  byteOut = new ByteArrayOutputStream();
		//format->��ʽ; quality->����
		bitmap.compress(CompressFormat.PNG, 45,byteOut);
		byte[] buffer = byteOut.toByteArray();
		try {
			FileOutputStream out = new FileOutputStream(
					ctx.getExternalCacheDir().
					getAbsolutePath()+"/"+name);
			//			FileOutputStream out = new FileOutputStream(
			//					Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+name);
			out.write(buffer);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	读取一张图片
	 */
	public Bitmap ReadBitmap(String name){
		if(name == null)
			return null;
		if(!IsSDcard())
			return null;
		try {
			FileInputStream in = new FileInputStream(ctx.getExternalCacheDir().getAbsolutePath()+"/"+name);
			//			FileInputStream in = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+name);
			Bitmap bitmap = BitmapFactory.decodeStream(in);
			in.close();
			return bitmap;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/*
	储存txt
	 */
	public void saveTxt(String name,String value){
		if(name == null)
			return;
		if(value == null)
			return;
		if(!IsSDcard())
			return;
		byte[]buffer = value.getBytes();
		try {
			FileOutputStream out = new FileOutputStream(ctx.getExternalCacheDir().getAbsolutePath()+"/"+name+".txt");
			out.write(buffer);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//读取text
	public String ReadTxt(String name){
		if(name == null)
			return null;
		if(!IsSDcard())
			return null;
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(ctx.getExternalCacheDir().getAbsolutePath()+"/"+name+".txt")));
			StringBuffer buffer = new StringBuffer();
			String line;
			while((line = br.readLine())!=null){
				buffer.append(line);
			}
			br.close();
			return buffer.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	删除文件txt
	 */
	public void deleteFile(String name){
		if(name == null){
			return;
		}
		if(!IsSDcard())
			return;
		File file = new File(ctx.getExternalCacheDir().getAbsolutePath()+"/"+name+".txt");
		file.delete();
	}

}
