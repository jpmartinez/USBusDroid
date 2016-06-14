package tecnoinf.proyecto.grupo4.usbusdroid3.Activities.NewTicket;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import tecnoinf.proyecto.grupo4.usbusdroid3.Helpers.RestCallAsync;
import tecnoinf.proyecto.grupo4.usbusdroid3.Models.BusStop;
import tecnoinf.proyecto.grupo4.usbusdroid3.R;

public class NewTicket extends AppCompatActivity {

    private static String journeysFromToRest;
    private Button btnSelectDate;
    private static TextView dateField;
    private int year_x, month_x, day_x;
    static final int DIALOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ticket);

        dateField = (TextView) findViewById(R.id.tvNTSelectedDate);
        showDatePicker();

        Intent father = getIntent();

        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);

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
                        //postData.put("token", token);
                        String origin = spinnerFrom.getSelectedItem().toString();
                        String destination = spinnerTo.getSelectedItem().toString();
                        postData.put("origin", origin);
                        postData.put("destination", destination);
                        postData.put("date", day_x + "/" + month_x + "/" + year_x);

                        journeysFromToRest = getString(R.string.URLjourneysFromTo, getString(R.string.URL_REST_API), getString(R.string.tenantId));
                        //TODO: en la URL de arriba falta agregar parametros de origen y destino.
                        AsyncTask<Void, Void, JSONObject> journeyResult = new RestCallAsync(getApplicationContext(), journeysFromToRest, "GET", null, token).execute();
                        JSONObject journeyData = journeyResult.get();

                        Intent listJourneysFromToIntent = new Intent(v.getContext(), NTJourneyListActivity.class);
                        listJourneysFromToIntent.putExtra("token", token);
                        listJourneysFromToIntent.putExtra("data", journeyData.toString());
                        startActivity(listJourneysFromToIntent);
//                        RestCallAsync restCall = new RestCallAsync(journeysFromToRest, "POST", postData, listServicesFromToIntent);
//                        restCall.execute((Void) null);
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

        //TODO: paso uno sería mostrar 2 combos con origen y destino.
        //TODO: StartActivityForResult llamando a la Common que traiga los posibles trayectos, los liste y permita seleccionar uno.
        //TODO: luego que vuelva el seleccionado hacer otro StartAct4Result que liste los Viajes o Servicios (ver cual es mejor) y que permita seleccionar uno.
        //TODO: luego que vuelva el viaje seleccionado, ponerlo como extra y llamar a otra activity de este paquete que muestre mapa de asientos y se haga cargo
        //TODO: Este mapa al seleccionar un asiento permite realizar la compra del mismo, pagando andá a saber como.
    }

    public void showDatePicker() {
        btnSelectDate = (Button) findViewById(R.id.btnNTSelectDate);
        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);

            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID){
            return new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear;
            day_x = dayOfMonth;
            dateField.setText(day_x + "/" + month_x + "/" + year_x);
        }
    };
}
