package data;

/**
 *
 * @author st52550
 */
public class ViewKatedry {
    private int id;
    private String zkratkaK;
    private String nazevK;
    private String zkratkaF;
    private String nazevF;
    private int telefon;
    private int mobil;
    private String email;

    public ViewKatedry(int id, String zkratkaK, String nazevK, String zkratkaF,
            String nazevF,int telefon, int mobil, String email) {
        this.id = id;
        this.zkratkaK = zkratkaK;
        this.nazevK = nazevK;
        this.zkratkaF = zkratkaF;
        this.nazevF = nazevF;
        this.telefon = telefon;
        this.mobil = mobil;
        this.email = email;
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

    public int getId() {
        return id;
    }
    
    public String getZkratkaK() {
        return zkratkaK;
    }

    public void setZkratkaK(String zkratkaK) {
        this.zkratkaK = zkratkaK;
    }

    public String getNazevK() {
        return nazevK;
    }

    public void setNazevK(String nazevK) {
        this.nazevK = nazevK;
    }

    public String getZkratkaF() {
        return zkratkaF;
    }

    public void setZkratkaF(String zkratkaF) {
        this.zkratkaF = zkratkaF;
    }

    public String getNazevF() {
        return nazevF;
    }

    public void setNazevF(String nazevF) {
        this.nazevF = nazevF;
    }
}
