
package org.nla.tarotdroid.biz;

import org.nla.tarotdroid.biz.computers.BaseGameScoresComputer;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseGame extends PersistableBusinessObject implements Serializable {

    @CloudField(cloudify = false)
    private static final long serialVersionUID = 3092786288170636063L;

    protected GameSetParameters gameSetParameters;
    protected PlayerList players;
    @CloudField(cloudify = false)
    protected BaseGameScoresComputer computer;
    private PlayerList deadPlayers;
    private Player dealer;
    @CloudField(cloudify = false)
    private Map<String, Player> playersMap;
    private int index;
    private int highestGameIndex;
    private Date creationTs;

    public BaseGame() {
        super();
        this.players = new PlayerList();
        this.deadPlayers = new PlayerList();
        this.playersMap = new HashMap<String, Player>();
        this.creationTs = new Date(System.currentTimeMillis());
    }


    public GameSetParameters getGameSetParameters() {
        return this.gameSetParameters;
    }


    public void setGameSetParameters(final GameSetParameters gameSetParameters) {
        this.gameSetParameters = gameSetParameters;
    }


    public int getIndex() {
        return this.index;
    }


    public void setIndex(final int index) {
        this.index = index;
    }


    public int getReverseIndex() {
        return this.highestGameIndex - this.index;
    }


    public PlayerList getPlayers() {
        return this.players;
    }

    public void setPlayers(final PlayerList players) {
        if (players == null) {
            throw new IllegalArgumentException("players=" + players);
        }

        this.players = players;
        for (Player player : this.players) {
            this.playersMap.put(player.getName().toLowerCase(), player);
        }
    }

    public PlayerList getDeadPlayers() {
        return this.deadPlayers;
    }

    public final void setDeadPlayers(final PlayerList players) {
        if (players == null) {
            this.deadPlayers = new PlayerList();
        } else {
            this.deadPlayers = players;
        }

        for (Player player : this.players) {
            this.playersMap.put(player.getName().toLowerCase(), player);
        }
    }

    public Player getDeadPlayer() {
        if (this.deadPlayers != null && this.deadPlayers.size() >= 1) {
            return this.deadPlayers.get(1);
        }
        return null;
    }

    public void setDeadPlayer(final Player player) {
        if (player != null) {
            PlayerList players = new PlayerList();
            players.add(player);
            this.setDeadPlayers(players);
        }
    }


    public Player getPlayerForName(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("name is null");
        }
        return this.playersMap.get(name.toLowerCase());
    }


    public Player getDealer() {
        return this.dealer;
    }


    public void setDealer(final Player dealer) {
        this.dealer = dealer;
    }


    public int getHighestGameIndex() {
        return this.highestGameIndex;
    }

    public void setHighestGameIndex(final int highestGameIndex) {
        if (highestGameIndex < 1) {
            throw new IllegalArgumentException("highestGameIndex<1=" + highestGameIndex);
        }
        this.highestGameIndex = highestGameIndex;
    }

    public boolean isLatestGame() {
        return this.highestGameIndex == this.index;
    }

    public Date getCreationTs() {
        return this.creationTs;
    }


    public void setCreationTs(final Date creationTs) {
        this.creationTs = creationTs;
    }


    public BaseGameScoresComputer getComputer() {
        return this.computer;
    }


    public boolean isWinner(final Player player) {
        if (player == null) {
            throw new IllegalArgumentException("player is null");
        }
        return this.getGameScores().isWinner(player);
    }


    public abstract GameScores getGameScores();
}
