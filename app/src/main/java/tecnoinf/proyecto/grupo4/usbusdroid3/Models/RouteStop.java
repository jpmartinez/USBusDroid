package tecnoinf.proyecto.grupo4.usbusdroid3.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kavesa on 31/05/16.
 */
public class RouteStop {
    private String busStop;
    private Double km;
    private boolean isCombinationPoint;

    public String getBusStop() {
        return busStop;
    }

    public void setBusStop(String busStop) {
        this.busStop = busStop;
    }

    public Double getKm() {
        return km;
    }

    public void setKm(Double km) {
        this.km = km;
    }

    public boolean isCombinationPoint() {
        return isCombinationPoint;
    }

    public void setCombinationPoint(boolean combinationPoint) {
        isCombinationPoint = combinationPoint;
    }

    public RouteStop(JSONObject object){
        try {
            busStop = object.getString("busStop");
            km = object.getDouble("km");
            isCombinationPoint = object.getBoolean("combinationPoint");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static ArrayList<RouteStop> fromJson(JSONArray jsonObjects) {
        ArrayList<RouteStop> routeStops = new ArrayList<>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                routeStops.add(new RouteStop(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return routeStops;
    }
}
