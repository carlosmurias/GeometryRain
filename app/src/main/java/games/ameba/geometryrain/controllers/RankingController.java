package games.ameba.geometryrain.controllers;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import games.ameba.geometryrain.RankedUser;

public class RankingController {
    private FirebaseFirestore db;

    public RankingController() {
        if (db==null){
            db = FirebaseFirestore.getInstance();
        }
    }

    public interface onRankingResultListener {
        void onRankingResult(ArrayList<RankedUser> rankedUsers);
        void onError(Exception e);
    }
    private onRankingResultListener onRankingResultListener;
    public void setOnSignInListener(onRankingResultListener listener) {
        onRankingResultListener = listener;
    }

    public RankingController getGlobalTop(final int numberOfRows) {
        db.collection("ranking")
                .orderBy("score", Query.Direction.DESCENDING)
                .limit(numberOfRows)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<RankedUser> rankedUsers = new ArrayList<>(numberOfRows);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                RankedUser rankedUser = document.toObject(RankedUser.class);
                                rankedUser.setUsername(document.getId());
                                rankedUsers.add(rankedUser);
                            }
                            onRankingResultListener.onRankingResult(rankedUsers);
                        } else {
                            onRankingResultListener.onError(task.getException());
                        }
                    }
                });
        return this;
    }

    public RankingController getCountryTop(String country, final int numberOfRows) {
        db.collection("ranking")
                .whereEqualTo("country", country)
                .orderBy("score", Query.Direction.DESCENDING)
                .limit(numberOfRows)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<RankedUser> rankedUsers = new ArrayList<>(numberOfRows);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                RankedUser rankedUser = document.toObject(RankedUser.class);
                                rankedUser.setUsername(document.getId());
                                rankedUsers.add(rankedUser);
                            }
                            onRankingResultListener.onRankingResult(rankedUsers);
                        } else {
                            onRankingResultListener.onError(task.getException());
                        }
                    }
                });
        return this;
    }
}

