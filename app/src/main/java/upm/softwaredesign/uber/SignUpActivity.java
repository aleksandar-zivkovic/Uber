package upm.softwaredesign.uber;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import upm.softwaredesign.uber.utilities.HttpManager;

public class SignUpActivity extends AppCompatActivity {

    private String email, password1, password2, firstName, lastName, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ((FloatingActionButton)findViewById(R.id.signup_back_button_bar)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void clickedBack(View v){
        finish();
    }

    public void clickedSignUp(View view){
        email = ((EditText)findViewById(R.id.signup_fragment_email)).getText().toString();
        password1 = ((EditText)findViewById(R.id.signup_fragment_password)).getText().toString();
        password2 = ((EditText)findViewById(R.id.signup_fragment_retype_password)).getText().toString() ;
        firstName = ((EditText)findViewById(R.id.signup_fragment_firstname)).getText().toString();
        lastName = ((EditText)findViewById(R.id.signup_fragment_lastname)).getText().toString();
        phone = ((EditText)findViewById(R.id.signup_fragment_phone)).getText().toString();

        // if all the fields are valid send the request
        if(allFieldsValid())
            new HttpManager(this).sendRegistration(email, password1, firstName, lastName, phone);

        /*Intent intent = new Intent();
        intent.setClass(SignUpActivity.this,LoginActivity.class);
        startActivity(intent);
        Toast.makeText(SignUpActivity.this,RegisterStatusJson,Toast.LENGTH_LONG).show();
        RegisterStatusJson = "";*/
    }

    // check for all fields and show appropriate error message
    public boolean allFieldsValid() {
        String errorMessage = null;
        if(!isValidEmail(email)) errorMessage = "Please enter a valid email address";
        else if(!isValidPassword(password1)) errorMessage = "Enter an alphanumerical (case sensitive) password of at least 6 chars";
        else if(!password1.equals(password2)) errorMessage = "Your passwords are not the same, Please check them";

        if(errorMessage != null){
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    boolean isValidEmail(CharSequence email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // alphanumerical (case sensitive) password of at least 6 characters
    boolean isValidPassword(String password) {
        if(password.length() < 6) return false;
        for(int i = 0; i < password.length(); i++){
            char c = password.charAt(i);
            if((c < 'a' || c > 'z') && (c < 'A' || c > 'Z') && (c < '0' || c > '9')) return false;
        }
        return true;
    }
}
