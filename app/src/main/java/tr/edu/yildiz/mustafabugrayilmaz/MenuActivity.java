package tr.edu.yildiz.mustafabugrayilmaz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    private User loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        loggedUser = getIntent().getParcelableExtra("loggedUser");
    }

    public void addQuestionActivity(View view) {
        Intent addIntent = new Intent(this, AddQuestionActivity.class);

        addIntent.putExtra("loggedUser", (Parcelable) loggedUser);

        startActivity(addIntent);
    }

    public void listQuestionsActivity(View view) {
        Intent addIntent = new Intent(this, ListQuestionsActivity.class);

        addIntent.putExtra("loggedUser", (Parcelable) loggedUser);

        startActivity(addIntent);
    }

    public void createExam(View view){
        Intent createIntent=new Intent(this,CreateExamActivity.class);

        createIntent.putExtra("loggedUser",(Parcelable) loggedUser);

        startActivity(createIntent);
    }

    public void examSettingsActivity(View view){
        Intent settingsIntent=new Intent(this,ExamSettingsActivity.class);

        settingsIntent.putExtra("loggedUser", (Parcelable) loggedUser);

        startActivity(settingsIntent);
    }
}