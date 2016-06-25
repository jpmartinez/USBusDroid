package tecnoinf.proyecto.grupo4.usbusdroid3.Models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Kavesa on 15/06/16.
 */
public class TicketPut {
    private long tenantId;
    private long id;
    private String paymentToken;
    private String username;
    private TicketStatus status;

    public long getTenantId() {
        return tenantId;
    }

    public void setTenantId(long tenantId) {
        this.tenantId = tenantId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPaymentToken() {
        return paymentToken;
    }

    public void setPaymentToken(String paymentToken) {
        this.paymentToken = paymentToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public TicketPut(long tenantId, long id, String paymentToken, String username, TicketStatus status) {
        this.tenantId = tenantId;
        this.id = id;
        this.paymentToken = paymentToken;
        this.username = username;
        this.status = status;
    }

    public JSONObject toJsonObject(TicketPut ticket) throws JSONException {
        JSONObject result = new JSONObject();
        result.put("tenantId", ticket.getTenantId());
        result.put("id", ticket.getTenantId());
        result.put("paymentToken", ticket.getPaymentToken());
        result.put("username", ticket.getUsername());
        result.put("status", ticket.getStatus());

        return result;
    }
}
