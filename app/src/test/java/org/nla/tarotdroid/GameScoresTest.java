package org.nla.tarotdroid;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nla.tarotdroid.biz.GameScores;
import org.nla.tarotdroid.biz.Player;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class GameScoresTest {

    private Player nicol;
    private Player jk;
    private Player ludas;
    private Player guillaume;
    private Player arthur;

    @Before
    public void setUp() throws Exception {
        this.nicol = new Player("NicoL");
        this.jk = new Player("JK");
        this.arthur = new Player("Arthur");
        this.ludas = new Player("Ludas");
        this.guillaume = new Player("Guillaume");
    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void testGameScores() {
        GameScores gameScores = new GameScores();
        assertNotNull("instance of GameScores must not be null", gameScores);
    }


    @Test
    public void testAddScore() {
        GameScores gameScores = new GameScores();
        gameScores.addScore(this.nicol, 200);
        assertTrue("GameScores should contain a score for NicoL",
                   gameScores.getResults().containsKey(this.nicol));
        assertEquals("NicoL should have a score of 200",
                     200,
                     gameScores.getResults().get(this.nicol).intValue());
    }


    @Test(expected = IllegalArgumentException.class)
    public void testAddScoreForNullPlayer() {
        GameScores gameScores = new GameScores();
        gameScores.addScore(null, 200);
        fail("An exception should have been thrown...");
    }


    @Test
    public void testUpdateScore() {
        GameScores gameScores = new GameScores();
        gameScores.addScore(this.nicol, 200);
        gameScores.updateScore(this.nicol, 100);
        assertTrue("GameScores should contain a score for NicoL",
                   gameScores.getResults().containsKey(this.nicol));
        assertEquals("NicoL should have an updated score of 300",
                     300,
                     gameScores.getResults().get(this.nicol).intValue());
    }


    @Test(expected = IllegalArgumentException.class)
    public void testUpdateScoreForNullPlayer() {
        GameScores gameScores = new GameScores();
        gameScores.updateScore(null, 100);
        fail("An exception should have been thrown...");
    }


    @Test
    public void testGetIndividualResult() {
        GameScores gameScores = new GameScores();
        gameScores.addScore(this.nicol, 200);
        assertEquals("NicoL should have a score of 200",
                     200,
                     gameScores.getIndividualResult(this.nicol));
    }


    @Test
    public void testGetIndividualResultForUnknownPlayer() {
        GameScores gameScores = new GameScores();
        assertEquals(
                "NicoL isn't in the GameScores, he should hence be considered to have a score of 0",
                0,
                gameScores.getIndividualResult(this.nicol));
    }


    @Test(expected = IllegalArgumentException.class)
    public void testGetIndividualResultForNullPlayer() {
        GameScores gameScores = new GameScores();
        gameScores.getIndividualResult(null);
        fail("An exception should have been thrown...");
    }


    @Test
    public void testGetResults() {
        GameScores gameScores = new GameScores();
        assertNotNull("Results should never be null", gameScores.getResults());
        gameScores.addScore(this.nicol, 200);
        assertNotNull("Results should never be null", gameScores.getResults());
    }


    @Test
    public void testGetMinScore() {
        GameScores gameScores = new GameScores();
        gameScores.addScore(this.nicol, 200);
        gameScores.addScore(this.ludas, 100);
        gameScores.addScore(this.guillaume, 300);
        gameScores.addScore(this.arthur, 400);
        gameScores.addScore(this.jk, 500);
        assertEquals("Ludas should have the min score", 100, gameScores.getMinScore());
    }


    @Test
    public void testGetMaxScore() {
        GameScores gameScores = new GameScores();
        gameScores.addScore(this.nicol, 200);
        gameScores.addScore(this.ludas, 100);
        gameScores.addScore(this.guillaume, 300);
        gameScores.addScore(this.arthur, 400);
        gameScores.addScore(this.jk, 500);
        assertEquals("Jk should have the max score", 500, gameScores.getMaxScore());
    }


    @Test
    public void testValues() {
        GameScores gameScores = new GameScores();
        gameScores.addScore(this.nicol, 200);
        gameScores.addScore(this.ludas, 100);
        gameScores.addScore(this.guillaume, 300);
        gameScores.addScore(this.arthur, 400);
        gameScores.addScore(this.jk, 500);
        Collection<Integer> values = gameScores.values();
        assertTrue("Collection shoud contain 100", values.contains(100));
        assertTrue("Collection shoud contain 200", values.contains(200));
        assertTrue("Collection shoud contain 300", values.contains(300));
        assertTrue("Collection shoud contain 400", values.contains(400));
        assertTrue("Collection shoud contain 500", values.contains(500));
    }


    @Test
    public void testIsWinner() {
        GameScores gameScores = new GameScores();
        gameScores.addScore(this.nicol, 200);
        gameScores.addScore(this.ludas, 100);
        gameScores.addScore(this.guillaume, 300);
        gameScores.addScore(this.arthur, 400);
        gameScores.addScore(this.jk, 500);
        assertTrue("JK should be the winner", gameScores.isWinner(this.jk));
    }


    @Test
    public void testIsWinnerWithEqualRanks() {
        GameScores gameScores = new GameScores();
        gameScores.addScore(this.nicol, 200);
        gameScores.addScore(this.ludas, 500);
        gameScores.addScore(this.guillaume, 300);
        gameScores.addScore(this.arthur, 400);
        gameScores.addScore(this.jk, 500);
        assertTrue("JK should be the winner", gameScores.isWinner(this.jk));
        assertTrue("Ludas should be the winner", gameScores.isWinner(this.ludas));
    }
}