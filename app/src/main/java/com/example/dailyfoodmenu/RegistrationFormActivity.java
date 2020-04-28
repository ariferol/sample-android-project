package com.example.dailyfoodmenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationFormActivity extends AppCompatActivity {
    EditText ed_username,ed_email,ed_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form);
        ed_email = findViewById(R.id.ed_email);
        ed_username = findViewById(R.id.ed_username);
        ed_password = findViewById(R.id.ed_password);
    }

    public void moveToLogin(View view) {
        startActivity(new Intent(getApplicationContext(),LoginFormActivity.class));
        finish();
    }

    public void Register(View view) {
        if(ed_username.getText().toString().equals("")){
            Toast.makeText(this, "Kullanıcı adını girin.", Toast.LENGTH_SHORT).show();
        }
        else if(ed_email.getText().toString().equals("")){
            Toast.makeText(this, "E-Posta bilgisini girin.", Toast.LENGTH_SHORT).show();
        }
        else if(ed_password.getText().toString().equals("")){
            Toast.makeText(this, "Şifre bilgisini girin.", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Kayıt işlemi başarıyla tamamlandı.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
    }
}
