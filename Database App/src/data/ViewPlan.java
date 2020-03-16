package data;

/**
 *
 * @author st52550
 */
public class ViewPlan {
    private int id;
    private int idKategore;
    private String kategorie;
    private int idPredmet;
    private String nazevP;
    private String zkratkaP;
    private int delkaP;
    private int odhadS;
    private int rocnik;
    private int idZpusob;
    private String zpusobV;
    private int idZakonceni;
    private String zakonceni;
    private int idForma;
    private String formaV;
    private int idSemestr;
    private String semestr; 

    public ViewPlan(int id, int idKategorie, String kategorie, int idPredmet, String nazevP, String zkratkaP, 
            int delkaP, int odhadS, int rocnik, int idZpusob, String zpusobV, int idSemestr, String semestr, 
            int idZakonceni, String zakonceni, int idForma, String formaV) {
        this.id = id;
        this.idPredmet = idPredmet;
        this.nazevP = nazevP;
        this.zkratkaP = zkratkaP;
        this.delkaP = delkaP;
        this.odhadS = odhadS;
        this.rocnik = rocnik;
        this.zpusobV = zpusobV;
        this.semestr = semestr;
        this.zakonceni = zakonceni;
        this.formaV = formaV;
        this.kategorie = kategorie;
        this.idKategore = idKategorie;
        this.idZpusob = idZpusob;
        this.idZakonceni = idZakonceni;
        this.idForma = idForma;
        this.idSemestr = idSemestr;
    }

    public int getRocnik() {
        return rocnik;
    }

    public void setRocnik(int rocnik) {
        this.rocnik = rocnik;
    }

    public int getIdPredmet() {
        return idPredmet;
    }

    public void setIdPredmet(int idPredmet) {
        this.idPredmet = idPredmet;
    }

    public int getIdKategore() {
        return idKategore;
    }

    public void setIdKategore(int idKategore) {
        this.idKategore = idKategore;
    }

    public int getIdZpusob() {
        return idZpusob;
    }

    public void setIdZpusob(int idZpusob) {
        this.idZpusob = idZpusob;
    }

    public int getIdZakonceni() {
        return idZakonceni;
    }

    public void setIdZakonceni(int idZakonceni) {
        this.idZakonceni = idZakonceni;
    }

    public int getIdForma() {
        return idForma;
    }

    public void setIdForma(int idForma) {
        this.idForma = idForma;
    }

    public int getIdSemestr() {
        return idSemestr;
    }

    public void setIdSemestr(int idSemestr) {
        this.idSemestr = idSemestr;
    }

    public int getDelkaP() {
        return delkaP;
    }

    public void setDelkaP(int delkaP) {
        this.delkaP = delkaP;
    }

    public int getOdhadS() {
        return odhadS;
    }

    public void setOdhadS(int odhadS) {
        this.odhadS = odhadS;
    }

    public String getKategorie() {
        return kategorie;
    }

    public void setKategorie(String kategorie) {
        this.kategorie = kategorie;
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

    public String getZpusobV() {
        return zpusobV;
    }

    public void setZpusobV(String zpusobV) {
        this.zpusobV = zpusobV;
    }

    public String getSemestr() {
        return semestr;
    }

    public void setSemestr(String semestr) {
        this.semestr = semestr;
    }

    public String getZakonceni() {
        return zakonceni;
    }

    public void setZakonceni(String zakonceni) {
        this.zakonceni = zakonceni;
    }

    public String getFormaV() {
        return formaV;
    }

    public void setFormaV(String formaV) {
        this.formaV = formaV;
    }
    
    @Override
    public String toString(){
        return zkratkaP + "  " + nazevP + "  " + zpusobV;
    }
}
