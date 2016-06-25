package tecnoinf.proyecto.grupo4.usbusdroid3.Activities.NewTicket;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tecnoinf.proyecto.grupo4.usbusdroid3.R;

public class NTSelectSeatActivity extends AppCompatActivity {

    private int selectedSeat = 0;
    private int lastSelectedPosition = -1;
    private static ArrayList<Integer> occupied;


    public class MyAdapter extends BaseAdapter {

        final int numberOfItem = 45 + 45/4; //TODO: cambiar por cantidad total de seats en el bus del journey (journey.bus.seats)
        private Bitmap[] bitmap = new Bitmap[numberOfItem];

        private Context context;
        private LayoutInflater layoutInflater;

        MyAdapter(Context c){
            context = c;
            layoutInflater = LayoutInflater.from(context);

            for(int i = 0; i < numberOfItem; i++){
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
            return ((((position + 3) % 5) != 0) && !occupied.contains(position)); //TODO: ...&& position no está en rango de libres
            // Return true for clickable, false for not
            //return false;
        }

        @Override
        public int getCount() {
            return bitmap.length;
        }

        @Override
        public Object getItem(int position) {
            return bitmap[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Integer positionI = position;

            View grid;
            if(true){//(convertView==null){
                //grid = new View(context);
                layoutInflater = getLayoutInflater();
                grid = layoutInflater.inflate(R.layout.gridview_seat, null);
            }else{
                grid = (View)convertView;
            }

            ImageView imageView = (ImageView)grid.findViewById(R.id.seatImage);
            imageView.setImageBitmap(bitmap[position]);
            TextView textView = (TextView)grid.findViewById(R.id.seatNumber);

            int seatNbr;
            if(positionI < 3) {
                seatNbr = (positionI + 1);
            } else {
                seatNbr = (positionI + 1) - (((positionI-2) / 5) + 1);
            }

            textView.setText(String.valueOf(seatNbr));

            if(occupied != null && !occupied.isEmpty() && occupied.indexOf(positionI) != -1) {
                System.out.println("position: " + positionI);
                System.out.println("indexOf: " + occupied.indexOf(positionI));
            //if((position+1) % 3 == 0 && (((position + 3) % 5) != 0)) {
                imageView.setColorFilter(Color.RED);
                grid.setEnabled(false);
                grid.setClickable(false);
            }

            if(seatNbr == selectedSeat && positionIsEnabled(positionI)) {
                System.out.println("============================");
                System.out.println("seatNbr = selectedSeat " + seatNbr + " + " + selectedSeat);
                imageView.setColorFilter(Color.GREEN);
            }

    //        if((position - 1) % 4 == 0) {
    //            grid.setPadding(0, 0, 20, 0);
    //        }

            return grid;
        }
    }

    GridView gridView;
    Button confirmButton;
    private String token;
    private Intent father;
    private JSONObject journeyJSON;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ntselect_seat);
            gridView = (GridView)findViewById(R.id.seatsGV);
            confirmButton = (Button) findViewById(R.id.confirmSeatBtn);
            father = getIntent();
            SharedPreferences sharedPreferences = getSharedPreferences("USBusData", Context.MODE_PRIVATE);
            token = sharedPreferences.getString("token", "");
            //token = father.getStringExtra("token");
            //ticketPriceRest = getString(R.string.URLticketPrice, );

            journeyJSON = new JSONObject(father.getStringExtra("journey"));

            System.out.println("SelectSeat journey: " + journeyJSON);
            JSONArray occupiedJSONArray = journeyJSON.getJSONArray("seatsState");

            //occupied = father.getIntegerArrayListExtra("ocuppiedSeats");
            occupied = new ArrayList<>();
            Integer occupiedSeat;
            Integer occupiedPosition;
            for (int i = 0; i < occupiedJSONArray.length(); i++) {
                if (!occupiedJSONArray.getJSONObject(i).getBoolean("free")) {
                    occupiedSeat = occupiedJSONArray.getJSONObject(i).getInt("number");
                    occupiedPosition = seat2Position(occupiedSeat);
                    System.out.println("Seat: "+occupiedSeat + "  Position: " + occupiedPosition);

                    System.out.println("adding to occupied: " + occupiedPosition);
                    occupied.add(occupiedPosition);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MyAdapter adapter = new MyAdapter(this);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //lastSelectedPreviousColor = view.findViewById(R.id.seatImage).getSolidColor();
                //selectedSeatPreviousView = (View) gridView.getItemAtPosition(position);

                if(positionIsEnabled(position)) {
                    selectedSeat = position2Seat(position);
//                    if (position < 3) {
//                        selectedSeat = (position + 1);
//                    } else {
//                        selectedSeat = (position + 1) - (((position - 2) / 5) + 1);
//                    }
                    System.out.println("Selected Seat: " + selectedSeat);

                    ImageView selectedSeatImage = (ImageView) view.findViewById(R.id.seatImage);
                    if (occupied != null && !occupied.isEmpty() && !occupied.contains(position)) {
                        selectedSeatImage.setColorFilter(Color.GREEN);
                    }

                    //System.out.println("previous position: " + lastSelectedPosition + " color: " + lastSelectedPreviousColor);
                    if (lastSelectedPosition > -1 &&
                            lastSelectedPosition != position &&
                            occupied != null &&
                            !occupied.isEmpty() &&
                            !occupied.contains(lastSelectedPosition) &&
                            !occupied.contains(position)) {
                        View lastView = parent.getChildAt(lastSelectedPosition);
                        //View lastView = (View) gridView.getItemAtPosition(lastSelectedPosition);
                        ImageView lastImage = (ImageView) lastView.findViewById(R.id.seatImage);
                        lastImage.clearColorFilter();
                        //lastImage.setColorFilter(lastSelectedPreviousColor);
                    }

                    //selectedSeatPreviousImage = (ImageView) view.findViewById(R.id.seatImage);
                    if (occupied != null && !occupied.isEmpty() && !occupied.contains(position)) {
                        lastSelectedPosition = position;
                    }
                }
            }
        });

            assert confirmButton != null;
            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectedSeat > 0) {
                        System.out.println("Boton onClick selected seat: " + selectedSeat);
                        //TODO: recabar información para solicitar precio al rest
//                            JSONObject postData = new JSONObject();
//                            postData.put("token", token);
                        //postData.put("from", journeyJSON.getJSONObject("service").getJSONObject("route").get("origin"));
                        //postData.put("destination", journeyJSON.getJSONObject("service").getJSONObject("route").get("destination"));
//                            postData.put("otros", "otros");

                        //TODO: insertar una activity antes del confirmation, en donde seleccione subida y bajada
                        //TODO: y en base a esa subida y bajada poder calcular el precio para enviar al confirmation
//                            AsyncTask<Void, Void, JSONObject> ticketCostResult = new RestCallAsync(getApplicationContext(), ticketPriceRest, "POST", postData, token).execute();
//                            JSONObject ticketCostData = ticketCostResult.get();

//                            System.out.println("===========Data del ticket:");
//                            System.out.println(ticketCostData);


                        Intent busStopSelectionIntent = new Intent(getBaseContext(), NTBusStopSelectionActivity.class);
                        busStopSelectionIntent.putExtra("seat", String.valueOf(selectedSeat));
                        busStopSelectionIntent.putExtra("journey", father.getStringExtra("journey"));
                        //busStopSelectionIntent.putExtra("token", token);
                        //confirmationIntent.putExtra("ticketCost", ticketCostData.getJSONObject("data").get("cost").toString());
                        startActivity(busStopSelectionIntent);

                    } else {
                        Toast.makeText(getApplicationContext(), "Debe seleccionar un asiento", Toast.LENGTH_LONG).show();
                    }
                }
            });
        //TODO: llamar a siguiente activity
    }

    public boolean positionIsEnabled(int position) {
        System.out.println("positionIsEnabled position:"+position);
        Integer positionI = position;
        if(occupied != null && !occupied.isEmpty()) {
            return (((position+3) % 5) != 0) && (occupied.indexOf(positionI) == -1);
        } else {
            Integer occupiedPosition = occupied.indexOf(positionI);
            System.out.println("ocupado: " + occupiedPosition);
            Boolean result;
            result = ((((positionI + 3) % 5) != 0) && occupiedPosition.intValue() == -1); //TODO: ...&& position no está en rango de libres
            System.out.println("result: "+result);
            return result;
            // Return true for clickable, false for not
            //return false;
        }
    }

    private Integer position2Seat (Integer position) {
        Integer seat;

        if(position < 3) {
            seat = (position + 1);
        } else {
            seat = (position + 1) - (((position-2) / 5) + 1);
        }

        return seat;
    }

    private Integer seat2Position (Integer seat) {
        Integer position;

        if(seat < 3) {
            position = (seat - 1);
        } else if (seat == 6){
            position = 6;
        } else {
            position = (seat - 1) + (((seat-2) / 4) + 1);
        }
        return position;
    }
}
