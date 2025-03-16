package tra4.bogdan.vetregistry2;

public class TownItem {
    private String townName;
    private int townId;

    public TownItem(int townId, String townName) {
        this.townName = townName;
        this.townId = townId;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public int getTownId() {
        return townId;
    }

    public void setTownId(int townId) {
        this.townId = townId;
    }

    @Override
    public String toString() {
        return this.getTownName();
    }
}
