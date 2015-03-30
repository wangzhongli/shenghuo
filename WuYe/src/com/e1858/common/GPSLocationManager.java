package com.e1858.common;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
/**
 * GPS定位
 * @author jiajia
 *
 */
public class GPSLocationManager {
	private double latitude = 0.0;
	private double longitude = 0.0;
	private LocationManager locationManager;
	private static GPSLocationManager mInstance;

	public GPSLocationManager(Context context) {
		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		location();
	}

	public static GPSLocationManager getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new GPSLocationManager(context);
		}
		return mInstance;
	}

	private void location() {
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗
		String provider = locationManager.getBestProvider(criteria, true); // 获取GPS信息
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Location location = locationManager.getLastKnownLocation(provider); // 通过GPS获取位置
			if (location != null) {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
			}
		} else {
			LocationListener locationListener = new LocationListener() {

				@Override
				public void onStatusChanged(String provider, int status,
						Bundle extras) {

				}

				@Override
				public void onProviderEnabled(String provider) {

				}

				@Override
				public void onProviderDisabled(String provider) {

				}

				@Override
				public void onLocationChanged(Location location) {
					if (location != null) {
						Log.e("Map",
								"Location changed : Lat: "
										+ location.getLatitude() + " Lng: "
										+ location.getLongitude());
					}
				}
			};
			locationManager
					.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
							1000, 0, locationListener);
			Location location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (location != null) {
				latitude = location.getLatitude(); // 经度
				longitude = location.getLongitude(); // 纬度

			
			}
		}
		Log.i("Latitude", "=" + latitude);
		Log.i("Longitude", "=" + longitude);
		setLatitude(latitude);
		setLongitude(longitude);
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}
