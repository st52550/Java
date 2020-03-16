package data;

/**
 *
 * @author st52550
 */
public class Semestr {
    private int id;
    private String semestr;

    public Semestr(int id, String semestr) {
        this.id = id;
        this.semestr = semestr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSemestr() {
        return semestr;
    }

    public void setSemestr(String semestr) {
        this.semestr = semestr;
    }

    @Override
    public String toString() {
        return semestr;
    }   
}
