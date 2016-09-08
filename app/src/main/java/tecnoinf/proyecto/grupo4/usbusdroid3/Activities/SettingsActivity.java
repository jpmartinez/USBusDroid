package tecnoinf.proyecto.grupo4.usbusdroid3.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import tecnoinf.proyecto.grupo4.usbusdroid3.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final EditText serverET = (EditText) findViewById(R.id.settingsServerET);
        final EditText portET = (EditText) findViewById(R.id.settingsPortET);
        final EditText tenantIdET = (EditText) findViewById(R.id.settingsTenantIdET);
        Button applyBtn = (Button) findViewById(R.id.settingsApplyBtn);

        assert serverET != null;
        assert portET != null;
        assert tenantIdET != null;
        assert applyBtn != null;

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(serverET.getText().toString().isEmpty()
                        && portET.getText().toString().isEmpty()
                        && tenantIdET.getText().toString().isEmpty()) {
                    Toast.makeText(getBaseContext(), "No se ingresó ningún valor", Toast.LENGTH_LONG).show();
                } else {
                    String serverIP = serverET.getText().toString();
                    String port = portET.getText().toString();
                    String tenantId = tenantIdET.getText().toString();

                    SharedPreferences sharedPreferences = getSharedPreferences("USBusData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    if(!serverIP.isEmpty()) {
                        editor.putString("serverIP", serverIP);
                    }
                    if(!port.isEmpty()) {
                        editor.putString("port", port);
                    }
                    if(!tenantId.isEmpty()) {
                        editor.putString("tenantId", tenantId);
                    }

                    if(!serverIP.isEmpty() || !port.isEmpty() || !tenantId.isEmpty()) {
                        editor.apply();
                        Toast.makeText(getBaseContext(), "Datos ingresados correctamente", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getBaseContext(), "No se ingresaron datos", Toast.LENGTH_LONG).show();
                    }

                    Intent loginIntent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(loginIntent);
                }
            }
        });
    }
}
