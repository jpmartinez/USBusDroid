package tecnoinf.proyecto.grupo4.usbusdroid3.Activities.MyTickets;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import tecnoinf.proyecto.grupo4.usbusdroid3.R;

public class MyBookingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);
        //TODO: traer listado de tickets cambiando fondo por algo con el clock de schedule

        //TODO: al seleccionar un booking de la lista, dar la opción a comprar, llamando al select routestops (posiblemente)
        //TODO:   ya que el asiento y journey ya deberían estar en la reserva.
        //TODO:   una vez que se concrete la compra, marcar la reserva como active=false
    }
}
