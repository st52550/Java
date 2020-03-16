package data;

/**
 *
 * @author st52550
 */
public class FormaVyuky {
    private int id;
    private String forma;

    public FormaVyuky(int id, String forma) {
        this.id = id;
        this.forma = forma;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getForma() {
        return forma;
    }

    public void setForma(String forma) {
        this.forma = forma;
    }

    @Override
    public String toString() {
        return forma;
    }  
}
