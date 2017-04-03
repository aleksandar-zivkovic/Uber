package upm.softwaredesign.uber;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import upm.softwaredesign.uber.fragments.Signup1Fragment;
import upm.softwaredesign.uber.fragments.Signup2Fragment;

public class SignUpActivity extends AppCompatActivity implements Signup1Fragment.OnFragmentInteractionListener,
        Signup2Fragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //added onclick listener for back button
        View backButton = findViewById(R.id.signup_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpActivity.this.finish();
            }
        });


        //added signup 1 fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Signup1Fragment fragment = new Signup1Fragment();
        fragmentTransaction.add(R.id.signup_frame, fragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

}
