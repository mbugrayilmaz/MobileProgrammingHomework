package tr.edu.yildiz.mustafabugrayilmaz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class EditQuestionActivity extends AppCompatActivity {

    private Question question;

    private User loggedUser;

    private final int MEDIA_INTENT_CODE = 1;

    private ArrayList<Question> questionList = new ArrayList<>();

    private EditText editQuestionText, editOption1, editOption2, editOption3, editOption4, editOption5;
    private RadioGroup editCorrectOptionGroup;

    private TextView editAttachTextView;


    private Uri attachment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);

        question = getIntent().getParcelableExtra("question");
        loggedUser = getIntent().getParcelableExtra("loggedUser");

        initializeViews();

        setValues();
    }

    public void apply(View view) {
        Uri uri = null;

        int index;

        question.setQuestionText(editQuestionText.getText().toString());

        question.setOptions(new String[]{
                editOption1.getText().toString(),
                editOption2.getText().toString(),
                editOption3.getText().toString(),
                editOption4.getText().toString(),
                editOption5.getText().toString()
        });

        question.setCorrectOptionIndex(editCorrectOptionGroup.indexOfChild(findViewById(editCorrectOptionGroup.getCheckedRadioButtonId())));

        if (attachment != null) {
            index = getIntent().getIntExtra("index", 0);
            uri = Tools.saveFileToExternal(this, attachment, loggedUser.getUsername() + index);
        }

        if (uri != null) {
            question.setAttachmentUri(uri.toString());
        }

        Intent editReturn = new Intent();

        editReturn.putExtra("editedQuestion", (Parcelable) question);
        editReturn.putExtra("position", getIntent().getIntExtra("position", 0));

        setResult(2, editReturn);

        System.out.println(question);

        finish();
    }

    private void setValues() {
        editQuestionText.setText(question.getQuestionText());

        editOption1.setText(question.getOptions()[0]);
        editOption2.setText(question.getOptions()[1]);
        editOption3.setText(question.getOptions()[2]);
        editOption4.setText(question.getOptions()[3]);
        editOption5.setText(question.getOptions()[4]);

        editCorrectOptionGroup.check(editCorrectOptionGroup.getChildAt(question.getCorrectOptionIndex()).getId());

        if (question.getAttachmentUri() != null) {
            editAttachTextView.setText(Tools.getFileName(this, question.getAttachmentUri()));
        } else {
            editAttachTextView.setText("No attachment");
        }
    }

    public void editAttach(View view) {
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

                editAttachTextView.setText(documentFile.getName());
            }
        }
    }

    private void initializeViews() {
        editQuestionText = findViewById(R.id.editQuestionText);

        editOption1 = findViewById(R.id.editOption1);
        editOption2 = findViewById(R.id.editOption2);
        editOption3 = findViewById(R.id.editOption3);
        editOption4 = findViewById(R.id.editOption4);
        editOption5 = findViewById(R.id.editOption5);

        editCorrectOptionGroup = findViewById(R.id.editCorrectOptionGroup);

        editAttachTextView = findViewById(R.id.editAttachTextView);
    }
}