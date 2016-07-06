package tecnoinf.proyecto.grupo4.usbusdroid3.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import tecnoinf.proyecto.grupo4.usbusdroid3.Activities.MyTickets.MyTicketsActivity;
import tecnoinf.proyecto.grupo4.usbusdroid3.Activities.NewTicket.NewTicket;
import tecnoinf.proyecto.grupo4.usbusdroid3.Activities.TimeTable.TimeTable;
import tecnoinf.proyecto.grupo4.usbusdroid3.Helpers.RestCallAsync;
import tecnoinf.proyecto.grupo4.usbusdroid3.R;

public class MainClient extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 1;
    private static String allBusStopsRest;
    //private static String myTicketsRest;
    //private static String allServicesRest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_client);

        allBusStopsRest = getString(R.string.URLallBusStops,
                getString(R.string.URL_REST_API),
                getString(R.string.tenantId));

        final ImageButton newTicketBt = (ImageButton) findViewById(R.id.newticketButton);
        ImageButton myTicketsBt = (ImageButton) findViewById(R.id.myticketsButton);
        ImageButton timeTableBt = (ImageButton) findViewById(R.id.timetableButton);
        ImageButton contactBt = (ImageButton) findViewById(R.id.contactButton);
        ImageButton signOutBt = (ImageButton) findViewById(R.id.signoutButton);

        final SharedPreferences sharedPreferences = getSharedPreferences("USBusData", Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("token", "");
        final String username = sharedPreferences.getString("username", "");

//        Intent father = getIntent();
//        final String token = father.getStringExtra("token");
//        final String username = father.getStringExtra("username");

        assert newTicketBt != null;
        newTicketBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AsyncTask<Void, Void, JSONObject> busStopsResult = new RestCallAsync(getApplicationContext(), allBusStopsRest, "GET", null, token).execute();
                    JSONObject busStopsData = busStopsResult.get();

                    Intent newTicketIntent = new Intent(v.getContext(), NewTicket.class);
                    //newTicketIntent.putExtra("token", token);
                    newTicketIntent.putExtra("data", busStopsData.toString());
                    startActivity(newTicketIntent);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        assert myTicketsBt != null;
        myTicketsBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myTicketsIntent = new Intent(v.getContext(), MyTicketsActivity.class);
                //myTicketsIntent.putExtra("token", token);
                myTicketsIntent.putExtra("username", username);
                System.out.println("En MainClient click on MyTickets");
                startActivity(myTicketsIntent);
            }
        });

        assert timeTableBt != null;
        timeTableBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    AsyncTask<Void, Void, JSONObject> busStopsResult = new RestCallAsync(getApplicationContext(), allBusStopsRest, "GET", null, token).execute();
                    JSONObject busStopsData = busStopsResult.get();

                    Intent timeTableIntent = new Intent(v.getContext(), TimeTable.class);
                    //timeTableIntent.putExtra("token", token);
                    timeTableIntent.putExtra("data", busStopsData.toString());
                    startActivity(timeTableIntent);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        assert contactBt != null;
        contactBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[] {getString(R.string.tenant_email_address)});
                email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contact_us_subject));
                email.putExtra(Intent.EXTRA_TEXT, getString(R.string.contact_us_body_start));
                email.setType("message/rfc822");

                startActivityForResult(Intent.createChooser(email, getString(R.string.contact_us_email_chooser_text)), 1);
            }
        });

        assert signOutBt != null;
        signOutBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", "");
                editor.putString("password", "");
                editor.apply();

                startActivity(new Intent(getBaseContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == MY_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                Toast.makeText(this, R.string.contact_us_result_toast, Toast.LENGTH_LONG).show();
            } else {
                Intent backToMain = new Intent(this.getBaseContext(), MainClient.class);
                backToMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(backToMain);
            }
        }
        finish();
    }
}
