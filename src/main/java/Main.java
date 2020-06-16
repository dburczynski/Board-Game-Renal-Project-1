import domain.Client;
import domain.Game;
import service.ClientService;
import service.ConnectionClass;
import service.GameService;
import service.TableCreate;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ConnectionClass connection = new ConnectionClass();
        connection.openConnection();

        TableCreate t = new TableCreate(connection);
        GameService gs = new GameService(connection);
        ClientService cs = new ClientService(connection);

        gs.addGame(new Game("GoT","Hasbro","Strategy"));
        List<Game> gameList = new ArrayList<Game>();
        gameList.add(new Game("Monopoly","GenericCompany","Family"));
        gameList.add(new Game("Risk","Hasbro","Strategy"));
        gameList.add(new Game("Dixit","GenericCompany","Family"));
        gs.addGames(gameList);
        //System.out.println(gs.getGames().toString());

        cs.addClient(new Client("Darek",new Date(2018,12,31),"736888296", "Rakoczego 9"));
        List<Client> clientList = new ArrayList<Client>();
        clientList.add(new Client("Andrzej", new Date(2019,3,14),"222333444","Grunwaldzka 12"));
        clientList.add(new Client("Sandra", new Date(2016,6,17),"111009013","Bazynskiego 2"));
        cs.addClients(clientList);
        //System.out.print(cs.getClients().toString());

        List<Integer> gamesDarek = new ArrayList<Integer>();
        gamesDarek.add(0);
        gamesDarek.add(1);

        List<Integer> gamesAndrzej = new ArrayList<Integer>();
        gamesAndrzej.add(2);
        gamesAndrzej.add(3);

        List<Integer> gamesSandra = new ArrayList<Integer>();
        gamesSandra.add(0);

        gs.rentGames(gamesDarek,0);
        gs.rentGames(gamesAndrzej,1);
        gs.rentGames(gamesSandra,2);

        cs.removeClient(0);

        List<Integer> gamesToReturn = new ArrayList<Integer>();
        gamesToReturn.add(0);
        gamesToReturn.add(1);
        gs.returnGames(gamesToReturn);

        cs.removeClient(0);

        gs.rentGames(gamesSandra,2);

        List<Integer> removeGameList = new ArrayList<Integer>();
        removeGameList.add(2);

        gs.removeGames(removeGameList);

        System.out.println(cs.getClientGames(1));




        //cs.dropTable();
        gs.dropTable();
        cs.dropTable();

        connection.closeConnection();


    }
}
