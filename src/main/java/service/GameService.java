package service;

import domain.Game;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameService {
    private ConnectionClass con;

    private final String INSERT_GAME= "INSERT INTO GAME(gameName,manufacturer,genre,clientID) VALUES (?,?,?,?)";
    private final String SELECT_GAMES = "SELECT id, gameName, manufacturer, genre, clientID from Game";
    private final String SELECT_CLIENT_ID_GAME_BY_ID = "SELECT clientID from GAME where id = ?";
    private final String UPDATE_CLIENT_ID = "UPDATE Game SET clientID = ? WHERE id = ?";
    private final String CLIENT_ID_TO_NULL = "UPDATE Game SET clientID = null WHERE id = ?";
    private final String DELETE_GAME = "DELETE from Game WHERE id = ?";
    private final String DROP_TABLE = "DROP TABLE Game";
    private final String TOTAL_RECORDS = "SELECT count(*) as total from Game";
    private final String FIND_GAME = "SELECT gameName from GAME WHERE id = ?";


    private PreparedStatement insertGamePST;
    private PreparedStatement selectGamePST;
    private PreparedStatement selectCIDByID;
    private PreparedStatement rentGamePST;
    private PreparedStatement returnGamePST;
    private PreparedStatement deleteGamePST;
    private PreparedStatement dropTablePST;
    private PreparedStatement getTotalRecordsPST;
    private PreparedStatement findGamePST;

    public GameService(ConnectionClass con) {
        try {
            this.con = con;


            insertGamePST = this.con.getCon().prepareStatement(INSERT_GAME);
            selectGamePST = this.con.getCon().prepareStatement(SELECT_GAMES);
            selectCIDByID = this.con.getCon().prepareStatement(SELECT_CLIENT_ID_GAME_BY_ID);
            rentGamePST = this.con.getCon().prepareStatement(UPDATE_CLIENT_ID);
            returnGamePST = this.con.getCon().prepareStatement(CLIENT_ID_TO_NULL);
            deleteGamePST = this.con.getCon().prepareStatement(DELETE_GAME);
            dropTablePST = this.con .getCon().prepareStatement(DROP_TABLE);
            getTotalRecordsPST = this.con.getCon().prepareStatement(TOTAL_RECORDS);
            findGamePST = this.con.getCon().prepareStatement(FIND_GAME);


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

    public String findGame(int gameId) {
        try {
            findGamePST.setInt(1, gameId);
            ResultSet rs = findGamePST.executeQuery();
            String gameName;
            while(rs.next()) {
                gameName = rs.getString("gameName");
                return gameName;
            }
            return null;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addGame(Game g) {
        try {
            con.getCon().setAutoCommit(true);
            insertGamePST.setString(1,g.getGameName());
            insertGamePST.setString(2,g.getManufacturer());
            insertGamePST.setString(3,g.getGenre());
            insertGamePST.setString(4,null);
            insertGamePST.executeUpdate();
            System.out.println("Game has been added!");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean removeGame(int gameID) {
        try {
            con.getCon().setAutoCommit(true);
            deleteGamePST.setInt(1,gameID);
            deleteGamePST.executeUpdate();
            System.out.println("Game has been removed!");
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Game> getGames() {
        List<Game> gameList = new ArrayList<Game>();

        try {
            con.getCon().setAutoCommit(true);
            ResultSet rs = selectGamePST.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String gameName = rs.getString("gameName");
                String manufacturer = rs.getString("manufacturer");
                String genre = rs.getString("genre");
                String clientID = rs.getString("clientID");
                Game g = new Game(id, gameName, manufacturer, genre);
                g.setRentedBy(clientID);
                gameList.add(g);
            }
        }
        catch (SQLException e ) {
            e.printStackTrace();
        }

        return gameList;
    }

    public boolean addGames(List<Game> gameList) {
        try {
            con.getCon().setAutoCommit(false);
            for(Game g: gameList) {
                insertGamePST.setString(1,g.getGameName());
                insertGamePST.setString(2,g.getManufacturer());
                insertGamePST.setString(3,g.getGenre());
                insertGamePST.setString(4,null);
                insertGamePST.executeUpdate();

            }
            con.getCon().commit();
            System.out.println("Games have been added!");
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

    public boolean removeGames(List<Integer> gameIdList) {
        try {
            con.getCon().setAutoCommit(false);
            for(int i: gameIdList){
                deleteGamePST.setInt(1,i);
                deleteGamePST.executeUpdate();
            }
            System.out.println("Games have been removed");
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

    public int getCIDbyID(int gameID) {
        try {
            selectCIDByID.setInt(1, gameID);
            ResultSet rs = selectCIDByID.executeQuery();
            while(rs.next()) {
                String id = rs.getString("clientID");

                if (id == null) {
                    return -1;
                }
                System.out.println(Integer.valueOf(id));
                return Integer.valueOf(id);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean rentGames(List<Integer> gameList, int clientID) {
        try {
            con.getCon().setAutoCommit(false);
            for(int x : gameList) {
                int cID = getCIDbyID(x);
                rentGamePST.setInt(1, clientID);
                rentGamePST.setInt(2, x);
                rentGamePST.executeUpdate();
                if(cID != -1) {
                    System.out.println("One or more games is already rented!");
                    con.getCon().rollback();
                    return false;
                }
            }
            con.getCon().commit();
            System.out.println("Game(s) have been rented!");
            return true;
        }
        catch (SQLException e ) {
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

    public boolean returnGames(List<Integer> gameList) {
        try {
            con.getCon().setAutoCommit(true);
            for (int x : gameList) {
                returnGamePST.setInt(1, x);
                returnGamePST.executeUpdate();
            }
            System.out.println("Games have been returned!");
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void dropTable() {
        try {
            con.getCon().setAutoCommit(true);
            dropTablePST.executeUpdate();
            System.out.println("Table Games has been dropped!");
        }
        catch (SQLException e ) {
            e.printStackTrace();
        }
    }





}
