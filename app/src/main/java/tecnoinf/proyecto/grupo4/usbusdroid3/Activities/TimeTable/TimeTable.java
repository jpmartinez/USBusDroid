package tecnoinf.proyecto.grupo4.usbusdroid3.Activities.TimeTable;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import tecnoinf.proyecto.grupo4.usbusdroid3.Activities.NewTicket.NTJourneyListActivity;
import tecnoinf.proyecto.grupo4.usbusdroid3.Helpers.RestCallAsync;
import tecnoinf.proyecto.grupo4.usbusdroid3.Models.BusStop;
import tecnoinf.proyecto.grupo4.usbusdroid3.R;

public class TimeTable extends AppCompatActivity {

    private static final String servicesFromToRest = "http://10.0.2.2:8080/usbus/api/1/test/journeys";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        Intent father = getIntent();
        final String token = father.getStringExtra("token");

        try {
            JSONObject intentData = new JSONObject(father.getStringExtra("data"));
            JSONArray busStops = new JSONArray(intentData.get("data").toString().replace("\\", ""));

            List<BusStop> busStopList = BusStop.fromJson(busStops);
            ArrayList<String> busStopsNames = new ArrayList<>();
            for (BusStop bs: busStopList) {
                busStopsNames.add(bs.getName());
            }

            final Spinner spinnerFrom = (Spinner) findViewById(R.id.spnFrom);
            ArrayAdapter<String> fromAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, busStopsNames);
            assert spinnerFrom != null;
            spinnerFrom.setAdapter(fromAdapter);

            final Spinner spinnerTo = (Spinner) findViewById(R.id.spnTo);
            ArrayAdapter<String> toAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, busStopsNames);
            assert spinnerTo != null;
            spinnerTo.setAdapter(toAdapter);

            Button submitBtn = (Button) findViewById(R.id.btnSearch);
            assert submitBtn != null;
            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        JSONObject postData = new JSONObject();
                        postData.put("token", token);
                        String origin = spinnerFrom.getSelectedItem().toString();
                        String destination = spinnerTo.getSelectedItem().toString();
                        postData.put("origin", origin);
                        postData.put("destination", destination);

                        AsyncTask<Void, Void, JSONObject> servicesResult = new RestCallAsync(servicesFromToRest, "POST", postData).execute();
                        JSONObject servicesData = servicesResult.get();

                        Intent listServicesFromToIntent = new Intent(v.getContext(), TTServicesListActivity.class);
                        listServicesFromToIntent.putExtra("token", token);
                        listServicesFromToIntent.putExtra("data", servicesData.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
