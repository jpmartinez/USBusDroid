package tecnoinf.proyecto.grupo4.usbusdroid3.Activities.NewTicket;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import tecnoinf.proyecto.grupo4.usbusdroid3.Activities.MainClient;
import tecnoinf.proyecto.grupo4.usbusdroid3.Helpers.RestCallAsync;
import tecnoinf.proyecto.grupo4.usbusdroid3.R;

public class NTResultActivity extends AppCompatActivity {

    private String token;
    private String buyTicketRest;
    private JSONObject newTicket;
    private JSONObject journey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ntresult);

        Intent father = getIntent();
        //token = father.getStringExtra("token");
        SharedPreferences sharedPreferences = getSharedPreferences("USBusData", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        try {
            journey = new JSONObject(father.getStringExtra("journey"));
            JSONObject jsonDetails = new JSONObject(father.getStringExtra("PaymentDetails"));
            newTicket = new JSONObject();
            newTicket.put("journeyId", journey.get("id").toString());
            newTicket.put("paymentAmount", father.getStringExtra("paymentAmount"));
            //TODO: agregar el resto que pida el rest

            AsyncTask<Void, Void, JSONObject> priceResult = new RestCallAsync(getApplicationContext(), buyTicketRest, "POST", newTicket, token).execute();
            JSONObject priceData = priceResult.get();
            //Displaying payment details
            showDetails(jsonDetails.getJSONObject("response"), father.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void showDetails(JSONObject jsonDetails, String paymentAmount) throws JSONException {
        //Views
        TextView textViewId = (TextView) findViewById(R.id.paymentId);
        TextView textViewStatus= (TextView) findViewById(R.id.paymentStatus);
        TextView textViewAmount = (TextView) findViewById(R.id.paymentAmount);

        //Showing the details from json object
        textViewId.setText(jsonDetails.getString("id"));
        textViewStatus.setText(jsonDetails.getString("state"));
        textViewAmount.setText("USD " + paymentAmount);
    }

    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(this, MainClient.class);
        //homeIntent.putExtra("token", token);
        startActivity(homeIntent);
    }
}
