package org.nla.tarotdroid;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import junit.framework.TestCase;

import org.junit.Test;
import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.Bet;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.GameSetParameters;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.PlayerList;
import org.nla.tarotdroid.biz.StandardTarot3Game;
import org.nla.tarotdroid.biz.StandardTarot4Game;
import org.nla.tarotdroid.biz.StandardTarot5Game;
import org.nla.tarotdroid.biz.computers.IPlayerStatisticsComputer;
import org.nla.tarotdroid.biz.computers.PlayerStatisticsComputerFactory;
import org.nla.tarotdroid.biz.enums.BetType;
import org.nla.tarotdroid.biz.enums.GameStyleType;
import org.nla.tarotdroid.biz.enums.ResultType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class GuavaPlayerStatisticsComputerTest extends TestCase {


    private GameSetParameters gameSetParameters;


    private Player nicol;


    private Player jk;


    private Player ludas;


    private Player guillaume;


    private Player arthur;


    private Player pm;


    private Player cyril;

    private static GameSet createGameSet(
            final GameSetParameters gameSetParameters,
            final GameStyleType gameStyleType,
            final List<Player> players
    ) {
        GameSet gameSet = new GameSet();
        gameSet.setGameStyleType(gameStyleType);
        gameSet.setPlayers(new PlayerList(players));
        gameSet.setGameSetParameters(gameSetParameters);
        return gameSet;
    }

    private StandardTarot3Game create3PlayerStyleGame(
            final GameSetParameters gameSetParameters,
            final List<Player> players,
            final Player leader,
            final ResultType resultType
    ) {
        StandardTarot3Game game = new StandardTarot3Game();
        game.setPlayers(new PlayerList(players));
        game.setLeadingPlayer(leader);
        game.setGameSetParameters(gameSetParameters);
        game.setBet(Bet.GARDE);
        game.setNumberOfOudlers(2);
        game.setPoints(resultType == ResultType.Failure ? 30 : 50);
        return game;
    }

    private StandardTarot3Game create3PlayerStyleGame(
            final GameSetParameters gameSetParameters,
            final List<Player> players,
            final Player leader,
            final Bet bet,
            final ResultType resultType
    ) {
        StandardTarot3Game game = create3PlayerStyleGame(gameSetParameters,
                                                         players,
                                                         leader,
                                                         resultType);
        game.setBet(bet);
        return game;
    }

    private StandardTarot4Game create4PlayerStyleGame(
            final GameSetParameters gameSetParameters,
            final List<Player> players,
            final Player leader,
            final ResultType resultType
    ) {
        StandardTarot4Game game = new StandardTarot4Game();
        game.setPlayers(new PlayerList(players));
        game.setLeadingPlayer(leader);
        game.setGameSetParameters(gameSetParameters);
        game.setBet(Bet.GARDE);
        game.setNumberOfOudlers(2);
        game.setPoints(resultType == ResultType.Failure ? 30 : 50);
        return game;
    }

    private StandardTarot4Game create4PlayerStyleGame(
            final GameSetParameters gameSetParameters,
            final List<Player> players,
            final Player leader,
            final Bet bet,
            final ResultType resultType
    ) {
        StandardTarot4Game game = create4PlayerStyleGame(gameSetParameters,
                                                         players,
                                                         leader,
                                                         resultType);
        game.setBet(bet);
        return game;
    }

    private StandardTarot5Game create5PlayerStyleGame(
            final GameSetParameters gameSetParameters,
            final List<Player> players,
            final Player leader,
            final ResultType resultType
    ) {
        StandardTarot5Game game = new StandardTarot5Game();
        game.setPlayers(new PlayerList(players));
        game.setLeadingPlayer(leader);
        game.setCalledPlayer(leader);
        game.setGameSetParameters(gameSetParameters);
        game.setBet(Bet.GARDE);
        game.setNumberOfOudlers(2);
        game.setPoints(resultType == ResultType.Failure ? 30 : 50);
        return game;
    }

    private StandardTarot5Game create5PlayerStyleGame(
            final GameSetParameters gameSetParameters,
            final List<Player> players,
            final Player leader,
            final Player called,
            final ResultType resultType
    ) {
        StandardTarot5Game game = new StandardTarot5Game();
        game.setPlayers(new PlayerList(players));
        game.setLeadingPlayer(leader);
        game.setCalledPlayer(called);
        game.setGameSetParameters(gameSetParameters);
        game.setBet(Bet.GARDE);
        game.setNumberOfOudlers(2);
        game.setPoints(resultType == ResultType.Failure ? 30 : 50);
        return game;
    }

    private StandardTarot5Game create5PlayerStyleGame(
            final GameSetParameters gameSetParameters,
            final List<Player> players,
            final Player leader,
            final Bet bet,
            final ResultType resultType
    ) {
        StandardTarot5Game game = create5PlayerStyleGame(gameSetParameters,
                                                         players,
                                                         leader,
                                                         resultType);
        game.setBet(bet);
        return game;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // game set parameters
        this.gameSetParameters = new GameSetParameters();

        // players creation
        this.nicol = new Player("NicoL");
        this.jk = new Player("JK");
        this.arthur = new Player("Arthur");
        this.ludas = new Player("Ludas");
        this.guillaume = new Player("Guillaume");
        this.pm = new Player("PM");
        this.cyril = new Player("Cyril");
    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        this.gameSetParameters = null;
        this.nicol = null;
        this.jk = null;
        this.arthur = null;
        this.ludas = null;
        this.guillaume = null;
        this.pm = null;
        this.cyril = null;
    }


    @Test
    public void testGetGameSetCountForPlayer() {
        List<Player> list1 = Arrays.asList(this.nicol, this.guillaume, this.jk);
        List<Player> list2 = Arrays.asList(this.guillaume, this.jk, this.arthur);
        List<Player> list3 = Arrays.asList(this.ludas, this.guillaume, this.nicol, this.cyril);
        List<Player> list4 = Arrays.asList(this.ludas, this.guillaume, this.arthur, this.jk);
        List<Player> list5 = Arrays.asList(this.jk, this.arthur, this.nicol, this.cyril);
        List<Player> list6 = Arrays.asList(this.cyril,
                                           this.arthur,
                                           this.nicol,
                                           this.jk,
                                           this.guillaume);

        GameSet gameSet1 = createGameSet(this.gameSetParameters,
                                         GameStyleType.Tarot3,
                                         list1); // <= nicoL here
        GameSet gameSet2 = createGameSet(this.gameSetParameters, GameStyleType.Tarot3, list2);
        GameSet gameSet3 = createGameSet(this.gameSetParameters,
                                         GameStyleType.Tarot4,
                                         list3); // <= nicoL here
        GameSet gameSet4 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list4);
        GameSet gameSet5 = createGameSet(this.gameSetParameters,
                                         GameStyleType.Tarot4,
                                         list5); // <= nicoL here
        GameSet gameSet6 = createGameSet(this.gameSetParameters,
                                         GameStyleType.Tarot5,
                                         list6); // <= nicoL here

        IPlayerStatisticsComputer nicoLComputer = PlayerStatisticsComputerFactory.GetPlayerStatisticsComputer(
                Arrays.asList(gameSet1, gameSet2, gameSet3, gameSet4, gameSet5, gameSet6),
                this.nicol,
                "guava"
        );

        int expectedForNicoL = 4;
        int actualForNicoL = nicoLComputer.getGameSetCountForPlayer();
        assertEquals("Incorrect number of GameSets in which is NicoL",
                     expectedForNicoL,
                     actualForNicoL);

        IPlayerStatisticsComputer pmComputer = PlayerStatisticsComputerFactory.GetPlayerStatisticsComputer(
                Arrays.asList(gameSet1, gameSet2, gameSet3, gameSet4, gameSet5, gameSet6),
                this.pm,
                "guava"
        );

        int expectedForPm = 0;
        int actualForPm = pmComputer.getGameSetCountForPlayer();
        assertEquals("Incorrect number of GameSets in which is PM", expectedForPm, actualForPm);
    }


    @Test
    public void testGetWonAndLostGameSetsForPlayer() {
        List<Player> list1 = Arrays.asList(this.nicol, this.ludas, this.jk);
        List<Player> list2 = Arrays.asList(this.nicol, this.ludas, this.arthur);
        List<Player> list3 = Arrays.asList(this.guillaume, this.nicol, this.cyril, this.ludas);
        List<Player> list4 = Arrays.asList(this.nicol, this.ludas, this.arthur, this.jk);
        List<Player> list5 = Arrays.asList(this.jk, this.ludas, this.nicol, this.cyril);
        List<Player> list6 = Arrays.asList(this.cyril,
                                           this.arthur,
                                           this.nicol,
                                           this.ludas,
                                           this.guillaume);

        GameSet gameSet1 = createGameSet(this.gameSetParameters, GameStyleType.Tarot3, list1);
        GameSet gameSet2 = createGameSet(this.gameSetParameters, GameStyleType.Tarot3, list2);
        GameSet gameSet3 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list3);
        GameSet gameSet4 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list4);
        GameSet gameSet5 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list5);
        GameSet gameSet6 = createGameSet(this.gameSetParameters, GameStyleType.Tarot5, list6);

        gameSet1.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list1,
                                                this.nicol,
                                                ResultType.Success));
        gameSet2.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list2,
                                                this.nicol,
                                                ResultType.Failure));
        gameSet3.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list3,
                                                this.nicol,
                                                ResultType.Success));
        gameSet4.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list4,
                                                this.nicol,
                                                ResultType.Failure));
        gameSet5.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list5,
                                                this.nicol,
                                                ResultType.Success));
        gameSet6.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list6,
                                                this.nicol,
                                                ResultType.Success));


        IPlayerStatisticsComputer nicoLComputer = PlayerStatisticsComputerFactory.GetPlayerStatisticsComputer(
                Arrays.asList(gameSet1, gameSet2, gameSet3, gameSet4, gameSet5, gameSet6),
                this.nicol,
                "guava"
        );

        Map<ResultType, Integer> nicoLResults = nicoLComputer.getWonAndLostGameSetsForPlayer();
        int expectedSuccessesForNicoL = 4;
        int expectedFailuresForNicoL = 2;
        int actualSuccessesForNicoL = nicoLResults.get(ResultType.Success);
        int actualFailuresForNicoL = nicoLResults.get(ResultType.Failure);

        assertEquals("Incorrect number of successes for NicoL",
                     expectedSuccessesForNicoL,
                     actualSuccessesForNicoL);
        assertEquals("Incorrect number of failures for NicoL",
                     expectedFailuresForNicoL,
                     actualFailuresForNicoL);

        IPlayerStatisticsComputer ludasComputer = PlayerStatisticsComputerFactory.GetPlayerStatisticsComputer(
                Arrays.asList(gameSet1, gameSet2, gameSet3, gameSet4, gameSet5, gameSet6),
                this.ludas,
                "guava"
        );

        Map<ResultType, Integer> ludasResults = ludasComputer.getWonAndLostGameSetsForPlayer();
        int expectedSuccessesForLudas = 2;
        int expectedFailuresForLudas = 4;
        int actualSuccessesForLudas = ludasResults.get(ResultType.Success);
        int actualFailuresForLudas = ludasResults.get(ResultType.Failure);

        assertEquals("Incorrect number of successes for Ludas",
                     expectedSuccessesForLudas,
                     actualSuccessesForLudas);
        assertEquals("Incorrect number of failures for Ludas",
                     expectedFailuresForLudas,
                     actualFailuresForLudas);

    }


    @Test
    public void testGetGameSetCountForPlayerByGameStyleType() {
        List<Player> list1 = Arrays.asList(this.nicol, this.ludas, this.jk);
        List<Player> list2 = Arrays.asList(this.nicol, this.ludas, this.arthur);
        List<Player> list3 = Arrays.asList(this.guillaume, this.nicol, this.cyril, this.ludas);
        List<Player> list4 = Arrays.asList(this.nicol, this.ludas, this.arthur, this.jk);
        List<Player> list5 = Arrays.asList(this.jk, this.ludas, this.nicol, this.cyril);
        List<Player> list6 = Arrays.asList(this.cyril,
                                           this.arthur,
                                           this.nicol,
                                           this.ludas,
                                           this.guillaume);
        List<Player> list7 = Arrays.asList(this.cyril, this.arthur, this.ludas, this.guillaume);

        GameSet gameSet1 = createGameSet(this.gameSetParameters, GameStyleType.Tarot3, list1);
        GameSet gameSet2 = createGameSet(this.gameSetParameters, GameStyleType.Tarot3, list2);
        GameSet gameSet3 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list3);
        GameSet gameSet4 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list4);
        GameSet gameSet5 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list5);
        GameSet gameSet6 = createGameSet(this.gameSetParameters, GameStyleType.Tarot5, list6);
        GameSet gameSet7 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list7);

        IPlayerStatisticsComputer nicoLComputer = PlayerStatisticsComputerFactory.GetPlayerStatisticsComputer(
                Arrays.asList(gameSet1, gameSet2, gameSet3, gameSet4, gameSet5, gameSet6, gameSet7),
                this.nicol,
                "guava"
        );

        Map<GameStyleType, Integer> nicoLResult = nicoLComputer.getGameSetCountForPlayerByGameStyleType();
        int actualTarot3CountForNicoL = nicoLResult.get(GameStyleType.Tarot3);
        int actualTarot4CountForNicoL = nicoLResult.get(GameStyleType.Tarot4);
        int actualTarot5CountForNicoL = nicoLResult.get(GameStyleType.Tarot5);
        int expectedTarot3CountForNicoL = 2;
        int expectedTarot4CountForNicoL = 3;
        int expectedTarot5CountForNicoL = 1;


        assertEquals("Incorrect number of tarot 3 game sets for NicoL",
                     expectedTarot3CountForNicoL,
                     actualTarot3CountForNicoL);
        assertEquals("Incorrect number of tarot 4 game sets for NicoL",
                     expectedTarot4CountForNicoL,
                     actualTarot4CountForNicoL);
        assertEquals("Incorrect number of tarot 5 game sets for NicoL",
                     expectedTarot5CountForNicoL,
                     actualTarot5CountForNicoL);
    }


    @Test
    public void testGetWonAndLostGameSetsForPlayerByGameStyleType() {

        List<Player> list1 = Arrays.asList(this.nicol, this.ludas, this.jk);
        List<Player> list2 = Arrays.asList(this.nicol, this.ludas, this.arthur);
        List<Player> list3 = Arrays.asList(this.guillaume, this.nicol, this.cyril, this.ludas);
        List<Player> list4 = Arrays.asList(this.nicol, this.ludas, this.arthur, this.jk);
        List<Player> list5 = Arrays.asList(this.jk, this.ludas, this.nicol, this.cyril);
        List<Player> list6 = Arrays.asList(this.cyril,
                                           this.arthur,
                                           this.nicol,
                                           this.ludas,
                                           this.guillaume);
        List<Player> list7 = Arrays.asList(this.cyril,
                                           this.arthur,
                                           this.ludas,
                                           this.guillaume,
                                           this.pm);
        List<Player> list8 = Arrays.asList(this.cyril,
                                           this.arthur,
                                           this.ludas,
                                           this.guillaume,
                                           this.nicol);

        GameSet gameSet1 = createGameSet(this.gameSetParameters, GameStyleType.Tarot3, list1);
        GameSet gameSet2 = createGameSet(this.gameSetParameters, GameStyleType.Tarot3, list2);
        GameSet gameSet3 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list3);
        GameSet gameSet4 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list4);
        GameSet gameSet5 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list5);
        GameSet gameSet6 = createGameSet(this.gameSetParameters, GameStyleType.Tarot5, list6);
        GameSet gameSet7 = createGameSet(this.gameSetParameters, GameStyleType.Tarot5, list7);
        GameSet gameSet8 = createGameSet(this.gameSetParameters, GameStyleType.Tarot5, list8);

        gameSet1.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list1,
                                                this.nicol,
                                                ResultType.Success));
        gameSet2.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list2,
                                                this.nicol,
                                                ResultType.Failure));
        gameSet3.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list3,
                                                this.nicol,
                                                ResultType.Success));
        gameSet4.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list4,
                                                this.nicol,
                                                ResultType.Failure));
        gameSet5.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list5,
                                                this.nicol,
                                                ResultType.Success));
        gameSet6.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list6,
                                                this.nicol,
                                                ResultType.Success));
        gameSet7.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list7,
                                                this.ludas,
                                                ResultType.Success));
        gameSet8.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list8,
                                                this.nicol,
                                                ResultType.Success));

        IPlayerStatisticsComputer nicoLComputer = PlayerStatisticsComputerFactory.GetPlayerStatisticsComputer(
                Arrays.asList(gameSet1,
                              gameSet2,
                              gameSet3,
                              gameSet4,
                              gameSet5,
                              gameSet6,
                              gameSet7,
                              gameSet8),
                this.nicol,
                "guava"
        );

        Map<GameStyleType, Map<ResultType, Integer>> nicoLResults = nicoLComputer.getWonAndLostGameSetsForPlayerByGameStyleType();
        int actualTarot3SuccessesForNicoL = nicoLResults.get(GameStyleType.Tarot3)
                                                        .get(ResultType.Success);
        int actualTarot3FailuresForNicoL = nicoLResults.get(GameStyleType.Tarot3)
                                                       .get(ResultType.Failure);
        int actualTarot4SuccessesForNicoL = nicoLResults.get(GameStyleType.Tarot4)
                                                        .get(ResultType.Success);
        int actualTarot4FailuresForNicoL = nicoLResults.get(GameStyleType.Tarot4)
                                                       .get(ResultType.Failure);
        int actualTarot5SuccessesForNicoL = nicoLResults.get(GameStyleType.Tarot5)
                                                        .get(ResultType.Success);
        int actualTarot5FailuresForNicoL = nicoLResults.get(GameStyleType.Tarot5)
                                                       .get(ResultType.Failure);
        int expectedTarot3SuccessesForNicoL = 1;
        int expectedTarot3FailuresForNicoL = 1;
        int expectedTarot4SuccessesForNicoL = 2;
        int expectedTarot4FailuresForNicoL = 1;
        int expectedTarot5SuccessesForNicoL = 2;
        int expectedTarot5FailuresForNicoL = 0;

        assertEquals("Incorrect number of won tarot 3 game sets for NicoL",
                     expectedTarot3SuccessesForNicoL,
                     actualTarot3SuccessesForNicoL);
        assertEquals("Incorrect number of lost tarot 3 game sets for NicoL",
                     expectedTarot3FailuresForNicoL,
                     actualTarot3FailuresForNicoL);
        assertEquals("Incorrect number of won tarot 4 game sets for NicoL",
                     expectedTarot4SuccessesForNicoL,
                     actualTarot4SuccessesForNicoL);
        assertEquals("Incorrect number of lost tarot 4 game sets for NicoL",
                     expectedTarot4FailuresForNicoL,
                     actualTarot4FailuresForNicoL);
        assertEquals("Incorrect number of won tarot 5 game sets for NicoL",
                     expectedTarot5SuccessesForNicoL,
                     actualTarot5SuccessesForNicoL);
        assertEquals("Incorrect number of lost tarot 5 game sets for NicoL",
                     expectedTarot5FailuresForNicoL,
                     actualTarot5FailuresForNicoL);
    }


    @Test
    public void testGetGameCountForPlayer() {
        List<Player> list1 = Arrays.asList(this.nicol, this.ludas, this.jk);
        List<Player> list2 = Arrays.asList(this.nicol, this.ludas, this.arthur);
        List<Player> list3 = Arrays.asList(this.guillaume, this.nicol, this.cyril, this.ludas);
        List<Player> list4 = Arrays.asList(this.nicol, this.ludas, this.arthur, this.jk);
        List<Player> list5 = Arrays.asList(this.jk, this.ludas, this.nicol, this.cyril);
        List<Player> list6 = Arrays.asList(this.cyril,
                                           this.arthur,
                                           this.nicol,
                                           this.ludas,
                                           this.guillaume);
        List<Player> list7 = Arrays.asList(this.cyril,
                                           this.arthur,
                                           this.ludas,
                                           this.guillaume,
                                           this.pm);
        List<Player> list8 = Arrays.asList(this.cyril,
                                           this.arthur,
                                           this.ludas,
                                           this.guillaume,
                                           this.nicol);

        GameSet gameSet1 = createGameSet(this.gameSetParameters, GameStyleType.Tarot3, list1);
        GameSet gameSet2 = createGameSet(this.gameSetParameters, GameStyleType.Tarot3, list2);
        GameSet gameSet3 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list3);
        GameSet gameSet4 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list4);
        GameSet gameSet5 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list5);
        GameSet gameSet6 = createGameSet(this.gameSetParameters, GameStyleType.Tarot5, list6);
        GameSet gameSet7 = createGameSet(this.gameSetParameters, GameStyleType.Tarot5, list7);
        GameSet gameSet8 = createGameSet(this.gameSetParameters, GameStyleType.Tarot5, list8);

        gameSet1.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list1,
                                                this.nicol,
                                                ResultType.Success));
        gameSet1.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list1,
                                                this.ludas,
                                                ResultType.Success));
        gameSet1.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list1,
                                                this.jk,
                                                ResultType.Failure));
        gameSet2.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list2,
                                                this.nicol,
                                                ResultType.Failure));
        gameSet3.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list3,
                                                this.nicol,
                                                ResultType.Success));
        gameSet4.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list4,
                                                this.nicol,
                                                ResultType.Failure));
        gameSet5.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list5,
                                                this.nicol,
                                                ResultType.Success));
        gameSet6.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list6,
                                                this.nicol,
                                                ResultType.Success));
        gameSet7.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list7,
                                                this.ludas,
                                                ResultType.Success));
        gameSet8.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list8,
                                                this.nicol,
                                                ResultType.Success));

        IPlayerStatisticsComputer nicoLComputer = PlayerStatisticsComputerFactory.GetPlayerStatisticsComputer(
                Arrays.asList(gameSet1,
                              gameSet2,
                              gameSet3,
                              gameSet4,
                              gameSet5,
                              gameSet6,
                              gameSet7,
                              gameSet8),
                this.nicol,
                "guava"
        );

        int expectedForNicoL = 9;
        int actualForNicoL = nicoLComputer.getGameCountForPlayer();
        assertEquals("Incorrect number of Games in which is NicoL",
                     expectedForNicoL,
                     actualForNicoL);
    }


    @Test
    public void testGetWonAndLostGamesForPlayer() {
        List<Player> list1 = Arrays.asList(this.nicol, this.ludas, this.jk);
        List<Player> list2 = Arrays.asList(this.nicol, this.ludas, this.arthur);
        List<Player> list3 = Arrays.asList(this.guillaume, this.nicol, this.cyril, this.ludas);
        List<Player> list4 = Arrays.asList(this.nicol, this.ludas, this.arthur, this.jk);
        List<Player> list5 = Arrays.asList(this.jk, this.ludas, this.nicol, this.cyril);
        List<Player> list6 = Arrays.asList(this.cyril,
                                           this.arthur,
                                           this.nicol,
                                           this.ludas,
                                           this.guillaume);
        List<Player> list7 = Arrays.asList(this.cyril,
                                           this.arthur,
                                           this.ludas,
                                           this.guillaume,
                                           this.pm);
        List<Player> list8 = Arrays.asList(this.cyril,
                                           this.arthur,
                                           this.ludas,
                                           this.guillaume,
                                           this.nicol);

        GameSet gameSet1 = createGameSet(this.gameSetParameters, GameStyleType.Tarot3, list1);
        GameSet gameSet2 = createGameSet(this.gameSetParameters, GameStyleType.Tarot3, list2);
        GameSet gameSet3 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list3);
        GameSet gameSet4 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list4);
        GameSet gameSet5 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list5);
        GameSet gameSet6 = createGameSet(this.gameSetParameters, GameStyleType.Tarot5, list6);
        GameSet gameSet7 = createGameSet(this.gameSetParameters, GameStyleType.Tarot5, list7);
        GameSet gameSet8 = createGameSet(this.gameSetParameters, GameStyleType.Tarot5, list8);

        gameSet1.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list1,
                                                this.nicol,
                                                ResultType.Success));
        gameSet1.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list1,
                                                this.ludas,
                                                ResultType.Success));
        gameSet1.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list1,
                                                this.jk,
                                                ResultType.Failure));
        gameSet2.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list2,
                                                this.nicol,
                                                ResultType.Failure));
        gameSet3.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list3,
                                                this.nicol,
                                                ResultType.Success));
        gameSet4.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list4,
                                                this.nicol,
                                                ResultType.Failure));
        gameSet5.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list5,
                                                this.nicol,
                                                ResultType.Success));
        gameSet6.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list6,
                                                this.nicol,
                                                ResultType.Success));
        gameSet7.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list7,
                                                this.ludas,
                                                ResultType.Success));
        gameSet8.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list8,
                                                this.nicol,
                                                ResultType.Success));

        IPlayerStatisticsComputer nicoLComputer = PlayerStatisticsComputerFactory.GetPlayerStatisticsComputer(
                Arrays.asList(gameSet1,
                              gameSet2,
                              gameSet3,
                              gameSet4,
                              gameSet5,
                              gameSet6,
                              gameSet7,
                              gameSet8),
                this.nicol,
                "guava"
        );

        Map<ResultType, Integer> nicoLResults = nicoLComputer.getWonAndLostGameSetsForPlayer();
        int expectedSuccessesForNicoL = 5;
        int expectedFailuresForNicoL = 2;
        int actualSuccessesForNicoL = nicoLResults.get(ResultType.Success);
        int actualFailuresForNicoL = nicoLResults.get(ResultType.Failure);

        assertEquals("Incorrect number of successes for NicoL",
                     expectedSuccessesForNicoL,
                     actualSuccessesForNicoL);
        assertEquals("Incorrect number of failures for NicoL",
                     expectedFailuresForNicoL,
                     actualFailuresForNicoL);
    }


    @Test
    public void testGetGameCountForPlayerByBetType() {
        List<Player> list1 = Arrays.asList(this.nicol, this.ludas, this.jk);
        List<Player> list2 = Arrays.asList(this.nicol, this.ludas, this.arthur);
        List<Player> list3 = Arrays.asList(this.guillaume, this.nicol, this.cyril, this.ludas);
        List<Player> list4 = Arrays.asList(this.nicol, this.ludas, this.arthur, this.jk);
        List<Player> list5 = Arrays.asList(this.jk, this.ludas, this.nicol, this.cyril);
        List<Player> list6 = Arrays.asList(this.cyril,
                                           this.arthur,
                                           this.nicol,
                                           this.ludas,
                                           this.guillaume);
        List<Player> list7 = Arrays.asList(this.jk,
                                           this.arthur,
                                           this.ludas,
                                           this.guillaume,
                                           this.cyril);

        GameSet gameSet1 = createGameSet(this.gameSetParameters, GameStyleType.Tarot3, list1);
        GameSet gameSet2 = createGameSet(this.gameSetParameters, GameStyleType.Tarot3, list2);
        GameSet gameSet3 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list3);
        GameSet gameSet4 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list4);
        GameSet gameSet5 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list5);
        GameSet gameSet6 = createGameSet(this.gameSetParameters, GameStyleType.Tarot5, list6);
        GameSet gameSet7 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list7);

        gameSet1.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list1,
                                                this.nicol,
                                                Bet.GARDE,
                                                ResultType.Success));
        gameSet1.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list1,
                                                this.ludas,
                                                Bet.PRISE,
                                                ResultType.Success));
        gameSet1.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list1,
                                                this.jk,
                                                Bet.GARDE,
                                                ResultType.Failure));
        gameSet2.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list2,
                                                this.nicol,
                                                Bet.GARDESANS,
                                                ResultType.Failure));
        gameSet2.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list2,
                                                this.nicol,
                                                Bet.GARDE,
                                                ResultType.Failure));
        gameSet3.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list3,
                                                this.nicol,
                                                Bet.GARDECONTRE,
                                                ResultType.Success));
        gameSet3.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list3,
                                                this.nicol,
                                                Bet.PRISE,
                                                ResultType.Success));
        gameSet3.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list3,
                                                this.nicol,
                                                Bet.GARDECONTRE,
                                                ResultType.Success));
        gameSet3.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list3,
                                                this.nicol,
                                                Bet.GARDE,
                                                ResultType.Success));
        gameSet4.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list4,
                                                this.nicol,
                                                Bet.GARDE,
                                                ResultType.Failure));
        gameSet4.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list4,
                                                this.ludas,
                                                Bet.PRISE,
                                                ResultType.Failure));
        gameSet5.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list5,
                                                this.nicol,
                                                Bet.GARDESANS,
                                                ResultType.Success));
        gameSet5.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list5,
                                                this.nicol,
                                                Bet.GARDESANS,
                                                ResultType.Success));
        gameSet5.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list5,
                                                this.nicol,
                                                Bet.GARDE,
                                                ResultType.Success));
        gameSet5.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list5,
                                                this.nicol,
                                                Bet.PRISE,
                                                ResultType.Success));
        gameSet5.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list5,
                                                this.nicol,
                                                Bet.GARDECONTRE,
                                                ResultType.Success));
        gameSet6.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list6,
                                                this.nicol,
                                                Bet.GARDE,
                                                ResultType.Success));
        gameSet6.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list6,
                                                this.ludas,
                                                Bet.GARDE,
                                                ResultType.Success));
        gameSet6.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list6,
                                                this.nicol,
                                                Bet.PRISE,
                                                ResultType.Success));

        gameSet7.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list7,
                                                this.ludas,
                                                Bet.PRISE,
                                                ResultType.Success));
        gameSet7.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list7,
                                                this.cyril,
                                                Bet.GARDECONTRE,
                                                ResultType.Success));
        gameSet7.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list7,
                                                this.ludas,
                                                Bet.GARDESANS,
                                                ResultType.Success));
        gameSet7.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list7,
                                                this.ludas,
                                                Bet.GARDE,
                                                ResultType.Success));

        IPlayerStatisticsComputer nicoLComputer = PlayerStatisticsComputerFactory.GetPlayerStatisticsComputer(
                Arrays.asList(gameSet1, gameSet2, gameSet3, gameSet4, gameSet5, gameSet6, gameSet7),
                this.nicol,
                "guava"
        );

        Map<BetType, Integer> nicoLResult = nicoLComputer.getGameCountForPlayerByBetType();
        int actualPriseCountForNicoL = nicoLResult.get(BetType.Prise);
        int actualGardeCountForNicoL = nicoLResult.get(BetType.Garde);
        int actualGardeSansCountForNicoL = nicoLResult.get(BetType.GardeSans);
        int actualGardeContreCountForNicoL = nicoLResult.get(BetType.GardeContre);

        int expectedPriseCountForNicoL = 5;
        int expectedGardeCountForNicoL = 8;
        int expectedGardeSansCountForNicoL = 3;
        int expectedGardeContreCountForNicoL = 3;

        assertEquals("Incorrect number of prise games for NicoL",
                     expectedPriseCountForNicoL,
                     actualPriseCountForNicoL);
        assertEquals("Incorrect number of garde games for NicoL",
                     expectedGardeCountForNicoL,
                     actualGardeCountForNicoL);
        assertEquals("Incorrect number of garde sans games for NicoL",
                     expectedGardeSansCountForNicoL,
                     actualGardeSansCountForNicoL);
        assertEquals("Incorrect number of garde contre games for NicoL",
                     expectedGardeContreCountForNicoL,
                     actualGardeContreCountForNicoL);
    }


    @Test
    public void testGetWonAndLostGamesForPlayerByBetType() {
        List<Player> list1 = Arrays.asList(this.nicol, this.ludas, this.jk);
        List<Player> list2 = Arrays.asList(this.nicol, this.ludas, this.arthur);
        List<Player> list3 = Arrays.asList(this.guillaume, this.nicol, this.cyril, this.ludas);
        List<Player> list4 = Arrays.asList(this.nicol, this.ludas, this.arthur, this.jk);
        List<Player> list5 = Arrays.asList(this.jk, this.ludas, this.nicol, this.cyril);
        List<Player> list6 = Arrays.asList(this.cyril,
                                           this.arthur,
                                           this.nicol,
                                           this.ludas,
                                           this.guillaume);
        List<Player> list7 = Arrays.asList(this.jk,
                                           this.arthur,
                                           this.ludas,
                                           this.guillaume,
                                           this.cyril);

        GameSet gameSet1 = createGameSet(this.gameSetParameters, GameStyleType.Tarot3, list1);
        GameSet gameSet2 = createGameSet(this.gameSetParameters, GameStyleType.Tarot3, list2);
        GameSet gameSet3 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list3);
        GameSet gameSet4 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list4);
        GameSet gameSet5 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list5);
        GameSet gameSet6 = createGameSet(this.gameSetParameters, GameStyleType.Tarot5, list6);
        GameSet gameSet7 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list7);

        gameSet1.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list1,
                                                this.nicol,
                                                Bet.GARDE,
                                                ResultType.Success));
        gameSet1.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list1,
                                                this.ludas,
                                                Bet.PRISE,
                                                ResultType.Success));
        gameSet1.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list1,
                                                this.jk,
                                                Bet.GARDE,
                                                ResultType.Failure));
        gameSet2.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list2,
                                                this.nicol,
                                                Bet.GARDESANS,
                                                ResultType.Failure));
        gameSet2.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list2,
                                                this.nicol,
                                                Bet.GARDE,
                                                ResultType.Failure));
        gameSet3.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list3,
                                                this.nicol,
                                                Bet.GARDECONTRE,
                                                ResultType.Success));
        gameSet3.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list3,
                                                this.nicol,
                                                Bet.PRISE,
                                                ResultType.Success));
        gameSet3.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list3,
                                                this.nicol,
                                                Bet.GARDECONTRE,
                                                ResultType.Success));
        gameSet3.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list3,
                                                this.nicol,
                                                Bet.GARDE,
                                                ResultType.Success));
        gameSet4.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list4,
                                                this.nicol,
                                                Bet.GARDE,
                                                ResultType.Failure));
        gameSet4.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list4,
                                                this.ludas,
                                                Bet.PRISE,
                                                ResultType.Failure));
        gameSet5.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list5,
                                                this.nicol,
                                                Bet.GARDESANS,
                                                ResultType.Success));
        gameSet5.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list5,
                                                this.nicol,
                                                Bet.GARDESANS,
                                                ResultType.Success));
        gameSet5.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list5,
                                                this.nicol,
                                                Bet.GARDE,
                                                ResultType.Success));
        gameSet5.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list5,
                                                this.nicol,
                                                Bet.PRISE,
                                                ResultType.Success));
        gameSet5.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list5,
                                                this.nicol,
                                                Bet.GARDECONTRE,
                                                ResultType.Success));
        gameSet6.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list6,
                                                this.nicol,
                                                Bet.GARDE,
                                                ResultType.Success));
        gameSet6.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list6,
                                                this.ludas,
                                                Bet.GARDE,
                                                ResultType.Success));
        gameSet6.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list6,
                                                this.nicol,
                                                Bet.PRISE,
                                                ResultType.Success));

        gameSet7.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list7,
                                                this.ludas,
                                                Bet.PRISE,
                                                ResultType.Success));
        gameSet7.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list7,
                                                this.cyril,
                                                Bet.GARDECONTRE,
                                                ResultType.Success));
        gameSet7.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list7,
                                                this.ludas,
                                                Bet.GARDESANS,
                                                ResultType.Success));
        gameSet7.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list7,
                                                this.ludas,
                                                Bet.GARDE,
                                                ResultType.Success));

        IPlayerStatisticsComputer nicoLComputer = PlayerStatisticsComputerFactory.GetPlayerStatisticsComputer(
                Arrays.asList(gameSet1, gameSet2, gameSet3, gameSet4, gameSet5, gameSet6, gameSet7),
                this.nicol,
                "guava"
        );

        Map<BetType, Map<ResultType, Integer>> nicoLResults = nicoLComputer.getWonAndLostGamesForPlayerByBetType();
        int actualPriseSuccessesForNicoL = nicoLResults.get(BetType.Prise).get(ResultType.Success);
        int actualPriseFailuresForNicoL = nicoLResults.get(BetType.Prise).get(ResultType.Failure);
        int actualGardeSuccessesForNicoL = nicoLResults.get(BetType.Garde).get(ResultType.Success);
        int actualGardeFailuresForNicoL = nicoLResults.get(BetType.Garde).get(ResultType.Failure);
        int actualGardeSansSuccessesForNicoL = nicoLResults.get(BetType.GardeSans)
                                                           .get(ResultType.Success);
        int actualGardeSansFailuresForNicoL = nicoLResults.get(BetType.GardeSans)
                                                          .get(ResultType.Failure);
        int actualGardeContreSuccessesForNicoL = nicoLResults.get(BetType.GardeContre)
                                                             .get(ResultType.Success);
        int actualGardeContreFailuresForNicoL = nicoLResults.get(BetType.GardeContre)
                                                            .get(ResultType.Failure);

        int expectedPriseSuccessesForNicoL = 4;
        int expectedPriseFailuresForNicoL = 1;
        int expectedGardeSuccessesForNicoL = 5;
        int expectedGardeFailuresForNicoL = 3;
        int expectedGardeSansSuccessesForNicoL = 2;
        int expectedGardeSansFailuresForNicoL = 1;
        int expectedGardeContreSuccessesForNicoL = 3;
        int expectedGardeContreFailuresForNicoL = 0;

        assertEquals("Incorrect number of won prise games for NicoL",
                     expectedPriseSuccessesForNicoL,
                     actualPriseSuccessesForNicoL);
        assertEquals("Incorrect number of lost prise games for NicoL",
                     expectedPriseFailuresForNicoL,
                     actualPriseFailuresForNicoL);
        assertEquals("Incorrect number of won garde games for NicoL",
                     expectedGardeSuccessesForNicoL,
                     actualGardeSuccessesForNicoL);
        assertEquals("Incorrect number of lost garde games for NicoL",
                     expectedGardeFailuresForNicoL,
                     actualGardeFailuresForNicoL);
        assertEquals("Incorrect number of won garde sans games for NicoL",
                     expectedGardeSansSuccessesForNicoL,
                     actualGardeSansSuccessesForNicoL);
        assertEquals("Incorrect number of lost garde sans games for NicoL",
                     expectedGardeSansFailuresForNicoL,
                     actualGardeSansFailuresForNicoL);
        assertEquals("Incorrect number of won garde contre games for NicoL",
                     expectedGardeContreSuccessesForNicoL,
                     actualGardeContreSuccessesForNicoL);
        assertEquals("Incorrect number of lost garde contre games for NicoL",
                     expectedGardeContreFailuresForNicoL,
                     actualGardeContreFailuresForNicoL);
    }


    @Test
    public void TestGetGameCountAsLeadingPlayer() {
        List<Player> list1 = Arrays.asList(this.nicol, this.ludas, this.jk);
        List<Player> list2 = Arrays.asList(this.nicol, this.ludas, this.arthur);
        List<Player> list3 = Arrays.asList(this.guillaume, this.nicol, this.cyril, this.ludas);
        List<Player> list4 = Arrays.asList(this.nicol, this.ludas, this.arthur, this.jk);
        List<Player> list5 = Arrays.asList(this.jk, this.ludas, this.nicol, this.cyril);
        List<Player> list6 = Arrays.asList(this.cyril,
                                           this.arthur,
                                           this.nicol,
                                           this.ludas,
                                           this.guillaume);
        List<Player> list7 = Arrays.asList(this.jk,
                                           this.arthur,
                                           this.ludas,
                                           this.guillaume,
                                           this.cyril);

        GameSet gameSet1 = createGameSet(this.gameSetParameters, GameStyleType.Tarot3, list1);
        GameSet gameSet2 = createGameSet(this.gameSetParameters, GameStyleType.Tarot3, list2);
        GameSet gameSet3 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list3);
        GameSet gameSet4 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list4);
        GameSet gameSet5 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list5);
        GameSet gameSet6 = createGameSet(this.gameSetParameters, GameStyleType.Tarot5, list6);
        GameSet gameSet7 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list7);

        gameSet1.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list1,
                                                this.nicol,
                                                Bet.GARDE,
                                                ResultType.Success));
        gameSet1.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list1,
                                                this.ludas,
                                                Bet.PRISE,
                                                ResultType.Success));
        gameSet1.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list1,
                                                this.jk,
                                                Bet.GARDE,
                                                ResultType.Failure));
        gameSet2.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list2,
                                                this.nicol,
                                                Bet.GARDESANS,
                                                ResultType.Failure));
        gameSet2.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list2,
                                                this.nicol,
                                                Bet.GARDE,
                                                ResultType.Failure));
        gameSet3.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list3,
                                                this.nicol,
                                                Bet.GARDECONTRE,
                                                ResultType.Success));
        gameSet3.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list3,
                                                this.nicol,
                                                Bet.PRISE,
                                                ResultType.Success));
        gameSet3.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list3,
                                                this.nicol,
                                                Bet.GARDECONTRE,
                                                ResultType.Success));
        gameSet3.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list3,
                                                this.nicol,
                                                Bet.GARDE,
                                                ResultType.Success));
        gameSet4.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list4,
                                                this.nicol,
                                                Bet.GARDE,
                                                ResultType.Failure));
        gameSet4.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list4,
                                                this.ludas,
                                                Bet.PRISE,
                                                ResultType.Failure));
        gameSet5.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list5,
                                                this.nicol,
                                                Bet.GARDESANS,
                                                ResultType.Success));
        gameSet5.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list5,
                                                this.nicol,
                                                Bet.GARDESANS,
                                                ResultType.Success));
        gameSet5.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list5,
                                                this.nicol,
                                                Bet.GARDE,
                                                ResultType.Success));
        gameSet5.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list5,
                                                this.nicol,
                                                Bet.PRISE,
                                                ResultType.Success));
        gameSet5.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list5,
                                                this.nicol,
                                                Bet.GARDECONTRE,
                                                ResultType.Success));
        gameSet6.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list6,
                                                this.nicol,
                                                Bet.GARDE,
                                                ResultType.Success));
        gameSet6.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list6,
                                                this.ludas,
                                                Bet.GARDE,
                                                ResultType.Success));
        gameSet6.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list6,
                                                this.nicol,
                                                Bet.PRISE,
                                                ResultType.Success));

        gameSet7.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list7,
                                                this.ludas,
                                                Bet.PRISE,
                                                ResultType.Success));
        gameSet7.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list7,
                                                this.cyril,
                                                Bet.GARDECONTRE,
                                                ResultType.Success));
        gameSet7.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list7,
                                                this.ludas,
                                                Bet.GARDESANS,
                                                ResultType.Success));
        gameSet7.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list7,
                                                this.ludas,
                                                Bet.GARDE,
                                                ResultType.Success));

        IPlayerStatisticsComputer nicoLComputer = PlayerStatisticsComputerFactory.GetPlayerStatisticsComputer(
                Arrays.asList(gameSet1, gameSet2, gameSet3, gameSet4, gameSet5, gameSet6, gameSet7),
                this.nicol,
                "guava"
        );

        int actualGameCountWithNicoLAsLeadingPlayer = nicoLComputer.getGameCountAsLeadingPlayer();
        int expectedGameCountWithNicoLAsLeadingPlayer = 15;
        assertEquals("Incorrect game count where NicoL is leading player",
                     expectedGameCountWithNicoLAsLeadingPlayer,
                     actualGameCountWithNicoLAsLeadingPlayer);
    }


    @Test
    public void TestGetGameCountAsCalledPlayer() {
        List<Player> list1 = Arrays.asList(this.nicol, this.ludas, this.jk);
        List<Player> list2 = Arrays.asList(this.nicol, this.ludas, this.arthur);
        List<Player> list3 = Arrays.asList(this.guillaume, this.nicol, this.cyril, this.ludas);
        List<Player> list4 = Arrays.asList(this.pm, this.ludas, this.arthur, this.jk);
        List<Player> list5 = Arrays.asList(this.jk, this.ludas, this.nicol, this.cyril);
        List<Player> list6 = Arrays.asList(this.cyril,
                                           this.arthur,
                                           this.nicol,
                                           this.ludas,
                                           this.guillaume);
        List<Player> list7 = Arrays.asList(this.jk,
                                           this.nicol,
                                           this.ludas,
                                           this.guillaume,
                                           this.cyril);

        GameSet gameSet1 = createGameSet(this.gameSetParameters, GameStyleType.Tarot3, list1);
        GameSet gameSet2 = createGameSet(this.gameSetParameters, GameStyleType.Tarot3, list2);
        GameSet gameSet3 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list3);
        GameSet gameSet4 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list4);
        GameSet gameSet5 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list5);
        GameSet gameSet6 = createGameSet(this.gameSetParameters, GameStyleType.Tarot5, list6);
        GameSet gameSet7 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list7);

        gameSet1.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list1,
                                                this.nicol,
                                                Bet.GARDE,
                                                ResultType.Success));
        gameSet1.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list1,
                                                this.ludas,
                                                Bet.PRISE,
                                                ResultType.Success));
        gameSet1.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list1,
                                                this.jk,
                                                Bet.GARDE,
                                                ResultType.Failure));
        gameSet2.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list2,
                                                this.nicol,
                                                Bet.GARDESANS,
                                                ResultType.Failure));
        gameSet2.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list2,
                                                this.nicol,
                                                Bet.GARDE,
                                                ResultType.Failure));
        gameSet3.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list3,
                                                this.nicol,
                                                Bet.GARDECONTRE,
                                                ResultType.Success));
        gameSet3.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list3,
                                                this.nicol,
                                                Bet.PRISE,
                                                ResultType.Success));
        gameSet3.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list3,
                                                this.nicol,
                                                Bet.GARDECONTRE,
                                                ResultType.Success));
        gameSet3.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list3,
                                                this.nicol,
                                                Bet.GARDE,
                                                ResultType.Success));
        gameSet4.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list4,
                                                this.pm,
                                                Bet.GARDE,
                                                ResultType.Failure));
        gameSet4.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list4,
                                                this.ludas,
                                                Bet.PRISE,
                                                ResultType.Failure));
        gameSet5.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list5,
                                                this.nicol,
                                                Bet.GARDESANS,
                                                ResultType.Success));
        gameSet5.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list5,
                                                this.nicol,
                                                Bet.GARDESANS,
                                                ResultType.Success));
        gameSet5.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list5,
                                                this.nicol,
                                                Bet.GARDE,
                                                ResultType.Success));
        gameSet5.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list5,
                                                this.nicol,
                                                Bet.PRISE,
                                                ResultType.Success));
        gameSet5.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list5,
                                                this.nicol,
                                                Bet.GARDECONTRE,
                                                ResultType.Success));

        gameSet6.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list6,
                                                this.nicol,
                                                this.ludas,
                                                ResultType.Success));
        gameSet6.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list6,
                                                this.ludas,
                                                this.cyril,
                                                ResultType.Success));
        gameSet6.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list6,
                                                this.nicol,
                                                this.nicol,
                                                ResultType.Success)); // <-

        gameSet7.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list7,
                                                this.ludas,
                                                this.jk,
                                                ResultType.Success));
        gameSet7.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list7,
                                                this.cyril,
                                                this.nicol,
                                                ResultType.Success)); // <-
        gameSet7.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list7,
                                                this.guillaume,
                                                this.nicol,
                                                ResultType.Success)); // <-
        gameSet7.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                                list7,
                                                this.ludas,
                                                this.jk,
                                                ResultType.Success));

        IPlayerStatisticsComputer nicoLComputer = PlayerStatisticsComputerFactory.GetPlayerStatisticsComputer(
                Arrays.asList(gameSet1, gameSet2, gameSet3, gameSet4, gameSet5, gameSet6, gameSet7),
                this.nicol,
                "guava"
        );

        int actualGameCountWithNicoLAsCalledPlayer = nicoLComputer.getGameCountAsCalledPlayer();
        int expectedGameCountWithNicoLAsCalledPlayer = 3;
        assertEquals("Incorrect game count where NicoL is Called player",
                     expectedGameCountWithNicoLAsCalledPlayer,
                     actualGameCountWithNicoLAsCalledPlayer);
    }


    @Test
    public void testMergeSubObjectListsWithTransform() {
        List<Player> list1 = Arrays.asList(this.nicol, this.ludas, this.jk);
        List<Player> list2 = Arrays.asList(this.nicol, this.ludas, this.arthur);
        List<Player> list3 = Arrays.asList(this.guillaume, this.nicol, this.cyril, this.ludas);

        GameSet gameSet1 = createGameSet(this.gameSetParameters, GameStyleType.Tarot3, list1);
        GameSet gameSet2 = createGameSet(this.gameSetParameters, GameStyleType.Tarot3, list2);
        GameSet gameSet3 = createGameSet(this.gameSetParameters, GameStyleType.Tarot4, list3);

        gameSet1.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list1,
                                                this.nicol,
                                                ResultType.Success));
        gameSet1.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list1,
                                                this.jk,
                                                ResultType.Failure));
        gameSet1.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list1,
                                                this.ludas,
                                                ResultType.Success));

        gameSet2.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list2,
                                                this.ludas,
                                                ResultType.Failure));
        gameSet2.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list2,
                                                this.ludas,
                                                ResultType.Success));
        gameSet2.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list2,
                                                this.arthur,
                                                ResultType.Success));
        gameSet2.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                                list2,
                                                this.nicol,
                                                ResultType.Failure));

        gameSet3.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list3,
                                                this.cyril,
                                                ResultType.Success));
        gameSet3.addGame(create4PlayerStyleGame(this.gameSetParameters,
                                                list3,
                                                this.guillaume,
                                                ResultType.Failure));


        List<GameSet> input = Arrays.asList(gameSet1, gameSet2, gameSet3);

        Function<GameSet, List<BaseGame>> function = new Function<GameSet, List<BaseGame>>() {

            @Override
            public List<BaseGame> apply(final GameSet input) {
                return input.getGames();
            }
        };
        Iterable<BaseGame> transform = Iterables.concat(Iterables.transform(input, function));
        List<BaseGame> newArrayList = Lists.newArrayList(transform);

        assertEquals("Incorrect size of new list", newArrayList.size(), 9);
    }
}
