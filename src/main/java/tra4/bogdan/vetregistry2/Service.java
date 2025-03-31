package tra4.bogdan.vetregistry2;

public class Service {
    private String serviceName;
    private int serviceId;
    private int serviceCost;

    public Service(int serviceId, String serviceName, int serviceCost) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceCost = serviceCost;
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

    public int getCost() { return serviceCost;}

    public void setCost(int serviceCost) { this.serviceCost = serviceCost;}

    @Override
    public String toString() {
        return this.getName();
    }
}
