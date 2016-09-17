package org.nla.tarotdroid.cloud;

import com.google.common.base.Predicate;

import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.clientmodel.RestGameSet;

import java.util.List;

public class GameSetToDownloadPredicate implements Predicate<RestGameSet> {
	
	private List<GameSet> toCompare;
	
	public GameSetToDownloadPredicate(List<GameSet> toCompare) {
		this.toCompare = toCompare;
	}
	
	@Override
	public boolean apply(RestGameSet restGameSet) {
		
		for(GameSet gameSet : this.toCompare) {
			if (restGameSet.getUuid().equals(gameSet.getUuid())) {
				return false;
			}
		}
		
		return true;
	}
}