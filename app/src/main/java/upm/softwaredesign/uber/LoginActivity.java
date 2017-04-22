package upm.softwaredesign.uber;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import upm.softwaredesign.uber.utilities.HttpManager;

import static upm.softwaredesign.uber.utilities.HttpManager.LoginStatus;
import static upm.softwaredesign.uber.utilities.HttpManager.RegisterStatusJson;
import static upm.softwaredesign.uber.utilities.HttpManager.loginStatusJson;

public class LoginActivity extends AppCompatActivity {
    EditText etaccount,etpassword;
    public static String login_account,login_password;
    int temp=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //added onclick listener for profile name
        View loginButton = findViewById(R.id.login_login_button);
        etaccount = (EditText)findViewById(R.id.login_email);
        etpassword = (EditText)findViewById(R.id.login_password);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_account = etaccount.getText().toString();
                login_password = etpassword.getText().toString();
                HttpManager httpManager = new HttpManager(LoginActivity.this);
                httpManager.sendLogin();

                while(LoginStatus==0)
                {
                    temp=1;
                }
                if(LoginStatus==200){
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    LoginStatus=0;
                }else if(LoginStatus==400){
                    Toast.makeText(LoginActivity.this,"Invalid credentials",Toast.LENGTH_LONG).show();
                    LoginStatus=0;
                }
                //TODO:Save token and login in without inputing account



            }
        });

        //added onclick listener for sign up button
        View signUpButton = findViewById(R.id.login_signup_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });
    }
}
