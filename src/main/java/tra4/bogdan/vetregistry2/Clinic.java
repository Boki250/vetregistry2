package tra4.bogdan.vetregistry2;

public class Clinic {
    private int id;
    private String title;
    private String address;
    private TownItem town;
    private String phoneNumber;

    public Clinic(int id, String title, String address, TownItem town, String phoneNumber) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.town = town;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public TownItem getTown() {
        return town;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

}
