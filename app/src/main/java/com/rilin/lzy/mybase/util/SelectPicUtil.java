package com.rilin.lzy.mybase.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;



import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rilin.lzy.mybase.R;
import com.rilin.lzy.mybase.activity.BaseActivity;


/**
 * @author zhaidongming
 * 
 *         switch (arg0) {
 * 
 *         case SelectPicUtil.PHOTOHRAPH:
 * 
 *         // 设置文件保存路径这里放在跟目录下 File picture = new File(SelectPicUtil.path +
 *         "/temp.png"); int degree = SelectPicUtil.getBitmapDegree(picture
 *         .getAbsolutePath()); if (degree != 0) { Bitmap bb =
 *         BitmapFactory.decodeFile(picture.getAbsolutePath()); bb =
 *         SelectPicUtil.rotateBitmapByDegree(bb, degree); FileOutputStream f =
 *         null; try { f = new FileOutputStream(picture); } catch
 *         (FileNotFoundException e) { e.printStackTrace(); } boolean b =
 *         bb.compress(Bitmap.CompressFormat.PNG, 100, f); if (b) {
 *         bb.recycle(); } try { f.close(); } catch (IOException e) {
 *         e.printStackTrace(); } } SelectPicUtil.file = new
 *         File(SelectPicUtil.path + "/temp.png");
 *         SelectPicUtil.startPhotoZoom(Uri.fromFile(SelectPicUtil.file),
 *         context); // image=BitmapFactory.decodeFile(file.getAbsolutePath());
 *         // groupimage.setImageBitmap(image);
 * 
 *         break; case SelectPicUtil.PHOTOZOOM: if (arg2 == null) { return; }
 *         SelectPicUtil.startPhotoZoom(arg2.getData(), context); break; case
 *         SelectPicUtil.PHOTOZOOM1: if (arg1 == RESULT_OK && arg2 != null) {
 *         String mFileName = SelectPicUtil.getPath( getApplicationContext(),
 *         arg2.getData()); // file = new File(path + "/temp.png"); //
 *         mColorBitmap = getBitmap(mFileName); //
 *         mImage.setImageBitmap(mColorBitmap); SelectPicUtil.file = new
 *         File(mFileName);
 *         SelectPicUtil.startPhotoZoom(Uri.fromFile(SelectPicUtil.file),
 *         context); } break; case SelectPicUtil.PHOTORESOULT: if (arg2 == null)
 *         { return; } Bundle extras = arg2.getExtras(); if (extras != null) {
 * 
 *         SelectPicUtil.image = extras.getParcelable("data");
 *         ByteArrayOutputStream stream = new ByteArrayOutputStream();
 *         SelectPicUtil.image.compress(Bitmap.CompressFormat.PNG, 100,
 *         stream);// (0
 * 
 *         String fileName = "temp.png"; File pfile = new
 *         File(SelectPicUtil.path); if (!pfile.exists()) { pfile.mkdirs(); }
 *         SelectPicUtil.file = new File(SelectPicUtil.path + File.separator +
 *         fileName); FileOutputStream f = null; try { f = new
 *         FileOutputStream(SelectPicUtil.file); } catch (FileNotFoundException
 *         e) { e.printStackTrace(); } boolean b = SelectPicUtil.image.compress(
 *         Bitmap.CompressFormat.PNG, 100, f); if (b) {
 *         shop_pic.setImageBitmap(SelectPicUtil.image); }
 * 
 *         } break; }
 * 
 * 
 *         if (resultCode == RESULT_OK) { switch (requestCode) { case
 *         PHOTO_REQUEST_TAKEPHOTO:
 * 
 *         startPhotoZoom( Uri.fromFile(new File("/sdcard/fanxin/", imageName)),
 *         480); break;
 * 
 *         case PHOTO_REQUEST_GALLERY: if (data != null)
 *         startPhotoZoom(data.getData(), 480); break;
 * 
 *         case PHOTO_REQUEST_CUT: // BitmapFactory.Options options = new
 *         BitmapFactory.Options(); // // // 最关键在此，把options.inJustDecodeBounds =
 *         true; // 这里再decodeFile()，返回的bitmap为空 //
 *         ，但此时调用options.outHeight时，已经包含了图片的高了 // // options.inJustDecodeBounds
 *         = true; Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/fanxin/" +
 *         imageName); iv_avatar.setImageBitmap(bitmap);
 *         updateAvatarInServer(imageName); break; }
 *         super.onActivityResult(requestCode, resultCode, data); }
 */
public class SelectPicUtil {
	public static String path = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/zy/";
	public static File file;
	public static Bitmap image;

	public static int type = 0;// 区别是哪个照片

	public static final int PHOTOHRAPH = 11;// 拍照
	public static final int PHOTOZOOM = 22; // 相册4.4之前
	public static final int PHOTOZOOM1 = 44; // 相册4.4之后
	public static final int PHOTORESOULT = 33;// 结果

	public static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
	public static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	public static final int PHOTO_REQUEST_CUT = 3;// 结果

	public static String imageName;

	Button save;
	boolean issave;

	// public int getType() {
	// return type;
	// }
	//
	// public void setType(int type) {
	// this.type = type;
	// }

	private static CallBack ba = null;

	public static void setCallBack(CallBack back) {
		ba = back;
	}

	public interface CallBack {
		void getType(int type);
	}

	public static void initPop(final BaseActivity context) {
		
		File f = new File(path);
		if (!f.exists()) {
			if (f.mkdirs()) {
				// LogUtils.e("selectpic", "true");
			} else {
				// LogUtils.e("selectpic", "false");
			}
		}

		if (ba != null) {
			ba.getType(type);
		} else {
			Log.i("tag", "ba==null");
		}
		
		final Dialog dlg = new Dialog(context, R.style.ActionSheet);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.camera,
				null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);

		TextView pop_cancel = (TextView) layout.findViewById(R.id.pop_cancel);
		TextView takephoto = (TextView) layout.findViewById(R.id.takephoto);
		TextView photozoom = (TextView) layout.findViewById(R.id.photozoom);

		pop_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dlg.dismiss();

			}
		});
		takephoto.setOnClickListener(new OnClickListener() {//拍照

			@Override
			public void onClick(View v) {
				// Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// intent.putExtra(MediaStore.EXTRA_OUTPUT,
				// Uri.fromFile(new File(path, "temp.png")));
				// intent.putExtra("flag", true);
				// context.startActivityForResult(intent, PHOTOHRAPH);

				imageName = "zy_" + getNowTime() + ".png";
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// 指定调用相机拍照后照片的储存路径
				intent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(path, imageName)));
				Log.i("tag","拍照");
				context.startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
				dlg.dismiss();
			}
		});
		photozoom.setOnClickListener(new OnClickListener() {//相册

			@Override
			public void onClick(View v) {
				// Intent intent = new Intent();
				// intent.setDataAndType(
				// MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				// if (android.os.Build.VERSION.SDK_INT >=
				// android.os.Build.VERSION_CODES.KITKAT) {
				// intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
				// context.startActivityForResult(intent, PHOTOZOOM1);
				// } else {
				// intent.setAction(Intent.ACTION_GET_CONTENT);
				// context.startActivityForResult(intent, PHOTOZOOM);
				// }
				imageName = "zy_" + getNowTime() + ".png";
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				context.startActivityForResult(intent, PHOTO_REQUEST_GALLERY);

				dlg.dismiss();
			}
		});
		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(true);
		dlg.setContentView(layout);
		dlg.show();

	}

	@SuppressLint("SimpleDateFormat")
	private static String getNowTime() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("HHmmSS");
		return dateFormat.format(date);
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
	 * 读取图片的旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return 图片的旋转角度
	 */
	public static int getBitmapDegree(String path) {
		int degree = 0;
		try {
			// 从指定路径下读取图片，并获取其EXIF信息
			ExifInterface exifInterface = new ExifInterface(path);
			// 获取图片的旋转信息
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 将图片按照某个角度进行旋转
	 * 
	 * @param bm
	 *            需要旋转的图片
	 * @param degree
	 *            旋转角度
	 * @return 旋转后的图片
	 */
	public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
		Bitmap returnBm = null;

		// 根据旋转角度，生成旋转矩阵
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		try {
			// 将原始图片按照旋转矩阵进行旋转，并得到新的图片
			returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
					bm.getHeight(), matrix, true);
		} catch (OutOfMemoryError e) {
		}
		if (returnBm == null) {
			returnBm = bm;
		}
		if (bm != returnBm) {
			bm.recycle();
		}
		return returnBm;
	}

	public static void startPhotoZoom(Uri uri, BaseActivity context) {// content://media/external/images/media/57063
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例,不设置，则自由控�?
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// intent.putExtra("scale", true);// 黑边
		// intent.putExtra("scaleUpIfNeeded", true);// 黑边
		// outputX outputY 是裁剪图片宽�?
		intent.putExtra("outputX", 128);
		intent.putExtra("outputY", 128);
		intent.putExtra("return-data", false);

		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(path, imageName)));
		intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		context.startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}
}
