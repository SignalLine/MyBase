package com.rilin.lzy.mybase.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

import com.rilin.lzy.mybase.R;


public class SelectPicDialog {
	
	
	public static String path= Environment.getExternalStorageDirectory().getAbsolutePath()
			+ "/enong";
	public static final int PHOTOHRAPH = 11;// 拍照
	public static final int PHOTOZOOM = 22; // 相册4.4之前
	public static final int PHOTOZOOM1 = 44; // 相册4.4之后
	public static Bitmap bitmap;//图片
	public static File file;//文件
	
	
	/**
	 * 在Activity中显示
	 * @param context
	 */
	public static void showDialog(final Activity context){
		final Dialog dialog = new Dialog(context, R.style.ActionSheetAnimation);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		
		View view = inflater.inflate(R.layout.dialog_select_photo, null);
		Button pop_cancel = (Button) view.findViewById(R.id.pop_cancel);
		Button takephoto = (Button) view.findViewById(R.id.takephoto);
		Button photozoom = (Button) view.findViewById(R.id.photozoom);

		pop_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});
		takephoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(SelectPicDialog.path, "temp.png")));
				intent.putExtra("flag", true);
				context.startActivityForResult(intent, PHOTOHRAPH);
			}
		});
		photozoom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				Intent intent = new Intent();
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
					intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
					context.startActivityForResult(intent, PHOTOZOOM1);
				} else {
					intent.setAction(Intent.ACTION_GET_CONTENT);
					context.startActivityForResult(intent, PHOTOZOOM);
				}

			}
		});
		
		
		
		dialog.setContentView(view);
		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
		params.gravity = Gravity.BOTTOM;
		params.width = (int) (manager.getDefaultDisplay().getWidth()*0.95);
		dialog.getWindow().setAttributes(params);
		dialog.show();
	}
	
	/**
	 * 在Fragment中显示
	 * @param fragment
	 */
	public static void showDialog(final Fragment fragment){
		Activity context = fragment.getActivity();
		final Dialog dialog = new Dialog(context,R.style.ActionSheetAnimation);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		
		View view = inflater.inflate(R.layout.dialog_select_photo, null);
		Button pop_cancel = (Button) view.findViewById(R.id.pop_cancel);
		Button takephoto = (Button) view.findViewById(R.id.takephoto);
		Button photozoom = (Button) view.findViewById(R.id.photozoom);

		pop_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});
		takephoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(path, "temp.png")));
				intent.putExtra("flag", true);
				fragment.startActivityForResult(intent, PHOTOHRAPH);
			}
		});
		photozoom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				Intent intent = new Intent();
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
					intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
					fragment.startActivityForResult(intent, PHOTOZOOM1);
				} else {
					intent.setAction(Intent.ACTION_GET_CONTENT);
					fragment.startActivityForResult(intent, PHOTOZOOM);
				}

			}
		});
		
		
		
		dialog.setContentView(view);
		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
		params.gravity = Gravity.BOTTOM;
		params.width = (int) (manager.getDefaultDisplay().getWidth()*0.95);
		dialog.getWindow().setAttributes(params);
		dialog.show();
	}
	/**
	 * 
	 * 4.4 以上处理相册照片获取
	 */
	@SuppressLint("NewApi")
	public static String getPath(final Context context, final Uri uri) {
		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];
				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}
				// TODO handle non-primary volumes
			} else if (isDownloadsDocument(uri)) {
				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));
				return getDataColumn(context, contentUri, null, null);
			} else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];
				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}
				final String selection = MediaColumns._ID + "=?";
				final String[] selectionArgs = new String[] { split[1] };
				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		} else if ("content".equalsIgnoreCase(uri.getScheme())) {
			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}
		return null;

	}
	
	//图片sd地址  上传服务器时把图片调用下面方法压缩后 保存到临时文件夹 图片压缩后小于100KB，失真度不明显
	public static Bitmap revitionImageSize(String path){
		BufferedInputStream in;
		try {
			in = new BufferedInputStream(new FileInputStream(
					new File(path)));
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, options);
			in.close();
			int i = 0;
			Bitmap bitmap = null;
			while (true) {
				if ((options.outWidth >> i <= 1000)
						&& (options.outHeight >> i <= 1000)) {
					in = new BufferedInputStream(
							new FileInputStream(new File(path)));
					options.inSampleSize = (int) Math.pow(2.0D, i);
					options.inJustDecodeBounds = false;
					bitmap = BitmapFactory.decodeStream(in, null, options);
					break;
				}
				i += 1;
			}
			return bitmap;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 得到压缩后的图片
	 * @param file
	 * @param sampleSize 压缩后图片大小
	 * @return
	 */
	public static Bitmap getSampleSizedBitmap(File file,int sampleSize){
		long fileSize = SDcardUtils.getFileSize(file);
		int scale = (int) (fileSize/(sampleSize*1024));
		BitmapFactory.Options options = new BitmapFactory.Options();
		if(scale <= 1){
			options.inSampleSize = 1;
		}else{
			options.inSampleSize = scale+1;
		}
		
		Bitmap bm = null;
		try{
			bm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		}catch(OutOfMemoryError e){
			e.printStackTrace();
			options.inSampleSize = 10;
			bm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		}
		return bm;
	}
	
	
	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {
		Cursor cursor = null;
		final String column = MediaColumns.DATA;
		final String[] projection = { column };
		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}
	/**
	 * 把图片保存到文件
	 * @param bitmap
	 * @param fileName
	 * @return
	 */
	public static File saveBitmapToFile(Bitmap bitmap,String fileName){
		File f = SDcardUtils.createFile2(SelectPicDialog.path, fileName);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return f;
	}
	
	/**
	 * 根据uri得到文件的绝对路径
	 * @param uri
	 * @return
	 */
	public static String getAbsoluteImagePath(Activity context,Uri uri)
	   {
	       // can post image
	       String [] proj={MediaStore.Images.Media.DATA};
	       Cursor cursor = context.managedQuery( uri,
	                       proj,                 // Which columns to return
	                       null,       // WHERE clause; which rows to return (all rows)
	                       null,       // WHERE clause selection arguments (none)
	                       null);                 // Order-by clause (ascending by name)
	      
	       int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	       cursor.moveToFirst();
	       String path = cursor.getString(column_index);
	       return path;
	   }
	
	/**
	 * 此方法必须在Activity或者Fragment的OnActivityResult中调用
	 * @param requestCode
	 * @param resultCode
	 * @param intent
	 */
	public static void onActivityResult(Activity activity,int requestCode,int resultCode,Intent intent){
		final String fileName = System.currentTimeMillis()+".png";
		File file = null;
		switch (requestCode) {
		case SelectPicDialog.PHOTOHRAPH://拍照
			
			file = new File(SelectPicDialog.path + "/temp.png");
			
			break;
		case SelectPicDialog.PHOTOZOOM://android 4.4以前相册选择
			if (intent == null) {
				return;
			}
			String pathName = getAbsoluteImagePath(activity,intent.getData());
			file = new File(pathName);
			break;
		case SelectPicDialog.PHOTOZOOM1://Android 4.4以后相册选择
			if (intent == null) {
				return;
			}
			String mFileName = SelectPicDialog.getPath(
					activity, intent.getData());
			file = new File(mFileName);
			break;
		}
		if(file != null){
			SelectPicDialog.bitmap = SelectPicDialog.revitionImageSize(file.getAbsolutePath());
			SelectPicDialog.file = SelectPicDialog.saveBitmapToFile(SelectPicDialog.bitmap, fileName);
		}
	}
}
