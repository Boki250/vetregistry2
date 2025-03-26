package tra4.bogdan.vetregistry2;

public class Service {
    private String serviceName;
    private int serviceId;
    private int servicePrice;

    public Service(int serviceId, String serviceName, int servicePrice) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
    }

    public String getName() {
        return serviceName;
    }

    public void setName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getId() {
        return serviceId;
    }

    public void setId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getZip() { return servicePrice;}

    public void setZip(int servicePrice) { this.servicePrice = servicePrice;}

    @Override
    public String toString() {
        return this.getName();
    }
}
