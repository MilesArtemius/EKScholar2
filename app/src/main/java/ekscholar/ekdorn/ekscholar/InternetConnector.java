package ekscholar.ekdorn.ekscholar;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InternetConnector {
    private static final String students_coll = "students";
    private static final String teachers_name = "user";
    private static final String task_coll = "homeworks";

    private static InternetConnector now;

    private FirebaseFirestore db;
    Map<String, Object> resultOld;
    List<DocumentSnapshot> result;

    onLoadedListener onLoaded;

    public static InternetConnector get(onLoadedListener listener) {
        if (now == null) {
            now = new InternetConnector();
            now.db = FirebaseFirestore.getInstance();
            now.onLoaded = listener;
        }
        return now;
    }

    private InternetConnector() {}

    public void loadByDate(int date) {
        resultOld = new HashMap<>();

        db.collection(students_coll)
                .document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .collection(task_coll)
                .document(String.valueOf(date))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            resultOld = (HashMap<String, Object>) task.getResult().getData().get(task_coll);

                            Log.d("TAG", task.getResult().getId() + " => " + task.getResult().getData());
                            onLoaded.onComplete(resultOld);


                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void loadByDate(final Calendar cl) {
        result = new ArrayList<>();

        String dateKey = String.valueOf(10000 * cl.get(Calendar.YEAR) +
                100 * (cl.get(Calendar.MONTH) + 1) + cl.get(Calendar.DATE));

        Log.e("TAG", "fillDaysIn: " + dateKey);

        CollectionReference qu = db.collection(students_coll)
                .document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .collection(task_coll);
        qu.whereEqualTo("date", dateKey)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            result = task.getResult().getDocuments();

                            Log.e("TAG", "onComplete: " + cl.get(Calendar.DATE) );
                            Log.d("TAG", task.getResult().toString() + " => " + task.getResult().getDocuments());
                            onLoaded.onComplete(result, cl);


                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    //public void push

    public interface onLoadedListener {
        public void onComplete(List<DocumentSnapshot> loaded, Calendar cl);
        public void onComplete(Map<String, Object> loaded);
    }
}
