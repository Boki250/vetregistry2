package tra4.bogdan.vetregistry2;

public class SpeciesItem {
    private String speciesName;
    private int speciesId;

    public SpeciesItem(int speciesId, String speciesName) {
        this.speciesName = speciesName;
        this.speciesId = speciesId;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesname(String speciesName) {
        this.speciesName = speciesName;
    }

    public int getSpeciesId() {
        return speciesId;
    }

    public void setSpeciesId(int speciesId) {
        this.speciesId = speciesId;
    }

    @Override
    public String toString() {
        return this.getSpeciesName();
    }
}
