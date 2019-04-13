package games.ameba.geometryrain;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import games.ameba.geometryrain.controllers.UserController;

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener{
    EditText txtUser, txtPassword;
    TextView txtRegister;
    Button btnLogin, btnBack;

    /**
     * Mètode callback de creació de l'activity. S'indica el layout,
     * es crea una instància de FireBaseAuth per fer el login,
     * es referencien els views i layouts amb els que l'usuari interactua
     * i s'afegeixen els clickListeners corresponents.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        UserController.init();

        txtUser = (EditText) findViewById(R.id.txtLoginEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnBack = (Button) findViewById(R.id.btnBackToMain);
        txtRegister = (TextView) findViewById(R.id.txtRegisterNew);
        btnBack.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        txtRegister.setOnClickListener(this);
    }

    /**
     * mètode callback que gestiona l'inici de l'activitat
     */
    @Override
    protected void onStart() {
        super.onStart();

        if (UserController.getSignedInUser() != null) {
            // UserController is logged in, must not be in this activity.
            logged();
        }
    }

    /**
     * mètode callback que gestiona els clics als views
     * @param v el view sobre el que es fa clic
     */
    @Override
    public void onClick(View v) {
        if (v == btnLogin){
            checkLoginData();
        } else if (v == txtRegister){
            registerActivity();
        } else if (v ==btnBack) {
            backToMain();
        }
    }

    /**
     * comprova que cap EditText es quedi en blanc i després, mitjançant el mètode no estàtic de
     * Firebase .signInWithEmailAndPassword() el que fa es intentar connectar amb el servidor fent
     * l'usuari introduir el seu email i contrasenya.
     * Si la connexió té èxit, dona un missatge de benvinguda i, si falla, avisa l'usuari.
     */
    public void checkLoginData(){
        String inputEmail = txtUser.getText().toString();
        String inputPassword = txtPassword.getText().toString();

        if (!inputEmail.isEmpty() && !inputPassword.isEmpty()) {
            UserController.signIn(inputEmail,inputPassword);
            UserController.setOnSignInListener(new UserController.onSignInListener() {
                @Override
                public void onUserSignedIn(User user) {
                    showToast("Welcome "+user.getUsername());
                    logged();
                }
                @Override
                public void onError(Exception e) {
                    showToast(e.getLocalizedMessage());
                }
            });

        } else {
            showToast("Please fill all fields");
        }
    }

    /**
     * torna al MainActivity amb les dades de sessió
     */
    private void logged() {
        Intent i = new Intent(this, MainActivity.class);
        Bundle extras = new Bundle();
        extras.putBoolean("logged", true);
        extras.putString("name", txtUser.getText().toString());
        i.putExtras(extras);
        startActivity(i);
        finish();
    }

    /**
     * obre una instància de RegisterActivity
     */
    public void registerActivity(){
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
        finish();
    }

    /**
     * tanca l'actual activity i les seves dependencies, i amb això torna al MainActivity.
     * Es produeix al fer clic a Tornar sense haver iniciat sessió.
     */
    public void backToMain(){
        finishAffinity();
        /*Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();*/
    }

    /**
     * mostra per pantalla un toast amb informació rellevant per a l'usuari
     * @param message el missatge rebut per paràmetre
     */
    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
