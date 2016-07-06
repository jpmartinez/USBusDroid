package tecnoinf.proyecto.grupo4.usbusdroid3.Activities.MyTickets;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import tecnoinf.proyecto.grupo4.usbusdroid3.Helpers.RestCallAsync;
import tecnoinf.proyecto.grupo4.usbusdroid3.R;

public class MyTicketsActivity extends AppCompatActivity {

    private static String myUnusedTicketsURL;
    private static String myUsedTicketsURL;
    private static String token;
    private static String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tickets);
        Intent father = getIntent();
        SharedPreferences sharedPreferences = getSharedPreferences("USBusData", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        //token = father.getStringExtra("token");
        username = father.getStringExtra("username");
        myUsedTicketsURL = getString(R.string.URLmyTickets,
                getString(R.string.URL_REST_API),
                getString(R.string.tenantId),
                username,
                "USED");
        myUnusedTicketsURL = getString(R.string.URLmyTickets,
                getString(R.string.URL_REST_API),
                getString(R.string.tenantId),
                username,
                "CONFIRMED");

        ImageButton usedTicketsBtn = (ImageButton) findViewById(R.id.usedBtn);
        ImageButton unusedTicketsBtn = (ImageButton) findViewById(R.id.unusedBtn);

        assert usedTicketsBtn != null;
        usedTicketsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestCallAsync call = new RestCallAsync(getApplicationContext(), myUsedTicketsURL, "GET", null, token);
                call.execute((Void) null);

                try {
                    JSONObject usedTicketsRestData = call.get();
                    JSONArray usedTickets = new JSONArray(usedTicketsRestData.get("data").toString().replace("\\", ""));

                    if(usedTickets.length() > 0) {
                        Intent usedTicketsIntent = new Intent(getBaseContext(), MyUsedTicketsActivity.class);
                        usedTicketsIntent.putExtra("usedTickets", usedTickets.toString());
                        startActivity(usedTicketsIntent);
                    } else {
                        Toast.makeText(getBaseContext(), "No tiene tickets utilizados", Toast.LENGTH_LONG).show();
                    }

                } catch (InterruptedException | ExecutionException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        assert unusedTicketsBtn != null;
        unusedTicketsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestCallAsync call = new RestCallAsync(getApplicationContext(), myUnusedTicketsURL, "GET", null, token);
                call.execute((Void) null);

                try {
                    JSONObject unusedTicketsRestData = call.get();
                    JSONArray unusedTickets = new JSONArray(unusedTicketsRestData.get("data").toString().replace("\\", ""));

                    if(unusedTickets.length() > 0) {
                        Intent unusedTicketsIntent = new Intent(getBaseContext(), MyUnusedTicketsActivity.class);
                        unusedTicketsIntent.putExtra("unusedTickets", unusedTickets.toString());
                        startActivity(unusedTicketsIntent);
                    } else {
                        Toast.makeText(getBaseContext(), "No tiene tickets sin utilizar", Toast.LENGTH_LONG).show();
                    }

                } catch (InterruptedException | ExecutionException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }
}


