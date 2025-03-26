package tra4.bogdan.vetregistry2;

public class Species {
    private String speciesName;
    private int speciesId;

    public Species(int speciesId, String speciesName) {
        this.speciesName = speciesName;
        this.speciesId = speciesId;
    }

    public String getName() {
        return speciesName;
    }

    public void setName(String speciesName) {
        this.speciesName = speciesName;
    }

    public int getId() {
        return speciesId;
    }

    public void setId(int speciesId) {
        this.speciesId = speciesId;
    }


    @Override
    public String toString() {
        return this.getName();
    }
}
