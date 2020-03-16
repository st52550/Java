package data;

/**
 *
 * @author st52550
 */
public class ViewVyucujici {
    private int idVyucujici;
    private String titulPred;
    private String prijmeni;
    private String jmeno;
    private String titulZa;
    private String katedra;
    private String fakulta;
    private int telefon;
    private String email;
    private int mobil;
    
    public ViewVyucujici(int idVyucujici, String titulPred, String prijmeni, 
            String jmeno, String titulZa, String katedra, String fakulta, 
            int telefon, int mobil, String email) {
        this.idVyucujici = idVyucujici;
        this.titulPred = titulPred;
        this.prijmeni = prijmeni;
        this.jmeno = jmeno;
        this.titulZa = titulZa;
        this.katedra = katedra;
        this.fakulta = fakulta;
        this.telefon = telefon;
        this.mobil = mobil;
        this.email = email;
    }

    public String getFakulta() {
        return fakulta;
    }

    public void setFakulta(String fakulta) {
        this.fakulta = fakulta;
    }

    public int getMobil() {
        return mobil;
    }

    public void setMobil(int mobil) {
        this.mobil = mobil;
    }
    
    public int getTelefon() {
        return telefon;
    }

    public void setTelefon(int telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    

    public String getKatedra() {
        return katedra;
    }

    public void setKatedra(String katedra) {
        this.katedra = katedra;
    }

    public int getIdVyucujici() {
        return idVyucujici;
    }

    public void setIdVyucujici(int idVyucujici) {
        this.idVyucujici = idVyucujici;
    }

    public String getTitulPred() {
        return titulPred;
    }

    public void setTitulPred(String titulPred) {
        this.titulPred = titulPred;
    }

    public String getPrijmeni() {
        return prijmeni;
    }

    public void setPrijemni(String prijemni) {
        this.prijmeni = prijemni;
    }

    public String getJmeno() {
        return jmeno;
    }

    public void setJmeno(String jmeno) {
        this.jmeno = jmeno;
    }

    public String getTitulZa() {
        return titulZa;
    }

    public void setTitulZa(String titulZa) {
        this.titulZa = titulZa;
    }
    
    @Override
    public String toString(){
        String s = titulPred + " " + prijmeni + " " + jmeno + " " + titulZa;
        s = s.replace("null", "");
        return s;
    }
}
