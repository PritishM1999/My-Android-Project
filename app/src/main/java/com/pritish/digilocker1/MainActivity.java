package com.pritish.digilocker1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {
    public CardView continuebutton = findViewById(R.id.login);
    public Button backbutton;
    public EditText phone_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backbutton=findViewById(R.id.back);
        phone_input=findViewById(R.id.phone);

        backbutton.setOnClickListener(view -> {
        });
        continuebutton.setOnClickListener(view -> {
            Intent intent =new Intent(MainActivity.this,otp_screeen.class);
            String number=phone_input.getText().toString();
            intent.putExtra("number",number);
            startActivity(intent);

        });
    }
}