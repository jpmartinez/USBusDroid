package tecnoinf.proyecto.grupo4.usbusdroid3.Helpers;

import android.content.Context;
import android.os.AsyncTask;

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

import tecnoinf.proyecto.grupo4.usbusdroid3.R;

public class RestCallAsync extends AsyncTask<Void, Void, JSONObject> {

    private Context mCtx;
    private String REST_API_URL;
    private String restURL;
    private String method;
    private JSONObject dataOut;
    private String token;

    public RestCallAsync(Context ctx, String url, String callMethod, JSONObject data, String auth_token) {
        restURL = url;
        method = callMethod;
        dataOut = data; //Se instancia con dataOut en null si el rest no requiere datos de entrada (GET)
        token = auth_token;
        mCtx = ctx.getApplicationContext();
        REST_API_URL = mCtx.getString(R.string.URL_REST_API);
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        JSONObject result = null;
        try {
            result = getData();
            System.out.println("====doInBackground de RestCallAsync: ");
            System.out.println(result);

            if(result.get("result").toString().equalsIgnoreCase("OK")){
                //llamada OK
                JSONObject data = new JSONObject();
                data.put("data", result.get("data").toString());
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
            System.out.println("888888888888888 URL que llegó: " + restURL);
            HttpURLConnection connection = null;
            StringBuilder sb = new StringBuilder();
            try {
                URL restURL = new URL(this.restURL);
                connection = (HttpURLConnection) restURL.openConnection();
                connection.setRequestMethod(this.method);
                connection.setRequestProperty("Content-Type", "application/json");
                if (token != null && !token.isEmpty()) {
                    System.out.println("77777777777cargando token bearer: " + token);
                    connection.setRequestProperty("Authorization", "Bearer " + token);
                }
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

                    System.out.println("=======Vino del rest:");
                    System.out.println(sb.toString());

                    toReturn = new JSONObject();
                    toReturn.put("result", "OK");
                    toReturn.put("data", sb.toString());

                } else {
                    System.out.println(connection.getResponseMessage());
                    toReturn = new JSONObject("{\"result\":\"ERROR\", \"data\": \"" + HttpResult + connection.getResponseMessage() + "\"}");
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