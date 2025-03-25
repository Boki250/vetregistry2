package tra4.bogdan.vetregistry2;

public class Town {
    private String townName;
    private int townId;
    private int townZip;

    public Town(int townId, String townName, int townZip) {
        this.townName = townName;
        this.townId = townId;
        this.townZip = townZip;
    }

    public String getName() {
        return townName;
    }

    public void setName(String townName) {
        this.townName = townName;
    }

    public int getId() {
        return townId;
    }

    public void setId(int townId) {
        this.townId = townId;
    }

    public int getZip() { return townZip;}

    public void setZip(int townZip) { this.townZip = townZip;}

    @Override
    public String toString() {
        return this.getName();
    }
}
