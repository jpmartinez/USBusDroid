package tecnoinf.proyecto.grupo4.usbusdroid3.Activities.MyTickets;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONException;
import org.json.JSONObject;

import tecnoinf.proyecto.grupo4.usbusdroid3.Helpers.QRCodeEncoder;
import tecnoinf.proyecto.grupo4.usbusdroid3.Models.TicketShort;
import tecnoinf.proyecto.grupo4.usbusdroid3.R;

public class MTShowTicketActivity extends AppCompatActivity {

    private Integer WIDTH;
    private Integer HEIGHT;
    private String ticketIdEncrypted;
    private ImageButton qrImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mtshow_ticket);
        Intent father = getIntent();
        try {
            JSONObject ticket = new JSONObject(father.getStringExtra("ticket"));
            JSONObject qrTicket = new JSONObject();
            qrTicket.put("tenantId", ticket.get("tenantId"));
            qrTicket.put("id", ticket.get("id"));

            //TODO: aqui muestra el QR (y alguna otra bobada)
            ticketIdEncrypted = qrTicket.toString();
            qrImage = (ImageButton) findViewById(R.id.qrCodeBtn);

            Bitmap bitmap = encodeAsBitmap(ticketIdEncrypted);
            assert qrImage != null;
            qrImage.setImageBitmap(bitmap);

        } catch (WriterException | JSONException e) {
            e.printStackTrace();
        }
    }

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;

        //Find screen size
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        WIDTH = point.x;
        HEIGHT = point.y - 400;
        int smallerDimension = WIDTH < HEIGHT ? WIDTH : HEIGHT;
        smallerDimension = smallerDimension * 3/4;

        //Encode with a QR Code image
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(ticketIdEncrypted,
                null,
                BarcodeFormat.QR_CODE.toString(),
                smallerDimension);
        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
//            ImageView myImage = (ImageView) findViewById(R.id.imageView1);
            qrImage.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }

        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, HEIGHT, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, WIDTH, 0, 0, w, h);
        return bitmap;
    }
}
