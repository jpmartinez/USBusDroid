package tecnoinf.proyecto.grupo4.usbusdroid3.Activities.NewTicket;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import tecnoinf.proyecto.grupo4.usbusdroid3.Models.BusStop;
import tecnoinf.proyecto.grupo4.usbusdroid3.R;

public class NewTicket extends AppCompatActivity {

    private static final String journeysFromToRest = "http://10.0.2.2:8080/usbus/api/1/test/journeys";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ticket);
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

                        Intent listServicesFromToIntent = new Intent(v.getContext(), NTJourneyList.class);
                        RestCallAsync restCall = new RestCallAsync(journeysFromToRest, "POST", postData, listServicesFromToIntent);
                        restCall.execute((Void) null);
                    } catch (JSONException e) {
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

    public class RestCallAsync extends AsyncTask<Void, Void, Boolean> {

        private String restURL;
        private String method;
        private JSONObject dataOut;
        private Intent nextActivity;

        public RestCallAsync(String url, String callMethod, JSONObject data, Intent intent) {
            restURL = url;
            method = callMethod;
            dataOut = data; //Se instancia con dataOut en null si el rest no requiere datos de entrada
            nextActivity = intent;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject result;
            try {
                result = getData();
                System.out.println(result);

                if(result.get("result").toString().equalsIgnoreCase("OK")){
                    //llamada OK
                    System.out.println("LLAMADA OK...");
                    JSONObject data = new JSONObject();
                    data.put("data", result.get("data").toString());
                    nextActivity.putExtra("data", data.toString());
                } else {
                    //algun error
                    System.out.println("DANGER WILL ROBINSON..." + result.get("result").toString());
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }

        public JSONObject getData() throws JSONException {
            JSONObject toReturn = null;

            if(this.restURL == null || this.restURL.isEmpty()) {
                toReturn = new JSONObject("{\"error\":\"USBus - URL not initialized\"");
            }
            else {
                HttpURLConnection connection = null;
                StringBuilder sb = new StringBuilder();
                try {
                    URL restURL = new URL(this.restURL);
                    connection = (HttpURLConnection) restURL.openConnection();
                    connection.setRequestMethod(this.method);
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.connect();

                    if(dataOut != null) {
                        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                        out.write(this.dataOut.toString());
                        out.close();
                    }

                    int HttpResult = connection.getResponseCode();

                    if (HttpResult == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(
                                connection.getInputStream(), "utf-8"));
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        br.close();

                        toReturn = new JSONObject();
                        toReturn.put("result", "OK");
                        toReturn.put("data", sb.toString());

                    } else {
                        System.out.println(connection.getResponseMessage());
                        toReturn = new JSONObject("{\"result\":\"ERROR\", \"data\": \"" + connection.getResponseMessage() + "\"}");
                    }

                } catch (ProtocolException e1) {
                    e1.printStackTrace();
                    toReturn = new JSONObject("{\"result\":\"ERROR\", \"data\": \"ProtocolException - " + e1.getMessage().replace(":", "-") + "\"}");
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                    toReturn = new JSONObject("{\"result\":\"ERROR\", \"data\": \"MalformedURLException - " + e1.getMessage().replace(":", "-") + "\"}");
                } catch (IOException e1) {
                    e1.printStackTrace();
                    toReturn = new JSONObject("{\"result\":\"ERROR\", \"data\": \"IOException - " + e1.getMessage().replace(":", "-") + "\"}");
                } catch (Exception e1) {
                    e1.printStackTrace();
                    toReturn = new JSONObject("{\"result\":\"ERROR\", \"data\": \"Exception - " + e1.getMessage().replace(":", "-") + "\"}");
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    return toReturn;
                }
            }
            return toReturn;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            System.out.println("en PostExecute con success en: " + success.toString());
            if (success) {
                startActivity(nextActivity);
                finish();
            } else {
                System.out.println("================onPostExecute con success en false");
                //TODO: Ver como manejar esto. Volver a la activity anterior? darle finish nomas? Buscar qué se suele hacer
            }
        }
    }
}
