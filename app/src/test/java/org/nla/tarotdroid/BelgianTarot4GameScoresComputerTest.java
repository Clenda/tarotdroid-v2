package org.nla.tarotdroid;

import org.junit.Test;
import org.nla.tarotdroid.biz.BelgianTarot4Game;
import org.nla.tarotdroid.biz.GameScores;
import org.nla.tarotdroid.biz.GameSetParameters;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.PlayerList;

import static org.junit.Assert.assertEquals;

public class BelgianTarot4GameScoresComputerTest {

    private BelgianTarot4Game createGame() {
        // players creation
        Player pm = new Player();
        pm.setName("PM");

        Player guillaume = new Player();
        guillaume.setName("Guillaume");

        Player arthur = new Player();
        arthur.setName("Arthur");

        Player ludo = new Player();
        ludo.setName("Ludo");

        // player list creation
        PlayerList players = new PlayerList();
        players.add(pm);
        players.add(ludo);
        players.add(arthur);
        players.add(guillaume);

        // game creation
        BelgianTarot4Game game = new BelgianTarot4Game();
        game.setPlayers(players);
        game.setFirst(pm);
        game.setSecond(guillaume);
        game.setThird(arthur);
        game.setFourth(ludo);

        return game;
    }

    @Test
    public final void testComputeScoreWithoutException() {
        BelgianTarot4Game game = this.createGame();
        game.setGameSetParameters(new GameSetParameters());
        GameScores scores = game.getGameScores();

        assertEquals(200, scores.getIndividualResult(game.getPlayerForName("PM")));
        assertEquals(100, scores.getIndividualResult(game.getPlayerForName("Guillaume")));
        assertEquals(-100, scores.getIndividualResult(game.getPlayerForName("Arthur")));
        assertEquals(-200, scores.getIndividualResult(game.getPlayerForName("Ludo")));
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testComputeScoreWithFirstPlayerNull() {
        BelgianTarot4Game game = this.createGame();
        game.setFirst(null);
        game.getGameScores();
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testComputeScoreWithSecondPlayerNull() {
        BelgianTarot4Game game = this.createGame();
        game.setSecond(null);
        game.getGameScores();
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testComputeScoreWithThirdPlayerNull() {
        BelgianTarot4Game game = this.createGame();
        game.setThird(null);
        game.getGameScores();
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testComputeScoreWithFourthPlayerNull() {
        BelgianTarot4Game game = this.createGame();
        game.setFourth(null);
        game.getGameScores();
    }
}
