package tr.edu.yildiz.mustafabugrayilmaz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    Context context;
    List<Question> questions;

    private User loggedUser;

    public QuestionAdapter(Context context, List<Question> questions, User loggedUser) {
        this.context = context;
        this.questions = questions;
        this.loggedUser = loggedUser;
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView;
        TextView option1TextView;
        TextView option2TextView;
        TextView option3TextView;
        TextView option4TextView;
        TextView option5TextView;
        TextView correctOptionTextView;
        TextView attachTextView;
        Button editQuestionButton;
        Button deleteQuestionButton;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);

            questionTextView = itemView.findViewById(R.id.questionTextView);
            option1TextView = itemView.findViewById(R.id.option1TextView);
            option2TextView = itemView.findViewById(R.id.option2TextView);
            option3TextView = itemView.findViewById(R.id.option3TextView);
            option4TextView = itemView.findViewById(R.id.option4TextView);
            option5TextView = itemView.findViewById(R.id.option5TextView);

            correctOptionTextView = itemView.findViewById(R.id.correctOptionTextView);

            attachTextView = itemView.findViewById(R.id.attachTextView);

            editQuestionButton = itemView.findViewById(R.id.editQuestionButton);

            deleteQuestionButton = itemView.findViewById(R.id.deleteQuestionButton);
        }
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.question_list_item, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        holder.questionTextView.setText(questions.get(position).getQuestionText());

        holder.option1TextView.setText(questions.get(position).getOptions()[0]);
        holder.option2TextView.setText(questions.get(position).getOptions()[1]);
        holder.option3TextView.setText(questions.get(position).getOptions()[2]);
        holder.option4TextView.setText(questions.get(position).getOptions()[3]);
        holder.option5TextView.setText(questions.get(position).getOptions()[4]);

        holder.correctOptionTextView.setText((questions.get(position).getCorrectOptionIndex() + 1) + ") " + questions.get(position).getCorrectOption());

        if (questions.get(position).getAttachmentUri() != null) {
            holder.attachTextView.setText(Tools.getFileName(holder.attachTextView.getContext(), questions.get(position).getAttachmentUri()));

            holder.attachTextView.setOnClickListener(l -> {
                Uri fileUri = Uri.parse(questions.get(position).getAttachmentUri());

                Intent viewIntent = new Intent(Intent.ACTION_VIEW);

                ContentResolver cR = context.getContentResolver();

                viewIntent.setDataAndType(fileUri, cR.getType(fileUri));

                viewIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                FileProvider.getUriForFile(holder.attachTextView.getContext(),
                        holder.attachTextView.getContext().getApplicationContext().getPackageName() + ".provider",
                        new File(fileUri.getPath()));

                holder.attachTextView.getContext().startActivity(viewIntent);
            });
        } else {
            holder.attachTextView.setText("No attachment");
        }

        holder.editQuestionButton.setOnClickListener(l -> {
            int EDIT_INTENT = 2;

            Intent editIntent = new Intent(context, EditQuestionActivity.class);

            int index;

            if (questions.size() == 0) {
                index = 0;
            } else {
                index = questions.get(questions.size() - 1).getId() + 1;
            }

            editIntent.putExtra("question", (Parcelable) questions.get(position));
            editIntent.putExtra("loggedUser", (Parcelable) loggedUser);
            editIntent.putExtra("index", index);
            editIntent.putExtra("position", position);

            ((Activity) context).startActivityForResult(editIntent, EDIT_INTENT);

            notifyItemChanged(position);
        });

        holder.deleteQuestionButton.setOnClickListener(l -> {
            new AlertDialog.Builder(l.getContext())
                    .setTitle("Delete Question")
                    .setMessage("Are you sure you want to delete this question?")
                    .setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
                        questions.remove(position);

                        notifyItemRemoved(position);

                        notifyItemRangeChanged(position, getItemCount());

                        saveChanges();
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 2) {
            if (resultCode == 2) {
                int position = data.getIntExtra("position", 0);

                questions.set(position, data.getParcelableExtra("editedQuestion"));

                notifyItemChanged(position);

                saveChanges();
            }
        }
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    private void saveChanges() {
        File users = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), loggedUser.getUsername() + "Questions");

        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(users, false))) {
            objectOutputStream.writeObject(questions);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
