package upm.softwaredesign.uber;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import upm.softwaredesign.uber.utilities.HttpManager;

public class LoginActivity extends AppCompatActivity {
    EditText etaccount,etpassword;
    public static String login_account,login_password;
    public static String servertoken_whenlogin;
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
                //TODO: send the json to the server and to check
                HttpManager httpManager = new HttpManager(LoginActivity.this);
                httpManager.sendLogin();
                servertoken_whenlogin = HttpManager.requestlogin;

                // can be use when communicate with the server successfully
             //   if(servertoken_whenlogin.equals(Signup2Fragment.localtoken_whenregister))
             //   {

             //       Intent i = new Intent(LoginActivity.this, MainActivity.class);
             //       startActivity(i);
             //   }

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
