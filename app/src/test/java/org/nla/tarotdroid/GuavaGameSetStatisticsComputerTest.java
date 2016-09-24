package org.nla.tarotdroid;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.nla.tarotdroid.biz.Bet;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.GameSetParameters;
import org.nla.tarotdroid.biz.King;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.PlayerList;
import org.nla.tarotdroid.biz.StandardTarot3Game;
import org.nla.tarotdroid.biz.StandardTarot5Game;
import org.nla.tarotdroid.biz.computers.GameSetStatisticsComputerFactory;
import org.nla.tarotdroid.biz.computers.IGameSetStatisticsComputer;
import org.nla.tarotdroid.biz.enums.BetType;
import org.nla.tarotdroid.biz.enums.GameStyleType;
import org.nla.tarotdroid.biz.enums.KingType;
import org.nla.tarotdroid.biz.enums.ResultType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;


public class GuavaGameSetStatisticsComputerTest {


    private GameSetParameters gameSetParameters;


    private Player nicol;


    private Player jk;


    private Player ludas;


    private Player guillaume;


    private Player arthur;


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

    private StandardTarot5Game create5PlayerStyleGame(
            final GameSetParameters gameSetParameters,
            final List<Player> players,
            final Player leader,
            final King king
    ) {
        StandardTarot5Game game = new StandardTarot5Game();
        game.setPlayers(new PlayerList(players));
        game.setLeadingPlayer(leader);
        game.setCalledPlayer(leader);
        game.setCalledKing(king);
        game.setGameSetParameters(gameSetParameters);
        game.setBet(Bet.GARDE);
        game.setNumberOfOudlers(2);
        game.setPoints(50);
        return game;
    }

    @Before
    public void setUp() throws Exception {

        this.gameSetParameters = new GameSetParameters();


        this.nicol = new Player("NicoL");
        this.jk = new Player("JK");
        this.arthur = new Player("Arthur");
        this.ludas = new Player("Ludas");
        this.guillaume = new Player("Guillaume");
        this.cyril = new Player("Cyril");
    }


    @After
    public void tearDown() throws Exception {
        this.gameSetParameters = null;
        this.nicol = null;
        this.jk = null;
        this.arthur = null;
        this.ludas = null;
        this.guillaume = null;
        this.cyril = null;
    }


    @Test
    public void testGetResultsCount() {

        List<Player> players = Arrays.asList(this.nicol, this.ludas, this.jk);
        GameSet gameSet = createGameSet(this.gameSetParameters, GameStyleType.Tarot3, players);
        gameSet.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.nicol,
                                               ResultType.Success));
        gameSet.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.nicol,
                                               ResultType.Failure));
        gameSet.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.jk,
                                               ResultType.Failure));
        gameSet.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.ludas,
                                               ResultType.Success));
        gameSet.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.jk,
                                               ResultType.Failure));
        gameSet.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.ludas,
                                               ResultType.Success));
        gameSet.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.jk,
                                               ResultType.Success));
        gameSet.addGame(create3PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.nicol,
                                               ResultType.Success));

        IGameSetStatisticsComputer gameSetStatisticsComputer = GameSetStatisticsComputerFactory.GetGameSetStatisticsComputer(
                gameSet,
                "guava");
        Map<ResultType, Integer> resultsCount = gameSetStatisticsComputer.getResultsCount();
        assertEquals("Incorrect number of successes",
                     5,
                     resultsCount.get(ResultType.Success).intValue());
        assertEquals("Incorrect number of failures",
                     3,
                     resultsCount.get(ResultType.Failure).intValue());
    }


    @Test
    public void testGetLeadingCount() {

        List<Player> players = Arrays.asList(this.cyril,
                                             this.arthur,
                                             this.nicol,
                                             this.ludas,
                                             this.guillaume);
        GameSet gameSet = createGameSet(this.gameSetParameters, GameStyleType.Tarot3, players);
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.nicol,
                                               this.ludas,
                                               ResultType.Success));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.ludas,
                                               this.cyril,
                                               ResultType.Success));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.nicol,
                                               this.nicol,
                                               ResultType.Success));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.ludas,
                                               this.cyril,
                                               ResultType.Success));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.cyril,
                                               this.nicol,
                                               ResultType.Success));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.guillaume,
                                               this.nicol,
                                               ResultType.Success));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.ludas,
                                               this.guillaume,
                                               ResultType.Success));

        IGameSetStatisticsComputer gameSetStatisticsComputer = GameSetStatisticsComputerFactory.GetGameSetStatisticsComputer(
                gameSet,
                "guava");
        Map<Player, Integer> leadingCount = gameSetStatisticsComputer.getLeadingCount();

        assertEquals("Incorrect game count where NicoL is the leading player",
                     2,
                     leadingCount.get(this.nicol).intValue());
        assertEquals("Incorrect game count where NicoL is the leading player",
                     3,
                     leadingCount.get(this.ludas).intValue());
        assertEquals("Incorrect game count where NicoL is the leading player",
                     1,
                     leadingCount.get(this.cyril).intValue());
        assertEquals("Incorrect game count where NicoL is the leading player",
                     1,
                     leadingCount.get(this.guillaume).intValue());
        assertNull("Incorrect game count where NicoL is the leading player",
                   leadingCount.get(this.arthur));
    }


    @Test
    public void testGetLeadingSuccessCount() {

        List<Player> players = Arrays.asList(this.cyril,
                                             this.arthur,
                                             this.nicol,
                                             this.ludas,
                                             this.guillaume);
        GameSet gameSet = createGameSet(this.gameSetParameters, GameStyleType.Tarot3, players);
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.nicol,
                                               this.ludas,
                                               ResultType.Success));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.ludas,
                                               this.cyril,
                                               ResultType.Success));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.nicol,
                                               this.nicol,
                                               ResultType.Failure));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.ludas,
                                               this.cyril,
                                               ResultType.Failure));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.cyril,
                                               this.nicol,
                                               ResultType.Success));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.guillaume,
                                               this.nicol,
                                               ResultType.Success));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.arthur,
                                               this.guillaume,
                                               ResultType.Failure));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.arthur,
                                               this.guillaume,
                                               ResultType.Failure));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.arthur,
                                               this.guillaume,
                                               ResultType.Success));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.guillaume,
                                               this.guillaume,
                                               ResultType.Failure));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.ludas,
                                               this.guillaume,
                                               ResultType.Success));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.nicol,
                                               this.guillaume,
                                               ResultType.Failure));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.nicol,
                                               this.guillaume,
                                               ResultType.Failure));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.cyril,
                                               this.guillaume,
                                               ResultType.Success));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.cyril,
                                               this.guillaume,
                                               ResultType.Success));

        IGameSetStatisticsComputer gameSetStatisticsComputer = GameSetStatisticsComputerFactory.GetGameSetStatisticsComputer(
                gameSet,
                "guava");
        Map<Player, Integer> leadingSuccessCount = gameSetStatisticsComputer.getLeadingSuccessCount();

        assertEquals("Incorrect succesful game count where NicoL is the leading player",
                     1,
                     leadingSuccessCount.get(this.nicol).intValue());
        assertEquals("Incorrect succesful game count where Ludas is the leading player",
                     2,
                     leadingSuccessCount.get(this.ludas).intValue());
        assertEquals("Incorrect succesful game count where Cyril is the leading player",
                     3,
                     leadingSuccessCount.get(this.cyril).intValue());
        assertEquals("Incorrect succesful game count where Arthur is the leading player",
                     1,
                     leadingSuccessCount.get(this.arthur).intValue());
        assertEquals("Incorrect succesful game count where Guillaume is the leading player",
                     1,
                     leadingSuccessCount.get(this.guillaume).intValue());
    }


    @Test
    public void testGetCalledCount() {
        List<Player> players = Arrays.asList(this.cyril,
                                             this.arthur,
                                             this.nicol,
                                             this.ludas,
                                             this.guillaume);
        GameSet gameSet = createGameSet(this.gameSetParameters, GameStyleType.Tarot3, players);
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.nicol,
                                               this.ludas,
                                               ResultType.Success));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.ludas,
                                               this.cyril,
                                               ResultType.Success));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.nicol,
                                               this.nicol,
                                               ResultType.Success));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.ludas,
                                               this.cyril,
                                               ResultType.Success));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.cyril,
                                               this.nicol,
                                               ResultType.Success));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.guillaume,
                                               this.nicol,
                                               ResultType.Success));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.ludas,
                                               this.guillaume,
                                               ResultType.Success));

        IGameSetStatisticsComputer gameSetStatisticsComputer = GameSetStatisticsComputerFactory.GetGameSetStatisticsComputer(
                gameSet,
                "guava");
        Map<Player, Integer> leadingCount = gameSetStatisticsComputer.getCalledCount();

        assertEquals("Incorrect game count where NicoL is the called player",
                     3,
                     leadingCount.get(this.nicol).intValue());
        assertEquals("Incorrect game count where NicoL is the called player",
                     1,
                     leadingCount.get(this.ludas).intValue());
        assertEquals("Incorrect game count where NicoL is the called player",
                     2,
                     leadingCount.get(this.cyril).intValue());
        assertEquals("Incorrect game count where NicoL is the called player",
                     1,
                     leadingCount.get(this.guillaume).intValue());
        assertNull("Incorrect game count where NicoL is the called player",
                   leadingCount.get(this.arthur));
    }


    @Test
    public void testGetBetCount() {
        List<Player> players = Arrays.asList(this.cyril,
                                             this.arthur,
                                             this.nicol,
                                             this.ludas,
                                             this.guillaume);
        GameSet gameSet = createGameSet(this.gameSetParameters, GameStyleType.Tarot3, players);
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.nicol,
                                               Bet.GARDE,
                                               ResultType.Success));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.ludas,
                                               Bet.GARDESANS,
                                               ResultType.Success));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.nicol,
                                               Bet.GARDESANS,
                                               ResultType.Success));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.ludas,
                                               Bet.GARDE,
                                               ResultType.Success));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.cyril,
                                               Bet.GARDECONTRE,
                                               ResultType.Success));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.guillaume,
                                               Bet.GARDE,
                                               ResultType.Success));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.ludas,
                                               Bet.GARDE,
                                               ResultType.Success));

        IGameSetStatisticsComputer gameSetStatisticsComputer = GameSetStatisticsComputerFactory.GetGameSetStatisticsComputer(
                gameSet,
                "guava");
        Map<BetType, Integer> betCount = gameSetStatisticsComputer.getBetCount();

        assertNull("Incorrect game count where the bet is a Prise", betCount.get(BetType.Prise));
        assertEquals("Incorrect game count where the bet is a Garde",
                     4,
                     betCount.get(BetType.Garde).intValue());
        assertEquals("Incorrect game count where the bet is a Garde Sans",
                     2,
                     betCount.get(BetType.GardeSans).intValue());
        assertEquals("Incorrect game count where the bet is a Garde Contre",
                     1,
                     betCount.get(BetType.GardeContre).intValue());
        assertNull("Incorrect game count where the bet is a Petite", betCount.get(BetType.Petite));
    }


    @Test
    public void testGetKingCount() {
        List<Player> players = Arrays.asList(this.cyril,
                                             this.arthur,
                                             this.nicol,
                                             this.ludas,
                                             this.guillaume);
        GameSet gameSet = createGameSet(this.gameSetParameters, GameStyleType.Tarot3, players);
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.nicol,
                                               King.CLUB));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.ludas,
                                               King.SPADE));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.nicol,
                                               King.HEART));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.ludas,
                                               King.DIAMOND));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.cyril,
                                               King.DIAMOND));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.guillaume,
                                               King.SPADE));
        gameSet.addGame(create5PlayerStyleGame(this.gameSetParameters,
                                               players,
                                               this.ludas,
                                               King.HEART));

        IGameSetStatisticsComputer gameSetStatisticsComputer = GameSetStatisticsComputerFactory.GetGameSetStatisticsComputer(
                gameSet,
                "guava");
        Map<KingType, Integer> leadingCount = gameSetStatisticsComputer.getKingCount();

        assertEquals("Incorrect game count where the king is Clubs",
                     1,
                     leadingCount.get(KingType.Clubs).intValue());
        assertEquals("Incorrect game count where the king is Spades",
                     2,
                     leadingCount.get(KingType.Spades).intValue());
        assertEquals("Incorrect game count where the king is Hearts",
                     2,
                     leadingCount.get(KingType.Hearts).intValue());
        assertEquals("Incorrect game count where the king is Diamonds",
                     2,
                     leadingCount.get(KingType.Diamonds).intValue());
    }


    @Test
    @Ignore
    public void testGetScores() {
        fail("Not yet implemented");
    }


    @Test
    @Ignore
    public void testGetMaxScoreEver() {
        fail("Not yet implemented");
    }


    @Test
    @Ignore
    public void testGetMinScoreEver() {
        fail("Not yet implemented");
    }


    @Test
    @Ignore
    public void testGetMinScoreEverForPlayer() {
        fail("Not yet implemented");
    }


    @Test
    @Ignore
    public void testGetMaxScoreEverForPlayer() {
        fail("Not yet implemented");
    }


    @Test
    @Ignore
    public void testGetMaxAbsoluteScore() {
        fail("Not yet implemented");
    }


    @Test
    @Ignore
    public void testGetPlayerCount() {
        fail("Not yet implemented");
    }


    @Test
    @Ignore
    public void testGetGameCount() {
        fail("Not yet implemented");
    }


    @Test
    @Ignore
    public void testGetPlayerNames() {
        fail("Not yet implemented");
    }


    @Test
    @Ignore
    public void testGetScoresColors() {
        fail("Not yet implemented");
    }


    @Test
    @Ignore
    public void testGetBetCountColors() {
        fail("Not yet implemented");
    }


    @Test
    @Ignore
    public void testGetKingCountColors() {
        fail("Not yet implemented");
    }


    @Test
    @Ignore
    public void testGetCalledCountColors() {
        fail("Not yet implemented");
    }


    @Test
    @Ignore
    public void testGetLeadingCountColors() {
        fail("Not yet implemented");
    }


    @Test
    @Ignore
    public void testGetResultsColors() {
        fail("Not yet implemented");
    }
}
