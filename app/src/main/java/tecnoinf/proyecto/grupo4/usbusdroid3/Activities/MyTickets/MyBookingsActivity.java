package tecnoinf.proyecto.grupo4.usbusdroid3.Activities.MyTickets;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import tecnoinf.proyecto.grupo4.usbusdroid3.Activities.NewTicket.NTBusStopSelectionActivity;
import tecnoinf.proyecto.grupo4.usbusdroid3.Helpers.RestCallAsync;
import tecnoinf.proyecto.grupo4.usbusdroid3.Models.BookingShort;
import tecnoinf.proyecto.grupo4.usbusdroid3.Models.TicketShort;
import tecnoinf.proyecto.grupo4.usbusdroid3.R;

public class MyBookingsActivity extends ListActivity {

    private static String token;
    private static JSONArray bookingsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);
        //TODO: traer listado de tickets reservados cambiando fondo por algo con el clock de schedule

        //TODO: al seleccionar un booking de la lista, dar la opción a comprar, llamando al select routestops (posiblemente)
        //TODO:   ya que el asiento y journey ya deberían estar en la reserva.
        //TODO:   una vez que se concrete la compra, marcar la reserva como active=false

        Intent father = getIntent();
        SharedPreferences sharedPreferences = getSharedPreferences("USBusData", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        try {
            bookingsArray = new JSONArray(father.getStringExtra("myBookings").replace("\\", ""));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

            final List<BookingShort> bookingsList = BookingShort.fromJson(bookingsArray);
            ArrayList<HashMap<String, String>> ticketsMap = new ArrayList<>();

            for (BookingShort bs2 : bookingsList) {
                HashMap<String, String> t = new HashMap<>();
                t.put("id", bs2.getId().toString());
                t.put("journeyId", bs2.getJourneyId().toString());
                t.put("dueDate", dateFormat.format(bs2.getDueDate()));
                t.put("seat", bs2.getSeat().toString());
                t.put("serviceName", bs2.getServiceName());
                t.put("getsOn", bs2.getGetsOn());
                t.put("getsOff", bs2.getGetsOff());

                ticketsMap.add(t);
            }

            ListAdapter adapter = new SimpleAdapter(
                    getApplicationContext(),
                    ticketsMap,
                    R.layout.activity_mybookings_list_item,
                    new String[] { "id",
                            "journeyId",
                            "serviceName",
                            "dueDate",
                            "seat",
                            "getsOn",
                            "getsOff"},
                    new int[] { R.id.bookingItemid,
                            R.id.bookingJourneyId,
                            R.id.bookingsJourneyNameTV,
                            R.id.bookingsDueDateTV,
                            R.id.bookingsSeatTV,
                            R.id.bookingsGetsOnTV,
                            R.id.bookingsGetsOffTV});

            setListAdapter(adapter);
            ListView lv = getListView();
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    try {
                        String bookingId = ((TextView) view.findViewById(R.id.bookingItemid)).getText().toString();
                        String journeyId = ((TextView) view.findViewById(R.id.bookingJourneyId)).getText().toString();
                        String seat =  ((TextView) view.findViewById(R.id.bookingsSeatTV)).getText().toString();
                        String getsOn = ((TextView) view.findViewById(R.id.bookingsGetsOnTV)).getText().toString();
                        String getsOff = ((TextView) view.findViewById(R.id.bookingsGetsOffTV)).getText().toString();

                        String urlGetJourney = getString(R.string.URLgetJourney,
                                                        getString(R.string.URL_REST_API),
                                                        getString(R.string.tenantId),
                                                        journeyId);
                        AsyncTask<Void, Void, JSONObject> journeyResult = new RestCallAsync(getApplicationContext(), urlGetJourney, "GET", null, token).execute();
                        JSONObject journeyData =  journeyResult.get();

                        String ticketPriceRest = getString(R.string.URLticketPrice,
                                                            getString(R.string.URL_REST_API),
                                                            getString(R.string.tenantId),
                                                            journeyId,
                                                            getsOn.replace(" ", "+"),
                                                            getsOff.replace(" ", "+"));

                        AsyncTask<Void, Void, JSONObject> priceResult = new RestCallAsync(getApplicationContext(), ticketPriceRest, "GET", null, token).execute();
                        JSONObject priceData = priceResult.get();
                        Double ticketPriceDouble = new JSONObject(priceData.getString("data")).getDouble("price");
                        String ticketPrice = String.format("%.2f", ticketPriceDouble);

                        Intent buyTicketIntent = new Intent(getBaseContext(), MTBuyBookingActivity.class);
                        buyTicketIntent.putExtra("bookingId", bookingId);
                        buyTicketIntent.putExtra("seat", seat);
                        buyTicketIntent.putExtra("journey", journeyData.getString("data"));
                        buyTicketIntent.putExtra("ticketPrice", ticketPrice);
                        buyTicketIntent.putExtra("getsOn", getsOn);
                        buyTicketIntent.putExtra("getsOff", getsOff);

                        startActivity(buyTicketIntent);
                    } catch (InterruptedException | ExecutionException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            });





        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }
}
