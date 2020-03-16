package data;

/**
 *
 * @author st52550
 */
public class ViewRozvrh {
    private int id;
    private String zkratka;
    private String nazev;
    private String zpusob;
    private int pocetH;
    private String den;
    private String casOd;
    private String casDo;
    private String cas;
    private String mistnost;
    private String vyucujici;
    private String role;
    private int obsazenost;
    private int platnost;
    private String platnostSlovy;
    private int blokovaVyuka;
    private String blokovaVyukaSlovy;
    private String datum;

    public ViewRozvrh(int id, String zkratka, String nazev, String zpusob, int pocetH, String den, String casOd, String casDo, String cas, 
            String mistnost, String vyucujici, String role, int obsazenost, int platnost, String platnostSlovy, 
            int blokovaVyuka, String blokovaVyukaSlovy, String datum) {
        this.id = id;
        this.zkratka = zkratka;
        this.nazev = nazev;
        this.zpusob = zpusob;
        this.pocetH = pocetH;
        this.den = den;
        this.casOd = casOd;
        this.casDo = casDo;
        this.cas = cas;
        this.mistnost = mistnost;
        this.vyucujici = vyucujici;
        this.role = role;
        this.obsazenost = obsazenost;
        this.platnost = platnost;
        this.platnostSlovy = platnostSlovy;
        this.blokovaVyuka = blokovaVyuka;
        this.blokovaVyukaSlovy = blokovaVyukaSlovy;
        this.datum = datum;
    }

    public int getBlokovaVyuka() {
        return blokovaVyuka;
    }

    public void setBlokovaVyuka(int blokovaVyuka) {
        this.blokovaVyuka = blokovaVyuka;
    }

    public String getBlokovaVyukaSlovy() {
        return blokovaVyukaSlovy;
    }

    public void setBlokovaVyukaSlovy(String blokovaVyukaSlovy) {
        this.blokovaVyukaSlovy = blokovaVyukaSlovy;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public int getPocetH() {
        return pocetH;
    }

    public void setPocetH(int pocetH) {
        this.pocetH = pocetH;
    }

    public String getCasOd() {
        return casOd;
    }

    public void setCasOd(String casOd) {
        this.casOd = casOd;
    }

    public String getCasDo() {
        return casDo;
    }

    public void setCasDo(String casDo) {
        this.casDo = casDo;
    }    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getZkratka() {
        return zkratka;
    }

    public void setZkratka(String zkratka) {
        this.zkratka = zkratka;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public String getZpusob() {
        return zpusob;
    }

    public void setZpusob(String zpusob) {
        this.zpusob = zpusob;
    }

    public String getDen() {
        return den;
    }

    public void setDen(String den) {
        this.den = den;
    }

    public String getCas() {
        return cas;
    }

    public void setCas(String cas) {
        this.cas = cas;
    }

    public String getMistnost() {
        return mistnost;
    }

    public void setMistnost(String mistnost) {
        this.mistnost = mistnost;
    }

    public String getVyucujici() {
        return vyucujici;
    }

    public void setVyucujici(String vyucujici) {
        this.vyucujici = vyucujici;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getObsazenost() {
        return obsazenost;
    }

    public void setObsazenost(int obsazenost) {
        this.obsazenost = obsazenost;
    }

    public int getPlatnost() {
        return platnost;
    }

    public void setPlatnost(int platnost) {
        this.platnost = platnost;
    }

    public String getPlatnostSlovy() {
        return platnostSlovy;
    }

    public void setPlatnostSlovy(String platnostSlovy) {
        this.platnostSlovy = platnostSlovy;
    }
    
    @Override
    public String toString(){
        String d = "";
        switch(den){
            case "1": d = "pondělí"; break;
            case "2": d = "úterý"; break;
            case "3": d = "středa"; break;
            case "4": d = "čtvrtek"; break;
            case "5": d = "pátek"; break;
        }
        
        return platnostSlovy + "    " + datum + "     " + d + "     " + zkratka + " " + nazev + " – " + zpusob + ", " + cas + ", " + 
        vyucujici + " – Místnost: " + mistnost + " (Obsazenost: " + obsazenost + ")";
    }
}
