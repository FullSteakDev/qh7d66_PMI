package Dormitory;

public class Room {

    private int szobaszam;
    private int hallgato;

    public Room(int szobaszam, int hallgato) {
        this.szobaszam = szobaszam;
        this.hallgato = hallgato;
    }

    public int getSzobaszam() {
        return szobaszam;
    }

    public void setSzobaszam(int szobaszam) {
        this.szobaszam = szobaszam;
    }

    public int getHallgato() {
        return hallgato;
    }

    public void setHallgato(int hallgato) {
        this.hallgato = hallgato;
    }
}
