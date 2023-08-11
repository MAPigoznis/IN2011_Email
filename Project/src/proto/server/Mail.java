package proto.server;

public class Mail {

    private String id;
    private String sender;
    private String recipient;
    private String subject;
    private String body;

    public Mail() {
    }

    public Mail(String id, String sender, String recipient, String subject, String body) {
        this.id = id;
        this.sender = sender;
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
    }

    public String generateEmail() {
        String line = "";
        line += id + "|" + sender + "|" + recipient + "|" + subject + "|" + body + "||";
        return line;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getRecipient() {
        return recipient;
    }
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
}
