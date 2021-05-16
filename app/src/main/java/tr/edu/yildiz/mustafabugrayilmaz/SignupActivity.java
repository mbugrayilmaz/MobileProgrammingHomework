package tr.edu.yildiz.mustafabugrayilmaz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity {

    private ArrayList<User> userList;

    private final int ICON_REQUEST_CODE = 1;

    private Uri profilePicture = Uri.parse("android.resource://tr.edu.yildiz.mustafabugrayilmaz/" + R.drawable.default_profile_picture);

    private ImageView signupIconImage;
    private EditText signupFirstNameText;
    private EditText signupLastNameText;
    private EditText signupPhoneText;
    private EditText signupEmailText;
    private EditText signupPasswordText;
    private EditText signupReenterPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initializeViews();

        signupPhoneText.addTextChangedListener(new PhoneNumberFormattingTextWatcher("TR") {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);

                System.out.println("\"" + s + "\" " + start + " " + before + " " + count);

                if (signupPhoneText.getText().length() < 3) {
                    signupPhoneText.setText("+90");
                    signupPhoneText.setSelection(3);
                } else if (signupPhoneText.getText().toString().replaceAll("\\s+", "").length() > 13) {
                    signupPhoneText.setText(signupPhoneText.getText().toString().toCharArray(), 0, 13);
                    signupPhoneText.setSelection(13);
                }
            }
        });

        userList = (ArrayList<User>) getIntent().getSerializableExtra("userList");

        System.out.println(userList);
    }

    private void initializeViews() {
        signupIconImage = findViewById(R.id.signupIconImage);
        signupFirstNameText = findViewById(R.id.signupFirstNameText);
        signupLastNameText = findViewById(R.id.signupLastNameText);
        signupPhoneText = findViewById(R.id.signupPhoneText);
        signupEmailText = findViewById(R.id.signupEmailText);
        signupPasswordText = findViewById(R.id.signupPasswordText);
        signupReenterPasswordText = findViewById(R.id.signupReenterPasswordText);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ICON_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                profilePicture = data.getData();
                signupIconImage.setImageURI(profilePicture);

            }
        }
    }

    public void chooseIcon(View view) {
        Intent choose = new Intent(Intent.ACTION_GET_CONTENT);

        choose.setType("image/*");

        startActivityForResult(choose, ICON_REQUEST_CODE);
    }

    public void signup(View view) {
        if (!isValid()) {
            return;
        }

        ContentResolver contentResolver = getContentResolver();

        String userName = signupFirstNameText.getText().toString() + signupLastNameText.getText().toString();

        Uri iconUri;
        String iconFileName = userName + "Icon";

        iconUri = Tools.saveImageToInternal(this, profilePicture, iconFileName);

        try {
            signupFirstNameText.setForeground(new BitmapDrawable(getResources(), BitmapFactory.decodeStream(contentResolver.openInputStream(iconUri))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        User user = new User(
                signupFirstNameText.getText().toString(),
                signupLastNameText.getText().toString(),
                signupPhoneText.getText().toString(),
                signupEmailText.getText().toString(),
                signupPasswordText.getText().toString(),
                iconUri);

        ArrayList<User> userList = (ArrayList<User>) getIntent().getSerializableExtra("userList");

        userList.add(user);

        File users = new File(getFilesDir(), "users");

        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(users, false))) {
            objectOutputStream.writeObject(userList);

            Intent menuIntent = new Intent(this, MenuActivity.class);

            menuIntent.putExtra("loggedUser", (Parcelable) user);

            startActivity(menuIntent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isValid() {
        if (
                signupFirstNameText.getText().toString().isEmpty() ||
                        signupLastNameText.getText().toString().isEmpty() ||
                        signupPhoneText.getText().toString().isEmpty() ||
                        signupEmailText.getText().toString().isEmpty() ||
                        signupPasswordText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill out all the fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (signupPhoneText.getText().length() < 17) {
            Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!signupPasswordText.getText().toString().equals(signupReenterPasswordText.getText().toString())) {
            Toast.makeText(this, "Password fields do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (usernameAlreadyExists()) {
            Toast.makeText(this, "There is already a user with this name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (emailAlreadyExists()) {
            Toast.makeText(this, "There is already a user with this email", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean usernameAlreadyExists() {
        for (User user : userList) {
            if ((signupFirstNameText.getText().toString() + signupLastNameText.getText().toString()).equals(user.getUsername())) {
                return true;
            }
        }

        return false;
    }

    private boolean emailAlreadyExists() {
        for (User user : userList) {
            if (signupEmailText.getText().toString().equals(user.getEmail())) {
                return true;
            }
        }

        return false;
    }
}