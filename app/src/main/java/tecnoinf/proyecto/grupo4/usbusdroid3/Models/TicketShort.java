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
public class TicketShort {
    private Long id;
    private Date emissionDate;
    private Double amount;
    private TicketStatus status;
    private String journeyName;
    private DayOfWeek journeyDay;
    private Date journeyTime;
    private Date journeyDate;
    private Integer busNumber;
    private Integer seat;

    public Integer getSeat() {
        return seat;
    }

    public void setSeat(Integer seat) {
        this.seat = seat;
    }

    public String getJourneyName() {
        return journeyName;
    }

    public void setJourneyName(String journeyName) {
        this.journeyName = journeyName;
    }

    public DayOfWeek getJourneyDay() {
        return journeyDay;
    }

    public void setJourneyDay(DayOfWeek journeyDay) {
        this.journeyDay = journeyDay;
    }

    public Date getJourneyTime() {
        return journeyTime;
    }

    public void setJourneyTime(Date journeyTime) {
        this.journeyTime = journeyTime;
    }

    public Date getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(Date journeyDate) {
        this.journeyDate = journeyDate;
    }

    public Integer getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(Integer busNumber) {
        this.busNumber = busNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getEmissionDate() {
        return emissionDate;
    }

    public void setEmissionDate(Date emissionDate) {
        this.emissionDate = emissionDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public TicketShort(JSONObject object) throws ParseException {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            id = object.getLong("id");
            amount = object.getDouble("amount");
            //emissionDate = dateFormat.parse(object.get("emissionDate").toString());
            status = TicketStatus.valueOf(object.get("status").toString());
            journeyName = object.getJSONObject("journey").getJSONObject("service").getString("name");
            journeyDay = DayOfWeek.valueOf(object.getJSONObject("journey").getJSONObject("service").getString("day"));
            journeyTime = new Date();
            journeyTime.setTime(Long.valueOf(object.getJSONObject("journey").getJSONObject("service").getString("time")));
            journeyDate = new Date();
            journeyDate.setTime(Long.valueOf(object.getJSONObject("journey").getString("date")));
            busNumber = object.getJSONObject("journey").getInt("busNumber");
            seat = object.getInt("seat");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<TicketShort> fromJson(JSONArray jsonObjects) throws ParseException {
        ArrayList<TicketShort> ticketList = new ArrayList<>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                ticketList.add(new TicketShort(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ticketList;
    }
}
