package org.nla.tarotdroid;/*
    This file is part of the Android application TarotDroid.
 	
	TarotDroid is free software: you can redistribute it and/or modify
 	it under the terms of the GNU General Public License as published by
 	the Free Software Foundation, either version 3 of the License, or
 	(at your option) any later version.
 	
 	TarotDroid is distributed in the hope that it will be useful,
 	but WITHOUT ANY WARRANTY; without even the implied warranty of
 	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 	GNU General Public License for more details.
 	
 	You should have received a copy of the GNU General Public License
 	along with TarotDroid. If not, see <http://www.gnu.org/licenses/>.
*/

import org.junit.Test;
import org.nla.tarotdroid.biz.Bet;
import org.nla.tarotdroid.biz.GameScores;
import org.nla.tarotdroid.biz.GameSetParameters;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.PlayerList;
import org.nla.tarotdroid.biz.StandardTarot5Game;
import org.nla.tarotdroid.biz.Team;
import org.nla.tarotdroid.biz.computers.StandardTarot5GameScoresComputer;

import static org.junit.Assert.assertEquals;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 */
public class StandardTarot5GameScoresComputerTest {

    /**
     * @return
     */
    private StandardTarot5Game createGame() {
        // players creation
        Player pm = new Player();
        pm.setName("PM");

        Player guillaume = new Player();
        guillaume.setName("Guillaume");

        Player arthur = new Player();
        arthur.setName("Arthur");

        Player ludas = new Player();
        ludas.setName("Ludas");

        Player nico = new Player();
        nico.setName("Nico");

        // main list of players
        PlayerList players = new PlayerList();
        players.add(pm);
        players.add(ludas);
        players.add(arthur);
        players.add(guillaume);
        players.add(nico);

        // game creation
        StandardTarot5Game game = new StandardTarot5Game();
        game.setPlayers(players);
        game.setLeadingPlayer(guillaume);
        game.setCalledPlayer(nico);
        game.setGameSetParameters(createCustomGameSetParameters());

        return game;
    }

    /**
     * Creates a custom GameSetParameters with the values we used at the beginning of TarotDroid's development (before the 27/11/2011), to avoid rewriting all the test cases....
     *
     * @return a custom GameSetParameters.
     */
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

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForPriseAndZeroOudler() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(0);
        game.setBet(Bet.PRISE);
        game.setPoints(45.0);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeBasePoints();

        // PRISE perdue de 11 = -20 + (-11 * 2) = -42
        assertEquals(-42, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForPriseAndOneOudler() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(1);
        game.setBet(Bet.PRISE);
        game.setPoints(45.0);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeBasePoints();

        // PRISE perdue de 6 = -20 - (6 * 2) = -32
        assertEquals(-32, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForPriseAndTwoOudlers() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.PRISE);
        game.setPoints(45.0);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeBasePoints();

        // PRISE gagnee de 4 = 20 + (4 * 2) = 28
        assertEquals(28, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForPriseAndThreeOudlers() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(3);
        game.setBet(Bet.PRISE);
        game.setPoints(45.0);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeBasePoints();

        // PRISE gagnee de 9 = 20 + (9 * 2) = 38
        assertEquals(38, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForGardeAndZeroOudler() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(0);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        ////scoreComputer.setGame(game);
        scoreComputer.computeBasePoints();

        // GARDE perdue de 11 = -40 -(11 * 4)
        int expectedBasePoints = -40 - (11 * 4);

        assertEquals(expectedBasePoints, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForGardeAndOneOudler() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(1);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        //scoreComputer.setGame(game);
        scoreComputer.computeBasePoints();

        // GARDE perdue de 6 = -40 -(6 * 4)
        int expectedBasePoints = -40 - (6 * 4);

        assertEquals(expectedBasePoints, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForGardeAndTwoOudlers() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        //scoreComputer.setGame(game);
        scoreComputer.computeBasePoints();

        // GARDE gagnee de 4 = 40 +(4 * 4)
        int expectedBasePoints = 40 + (4 * 4);

        assertEquals(expectedBasePoints, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForGardeAndThreeOudlers() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(3);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        //scoreComputer.setGame(game);
        scoreComputer.computeBasePoints();

        // GARDE gagnee de 9 = 40 +(9 * 4)
        int expectedBasePoints = 40 + (9 * 4);

        assertEquals(expectedBasePoints, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForGardeSansAndZeroOudler() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(0);
        game.setBet(Bet.GARDESANS);
        game.setPoints(45.0);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        //scoreComputer.setGame(game);
        scoreComputer.computeBasePoints();

        // GARDE sans perdue de 11 = -80 -(11 * 8)
        int expectedBasePoints = -80 - (11 * 8);

        assertEquals(expectedBasePoints, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForGardeSansAndOneOudler() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(1);
        game.setBet(Bet.GARDESANS);
        game.setPoints(45.0);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        //scoreComputer.setGame(game);
        scoreComputer.computeBasePoints();

        // GARDE sans perdue de 6 = -80 -(6 * 8)
        int expectedBasePoints = -80 - (6 * 8);

        assertEquals(expectedBasePoints, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForGardeSansAndTwoOudlers() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDESANS);
        game.setPoints(45.0);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        //scoreComputer.setGame(game);
        scoreComputer.computeBasePoints();

        // GARDE sans gagnee de 4 = 80 +(4 * 8)
        int expectedBasePoints = 80 + (4 * 8);

        assertEquals(expectedBasePoints, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForGardeSansAndThreeOudlers() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(3);
        game.setBet(Bet.GARDESANS);
        game.setPoints(45.0);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        //scoreComputer.setGame(game);
        scoreComputer.computeBasePoints();

        // GARDE sans gagnee de 9 = 80 +(9 * 8)
        int expectedBasePoints = 80 + (9 * 8);

        assertEquals(expectedBasePoints, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForGardeContreAndZeroOudler() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(0);
        game.setBet(Bet.GARDECONTRE);
        game.setPoints(45.0);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        //scoreComputer.setGame(game);
        scoreComputer.computeBasePoints();

        // GARDE contre perdue de 11 = -160 -(11 * 16)
        int expectedBasePoints = -160 - (11 * 16);

        assertEquals(expectedBasePoints, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForGardeContreAndOneOudler() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(1);
        game.setBet(Bet.GARDECONTRE);
        game.setPoints(45.0);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        //scoreComputer.setGame(game);
        scoreComputer.computeBasePoints();

        // GARDE contre perdue de 6 = -160 -(6 * 16)
        int expectedBasePoints = -160 - (6 * 16);

        assertEquals(expectedBasePoints, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForGardeContreAndTwoOudlers() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDECONTRE);
        game.setPoints(45.0);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        //scoreComputer.setGame(game);
        scoreComputer.computeBasePoints();

        // GARDE contre gagnee de 4 = 160 +(4 * 16)
        int expectedBasePoints = 160 + (4 * 16);

        assertEquals(expectedBasePoints, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForGardeContreAndThreeOudlers() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(3);
        game.setBet(Bet.GARDECONTRE);
        game.setPoints(45.0);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        //scoreComputer.setGame(game);
        scoreComputer.computeBasePoints();

        // GARDE contre gagnee de 9 = 160 +(9 * 16)
        int expectedBasePoints = 160 + (9 * 16);

        assertEquals(expectedBasePoints, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForZeroAdditionnalPoints() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(3);
        game.setBet(Bet.GARDECONTRE);
        game.setPoints(36.0);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        //scoreComputer.setGame(game);
        scoreComputer.computeBasePoints();

        // GARDE contre passee de 0 = 160 +(0 * 16)
        int expectedBasePoints = 160;

        assertEquals(expectedBasePoints, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#computeEachIndividualResult()}.
     */
    @Test
    public final void testComputeEachIndividualScoreForTwoPlayersInLeadingTeam() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        //scoreComputer.setGame(game);
        scoreComputer.computeBasePoints();
        scoreComputer.computeEachIndividualResult();
        GameScores score = scoreComputer.getGameScores();

        // GARDE passee de 4, on doit avoir les resultats suivants :
        // guillaume : (40 + (4*4)) * 2 => 112
        // nico : 40 + (4*4) => 56
        // arthur : -40 - (4*4) => -56
        // pm : -40 - (4*4) => -56
        // ludas : -40 - (4*4) => -56

        assertEquals(112, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(56, score.getIndividualResult(game.getPlayerForName("nico")));
        assertEquals(-56, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-56, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(-56, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#computeEachIndividualResult()}.
     */
    @Test
    public final void testComputeEachIndividualScoreForOnlyLeadingPlayerInLeadingTeam() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);
        game.setCalledPlayer(null);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        //scoreComputer.setGame(game);
        scoreComputer.computeBasePoints();
        scoreComputer.computeEachIndividualResult();
        GameScores score = scoreComputer.getGameScores();

        // GARDE passee de 4, on doit avoir les resultats suivants :
        // guillaume : (40 + (4*4)) * 4 => 224
        // nico : -40 - (4*4) => -56
        // arthur : -40 - (4*4) => -56
        // pm : -40 - (4*4) => -56
        // ludas : -40 - (4*4) => -56

        assertEquals(224, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-56, score.getIndividualResult(game.getPlayerForName("nico")));
        assertEquals(-56, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-56, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(-56, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

//	/**
//	 * Test method for {@link org.nla.tarotdroid.biz.computers.StandardTarot5GameScoresComputer#takeInAccountMiseries()}.
//	 */
//	@Test
//	public final void testTakeInAccountOneMisery() {
//		StandardTarot5Game game = createGame();
//		game.setNumberOfOudlers(2);
//		game.setBet(Bet.GARDE);
//		game.setPoints(45.0);
//		
//		PlayerList playersWithMisery = new PlayerList();
//		playersWithMisery.add(game.getPlayerForName("pm"));
//		game.setPlayersWithMisery(playersWithMisery);
//		
//		StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game, createCustomGameSetParameters());;
//		//scoreComputer.setGame(game);
//		scoreComputer.computeBasePoints();
//		scoreComputer.computeEachIndividualResult();
//		scoreComputer.takeInAccountMiseries();
//		GameScores score = scoreComputer.getGameScores();
//		
//		// GARDE passee de 4 avec misere de pm, on doit avoir les resultats suivants :
//		// guillaume : (40 + (4*4)) * 2 (-10) => 102  
//		// nico : 40 + (4*4) => 56 (-10) => 46
//		// arthur : -40 - (4*4) (-10) => -66
//		// pm : -40 - (4*4) (+40) => -16
//		// ludas : -40 - (4*4) (-10) => -66
//		
//		assertEquals(102, score.getIndividualResult(game.getPlayerForName("guillaume")));
//		assertEquals(46, score.getIndividualResult(game.getPlayerForName("nico")));
//		assertEquals(-66, score.getIndividualResult(game.getPlayerForName("arthur")));
//		assertEquals(-16, score.getIndividualResult(game.getPlayerForName("pm")));
//		assertEquals(-66, score.getIndividualResult(game.getPlayerForName("ludas")));
//	}

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#takeInAccountMiseries()}.
     */
    @Test
    public final void testTakeInAccountTwoMiseries() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);

        PlayerList playersWithMisery = new PlayerList();
        playersWithMisery.add(game.getPlayerForName("pm"));
        playersWithMisery.add(game.getPlayerForName("arthur"));
        game.setPlayersWithMisery(playersWithMisery);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        //scoreComputer.setGame(game);
        scoreComputer.computeBasePoints();
        scoreComputer.computeEachIndividualResult();
        scoreComputer.takeInAccountMiseries();
        GameScores score = scoreComputer.getGameScores();

        // GARDE passee de 4 avec misere de pm, on doit avoir les resultats suivants :
        // guillaume : (40 + (4*4)) * 2 (-10) (-10) => 92
        // nico : 40 + (4*4) => 56 (-10) (-10) => 36
        // arthur : -40 - (4*4) (-10) (+40) => -26
        // pm : -40 - (4*4) (+40) (-10) => -26
        // ludas : -40 - (4*4) (-10) (-10) => -76

        assertEquals(92, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(36, score.getIndividualResult(game.getPlayerForName("nico")));
        assertEquals(-26, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-26, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(-76, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#takeInAccountPoignees()}.
     */
    @Test
    public final void testTakeInAccountOnePoigneeForLeadingTeamThatWins() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);
        game.setTeamWithPoignee(Team.LEADING_TEAM);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        //scoreComputer.setGame(game);
        scoreComputer.computeBasePoints();
        scoreComputer.computeEachIndividualResult();
        scoreComputer.takeInAccountPoignees();
        GameScores score = scoreComputer.getGameScores();

        // GARDE passee de 4 avec poignee de l'attaque, on doit avoir les resultats suivants :
        // guillaume : (40 + (4*4)) * 2 + (10*2) => 132
        // nico : 40 + (4*4) => 56 + (10) => 66
        // arthur : -40 - (4*4) (-10) => -66
        // pm : -40 - (4*4) (-10) => -66
        // ludas : -40 - (4*4) (-10) => -66

        assertEquals(132, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(66, score.getIndividualResult(game.getPlayerForName("nico")));
        assertEquals(-66, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-66, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(-66, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#takeInAccountPoignees()}.
     */
    @Test
    public final void testTakeInAccountOnePoigneeForLeadingTeamThatWinsAndLeaderAlone() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);
        game.setTeamWithPoignee(Team.LEADING_TEAM);
        game.setCalledPlayer(null);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        //scoreComputer.setGame(game);
        scoreComputer.computeBasePoints();
        scoreComputer.computeEachIndividualResult();
        scoreComputer.takeInAccountPoignees();
        GameScores score = scoreComputer.getGameScores();

        // GARDE passee de 4 avec poignee de l'attaque, on doit avoir les resultats suivants :
        // guillaume : (40 + (4*4)+10) * 4 => 264
        // nico : 40 + (4*4) => 56 + (10) => -66
        // arthur : -40 - (4*4) (-10) => -66
        // pm : -40 - (4*4) (-10) => -66
        // ludas : -40 - (4*4) (-10) => -66

        assertEquals(264, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-66, score.getIndividualResult(game.getPlayerForName("nico")));
        assertEquals(-66, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-66, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(-66, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#takeInAccountDoublePoignees()}.
     */
    @Test
    public final void testTakeInAccountOneDoublePoigneeForLeadingTeamThatWins() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);
        game.setTeamWithDoublePoignee(Team.LEADING_TEAM);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        //scoreComputer.setGame(game);
        scoreComputer.computeBasePoints();
        scoreComputer.computeEachIndividualResult();
        scoreComputer.takeInAccountDoublePoignees();
        GameScores score = scoreComputer.getGameScores();

        // GARDE passee de 4 avec double poignee de l'attaque, on doit avoir les resultats suivants :
        // guillaume : (40 + (4*4)) * 2 + (20*2) => 152
        // nico : 40 + (4*4) => 56 + (20) => 76
        // arthur : -40 - (4*4) (-20) => -76
        // pm : -40 - (4*4) (-20) => -76
        // ludas : -40 - (4*4) (-20) => -76

        assertEquals(152, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(76, score.getIndividualResult(game.getPlayerForName("nico")));
        assertEquals(-76, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-76, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(-76, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#takeInAccountDoublePoignees()}.
     */
    @Test
    public final void testTakeInAccountOneDoublePoigneeForLeadingTeamThatWinsAndLeaderAlone() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);
        game.setTeamWithDoublePoignee(Team.LEADING_TEAM);
        game.setCalledPlayer(null);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        //scoreComputer.setGame(game);
        scoreComputer.computeBasePoints();
        scoreComputer.computeEachIndividualResult();
        scoreComputer.takeInAccountDoublePoignees();
        GameScores score = scoreComputer.getGameScores();

        // GARDE passee de 4 avec double poignee de l'attaque, on doit avoir les resultats suivants :
        // guillaume : (40 + (4*4)+20) * 4  => 304
        // nico : => -76
        // arthur : => -76
        // pm :  => -76
        // ludas :  => -76

        assertEquals(304, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-76, score.getIndividualResult(game.getPlayerForName("nico")));
        assertEquals(-76, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-76, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(-76, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#takeInAccountDoublePoignees()}.
     */
    @Test
    public final void testTakeInAccountOneTriplePoigneeForLeadingTeamThatWins() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);
        game.setTeamWithTriplePoignee(Team.LEADING_TEAM);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        //scoreComputer.setGame(game);
        scoreComputer.computeBasePoints();
        scoreComputer.computeEachIndividualResult();
        scoreComputer.takeInAccountTriplePoignees();
        GameScores score = scoreComputer.getGameScores();

        // GARDE passee de 4 avec triple poignee de l'attaque, on doit avoir les resultats suivants :
        // guillaume : (40 + (4*4)) * 2 + (30*2) => 172
        // nico : 40 + (4*4) => 56 + (30) => 86
        // arthur : -40 - (4*4) (-30) => -86
        // pm : -40 - (4*4) (-30) => -86
        // ludas : -40 - (4*4) (-30) => -86

        assertEquals(172, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(86, score.getIndividualResult(game.getPlayerForName("nico")));
        assertEquals(-86, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-86, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(-86, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#takeInAccountDoublePoignees()}.
     */
    @Test
    public final void testTakeInAccountOneTriplePoigneeForLeadingTeamThatWinsAndLeaderAlone() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);
        game.setTeamWithTriplePoignee(Team.LEADING_TEAM);
        game.setCalledPlayer(null);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        //scoreComputer.setGame(game);
        scoreComputer.computeBasePoints();
        scoreComputer.computeEachIndividualResult();
        scoreComputer.takeInAccountTriplePoignees();
        GameScores score = scoreComputer.getGameScores();

        // GARDE passee de 4 avec triple poignee de l'attaque, on doit avoir les resultats suivants :
        // guillaume : (40 + (4*4)+30) * 4 => 344
        // nico : => -86
        // arthur :  => -86
        // pm :  => -86
        // ludas :  => -86

        assertEquals(344, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-86, score.getIndividualResult(game.getPlayerForName("nico")));
        assertEquals(-86, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-86, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(-86, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#takeInAccountPoignees()}.
     */
    @Test
    public final void testTakeInAccountOnePoigneeForLeadingTeamThatLoses() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(37.0);
        game.setTeamWithPoignee(Team.LEADING_TEAM);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeBasePoints();
        scoreComputer.computeEachIndividualResult();
        scoreComputer.takeInAccountPoignees();
        GameScores score = scoreComputer.getGameScores();

        // GARDE chutee de 4 avec poignee de l'attaque, on doit avoir les resultats suivants :
        // guillaume : (-40 -(4*4)-10) * 2 => -132
        // nico : (-40 -(4*4)-10) => -66
        // arthur : +40 +(4*4) +10 => 66
        // pm : +40 +(4*4) +10 => 66
        // ludas : +40 +(4*4) +10 => 66

        assertEquals(-132, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-66, score.getIndividualResult(game.getPlayerForName("nico")));
        assertEquals(66, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(66, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(66, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#takeInAccountPoignees()}.
     */
    @Test
    public final void testTakeInAccountOnePoigneeForLeadingTeamThatLosesAndLeaderAlone() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(37.0);
        game.setTeamWithPoignee(Team.LEADING_TEAM);
        game.setCalledPlayer(null);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeBasePoints();
        scoreComputer.computeEachIndividualResult();
        scoreComputer.takeInAccountPoignees();
        GameScores score = scoreComputer.getGameScores();

        // GARDE chutee de 4 avec poignee de l'attaque, on doit avoir les resultats suivants :
        // guillaume : (-40 -(4*4)-10) * 4 => 264
        // nico : => 66
        // arthur :  => 66
        // pm :  => 66
        // ludas :  => 66

        assertEquals(-264, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(66, score.getIndividualResult(game.getPlayerForName("nico")));
        assertEquals(66, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(66, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(66, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#takeInAccountPoignees()}.
     */
    @Test
    public final void testTakeInAccountOnePoigneeForBothTeamsAndLeadingTeamLoses() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(37.0);
        game.setTeamWithPoignee(Team.BOTH_TEAMS);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeBasePoints();
        scoreComputer.computeEachIndividualResult();
        scoreComputer.takeInAccountPoignees();
        GameScores score = scoreComputer.getGameScores();

        // GARDE chutee de 4 avec poignee de l'attaque, on doit avoir les resultats suivants :
        // guillaume : (-40 -(4*4)-10-10) * 2 => -152
        // nico : (-40 -(4*4)-10-10) => -76
        // arthur : +40 +(4*4) +10+10 => 76
        // pm : +40 +(4*4) +10+10 => 76
        // ludas : +40 +(4*4) +10+10 => 76

        assertEquals(-152, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-76, score.getIndividualResult(game.getPlayerForName("nico")));
        assertEquals(76, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(76, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(76, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#takeInAccountPoignees()}.
     */
    @Test
    public final void testTakeInAccountOnePoigneeForBothTeamsAndLeadingTeamLosesAndLeaderAlone() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(37.0);
        game.setTeamWithPoignee(Team.BOTH_TEAMS);
        game.setCalledPlayer(null);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeBasePoints();
        scoreComputer.computeEachIndividualResult();
        scoreComputer.takeInAccountPoignees();
        GameScores score = scoreComputer.getGameScores();

        // GARDE chutee de 4 avec poignee de l'attaque, on doit avoir les resultats suivants :
        // guillaume : (-40 -(4*4)-10-10) * 4 => -304
        // nico :  => 76
        // arthur :  => 76
        // pm : => 76
        // ludas :  => 76

        assertEquals(-304, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(76, score.getIndividualResult(game.getPlayerForName("nico")));
        assertEquals(76, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(76, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(76, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#takeInAccountDoublePoignees()}.
     */
    @Test
    public final void testTakeInAccountOneDoublePoigneeForLeadingTeamThatLoses() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(37.0);
        game.setTeamWithDoublePoignee(Team.LEADING_TEAM);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeBasePoints();
        scoreComputer.computeEachIndividualResult();
        scoreComputer.takeInAccountDoublePoignees();
        GameScores score = scoreComputer.getGameScores();

        // GARDE chutee de 4 avec double poignee de l'attaque, on doit avoir les resultats suivants :
        // guillaume : (-40 -(4*4)-20) * 2 => -152
        // nico : (-40 -(4*4)-20) => -76
        // arthur : +40 +(4*4) +20 => 76
        // pm : +40 +(4*4) +20 => 76
        // ludas : +40 +(4*4) +20 => 76

        assertEquals(-152, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-76, score.getIndividualResult(game.getPlayerForName("nico")));
        assertEquals(76, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(76, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(76, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#takeInAccountDoublePoignees()}.
     */
    @Test
    public final void testTakeInAccountOneDoublePoigneeForBothTeamsAndLeadingTeamLoses() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(37.0);
        game.setTeamWithDoublePoignee(Team.BOTH_TEAMS);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeBasePoints();
        scoreComputer.computeEachIndividualResult();
        scoreComputer.takeInAccountDoublePoignees();
        GameScores score = scoreComputer.getGameScores();

        // GARDE chutee de 4 avec double poignee de l'attaque, on doit avoir les resultats suivants :
        // guillaume : (-40 -(4*4)-20-20) * 2 => -192
        // nico : (-40 -(4*4)-20-20) => -96
        // arthur : +40 +(4*4) +20+20 => 96
        // pm : +40 +(4*4) +20+20 => 96
        // ludas : +40 +(4*4) +20+20 => 96

        assertEquals(-192, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-96, score.getIndividualResult(game.getPlayerForName("nico")));
        assertEquals(96, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(96, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(96, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#takeInAccountDoublePoignees()}.
     */
    @Test
    public final void testTakeInAccountOneTriplePoigneeForLeadingTeamThatLoses() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(37.0);
        game.setTeamWithTriplePoignee(Team.LEADING_TEAM);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeBasePoints();
        scoreComputer.computeEachIndividualResult();
        scoreComputer.takeInAccountTriplePoignees();
        GameScores score = scoreComputer.getGameScores();

        // GARDE chutee de 4 avec triple poignee de l'attaque, on doit avoir les resultats suivants :
        // guillaume : (-40 -(4*4)-30) * 2 => -172
        // nico : (-40 -(4*4)-30) => -86
        // arthur : +40 +(4*4) +30 => -86
        // pm : +40 +(4*4) +30 => -86
        // ludas : +40 +(4*4) +30 => -86

        assertEquals(-172, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-86, score.getIndividualResult(game.getPlayerForName("nico")));
        assertEquals(86, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(86, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(86, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#takeInAccountDoublePoignees()}.
     */
    @Test(expected = IllegalStateException.class)
    public final void testTakeInAccountOneTriplePoigneeForBothTeams() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(37.0);
        game.setTeamWithTriplePoignee(Team.BOTH_TEAMS);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeBasePoints();
        scoreComputer.computeEachIndividualResult();
        scoreComputer.takeInAccountTriplePoignees();
        scoreComputer.getGameScores();
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#takeInAccountTriplePoignees()}.
     */
    @SuppressWarnings("deprecation")
    @Test
    public final void testTakeInAccountOneTriplePoigneeForLeadingTeamThatWinsHistoric() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);
        game.setPlayerWithTriplePoignee(game.getPlayerForName("guillaume"));

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeBasePoints();
        scoreComputer.computeEachIndividualResult();
        scoreComputer.takeInAccountTriplePoignees();
        GameScores score = scoreComputer.getGameScores();

        // GARDE passee de 4 avec triple poignee de l'attaque, on doit avoir les resultats suivants :
        // guillaume : (40 + (4*4)+30) * 2 => 172
        // nico : (40 + (4*4)+30) => 86
        // arthur : -40 - (4*4) (-30) => -86
        // pm : -40 - (4*4) (-30) => -86
        // ludas : -40 - (4*4) (-30) => -86

        assertEquals(172, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(86, score.getIndividualResult(game.getPlayerForName("nico")));
        assertEquals(-86, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-86, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(-86, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#takeInAccountTriplePoignees()}.
     */
    @SuppressWarnings("deprecation")
    @Test
    public final void testTakeInAccountOneTriplePoigneeForLeadingTeamThatLosesHistoric() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(37.0);
        game.setPlayerWithTriplePoignee(game.getPlayerForName("guillaume"));

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeBasePoints();
        scoreComputer.computeEachIndividualResult();
        scoreComputer.takeInAccountTriplePoignees();
        GameScores score = scoreComputer.getGameScores();

        // GARDE chutee de 4 avec triple poignee de l'attaque, on doit avoir les resultats suivants :
        // guillaume : (-40 - (4*4)-30) * 2 => -172
        // nico : (-40 - (4*4)-30) => -86
        // arthur : +40 + (4*4) (+30) => 86
        // pm : +40 + (4*4) (+30) => 86
        // ludas : +40 + (4*4) (+30) => 86

        assertEquals(-172, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-86, score.getIndividualResult(game.getPlayerForName("nico")));
        assertEquals(86, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(86, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(86, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#takeInAccountTriplePoignees()}.
     */
    @SuppressWarnings("deprecation")
    @Test
    public final void testTakeInAccountOneTriplePoigneeForDefenseTeamThatLosesHistoric() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);
        game.setPlayerWithTriplePoignee(game.getPlayerForName("pm"));

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeBasePoints();
        scoreComputer.computeEachIndividualResult();
        scoreComputer.takeInAccountTriplePoignees();
        GameScores score = scoreComputer.getGameScores();

        // GARDE passee de 4 avec triple poignee de la defense (perdue), on doit avoir les resultats suivants :
        // guillaume : (40 + (4*4)+30) * 2 => 172
        // nico : (40 + (4*4)+30) => 86
        // arthur : -40 - (4*4) (-30) => -86
        // pm : -40 - (4*4) (-30) => -86
        // ludas : -40 - (4*4) (-30) => -86

        assertEquals(172, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(86, score.getIndividualResult(game.getPlayerForName("nico")));
        assertEquals(-86, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-86, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(-86, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#takeInAccountTriplePoignees()}.
     */
    @SuppressWarnings("deprecation")
    @Test
    public final void testTakeInAccountOneTriplePoigneeForDefenseTeamThatWinsHistoric() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(37.0);
        game.setPlayerWithTriplePoignee(game.getPlayerForName("pm"));

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeBasePoints();
        scoreComputer.computeEachIndividualResult();
        scoreComputer.takeInAccountTriplePoignees();
        GameScores score = scoreComputer.getGameScores();

        // GARDE chutee de 4 avec triple poignee de l'attaque, on doit avoir les resultats suivants :
        // guillaume : (-40 - (4*4)-30) * 2 => -172
        // nico : (-40 - (4*4)-30) => -86
        // arthur : +40 + (4*4) (+30) => 86
        // pm : +40 + (4*4) (+30) => 86
        // ludas : +40 + (4*4) (+30) => 86

        assertEquals(-172, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-86, score.getIndividualResult(game.getPlayerForName("nico")));
        assertEquals(86, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(86, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(86, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#getGameScores()}.
     */
    @Test
    public final void testTakeInAccountKidAtTheEndForLeadingTeamForPrise() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.PRISE);
        game.setPoints(45.0);    // +4 points
        game.setTeamWithKidAtTheEnd(Team.LEADING_TEAM);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // Prise passee de 4 + Petit au bout pour l'attaque, on doit avoir les resultats suivants :
        // guillaume : (20 + (4*2) + 10*2) * 2 => 96
        // nico : => 48
        // arthur :  => -48
        // pm :  => -48
        // ludas :  => -48

        assertEquals(96, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(48, score.getIndividualResult(game.getPlayerForName("nico")));
        assertEquals(-48, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-48, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(-48, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#getGameScores()}.
     */
    @Test
    public final void testTakeInAccountKidAtTheEndForLeadingTeamForGarde() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);    // +4 points
        game.setTeamWithKidAtTheEnd(Team.LEADING_TEAM);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE passee de 4 + Petit au bout pour l'attaque, on doit avoir les resultats suivants :
        // guillaume : (40 + (4*4) + 10 * 4) * 2 => 192
        // nico : => 96
        // arthur :  => -96
        // pm :  => -96
        // ludas :  => -96

        assertEquals(192, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(96, score.getIndividualResult(game.getPlayerForName("nico")));
        assertEquals(-96, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-96, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(-96, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#getGameScores()}.
     */
    @Test
    public final void testTakeInAccountKidAtTheEndForLeadingTeamForGardeSans() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDESANS);
        game.setPoints(45.0);    // +4 points
        game.setTeamWithKidAtTheEnd(Team.LEADING_TEAM);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE Sans passee de 4 + Petit au bout pour l'attaque, on doit avoir les resultats suivants :
        // guillaume : (80 + (4*8) + 10 * 8) * 2 => 384
        // nico : => 192
        // arthur :  => -192
        // pm :  => -192
        // ludas :  => -192

        assertEquals(384, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(192, score.getIndividualResult(game.getPlayerForName("nico")));
        assertEquals(-192, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-192, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(-192, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#getGameScores()}.
     */
    @Test
    public final void testTakeInAccountKidAtTheEndForLeadingTeamForGardeContre() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDECONTRE);
        game.setPoints(45.0);    // +4 points
        game.setTeamWithKidAtTheEnd(Team.LEADING_TEAM);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE Contre passee de 4 + Petit au bout pour l'attaque, on doit avoir les resultats suivants :
        // guillaume : (160 + (4*16) + 10 * 16) * 2 => 768
        // nico : => 384
        // arthur :  => -384
        // pm :  => -384
        // ludas :  => -384

        assertEquals(768, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(384, score.getIndividualResult(game.getPlayerForName("nico")));
        assertEquals(-384, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-384, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(-384, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#getGameScores()}.
     */
    @Test
    public final void testTakeInAccountKidAtTheEndForDefenseTeamForPrise() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.PRISE);
        game.setPoints(45.0);    // +4 points
        game.setTeamWithKidAtTheEnd(Team.DEFENSE_TEAM);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // Prise passee de 4 + Petit au bout pour la defense, on doit avoir les resultats suivants :
        // guillaume : (20 + (4*2) - 10*2) * 2 => 16
        // nico : => 8
        // arthur :  => -8
        // pm :  => -8
        // ludas :  => -8

        assertEquals(16, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(8, score.getIndividualResult(game.getPlayerForName("nico")));
        assertEquals(-8, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-8, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(-8, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#getGameScores()}.
     */
    @Test
    public final void testTakeInAccountKidAtTheEndForDefenseTeamForGarde() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);    // +4 points
        game.setTeamWithKidAtTheEnd(Team.DEFENSE_TEAM);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE passee de 4 + Petit au bout pour la defense, on doit avoir les resultats suivants :
        // guillaume : (40 + (4*4) - 10 * 4) * 2 => 32
        // nico : => 16
        // arthur :  => -16
        // pm :  => -16
        // ludas :  => -16

        assertEquals(32, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(16, score.getIndividualResult(game.getPlayerForName("nico")));
        assertEquals(-16, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-16, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(-16, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#getGameScores()}.
     */
    @Test
    public final void testTakeInAccountKidAtTheEndForDefenseTeamForGardeSans() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDESANS);
        game.setPoints(45.0);    // +4 points
        game.setTeamWithKidAtTheEnd(Team.DEFENSE_TEAM);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE Sans passee de 4 + Petit au bout pour la defense, on doit avoir les resultats suivants :
        // guillaume : (80 + (4*8) - 10 * 8) * 2 => 64
        // nico : => 32
        // arthur :  => -32
        // pm :  => -32
        // ludas :  => -32

        assertEquals(64, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(32, score.getIndividualResult(game.getPlayerForName("nico")));
        assertEquals(-32, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-32, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(-32, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot5GameScoresComputer#getGameScores()}.
     */
    @Test
    public final void testTakeInAccountKidAtTheEndForDefenseTeamForGardeContre() {
        StandardTarot5Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDECONTRE);
        game.setPoints(45.0);    // +4 points
        game.setTeamWithKidAtTheEnd(Team.DEFENSE_TEAM);

        StandardTarot5GameScoresComputer scoreComputer = new StandardTarot5GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE Contre passee de 4 + Petit au bout pour la defense, on doit avoir les resultats suivants :
        // guillaume : (160 + (4*16) + 10 * 16) * 2 => 128
        // nico : => 64
        // arthur :  => -64
        // pm :  => -64
        // ludas :  => -64

        assertEquals(128, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(64, score.getIndividualResult(game.getPlayerForName("nico")));
        assertEquals(-64, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-64, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(-64, score.getIndividualResult(game.getPlayerForName("ludas")));
    }
}
