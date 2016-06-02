package tecnoinf.proyecto.grupo4.usbusdroid3.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

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
import java.util.concurrent.ExecutionException;

import tecnoinf.proyecto.grupo4.usbusdroid3.Activities.ContactUs.ContactUs;
import tecnoinf.proyecto.grupo4.usbusdroid3.Activities.MyTickets.MyTickets;
import tecnoinf.proyecto.grupo4.usbusdroid3.Activities.NewTicket.NewTicket;
import tecnoinf.proyecto.grupo4.usbusdroid3.Activities.TimeTable.TimeTable;
import tecnoinf.proyecto.grupo4.usbusdroid3.R;

public class MainClient extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 1;
    private static final String allBusStopsRest = "http://10.0.2.2:8080/usbus/api/1/test/busStops";
    private static final String myTicketsRest = "http://10.0.2.2:8080/usbus/api/1/test/xxxxxxxx";
    private static final String allServicesRest = "http://10.0.2.2:8080/usbus/api/1/test/yyyyyyy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cliente);

        final ImageButton newTicketBt = (ImageButton) findViewById(R.id.newticketButton);
        ImageButton myTicketsBt = (ImageButton) findViewById(R.id.myticketsButton);
        ImageButton timeTableBt = (ImageButton) findViewById(R.id.timetableButton);
        ImageButton contactBt = (ImageButton) findViewById(R.id.contactButton);
        Intent loginIntent = getIntent();
        final String token = loginIntent.getStringExtra("token");

        //Toast.makeText(MainClient.this, loginIntent.getStringExtra("token"), Toast.LENGTH_LONG).show();
        //System.out.println(token);
        assert newTicketBt != null;
        newTicketBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject restSecurity = new JSONObject();
                    restSecurity.put("token", token);

                    AsyncTask<Void, Void, JSONObject> busStopResult = new RestCallAsync(allBusStopsRest, "POST", restSecurity).execute();
                    JSONObject journeyData = busStopResult.get();

                    Intent newTicketIntent = new Intent(v.getContext(), NewTicket.class);
                    newTicketIntent.putExtra("token", token);
                    newTicketIntent.putExtra("data", journeyData.toString());
                    startActivity(newTicketIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        assert myTicketsBt != null;
        myTicketsBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    JSONObject restSecurity = new JSONObject();
                    restSecurity.put("token", token);

                    AsyncTask<Void, Void, JSONObject> ticketsResult = new RestCallAsync(allBusStopsRest, "POST", restSecurity).execute();
                    JSONObject ticketsData = ticketsResult.get();

                    Intent myTicketsIntent = new Intent(v.getContext(), MyTickets.class);
                    myTicketsIntent.putExtra("token", token);
                    myTicketsIntent.putExtra("data", ticketsData.toString());
                    startActivity(myTicketsIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        assert timeTableBt != null;
        timeTableBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    JSONObject restSecurity = new JSONObject();
                    restSecurity.put("token", token);

                    AsyncTask<Void, Void, JSONObject> schedResult = new RestCallAsync(allBusStopsRest, "POST", restSecurity).execute();
                    JSONObject schedData = schedResult.get();

                    Intent timeTableIntent = new Intent(v.getContext(), TimeTable.class);
                    timeTableIntent.putExtra("token", token);
                    timeTableIntent.putExtra("data", schedData.toString());
                    startActivity(timeTableIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                //TODO: Ir a buscar datos para mostrar en la activity (list de servicios activos, ya que como no interesa comprar sino horarios, los viajes pueden traer información limitada)
            }
        });

        assert contactBt != null;
        contactBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[] {"proyectotecnoinf2016@gmail.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, "Contacto USBus");
                email.putExtra(Intent.EXTRA_TEXT, "Escriba su mensaje: ");
                email.setType("message/rfc822");

                startActivityForResult(Intent.createChooser(email, "Escoja su cliente de correo:"), 1);
                //TODO: cambiar strings por variables del "config"
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == MY_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
            } else {
                Intent backToMain = new Intent(this.getBaseContext(), MainClient.class);
                backToMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(backToMain);
            }
        }
        finish();
    }

    public class RestCallAsync extends AsyncTask<Void, Void, JSONObject> {

        private String restURL;
        private String method;
        private JSONObject dataOut;

        public RestCallAsync() {
        }

        public RestCallAsync(String url, String callMethod, JSONObject data) {
            restURL = url;
            method = callMethod;
            dataOut = data; //Se instancia con dataOut en null si el rest no requiere datos de entrada
        }

        public String getRestURL() {
            return restURL;
        }

        public void setRestURL(String restURL) {
            this.restURL = restURL;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public JSONObject getDataOut() {
            return dataOut;
        }

        public void setDataOut(JSONObject dataOut) {
            this.dataOut = dataOut;
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject result = null;
            try {
                result = getData();
                System.out.println(result);

                if(result.get("result").toString().equalsIgnoreCase("OK")){
                    //llamada OK
                    JSONObject data = new JSONObject();
                    data.put("data", result.get("data").toString());
//                    nextActivity.putExtra("data", data.toString());
                } else {
                    //algun error
                    System.out.println("DANGER WILL ROBINSON..." + result.get("result").toString());
                    return result;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
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
    }
}
