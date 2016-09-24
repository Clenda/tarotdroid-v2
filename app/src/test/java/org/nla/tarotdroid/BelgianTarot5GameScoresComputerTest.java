package org.nla.tarotdroid;

import org.junit.Test;
import org.nla.tarotdroid.biz.BelgianTarot5Game;
import org.nla.tarotdroid.biz.GameScores;
import org.nla.tarotdroid.biz.GameSetParameters;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.PlayerList;

import static org.junit.Assert.assertEquals;


public class BelgianTarot5GameScoresComputerTest {

    private BelgianTarot5Game createGame() {
        // players creation
        Player pm = new Player();
        pm.setName("PM");

        Player guillaume = new Player();
        guillaume.setName("Guillaume");

        Player arthur = new Player();
        arthur.setName("Arthur");

        Player ludo = new Player();
        ludo.setName("Ludo");

        Player nico = new Player();
        nico.setName("Nico");

        // player list creation
        PlayerList players = new PlayerList();
        players.add(pm);
        players.add(ludo);
        players.add(arthur);
        players.add(guillaume);
        players.add(nico);

        // game creation
        BelgianTarot5Game game = new BelgianTarot5Game();
        game.setPlayers(players);
        game.setFirst(pm);
        game.setSecond(arthur);
        game.setThird(nico);
        game.setFourth(ludo);
        game.setFifth(guillaume);

        return game;
    }


    @Test
    public final void testComputeScoreWithoutException() {
        BelgianTarot5Game game = this.createGame();
        game.setGameSetParameters(new GameSetParameters());
        GameScores scores = game.getGameScores();

        assertEquals(200, scores.getIndividualResult(game.getPlayerForName("PM")));
        assertEquals(100, scores.getIndividualResult(game.getPlayerForName("Arthur")));
        assertEquals(0, scores.getIndividualResult(game.getPlayerForName("Nico")));
        assertEquals(-100, scores.getIndividualResult(game.getPlayerForName("Ludo")));
        assertEquals(-200, scores.getIndividualResult(game.getPlayerForName("Guillaume")));
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testComputeScoreWithFirstPlayerNull() {
        BelgianTarot5Game game = createGame();
        game.setFirst(null);
        game.getGameScores();
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testComputeScoreWithSecondPlayerNull() {
        BelgianTarot5Game game = createGame();
        game.setSecond(null);
        game.getGameScores();
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testComputeScoreWithThirdPlayerNull() {
        BelgianTarot5Game game = createGame();
        game.setThird(null);
        game.getGameScores();
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testComputeScoreWithFourthPlayerNull() {
        BelgianTarot5Game game = createGame();
        game.setFourth(null);
        game.getGameScores();
    }


    @Test(expected = IllegalArgumentException.class)
    public final void testComputeScoreWithFifthPlayerNull() {
        BelgianTarot5Game game = createGame();
        game.setFifth(null);
        game.getGameScores();
    }
}
