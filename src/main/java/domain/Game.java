package domain;

import java.util.Date;

public class Game {

    public int id;
    public String gameName;
    public String manufacturer;
    public String genre;
    public String clientID;

    public Game(int id, String gameName, String manufacturer, String genre) {
        this.id = id;
        this.gameName = gameName;
        this.manufacturer = manufacturer;
        this.genre = genre;
    }

    public Game(String gameName, String manufacturer, String genre) {
        this.gameName = gameName;
        this.manufacturer = manufacturer;
        this.genre = genre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getClientID() { return this.clientID; }

    public void setRentedBy(String clientID) { this.clientID = clientID; }

    @Override
    public String toString() {
        String cID ;
        if(this.clientID == null)
            cID = "Not Rented";
        else
            cID = this.clientID;
        return "Game ID: "+this.id+
                "\nGame Name: "+this.gameName+
                "\nManufacturer: "+this.manufacturer+
                "\nGenre: "+this.genre+
                "\nRented by ID: "+cID+"\n";
    }

}
