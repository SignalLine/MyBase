package com.rilin.lzy.mybase.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.os.StatFs;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.rilin.lzy.mybase.exception.FzException;

/**   
 * 系统属性操作类 
 * @author cate
 * 2014-12-6 上午11:33:01   
 */

public class SystemUtils
{
	public static String TEMPFILEPATH = "blst_cameraTmp";
	private static final String TAG = SystemUtils.class.getSimpleName();

	// 获取本地（asserts）文件，转化为utf-8
	public static String getFromAssets(Context context, String fileName) {
		String result = "";
		try {
			InputStream in = context.getResources().getAssets().open(fileName);
			// 获取文件的字节数
			int lenght = in.available();
			// 创建byte数组
			byte[] buffer = new byte[lenght];
			// 将文件中的数据读到byte数组中
			in.read(buffer);
			result = new String(buffer,"utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取sdcard上剩余空间
	 *
	 * @return 返回值单位为k
	 */
	public static float getFreeSpaceOnSD() {
		// 检测sdcard是否已经挂载了
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File path = Environment.getExternalStorageDirectory();
			StatFs statfs = new StatFs(path.getPath());
			long blockSize = statfs.getBlockSize();// sdcard存储单位
			long availaBlock = statfs.getAvailableBlocks();// 可用空间块
			return blockSize * availaBlock / 1024;
		} else {
			return -1;
		}
	}

	/**
	 * 把图片缓存到sdcard的临时文件中
	 *
	 * @param bmp
	 * @param
	 */
	public static void saveBmpToSd(Bitmap bmp, File file) {
		int MAXFREDSPACE = 1 * 1024 * 1024;// 最大剩余空间1m
		if (getFreeSpaceOnSD() < MAXFREDSPACE) {
			L.i(TAG, "Low free space onsd, do not cache");
			return;
		}
		try {
			if (!file.exists()) {
				file.createNewFile();
			} else {
				file.delete();
				file.createNewFile();
			}

			FileOutputStream fos = new FileOutputStream(file);// 手动构建临时文件路径
			bmp.compress(Bitmap.CompressFormat.PNG, 60, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.w(TAG, "没有找到文件");
		} catch (IOException e) {
			Log.w(TAG, "IOException io异常");
		}
	}

	/* 获取当前应用的版本号 */
	public static String getVersion(Context context) {
		String version = null;
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packInfo;
		try {
			// getPackageName()是你当前类的包名，0代表是获取版本信息(eg：1.1)
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
			version = packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}

	// 获取应用的缓存目录中的缓存图片
	public static File getCacheFile(String iconUrl) throws IOException {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			int start = iconUrl.lastIndexOf("/");
			int end = iconUrl.length();
			final String iconname = iconUrl.substring(start, end);
			File file = new File(TEMPFILEPATH, iconname);
			return file;
		}
		return null;
	}

	// 将手机中图片的uri转换成绝对路径
	public static String turnUri2AbsolutePath(Activity context, Uri uri) {
		// 获取图片在sdcard上的路径
		String[] proj = { MediaStore.Images.Media.DATA };
		// 好像是android多媒体数据库的封装接口，具体的看Android文档
		Cursor cursor = context.managedQuery(uri, proj, null, null, null);
		// 按我个人理解 这个是获得用户选择的图片的索引值
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		// 将光标移至开头 ，这个很重要，不小心很容易引起越界
		cursor.moveToFirst();
		// 最后根据索引值获取图片路径
		return cursor.getString(column_index);
	}

	// gzip压缩
	public static byte[] gZip(File srcFile) throws IOException {
		byte[] data = null;
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			FileInputStream fis = new FileInputStream(srcFile);
			byte[] buf = new byte[1024];
			int len = 0;
			while ((len = fis.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			byte[] bContent = out.toByteArray();
			out.reset();// 清空byte数组

			GZIPOutputStream gOut = new GZIPOutputStream(out, bContent.length); // 压缩级别,缺省为1级
			DataOutputStream objOut = new DataOutputStream(gOut);
			objOut.write(bContent);
			objOut.flush();
			gOut.close();
			data = out.toByteArray();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		return data;
	}

	// bitmapt缩放，并且压缩成方形图片
	/**
	 *
	 * @param sourceFile
	 * @param context
	 * @param targetWidth
	 *            目标宽度
	 */
	public static Bitmap scaleBitmap(File sourceFile, Context context,
									 Float targetWidth) {
		try {
			BitmapFactory.Options opt = getOptions(context);
			Bitmap decodeFile = BitmapFactory.decodeFile(
					sourceFile.getAbsolutePath(), opt);
			// // //
			// System.out.println("拍照图片宽度"+decodeFile.getWidth()+"高"+decodeFile.getHeight());
			int imgWidth = decodeFile.getWidth();
			int imgHeight = decodeFile.getHeight();
			int length = imgWidth < imgHeight ? imgWidth : imgHeight;
			int x = 0;
			int y = 0;
			if (imgWidth > imgHeight) {// 横屏拍摄
				x = (imgWidth - imgHeight) / 2;
				y = 0;
			} else {
				x = 0;
				y = (imgHeight - imgWidth) / 2;
			}
			// 按照600宽缩放
			float scaleFactor = targetWidth / length;
			// // // System.out.println("缩放比："+scaleFactor);
			Matrix m = new Matrix();
			m.postScale(scaleFactor, scaleFactor);
			// m.postRotate(90);//旋转90度（）
			Bitmap scaledImage = Bitmap.createBitmap(decodeFile, x, y, length,
					length, m, false);
			FileOutputStream outStream = new FileOutputStream(sourceFile);
			Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
			int quality = 90;
			scaledImage.compress(format, quality, outStream);// 把bitmap用format格式，quality质量，输出到outStream流
			decodeFile.recycle();
			return scaledImage;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	// 通过uri获取图片bitmap
	public static Bitmap getBitmapFromUri(Uri uri, Context context) {
		try {
			// 读取uri所在的图片
			Bitmap bitmap = MediaStore.Images.Media.getBitmap(
					context.getContentResolver(), uri);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// bitmap图片转换圆角（一般情况下参数二填30像素就行了）
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getWidth());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		bitmap = null;
		return output;
	}

	// 获取当前gps位置(原始坐标)通过SharedPreferences获取位置信息
	public static double[] getCurrentLocation(Context context) {

		double[] location = new double[2];
		SharedPreferences sp = context.getSharedPreferences("blytConfig",
				Context.MODE_PRIVATE);
		// 其实这里的当前位置是上一次应用开启时获取的gps坐标
		String gpsInfo = sp.getString("lastlocation", "");
		String[] locationStr = new String[2];
		if (!"".equals(gpsInfo)) {
			locationStr = gpsInfo.split("-");
		}
		if (locationStr.length == 2) {
			// // System.out.println(locationStr[0] + "数据" + locationStr[1]);
			if (locationStr[0] == null) {
				locationStr[0] = "39.9";
			}
			if (locationStr[1] == null) {
				locationStr[1] = "116.4";
			}
			double currentLat = Double.parseDouble(locationStr[0]);
			double currentLon = Double.parseDouble(locationStr[1]);
			location[0] = currentLat;
			location[1] = currentLon;
		}
		return location;
	}

	// 压缩图片时候获取压缩属性
	public static BitmapFactory.Options getOptions(Context context) {

		BitmapFactory.Options opt = new BitmapFactory.Options();
		// 这个isjustdecodebounds很重要
		opt.inJustDecodeBounds = true;

		// 获取到这个图片的原始宽度和高度
		int picWidth = opt.outWidth;
		int picHeight = opt.outHeight;

		// 获取屏的宽度和高度
		WindowManager windowManager = ((Activity) context).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int screenWidth = display.getWidth();
		int screenHeight = display.getHeight();

		// isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2，图片即为原图1/4
		opt.inSampleSize = 2;
		// 根据屏的大小和图片大小计算出缩放比例
		if (picWidth > picHeight) {
			if (picWidth > screenWidth)
				opt.inSampleSize = picWidth / screenWidth;
		} else {
			if (picHeight > screenHeight)
				opt.inSampleSize = picHeight / screenHeight;
		}
		// 这次再真正地生成一个有像素的，经过缩放了的bitmap
		opt.inJustDecodeBounds = false;

		return opt;
	}

	// 获取listview的条目高度
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			// listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	// 获取手机系统版本号
	public static String getAndroidSDKVersion() {
		String version = null;
		try {
			version = android.os.Build.VERSION.RELEASE;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return version;
	}

	/**
	 * 判断是否是不兼容的版本
	 *
	 * @return true是非兼容版本 false是兼容版本
	 */
	public static boolean IsErrorSDKVersion() {
		// String androidSDKVersion="4.0.4";
		String androidSDKVersion = getAndroidSDKVersion();
		if (androidSDKVersion != null) {
			String[] split = androidSDKVersion.split("\\.");
			if (split.length == 3) {
				if (Integer.parseInt(split[0]) > 4) {

					return true;
				} else if (Integer.parseInt(split[0]) == 4) {
					if (Integer.parseInt(split[1]) > 0) {
						return true;
					} else {
						if (Integer.parseInt(split[2]) > 3) {
							return true;
						}
					}
				} else {
					return false;
				}

			} else {
				return true;
			}
		}

		return false;
	}

	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	/**
	 * 获取手机号码
	 * @return
	 */
	public static String getPhoneNumber(Context context)
	{
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getLine1Number();
	}
    
    /**
     * 获取手机IMEI码
     */
    public static String getPhoneIMEI(Context cxt) {
        TelephonyManager tm = (TelephonyManager) cxt
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }
	/**
	 * 获取手机IMSI
	 * @return
	 */
	public static String getDeviceIMSI(Context context)
	{
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getSubscriberId();
	}
    
    /**
     * 获取手机系统SDK版本
     * 
     * @return 如API 17 则返回 17
     */
    public static int getSDKVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 获取系统版本
     * 
     * @return 形如2.3.3
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 调用系统发送短信
     */
    public static void sendSMS(Context cxt, String smsBody) {
        Uri smsToUri = Uri.parse("smsto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", smsBody);
        cxt.startActivity(intent);
    }
    /**
	 * 调用系统打电话
	 * 
	 * @param activity
	 * @param telString
	 */
	public static boolean makeCall(Activity activity, String telString)
	{
		try
		{
			Intent intent = new Intent(Intent.ACTION_CALL);
			intent.setData(Uri.parse("tel:" + telString));
			activity.startActivity(intent);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
//	public static boolean makeCall(Activity activity, String telString)
//	{
//		try
//		{
//			Intent intent = new Intent(Intent.ACTION_VIEW);
//			intent.setData(Uri.parse("tel:" + telString));
//			activity.startActivity(intent);
//			return true;
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//		
//		return false;
//	}
    
    /**
     * 判断网络是否连接
     */
    public static boolean checkNet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null;// 网络是否连接
    }
    
	/**
	 * 网络是否可用
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context)
	{
		ConnectivityManager mConnMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = mConnMan.getActiveNetworkInfo();
		if (info == null) { return false; }
		return info.isConnected();
	}

//    /**
//     * 仅wifi联网功能是否开启
//     */
//    public static boolean checkOnlyWifi(Context context) {
//        if (PreferenceHelper.readBoolean(context,
//                KJConfig.SETTING_FILE, KJConfig.ONLY_WIFI)) {
//            return isWiFi(context);
//        } else {
//            return true;
//        }
//    }

    /**
     * 判断是否为wifi联网
     */
    public static boolean isWiFi(Context cxt) {
        ConnectivityManager cm = (ConnectivityManager) cxt
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // wifi的状态：ConnectivityManager.TYPE_WIFI
        // 3G的状态：ConnectivityManager.TYPE_MOBILE
        State state = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        return State.CONNECTED == state;
    }



	/**
	 * 隐藏输入法
	 * @param a
	 */
	public static void hideInputMethod(Activity a)
	{
		InputMethodManager imm = (InputMethodManager) a.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null)
		{
			View focus = a.getCurrentFocus();
			if (focus != null)
			{
				IBinder binder = focus.getWindowToken();
				if (binder != null)
				{
					imm.hideSoftInputFromWindow(binder, InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		}
		
	}
	
    /**
     * need < uses-permission android:name =“android.permission.GET_TASKS” />
     * 判断是否前台运行
     */
    public static boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName componentName = taskList.get(0).topActivity;
            if (componentName != null && componentName.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }
	
    
    /**
     * 判断当前应用程序是否后台运行
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context
                    .getPackageName())) {
                if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    // 后台运行
                    return true;
                } else {
                    // 前台运行
                    return false;
                }
            }
        }
        return false;
    }

	/**
	 * 指定程序是否在运行
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isPackageRunning(Context context, String packageName)
	{
		
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = manager.getRunningTasks(Integer.MAX_VALUE);
		for (RunningTaskInfo taskInfo : list)
		{
			
			if (taskInfo.topActivity.getPackageName().equals(packageName)
					|| taskInfo.baseActivity.getPackageName().equals(packageName)) {
			
			return true; }
		}
		
		return false;
	}
    
	/**
	 * app是否存在
	 * @param a
	 * @param packageName
	 * @return
	 */
	public static boolean isAppExists(Activity a, String packageName)
	{
		
		PackageInfo packageInfo;
		try
		{
			packageInfo = a.getPackageManager().getPackageInfo(packageName, 0);
			
		}
		catch (NameNotFoundException e)
		{
			packageInfo = null;
			e.printStackTrace();
		}
		
		return packageInfo == null ? false : true;
	}
    
    
    /**
     * 判断手机是否处理睡眠
     */
    public static boolean isSleeping(Context context) {
        KeyguardManager kgMgr = (KeyguardManager) context
                .getSystemService(Context.KEYGUARD_SERVICE);
        boolean isSleeping = kgMgr.inKeyguardRestrictedInputMode();
        return isSleeping;
    }

    /**
     * 安装apk
     * 
     * @param context
     * @param file
     */
    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("application/vnd.android.package-archive");
        intent.setData(Uri.fromFile(file));
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 获取当前应用程序的版本名称
     */
    public static String getAppVersion(Context context) {
        String version = "0";
        try {
            version = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            throw new FzException(SystemUtils.class.getName()
                    + "the application not found");
        }
        return version;
    }

	/**
	 * 获取当前软件版本号
	 * @param ctx
	 * @return
	 */
	public static String getVersionCode(Context ctx)
	{
		PackageManager pm = ctx.getPackageManager();
		PackageInfo pi;
		try
		{
			pi = pm.getPackageInfo(ctx.getPackageName(), 0);
			
			return String.valueOf(pi.versionCode);
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
    
    
    /**
     * 回到home，后台运行
     */
    public static void goHome(Context context) {
        Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        context.startActivity(mHomeIntent);
    }

    /**
     * 获取应用签名
     * 
     * @param context
     * @param pkgName
     */
    public static String getSign(Context context, String pkgName) {
        try {
            PackageInfo pis = context.getPackageManager()
                    .getPackageInfo(pkgName,
                            PackageManager.GET_SIGNATURES);
            return hexdigest(pis.signatures[0].toByteArray());
        } catch (NameNotFoundException e) {
            throw new FzException(SystemUtils.class.getName() + "the "
                    + pkgName + "'s application not found");
        }
    }

    /**
     * 将签名字符串转换成需要的32位签名
     */
    private static String hexdigest(byte[] paramArrayOfByte) {
        final char[] hexDigits = { 48, 49, 50, 51, 52, 53, 54, 55,
                                  56, 57, 97, 98, 99, 100, 101, 102 };
        try {
            MessageDigest localMessageDigest = MessageDigest
                    .getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            char[] arrayOfChar = new char[32];
            for (int i = 0, j = 0;; i++, j++) {
                if (i >= 16) {
                    return new String(arrayOfChar);
                }
                int k = arrayOfByte[i];
                arrayOfChar[j] = hexDigits[(0xF & k >>> 4)];
                arrayOfChar[++j] = hexDigits[(k & 0xF)];
            }
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 获取设备的可用内存大小
     * 
     * @param cxt
     *            应用上下文对象context
     * @return 当前内存大小
     */
    public static int getDeviceUsableMemory(Context cxt) {
        ActivityManager am = (ActivityManager) cxt
                .getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo mi = new MemoryInfo();
        am.getMemoryInfo(mi);
        // 返回当前系统的可用内存
        return (int) (mi.availMem / (1024 * 1024));
    }

    /**
     * 清理后台进程与服务
     * 
     * @param cxt
     *            应用上下文对象context
     * @return 被清理的数量
     */
    public static int gc(Context cxt) {
        long i = getDeviceUsableMemory(cxt);
        int count = 0; // 清理掉的进程数
        ActivityManager am = (ActivityManager) cxt
                .getSystemService(Context.ACTIVITY_SERVICE);
        // 获取正在运行的service列表
        List<RunningServiceInfo> serviceList = am
                .getRunningServices(100);
        if (serviceList != null)
            for (RunningServiceInfo service : serviceList) {
                if (service.pid == android.os.Process.myPid())
                    continue;
                try {
                    android.os.Process.killProcess(service.pid);
                    count++;
                } catch (Exception e) {
                    e.getStackTrace();
                    continue;
                }
            }

        // 获取正在运行的进程列表
        List<RunningAppProcessInfo> processList = am
                .getRunningAppProcesses();
        if (processList != null)
            for (RunningAppProcessInfo process : processList) {
                // 一般数值大于RunningAppProcessInfo.IMPORTANCE_SERVICE的进程都长时间没用或者空进程了
                // 一般数值大于RunningAppProcessInfo.IMPORTANCE_VISIBLE的进程都是非可见进程，也就是在后台运行着
                if (process.importance > RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                    // pkgList 得到该进程下运行的包名
                    String[] pkgList = process.pkgList;
                    for (String pkgName : pkgList) {
                        L.i(SystemUtils.class.getName(),"======正在杀死包名：" + pkgName);
                        try {
                            am.killBackgroundProcesses(pkgName);
                            count++;
                        } catch (Exception e) { // 防止意外发生
                            e.getStackTrace();
                            continue;
                        }
                    }
                }
            }
        L.i(SystemUtils.class.getName(),"清理了" + (getDeviceUsableMemory(cxt) - i)
                + "M内存");
        return count;
    }
    
    
	/**
	 * 强制退出系统（不推荐使用）
	 * @param a
	 */
	public static void quitApplication(Activity a)
	{
		a.finish();
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}
    
	
	/**
	 * 解析程序启动activity的名称
	 * 
	 * @param a
	 * @param packageName
	 * @return
	 */
	public static String parserLauncherActivityName(Activity a, String packageName)
	{
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setPackage(packageName);
		List<ResolveInfo> appList = a.getPackageManager().queryIntentActivities(intent, 0);
		if (appList == null || appList.size() <= 0) return "";
		
		return appList.get(0).activityInfo.name;
	}
	
	/**
	 * 获取所有的app列表
	 * 
	 * @param a
	 * @return
	 */
	public static List<ResolveInfo> parserAllAppList(Activity a)
	{
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		return a.getPackageManager().queryIntentActivities(intent, 0);
	}
	
	/**
	 * 获取外网ip
	 * 
	 * @return
	 */
	public static String getExternalIpAddress()
	{
		try
		{
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
			{
				NetworkInterface netIntf = en.nextElement();
				for (Enumeration<InetAddress> enumIp = netIntf.getInetAddresses(); enumIp.hasMoreElements();)
				{
					InetAddress inetAddress = enumIp.nextElement();
					if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address))
					{
						String ip = inetAddress.getHostAddress().toString();
						Log.d("SystemUtils", "ip" + ip);
						return ip;
					}
				}
			}
		}
		catch (SocketException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	/**
	 * 新建uuid
	 * 
	 * @return
	 */
	public static String newRandomUUID()
	{
		String uuidRaw = UUID.randomUUID().toString();
		return uuidRaw.replaceAll("-", "");
	}
	
}
