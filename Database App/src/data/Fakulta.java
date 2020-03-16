package data;

/**
 *
 * @author st52550
 */
public class Fakulta {
    private int id;
    private String zkratkaF;
    private String nazevF;
    private int telefon;
    private int mobil;
    private String email;

    public Fakulta(int id, String zkratkaF, String nazevF, int telefon, int mobil, String email) {
        this.id = id;
        this.zkratkaF = zkratkaF;
        this.nazevF = nazevF;
        this.telefon = telefon;
        this.mobil = mobil;
        this.email = email;
    }

    public String getZkratkaF() {
        return zkratkaF;
    }

    public void setZkratkaF(String zkratkaF) {
        this.zkratkaF = zkratkaF;
    }

    public int getTelefon() {
        return telefon;
    }

    public void setTelefon(int telefon) {
        this.telefon = telefon;
    }

    public int getMobil() {
        return mobil;
    }

    public void setMobil(int mobil) {
        this.mobil = mobil;
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

    public void setId(int id) {
        this.id = id;
    }

    public String getNazevF() {
        return nazevF;
    }

    public void setNazevF(String nazevF) {
        this.nazevF = nazevF;
    }
    
    @Override
    public String toString(){
        return nazevF;
    }
}
