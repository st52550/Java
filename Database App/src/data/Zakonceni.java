package data;

/**
 *
 * @author st52550
 */
public class Zakonceni {
    private int id;
    private String zakonceni;

    public Zakonceni(int id, String zakonceni) {
        this.id = id;
        this.zakonceni = zakonceni;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getZakonceni() {
        return zakonceni;
    }

    public void setZakonceni(String zakonceni) {
        this.zakonceni = zakonceni;
    }

    @Override
    public String toString() {
        return zakonceni;
    }  
}
