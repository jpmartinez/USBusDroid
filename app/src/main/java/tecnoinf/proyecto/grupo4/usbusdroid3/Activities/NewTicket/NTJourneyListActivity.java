package tecnoinf.proyecto.grupo4.usbusdroid3.Activities.NewTicket;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import tecnoinf.proyecto.grupo4.usbusdroid3.Helpers.DayConverter_ES;
import tecnoinf.proyecto.grupo4.usbusdroid3.Models.JourneyShort;
import tecnoinf.proyecto.grupo4.usbusdroid3.R;

public class NTJourneyListActivity extends ListActivity {

    private ProgressDialog pDialog;

    // URL to get contacts JSON
    //private static String url = "http://10.0.2.2:8080/SAPo-FO/rest/lucia/category/all";
    private static String urlVS = "/SAPo-FO/api/VirtualStorage/myVSs/";
    private static String urlUser = "/SAPo-FO/api/usuario/buscar?nick=";
    private String userId;

    // JSON Node names
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_ORIGIN = "origin";

    // contacts JSONArray
    JSONArray contacts = null;
    JSONArray virtualStorages = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> vsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_journeylist);
//
//        Intent in = getIntent();
//        String userName = in.getStringExtra("userName");
//
//        this.userId = in.getExtras().getString("sapoUserId");
//
//        //get myVSs from SAPo
//        AsyncTask<String, String, JSONObject> vsResult = new GetVSData().execute(userId);
//        JSONObject vsData = null;
//
//        try {
//            vsData = vsResult.get();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        //TODO: if vsData llega null, llamar Activity que diga que no se encontraron VSs
//        // o a la misma vslistactivity pero con un extra en el intent para que ella muestre el mensaje
//
//        JSONArray ownedVS = null;
//        JSONArray followedVS = null;
//        try {
//            followedVS = vsData.getJSONArray("following");
//            ownedVS = vsData.getJSONArray("owned");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            virtualStorages = concatArray(ownedVS, followedVS);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        vsList = new ArrayList<HashMap<String, String>>();
//
//        if (virtualStorages != null && virtualStorages.length() > 0) {
//            for (int i = 0; i < virtualStorages.length(); i++) {
//                JSONObject c;
//                try {
//                    c = virtualStorages.getJSONObject(i);
//
//                    String id = c.getString(TAG_ID);
//                    String name = c.getString(TAG_NAME);
//
//                    // tmp hashmap for single VS
//                    HashMap<String, String> vs = new HashMap<String, String>();
//
//                    // adding each child node to HashMap key => value
//                    vs.put(TAG_ID, id);
//                    vs.put(TAG_NAME, name);
//
//                    if(c.getString("owner").equals(userId)){
//                        vs.put("origin", "Owned");
//                    }
//                    else{
//                        vs.put("origin", "Following");
//                    }
//
//                    // adding VS to VSlist
//                    vsList.add(vs);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        if (pDialog.isShowing()){
//            Timer loading = new Timer();
//            loading.schedule(new TimerTask() {
//
//                @Override
//                public void run() {
//                    pDialog.dismiss();
//                }
//            }, 1000);
//        }
//
//        ListAdapter adapter = new SimpleAdapter(
//                getApplicationContext(),
//                vsList,
//                R.layout.activity_ntjourneys_list_item,
//                new String[] { "id", "name", "day", "time", "bus" },
//                new int[] { R.id.id, R.id.journeyNameTV, R.id.journeyDayTV, R.id.journeyTimeTV, R.id.busNumberTV });
//
//        setListAdapter(adapter);
//
//        ListView lv = getListView();
//
//        // Listview on item click listener
//        lv.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                // getting values from selected ListItem
//                String name = ((TextView) view.findViewById(R.id.name))
//                        .getText().toString();
//                String vsid = ((TextView) view.findViewById(R.id.id))
//                        .getText().toString();
//                // Starting single VS activity
//                Intent selectedVS = new Intent(getBaseContext(), SingleVSActivity.class);
//                selectedVS.putExtra(TAG_NAME, name);
//                selectedVS.putExtra("userid", userId);
//                selectedVS.putExtra(TAG_ID, vsid);
//                startActivity(selectedVS);
//            }
//        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ntjourneys_list);
        Intent father = getIntent();
        final String token = father.getStringExtra("token");

        try {
            JSONObject intentData = new JSONObject(father.getStringExtra("data"));
            final JSONArray journeyJsonArray = new JSONArray(intentData.get("data").toString().replace("\\", ""));

            final List<JourneyShort> journeyList = JourneyShort.fromJson(journeyJsonArray);
            System.out.println("======estoy, journeyList size: " + journeyList.size());
            //ArrayList<String> busStopsNames = new ArrayList<>();
            for (JourneyShort js: journeyList) {
                System.out.println("====Journey:");
                System.out.println(js.getName());
                System.out.println(js.getDay());
                System.out.println(js.getTime());
                System.out.println(js.getBusNumber());
            }

            ArrayList<HashMap<String, String>> journeyMap = new ArrayList<>();

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            timeFormat.setTimeZone(TimeZone.getTimeZone("America/Montevideo"));

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dateFormat.setTimeZone(TimeZone.getTimeZone("America/Montevideo"));

            for (JourneyShort js2 :
                    journeyList) {
                HashMap<String, String> j = new HashMap<>();
                j.put("id", js2.getId().toString());
                j.put("name", js2.getName());
                j.put("day", DayConverter_ES.convertES(js2.getDay()));
                j.put("date", dateFormat.format(js2.getDate()));
                j.put("time", timeFormat.format(js2.getTime()));
                //j.put("busNumber", js2.getBusNumber().toString());
                j.put("busNumber", js2.getBusNumber().toString());

                journeyMap.add(j);
            }

            ListAdapter adapter = new SimpleAdapter(
                    getApplicationContext(),
                    journeyMap,
                    R.layout.activity_ntjourneys_list_item,
                    new String[] { "id", "name", "day", "date", "time", "busNumber" },
                    new int[] { R.id.id, R.id.journeyNameTV, R.id.journeyDayTV, R.id.journeyDateTV, R.id.journeyTimeTV, R.id.busNumberTV });

            setListAdapter(adapter);

            ListView lv = getListView();

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    try {
                        // getting values from selected ListItem
                        String name = ((TextView) view.findViewById(R.id.journeyNameTV))
                                .getText().toString();
                        String journeyid = ((TextView) view.findViewById(R.id.id))
                            .getText().toString();

//                        System.out.println("===============selected journey: ");
//                        System.out.println(journeyid + name);
                        Intent selectSeat = new Intent(getBaseContext(), NTSelectSeatActivity.class);

                        selectSeat.putExtra("journey", journeyJsonArray.get(Integer.valueOf(journeyid)).toString());
                        selectSeat.putExtra("token", token);
                        startActivity(selectSeat);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

//            ArrayAdapter<JourneyShort> itemsAdapter =
//                    new ArrayAdapter<>(this, R.layout.activity_ntjourneys_list_item, journeyList);
//
//            ListView journeysListView = (ListView) findViewById(R.id.journeysLV);
//            assert journeysListView != null;
//            journeysListView.setAdapter(itemsAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //TODO: agarrar el father intent
        //TODO: convertir el array json en array de objetos (journeys) (armar el conversor tal como el busstop)
        //TODO: desplegar el listview

        //List<JourneyShort> journeyList = new ArrayList<>();
        //TODO: cargar en este journeyList los items del array que vino en el intent
        //TODO: journey.id / busnumber / journey.service.name / day / time


    }

    private JSONArray concatArray(JSONArray arr1, JSONArray arr2) throws JSONException {

        JSONArray result = new JSONArray();
        if((arr1 == null || arr1.length() == 0) && (arr2 == null || arr2.length() == 0)){
            return null;
        }
        if(arr1 == null || arr1.length() == 0){
            return arr2;
        }
        if(arr2 == null || arr2.length() == 0){
            return arr1;
        }

        for (int i = 0; i < arr1.length(); i++) {
            result.put(arr1.get(i));
        }
        for (int i = 0; i < arr2.length(); i++) {
            result.put(arr2.get(i));
        }
        return result;
    }

//    private class GetUserData extends AsyncTask<String, String, JSONObject> {
//
//        protected void onPreExecute(){
//        }
//
//        @Override
//        protected JSONObject doInBackground(String... params) {
//
//            // Creating service handler class instance
//            ServiceHandler sh = new ServiceHandler();
//
//            // Making a request to url and getting response
//            String jsonStr = sh.makeServiceCall(urlUser+params[0], ServiceHandler.GET);
//
//            Log.d("Response UserData: ", "> " + jsonStr);
//
//            if (jsonStr != null) {
//                try {
//                    JSONObject jsonObj = new JSONObject(jsonStr);
//                    return jsonObj;
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                Log.e("ServiceHandler", "Couldn't get any data from the url");
//            }
//            return null;
//        }
//    }

//    private class GetVSData extends AsyncTask<String, String, JSONObject> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // Showing progress dialog
//            pDialog = new ProgressDialog(VSListActivity.this);
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();
//
//        }
//
//        @Override
//        protected JSONObject doInBackground(String... params) {
//            // Creating service handler class instance
//            ServiceHandler sh = new ServiceHandler();
//
//            // Making a request to url and getting response
//            String jsonStr = sh.makeServiceCall(urlVS+params[0], ServiceHandler.GET);
//
//            Log.d("Response MyVSs: ", "> " + jsonStr);
//
//            if (jsonStr != null) {
//                try {
//                    JSONObject jsonObj = new JSONObject(jsonStr);
//                    return jsonObj;
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                Log.e("ServiceHandler", "Couldn't get any data from the url");
//            }
//            return null;
//        }
//
//    }

}
