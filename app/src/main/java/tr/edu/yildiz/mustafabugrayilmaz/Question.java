package tr.edu.yildiz.mustafabugrayilmaz;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Arrays;

public class Question implements Serializable, Parcelable {
    private static final long serialVersionUID=2758827682768762257L;

    private int id;
    private String questionText;
    private String[] options;
    private int correctOptionIndex;
    private String attachmentUri;

    public Question(int id, String questionText, String[] options, int correctOptionIndex) {
        this.id=id;
        this.questionText=questionText;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
        attachmentUri = null;
    }

    public Question(int id, String questionText, String[] options, int correctOptionIndex, String attachmentUri) {
        this.id=id;
        this.questionText=questionText;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
        this.attachmentUri = attachmentUri;
    }

    public Question(int id, String questionText, String[] options, int correctOptionIndex, Uri attachmentUri) {
        this.id=id;
        this.questionText=questionText;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
        this.attachmentUri = attachmentUri.toString();
    }

    protected Question(Parcel in) {
        id = in.readInt();
        questionText = in.readString();
        options = in.createStringArray();
        correctOptionIndex = in.readInt();
        attachmentUri = in.readString();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public void setOption(int index, String option) {
        options[index] = option;
    }

    public void setCorrectOptionIndex(int correctOptionIndex) {
        this.correctOptionIndex = correctOptionIndex;
    }

    public int getCorrectOptionIndex(){
        return correctOptionIndex;
    }

    public String getCorrectOption() {
        return options[correctOptionIndex];
    }

    public String getAttachmentUri() {
        return attachmentUri;
    }

    public void setAttachmentUri(String attachmentUri) {
        this.attachmentUri = attachmentUri;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", questionText='" + questionText + '\'' +
                ", options=" + Arrays.toString(options) +
                ", correctOption=" + correctOptionIndex +
                ", attachmentUri='" + attachmentUri + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(questionText);
        parcel.writeStringArray(options);
        parcel.writeInt(correctOptionIndex);
        parcel.writeString(attachmentUri);
    }
}
