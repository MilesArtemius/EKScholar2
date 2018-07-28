package ekscholar.ekdorn.ekscholar;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeWork {
    private static final String key_type = "type";
    private static final String value_text = "text";
    private static final String value_pic = "pic"; //TODO: something...
    private static final String key_task = "task";
    private static final String key_answers = "answer";
    private static final String key_subject = "subject";
    private static final String key_title  = "title";

    public String taskT; //task
    public Image taskI; //task
    public ArrayList<String> answers; //answers
    public String name; //subject
    public String title; //title

    public int mark;
    public int back;

    public boolean isSection;

    public HomeWork(@Nullable DocumentSnapshot value, @Nullable String name, boolean section) {
        this.isSection = section;

        if (section) {
            this.name = name;
        } else {
            if (((String) value.get(key_type)).equals(value_text)) {
                this.taskT = (String) value.get(key_task);
            } else {
                //this.taskI = ...
            }
            this.answers = (ArrayList<String>) value.get(key_answers);
            this.name = (String) value.get(key_subject);
            this.title = (String) value.get(key_title);
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
