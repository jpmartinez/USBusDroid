package tecnoinf.proyecto.grupo4.usbusdroid3.Activities.MyTickets;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import tecnoinf.proyecto.grupo4.usbusdroid3.Activities.MainClient;
import tecnoinf.proyecto.grupo4.usbusdroid3.Helpers.RestCallAsync;
import tecnoinf.proyecto.grupo4.usbusdroid3.Models.TicketStatus;
import tecnoinf.proyecto.grupo4.usbusdroid3.R;

public class MTBuyBookingResultActivity extends AppCompatActivity {

    private String token;
    private String updateTicketRest;
    private String username;
    private JSONObject tempTicket;
    private JSONObject updatedTicket;
    private JSONObject journey;
    private Button homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ntresult);


        Intent father = getIntent();
        //token = father.getStringExtra("token");
        SharedPreferences sharedPreferences = getSharedPreferences("USBusData", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        token = sharedPreferences.getString("token", "");

        try {
            tempTicket = new JSONObject(father.getStringExtra("ticket"));

            JSONObject paymentDetails = new JSONObject(father.getStringExtra("PaymentDetails"));

            System.out.println("8181818181818 paymentDetails: "+paymentDetails);

            //TODO: if response.state == approved
            updatedTicket = new JSONObject();
            updatedTicket.put("tenantId", getString(R.string.tenantId));
            updatedTicket.put("id", tempTicket.get("id"));
            updatedTicket.put("paymentToken", paymentDetails.getJSONObject("response").get("id"));
            updatedTicket.put("username", username);
            updatedTicket.put("status", TicketStatus.CONFIRMED);

            updateTicketRest = getString(R.string.URLTickets, getString(R.string.URL_REST_API), getString(R.string.tenantId)) + "/" + tempTicket.get("id").toString();
            AsyncTask<Void, Void, JSONObject> updTicketResult = new RestCallAsync(getApplicationContext(), updateTicketRest, "PUT", updatedTicket, token).execute();
            JSONObject updTicketData = updTicketResult.get();

            String updateBookingRest = getString(R.string.URLdeleteBooking, getString(R.string.URL_REST_API), getString(R.string.tenantId), father.getStringExtra("bookingId"));
            AsyncTask<Void, Void, JSONObject> updBookingResult = new RestCallAsync(getApplicationContext(), updateBookingRest, "DELETE", null, token).execute();
            JSONObject updBookingData = updBookingResult.get();

            //Displaying payment details
            showDetails(paymentDetails.getJSONObject("response"), father.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        homeButton = (Button) findViewById(R.id.ntresult_homeBtn);
        assert homeButton != null;
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(getApplicationContext(), MainClient.class);
                startActivity(homeIntent);
            }
        });
    }

    private void showDetails(JSONObject jsonDetails, String paymentAmount) throws JSONException {
        //Views
        TextView textViewId = (TextView) findViewById(R.id.paymentId);
        TextView textViewStatus= (TextView) findViewById(R.id.paymentStatus);
        TextView textViewAmount = (TextView) findViewById(R.id.paymentAmount);

        //Showing the details from json object
        textViewId.setText(jsonDetails.getString("id"));
        textViewStatus.setText(jsonDetails.getString("state"));
        textViewAmount.setText("$ " + paymentAmount);
    }

    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(this, MainClient.class);
        //homeIntent.putExtra("token", token);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
    }
}
