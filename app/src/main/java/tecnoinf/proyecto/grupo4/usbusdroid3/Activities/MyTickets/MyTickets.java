package tecnoinf.proyecto.grupo4.usbusdroid3.Activities.MyTickets;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import tecnoinf.proyecto.grupo4.usbusdroid3.R;

public class MyTickets extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tickets);
        Intent father = getIntent();
        final String token = father.getStringExtra("token");

    }
}
