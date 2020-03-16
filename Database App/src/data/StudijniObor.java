package data;

/**
 *
 * @author st52550
 */
public class StudijniObor {
    private int id;
    private String nazev;

    public StudijniObor(int id, String nazev) {
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
    
}
