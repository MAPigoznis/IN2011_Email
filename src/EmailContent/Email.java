package EmailContent;

public class Email {
    private Header header;
    private String content;

    public Email(Header header, String content) {
        this.header = header;
        this.content = content;
    }

    //getters, setters
    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return header.toString() + "\nContent: " + content;
    }
}

