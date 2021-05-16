package tr.edu.yildiz.mustafabugrayilmaz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class ListQuestionsActivity extends AppCompatActivity {

    private ArrayList<Question> questionList = new ArrayList<>();
    private User loggedUser;

    private QuestionAdapter questionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_questions);

        loggedUser = getIntent().getParcelableExtra("loggedUser");

        setQuestionList();

        RecyclerView recyclerView = findViewById(R.id.questionRecyclerView);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        questionAdapter = new QuestionAdapter(this, questionList, loggedUser);

        recyclerView.setAdapter(questionAdapter);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        questionAdapter.onActivityResult(requestCode, resultCode, data);
    }
}