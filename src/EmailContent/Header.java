package EmailContent;

import java.util.*;

public class Header {
    private String from;
    private List<String> to;
    private String subject;
    private Date date;
    private String messageId;
    private List<String> keywords;
    //TODO double triple quadruple check what headers should i use

    public Header(String from, List<String> to, String subject, Date date, String messageId, List<String> keywords) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.date = date;
        this.messageId = messageId;
        this.keywords = keywords;
    }

    //getters, setters
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public String toString() {
        return "From: " + from +
                "\nTo: " + String.join(", ", to) +
                "\nSubject: " + subject +
                "\nDate: " + date +
                "\nMessage-ID: " + messageId +
                "\nKeywords: " + String.join(", ", keywords);
    }
}