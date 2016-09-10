package org.nla.tarotdroid.biz;

import org.nla.tarotdroid.biz.enums.KingType;

import java.io.Serializable;

public class King implements Serializable {

    private static final long serialVersionUID = -2271742674453366926L;

	public static final King HEART = new King();
	public static final King DIAMOND = new King();
	public static final King SPADE = new King();
	public static final King CLUB = new King();

	static {
	    King.HEART.setKingType(KingType.Hearts);
	    King.DIAMOND.setKingType(KingType.Diamonds);
	    King.SPADE.setKingType(KingType.Spades);
	    King.CLUB.setKingType(KingType.Clubs);
	}
	
	public static King valueOf(final String kingTypeAsString) {
		if (kingTypeAsString == null  || kingTypeAsString.equals("")) {
			return null;
		}
		
		KingType kingType = KingType.valueOf(kingTypeAsString);
		
		switch(kingType) {
    		case Hearts:
    			return King.HEART;
    		case Spades:
    			return King.SPADE;
    		case Clubs:
    			return King.CLUB;
    		case Diamonds:
    			return King.DIAMOND;
            default:
                return null;
		}
	}

	private KingType king;
	private String label;
	private String image;

	public KingType getKingType() {
		return this.king;
	}
	
	public void setKingType(final KingType kingType) {
		this.king = kingType;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

	public String getImage() {
		return this.image;
	}

	public void setImage(final String image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return this.label != null ? this.label : this.king.toString();
	}
}
