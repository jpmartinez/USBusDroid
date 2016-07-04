package tecnoinf.proyecto.grupo4.usbusdroid3.Activities.MyTickets;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        SharedPreferences sharedPreferences = getSharedPreferences("USBusData", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        //token = father.getStringExtra("token");
        try {
            usedTicketsArray = new JSONArray(father.getStringExtra("usedTickets").replace("\\", ""));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
            //TODO: probar con array vacío y en caso de explotar arreglar
            //TODO: y en caso que no explote, mostrar un toast con el mensaje, en lugar de llamar al ShowTicket

            final List<TicketShort> ticketsList = TicketShort.fromJson(usedTicketsArray);
            ArrayList<HashMap<String, String>> ticketsMap = new ArrayList<>();

            for (TicketShort ts2 : ticketsList) {
                HashMap<String, String> t = new HashMap<>();
                t.put("id", ts2.getId().toString());
                t.put("amount", ts2.getAmount().toString());
                t.put("status", ts2.getStatus().toString());
                t.put("journeyName", ts2.getJourneyName());
                t.put("journeyDate", dateFormat.format(ts2.getJourneyDate()));
                t.put("journeyTime", timeFormat.format(ts2.getJourneyTime()));
                t.put("busNumber", ts2.getBusNumber().toString());
                t.put("seat", ts2.getSeat().toString());

                ticketsMap.add(t);
            }

            ListAdapter adapter = new SimpleAdapter(
                    getApplicationContext(),
                    ticketsMap,
                    R.layout.activity_mytickets_list_used_item,
                    new String[] { "id",
                            "amount",
                            "status",
                            "journeyName",
                            "journeyDate",
                            "journeyTime",
                            "busNumber",
                            "seat"},
                    new int[] { R.id.mtItemid,
                            R.id.mtAmountTV,
                            R.id.mtStatusTV,
                            R.id.mtJourneyNameTV,
                            R.id.mtJourneyDateTV,
                            R.id.mtJourneyTimeTV,
                            R.id.mtBusNumberTV,
                            R.id.mtSeatTV});

            setListAdapter(adapter);
            ListView lv = getListView();
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    try {
                        //String ticketid = ((TextView) view.findViewById(R.id.id)).getText().toString();
                        Intent showTicketIntent = new Intent(getBaseContext(), MTShowTicketActivity.class);
                        showTicketIntent.putExtra("ticket", usedTicketsArray.get(position).toString());
                        //showTicketIntent.putExtra("token", token);
                        startActivity(showTicketIntent);
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
