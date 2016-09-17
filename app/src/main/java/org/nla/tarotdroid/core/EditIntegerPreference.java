package org.nla.tarotdroid.core;

import android.content.Context;
import android.support.v7.preference.EditTextPreferenceFix;
import android.util.AttributeSet;

// http://code.google.com/p/android-eco-tools/source/browse/trunk/ecoLamp/src/com/android/custom/EditIntegerPreference.java?spec=svn83&r=83
// TODO Fix this broken appcompat stuff, check https://github.com/Gericop/Android-Support-Preference-V7-Fix
public class EditIntegerPreference extends EditTextPreferenceFix {

	public EditIntegerPreference(final Context context) {
		super(context);
	}

	public EditIntegerPreference(final Context context, final AttributeSet attrs) {
		super(context, attrs);
	}

	public EditIntegerPreference(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public String getText() {
		try {
			return String.valueOf(getSharedPreferences().getInt(getKey(), 0));
		}
		catch (Exception e) {
			return "0";
		}
	}

	@Override
	public void setText(final String text) {
		getSharedPreferences().edit().putInt(getKey(), Integer.parseInt(text)).commit();
	}

	@Override
	protected void onSetInitialValue(final boolean restoreValue, final Object defaultValue) {
		if (restoreValue) {
			getEditText().setText(this.getText());
		}
		else {
			super.onSetInitialValue(restoreValue, defaultValue);
		}
	}
}
