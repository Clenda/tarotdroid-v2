package org.nla.tarotdroid.ui.cloud;

import com.facebook.model.OpenGraphAction;

public interface PlayGraphAction extends OpenGraphAction {
	    // The meal object
	    public GameSetGraphObject getGame();
	    public void setGame(GameSetGraphObject meal);
	}