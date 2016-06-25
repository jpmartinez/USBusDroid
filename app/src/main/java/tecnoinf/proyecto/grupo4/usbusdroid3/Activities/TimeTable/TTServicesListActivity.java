package tecnoinf.proyecto.grupo4.usbusdroid3.Activities.TimeTable;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import tecnoinf.proyecto.grupo4.usbusdroid3.Helpers.DayConverter_ES;
import tecnoinf.proyecto.grupo4.usbusdroid3.Models.JourneyShort;
import tecnoinf.proyecto.grupo4.usbusdroid3.Models.ServiceShort;
import tecnoinf.proyecto.grupo4.usbusdroid3.R;

public class TTServicesListActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ttservices_list);
        Intent father = getIntent();
        //final String token = father.getStringExtra("token");
        //SharedPreferences sharedPreferences = getSharedPreferences("USBusData", Context.MODE_PRIVATE);
        //final String token = sharedPreferences.getString("token", "");

        try {
            JSONObject intentData = new JSONObject(father.getStringExtra("data"));
            final JSONArray servicesJsonArray = new JSONArray(intentData.get("data").toString().replace("\\", ""));

            final List<ServiceShort> serviceList = ServiceShort.fromJson(servicesJsonArray);

            ArrayList<HashMap<String, String>> servicesMap = new ArrayList<>();

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            timeFormat.setTimeZone(TimeZone.getTimeZone("America/Montevideo"));

//            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//            dateFormat.setTimeZone(TimeZone.getTimeZone("America/Montevideo"));

            for (ServiceShort ss : serviceList) {
                HashMap<String, String> j = new HashMap<>();
                j.put("id", ss.getId().toString());
                j.put("name", ss.getName());
                j.put("day", DayConverter_ES.convertES(ss.getDay()));
                j.put("time", timeFormat.format(ss.getTime()));
                j.put("numberOfBuses", ss.getNumberOfBuses().toString());

                servicesMap.add(j);
            }

            ListAdapter adapter = new SimpleAdapter(
                    getApplicationContext(),
                    servicesMap,
                    R.layout.activity_ttservices_list_item,
                    new String[] { "id", "name", "day", "time", "numberOfBuses" },
                    new int[] { R.id.ttServiceId, R.id.ttServiceNameTV, R.id.ttServiceDayTV, R.id.ttServiceTimeTV, R.id.ttNumberOfBusesTV });

            setListAdapter(adapter);

            ListView lv = getListView();

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    //                        String journeyid = ((TextView) view.findViewById(R.id.id)).getText().toString();
//
//                        Intent selectSeat = new Intent(getBaseContext(), NTSelectSeatActivity.class);
//                        selectSeat.putExtra("journey", journeyJsonArray.get(position).toString());
//                        //selectSeat.putExtra("token", token);
//                        startActivity(selectSeat);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

