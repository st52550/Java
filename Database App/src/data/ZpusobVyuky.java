package data;

/**
 *
 * @author st52550
 */
public class ZpusobVyuky {
    private int id;
    private String zpusobVyuky;

    public ZpusobVyuky(int id, String zpusobVyuky) {
        this.id = id;
        this.zpusobVyuky = zpusobVyuky;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getZpusobVyuky() {
        return zpusobVyuky;
    }

    public void setZpusobVyuky(String zpusobVyuky) {
        this.zpusobVyuky = zpusobVyuky;
    }

    @Override
    public String toString() {
        return zpusobVyuky;
    }  
}
