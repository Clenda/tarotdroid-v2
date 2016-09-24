package org.nla.tarotdroid;

import org.junit.Test;
import org.nla.tarotdroid.biz.Bet;
import org.nla.tarotdroid.biz.GameScores;
import org.nla.tarotdroid.biz.GameSetParameters;
import org.nla.tarotdroid.biz.GameSetScores;
import org.nla.tarotdroid.biz.MapPlayersScores;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.PlayerList;
import org.nla.tarotdroid.biz.StandardTarot3Game;
import org.nla.tarotdroid.biz.StandardTarot5Game;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class GameSetScoresTest {


    private PlayerList create6Players() {

        // players creation
        Player pm = new Player();
        pm.setName("PM");

        Player guillaume = new Player();
        guillaume.setName("Guillaume");

        Player arthur = new Player();
        arthur.setName("Arthur");

        Player ludo = new Player();
        ludo.setName("Ludo");

        Player nicor = new Player();
        nicor.setName("NicoR");

        Player nicol = new Player();
        nicol.setName("NicoL");

        PlayerList players = new PlayerList();
        players.add(pm);
        players.add(ludo);
        players.add(arthur);
        players.add(guillaume);
        players.add(nicor);
        players.add(nicol);

        return players;
    }

    /**
     * Creates a list of 3 players.
     */
    private PlayerList create3Players() {

        // players creation
        Player pm = new Player();
        pm.setName("PM");

        Player guillaume = new Player();
        guillaume.setName("Guillaume");

        Player arthur = new Player();
        arthur.setName("Arthur");

        PlayerList players = new PlayerList();
        players.add(pm);
        players.add(arthur);
        players.add(guillaume);

        return players;
    }


    private GameSetParameters createCustomGameSetParameters() {
        GameSetParameters gameSetParameters = new GameSetParameters();
        gameSetParameters.setPriseBasePoints(20);
        gameSetParameters.setPriseRate(2);
        gameSetParameters.setGardeBasePoints(40);
        gameSetParameters.setGardeRate(4);
        gameSetParameters.setGardeSansBasePoints(80);
        gameSetParameters.setGardeSansRate(8);
        gameSetParameters.setGardeContreBasePoints(160);
        gameSetParameters.setGardeContreRate(16);
        gameSetParameters.setPoigneePoints(10);
        gameSetParameters.setDoublePoigneePoints(20);
        gameSetParameters.setTriplePoigneePoints(30);
        gameSetParameters.setMiseryPoints(10);
        gameSetParameters.setKidAtTheEndPoints(10);
        gameSetParameters.setAnnouncedAndSucceededChelemPoints(400);
        gameSetParameters.setAnnouncedAndFailedChelemPoints(-200);
        gameSetParameters.setNotAnnouncedButSucceededChelemPoints(200);
        gameSetParameters.setBelgianBaseStepPoints(100);
        return gameSetParameters;
    }


    @Test
    public final void testAddFirstGameScore() {

        // creation of a list of 6 players => one player is dead at each game
        PlayerList players = create6Players();

        // creation of a game set score
        GameSetScores gameSetScores = new GameSetScores();
        gameSetScores.setPlayers(players);

        // first game creation => won by zero points by attack
        // player 6 in list is dead, he should have zero points
        StandardTarot5Game game = new StandardTarot5Game();
        game.setGameSetParameters(createCustomGameSetParameters());
        game.setPlayers(players.subList(1, 5));
        game.setLeadingPlayer(players.get(1));
        game.setCalledPlayer(players.get(2));
        game.setBet(Bet.GARDE);
        game.setNumberOfOudlers(2);
        game.setPoints(41);
        gameSetScores.addGameScore(game.getGameScores());

        // GARDE passee de zero par attaque, appelant gagne 2 * 40 points
        assertEquals(80, gameSetScores.getIndividualResultsAtGameOfIndex(1, players.get(1)));
        assertEquals(40, gameSetScores.getIndividualResultsAtGameOfIndex(1, players.get(2)));
        assertEquals(-40, gameSetScores.getIndividualResultsAtGameOfIndex(1, players.get(3)));
        assertEquals(-40, gameSetScores.getIndividualResultsAtGameOfIndex(1, players.get(4)));
        assertEquals(-40, gameSetScores.getIndividualResultsAtGameOfIndex(1, players.get(5)));

        // le mort reste a zero
        assertEquals(0, gameSetScores.getIndividualResultsAtGameOfIndex(1, players.get(6)));
    }


    @Test
    public final void testAddTwoGameScores() {

        // creation of a list of 6 players => one player is dead at each game
        PlayerList players = create6Players();

        // creation of a game set score
        GameSetScores gameSetScores = new GameSetScores();
        gameSetScores.setPlayers(players);

        // first game creation => won by zero points by attack
        // player 6 in list is dead, he should have zero points
        StandardTarot5Game game1 = new StandardTarot5Game();
        game1.setGameSetParameters(createCustomGameSetParameters());
        game1.setPlayers(players.subList(1, 5));
        game1.setLeadingPlayer(players.get(1));
        game1.setCalledPlayer(players.get(2));
        game1.setBet(Bet.GARDE);
        game1.setNumberOfOudlers(2);
        game1.setPoints(41);
        gameSetScores.addGameScore(game1.getGameScores());

        // GARDE gagnee de zero par attaque, appelant gagne 2 * 40 points
        assertEquals(80, gameSetScores.getIndividualResultsAtGameOfIndex(1, players.get(1)));
        assertEquals(40, gameSetScores.getIndividualResultsAtGameOfIndex(1, players.get(2)));
        assertEquals(-40, gameSetScores.getIndividualResultsAtGameOfIndex(1, players.get(3)));
        assertEquals(-40, gameSetScores.getIndividualResultsAtGameOfIndex(1, players.get(4)));
        assertEquals(-40, gameSetScores.getIndividualResultsAtGameOfIndex(1, players.get(5)));
        assertEquals(0, gameSetScores.getIndividualResultsAtGameOfIndex(1, players.get(6)));

        // so far code was identical to testAddFirstGameScore()

        // second game creation => won by 0 points by attack
        // player 1 is dead, he should earn zero and remain at his first score => 80
        // player 6 now plays and is in the attack, he should have 40 points total
        StandardTarot5Game game2 = new StandardTarot5Game();
        game2.setGameSetParameters(createCustomGameSetParameters());
        game2.setPlayers(players.subList(2, 6));
        game2.setLeadingPlayer(players.get(2));
        game2.setCalledPlayer(players.get(6));
        game2.setBet(Bet.GARDE);
        game2.setNumberOfOudlers(2);
        game2.setPoints(41);
        gameSetScores.addGameScore(game2.getGameScores());

        // verification que le score � la fin du jeu 1 n'a pas changee
        assertEquals(80, gameSetScores.getIndividualResultsAtGameOfIndex(1, players.get(1)));
        assertEquals(40, gameSetScores.getIndividualResultsAtGameOfIndex(1, players.get(2)));
        assertEquals(-40, gameSetScores.getIndividualResultsAtGameOfIndex(1, players.get(3)));
        assertEquals(-40, gameSetScores.getIndividualResultsAtGameOfIndex(1, players.get(4)));
        assertEquals(-40, gameSetScores.getIndividualResultsAtGameOfIndex(1, players.get(5)));
        assertEquals(0, gameSetScores.getIndividualResultsAtGameOfIndex(1, players.get(6)));

        // verification des scores � la fin du jeu 2, notamment :
        //  - le joueur 1 est mort, il reste au m�me score
        //  - le joueur 6 vient de rentrer et �tait mort avant, son score est le m�me que le score du jeu 2
        //  - les autres ont �volu�
        assertEquals(80, gameSetScores.getIndividualResultsAtGameOfIndex(2, players.get(1)));
        assertEquals(120, gameSetScores.getIndividualResultsAtGameOfIndex(2, players.get(2)));
        assertEquals(-80, gameSetScores.getIndividualResultsAtGameOfIndex(2, players.get(3)));
        assertEquals(-80, gameSetScores.getIndividualResultsAtGameOfIndex(2, players.get(4)));
        assertEquals(-80, gameSetScores.getIndividualResultsAtGameOfIndex(2, players.get(5)));
        assertEquals(40, gameSetScores.getIndividualResultsAtGameOfIndex(2, players.get(6)));
    }


    @Test
    public final void testCreateFirstResults() {
        // creation of a list of 6 players => one player is dead at each game
        PlayerList players = create6Players();

        // creation of a game set score
        GameSetScores gameSetScores = new GameSetScores();
        gameSetScores.setPlayers(players);

        // first game creation => won by zero points by attack
        // player 6 in list is dead, he should have zero points
        StandardTarot5Game game1 = new StandardTarot5Game();
        game1.setGameSetParameters(createCustomGameSetParameters());
        game1.setPlayers(players.subList(1, 5));
        game1.setLeadingPlayer(players.get(1));
        game1.setCalledPlayer(players.get(2));
        game1.setBet(Bet.GARDE);
        game1.setNumberOfOudlers(2);
        game1.setPoints(41);

        // checks all 6 players have a consistent score
        MapPlayersScores firstResults = gameSetScores.createFirstResults(game1.getGameScores());
        assertEquals(80, firstResults.get(players.get(1)).intValue());
        assertEquals(40, firstResults.get(players.get(2)).intValue());
        assertEquals(-40, firstResults.get(players.get(3)).intValue());
        assertEquals(-40, firstResults.get(players.get(4)).intValue());
        assertEquals(-40, firstResults.get(players.get(5)).intValue());
        assertEquals(0, firstResults.get(players.get(6)).intValue());
    }

    @Test
    public final void testGetLastResults() {
        // creation of a list of 6 players => one player is dead at each game
        PlayerList players = create6Players();

        // creation of a game set score
        GameSetScores gameSetScores = new GameSetScores();
        gameSetScores.setPlayers(players);

        // first game creation => won by zero points by attack
        // player 6 in list is dead, he should have zero points
        StandardTarot5Game game1 = new StandardTarot5Game();
        game1.setGameSetParameters(createCustomGameSetParameters());
        game1.setPlayers(players.subList(1, 5));
        game1.setLeadingPlayer(players.get(1));
        game1.setCalledPlayer(players.get(2));
        game1.setBet(Bet.GARDE);
        game1.setNumberOfOudlers(2);
        game1.setPoints(41);
        gameSetScores.addGameScore(game1.getGameScores());

        // GARDE gagnee de zero par attaque, appelant gagne 2 * 40 points
        assertEquals(80, gameSetScores.getIndividualResultsAtGameOfIndex(1, players.get(1)));
        assertEquals(40, gameSetScores.getIndividualResultsAtGameOfIndex(1, players.get(2)));
        assertEquals(-40, gameSetScores.getIndividualResultsAtGameOfIndex(1, players.get(3)));
        assertEquals(-40, gameSetScores.getIndividualResultsAtGameOfIndex(1, players.get(4)));
        assertEquals(-40, gameSetScores.getIndividualResultsAtGameOfIndex(1, players.get(5)));
        assertEquals(0, gameSetScores.getIndividualResultsAtGameOfIndex(1, players.get(6)));

        // so far code was identical to testAddFirstGameScore()

        // second game creation => won by 0 points by attack
        // player 1 is dead, he should earn zero and remain at his first score => 80
        // player 6 now plays and is in the attack, he should have 40 points total
        StandardTarot5Game game2 = new StandardTarot5Game();
        game2.setGameSetParameters(createCustomGameSetParameters());
        game2.setPlayers(players.subList(2, 6));
        game2.setLeadingPlayer(players.get(2));
        game2.setCalledPlayer(players.get(6));
        game2.setBet(Bet.GARDE);
        game2.setNumberOfOudlers(2);
        game2.setPoints(41);
        gameSetScores.addGameScore(game2.getGameScores());

        // checks all 6 players have a consistent score that corresponds to the last game
        MapPlayersScores lastResults = gameSetScores.getLastResults();
        assertEquals(80, lastResults.get(players.get(1)).intValue());
        assertEquals(120, lastResults.get(players.get(2)).intValue());
        assertEquals(-80, lastResults.get(players.get(3)).intValue());
        assertEquals(-80, lastResults.get(players.get(4)).intValue());
        assertEquals(-80, lastResults.get(players.get(5)).intValue());
        assertEquals(40, lastResults.get(players.get(6)).intValue());
    }


    @Test
    public final void testAddResults() {

        // creation of a list of 6 players => one player is dead at each game
        PlayerList players = create6Players();

        // creation of a game set score
        GameSetScores gameSetScores = new GameSetScores();
        gameSetScores.setPlayers(players);

        // create two virtual results with dead players
        MapPlayersScores results1 = new MapPlayersScores();
        results1.put(players.get(1), 10);
        results1.put(players.get(2), 10);
        results1.put(players.get(3), 10);
        results1.put(players.get(4), 10);
        results1.put(players.get(5), 10);

        MapPlayersScores results2 = new MapPlayersScores();
        results2.put(players.get(1), 100);
        results2.put(players.get(2), 200);
        results2.put(players.get(4), 400);
        results2.put(players.get(5), 500);
        results2.put(players.get(6), 600);

        // and them and check their consistency
        MapPlayersScores sum = gameSetScores.addResults(results1, results2);

        assertEquals(110, sum.get(players.get(1)).intValue());
        assertEquals(210, sum.get(players.get(2)).intValue());
        assertEquals(10, sum.get(players.get(3)).intValue());
        assertEquals(410, sum.get(players.get(4)).intValue());
        assertEquals(510, sum.get(players.get(5)).intValue());
        assertEquals(600, sum.get(players.get(6)).intValue());
    }


    @Test
    public final void testGetPlayerRankAtLastGame() {

        // creation of a list of 6 players => one player is dead at each game
        PlayerList players = create6Players();

        // creation of a game set score
        GameSetScores gameSetScores = new GameSetScores();
        gameSetScores.setPlayers(players);

        // first game creation => won by zero points by attack
        // player 6 in list is dead, he should have zero points
        StandardTarot5Game game = new StandardTarot5Game();
        game.setGameSetParameters(createCustomGameSetParameters());
        game.setPlayers(players.subList(1, 5));
        game.setLeadingPlayer(players.get(1));
        game.setCalledPlayer(players.get(2));
        game.setBet(Bet.GARDE);
        game.setNumberOfOudlers(2);
        game.setPoints(41);
        gameSetScores.addGameScore(game.getGameScores());

        // rank should be the following
        // player 1 80 => 1st
        // player 2 40 => 2nd
        // player 3 -40 => 4th
        // player 4 -40 => 4th
        // player 5 -40 => 4th
        // player 6 0 => 3rd
        assertEquals(1, gameSetScores.getPlayerRankAtLastGame(players.get(1)));
        assertEquals(2, gameSetScores.getPlayerRankAtLastGame(players.get(2)));
        assertEquals(4, gameSetScores.getPlayerRankAtLastGame(players.get(3)));
        assertEquals(4, gameSetScores.getPlayerRankAtLastGame(players.get(4)));
        assertEquals(4, gameSetScores.getPlayerRankAtLastGame(players.get(5)));
        assertEquals(3, gameSetScores.getPlayerRankAtLastGame(players.get(6)));
    }


    @Test
    public final void testGetMinAndMaxScoreEverForPlayer() {

        // creation of a list of 6 players => one player is dead at each game
        PlayerList players = create3Players();

        // creation of a game set score
        GameSetScores gameSetScores = new GameSetScores();
        gameSetScores.setPlayers(players);

        // min score ever for all players should be 0
        assertEquals("Min score ever for player 1 should be 0",
                     0,
                     gameSetScores.getMinScoreEverForPlayer(players.get(1)));
        assertEquals("Min score ever for player 2 should be 0",
                     0,
                     gameSetScores.getMinScoreEverForPlayer(players.get(2)));
        assertEquals("Min score ever for player 3 should be 0",
                     0,
                     gameSetScores.getMinScoreEverForPlayer(players.get(3)));
        // max score ever for all players should be 0
        assertEquals("Max score ever for player 1 should be 0",
                     0,
                     gameSetScores.getMaxScoreEverForPlayer(players.get(1)));
        assertEquals("Max score ever for player 2 should be 0",
                     0,
                     gameSetScores.getMaxScoreEverForPlayer(players.get(2)));
        assertEquals("Max score ever for player 3 should be 0",
                     0,
                     gameSetScores.getMaxScoreEverForPlayer(players.get(3)));

        // first game creation, won by zero points by attack
        StandardTarot3Game game1 = new StandardTarot3Game();
        game1.setGameSetParameters(createCustomGameSetParameters());
        game1.setPlayers(players);
        game1.setLeadingPlayer(players.get(1));
        game1.setBet(Bet.GARDE);
        game1.setNumberOfOudlers(2);
        game1.setPoints(41);
        gameSetScores.addGameScore(game1.getGameScores());

        // min score ever for players 2 and 3 is -40, min score ever for player 1 is 0
        assertEquals("Min score ever for player 1 should be 0",
                     0,
                     gameSetScores.getMinScoreEverForPlayer(players.get(1)));
        assertEquals("Min score ever for player 2 should be -40",
                     -40,
                     gameSetScores.getMinScoreEverForPlayer(players.get(2)));
        assertEquals("Min score ever for player 3 should be -40",
                     -40,
                     gameSetScores.getMinScoreEverForPlayer(players.get(3)));
        // max score ever for players 2 and 3 is 0, max score ever for player 1 is 80
        assertEquals("Max score ever for player 1 should be 80",
                     80,
                     gameSetScores.getMaxScoreEverForPlayer(players.get(1)));
        assertEquals("Max score ever for player 2 should be 0",
                     0,
                     gameSetScores.getMaxScoreEverForPlayer(players.get(2)));
        assertEquals("Max score ever for player 3 should be 0",
                     0,
                     gameSetScores.getMaxScoreEverForPlayer(players.get(3)));

        // second game creation, lost by 10 points by attack
        StandardTarot3Game game2 = new StandardTarot3Game();
        game2.setGameSetParameters(createCustomGameSetParameters());
        game2.setPlayers(players);
        game2.setLeadingPlayer(players.get(1));
        game2.setBet(Bet.GARDESANS);
        game2.setNumberOfOudlers(2);
        game2.setPoints(31);
        gameSetScores.addGameScore(game2.getGameScores());

        // min score ever for players 2 and 3 is -40, min score ever for player 1 is 0
        assertEquals("Min score ever for player 1 should be -240",
                     -240,
                     gameSetScores.getMinScoreEverForPlayer(players.get(1)));
        assertEquals("Min score ever for player 2 should be -40",
                     -40,
                     gameSetScores.getMinScoreEverForPlayer(players.get(2)));
        assertEquals("Min score ever for player 3 should be -40",
                     -40,
                     gameSetScores.getMinScoreEverForPlayer(players.get(3)));
        // max score ever for players 2 and 3 is 0, max score ever for player 1 is 80
        assertEquals("Max score ever for player 1 should be 80",
                     80,
                     gameSetScores.getMaxScoreEverForPlayer(players.get(1)));
        assertEquals("Max score ever for player 2 should be 120",
                     120,
                     gameSetScores.getMaxScoreEverForPlayer(players.get(2)));
        assertEquals("Max score ever for player 3 should be 120",
                     120,
                     gameSetScores.getMaxScoreEverForPlayer(players.get(3)));
    }


    @Test
    public final void testGetMinAndMaxScoresEver() {

        // creation of a list of 6 players => one player is dead at each game
        PlayerList players = create3Players();

        // creation of a game set score
        GameSetScores gameSetScores = new GameSetScores();
        gameSetScores.setPlayers(players);

        // min score ever for all players should be 0
        assertEquals("Min score ever should be 0", 0, gameSetScores.getMinScoreEver());
        // max score ever for all players should be 0
        assertEquals("Max score ever should be 0", 0, gameSetScores.getMaxScoreEver());

        // first game creation, won by zero points by attack
        StandardTarot3Game game1 = new StandardTarot3Game();
        game1.setGameSetParameters(createCustomGameSetParameters());
        game1.setPlayers(players);
        game1.setLeadingPlayer(players.get(1));
        game1.setBet(Bet.GARDE);
        game1.setNumberOfOudlers(2);
        game1.setPoints(41);
        gameSetScores.addGameScore(game1.getGameScores());

        // min score ever is -40
        assertEquals("Min score ever should be -40", -40, gameSetScores.getMinScoreEver());
        // max score ever for is 80
        assertEquals("Max score ever should be 80", 80, gameSetScores.getMaxScoreEver());

        // second game creation, lost by 10 points by attack
        StandardTarot3Game game2 = new StandardTarot3Game();
        game2.setGameSetParameters(createCustomGameSetParameters());
        game2.setPlayers(players);
        game2.setLeadingPlayer(players.get(1));
        game2.setBet(Bet.GARDESANS);
        game2.setNumberOfOudlers(2);
        game2.setPoints(31);
        gameSetScores.addGameScore(game2.getGameScores());

        // min score ever is -40
        assertEquals("Min score ever should be -240", -240, gameSetScores.getMinScoreEver());
        // max score ever is 120
        assertEquals("Max score ever should be 120", 120, gameSetScores.getMaxScoreEver());
    }


    @Test
    public final void testGetMaxAbsoluteScore() {
        // creation of a list of 6 players => one player is dead at each game
        PlayerList players = create3Players();

        // creation of a game set score
        GameSetScores gameSetScores = new GameSetScores();
        gameSetScores.setPlayers(players);

        // max absolute score ever is 0
        assertEquals("Max absolute score should be 0", 0, gameSetScores.getMaxAbsoluteScore());

        // first game creation, won by zero points by attack
        StandardTarot3Game game1 = new StandardTarot3Game();
        game1.setGameSetParameters(createCustomGameSetParameters());
        game1.setPlayers(players);
        game1.setLeadingPlayer(players.get(1));
        game1.setBet(Bet.GARDE);
        game1.setNumberOfOudlers(2);
        game1.setPoints(41);
        gameSetScores.addGameScore(game1.getGameScores());

        // max absolute score ever is 80
        assertEquals("Max absolute score should be 80", 80, gameSetScores.getMaxAbsoluteScore());

        // second game creation, lost by 10 points by attack
        StandardTarot3Game game2 = new StandardTarot3Game();
        game2.setGameSetParameters(createCustomGameSetParameters());
        game2.setPlayers(players);
        game2.setLeadingPlayer(players.get(1));
        game2.setBet(Bet.GARDESANS);
        game2.setNumberOfOudlers(2);
        game2.setPoints(31);
        gameSetScores.addGameScore(game2.getGameScores());

        // max absolute score ever is 240
        assertEquals("Max absolute score should be 240", 240, gameSetScores.getMaxAbsoluteScore());
    }


    @Test(expected = java.util.NoSuchElementException.class)
    public final void testIterator() {
        // creation of a list of 6 players => one player is dead at each game
        PlayerList players = create3Players();

        // creation of a game set score
        GameSetScores gameSetScores = new GameSetScores();
        gameSetScores.setPlayers(players);

        // first game creation
        StandardTarot3Game game1 = new StandardTarot3Game();
        game1.setGameSetParameters(createCustomGameSetParameters());
        game1.setPlayers(players);
        game1.setLeadingPlayer(players.get(1));
        game1.setBet(Bet.GARDE);
        game1.setNumberOfOudlers(2);
        game1.setPoints(41);
        GameScores gameScore1 = game1.getGameScores();
        gameSetScores.addGameScore(gameScore1);

        // second game creation
        StandardTarot3Game game2 = new StandardTarot3Game();
        game2.setGameSetParameters(createCustomGameSetParameters());
        game2.setPlayers(players);
        game2.setLeadingPlayer(players.get(1));
        game2.setBet(Bet.GARDESANS);
        game2.setNumberOfOudlers(2);
        game2.setPoints(31);
        GameScores gameScore2 = game2.getGameScores();
        gameSetScores.addGameScore(gameScore2);

        Iterator<GameScores> iterator = gameSetScores.iterator();
        assertEquals("Incorrect reference for game score 1", gameScore1, iterator.next());
        assertEquals("Incorrect reference for game score 2", gameScore2, iterator.next());
        iterator.next();
        fail("NoSuchElementException should have been thrown...");
    }
}