package Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EmailStorage {

    //an email is stored as such
    //MessageID, From, To, Subject, Keywords, Text
    private HashMap<String, List<String>> emailMap;

    public EmailStorage() {
        emailMap = new HashMap<>();
    }

    public void addEmail(List<String> email) {
        String messageId = email.get(0);
        emailMap.put(messageId, email);
    }

    public List<String> getEmail(String messageId) {
        return emailMap.get(messageId);
    }

    public void deleteEmail(String messageId) {
        emailMap.remove(messageId);
    }

    public List<List<String>> getEmailsByFrom(String from) {
        List<List<String>> emails = new ArrayList<>();
        for (List<String> email : emailMap.values()) {
            if (email.get(1).equals(from)) {
                emails.add(email);
            }
        }
        return emails;
    }

    public List<List<String>> getEmailsByTo(String to) {
        List<List<String>> emails = new ArrayList<>();
        for (List<String> email : emailMap.values()) {
            if (email.get(2).equals(to)) {
                emails.add(email);
            }
        }
        return emails;
    }

    public List<List<String>> getEmailsBySubject(String subject) {
        List<List<String>> emails = new ArrayList<>();
        for (List<String> email : emailMap.values()) {
            if (email.get(3).equals(subject)) {
                emails.add(email);
            }
        }
        return emails;
    }

    public List<List<String>> getEmailsByKeyword(String keyword) {
        List<List<String>> emails = new ArrayList<>();
        for (List<String> email : emailMap.values()) {
            for (int i = 4; i < email.size(); i++) {
                if (email.get(i).equals(keyword)) {
                    emails.add(email);
                    break;
                }
            }
        }
        return emails;
    }

    public List<List<String>> getAllEmails() {
        return new ArrayList<>(emailMap.values());
    }
}
