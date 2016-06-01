package tecnoinf.proyecto.grupo4.usbusdroid3.Activities.MyTickets;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import tecnoinf.proyecto.grupo4.usbusdroid3.Activities.MainClient;
import tecnoinf.proyecto.grupo4.usbusdroid3.R;

public class MyTickets extends AppCompatActivity {

    private static final String busStopsURL = "http://10.0.2.2:8080/usbus/api/1/test/busStops";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tickets);
        Intent father = getIntent();
        final String token = father.getStringExtra("token");

        RestCallAsync call = new RestCallAsync(busStopsURL, "POST", null);
        call.execute((Void) null);
    }


    public class RestCallAsync extends AsyncTask<Void, Void, Boolean> {

        private String restURL;
        private String method;
        private JSONObject dataOut;

        public RestCallAsync() {
        }

        public RestCallAsync(String url, String callMethod, JSONObject data) {
            restURL = url;
            method = callMethod;
            dataOut = data;
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
        protected Boolean doInBackground(Void... params) {
            JSONObject result;
            try {
                //String testURL = "http://10.0.2.2:8080/usbus/api/authentication"; //usar loginURL

                result = getData();
                //String dummy = result.toString();
                System.out.println(result);
                if(result.get("result").toString().equalsIgnoreCase("OK")){
                    //login OK
                    System.out.println("LOGIN OK...");
                    JSONArray data = new JSONArray(result.get("data").toString());
                    //TODO: llegué hasta aquí, tengo el array OK
                    //TODO: ahora hacete mago y mandalo al ListView (que ni existe aún)
                    //TODO: Esto es todo prueba de concepto. En produccion los datos iniciales van a llegar a la activity desde la anterior
                } else {
                    //algun error
                    System.out.println("DANGER WILL ROBINSON..." + result.get("result").toString());
                    return false;
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            // TODO: register the new account here.
            return true;
        }

        public JSONObject getData() throws JSONException {
            JSONObject toReturn = null;

            if(this.restURL == null || this.restURL.isEmpty()) {
                toReturn = new JSONObject("{\"error\":\"USBus - URL not initialized\"");
            }
            else {

                HttpURLConnection connection = null;
                BufferedReader reader = null;
                StringBuilder sb = new StringBuilder();
                try {
                    URL restURL = new URL(this.restURL);
                    connection = (HttpURLConnection) restURL.openConnection();
                    connection.setRequestMethod(this.method);
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.connect();

//                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
//                out.write(this.dataOut.toString());
//                out.close();

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
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                        return toReturn;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return toReturn;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            System.out.println("en PostExecute con success en: " + success.toString());
            //mAuthTask = null;
//            showProgress(false);

            if (success) {
//                Intent mainIntent = new Intent(getBaseContext(), MainClient.class);
//                mainIntent.putExtra("token", token);
//                startActivity(mainIntent);
                System.out.println("==============postex");

                finish();
            } else {
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
            }
        }
    }

}


