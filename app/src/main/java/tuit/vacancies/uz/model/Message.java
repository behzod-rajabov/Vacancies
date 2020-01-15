package tuit.vacancies.uz.model;

public class Message {
    private String message;
    private String room;
    private String user1;
    private String user2;
    private String date;

    public Message(String message, String room, String user1, String user2, String date) {
        this.message = message;
        this.room = room;
        this.user1 = user1;
        this.user2 = user2;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
