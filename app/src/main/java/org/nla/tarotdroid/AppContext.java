package org.nla.tarotdroid;

public class AppContext {

	private static TarotDroidApp app;
	
	private AppContext() {
	}

	public static TarotDroidApp getApplication() {
		return app;
	}

	public static void setApplication(TarotDroidApp application) {
		app = application;
	}	
}
