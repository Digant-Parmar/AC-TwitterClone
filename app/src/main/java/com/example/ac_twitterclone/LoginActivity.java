package com.example.ac_twitterclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.accessibility.AccessibilityViewCommand;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.Set;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edtLoginEmail,edtLoginPass;
    private Button btnLoginLogin, btnLoginSignup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtLoginEmail = findViewById(R.id.edtLoginEmail);
        edtLoginPass = findViewById(R.id.edtLoginPass);

        btnLoginLogin = findViewById(R.id.btnLoginLogin);
        btnLoginSignup = findViewById(R.id.btnLoginSignUp);

        btnLoginSignup.setOnClickListener(this);
        btnLoginLogin.setOnClickListener(this);


        edtLoginPass.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                    onClick(btnLoginLogin);
                }
                return false;
            }
        });

        setTitle("Twitter-Login");

        if(ParseUser.getCurrentUser()!=null){
            ParseUser.getCurrentUser().logOut();
        }




    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){


            case R.id.btnLoginSignUp:
                Intent intent = new Intent(this, SignUp.class);
                startActivity(intent);
                break;


            case R.id.btnLoginLogin:
                if(edtLoginPass.getText().toString().equals("") || edtLoginEmail.getText().toString().equals("")){
                    FancyToast.makeText(this, "Empty values are not allowded", Toast.LENGTH_SHORT, FancyToast.INFO, true).show();
                }else{
                    final ParseUser parseUser = new ParseUser();
                    parseUser.logInInBackground(edtLoginEmail.getText().toString(), edtLoginPass.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if(user!=null && e ==null){
                                FancyToast.makeText(LoginActivity.this,user.getUsername()+" is Logged in",
                                        Toast.LENGTH_SHORT,
                                        FancyToast.SUCCESS,
                                        true).show();
                                Intent intent = new Intent(LoginActivity.this,Twitter.class);
                                startActivity(intent);
                                finish();
                            }

                        }
                    });


                }
                break;
        }

    }
}