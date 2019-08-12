package ppla01.book_room;

/**
 * Created by isramela on 24/03/18.
 */

public class DetailMeeting {
    private String roomName;
    private String roomLocation;
    private String roomAddress;
    private String idRoom;
    private String meetingSubject;
    private String meetingDate;
    private String meetingStart;
    private String meetingEnd;
    private String roomPicture;

    public String getRoomPicture(){
        return  roomPicture;
    }

    public void setRoomPicture(String roomPicture){
        this.roomPicture=roomPicture;
    }

    public void setMeetingSubject(String meetingSubject) {
        this.meetingSubject = meetingSubject;
    }

    public void setMeetingDate(String meetingDate) {
        this.meetingDate = meetingDate;
    }

    public void setMeetingStart(String meetingStart) {
        this.meetingStart = meetingStart;
    }

    public void setMeetingEnd(String meetingEnd) {
        this.meetingEnd = meetingEnd;
    }

    public String getMeetingSubject() {
        return meetingSubject;
    }

    public String getMeetingDate() {
        return meetingDate;
    }

    public String getMeetingStart() {
        return meetingStart;
    }

    public String getMeetingEnd() {
        return meetingEnd;
    }

    public String getIdRoom(){
        return idRoom;
    }

    public void setIdRoom(String idRoom){
        this.idRoom=idRoom;
    }

    public String getRoomAddress() {
        return roomAddress;
    }

    public void setRoomAddress(String roomAddress) {
        this.roomAddress = roomAddress;
    }

    public String getRoomName (){
        return roomName;
    }

    public void setRoomName(String roomName){
        this.roomName=roomName;
    }

    public String getRoomLocation() {
        return roomLocation;
    }
    public void setRoomLocation(String cal) {
        this.roomLocation =cal;

    }
}



