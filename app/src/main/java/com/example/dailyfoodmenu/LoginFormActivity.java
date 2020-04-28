package com.example.dailyfoodmenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginFormActivity extends AppCompatActivity {
    EditText txtUserName,txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);
        txtUserName = findViewById(R.id.txt_username);
        txtPassword = findViewById(R.id.txt_password);
    }

    public void Login(View view) {

        if(txtUserName.getText().toString().equals("")){
            Toast.makeText(this, "Kullanıcı adını giriniz.", Toast.LENGTH_SHORT).show();
        }
        else if(txtPassword.getText().toString().equals("")){
            Toast.makeText(this, "Şifreyi giriniz.", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Kullanıcı adı ve şifre başarıyla doğrulandı.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
    }

    public void moveToRegistrationFormActivity(View view) {
        startActivity(new Intent(getApplicationContext(),RegistrationFormActivity.class));
        finish();
    }
}
