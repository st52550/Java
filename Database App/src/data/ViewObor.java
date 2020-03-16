package data;

/**
 *
 * @author st52550
 */
public class ViewObor {
    private int id;
    private String oznaceni;
    private String nazev;
    private String katedra;
    private String fakulta;

    public ViewObor(int id, String oznaceni, String nazev, String katedra, String fakulta) {
        this.id = id;
        this.oznaceni = oznaceni;
        this.nazev = nazev;
        this.katedra = katedra;
        this.fakulta = fakulta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOznaceni() {
        return oznaceni;
    }

    public void setOznaceni(String oznaceni) {
        this.oznaceni = oznaceni;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public String getKatedra() {
        return katedra;
    }

    public void setKatedra(String katedra) {
        this.katedra = katedra;
    }

    public String getFakulta() {
        return fakulta;
    }

    public void setFakulta(String fakulta) {
        this.fakulta = fakulta;
    }
    
    @Override
    public String toString(){
        return nazev;
    }
    
}
