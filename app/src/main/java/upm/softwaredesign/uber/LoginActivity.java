package upm.softwaredesign.uber;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import upm.softwaredesign.uber.utilities.HttpManager;

public class LoginActivity extends AppCompatActivity {

    public static LoginActivity loginInstance;
    private String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginInstance = this;

        loadToken();
        if(!token.equals(""))
            startActivity(new Intent(this, MainActivity.class));
    }

    public void loginClicked(View view){
        String email = ((TextView)findViewById(R.id.login_email)).getText().toString();
        String password = ((TextView)findViewById(R.id.login_password)).getText().toString();
        if(!validEmail(email)){
            Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        HttpManager httpManager = new HttpManager(LoginActivity.this);
        httpManager.sendLogin(email, password);
    }

    public void signUpClicked(View view){
        startActivity(new Intent(this, SignUpActivity.class));
    }

    // load the token from the device
    public void loadToken(){
        SharedPreferences mPrefs = getSharedPreferences("data", 0);
        this.token = mPrefs.getString("token", "");
        HttpManager.token = this.token;
        System.out.println("loadingToken = "+this.token);
    }

    // save the token to the device
    public void saveToken(String token){
        this.token = token;
        SharedPreferences mPrefs = getSharedPreferences("data", 0);
        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putString("token", token).commit();
        System.out.println("savingToken = "+this.token);
    }

    boolean validEmail(CharSequence email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
