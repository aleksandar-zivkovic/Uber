package upm.softwaredesign.uber;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import upm.softwaredesign.uber.utilities.HttpManager;

public class SignUpActivity extends AppCompatActivity {
    public static String account,pw1,pw2;
    EditText email,pw,pwr;
    Button goback,finishsignup;
    EditText etfn,etln,etphone;
    int flag=0;
    public static String firstname,lastname,phonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //added onclick listener for back button
        goback = (Button)findViewById(R.id.signup_back_button);
        finishsignup = (Button)findViewById(R.id.signup_signup_button) ;
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpActivity.this.finish();
            }
        });
        email = (EditText)findViewById(R.id.signup_fragment_email);
        pw = (EditText)findViewById(R.id.signup_fragment_password);
        pwr = (EditText)findViewById(R.id.signup_fragment_retype_password) ;
        etfn = (EditText)findViewById(R.id.signup_fragment_firstname);
        etln = (EditText)findViewById(R.id.signup_fragment_lastname);
        etphone = (EditText)findViewById(R.id.signup_fragment_phone);
        finishsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=0;
                account = email.getText().toString();
                pw1=pw.getText().toString();
                pw2=pwr.getText().toString();
                firstname = etfn.getText().toString();
                lastname = etln.getText().toString();
                phonenumber = etphone.getText().toString();
                if((pw1.equals("")&&pw2.equals(""))||(account.equals(""))){
                    Toast toast = Toast.makeText(SignUpActivity.this, "The account or password cannot be null!",Toast.LENGTH_LONG);
                    toast.show();
                    flag=1;
                }
                else if((!pw1.equals(pw2))&&(!account.equals(""))){
                    flag=1;
                    Toast toast = Toast.makeText(SignUpActivity.this, "Your passwords are not the same. Please check them!",Toast.LENGTH_LONG);
                    toast.show();
                }
                if(flag==0){
                    //TODO: send this json to the server.
                    HttpManager httpManager = new HttpManager(SignUpActivity.this);
                    httpManager.sendRegisteration();

                    Intent intent = new Intent();
                    intent.setClass(SignUpActivity.this,LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(SignUpActivity.this,httpManager.RegisterStatusJson,Toast.LENGTH_LONG).show();
                }

            }
        });


        //added signup 1 fragment
       //FragmentManager fragmentManager = getSupportFragmentManager();
        //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
       // Signup1Fragment fragment = new Signup1Fragment();
        //fragmentTransaction.add(R.id.signup_frame, fragment);
        //fragmentTransaction.commit();

    }


}
