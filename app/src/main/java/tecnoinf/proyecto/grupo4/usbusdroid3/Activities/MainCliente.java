package tecnoinf.proyecto.grupo4.usbusdroid3.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import tecnoinf.proyecto.grupo4.usbusdroid3.R;

public class MainCliente extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cliente);

        ImageButton newTicketBt = (ImageButton) findViewById(R.id.newticketButton);
        ImageButton myTicketsBt = (ImageButton) findViewById(R.id.myticketsButton);
        ImageButton timeTableBt = (ImageButton) findViewById(R.id.timetableButton);
        ImageButton contactBt = (ImageButton) findViewById(R.id.contactButton);
        newTicketBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cositas
                Intent newTicketIntent = new Intent(v.getContext(), NewTicket.class);
                startActivity(newTicketIntent);

            }
        });

        myTicketsBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myTicketsIntent = new Intent(v.getContext(), MyTickets.class);
                startActivity(myTicketsIntent);

            }
        });

        timeTableBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent timeTableIntent = new Intent(v.getContext(), TimeTable.class);
                startActivity(timeTableIntent);

            }
        });

        contactBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactIntent = new Intent(v.getContext(), ContactUs.class);
                startActivity(contactIntent);

            }
        });
    }
}
