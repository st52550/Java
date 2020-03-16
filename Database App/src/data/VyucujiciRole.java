package data;

/**
 *
 * @author st52550
 */
public class VyucujiciRole {
    private int idPredmet;
    private String titulPred;
    private String prijmeni;
    private String jmeno;
    private String titulZa;
    private String role;
    
    public VyucujiciRole(int idPredmet, String titulPred, String prijmeni, 
            String jmeno, String titulZa, String role) {
        this.idPredmet = idPredmet;
        this.titulPred = titulPred;
        this.prijmeni = prijmeni;
        this.jmeno = jmeno;
        this.titulZa = titulZa;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    public int getIdPredmet() {
        return idPredmet;
    }

    public void setIdPredmet(int idPredmet) {
        this.idPredmet = idPredmet;
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
}
