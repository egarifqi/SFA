package bookingcar.myapplication;

/**
 * Created by isramela on 22/06/18.
 */

public class BookingCar {
    private String id;
    private String image;
    private String location;
    private String used_for;
    private String pickup_time;
    private String license_plate;
    private String type;

    public String getUsed_for() {
        return used_for;
    }

    public void setUsed_for(String used_for) {
        this.used_for = used_for;
    }

    public String getPickup_time() {
        return pickup_time;
    }

    public void setPickup_time(String pickup_time) {
        this.pickup_time = pickup_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLicense_plate() {
        return license_plate;
    }

    public void setLicense_plate(String license_plate) {
        this.license_plate = license_plate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
