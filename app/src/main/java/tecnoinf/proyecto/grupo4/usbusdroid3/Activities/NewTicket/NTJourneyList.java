package tecnoinf.proyecto.grupo4.usbusdroid3.Activities.NewTicket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tecnoinf.proyecto.grupo4.usbusdroid3.Models.BusStop;
import tecnoinf.proyecto.grupo4.usbusdroid3.Models.Journey;
import tecnoinf.proyecto.grupo4.usbusdroid3.Models.JourneyShort;
import tecnoinf.proyecto.grupo4.usbusdroid3.R;

public class NTJourneyList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ntjourneys_list);
        Intent father = getIntent();
        final String token = father.getStringExtra("token");

        try {
            JSONObject intentData = new JSONObject(father.getStringExtra("data"));
            JSONArray journeyJsonArray = new JSONArray(intentData.get("data").toString().replace("\\", ""));

            List<JourneyShort> journeyList = JourneyShort.fromJson(journeyJsonArray);
            System.out.println("======estoy, journeyList size: " + journeyList.size());
            //ArrayList<String> busStopsNames = new ArrayList<>();
            for (JourneyShort js: journeyList) {
                System.out.println("====Journey:");
                System.out.println(js.getName());
                System.out.println(js.getDay());
                System.out.println(js.getTime());
                System.out.println(js.getBusNumber());
            }

            ArrayAdapter<JourneyShort> itemsAdapter =
                    new ArrayAdapter<>(this, R.layout.activity_ntjourneys_list_item, journeyList);

            ListView journeysListView = (ListView) findViewById(R.id.journeysLV);
            assert journeysListView != null;
            journeysListView.setAdapter(itemsAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //TODO: agarrar el father intent
        //TODO: convertir el array json en array de objetos (journeys) (armar el conversor tal como el busstop)
        //TODO: desplegar el listview

        //List<JourneyShort> journeyList = new ArrayList<>();
        //TODO: cargar en este journeyList los items del array que vino en el intent
        //TODO: journey.id / busnumber / journey.service.name / day / time


    }
}
