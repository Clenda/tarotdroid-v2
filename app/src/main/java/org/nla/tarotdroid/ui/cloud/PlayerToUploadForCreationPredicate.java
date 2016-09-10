package org.nla.tarotdroid.ui.cloud;

import com.google.common.base.Predicate;

import org.nla.tarotdroid.biz.Player;

/**
 * Predicate to figure which players to upload t the cloud.
 */
public class PlayerToUploadForCreationPredicate implements Predicate<Player> {
	
	/* (non-Javadoc)
	 * @see ch.lambdaj.function.matcher.Predicate#apply(java.lang.Object)
	 */
	@Override
	public boolean apply(Player player) {
		
		if (player.getSyncTimestamp() == null && player.getSyncAccount() == null) {
			return true;
		}
		else {
			return false;
		}
	}
}