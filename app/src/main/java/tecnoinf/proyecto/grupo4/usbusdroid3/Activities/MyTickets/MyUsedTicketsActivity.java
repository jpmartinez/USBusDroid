package tecnoinf.proyecto.grupo4.usbusdroid3.Activities.MyTickets;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tecnoinf.proyecto.grupo4.usbusdroid3.Activities.NewTicket.NTSelectSeatActivity;
import tecnoinf.proyecto.grupo4.usbusdroid3.Models.JourneyShort;
import tecnoinf.proyecto.grupo4.usbusdroid3.Models.TicketShort;
import tecnoinf.proyecto.grupo4.usbusdroid3.R;

public class MyUsedTicketsActivity extends ListActivity {

    private static String token;
    private static JSONArray usedTicketsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_used_tickets);
        Intent father = getIntent();
        token = father.getStringExtra("token");
        try {
            usedTicketsArray = new JSONArray(father.getStringExtra("usedTickets").replace("\\", ""));

            final List<TicketShort> ticketsList = TicketShort.fromJson(usedTicketsArray);
            ArrayList<HashMap<String, String>> ticketsMap = new ArrayList<>();

            for (TicketShort ts2 : ticketsList) {
                HashMap<String, String> t = new HashMap<>();
                t.put("id", ts2.getId().toString());
                t.put("emissiondate", ts2.getEmissionDate().toString());
                t.put("amount", ts2.getAmount().toString());
                t.put("status", ts2.getStatus().toString());

                ticketsMap.add(t);
            }

            ListAdapter adapter = new SimpleAdapter(
                    getApplicationContext(),
                    ticketsMap,
                    R.layout.activity_mytickets_list_item,
                    new String[] { "id",
                            "emissiondate",
                            "amount",
                            "status",
                            "journeyName",
                            "journeyDay",
                            "journeyDate",
                            "journeyTime",
                            "busNumber" },
                    new int[] { R.id.mtItemid,
                            R.id.mtJourneyNameTV,
                            R.id.mtJourneyDayTV,
                            R.id.mtJourneyDateTV,
                            R.id.mtJourneyTimeTV,
                            R.id.mtBusNumberTV });

            setListAdapter(adapter);
            ListView lv = getListView();
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    //TODO: analizar necesidad de una nueva activity NTShowTicket, visto que en la lista ya veo todos los datos
                    //TODO: posiblemente cambiar a onClick(mostrar toast o similar para enviar info por email)
                    try {
                        String ticketid = ((TextView) view.findViewById(R.id.id)).getText().toString();

                        Intent showTicket = new Intent(getBaseContext(), MTShowTicketActivity.class);
                        showTicket.putExtra("journey", usedTicketsArray.get(Integer.valueOf(ticketid)).toString());
                        showTicket.putExtra("token", token);
                        startActivity(showTicket);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

    }
}
