package com.henau.pictureselect.utils;

import android.os.Environment;

import java.io.File;

public class Constant {

	//默认保存数据路径
	public final static String IMAGES = "images";
	public final static String DEFAULT_SAVE_PATH = Environment.getExternalStorageDirectory() 
			+ File.separator + "select";
	public final static String DEFAULT_SAVE_IMAGES_PATH = DEFAULT_SAVE_PATH + File.separator
			+ IMAGES;

	// 拍照
	public static final int REQUEST_CODE_PICK = 1;//选取单张图片
	public static final int REQUEST_CODE_CAPTURE = 2;//拍照
	public static final int REQUEST_CODE_CROP = 3;//裁剪图片
	public static final int REQUEST_CODE_PICK_MULTI = 4;//选取多张图片

	public static final String IMAGE_LIST = "image_list";
}
