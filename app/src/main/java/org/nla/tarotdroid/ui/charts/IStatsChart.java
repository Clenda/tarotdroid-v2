package org.nla.tarotdroid.ui.charts;

import android.content.Context;
import android.content.Intent;

import org.nla.tarotdroid.helpers.AuditHelper;
import org.nla.tarotdroid.helpers.LocalizationHelper;

public interface IStatsChart {

	String NAME = "name";
	
	String DESC = "desc";

	String getName();

	String getDescription();
	
	AuditHelper.EventTypes getAuditEventType();

	Intent execute(Context context, LocalizationHelper localizationHelper);
}
