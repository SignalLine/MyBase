package com.rilin.lzy.mybase.util;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

public class GpsUtils {

	private Location location = null;
	private LocationManager locationManager = null;
	private Context context = null;

	/**
	 * 初始化
	 * 
	 * @param ctx
	 */
	public GpsUtils(Context ctx) {
		context = ctx;
		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		location = locationManager.getLastKnownLocation(getProvider());
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				2000, 10, locationListener);
	}

	/**
	 * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
	 * 
	 * @param context
	 * @return true 表示开启
	 */
	public static final boolean isOPen(final Context context) {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
		boolean gps = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
		boolean network = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (gps || network) {
			return true;
		}

		return false;
	}

	/**
	 * 强制帮用户打开GPS
	 * 
	 * @param context
	 */
	public static final void openGPS(Context context) {
		Intent intent = new Intent();
		intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			context.startActivity(intent);

		} catch (ActivityNotFoundException ex) {

			// The Android SDK doc says that the location settings activity
			// may not be found. In that case show the general settings.

			// General settings activity
			intent.setAction(Settings.ACTION_SETTINGS);
			try {
				context.startActivity(intent);
			} catch (Exception e) {
			}
		}
	}

	// 获取Location Provider
	private String getProvider() {
		// 构建位置查询条件
		Criteria criteria = new Criteria();
		// 查询精度：高
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// 是否查询海拨：否
		criteria.setAltitudeRequired(false);
		// 是否查询方位角 : 否
		criteria.setBearingRequired(false);
		// 是否允许付费：是
		criteria.setCostAllowed(true);
		// 电量要求：低
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		// 返回最合适的符合条件的provider，第2个参数为true说明 , 如果只有一个provider是有效的,则返回当前provider
		return locationManager.getBestProvider(criteria, true);
	}

	private LocationListener locationListener = new LocationListener() {
		// 位置发生改变后调用
		public void onLocationChanged(Location l) {
			if (l != null) {
				location = l;
			}
		}

		// provider 被用户关闭后调用
		public void onProviderDisabled(String provider) {
			location = null;
		}

		// provider 被用户开启后调用
		public void onProviderEnabled(String provider) {
			Location l = locationManager.getLastKnownLocation(provider);
			if (l != null) {
				location = l;
			}

		}

		// provider 状态变化时调用
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

	};

	public Location getLocation() {
		return location;
	}

	public void closeLocation() {
		if (locationManager != null) {
			if (locationListener != null) {
				locationManager.removeUpdates(locationListener);
				locationListener = null;
			}
			locationManager = null;
		}
	}

}
