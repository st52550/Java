package data;

/**
 *
 * @author st52550
 */
public class Vyucujici {
    private int id;
    private String titulPred;
    private String prijmeni;
    private String jmeno;
    private String titulZa;

    public Vyucujici(int id, String titulPred, String prijmeni, String jmeno, String titulZa) {
        this.id = id;
        this.titulPred = titulPred;
        this.prijmeni = prijmeni;
        this.jmeno = jmeno;
        this.titulZa = titulZa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setPrijmeni(String prijmeni) {
        this.prijmeni = prijmeni;
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
