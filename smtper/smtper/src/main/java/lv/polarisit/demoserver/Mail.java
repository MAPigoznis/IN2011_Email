package lv.polarisit.demoserver;

public class Mail {

    private String address;
    private String body;

    public Mail() {
    }

    public Mail(String address, String body) {
        this.address = address;
        this.body = body;
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

