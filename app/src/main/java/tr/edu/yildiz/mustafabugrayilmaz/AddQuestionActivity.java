package tr.edu.yildiz.mustafabugrayilmaz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class AddQuestionActivity extends AppCompatActivity {

    private final int MEDIA_INTENT_CODE = 1;

    private User loggedUser;

    private ArrayList<Question> questionList = new ArrayList<>();

    private EditText questionText, option1, option2, option3, option4, option5;
    private RadioButton option1Radio, option2Radio, option3Radio, option4Radio, option5Radio;
    private RadioGroup correctOptionGroup;

    private Button attachButton;
    private TextView attachText;

    private Button addQuestionButton;

    private Uri attachment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        initializeViews();

        loggedUser = getIntent().getParcelableExtra("loggedUser");

        setQuestionList();

        System.out.println(questionList);
    }

    public void addQuestion(View view) {
        if (!isValid()) {
            return;
        }

        Question question;

        Uri uri;

        int index;

        String[] options = new String[5];

        options[0] = option1.getText().toString();
        options[1] = option2.getText().toString();
        options[2] = option3.getText().toString();
        options[3] = option4.getText().toString();
        options[4] = option5.getText().toString();

        int selectedRadio = correctOptionGroup.getCheckedRadioButtonId();

        int selectedIndex = correctOptionGroup.indexOfChild(correctOptionGroup.findViewById(selectedRadio));

        if (questionList.size() == 0) {
            index = 0;
        } else {
            System.out.println(questionText.getText().toString());
            System.out.println(questionList.get(questionList.size() - 1).getId());
            index = questionList.get(questionList.size() - 1).getId() + 1;
        }

        if (attachment != null) {
            uri = Tools.saveFileToExternal(this, attachment, loggedUser.getUsername()+index);
            question = new Question(index, questionText.getText().toString(), options, selectedIndex, uri);
        } else {
            question = new Question(index, questionText.getText().toString(), options, selectedIndex);
        }

        questionList.add(question);

        File users = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), loggedUser.getUsername() + "Questions");

        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(users, false))) {
            objectOutputStream.writeObject(questionList);

            finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void attach(View view) {
        Intent mediaIntent = new Intent(Intent.ACTION_GET_CONTENT);

        mediaIntent.setType("*/*");

        mediaIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"video/*", "audio/*", "image/*"});

        startActivityForResult(mediaIntent, MEDIA_INTENT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MEDIA_INTENT_CODE) {
            if (resultCode == -1) {
                attachment = data.getData();

                DocumentFile documentFile = DocumentFile.fromSingleUri(this, attachment);

                attachText.setText(documentFile.getName());
            }
        }
    }

    private void setQuestionList() {
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

        if (questionText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please write a question", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (option1.getText().toString().isEmpty() ||
                option2.getText().toString().isEmpty() ||
                option3.getText().toString().isEmpty() ||
                option4.getText().toString().isEmpty() ||
                option5.getText().toString().isEmpty()) {

            Toast.makeText(this, "Please fill out all the options", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (correctOptionGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select a correct option", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void initializeViews() {
        questionText = findViewById(R.id.questionText);

        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        option5 = findViewById(R.id.option5);

        option1Radio = findViewById(R.id.option1Radio);
        option2Radio = findViewById(R.id.option2Radio);
        option3Radio = findViewById(R.id.option3Radio);
        option4Radio = findViewById(R.id.option4Radio);
        option5Radio = findViewById(R.id.option5Radio);

        correctOptionGroup = findViewById(R.id.correctOptionGroup);

        attachButton = findViewById(R.id.attachButton);
        attachText = findViewById(R.id.attachTextView);

        addQuestionButton = findViewById(R.id.addQuestionButton);
    }
}