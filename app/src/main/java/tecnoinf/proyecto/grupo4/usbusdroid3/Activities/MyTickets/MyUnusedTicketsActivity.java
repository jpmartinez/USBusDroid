package tecnoinf.proyecto.grupo4.usbusdroid3.Activities.MyTickets;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tecnoinf.proyecto.grupo4.usbusdroid3.Models.TicketShort;
import tecnoinf.proyecto.grupo4.usbusdroid3.R;

public class MyUnusedTicketsActivity extends ListActivity {

    private static String token;
    private static JSONArray unusedTicketsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_unused_tickets);
        Intent father = getIntent();
        SharedPreferences sharedPreferences = getSharedPreferences("USBusData", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        //token = father.getStringExtra("token");
        try {
            unusedTicketsArray = new JSONArray(father.getStringExtra("unusedTickets").replace("\\", ""));
            //TODO: probar con array vacío y en caso de explotar arreglar
            //TODO: y en caso que no explote, mostrar un toast con el mensaje, en lugar de llamar al ShowTicket

            final List<TicketShort> ticketsList = TicketShort.fromJson(unusedTicketsArray);
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
                    try {
                        //String ticketid = ((TextView) view.findViewById(R.id.id)).getText().toString();
                        Intent showTicketIntent = new Intent(getBaseContext(), MTShowTicketActivity.class);
                        showTicketIntent.putExtra("journey", unusedTicketsArray.get(position).toString());
                        //showTicketIntent.putExtra("token", token);
                        startActivity(showTicketIntent);
                        //TODO: Hace falta llamar al ShowTicket y mostrar QR? De qué sirve?
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
