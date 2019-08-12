package ppla01.book_room;

/**
 * Created by isramela on 05/03/18.
 */

public class RoomDetail {
    private String roomName;
    private String roomLocation;
    private String roomAddress;
    private String roomCapacity;
    private String roomFacilities;
    private String roomPicture;

    public String getRoomPicture (){
        return roomPicture;
    }

    public void setRoomPicture(String roomPicture){
        this.roomPicture=roomPicture;
    }

    public String getRoomCapacity() {
        return roomCapacity;
    }

    public String getRoomAddress() {
        return roomAddress;
    }

    public void setRoomAddress(String roomAddress) {
        this.roomAddress = roomAddress;
    }

    public void setRoomCapacity(String roomCapacity) {
        this.roomCapacity = roomCapacity;
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
    public String getRoomFacilities(){
        return roomFacilities;
    }

    public void setRoomFacilities(String roomFacilities){
        this.roomFacilities=roomFacilities;
    }


}
