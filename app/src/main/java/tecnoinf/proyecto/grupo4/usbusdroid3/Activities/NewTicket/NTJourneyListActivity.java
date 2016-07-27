package tecnoinf.proyecto.grupo4.usbusdroid3.Activities.NewTicket;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import tecnoinf.proyecto.grupo4.usbusdroid3.Helpers.DayConverter_ES;
import tecnoinf.proyecto.grupo4.usbusdroid3.Helpers.RestCallAsync;
import tecnoinf.proyecto.grupo4.usbusdroid3.Models.JourneyShort;
import tecnoinf.proyecto.grupo4.usbusdroid3.R;

public class NTJourneyListActivity extends ListActivity {

    private JSONArray journeyJsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ntjourneys_list);
        Intent father = getIntent();
        //final String token = father.getStringExtra("token");
        SharedPreferences sharedPreferences = getSharedPreferences("USBusData", Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("token", "");

        try {
            JSONObject intentData = new JSONObject(father.getStringExtra("data"));
            journeyJsonArray = new JSONArray(intentData.get("data").toString().replace("\\", ""));
            System.out.println("journeyJsonArray: " + journeyJsonArray);

            final List<JourneyShort> journeyList = JourneyShort.fromJson(journeyJsonArray);

            ArrayList<HashMap<String, String>> journeyMap = new ArrayList<>();

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            timeFormat.setTimeZone(TimeZone.getTimeZone("America/Montevideo"));

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dateFormat.setTimeZone(TimeZone.getTimeZone("America/Montevideo"));

            for (JourneyShort js2 : journeyList) {
                HashMap<String, String> j = new HashMap<>();
                j.put("id", js2.getId().toString());
                j.put("name", js2.getName());
                j.put("day", DayConverter_ES.convertES(js2.getDay()));
                j.put("date", dateFormat.format(js2.getDate()));
                j.put("time", timeFormat.format(js2.getTime()));
                j.put("busNumber", js2.getBusNumber().toString());

                journeyMap.add(j);
            }

            ListAdapter adapter = new SimpleAdapter(
                    getApplicationContext(),
                    journeyMap,
                    R.layout.activity_ntjourneys_list_item,
                    new String[] { "id", "name", "day", "date", "time", "busNumber" },
                    new int[] { R.id.id, R.id.journeyNameTV, R.id.journeyDayTV, R.id.journeyDateTV, R.id.journeyTimeTV, R.id.busNumberTV });
            setListAdapter(adapter);

            ListView lv = getListView();
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    try {
                        String journeyid = ((TextView) view.findViewById(R.id.id)).getText().toString();
                        String bookingsURL = getString(R.string.URLjourneyBookings,
                                getString(R.string.URL_REST_API),
                                getString(R.string.tenantId),
                                journeyid,
                                true);
                        AsyncTask<Void, Void, JSONObject> bookingsResult = new RestCallAsync(getApplicationContext(), bookingsURL, "GET", null, token).execute();
                        JSONObject bookingsData = bookingsResult.get();

                        Intent selectSeat = new Intent(getBaseContext(), NTSelectSeatActivity.class);
                        selectSeat.putExtra("journey", journeyJsonArray.get(position).toString());
                        selectSeat.putExtra("bookings", bookingsData.getString("data"));
                        startActivity(selectSeat);

                    } catch (JSONException | ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
