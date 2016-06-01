package tecnoinf.proyecto.grupo4.usbusdroid3.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Kavesa on 01/06/16.
 */
public class JourneyShort {
    private Long id;
    private String name;
    private DayOfWeek day;
    private Date time;
    private Integer busNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(Integer busNumber) {
        this.busNumber = busNumber;
    }


    public JourneyShort(JSONObject object){
        try {
            id = object.getLong("id");
            name = object.getJSONObject("service").getString("name");
            day = DayOfWeek.valueOf(object.getJSONObject("service").getString("day"));
            //time = new SimpleDateFormat("HH:mm").parse(object.getJSONObject("service").getString("time"));
            time = new Date();
            time.setTime(Long.valueOf(object.getJSONObject("service").getString("time")));
            busNumber = object.getInt("busNumber");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static ArrayList<JourneyShort> fromJson(JSONArray jsonObjects) {
        ArrayList<JourneyShort> journeyList = new ArrayList<>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                journeyList.add(new JourneyShort(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return journeyList;
    }
}
