package tecnoinf.proyecto.grupo4.usbusdroid3.Helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;

import tecnoinf.proyecto.grupo4.usbusdroid3.Activities.MyTickets.MTBuyBookingActivity;
import tecnoinf.proyecto.grupo4.usbusdroid3.Models.BookingShort;
import tecnoinf.proyecto.grupo4.usbusdroid3.R;

public class ListViewAdapter extends BaseSwipeAdapter {

    private Context mContext;
    private List<BookingShort> bookingsList;
    private String token;

    public ListViewAdapter(Context mContext, List<BookingShort> bookingsList) {
        this.mContext = mContext;
        this.bookingsList = bookingsList;

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("USBusData", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.bookingSwipeLayout;
    }

    @Override
    public View generateView(final int position, final ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.activity_my_bookings_swipe_item, null);
        SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                //YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
        });
        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(mContext, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });
        v.findViewById(R.id.deleteBookingBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bookingId = bookingsList.get(position).getId().toString();
                String cancelBookingRest = mContext.getString(R.string.URLdeleteBooking,
                                                        mContext.getString(R.string.URL_REST_API),
                                                        mContext.getString(R.string.tenantId),
                                                        bookingId);
                AsyncTask<Void, Void, JSONObject> cancelBookingResult = new RestCallAsync(mContext, cancelBookingRest, "DELETE", null, token).execute();
                try {
                    JSONObject cancelBookingData = cancelBookingResult.get();

                    if (cancelBookingData.getString("result").equalsIgnoreCase("OK")) {
                        Toast.makeText(mContext, "Reserva Cancelada Correctamente", Toast.LENGTH_SHORT).show();
                        bookingsList.remove(position);
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(mContext, "Error al cancelar la reserva", Toast.LENGTH_SHORT).show();
                    }

                } catch (InterruptedException | ExecutionException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        v.findViewById(R.id.buyBookingBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext, "click buy", Toast.LENGTH_SHORT).show();

                try {
                    String bookingId = bookingsList.get(position).getId().toString();
                    String journeyId = bookingsList.get(position).getJourneyId().toString();
                    String seat =  bookingsList.get(position).getSeat().toString();
                    String getsOn = bookingsList.get(position).getGetsOn();
                    String getsOff = bookingsList.get(position).getGetsOff();

                    String urlGetJourney = mContext.getString(R.string.URLgetJourney,
                            mContext.getString(R.string.URL_REST_API),
                            mContext.getString(R.string.tenantId),
                            journeyId);
                    AsyncTask<Void, Void, JSONObject> journeyResult = new RestCallAsync(mContext, urlGetJourney, "GET", null, token).execute();
                    JSONObject journeyData =  journeyResult.get();

                    String ticketPriceRest = mContext.getString(R.string.URLticketPrice,
                            mContext.getString(R.string.URL_REST_API),
                            mContext.getString(R.string.tenantId),
                            journeyId,
                            getsOn.replace(" ", "+"),
                            getsOff.replace(" ", "+"));

                    AsyncTask<Void, Void, JSONObject> priceResult = new RestCallAsync(mContext, ticketPriceRest, "GET", null, token).execute();
                    JSONObject priceData = priceResult.get();
                    Double ticketPriceDouble = new JSONObject(priceData.getString("data")).getDouble("price");
                    String ticketPrice = String.format("%.2f", ticketPriceDouble);

                    Intent buyTicketIntent = new Intent(mContext, MTBuyBookingActivity.class);
                    buyTicketIntent.putExtra("bookingId", bookingId);
                    buyTicketIntent.putExtra("seat", seat);
                    buyTicketIntent.putExtra("journey", journeyData.getString("data"));
                    buyTicketIntent.putExtra("ticketPrice", ticketPrice);
                    buyTicketIntent.putExtra("getsOn", getsOn);
                    buyTicketIntent.putExtra("getsOff", getsOff);

                    mContext.startActivity(buyTicketIntent);
                } catch (InterruptedException | ExecutionException | JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        TextView journeyNameTV = (TextView)convertView.findViewById(R.id.bookingsJourneyNameTV);
        TextView dueDateTV = (TextView)convertView.findViewById(R.id.bookingsDueDateTV);
        TextView seatTV = (TextView)convertView.findViewById(R.id.bookingsSeatTV);
        TextView getsOnTV = (TextView)convertView.findViewById(R.id.bookingsGetsOnTV);
        TextView getsOffTV = (TextView)convertView.findViewById(R.id.bookingsGetsOffTV);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        journeyNameTV.setText(bookingsList.get(position).getServiceName());
        dueDateTV.setText(dateFormat.format(bookingsList.get(position).getDueDate()));
        seatTV.setText(bookingsList.get(position).getSeat().toString());
        getsOnTV.setText(bookingsList.get(position).getGetsOn());
        getsOffTV.setText(bookingsList.get(position).getGetsOff());
    }

    @Override
    public int getCount() {
        return bookingsList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
