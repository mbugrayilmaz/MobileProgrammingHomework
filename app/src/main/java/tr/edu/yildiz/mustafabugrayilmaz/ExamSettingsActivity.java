package tr.edu.yildiz.mustafabugrayilmaz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ExamSettingsActivity extends AppCompatActivity {

    private User loggedUser;

    private EditText timeEditText;
    private EditText pointEditText;
    private EditText difficultyEditText;

    private String time;
    private int point;
    private int difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_settings);

        initializeViews();

        loggedUser = getIntent().getParcelableExtra("loggedUser");

        loadSettings();
    }

    public void loadSettings(){
        SharedPreferences sharedPreferences=getSharedPreferences(loggedUser.getUsername(), Context.MODE_PRIVATE);

        time= sharedPreferences.getString("time","0");

        point=sharedPreferences.getInt("point",20);

        difficulty=sharedPreferences.getInt("difficulty",4);

        timeEditText.setText(time);
        pointEditText.setText(String.valueOf(point));
        difficultyEditText.setText(String.valueOf(difficulty));
    }

    public void saveSettings(View view) {
        if (!isValid()){
            return;
        }

        SharedPreferences.Editor editor=getSharedPreferences(loggedUser.getUsername(), Context.MODE_PRIVATE).edit();

        editor.putString("time",timeEditText.getText().toString());
        editor.putInt("point",Integer.parseInt(pointEditText.getText().toString()));
        editor.putInt("difficulty",Integer.parseInt(difficultyEditText.getText().toString()));

        editor.apply();

        finish();
    }

    private void initializeViews() {
        timeEditText = findViewById(R.id.timeEditText);
        pointEditText = findViewById(R.id.pointEditText);
        difficultyEditText = findViewById(R.id.difficultyEditText);
    }

    private boolean isValid(){
        int difficulty=Integer.parseInt(difficultyEditText.getText().toString());

        if (Integer.parseInt(pointEditText.getText().toString())>100){
            Toast.makeText(this,"Point cannot be bigger than 100",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (difficulty>5||difficulty<2){
            Toast.makeText(this,"Difficulty must be between 2 and 5",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}