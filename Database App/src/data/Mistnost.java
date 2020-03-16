package data;

/**
 *
 * @author st52550
 */
public class Mistnost {
    private int id;
    private String oznaceni;
    private int kapacita;
    private String katedra;
    private String fakulta;

    public Mistnost(int id, String oznaceni, int kapacita, String katedra, String fakulta) {
        this.id = id;
        this.oznaceni = oznaceni;
        this.kapacita = kapacita;
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

    public int getKapacita() {
        return kapacita;
    }

    public void setKapacita(int kapacita) {
        this.kapacita = kapacita;
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
        return oznaceni;
    }
}
