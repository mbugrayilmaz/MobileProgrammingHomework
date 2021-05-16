package tr.edu.yildiz.mustafabugrayilmaz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<User> userList=new ArrayList<>();

    private EditText emailText;
    private EditText passwordText;
    private Button loginButton;
    private Button signupButton;

    private int loginAttempts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeVariables();

        setUserList();

        System.out.println(userList);
    }

    private void setUserList() {
        File users = new File(getFilesDir(), "users");

        if (!users.exists()) {
            userList = new ArrayList<>();
        } else {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(users));

                userList = (ArrayList<User>) objectInputStream.readObject();

                objectInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeVariables() {
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);
    }

    public void loginListener(View view) {
        if (loginAttempts >= 2) {
            Toast.makeText(this, "Failed login 3 times, please sign up", Toast.LENGTH_LONG).show();
            disableOnThreeAttempts();
        }

        boolean emailExists = false;

        if (emailText.getText().toString().isEmpty() || passwordText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill in the fields", Toast.LENGTH_SHORT).show();

        } else {
            for (int i = 0; i < userList.size() && !emailExists; i++) {
                User currUser = userList.get(i);

                if (currUser.getEmail().equals(emailText.getText().toString())) {
                    emailExists = true;

                    if (currUser.getPassword().equals(passwordText.getText().toString())) {
                        Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();

                        Intent loginIntent = new Intent(this, MenuActivity.class);

                        loginIntent.putExtra("loggedUser", (Parcelable) currUser);

                        clearText();

                        startActivity(loginIntent);
                    } else {
                        Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show();
                        loginAttempts++;
                    }
                }
            }

            if (!emailExists) {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                loginAttempts++;
            }
        }
    }

    public void signupListener(View view) {
        Intent signupIntent = new Intent(view.getContext(), SignupActivity.class);

        signupIntent.putExtra("userList", userList);

        startActivity(signupIntent);

        clearText();
    }

    private void disableOnThreeAttempts() {
        emailText.setEnabled(false);
        passwordText.setEnabled(false);
        loginButton.setEnabled(false);
    }

    private void clearText() {
        emailText.getText().clear();
        passwordText.getText().clear();
    }
}