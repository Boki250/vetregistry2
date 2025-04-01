package tra4.bogdan.vetregistry2;

public class ServiceItem {
    private String service;
    private int serviceId;

    public ServiceItem(int serviceId, String service) {
        this.service = service;
        this.serviceId = serviceId;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public String toString() {
        return this.getService();
    }
}
