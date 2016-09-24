package org.nla.tarotdroid;

import org.junit.Test;
import org.nla.tarotdroid.biz.Bet;
import org.nla.tarotdroid.biz.Chelem;
import org.nla.tarotdroid.biz.GameScores;
import org.nla.tarotdroid.biz.GameSetParameters;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.PlayerList;
import org.nla.tarotdroid.biz.StandardTarot3Game;
import org.nla.tarotdroid.biz.Team;
import org.nla.tarotdroid.biz.computers.StandardTarot3GameScoresComputer;

import static org.junit.Assert.assertEquals;


public class StandardTarot3GameScoresComputerTest {


    private StandardTarot3Game createGame() {
        // players creation
        Player pm = new Player();
        pm.setName("PM");

        Player guillaume = new Player();
        guillaume.setName("Guillaume");

        Player arthur = new Player();
        arthur.setName("Arthur");

        // main list of players
        PlayerList players = new PlayerList();
        players.add(pm);
        players.add(arthur);
        players.add(guillaume);

        // game creation
        StandardTarot3Game game = new StandardTarot3Game();
        game.setPlayers(players);
        game.setLeadingPlayer(guillaume);
        game.setGameSetParameters(createCustomGameSetParameters());

        return game;
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
    public final void testComputeEachIndividualScoreWhenLeaderLoses() {
        StandardTarot3Game game = createGame();
        game.setNumberOfOudlers(0);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);    // -11 points

        StandardTarot3GameScoresComputer scoreComputer = new StandardTarot3GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE chutee de 11, on doit avoir les resultats suivants :
        // guillaume : (-40 + (-11*4)) * 2 => -168
        // arthur : 40 + (11*4) => 84
        // pm : 40 + (11*4) => 84

        assertEquals(-168, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(84, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(84, score.getIndividualResult(game.getPlayerForName("pm")));
    }


    @Test
    public final void testComputeEachIndividualScoreWhenLeaderWins() {
        StandardTarot3Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);    // +4 points

        StandardTarot3GameScoresComputer scoreComputer = new StandardTarot3GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE passee de 4, on doit avoir les resultats suivants :
        // guillaume : (40 + (4*4)) * 2 => 112
        // arthur : -40 + (-4*4) => -56
        // pm : -40 + (-4*4) => -56

        assertEquals(112, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-56, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-56, score.getIndividualResult(game.getPlayerForName("pm")));
    }


    @SuppressWarnings("deprecation")
    @Test
    public final void testTakeInAccountOnePoigneeForLeadingTeamThatWinsHistoric() {
        StandardTarot3Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);
        game.setPlayerWithPoignee(game.getPlayerForName("guilaume"));

        StandardTarot3GameScoresComputer scoreComputer = new StandardTarot3GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE passee de 4, on doit avoir les resultats suivants :
        // guillaume : (40 + (4*4)+10) * 2 => 112
        // arthur :  => -56
        // pm : => -56

        assertEquals(112, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-56, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-56, score.getIndividualResult(game.getPlayerForName("pm")));
    }


    @SuppressWarnings("deprecation")
    @Test
    public final void testTakeInAccountOneDoublePoigneeForLeadingTeamThatLoses() {
        StandardTarot3Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(37.0);
        game.setPlayerWithDoublePoignee(game.getPlayerForName("guillaume"));

        StandardTarot3GameScoresComputer scoreComputer = new StandardTarot3GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE chutee de 4, on doit avoir les resultats suivants :
        // guillaume : (-40 -(4*4)-20) * 2 => -152
        // arthur : => 76
        // pm : => 76

        assertEquals(-152, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(76, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(76, score.getIndividualResult(game.getPlayerForName("pm")));
    }


    @SuppressWarnings("deprecation")
    @Test
    public final void testTakeInAccountOneTriplePoigneeForDefenseTeamThatWinsHistoric() {
        StandardTarot3Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(37.0);
        game.setPlayerWithTriplePoignee(game.getPlayerForName("PM"));

        StandardTarot3GameScoresComputer scoreComputer = new StandardTarot3GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE chutee de 4 avec poignee pour la defense, on doit avoir les resultats suivants :
        // guillaume : (-40 -(4*4)-30) * 2 => -172
        // arthur : => 86
        // pm : => 86

        assertEquals(-172, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(86, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(86, score.getIndividualResult(game.getPlayerForName("pm")));
    }


    @SuppressWarnings("deprecation")
    @Test
    public final void testTakeInAccountOneTriplePoigneeForDefenseTeamThatLosesHistoric() {
        StandardTarot3Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);
        game.setPlayerWithTriplePoignee(game.getPlayerForName("PM"));

        StandardTarot3GameScoresComputer scoreComputer = new StandardTarot3GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE passee de 4 avec poignee pour la defense (perdue), on doit avoir les resultats suivants :
        // guillaume : (40 +(4*4)+30) * 2 => 172
        // arthur : => -86
        // pm : => -86

        assertEquals(172, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-86, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-86, score.getIndividualResult(game.getPlayerForName("pm")));
    }


    @Test
    public final void testTakeInAccountKidAtTheEndForLeadingTeamForPrise() {
        StandardTarot3Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.PRISE);
        game.setPoints(45.0);    // +4 points
        game.setTeamWithKidAtTheEnd(Team.LEADING_TEAM);

        StandardTarot3GameScoresComputer scoreComputer = new StandardTarot3GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // Prise passee de 4 + Petit au bout pour l'attaque, on doit avoir les resultats suivants :
        // guillaume : (20 + (4*2) + 10*2) * 2 => 96
        // arthur :  => -48
        // pm :  => -48

        assertEquals(96, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-48, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-48, score.getIndividualResult(game.getPlayerForName("pm")));
    }


    @Test
    public final void testTakeInAccountKidAtTheEndForLeadingTeamForGarde() {
        StandardTarot3Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);    // +4 points
        game.setTeamWithKidAtTheEnd(Team.LEADING_TEAM);

        StandardTarot3GameScoresComputer scoreComputer = new StandardTarot3GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE passee de 4 + Petit au bout pour l'attaque, on doit avoir les resultats suivants :
        // guillaume : (40 + (4*4) + 10 * 4) * 2 => 192
        // arthur :  => -96
        // pm :  => -96

        assertEquals(192, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-96, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-96, score.getIndividualResult(game.getPlayerForName("pm")));
    }


    @Test
    public final void testTakeInAccountKidAtTheEndForLeadingTeamForGardeSans() {
        StandardTarot3Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDESANS);
        game.setPoints(45.0);    // +4 points
        game.setTeamWithKidAtTheEnd(Team.LEADING_TEAM);

        StandardTarot3GameScoresComputer scoreComputer = new StandardTarot3GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE Sans passee de 4 + Petit au bout pour l'attaque, on doit avoir les resultats suivants :
        // guillaume : (80 + (4*8) + 10 * 8) * 2 => 384
        // arthur :  => -192
        // pm :  => -192

        assertEquals(384, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-192, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-192, score.getIndividualResult(game.getPlayerForName("pm")));
    }


    @Test
    public final void testTakeInAccountKidAtTheEndForLeadingTeamForGardeContre() {
        StandardTarot3Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDECONTRE);
        game.setPoints(45.0);    // +4 points
        game.setTeamWithKidAtTheEnd(Team.LEADING_TEAM);

        StandardTarot3GameScoresComputer scoreComputer = new StandardTarot3GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE Contre passee de 4 + Petit au bout pour l'attaque, on doit avoir les resultats suivants :
        // guillaume : (160 + (4*16) + 10 * 16) * 2 => 768
        // arthur :  => -384
        // pm :  => -384

        assertEquals(768, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-384, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-384, score.getIndividualResult(game.getPlayerForName("pm")));
    }


    @Test
    public final void testTakeInAccountKidAtTheEndForDefenseTeamForPrise() {
        StandardTarot3Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.PRISE);
        game.setPoints(45.0);    // +4 points
        game.setTeamWithKidAtTheEnd(Team.DEFENSE_TEAM);

        StandardTarot3GameScoresComputer scoreComputer = new StandardTarot3GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // Prise passee de 4 + Petit au bout pour la defense, on doit avoir les resultats suivants :
        // guillaume : (20 + (4*2) - 10*2) * 2 => 16
        // arthur :  => -8
        // pm :  => -8

        assertEquals(16, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-8, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-8, score.getIndividualResult(game.getPlayerForName("pm")));
    }


    @Test
    public final void testTakeInAccountKidAtTheEndForDefenseTeamForGarde() {
        StandardTarot3Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);    // +4 points
        game.setTeamWithKidAtTheEnd(Team.DEFENSE_TEAM);

        StandardTarot3GameScoresComputer scoreComputer = new StandardTarot3GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE passee de 4 + Petit au bout pour la defense, on doit avoir les resultats suivants :
        // guillaume : (40 + (4*4) - 10 * 4) * 2 => 32
        // arthur :  => -16
        // pm :  => -16

        assertEquals(32, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-16, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-16, score.getIndividualResult(game.getPlayerForName("pm")));
    }


    @Test
    public final void testTakeInAccountKidAtTheEndForDefenseTeamForGardeSans() {
        StandardTarot3Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDESANS);
        game.setPoints(45.0);    // +4 points
        game.setTeamWithKidAtTheEnd(Team.DEFENSE_TEAM);

        StandardTarot3GameScoresComputer scoreComputer = new StandardTarot3GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE Sans passee de 4 + Petit au bout pour la defense, on doit avoir les resultats suivants :
        // guillaume : (80 + (4*8) - 10 * 8) * 2 => 64
        // arthur :  => -32
        // pm :  => -32

        assertEquals(64, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-32, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-32, score.getIndividualResult(game.getPlayerForName("pm")));
    }


    @Test
    public final void testTakeInAccountKidAtTheEndForDefenseTeamForGardeContre() {
        StandardTarot3Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDECONTRE);
        game.setPoints(45.0);    // +4 points
        game.setTeamWithKidAtTheEnd(Team.DEFENSE_TEAM);

        StandardTarot3GameScoresComputer scoreComputer = new StandardTarot3GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE Contre passee de 4 + Petit au bout pour la defense, on doit avoir les resultats suivants :
        // guillaume : (160 + (4*16) + 10 * 16) * 2 => 128
        // arthur :  => -64
        // pm :  => -64

        assertEquals(128, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-64, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-64, score.getIndividualResult(game.getPlayerForName("pm")));
    }


    @SuppressWarnings("deprecation")
    @Test
    public final void testTakeInAccountKidAtTheEndForLeadingTeamForPriseHistorical() {
        StandardTarot3Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.PRISE);
        game.setPoints(45.0);    // +4 points
        game.setPlayerWithKidAtTheEnd(game.getPlayerForName("guillaume"));

        StandardTarot3GameScoresComputer scoreComputer = new StandardTarot3GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // Prise passee de 4 + Petit au bout pour l'attaque, on doit avoir les resultats suivants :
        // guillaume : (20 + (4*2) + 10*2) * 2 => 96
        // arthur :  => -48
        // pm :  => -48

        assertEquals(96, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-48, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-48, score.getIndividualResult(game.getPlayerForName("pm")));
    }


    @SuppressWarnings("deprecation")
    @Test
    public final void testTakeInAccountKidAtTheEndForDefenseTeamForPriseHistorical() {
        StandardTarot3Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.PRISE);
        game.setPoints(45.0);    // +4 points
        game.setPlayerWithKidAtTheEnd(game.getPlayerForName("arthur"));

        StandardTarot3GameScoresComputer scoreComputer = new StandardTarot3GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // Prise passee de 4 + Petit au bout pour la defense, on doit avoir les resultats suivants :
        // guillaume : (20 + (4*2) - 10*2) * 2 => 16
        // arthur :  => -8
        // pm :  => -8

        assertEquals(16, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-8, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-8, score.getIndividualResult(game.getPlayerForName("pm")));
    }


    @Test
    public final void testTakeInAccountChelemAnouncedAndSucceeded() {
        StandardTarot3Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);    // +4 points
        game.setChelem(Chelem.CHELEM_ANOUNCED_AND_SUCCEEDED); // +400 * 2 pour guillaume, -400 par d�fenseur

        StandardTarot3GameScoresComputer scoreComputer = new StandardTarot3GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE passee de 4, on doit avoir les resultats suivants :
        // guillaume : (40 + (4*4)) + 400 )* 2 => 912
        // arthur : -40 + (-4*4) -400 => -456
        // pm : -40 + (-4*4) -400 => -456

        assertEquals(912, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-456, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-456, score.getIndividualResult(game.getPlayerForName("pm")));
    }


    @Test
    public final void testTakeInAccountChelemAnouncedAndFailed() {
        StandardTarot3Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);    // +4 points
        game.setChelem(Chelem.CHELEM_ANOUNCED_AND_FAILED); // -200 * 2 pour guillaume, +200 par d�fenseur

        StandardTarot3GameScoresComputer scoreComputer = new StandardTarot3GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE passee de 4, on doit avoir les resultats suivants :
        // guillaume : (40 + (4*4)) - 200) * 2 => -288
        // arthur : -40 + (-4*4) +200 => 144
        // pm : -40 + (-4*4) +200 => 144

        assertEquals(-288, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(144, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(144, score.getIndividualResult(game.getPlayerForName("pm")));
    }


    @Test
    public final void testTakeInAccountChelemNotAnouncedButSucceeded() {
        StandardTarot3Game game = createGame();
        game.setNumberOfOudlers(2);
        game.setBet(Bet.GARDE);
        game.setPoints(45.0);    // +4 points
        game.setChelem(Chelem.CHELEM_NOT_ANOUNCED_BUT_SUCCEEDED); // +200 * 2 pour guillaume, -200 par d�fenseus

        StandardTarot3GameScoresComputer scoreComputer = new StandardTarot3GameScoresComputer(game,
                                                                                              createCustomGameSetParameters());
        scoreComputer.computeScore();
        GameScores score = scoreComputer.getGameScores();

        // GARDE passee de 4, on doit avoir les resultats suivants :
        // guillaume : (40 + (4*4)) + 400) * 2=> 512
        // arthur : -40 + (-4*4) -200 => -256
        // pm : -40 + (-4*4) -200 => -256

        assertEquals(512, score.getIndividualResult(game.getPlayerForName("guillaume")));
        assertEquals(-256, score.getIndividualResult(game.getPlayerForName("arthur")));
        assertEquals(-256, score.getIndividualResult(game.getPlayerForName("pm")));
    }
}
