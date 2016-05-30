package tecnoinf.proyecto.grupo4.usbusdroid3.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import tecnoinf.proyecto.grupo4.usbusdroid3.Activities.ContactUs.ContactUs;
import tecnoinf.proyecto.grupo4.usbusdroid3.Activities.MyTickets.MyTickets;
import tecnoinf.proyecto.grupo4.usbusdroid3.Activities.NewTicket.NewTicket;
import tecnoinf.proyecto.grupo4.usbusdroid3.Activities.TimeTable.TimeTable;
import tecnoinf.proyecto.grupo4.usbusdroid3.R;

public class MainClient extends AppCompatActivity {

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
        newTicketBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newTicketIntent = new Intent(v.getContext(), NewTicket.class);
                newTicketIntent.putExtra("token", token);
                startActivity(newTicketIntent);

            }
        });

        myTicketsBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myTicketsIntent = new Intent(v.getContext(), MyTickets.class);
                myTicketsIntent.putExtra("token", token);
                startActivity(myTicketsIntent);

            }
        });

        timeTableBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent timeTableIntent = new Intent(v.getContext(), TimeTable.class);
                timeTableIntent.putExtra("token", token);
                startActivity(timeTableIntent);

            }
        });

        contactBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactIntent = new Intent(v.getContext(), ContactUs.class);
                contactIntent.putExtra("token", token);
                startActivity(contactIntent);

            }
        });
    }
}
