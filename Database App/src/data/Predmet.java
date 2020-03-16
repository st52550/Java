package data;

/**
 *
 * @author st52550
 */
public class Predmet {
    private int id;
    private String nazev;

    public Predmet(int id, String nazev) {
        this.id = id;
        this.nazev = nazev;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    @Override
    public String toString() {
        return nazev;
    }
    
    
}
