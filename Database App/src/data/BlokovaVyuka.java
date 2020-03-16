package data;

/**
 *
 * @author st52550
 */
public class BlokovaVyuka {
    private int id;
    private String datum;

    public BlokovaVyuka(int id, String datum) {
        this.id = id;
        this.datum = datum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }
    
    @Override
    public String toString(){
        return datum;
    }
}
