package com.example.ac_twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUp extends AppCompatActivity implements View.OnClickListener {


    private EditText edtEmail,edtPass,edtUser;
    private Button btnSignup,btnLogin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setTitle("TwitterClone - SignUp");

        edtEmail = findViewById(R.id.edtEmail);
        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        edtPass.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    onClick(btnSignup);
                }
                return false;
            }
        });

        btnLogin = findViewById(R.id.btnLogIn);
        btnSignup = findViewById(R.id.btnSignUp);


        if(ParseUser.getCurrentUser() !=null){
            ParseUser.getCurrentUser().logOut();
        }



        btnSignup.setOnClickListener(this);
        btnLogin.setOnClickListener(this);






    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){


            case R.id.btnLogIn:
                Intent intent = new Intent(SignUp.this, LoginActivity.class);
                startActivity(intent);
                break;


            case R.id.btnSignUp:
                if(edtPass.getText().toString().equals("") || edtUser.getText().toString().equals("") || edtEmail.getText().toString().equals("")){
                    FancyToast.makeText(SignUp.this, "Empty values are not allowded", Toast.LENGTH_SHORT, FancyToast.INFO, true).show();
                }else{
                    final ParseUser parseUser = new ParseUser();
                    parseUser.setEmail(edtEmail.getText().toString());
                    parseUser.setPassword(edtPass.getText().toString());
                    parseUser.setUsername(edtUser.getText().toString());

                    parseUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null){
                                FancyToast.makeText(SignUp.this, parseUser.getUsername() + " is Signed up", Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                                Intent intent1 = new Intent(SignUp.this,Twitter.class);
                                startActivity(intent1);
                                finish();
                            }else {
                                FancyToast.makeText(SignUp.this, "There is  an error " + e.getMessage(), Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();

                            }
                        }
                    });



                }
                break;
        }

    }

    public void rootLayoutTapped(View view) {
        try {

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        } catch ( Exception e){
            e.printStackTrace();

        }
    }


}