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
import org.nla.tarotdroid.biz.Chelem;
import org.nla.tarotdroid.biz.GameScores;
import org.nla.tarotdroid.biz.GameSetParameters;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.PlayerList;
import org.nla.tarotdroid.biz.StandardTarot4Game;
import org.nla.tarotdroid.biz.Team;
import org.nla.tarotdroid.biz.computers.StandardTarot4GameScoresComputer;

import static org.junit.Assert.assertEquals;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 */
public class StandardTarot4GameScoresComputerTest {

    /**
     * Creates a standard 4 player styla tarot game.
     *
     * @return
     */
    private StandardTarot4Game createGame() {
        // players creation
        Player pm = new Player();
        pm.setName("PM");

        Player guillaume = new Player();
        guillaume.setName("Guillaume");

        Player arthur = new Player();
        arthur.setName("Arthur");

        Player ludo = new Player();
        ludo.setName("Ludas");

        // main list of players
        PlayerList players = new PlayerList();
        players.add(pm);
        players.add(ludo);
        players.add(arthur);
        players.add(guillaume);

        // game creation
        StandardTarot4Game game = new StandardTarot4Game();
        game.setPlayers(players);
        game.setLeadingPlayer(guillaume);
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
     * Test method for {@link StandardTarot4GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForPriseAndZeroOudler() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(0);
        game.setBet(Bet.PRISE);
        game.setPoints(45.0);

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeBasePoints();

        // PRISE perdue de 11 = -20 + (-11 * 2) = -42
        assertEquals(-42, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForPriseAndOneOudler() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(1);
        game.setBet(Bet.PRISE);
        game.setPoints(45.0);

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeBasePoints();

        // PRISE perdue de 6 = -20 - (6 * 2) = -32
        assertEquals(-32, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForPriseAndTwoOudlers() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.PRISE);
        game.setPoints(45.0);

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeBasePoints();

        // PRISE gagnee de 4 = 20 + (4 * 2) = 28
        assertEquals(28, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForPriseAndThreeOudlers() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(3);
        game.setBet(Bet.PRISE);
        game.setPoints(45.0);

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeBasePoints();

        // PRISE gagnee de 9 = 20 + (9 * 2) = 38
        assertEquals(38, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForGardeAndZeroOudler() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(0);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeBasePoints();

        // GARDE perdue de 11 = -40 -(11 * 4)
        int expectedBasePoints = -40 - (11 * 4);

        assertEquals(expectedBasePoints, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForGardeAndOneOudler() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(1);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        //scoreComputer.setGame(game);
        scoreComputer.computeBasePoints();

        // GARDE perdue de 6 = -40 -(6 * 4)
        int expectedBasePoints = -40 - (6 * 4);

        assertEquals(expectedBasePoints, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForGardeAndTwoOudlers() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        //scoreComputer.setGame(game);
        scoreComputer.computeBasePoints();

        // GARDE gagnee de 4 = 40 +(4 * 4)
        int expectedBasePoints = 40 + (4 * 4);

        assertEquals(expectedBasePoints, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForGardeAndThreeOudlers() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(3);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeBasePoints();

        // GARDE gagnee de 9 = 40 +(9 * 4)
        int expectedBasePoints = 40 + (9 * 4);

        assertEquals(expectedBasePoints, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForGardeSansAndZeroOudler() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(0);
        game.setBet(Bet.GARDESANS);
        game.setPoints(45.0);

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeBasePoints();

        // GARDE sans perdue de 11 = -80 -(11 * 8)
        int expectedBasePoints = -80 - (11 * 8);

        assertEquals(expectedBasePoints, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForGardeSansAndOneOudler() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(1);
        game.setBet(Bet.GARDESANS);
        game.setPoints(45.0);

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeBasePoints();

        // GARDE sans perdue de 6 = -80 -(6 * 8)
        int expectedBasePoints = -80 - (6 * 8);

        assertEquals(expectedBasePoints, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForGardeSansAndTwoOudlers() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDESANS);
        game.setPoints(45.0);

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeBasePoints();

        // GARDE sans gagnee de 4 = 80 +(4 * 8)
        int expectedBasePoints = 80 + (4 * 8);

        assertEquals(expectedBasePoints, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForGardeSansAndThreeOudlers() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(3);
        game.setBet(Bet.GARDESANS);
        game.setPoints(45.0);

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeBasePoints();

        // GARDE sans gagnee de 9 = 80 +(9 * 8)
        int expectedBasePoints = 80 + (9 * 8);

        assertEquals(expectedBasePoints, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForGardeContreAndZeroOudler() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(0);
        game.setBet(Bet.GARDECONTRE);
        game.setPoints(45.0);

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeBasePoints();

        // GARDE contre perdue de 11 = -160 -(11 * 16)
        int expectedBasePoints = -160 - (11 * 16);

        assertEquals(expectedBasePoints, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForGardeContreAndOneOudler() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(1);
        game.setBet(Bet.GARDECONTRE);
        game.setPoints(45.0);

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeBasePoints();

        // GARDE contre perdue de 6 = -160 -(6 * 16)
        int expectedBasePoints = -160 - (6 * 16);

        assertEquals(expectedBasePoints, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForGardeContreAndTwoOudlers() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDECONTRE);
        game.setPoints(45.0);

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeBasePoints();

        // GARDE contre gagnee de 4 = 160 +(4 * 16)
        int expectedBasePoints = 160 + (4 * 16);

        assertEquals(expectedBasePoints, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForGardeContreAndThreeOudlers() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(3);
        game.setBet(Bet.GARDECONTRE);
        game.setPoints(45.0);

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeBasePoints();

        // GARDE contre gagnee de 9 = 160 +(9 * 16)
        int expectedBasePoints = 160 + (9 * 16);

        assertEquals(expectedBasePoints, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#computeBasePoints()}.
     */
    @Test
    public final void testComputeBaseScoreForZeroAdditionnalPoints() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(3);
        game.setBet(Bet.GARDECONTRE);
        game.setPoints(36.0);

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeBasePoints();

        // GARDE contre passee de 0 = 160 +(0 * 16)
        int expectedBasePoints = 160;

        assertEquals(expectedBasePoints, scoreComputer.getBasePoints());
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#getGameScores()}.
     */
    @Test
    public final void testComputeEachIndividualScoreWhenLeaderLoses() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(0);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);    // -11 points

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        //scoreComputer.computeBasePoints();
        //scoreComputer.computeEachIndividualResult();
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE chutee de 11, on doit avoir les resultats suivants :
        // guillaume : (-40 + (-11*4)) * 3 => -252
        // arthur : 40 + (11*4) => 84
        // pm : 40 + (11*4) => 84
        // ludo : 40 + (11*4) => 84

        assertEquals(-252, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(84, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(84, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(84, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#getGameScores()}.
     */
    @Test
    public final void testComputeEachIndividualScoreWhenLeaderWins() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);    // +4 points

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE passee de 4, on doit avoir les resultats suivants :
        // guillaume : (40 + (4*4)) * 3 => 168
        // arthur : -40 + (-4*4) => -56
        // pm : -40 + (-4*4) => -56
        // ludo : -40 + (-4*4) => -56

        assertEquals(168, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-56, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-56, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(-56, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#getGameScores()}.
     */
    @Test
    public final void testTakeInAccountOnePoigneeForLeadingTeamThatWins() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);
        game.setTeamWithPoignee(Team.LEADING_TEAM);

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE passee de 4, on doit avoir les resultats suivants :
        // guillaume : (40 +(4*4) +10) * 3 => 198
        // arthur : => -66
        // pm : => -66
        // ludo : => -66

        assertEquals(198, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-66, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-66, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(-66, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#getGameScores()}.
     */
    @Test
    public final void testTakeInAccountOnePoigneeForBothTeamsAndLeadingTeamThatWins() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);
        game.setTeamWithPoignee(Team.BOTH_TEAMS);

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE passee de 4, on doit avoir les resultats suivants :
        // guillaume : (40 +(4*4) +10+10) * 3 => 228
        // arthur : => -76
        // pm : => -76
        // ludo : => -76

        assertEquals(228, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-76, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-76, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(-76, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#getGameScores()}.
     */
    @Test
    public final void testTakeInAccountOnePoigneeForBothTeamsAndDefenseTeamThatWins() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(37.0);
        game.setTeamWithPoignee(Team.BOTH_TEAMS);

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE passee de 4, on doit avoir les resultats suivants :
        // guillaume : (-40 -(4*4) -10 -10) * 3 => -228
        // arthur : => 76
        // pm : => 76
        // ludo : => 76

        assertEquals(-228, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(76, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(76, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(76, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#getGameScores()}.
     */
    @Test
    public final void testTakeInAccountOneDoublePoigneeForLeadingTeamThatLoses() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(37.0);
        game.setTeamWithDoublePoignee(Team.LEADING_TEAM);

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE chutee de 4, on doit avoir les resultats suivants :
        // guillaume : (-40 -(4*4) -20) * 3 => -228
        // arthur : => 76
        // pm : => 76
        // ludo : => 76

        assertEquals(-228, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(76, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(76, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(76, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#getGameScores()}.
     */
    @Test(expected = IllegalStateException.class)
    public final void testTakeInAccountOneDoublePoigneeForBothTeams() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(37.0);
        game.setTeamWithDoublePoignee(Team.BOTH_TEAMS);

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        scoreComputer.getGameScores();
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#getGameScores()}.
     */
    @Test
    public final void testTakeInAccountOneTriplePoigneeForDefenseTeamThatWins() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(37.0);
        game.setTeamWithTriplePoignee(Team.DEFENSE_TEAM);

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE chutee de 4, on doit avoir les resultats suivants :
        // guillaume : (-40 -(4*4) -30) * 3 => -258
        // arthur : => 86
        // pm : => 86
        // ludo : => 86

        assertEquals(-258, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(86, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(86, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(86, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#getGameScores()}.
     */
    @Test
    public final void testTakeInAccountOneTriplePoigneeForDefenseTeamThatLoses() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);
        game.setTeamWithTriplePoignee(Team.DEFENSE_TEAM);

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE passee de 4, on doit avoir les resultats suivants :
        // guillaume : (40 +(4*4) +30) * 3 => 258
        // arthur : => -86
        // pm : => -86
        // ludo : => -86

        assertEquals(258, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-86, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-86, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(-86, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#getGameScores()}.
     */
    @Test(expected = IllegalStateException.class)
    public final void testTakeInAccountOneTriplePoigneeForBothTeams() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(37.0);
        game.setTeamWithTriplePoignee(Team.BOTH_TEAMS);

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        scoreComputer.getGameScores();
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#getGameScores()}.
     */
    @Test
    public final void testTakeInAccountKidAtTheEndForLeadingTeamForPrise() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.PRISE);
        game.setPoints(45.0);    // +4 points
        game.setTeamWithKidAtTheEnd(Team.LEADING_TEAM);

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // Prise passee de 4 + Petit au bout pour l'attaque, on doit avoir les resultats suivants :
        // guillaume : (20 + (4*2) + 10*2) * 3 => 144
        // arthur :  => -48
        // pm :  => -48
        // ludas :  => -48

        assertEquals(144, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-48, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-48, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(-48, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#getGameScores()}.
     */
    @Test
    public final void testTakeInAccountKidAtTheEndForLeadingTeamForGarde() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);    // +4 points
        game.setTeamWithKidAtTheEnd(Team.LEADING_TEAM);

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE passee de 4 + Petit au bout pour l'attaque, on doit avoir les resultats suivants :
        // guillaume : (40 + (4*4) + 10 * 4) * 3 => 288
        // arthur :  => -96
        // pm :  => -96
        // ludas :  => -96

        assertEquals(288, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-96, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-96, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(-96, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#getGameScores()}.
     */
    @Test
    public final void testTakeInAccountKidAtTheEndForLeadingTeamForGardeSans() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDESANS);
        game.setPoints(45.0);    // +4 points
        game.setTeamWithKidAtTheEnd(Team.LEADING_TEAM);

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE Sans passee de 4 + Petit au bout pour l'attaque, on doit avoir les resultats suivants :
        // guillaume : (80 + (4*8) + 10 * 8) * 3 => 576
        // arthur :  => -192
        // pm :  => -192
        // ludas :  => -192

        assertEquals(576, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-192, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-192, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(-192, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#getGameScores()}.
     */
    @Test
    public final void testTakeInAccountKidAtTheEndForLeadingTeamForGardeContre() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDECONTRE);
        game.setPoints(45.0);    // +4 points
        game.setTeamWithKidAtTheEnd(Team.LEADING_TEAM);

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE Contre passee de 4 + Petit au bout pour l'attaque, on doit avoir les resultats suivants :
        // guillaume : (160 + (4*16) + 10 * 16) * 3 => 1152
        // arthur :  => -384
        // pm :  => -384
        // ludas :  => -384

        assertEquals(1152, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-384, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-384, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(-384, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#getGameScores()}.
     */
    @Test
    public final void testTakeInAccountKidAtTheEndForDefenseTeamForPrise() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.PRISE);
        game.setPoints(45.0);    // +4 points
        game.setTeamWithKidAtTheEnd(Team.DEFENSE_TEAM);

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // Prise passee de 4 + Petit au bout pour la defense, on doit avoir les resultats suivants :
        // guillaume : (20 + (4*2) - 10*2) * 3 => 24
        // arthur :  => -8
        // pm :  => -8
        // ludas :  => -8

        assertEquals(24, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-8, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-8, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(-8, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#getGameScores()}.
     */
    @Test
    public final void testTakeInAccountKidAtTheEndForDefenseTeamForGarde() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);    // +4 points
        game.setTeamWithKidAtTheEnd(Team.DEFENSE_TEAM);

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE passee de 4 + Petit au bout pour la defense, on doit avoir les resultats suivants :
        // guillaume : (40 + (4*4) - 10 * 4) * 3 => 48
        // arthur :  => -16
        // pm :  => -16
        // ludas :  => -16

        assertEquals(48, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-16, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-16, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(-16, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#getGameScores()}.
     */
    @Test
    public final void testTakeInAccountKidAtTheEndForDefenseTeamForGardeSans() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDESANS);
        game.setPoints(45.0);    // +4 points
        game.setTeamWithKidAtTheEnd(Team.DEFENSE_TEAM);

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE Sans passee de 4 + Petit au bout pour la defense, on doit avoir les resultats suivants :
        // guillaume : (80 + (4*8) - 10 * 8) * 3 => 96
        // arthur :  => -32
        // pm :  => -32
        // ludas :  => -32

        assertEquals(96, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-32, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-32, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(-32, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#getGameScores()}.
     */
    @Test
    public final void testTakeInAccountKidAtTheEndForDefenseTeamForGardeContre() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDECONTRE);
        game.setPoints(45.0);    // +4 points
        game.setTeamWithKidAtTheEnd(Team.DEFENSE_TEAM);

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE Contre passee de 4 + Petit au bout pour la defense, on doit avoir les resultats suivants :
        // guillaume : (160 + (4*16) + 10 * 16) * 3 => 192
        // arthur :  => -64
        // pm :  => -64
        // ludas :  => -64

        assertEquals(192, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-64, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-64, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(-64, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#getGameScores()}.
     */
    @Test
    public final void testTakeInAccountChelemAnouncedAndSucceeded() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);    // +4 points
        game.setChelem(Chelem.CHELEM_ANOUNCED_AND_SUCCEEDED); // +400 * 3 pour guillaume, -400 par d�fenseur

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE passee de 4, on doit avoir les resultats suivants :
        // guillaume : (40 + (4*4)) * 3 + 400 => 168 + 400 * 3 => 1368
        // arthur : -40 + (-4*4) -400 => -456
        // pm : -40 + (-4*4) -400 => -456
        // ludo : -40 + (-4*4) -400 => -456

        assertEquals(1368, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-456, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-456, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(-456, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#getGameScores()}.
     */
    @Test
    public final void testTakeInAccountChelemAnouncedAndFailed() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);    // +4 points
        game.setChelem(Chelem.CHELEM_ANOUNCED_AND_FAILED); // -200 * 3 pour guillaume, +200 par d�fenseur

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE passee de 4, on doit avoir les resultats suivants :
        // guillaume : (40 + (4*4)) * 3 + 400 => 168 -200 * 3 => -432
        // arthur : -40 + (-4*4) +200 => 144
        // pm : -40 + (-4*4) +200 => 144
        // ludo : -40 + (-4*4) +200 => 144

        assertEquals(-432, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(144, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(144, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(144, score.getIndividualResult(game.getPlayerForName("ludas")));
    }

    /**
     * Test method for {@link StandardTarot4GameScoresComputer#getGameScores()}.
     */
    @Test
    public final void testTakeInAccountChelemNotAnouncedButSucceeded() {
        StandardTarot4Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);    // +4 points
        game.setChelem(Chelem.CHELEM_NOT_ANOUNCED_BUT_SUCCEEDED); // +200 * 3 pour guillaume, -200 par d�fenseus

        StandardTarot4GameScoresComputer scoreComputer = new StandardTarot4GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE passee de 4, on doit avoir les resultats suivants :
        // guillaume : (40 + (4*4)) * 3 + 400 => 168 + 200 * 3 => 768
        // arthur : -40 + (-4*4) -200 => -256
        // pm : -40 + (-4*4) -200 => -256
        // ludo : -40 + (-4*4) -200 => -256

        assertEquals(768, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-256, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-256, score.getIndividualResult(game.getPlayerForName("pm")));
        assertEquals(-256, score.getIndividualResult(game.getPlayerForName("ludas")));
    }
}
