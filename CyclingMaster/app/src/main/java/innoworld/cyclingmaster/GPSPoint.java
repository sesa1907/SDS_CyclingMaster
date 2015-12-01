package innoworld.cyclingmaster;

import java.util.Date;

/**
 * Created by RENZE-ACER on 16-11-2015.
 */
public class GPSPoint {
    private double lattitude;
    private double longitude;
    private double elevation;
    private Date date;

    GPSPoint(Double lattitude, Double longitude, Double elevation, Date date) {
        setLattitude(lattitude);
        setLongitude(longitude);
        setElevation(elevation);
        setDate(date);
    }

    private void setLattitude(Double lattitude) {
        this.lattitude = lattitude;
    }

    private void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    private void setElevation(Double elevation) {
        this.elevation = elevation;
    }

    private void setDate(Date date) {
        this.date = date;
    }

    public Double getLattitude() {
        return this.lattitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public Double getElevation() {
        return this.elevation;
    }

    public Date getDate() {
        return this.date;
    }
}
