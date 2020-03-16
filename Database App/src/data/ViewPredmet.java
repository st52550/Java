package data;

/**
 *
 * @author st52550
 */
public class ViewPredmet {
    private int id;
    private String nazevP;
    private String zkratkaP;
    private int kredity;
    private int rocnik;
    private int pocetPr;
    private int pocetCv;
    private int pocetSem;
    private String obor;
    private String katedra;
    private String fakulta;

    public ViewPredmet(int id, String nazevP, String zkratkaP, int kredity, int rocnik, 
            int pocetPr, int pocetCv, int pocetSem, String obor, String katedra, String fakulta) {
        this.id = id;
        this.nazevP = nazevP;
        this.zkratkaP = zkratkaP;
        this.kredity = kredity;
        this.rocnik = rocnik;
        this.pocetPr = pocetPr;
        this.pocetCv = pocetCv;
        this.pocetSem = pocetSem;
        this.obor = obor;
        this.katedra = katedra;
        this.fakulta = fakulta;
    }   

    public String getFakulta() {
        return fakulta;
    }

    public void setFakulta(String fakulta) {
        this.fakulta = fakulta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNazevP() {
        return nazevP;
    }

    public void setNazevP(String nazevP) {
        this.nazevP = nazevP;
    }

    public String getZkratkaP() {
        return zkratkaP;
    }

    public void setZkratkaP(String zkratkaP) {
        this.zkratkaP = zkratkaP;
    }

    public int getKredity() {
        return kredity;
    }

    public int getPocetPr() {
        return pocetPr;
    }

    public void setPocetPr(int pocetPr) {
        this.pocetPr = pocetPr;
    }

    public int getPocetCv() {
        return pocetCv;
    }

    public void setPocetCv(int pocetCv) {
        this.pocetCv = pocetCv;
    }

    public int getPocetSem() {
        return pocetSem;
    }

    public void setPocetSem(int pocetSem) {
        this.pocetSem = pocetSem;
    }

    public String getObor() {
        return obor;
    }

    public void setObor(String obor) {
        this.obor = obor;
    }

    public void setKredity(int kredity) {
        this.kredity = kredity;
    }

    public int getRocnik() {
        return rocnik;
    }

    public void setRocnik(int rocnik) {
        this.rocnik = rocnik;
    }

    public String getKatedra() {
        return katedra;
    }

    public void setKatedra(String katedra) {
        this.katedra = katedra;
    }
}
