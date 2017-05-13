package upm.softwaredesign.uber;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import upm.softwaredesign.uber.utilities.HttpManager;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class ProfileActivity extends AppCompatActivity {

    public static ProfileActivity profileInstance;
    private static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileInstance = this;

        if(!HttpManager.token.equals("")) new HttpManager(this).requestUserInfo();

        //added onclick listener for save button
        View saveButton = findViewById(R.id.edit_profile_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "Your profile is saved.", Toast.LENGTH_SHORT).show();
            }
        });

        //added onclick listener for back button
        View backButton = findViewById(R.id.edit_profile_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileActivity.this.finish();
            }
        });

        //added onclick listenser for change profile picture button
        View changeButton = findViewById(R.id.edit_profile_change_picture);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case PICK_IMAGE:
                if(resultCode == RESULT_OK){

                    Uri selectedImage = imageReturnedIntent.getData();

                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();


                    Bitmap imageBitmap = BitmapFactory.decodeFile(filePath);

                    ImageView imageView = (ImageView)findViewById(R.id.edit_profile_image_view);
                    imageView.setImageBitmap(imageBitmap);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    //Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));

                    Log.v("TEST", filePath);
                    //Now do whatever processing you want to do on it.
                }
        }
    }

    public void showUserInfo(String firstName, String lastName, String email) {

        EditText profile_name = (EditText)findViewById(R.id.edit_profile_name);
        profile_name.setText(firstName);

        EditText profile_lastname = (EditText)findViewById(R.id.edit_profile_last_name);
        profile_lastname.setText(lastName);

        EditText profile_email = (EditText)findViewById(R.id.edit_profile_email);
        profile_email.setText(email);

    }
}
