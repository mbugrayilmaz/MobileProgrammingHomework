package tr.edu.yildiz.mustafabugrayilmaz;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ExamViewHolder> {

    Context context;
    List<Question> questions;

    List<Question> selectedQuestions;

    private User loggedUser;

    private int difficulty;

    public ExamAdapter(Context context, List<Question> questions, ArrayList<Question> selectedQuestions, User loggedUser, int difficulty) {
        this.context = context;
        this.questions = questions;
        this.selectedQuestions = selectedQuestions;
        this.loggedUser = loggedUser;
        this.difficulty = difficulty;
    }

    public static class ExamViewHolder extends RecyclerView.ViewHolder {
        CheckBox selectQuestionCheck;
        TextView examQuestionTextView;
        TextView examOption1TextView;
        TextView examOption2TextView;
        TextView examOption3TextView;
        TextView examOption4TextView;
        TextView examOption5TextView;
        TextView examCorrectOptionTextView;
        TextView examAttachTextView;

        public ExamViewHolder(@NonNull View itemView) {
            super(itemView);

            examCorrectOptionTextView = itemView.findViewById(R.id.examCorrectOptionTextView);

            selectQuestionCheck = itemView.findViewById(R.id.selectQuestionCheck);
            examQuestionTextView = itemView.findViewById(R.id.examQuestionTextView);
            examOption1TextView = itemView.findViewById(R.id.examOption1TextView);
            examOption2TextView = itemView.findViewById(R.id.examOption2TextView);
            examOption3TextView = itemView.findViewById(R.id.examOption3TextView);
            examOption4TextView = itemView.findViewById(R.id.examOption4TextView);
            examOption5TextView = itemView.findViewById(R.id.examOption5TextView);

            examAttachTextView = itemView.findViewById(R.id.examAttachTextView);
        }
    }

    @NonNull
    @Override
    public ExamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.exam_list_item, parent, false);
        return new ExamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamViewHolder holder, int position) {
        holder.examQuestionTextView.setText(questions.get(position).getQuestionText());

        TextView[] arr = new TextView[5];

        arr[0] = holder.examOption1TextView;
        arr[1] = holder.examOption2TextView;
        arr[2] = holder.examOption3TextView;
        arr[3] = holder.examOption4TextView;
        arr[4] = holder.examOption5TextView;

        arr[0].setText(questions.get(position).getOptions()[0]);
        arr[1].setText(questions.get(position).getOptions()[1]);
        arr[2].setText(questions.get(position).getOptions()[2]);
        arr[3].setText(questions.get(position).getOptions()[3]);
        arr[4].setText(questions.get(position).getOptions()[4]);

        int disabledCount = 0;

        for (int i = 0; i < 5 && disabledCount < 5 - difficulty; i++) {
            if (i != questions.get(position).getCorrectOptionIndex()) {
                arr[i].setEnabled(false);
                disabledCount++;
            }
        }

        holder.examCorrectOptionTextView.setText((questions.get(position).getCorrectOptionIndex() + 1) + ") " + questions.get(position).getCorrectOption());

        holder.selectQuestionCheck.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isChecked()) {
                selectedQuestions.add(questions.get(position));
            } else {
                selectedQuestions.remove(questions.get(position));
            }
        });

        if (questions.get(position).getAttachmentUri() != null) {
            holder.examAttachTextView.setText(Tools.getFileName(holder.examAttachTextView.getContext(), questions.get(position).getAttachmentUri()));

            holder.examAttachTextView.setOnClickListener(l -> {
                Uri fileUri = Uri.parse(questions.get(position).getAttachmentUri());

                Intent viewIntent = new Intent(Intent.ACTION_VIEW);

                ContentResolver cR = context.getContentResolver();

                viewIntent.setDataAndType(fileUri, cR.getType(fileUri));

                viewIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                FileProvider.getUriForFile(holder.examAttachTextView.getContext(),
                        holder.examAttachTextView.getContext().getApplicationContext().getPackageName() + ".provider",
                        new File(fileUri.getPath()));

                holder.examAttachTextView.getContext().startActivity(viewIntent);
            });
        } else {
            holder.examAttachTextView.setText("No attachment");
        }
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }
}