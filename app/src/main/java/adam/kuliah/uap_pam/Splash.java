package adam.kuliah.uap_pam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity implements View.OnClickListener {
    private Button btDaftar, btMasuk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        btDaftar = findViewById(R.id.btDaftar);
        btMasuk = findViewById(R.id.btMasuk);

        btDaftar.setOnClickListener(this);
        btMasuk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btDaftar) {
            Intent intent = new Intent(Splash.this, Register.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(Splash.this, Login.class);
            startActivity(intent);
        }
    }
}