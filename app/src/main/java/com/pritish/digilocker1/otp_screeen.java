package com.pritish.digilocker1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import javax.security.auth.callback.Callback;

public class otp_screeen extends AppCompatActivity {
    public String number_entered_by_user, code_by_system;
    public CardView Next;
    public TextView Resend;
    public PinView otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_screeen);
        Intent intent = getIntent();
        number_entered_by_user = intent.getStringExtra("number");
        Next = findViewById(R.id.Next);
        Resend = findViewById(R.id.Resend);
        Resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resend_otp(number_entered_by_user);
            }
        });
        otp = findViewById(R.id.pinview);
        send_code_to_user(number_entered_by_user);
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_code();
            }
        });
    }

    private void resend_otp(String number_entered_by_user) {
        send_code_to_user(number_entered_by_user);
    }

    private void check_code() {
        String user_entered_otp = otp.getText().toString();
        if (user_entered_otp.isEmpty() || user_entered_otp.length() < 6) {
            Toast.makeText(this, "Wrong OTP", Toast.LENGTH_SHORT).show();
            return;
        }
        finish_everything(user_entered_otp);
    }

    private void send_code_to_user(String number_entered_by_user) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + number_entered_by_user,
                60,
                TimeUnit.SECONDS,
                this,
                mCallback
        );
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback =new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            code_by_system=s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String Code=phoneAuthCredential.getSmsCode();
            if(Code != null){
                finish_everything(Code);

            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(otp_screeen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void finish_everything(String Code) {
        otp.setText(Code);
        PhoneAuthCredential credential =PhoneAuthProvider.getCredential(code_by_system, Code);
        sign_in(credential);
    }

    private void sign_in(PhoneAuthCredential credential) {
        FirebaseAuth auth =FirebaseAuth.getInstance();
        auth.signInWithCredential(credential).addOnCompleteListener(otp_screeen.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(otp_screeen.this, "User signd Sucessfully",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(otp_screeen.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
