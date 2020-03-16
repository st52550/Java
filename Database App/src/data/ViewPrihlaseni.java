package data;

/**
 *
 * @author Spektrom
 */
public class ViewPrihlaseni {
    private int id_user;
    private int id_vyucujici;
    private String username;
    private String prijmeni;
    private String jmeno;
    private String opravneni;

    public ViewPrihlaseni(int id_user, int id_vyucujici, String username, String prijmeni, String jmeno, int opravneni) {
        this.id_user = id_user;
        this.id_vyucujici = id_vyucujici;
        this.username = username;
        this.prijmeni = prijmeni;
        this.jmeno = jmeno;
        if (opravneni == 0) {
            this.opravneni = "Admin/vyucujici";
        }else  {
            this.opravneni = "Vyucujici";
        }
       
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getId_vyucujici() {
        return id_vyucujici;
    }

    public void setId_vyucujici(int id_vyucujici) {
        this.id_vyucujici = id_vyucujici;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getOpravneni() {
        return opravneni;
    }

    public void setOpravneni(String opravneni) {
        this.opravneni = opravneni;
    }

    
    
    
    
    
    
}
