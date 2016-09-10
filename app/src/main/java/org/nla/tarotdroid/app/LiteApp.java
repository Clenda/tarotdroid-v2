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
package org.nla.tarotdroid.app;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.nla.tarotdroid.R;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 */
@ReportsCrashes(
		formKey="",
		mailTo = "tarotdroid@gmail.com",
		customReportContent = { ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME, ReportField.BRAND, ReportField.PHONE_MODEL, ReportField.ANDROID_VERSION, ReportField.SHARED_PREFERENCES, ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.LOGCAT },
		mode = ReportingInteractionMode.DIALOG,
		resToastText = R.string.crash_toast_text,
		resDialogText = R.string.crash_dialog_text,
		resDialogIcon = android.R.drawable.ic_dialog_info,
		resDialogTitle = R.string.crash_dialog_title,
		resDialogOkToast = R.string.crash_dialog_ok_toast
)
public class LiteApp extends BaseApp {

	/* (non-Javadoc)
	 * @see BaseApp#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		ACRA.init(this);
	}
	
	/* (non-Javadoc)
	 * @see ITarotDroidApp#getAppName()
	 */
	@Override
	public String getAppLogTag() {
		return "TarotDroidLite";
	}
    
	/* (non-Javadoc)
	 * @see ITarotDroidApp#getAppName()
	 */
	@Override
	public String getAppName() {
		return "TarotDroid Lite";
	}
	
	/* (non-Javadoc)
	 * @see ITarotDroidApp#getFlurryApplicationId()
	 */
	public String getFlurryApplicationId() {
		return "XXX";
	}

    /* (non-Javadoc)
     * @see ITarotDroidApp#isAppLimited()
     */
    @Override
    public boolean isAppLimited() {    	
    	//return !this.getPackageName().toLowerCase().contains("full");
    	return true;
    }

	/* (non-Javadoc)
	 * @see ITarotDroidApp#getGooglePlayUrl()
	 */
	@Override
	public String getGooglePlayUrl() {
		return this.getString(R.string.urlGooglePlayLiteApp);
	}
}