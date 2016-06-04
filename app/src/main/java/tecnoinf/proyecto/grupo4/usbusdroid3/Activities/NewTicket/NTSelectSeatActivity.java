package tecnoinf.proyecto.grupo4.usbusdroid3.Activities.NewTicket;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import tecnoinf.proyecto.grupo4.usbusdroid3.R;

public class NTSelectSeatActivity extends AppCompatActivity {

    private int selectedSeat;
    private int lastSelectedPosition = -1;
    private static ArrayList<Integer> occupied;

public class MyAdapter extends BaseAdapter {

    final int NumberOfItem = 45 + 45/4; //TODO: cambiar por cantidad total de seats en el bus del journey
    private Bitmap[] bitmap = new Bitmap[NumberOfItem];

    private Context context;
    private LayoutInflater layoutInflater;

    MyAdapter(Context c){
        context = c;
        layoutInflater = LayoutInflater.from(context);

        for(int i = 0; i < NumberOfItem; i++){
            if((i + 3) % 5 == 0) {
                bitmap[i] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bus_aisle_dotted);
            } else {
                bitmap[i] = BitmapFactory.decodeResource(context.getResources(), R.drawable.seat_black);
            }
        }
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        Integer occupiedPosition = occupied.indexOf(position);
        System.out.println("ocupado: " + occupiedPosition);
        return ((((position + 3) % 5) != 0) || occupied.contains(position)); //TODO: ...&& position no está en rango de libres
        // Return true for clickable, false for not
        //return false;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return bitmap.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return bitmap[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        View grid;
        if(convertView==null){
            grid = new View(context);
            grid = layoutInflater.inflate(R.layout.gridview_seat, null);
        }else{
            grid = (View)convertView;
        }

        ImageView imageView = (ImageView)grid.findViewById(R.id.seatImage);
        imageView.setImageBitmap(bitmap[position]);
        TextView textView = (TextView)grid.findViewById(R.id.seatNumber);

        int seatNbr;
        if(position < 3) {
            seatNbr = (position + 1);
        } else {
            seatNbr = (position + 1) - (((position-2) / 5) + 1);
        }

        textView.setText(String.valueOf(seatNbr));

        //TODO: cambiar por los que NO vienen en el array de ocupados
        if(occupied.contains(position)) {
        //if((position+1) % 3 == 0 && (((position + 3) % 5) != 0)) {
            imageView.setColorFilter(Color.RED);
            grid.setEnabled(false);
            grid.setClickable(false);
        }

//        if((position - 1) % 4 == 0) {
//            grid.setPadding(0, 0, 20, 0);
//        }

        return grid;
    }
}

    GridView gridView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ntselect_seat);
        gridView = (GridView)findViewById(R.id.seatsGV);
        Intent father = getIntent();
        //occupied = father.getIntegerArrayListExtra("ocuppiedSeats");
        occupied = new ArrayList<Integer>();
        occupied.add(3);
        occupied.add(15);
        occupied.add(9);

        MyAdapter adapter = new MyAdapter(this);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //lastSelectedPreviousColor = view.findViewById(R.id.seatImage).getSolidColor();
                //selectedSeatPreviousView = (View) gridView.getItemAtPosition(position);
                //TODO: poner en verde el view
                //TODO: sacar el verde de el seleccionado anterior

                if(position < 3) {
                    selectedSeat = (position + 1);
                } else {
                    selectedSeat = (position + 1) - (((position-2) / 5) + 1);
                }
                //System.out.println("Selected Seat: " + selectedSeat);

                ImageView selectedSeatImage = (ImageView) view.findViewById(R.id.seatImage);
                if(!occupied.contains(position)) {
                    selectedSeatImage.setColorFilter(Color.GREEN);
                }

                //System.out.println("previous position: " + lastSelectedPosition + " color: " + lastSelectedPreviousColor);
                if(lastSelectedPosition > -1 &&
                        lastSelectedPosition != position &&
                        !occupied.contains(lastSelectedPosition) &&
                        !occupied.contains(position)) {
                    View lastView = parent.getChildAt(lastSelectedPosition);
                    //View lastView = (View) gridView.getItemAtPosition(lastSelectedPosition);
                    ImageView lastImage = (ImageView) lastView.findViewById(R.id.seatImage);
                    lastImage.clearColorFilter();
                    //lastImage.setColorFilter(lastSelectedPreviousColor);
                }

                //selectedSeatPreviousImage = (ImageView) view.findViewById(R.id.seatImage);
                if (!occupied.contains(position)) {
                    lastSelectedPosition = position;
                }
                //TODO: llamar a siguiente activity
            }
        });
    }
}
