package data;

/**
 *
 * @author st52550
 */
public class AkRok {
    private int id;
    private String rok;

    public AkRok(int id, String rok) {
        this.id = id;
        this.rok = rok;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRok() {
        return rok;
    }

    public void setRok(String rok) {
        this.rok = rok;
    }
    
    @Override
    public String toString(){
        return rok;
    }
}
