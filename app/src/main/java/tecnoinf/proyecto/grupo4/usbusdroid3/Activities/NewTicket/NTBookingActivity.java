package tecnoinf.proyecto.grupo4.usbusdroid3.Activities.NewTicket;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutionException;

import tecnoinf.proyecto.grupo4.usbusdroid3.Activities.MainClient;
import tecnoinf.proyecto.grupo4.usbusdroid3.Helpers.RestCallAsync;
import tecnoinf.proyecto.grupo4.usbusdroid3.Models.TicketStatus;
import tecnoinf.proyecto.grupo4.usbusdroid3.R;

public class NTBookingActivity extends AppCompatActivity {

    private Intent father;
    private String paymentAmount;
    private String token;
    private String username;
    private JSONObject journey;
    private JSONObject newBooking;
    private String bookTicketRest;
    private Button confirmBookingBtn;
    private JSONObject bookingData;
    private String tenantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ntbooking);

        father = getIntent();
        try {
            journey = new JSONObject(father.getStringExtra("journey"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SharedPreferences sharedPreferences = getSharedPreferences("USBusData", Context.MODE_PRIVATE);
        bookTicketRest = getString(R.string.URLbookTicket, getString(R.string.URL_REST_API), getString(R.string.tenantId));
        token = sharedPreferences.getString("token", "");
        username = sharedPreferences.getString("username", "");
        tenantId = sharedPreferences.getString("tenantId", "");

        String selectedSeat = String.valueOf(father.getIntExtra("seat", 0));
        paymentAmount = father.getStringExtra("ticketPrice");
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            journey = new JSONObject(father.getStringExtra("journey"));

            TextView ticketOriginTV = (TextView) findViewById(R.id.bkticketOriginTV);
            TextView ticketDestinationTV = (TextView) findViewById(R.id.bkticketDestinationTV);
            TextView ticketDateTV = (TextView) findViewById(R.id.bkticketDateTV);
            TextView ticketBusIdTV = (TextView) findViewById(R.id.bkticketBusIdTV);
            TextView ticketSeatTV = (TextView) findViewById(R.id.bkticketSeatTV);
            TextView ticketCostTV = (TextView) findViewById(R.id.bkticketCostTV);
            assert ticketCostTV != null;
            ticketCostTV.setText(paymentAmount);
            assert ticketSeatTV != null;
            ticketSeatTV.setText(selectedSeat);
            assert ticketOriginTV != null;
            ticketOriginTV.setText(father.getStringExtra("origin"));
            assert ticketDestinationTV != null;
            ticketDestinationTV.setText(father.getStringExtra("destination"));
            assert ticketDateTV != null;
            ticketDateTV.setText(dateFormat.format(journey.get("date")));
            assert ticketBusIdTV != null;
            ticketBusIdTV.setText(journey.get("busNumber").toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        confirmBookingBtn = (Button) findViewById(R.id.confirmBookingBtn);
        assert confirmBookingBtn != null;
        confirmBookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newBooking = new JSONObject();
                try {
                    newBooking.put("tenantId", tenantId);
                    newBooking.put("journeyId", journey.get("id"));
                    newBooking.put("clientId", username);
                    newBooking.put("seat", String.valueOf(father.getIntExtra("seat", 0)));
                    newBooking.put("active", true);
                    newBooking.put("getsOn", father.getStringExtra("origin"));
                    newBooking.put("getsOff", father.getStringExtra("destination"));
                    newBooking.put("dueDate", Long.valueOf(journey.getString("date")) - 60000*30);
                    newBooking.put("serviceName", journey.getJSONObject("service").getString("name"));

                    AsyncTask<Void, Void, JSONObject> bookingResult = new RestCallAsync(getApplicationContext(), bookTicketRest, "POST", newBooking, token).execute();
                    bookingData =  bookingResult.get();

                    if (bookingData.getString("result").equalsIgnoreCase("OK")) {
                        Toast.makeText(getApplicationContext(), "Reserva creada correctamente", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Ocurrió un error al crear la reserva", Toast.LENGTH_LONG).show();
                    }

                    Intent mainIntent = new Intent(getBaseContext(), MainClient.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);

                } catch (JSONException | InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
