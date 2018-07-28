package ekscholar.ekdorn.ekscholar;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeWork implements Serializable {
    private static final String key_type = "type";
    private static final String value_text = "text";
    private static final String value_pic = "pic"; //TODO: something...
    private static final String key_task = "task";
    private static final String key_answers = "answer";
    private static final String key_subject = "subject";
    private static final String key_title  = "title";
    private static final String key_mark  = "mark";
    private static final String key_answersAct = "answers_act";

    public String taskT; //task
    public Image taskI; //task
    public ArrayList<String> answers; //answers
    public String name; //subject
    public String title; //title

    public int mark;
    public int back;
    public List<String> answersAct;
    public String id;

    public boolean isSection;

    public HomeWork(@Nullable DocumentSnapshot value, @Nullable String name, boolean section) {
        this.isSection = section;

        if (section) {
            this.name = name;
        } else {
            if (value != null) {
                if (((String) value.get(key_type)).equals(value_text)) {
                    this.taskT = (String) value.get(key_task);
                } else {
                    //this.taskI = ...
                }
                this.answers = (ArrayList<String>) value.get(key_answers);
                this.name = (String) value.get(key_subject);
                this.title = (String) value.get(key_title);
                this.mark = ((Number) value.get(key_mark)).intValue();
                this.answersAct = (ArrayList<String>) value.get(key_answersAct);
                this.id = (String) value.getId();
            }
        }
    }

    public void setMark(int mrk, Context app) {
        this.mark = mrk;
        switch (mrk) {
            case 0:
                this.back = ContextCompat.getColor(app, R.color.colorN);
                break;
            case 1:
                this.back = ContextCompat.getColor(app, R.color.colorTooBad);
                break;
            case 2:
                this.back = ContextCompat.getColor(app, R.color.colorBad);
                break;
            case 3:
                this.back = ContextCompat.getColor(app, R.color.colorOK);
                break;
            case 4:
                this.back = ContextCompat.getColor(app, R.color.colorGood);
                break;
            case 5:
                this.back = ContextCompat.getColor(app, R.color.colorPerfect);
                break;
            default:
                this.back = ContextCompat.getColor(app, R.color.colorN);
                break;
        }
    }

    public void setAnswers(List<String> ans) {
        this.answersAct = ans;
    }

    @Override
    public String toString() {
        return "HomeWork{" +
                "taskT='" + taskT + '\'' +
                ", taskI=" + taskI +
                ", answers=" + answers +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", mark=" + mark +
                ", back=" + back +
                ", isSection=" + isSection +
                '}';
    }
}
