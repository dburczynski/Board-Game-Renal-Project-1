import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import domain.Client;
import domain.Game;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import service.ClientService;
import service.ConnectionClass;
import service.GameService;
import service.TableCreate;

public class JUnitTest {
    static ConnectionClass con;
    static ClientService cs;
    static GameService gs;
    @BeforeClass
    public static void init() {
        con = new ConnectionClass();
        con.openConnection();
        TableCreate t = new TableCreate(con);
        cs = new ClientService(con);
        gs = new GameService(con);
        initDatabase();

    }

    @AfterClass
    public static void destroy() {
        gs.dropTable();
        cs.dropTable();
    }

    private static void initDatabase() {
        List<Client> clientList = new ArrayList<Client>();
        List<Game>  gameList = new ArrayList<Game>();

        gameList.add(new Game("GoT","Hasbro","Strategy"));
        gameList.add(new Game("Monopoly","GenericCompany","Family"));
        gameList.add(new Game("Risk","Hasbro","Strategy"));
        gameList.add(new Game("Dixit","GenericCompany","Family"));
        gameList.add(new Game("Eclipse","RandomCompany","SciFi"));
        gameList.add(new Game("The Game of Life","SomeCompany","Family"));

        clientList.add(new Client("Adam",new Date(2010,6,12),"740934123","Grunwaldzka 12"));
        clientList.add(new Client("Pawel",new Date(2016,10,3),"193892012","Rakoczego 4"));
        clientList.add(new Client("Michal",new Date(2018,5,5),"214049124","Bazynskiego 5"));
        clientList.add(new Client("Sandra", new Date(2019,6,1),"592852163","Traugutta 4"));

        gs.addGames(gameList);
        cs.addClients(clientList);

    }

    @Test
    public void getTotalRecordsGameTest() {
        assertThat(6, is(gs.getTotalRecords()));
    }
    @Test
    public void getTotalRecordsClientTest() {
        assertThat(4,is(cs.getTotalRecords()));
    }
    @Test
    public void checkRentingIfNoRenterTest() {
        List<Integer> gamesToRent = new ArrayList<Integer>();
        gamesToRent.add(0);
        gamesToRent.add(1);
        gamesToRent.add(2);
        assertThat(true, is(gs.rentGames(gamesToRent,0)));

        List<Game> actualGames = new ArrayList<Game>();
        Game g1 = new Game(0,"GoT","Hasbro","Strategy");
        g1.setRentedBy("0");
        Game g2 = new Game(1,"Monopoly","GenericCompany","Family");
        g2.setRentedBy("0");
        Game g3 = new Game(2,"Risk","Hasbro","Strategy");
        g3.setRentedBy("0");
        actualGames.add(g1);
        actualGames.add(g2);
        actualGames.add(g3);
        assertArrayEquals(actualGames.toArray(),cs.getClientGames(0).toArray());
    }
    @Test
    public void checkRentingIfRentedTest() {
        List<Integer> gamesToRent = new ArrayList<Integer>();
        gamesToRent.add(0);
        gamesToRent.add(1);
        gamesToRent.add(2);
        gs.rentGames(gamesToRent, 0);
        assertThat(false, is(gs.rentGames(gamesToRent,1)));
    }
    @Test
    public void checkIfReturnedTest() {
        List<Integer> gamesToRent = new ArrayList<Integer>();
        gamesToRent.add(0);
        gs.rentGames(gamesToRent,0);
        List<Integer> gamesToReturn = new ArrayList<Integer>();
        gamesToReturn.add(0);
        gs.returnGames(gamesToReturn);
        List<Game> games = gs.getGames();
        Game g = games.get(5);
        assertThat(null, is(g.getClientID()));
    }
    @Test
    public void checkFindClientTest() {
        assertThat("Adam", is(cs.findClient(0)));
    }
    @Test
    public void checkFindGameTest() {
        assertThat("Monopoly", is(gs.findGame(1)));
    }
    @Test
    public void removeGameTest() {
        gs.removeGame(5);
        assertThat(null, is(gs.findGame(5)));
    }
    @Test
    public void removeClientWhileRentingTest() {
        List<Integer> gamesToRent = new ArrayList<Integer>();
        gamesToRent.add(0);
        gamesToRent.add(1);
        gamesToRent.add(2);
        gs.rentGames(gamesToRent, 0);
        cs.removeClient(0);
        assertThat("Adam", is(cs.findClient(0)));
    }
    @Test
    public void removeClientTest() {
        cs.removeClient(2);
        assertThat("Michal", is(not(cs.findClient(2))));
    }

}
