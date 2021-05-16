package tr.edu.yildiz.mustafabugrayilmaz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class CreateExamActivity extends AppCompatActivity {

    private User loggedUser;

    private ArrayList<Question> questionList;
    private ArrayList<Question> selectedQuestionList = new ArrayList<>();

    private String time;
    private int point;
    private int difficulty;

    private EditText examTitleEditText;
    private EditText examTimeEditText;
    private EditText examPointEditText;
    private EditText examDifficultyEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exam);

        loggedUser = getIntent().getParcelableExtra("loggedUser");

        initializeViews();

        setQuestionList();

        loadSettings();

        RecyclerView recyclerView = findViewById(R.id.examRecyclerView);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ExamAdapter examAdapter = new ExamAdapter(this, questionList, selectedQuestionList, loggedUser, difficulty);

        recyclerView.setAdapter(examAdapter);
    }

    public void saveExam(View view) {
    }

    private void loadSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences(loggedUser.getUsername(), Context.MODE_PRIVATE);

        time = sharedPreferences.getString("time", "0");
        point = sharedPreferences.getInt("point", 0);
        difficulty = sharedPreferences.getInt("difficulty", 0);

        examTimeEditText.setText(time);
        examPointEditText.setText(String.valueOf(point));
        examDifficultyEditText.setText(String.valueOf(difficulty));
    }

    private void initializeViews() {
        examTitleEditText = findViewById(R.id.examTitleEditText);
        examTimeEditText = findViewById(R.id.examTimeEditText);
        examPointEditText = findViewById(R.id.examPointEditText);
        examDifficultyEditText = findViewById(R.id.examDifficultyEditText);
    }

    public void setQuestionList() {
        File questions = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), loggedUser.getUsername() + "Questions");

        if (!questions.exists()) {
            questionList = new ArrayList<>();
        } else {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(questions))) {
                questionList = (ArrayList<Question>) objectInputStream.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isValid() {
        int difficulty = Integer.parseInt(examDifficultyEditText.getText().toString());

        if (Integer.parseInt(examPointEditText.getText().toString()) > 100) {
            Toast.makeText(this, "Point cannot be bigger than 100", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (difficulty > 5 || difficulty < 2) {
            Toast.makeText(this, "Difficulty must be between 2 and 5", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selectedQuestionList.size() == 0) {
            Toast.makeText(this, "Difficulty must be between 2 and 5", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}