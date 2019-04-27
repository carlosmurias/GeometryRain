package games.ameba.geometryrain;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import games.ameba.geometryrain.controllers.UserController;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txtUserNameNew, txtPasswordNew, txtPasswordNewRepeat, txtPersonName, txtSurname, txtAddress,
            txtEmail, txtPhone;
    Spinner spCountry;
    Button btnOk, btnBack;

    /**
     * l'onCreate d'aquesta Activity fa el mateix que a les altres dues
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        UserController.init();

        txtUserNameNew = (EditText) findViewById(R.id.txtUserNameNew);
        txtPasswordNew = (EditText) findViewById(R.id.txtPasswordNew);
        txtPasswordNewRepeat = (EditText) findViewById(R.id.txtPasswordNewRepeat);
        txtPersonName = (EditText) findViewById(R.id.txtPersonName);
        txtSurname = (EditText) findViewById(R.id.txtSurname);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPhone = (EditText) findViewById(R.id.txtPhone);

        spCountry = (Spinner) findViewById(R.id.spCountry);
        btnOk = (Button) findViewById(R.id.btnOkNew);
        btnBack = (Button) findViewById(R.id.btnBackNew);

        btnOk.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        countries();

    }

    /**
     * el clickListener dels botons
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v==btnBack){
            backToLogin();
        } else if (v==btnOk){
            if (checkRegisterForm()) {
                doRegister();
            };
        }
    }

    /**
     * Crea un array amb el nom dels paísos a partir del paquet java.util.Locale, el converteix a un ArrayList
     * i el carrega a l'spinner mitjançamt im ArrayAdapter
     */
    private void countries(){
        //String[] locales = Locale.getISOCountries();
        String[] paisos = Commons.getCountries();
        List<String> countries = new ArrayList<>();
        countries.add("");


        /*for (String countryCode : locales) {
            Locale obj = new Locale("", countryCode);
            countries.add(obj.getDisplayCountry());
        }*/

        for (String pais : paisos){
            countries.add(pais);
        }

        Collections.sort(countries);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, countries);
        spCountry.setAdapter(adapter);
    }

    /**
     * comprova sobre l'Activity i abans d'intentar registrar res al server, que totes les dades siguin correctes
     * @return true si tot està en ordre, false si es compleix alguna de les condicions per impedir-ho
     */
    private boolean checkRegisterForm() {
        //UserController name
        if (txtUserNameNew.getText().toString().isEmpty()){
            showToast(getString(R.string.REG_ERR_USERNAME1));
            txtUserNameNew.requestFocus();
            return false;
        }
        if (txtUserNameNew.getText().toString().length() < 4){ //minim 4 caracters per al nom d'usuari
            showToast(getString(R.string.REG_ERR_USERNAME2));
            txtUserNameNew.requestFocus();
            return false;
        }
        if (!checkExistingUserName(txtUserNameNew.getText().toString())){
            showToast(getString(R.string.REG_ERR_USERNAME3));
            txtUserNameNew.requestFocus();
            return false;
        }

        //Password
        if (txtPasswordNew.getText().toString().isEmpty()){
            showToast(getString(R.string.REG_ERR_PASSWORD1));
            txtPasswordNew.requestFocus();
            return false;
        }
        if (txtPasswordNew.getText().toString().length() < 8){ //minim 8 caracters per al password
            showToast(getString(R.string.REG_ERR_PASSWORD2));
            txtPasswordNew.requestFocus();
            return false;
        }

        //Repeating password
        if (txtPasswordNewRepeat.getText().toString().isEmpty()){
            showToast(getString(R.string.REG_ERR_PASSWORD3));
            txtPasswordNewRepeat.requestFocus();
            return false;
        }
        if (!txtPasswordNewRepeat.getText().toString().equals(txtPasswordNew.getText().toString())){
            showToast(getString(R.string.REG_ERR_PASSWORD4));
            txtPasswordNewRepeat.requestFocus();
            return false;
        }


        if (txtPersonName.getText().toString().length() <= 1){
            showToast(getString(R.string.REG_ERR_PERSON_NAME1));
            txtPersonName.requestFocus();
            return false;
        }
        if (txtSurname.getText().toString().length() <= 1){
            showToast(getString(R.string.REG_ERR_PERSON_NAME2));
            txtSurname.requestFocus();
            return false;
        }

        if (txtAddress.getText().toString().isEmpty()){
            showToast(getString(R.string.REG_ERR_ADDRESS));
            txtAddress.requestFocus();
            return false;
        }

        if (txtEmail.getText().toString().isEmpty()){
            showToast(getString(R.string.REG_ERR_EMAIL1));
            txtEmail.requestFocus();
            return false;
        }

        if (spCountry.getSelectedItemPosition() == 0){
            showToast(getString(R.string.REG_ERR_COUNTRY));
            spCountry.requestFocus();
            return false;
        }


        if (!isEmailValid(txtEmail.getText().toString())){
            showToast(getString(R.string.REG_ERR_EMAIL2));
            txtEmail.requestFocus();
            return false;
        }


        return true;
    }

    /**
     * crea una instància de la classe UserController i si aquesta compleix les condicions adeqüades,
     * la registra a Firebase
     */
    private void doRegister() {
        final User user = new User();

        user.setUsername(txtUserNameNew.getText().toString());
        user.setPassword(txtPasswordNew.getText().toString());
        user.setName(txtPersonName.getText().toString());
        user.setSurname(txtSurname.getText().toString());
        user.setAddress(txtAddress.getText().toString());
        user.setCountry(spCountry.getSelectedItem().toString());
        user.setEmail(txtEmail.getText().toString());
        user.setPhone(txtPhone.getText().toString());

        if (user.canRegister()) {
            UserController.register(user);
            UserController.setOnRegisterListener(new UserController.onRegisterListener() {
                @Override
                public void onUserRegistered(User user) {
                    showToast("User registered successfully");
                }

                @Override
                public void onError(Exception e) {
                    showToast(e.getLocalizedMessage());
                }
            });

        } else {
            showToast("Error: some data is invalid");
        }
    }

    //TODO: mètode per comprovar que el nom d'usuari existeix a la base de dades
    private boolean checkExistingUserName(String userName){
        return true;
    }

    /**
     * mètode per comprovar el format d'un email
     * @param email el text introduït a l'editText txtEmail
     * @return true o false en funció de si aquest text segueix o no el patró de caracters
     */
    private boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * torna a la pantalla de login
     */
    private void backToLogin() {
        finishAffinity();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);

    }
    /**
     * mostra per pantalla un toast amb informació rellevant per a l'usuari
     * @param message el missatge rebut per paràmetre que es mostrarà
     */
    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
