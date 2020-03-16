package data;

/**
 *
 * @author st52550
 */
public class Prihlaseni {
    private int id;
    private String username;
    private String heslo;
    private int opravneni;
    private int id_vyucujici;

    public Prihlaseni(int id, String username, String heslo, int opravneni, int id_vyucujici) {
        this.id = id;
        this.username = username;
        this.heslo = heslo;
        this.opravneni = opravneni;
        this.id_vyucujici = id_vyucujici;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeslo() {
        return heslo;
    }

    public void setHeslo(String heslo) {
        this.heslo = heslo;
    }

    public int getOpravneni() {
        return opravneni;
    }

    public void setOpravneni(int opravneni) {
        this.opravneni = opravneni;
    }

    public int getId_vyucujici() {
        return id_vyucujici;
    }

    public void setId_vyucujici(int id_vyucujici) {
        this.id_vyucujici = id_vyucujici;
    }

    
    
    @Override
    public String toString(){
        String s = username + " " + heslo + " " + opravneni + " " + id_vyucujici;
        s = s.replace("null", "");
        return s;
    }
}
