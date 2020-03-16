package data;

/**
 *
 * @author st52550
 */
public class Katedra {
    private int id;
    private String nazevK;

    public Katedra(int id, String nazevK) {
        this.id = id;
        this.nazevK = nazevK;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNazevK() {
        return nazevK;
    }

    public void setNazevK(String nazevK) {
        this.nazevK = nazevK;
    }
}
