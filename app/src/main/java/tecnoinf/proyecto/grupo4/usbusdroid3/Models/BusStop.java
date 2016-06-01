package tecnoinf.proyecto.grupo4.usbusdroid3.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kavesa on 31/05/16.
 */
public class BusStop {
    private Long id;
    private String name;
    private Boolean active;
    private Double stopTime;

    public BusStop() {}

    public BusStop(Long id, String name, Boolean active, Double stopTime) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.stopTime = stopTime;
    }

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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Double getStopTime() {
        return stopTime;
    }

    public void setStopTime(Double stopTime) {
        this.stopTime = stopTime;
    }

    public BusStop(JSONObject object){
        try {
            id = object.getLong("id");
            name = object.getString("name");
            active = object.getBoolean("active");
            stopTime = object.getDouble("stopTime");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static ArrayList<BusStop> fromJson(JSONArray jsonObjects) {
        ArrayList<BusStop> busStops = new ArrayList<>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                busStops.add(new BusStop(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return busStops;
    }

}
