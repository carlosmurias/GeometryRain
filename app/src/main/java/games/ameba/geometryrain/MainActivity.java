package games.ameba.geometryrain;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import games.ameba.geometryrain.controllers.UserController;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean logged = false;
    Button btnLoginRegister, btnNewGame, btnRank, btnExit, btnLogout;
    TextView txtUserName;
    LinearLayout layoutUser;
    String UserName, country;

    /**
     * Mètode callback de creació de l'activity. S'indica el layout,
     * es crea una instància de FireBaseAuth per gestionar la sessió actual d'usuari,
     * es referencien els views i layouts amb els que l'usuari interactua
     * i s'afegeixen els clickListeners corresponents. Per defecte el
     * LayoutUser serà invisible
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLoginRegister = (Button) findViewById(R.id.btnLoginReg);
        btnNewGame = (Button) findViewById(R.id.btnNewGame);
        btnRank = (Button) findViewById(R.id.btnRank);
        btnExit = (Button) findViewById(R.id.btnExit);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        layoutUser = (LinearLayout) findViewById(R.id.layoutUser);
        layoutUser.setVisibility(View.INVISIBLE);

        txtUserName = (TextView) findViewById(R.id.txtCurrentUser);

        btnLoginRegister.setOnClickListener(this);
        btnNewGame.setOnClickListener(this);
        btnRank.setOnClickListener(this);
        btnExit.setOnClickListener(this);
    }

    /**
     * Mètode callback que gestiona l'event d'inici de l'activitat. Si detecta que hi ha un usuari
     * amb sessió iniciada quan l'activitat inicia, mostra el compte d'usuari i el botó de logout
     * dins un layout, i oculta el botó de login
     */
    @Override
    protected void onStart() {
        super.onStart();

        btnLoginRegister.setVisibility(View.VISIBLE);
        setLogged(false);

        UserController.init();
        UserController.setOnAlreadySignedIn(new UserController.onSignInListener() {
            @Override
            public void onUserSignedIn(User user) {
                showToast("Welcome "+user.getUsername());
                setLogged(true);
                UserName = user.getUsername();
                country = user.getCountry();
                txtUserName.setText(UserName);
                layoutUser.setVisibility(View.VISIBLE);
                setButtonListenerToThis(btnLogout);
                btnLoginRegister.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onError(Exception e) {
                showToast(e.getLocalizedMessage());
            }
        });
    }


    public void setButtonListenerToThis (Button btn) {
        btn.setOnClickListener(this);
    }

    /**
     * mètode onClick corresponent a la interficie View.OnClickListener
     * @param v el view sobre el que ha d'actuar el listener
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnExit :
                exitApp();
                break;
            case R.id.btnLoginReg:
                openLogin();
                break;
            case R.id.btnLogout:
                logOut();
                break;
            case R.id.btnNewGame:
                newGame();
                break;
            case R.id.btnRank:
                rankDisplay();
                break;
        }
    }
/*
* al obrir l'activity, se li passa un bundle amb el pais perque mostri
* per defecte el ranking del pais del jugador
*/
    private void rankDisplay() {
        Intent i = new Intent(this, RankingActivity.class);
        Bundle extras = new Bundle();
        extras.putString("country", country);
        i.putExtras(extras);
        startActivity(i);
    }

    private void newGame() {
        Intent i = new Intent(this, GameActivity.class);
        startActivityForResult(i, 1);
    }

    /**
     * Obre una instància de LoginActivity mitjançant un Intent
     */
    public void openLogin(){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    /**
     * tanca la sessió d'usuari actual a Firebase
     */
    public void logOut() {
        UserController.signOut();
        //mAuth.signOut();
        setLogged(false);
        layoutUser.setVisibility(View.INVISIBLE);
        btnLoginRegister.setVisibility(View.VISIBLE);
        showToast("Logged out");
    }

    /**
     * tanca totes les activitats dependents de la principal, allibera recursos i elimina el procés
     * actual del sistema operatiu
     */
    public void exitApp(){
        finishAffinity();
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
        finish();
    }


    //getters i setters
    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    /**
     * mostra per pantalla un toast amb informació rellevant per a l'usuari
     * @param message el missatge rebut per paràmetre
     */
    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
