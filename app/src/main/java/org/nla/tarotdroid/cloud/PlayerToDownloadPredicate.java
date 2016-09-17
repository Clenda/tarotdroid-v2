package org.nla.tarotdroid.cloud;

import com.google.common.base.Predicate;

import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.clientmodel.RestPlayer;

import java.util.List;

/**
 * Predicate to figure which players to download from the cloud.
 */
public class PlayerToDownloadPredicate implements Predicate<RestPlayer> {
	
	/**
	 * Player to which every RestPlayer must be compared.
	 */
	private List<Player> toCompare;
	
	/**
	 * @param toCompare
	 */
	public PlayerToDownloadPredicate(List<Player> toCompare) {
		this.toCompare = toCompare;
	}
	
	/* (non-Javadoc)
	 * @see ch.lambdaj.function.matcher.Predicate#apply(java.lang.Object)
	 */
	@Override
	public boolean apply(RestPlayer restPlayer) {
		
		for(Player player : this.toCompare) {
			if (restPlayer.getUuid().equals(player.getUuid())) {
				return false;
			}
		}
		
		return true;
	}
}