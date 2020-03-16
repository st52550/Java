package data;

/**
 *
 * @author st52550
 */
public class PridaniAkce {
    private String muzePridat;
    private String hlaska;

    public PridaniAkce(String muzePridat, String hlaska) {
        this.muzePridat = muzePridat;
        this.hlaska = hlaska;
    }

    public String getMuzePridat() {
        return muzePridat;
    }

    public void setMuzePridat(String muzePridat) {
        this.muzePridat = muzePridat;
    }

    public String getHlaska() {
        return hlaska;
    }

    public void setHlaska(String hlaska) {
        this.hlaska = hlaska;
    }
    
    
}
