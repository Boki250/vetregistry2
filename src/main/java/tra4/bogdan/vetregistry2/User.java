package tra4.bogdan.vetregistry2;

public class User {
    private int id;
    private String username;
    private String password;
    private String status; // Status title (e.g., "skrbnik")

    public User(int id, String username, String password, String status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getStatus() {
        return status;
    }
}