package ekscholar.ekdorn.ekscholar;

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

    private static final String key_answers = "answers";

    public String task;
    public ArrayList<String> answers;
    private TimeTaking tt;

    public HomeWork(final int date, TimeTaking newtt) {
        tt = newtt;
        InternetConnector.get(new InternetConnector.onLoadedListener() {
            @Override
            public void onComplete(Map<String, Object> loaded) {
                System.out.println(date);
                System.out.println(loaded);
                HashMap<String, Object> data = (HashMap<String, Object>) loaded.get(String.valueOf(date));
                System.out.println(data);
                if (data.get(key_type).equals(value_text)) {
                    task = data.get(key_task).toString();
                } //else

                answers = (ArrayList<String>) data.get(key_answers);

                System.out.println("hw created");
                tt.onComplete();
            }

            @Override
            public void onComplete(List<DocumentSnapshot> loaded, Calendar cl) {}
        }).loadByDate(date);
    }

    public interface TimeTaking {
        public void onComplete();
    }
}
