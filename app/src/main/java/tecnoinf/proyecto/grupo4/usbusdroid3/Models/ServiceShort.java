package tecnoinf.proyecto.grupo4.usbusdroid3.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Kavesa on 31/05/16.
 */
public class ServiceShort {
    private Long id;
    private String name;
    private DayOfWeek day;
    private Date time;
    private Integer numberOfBuses;
    private Boolean active;

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

    public Integer getNumberOfBuses() {
        return numberOfBuses;
    }

    public void setNumberOfBuses(Integer numberOfBuses) {
        this.numberOfBuses = numberOfBuses;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ServiceShort(JSONObject object) throws ParseException {
        try {
            id = object.getLong("id");
            name = object.getString("name");
            day = DayOfWeek.valueOf(object.getString("day"));
            time = new Date();
            time.setTime(Long.valueOf(object.getString("time")));
            numberOfBuses = object.getInt("numberOfBuses");
            active = object.getBoolean("active");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static ArrayList<ServiceShort> fromJson(JSONArray jsonObjects) throws ParseException {
        ArrayList<ServiceShort> serviceList = new ArrayList<>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                serviceList.add(new ServiceShort(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return serviceList;
    }
}
