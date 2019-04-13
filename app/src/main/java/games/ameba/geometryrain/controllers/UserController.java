package games.ameba.geometryrain.controllers;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import games.ameba.geometryrain.User;

public final class UserController {
    private static FirebaseAuth mAuth;
    private static FirebaseFirestore db;

    private static User signedInUser;

    private UserController() { }

    public static void init() {
        if (mAuth==null) {
            mAuth = FirebaseAuth.getInstance();
        }
        if (db==null){
            db = FirebaseFirestore.getInstance();
        }
        if (signedInUser==null && mAuth.getCurrentUser()!= null) {
            signInOnInit();
            // setSignedInUserFromFirebaseUser(); // TODO: 31/3/19
        } else if (signedInUser!= null) {
            onAlreadySignedIn.onUserSignedIn(signedInUser);
        }
    }

    public static User getSignedInUser() {
        return signedInUser;
    }

    public interface onSignInListener {
        void onUserSignedIn(User user);
        void onError(Exception e);
    }
    private static onSignInListener onSignInListener;
    public static void setOnSignInListener(onSignInListener listener) {
        onSignInListener = listener;
    }
    public static void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            DocumentReference docRef = db.collection("users").document(mAuth.getCurrentUser().getUid());
                            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    signedInUser = documentSnapshot.toObject(User.class);
                                    signedInUser.setUid(mAuth.getCurrentUser().getUid());
                                    onSignInListener.onUserSignedIn(signedInUser);
                                }
                            });
                        } else {
                            onSignInListener.onError(task.getException());
                            Log.w("ameba-auth", "signInWithEmail:failure", task.getException());
                        }
                    }
                });
    }



    public interface onRegisterListener {
        void onUserRegistered(User user);
        void onError(Exception e);
    }
    private static onRegisterListener onRegisterListener;
    public static void setOnRegisterListener(onRegisterListener listener) {
        onRegisterListener = listener;
    }

    public static void register(final User user) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // The user (email & password) have been created.
                            // Now we need to add the additional user information to Firestore
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            if (firebaseUser != null) {

                                db.collection("users")
                                        .document(firebaseUser.getUid()) // We use the user UID as a document ID
                                        .set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                onRegisterListener.onUserRegistered(user);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                onRegisterListener.onError(e);
                                                Log.w("ameba-auth", "Error writing document", e);
                                            }
                                        });
                            }
                        } else {
                            onRegisterListener.onError(task.getException());
                            Log.w( "ameba-auth","Error writing document with the mail and password auth method", task.getException());
                        }
                    }
                });
    }


    private static onSignInListener onAlreadySignedIn;
    public static void setOnAlreadySignedIn(onSignInListener listener) {
        onAlreadySignedIn = listener;
    }

    public static void signInOnInit() {
        DocumentReference docRef = db.collection("users").document(mAuth.getCurrentUser().getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                signedInUser = documentSnapshot.toObject(User.class);
                signedInUser.setUid(mAuth.getCurrentUser().getUid());
                onAlreadySignedIn.onUserSignedIn(signedInUser);
            }
        });
    }

    public static void signOut() {
        mAuth.signOut();
        signedInUser=null;
    }

}
