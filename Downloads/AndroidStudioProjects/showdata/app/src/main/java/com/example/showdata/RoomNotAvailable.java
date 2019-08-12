package ppla01.book_room;

/**
 * Created by isramela on 26/03/18.
 */

public class RoomNotAvailable {
    private String date;
    private String meeting;
    private String pic;
    private String owner;
    private String contact;

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getOwner() {
        return owner;
    }

    public String getContact() {
        return contact;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMeeting(String meeting) {
        this.meeting = meeting;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDate() {
        return date;
    }

    public String getMeeting() {
        return meeting;
    }

    public String getPic() {
        return pic;
    }


}
