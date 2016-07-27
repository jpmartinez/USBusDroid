package tecnoinf.proyecto.grupo4.usbusdroid3.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Kavesa on 15/06/16.
 */
public class BookingShort {

    private Long id;
    private Date dueDate;
    private Long journeyId;
    private Integer seat;
    private String getsOn;
    private String getsOff;
    private String serviceName;

    public String getGetsOn() {
        return getsOn;
    }

    public void setGetsOn(String getsOn) {
        this.getsOn = getsOn;
    }

    public String getGetsOff() {
        return getsOff;
    }

    public void setGetsOff(String getsOff) {
        this.getsOff = getsOff;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Long getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(Long journeyId) {
        this.journeyId = journeyId;
    }

    public Integer getSeat() {
        return seat;
    }

    public void setSeat(Integer seat) {
        this.seat = seat;
    }

    public BookingShort(JSONObject object) throws ParseException {
        try {
            id = object.getLong("id");
            dueDate = new Date();
            dueDate.setTime(Long.valueOf(object.getString("dueDate")));
            seat = object.getInt("seat");
            journeyId = object.getLong("journeyId");
            getsOff = object.getString("getsOff");
            getsOn = object.getString("getsOn");
            serviceName = object.getString("serviceName");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<BookingShort> fromJson(JSONArray jsonObjects) throws ParseException {
        ArrayList<BookingShort> bookingsList = new ArrayList<>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                bookingsList.add(new BookingShort(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return bookingsList;
    }
}
