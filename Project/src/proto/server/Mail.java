package proto.server;

public class Mail {

    private int id;
    private String address;
    private String body;

    public Mail() {
    }

    public Mail(int id, String address, String body) {
        this.id = id;
        this.address = address;
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
