/*
	This file is part of the Android application TarotDroid.
 	
	TarotDroid is free software: you can redistribute it and/or modify
 	it under the terms of the GNU General Public License as published by
 	the Free Software Foundation, either version 3 of the License, or
 	(at your option) any later version.
 	
 	TarotDroid is distributed in the hope that it will be useful,
 	but WITHOUT ANY WARRANTY; without even the implied warranty of
 	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 	GNU General Public License for more details.
 	
 	You should have received a copy of the GNU General Public License
 	along with TarotDroid. If not, see <http://www.gnu.org/licenses/>.
 */
package org.nla.tarotdroid;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;

import org.nla.tarotdroid.dal.IDalService;
import org.nla.tarotdroid.helpers.BluetoothHelper;
import org.nla.tarotdroid.model.TarotDroidUser;
import org.nla.tarotdroid.ui.tasks.LoadDalTask;

import java.util.Map;
import java.util.UUID;

public interface ITarotDroidApp {
	
	String getAppName();
	String getAppVersion();
	String getAppPackage();
	String getAppLogTag();
	String getFlurryApplicationId();
	String getCloudDns();
	String getFacebookCloudUrl();
	Resources getResources();
	ContentResolver getContentResolver();
	UUID getUuid();
	String getServiceName();
	IDalService getDalService();

    void setDalService(IDalService dalService);

    long getLastLaunchTimestamp();

    BluetoothHelper getBluetoothHelper();

	LoadDalTask getLoadDalTask();

    TarotDroidUser getTarotDroidUser();

    void setTarotDroidUser(TarotDroidUser tarotDroidUser);

	Map<String, Integer> getNotificationIds();

	SQLiteDatabase getSQLiteDatabase();

	String getGooglePlayUrl();
}
