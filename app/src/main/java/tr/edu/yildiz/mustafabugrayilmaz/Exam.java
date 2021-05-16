package tr.edu.yildiz.mustafabugrayilmaz;

import java.util.ArrayList;

public class Exam {
    private String title;
    private Question[] questions;
    private String time;
    private int point;
    private int difficulty;

    public Exam(String title, Question[] questions, String time, int point, int difficulty) {
        this.title = title;
        this.questions = questions;
        this.time = time;
        this.point = point;
        this.difficulty = difficulty;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Question[] getQuestions() {
        return questions;
    }

    public void setQuestions(Question[] questions) {
        this.questions = questions;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}
