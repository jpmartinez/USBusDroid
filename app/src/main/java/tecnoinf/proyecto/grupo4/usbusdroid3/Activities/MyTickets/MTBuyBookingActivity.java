package tecnoinf.proyecto.grupo4.usbusdroid3.Activities.MyTickets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutionException;

import tecnoinf.proyecto.grupo4.usbusdroid3.Helpers.PayPalConfig;
import tecnoinf.proyecto.grupo4.usbusdroid3.Helpers.RestCallAsync;
import tecnoinf.proyecto.grupo4.usbusdroid3.Models.TicketStatus;
import tecnoinf.proyecto.grupo4.usbusdroid3.R;

public class MTBuyBookingActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton buttonPay;
    private String paymentAmount;
    private String token;
    private String username;
    private JSONObject journey;
    private JSONObject newTicket;
    private String buyTicketRest;
    private Intent father;
    private JSONObject ticketData;
    private String bookingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ntconfirmation);
        father = getIntent();
        try {
            journey = new JSONObject(father.getStringExtra("journey"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SharedPreferences sharedPreferences = getSharedPreferences("USBusData", Context.MODE_PRIVATE);
        buyTicketRest = getString(R.string.URLTickets, getString(R.string.URL_REST_API), getString(R.string.tenantId));
        token = sharedPreferences.getString("token", "");
        username = sharedPreferences.getString("username", "");

        String selectedSeat = father.getStringExtra("seat");
        //TODO: tomar el bookingId para luego cambiarle el status
        bookingId = father.getStringExtra("bookingId");
        paymentAmount = father.getStringExtra("ticketPrice");
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            //journey = new JSONObject(father.getStringExtra("journey"));

            TextView ticketOriginTV = (TextView) findViewById(R.id.ticketOriginTV);
            TextView ticketDestinationTV = (TextView) findViewById(R.id.ticketDestinationTV);
            TextView ticketDateTV = (TextView) findViewById(R.id.ticketDateTV);
            TextView ticketBusIdTV = (TextView) findViewById(R.id.ticketBusIdTV);
            TextView ticketSeatTV = (TextView) findViewById(R.id.ticketSeatTV);
            TextView ticketCostTV = (TextView) findViewById(R.id.ticketCostTV);
            assert ticketCostTV != null;
            ticketCostTV.setText(paymentAmount);
            assert ticketSeatTV != null;
            ticketSeatTV.setText(selectedSeat);
            assert ticketOriginTV != null;
            ticketOriginTV.setText(father.getStringExtra("getsOn"));
            assert ticketDestinationTV != null;
            ticketDestinationTV.setText(father.getStringExtra("getsOff"));
            assert ticketDateTV != null;
            ticketDateTV.setText(dateFormat.format(journey.get("date")));
            assert ticketBusIdTV != null;
            ticketBusIdTV.setText(journey.get("busNumber").toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        buttonPay = (ImageButton) findViewById(R.id.paypalPayBT);
        assert buttonPay != null;
        buttonPay.setOnClickListener(this);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
    }

    @Override
    public void onClick(View v) {
        newTicket = new JSONObject();
        try {
            newTicket.put("tenantId", getString(R.string.tenantId));
            newTicket.put("journeyId", journey.get("id"));
            newTicket.put("hasCombination", false);
            newTicket.put("combination", null);
            newTicket.put("combinationId", null);
            newTicket.put("amount", paymentAmount);
            newTicket.put("getOnStopName", father.getStringExtra("getsOn"));
            newTicket.put("getOffStopName", father.getStringExtra("getsOff"));
            newTicket.put("passengerName", username);
            newTicket.put("seat", father.getStringExtra("seat"));
            newTicket.put("closed", true);
            newTicket.put("status", TicketStatus.UNUSED);
            newTicket.put("routeId", journey.getJSONObject("service").getJSONObject("route").get("id"));
            newTicket.put("branchId", 0);
            newTicket.put("windowId", 0);

            AsyncTask<Void, Void, JSONObject> ticketResult = new RestCallAsync(getApplicationContext(), buyTicketRest, "POST", newTicket, token).execute();
            ticketData =  ticketResult.get();

        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        getPayment();
    }

    public static final int PAYPAL_REQUEST_CODE = 123;


    //Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    private void getPayment() {
        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(
                (new BigDecimal(String.valueOf(paymentAmount))).divide(new BigDecimal(32), BigDecimal.ROUND_UP).setScale(0, RoundingMode.UP),
                "USD",
                "Simplified Coding Fee",
                PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the result is from paypal
        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.i("paymentExample", paymentDetails);

                        //Starting a new activity for the payment details and also putting the payment details with intent
                        startActivity(new Intent(this, MTBuyBookingResultActivity.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", paymentAmount)
                                .putExtra("journey", journey.toString())
                                .putExtra("bookingId", bookingId)
                                .putExtra("ticket", ticketData.getString("data")));

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
                //TODO: llamar a la policía, y cancelar el ticket (http delete)
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

}
