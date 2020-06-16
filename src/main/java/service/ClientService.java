package service;

import domain.Client;
import domain.Game;
import org.hsqldb.SqlInvariants;

import java.security.Permission;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientService {
    private ConnectionClass con;

    private final String INSERT_CLIENT = "INSERT INTO Client(name,dateOfJoin,phoneNumber,address) VALUES (?,?,?,?)";
    private final String SELECT_CLIENTS = "SELECT id, name, dateOfJoin, phoneNumber, address from Client";
    private final String CLIENT_GAMES_FULL = "SELECT * from Game WHERE clientID = ?";
    private final String DELETE_CLIENT = "DELETE from Client WHERE id = ?";
    private final String DROP_TABLE = "DROP TABLE Client";
    private final String TOTAL_RECORDS = "SELECT count(*) as total from Client";
    private final String FIND_CLIENT = "SELECT name from Client WHERE id = ?";

    private PreparedStatement insertPersonPST;
    private PreparedStatement selectPersonPST;
    private PreparedStatement selectClientGamesPST;
    private PreparedStatement deleteClientPST;
    private PreparedStatement dropTablePST;
    private PreparedStatement getTotalRecordsPST;
    private PreparedStatement findClientPST;

    public ClientService(ConnectionClass con) {
        try {
            this.con = con;

            insertPersonPST = this.con.getCon().prepareStatement(INSERT_CLIENT);
            selectPersonPST = this.con.getCon().prepareStatement(SELECT_CLIENTS);
            selectClientGamesPST = this.con.getCon().prepareStatement(CLIENT_GAMES_FULL);
            deleteClientPST = this.con.getCon().prepareStatement(DELETE_CLIENT);
            dropTablePST = this.con.getCon().prepareStatement(DROP_TABLE);
            getTotalRecordsPST = this.con.getCon().prepareStatement(TOTAL_RECORDS);
            findClientPST = this.con.getCon().prepareStatement(FIND_CLIENT);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getTotalRecords() {
        int total = 0;
        try {
            ResultSet rs = getTotalRecordsPST.executeQuery();
            while(rs.next()) {
                total = rs.getInt("total");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public String findClient(int id) {
        try {
            findClientPST.setInt(1, id);
            ResultSet rs = findClientPST.executeQuery();
            String clientName;
            while(rs.next()) {
                clientName = rs.getString("name");
                return clientName;
            }
            return null;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean addClient(Client c) {
        try {
            con.getCon().setAutoCommit(true);
            insertPersonPST.setString(1, c.getName());
            insertPersonPST.setDate(2, c.getDateOfJoin());
            insertPersonPST.setString(3, c.getPhoneNumber());
            insertPersonPST.setString(4, c.getAddress());
            insertPersonPST.executeUpdate();
            System.out.println("Client has been added!");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Client> getClients() {
        List<Client> clientList = new ArrayList<Client>();

        try {
            con.getCon().setAutoCommit(true);
            ResultSet rs = selectPersonPST.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Date dateOfJoin = rs.getDate("dateOfJoin");
                String phoneNumber = rs.getString("phoneNumber");
                String address = rs.getString("address");

                Client c = new Client(id, name, dateOfJoin, phoneNumber, address);
                clientList.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clientList;
    }

    public boolean removeClient(int clientID) {
        try {
            con.getCon().setAutoCommit(true);
            selectClientGamesPST.setInt(1,clientID);
            ResultSet rs = selectClientGamesPST.executeQuery();
            int counter = 0;
            while(rs.next())
                counter++;
            if(counter > 0) {
                System.out.println("Can't remove client, he/she has rented games!");
                return false;
            }
            deleteClientPST.setInt(1,clientID);
            deleteClientPST.executeUpdate();
            System.out.println("Client has been removed!");
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addClients(List<Client> clientList) {
        try {
            con.getCon().setAutoCommit(false);
            for(Client c: clientList) {
                insertPersonPST.setString(1,c.getName());
                insertPersonPST.setDate(2,c.getDateOfJoin());
                insertPersonPST.setString(3,c.getPhoneNumber());
                insertPersonPST.setString(4,c.getAddress());
                insertPersonPST.executeUpdate();
            }
            con.getCon().commit();
            System.out.println("Clients have been added!");
            return true;
        }
        catch (SQLException e) {
            try {
                con.getCon().rollback();
                return false;
            }
            catch (SQLException e2) {
                e2.printStackTrace();
                return false;
            }
        }
    }

    public boolean removeClients(List<Integer> clientList) {
        try {
            con.getCon().setAutoCommit(false);
            for(int id: clientList) {
                deleteClientPST.setInt(1,id);
                deleteClientPST.executeUpdate();
                selectClientGamesPST.setInt(1,id);
                ResultSet rs = selectPersonPST.executeQuery();
                int counter = 0;
                while (rs.next())
                    counter++;
                if(counter > 0) {
                    con.getCon().rollback();
                    System.out.println("One or more clients have rented games!");
                    return false;
                }
            }
            con.getCon().commit();
            System.out.println("Clients have been removed!");
            return true;
        }
        catch (SQLException e) {
            try {
                con.getCon().rollback();
                return false;
            }
            catch (SQLException e2){
                e2.printStackTrace();
                return false;
            }
        }
    }

    public List<Game> getClientGames(int id) {
        List<Game> gameList = new ArrayList<Game>();
        try {
            con.getCon().setAutoCommit(true);
            selectClientGamesPST.setInt(1,id);
            ResultSet rs = selectClientGamesPST.executeQuery();
            while(rs.next()) {
                int idGame = rs.getInt("id");
                String gameName = rs.getString("gameName");
                String manufacturer = rs.getString("manufacturer");
                String genre = rs.getString("genre");
                Game g = new Game(idGame, gameName, manufacturer, genre);
                g.setRentedBy(String.valueOf(id));
                gameList.add(g);
            }
            return gameList;
        }
        catch (SQLException e) {
            System.out.println("Failed to retrieve client's games");
            e.printStackTrace();
            return null;
        }
    }

    public void dropTable() {
        try {
            con.getCon().setAutoCommit(true);
            dropTablePST.executeUpdate();
            System.out.println("Table Client has been deleted!");
        }
        catch (SQLException e) {
            try {
                con.getCon().rollback();
                System.out.println("Failed to delete table Client");
            }
            catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
    }




}
