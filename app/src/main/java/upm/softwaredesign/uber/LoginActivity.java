package upm.softwaredesign.uber;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import upm.softwaredesign.uber.fragments.Signup1Fragment;

public class LoginActivity extends AppCompatActivity {
    EditText etaccount,etpassword;
    static String login_account,login_password,login_json;
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
                login_json = "{"+"\"email\""+":\""+ login_account+"\","+
                        "\"password\""+":\""+login_password+"\","+
                        "}";
                //TODO: send the json to the server and to check

                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
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
