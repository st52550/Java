package data;

/**
 *
 * @author st52550
 */
public class Kategorie {
    private int id;
    private String kategorie;

    public Kategorie(int id, String kategorie) {
        this.id = id;
        this.kategorie = kategorie;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKategorie() {
        return kategorie;
    }

    public void setKategorie(String kategorie) {
        this.kategorie = kategorie;
    }

    @Override
    public String toString() {
        return kategorie;
    }   
}
