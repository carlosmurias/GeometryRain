package games.ameba.geometryrain;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    //Mida de la pantalla
    private int screenWidth;
    private int screenHeight;

    private ConstraintLayout constraintLayout; //layout principal de l'aplicació
    private LinearLayout scoreBar; //barra superior amb el marcador, la barra de vida i la imatge de referència

    //Llista que conté les figures, necessària per afegir i eliminar elements in-game
    private ArrayList<Shape> lista;

    //Un timer i un handler per anar refrescant la imatge de referència
    private Handler handler = new Handler();
    private Timer timer = new Timer();
    //aquest textView conté la puntuació
    private TextView textView;
    //així com a l'imageView hi ha el mètode setImageResource, no existeix el getImageResource que retorni l'ID.
    // El més senzill ha estat acompanyar les generacions d'imatge amb una variable auxiliar que informi de la mateixa,
    // i per això tant aquí com a la clase Shape hi ha un String tag
    private ImageView imatgeReferencia = null;
    private String tagReferencia = "";
    private int iRef;

    private ProgressBar progressBar;
    private long score = 0; //la puntuació inicialitza en 0 sempre


    /**
     * onCreate de l'activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getScreenSize();
        //referenciació de views
        constraintLayout = (ConstraintLayout)findViewById(R.id.mainLayout);
        try {
            imatgeReferencia = (ImageView) findViewById(R.id.imatgeReferencia);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }



        lista = new ArrayList<Shape>();
        textView = (TextView) findViewById(R.id.textView);
        progressBar = (ProgressBar)findViewById(R.id.progressBar2);

        /*Genera com a minim una figura, tot i que sovint surten dues degut al mètode setImage() que s'inizialitza a continuació.
        Un tema que m'ha portat de cap durant dies i al final he pogut solucionar d'una manera una mica ruda: sovint Android en temps d'execució,
        pel motiu que sigui, no agafa correctament els fitxers Drawable (els arxius d'imatge, vaja) i llença un ResourcesNotFoundException
        que penja irremediablement l'aplicació. Per aquesta raó, i per assegurar l'estabilitat de l'aplicació, he ficat cada crida al constructor dins un try-catch
        que com a minim faci que, si no es pot generar una figura en un moment concret, al menys l'aplicació pugui seguir executant-se.
         */
        while (lista.size() != 1){
            try{
                Shape shape = new Shape(this, lista, constraintLayout, screenWidth,screenHeight, Commons.setPeriod(), progressBar, this, "");
                lista.add(shape);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }


        scoreBar = (LinearLayout)findViewById(R.id.scoreBar);
        scoreBar.bringToFront(); //sense aquesta crida, les noves figures es pintaràn sobreposades a la barra superior
        timer.schedule(new TimerTask() { //posa en marxa el timer i programa una tasca cada 10 segons, que es canviar la imatge de referència
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        setImage(this);
                    }
                });
            }
        }, 0, 10000); //els 10 segons expresats en milisegons
    }


    /**
     * En aquesta ocasió com que el número de possibles imatges es redueix a 5, he fet que es generi un enter aleatori entre 5 valors possibles
     * i a partir d'aquí, posi la imatge de referència i el tag corresponent mitjançant un switch.
     * @param runnable tot i que no el faig servir expressament, sempre es una bona opció passar-ho donat que es un objecte Runnable
     */
    private void setImage(Runnable runnable){
        int i = (int)Math.floor(Math.random() * 6);
        while (i== iRef){
            i = (int)Math.floor(Math.random() * 6);
        }
        iRef = i;

        switch (iRef) {
            case 0 : imatgeReferencia.setImageResource(R.drawable.flecha_amarilla);
                tagReferencia = "groga";
                break;
            case 1 : imatgeReferencia.setImageResource(R.drawable.flecha_azul);
                tagReferencia = "blava";
                break;
            case 2: imatgeReferencia.setImageResource(R.drawable.flecha_naranja);
                tagReferencia = "taronja";
                break;
            case 3: imatgeReferencia.setImageResource(R.drawable.flecha_roja);
                tagReferencia = "vermella";
                break;
            case 4: imatgeReferencia.setImageResource(R.drawable.flecha_verde);
                tagReferencia = "verda";
                break;
        }

        /*
        Genera un Shape igual al de la imatge de referència, si no hi ha cap actualment en joc
         */
        int counter = 0;
        for (Shape a : lista){
            a.setTagReferencia(this.tagReferencia);
            if (a.getTag().equals(this.tagReferencia)){
                counter++;
            }
        }
        if (counter == 0){
            try{
                Shape shape = new Shape(this, lista, constraintLayout, screenWidth,screenHeight, Commons.setPeriod(), progressBar, this, this.tagReferencia);
                lista.add(shape);
                scoreBar.bringToFront();
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Agafa la mida de la pantalla del dispositiu. La idea es que pugui jugar amb la proporcionalitat segons diferents mides de pantalla.
     */
    private void getScreenSize() {
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
    }

    /**
     * event onClick, aplica només als objectes Shape, que cada vegada que es crida al seu constructor,
     * implementen cadascún el seu propi onClickListener
     * @param v l'objecte on s'ha fet click
     */
    @Override
    public void onClick(View v) {
        if (v instanceof Shape){ //si es un objecte de tipus Shape
            Shape shape = (Shape) v; //declara un nou objecte

            boolean noError = true;
            do {
                try {
                    if (shape.getTag().equals(this.tagReferencia)){ //si coincideixen els tags
                        updateScore(); //actualitza la puntuació
                    } else {
                        progressBar.setProgress(progressBar.getProgress() -10); //si no, resta 10 punts de vida

                        if (progressBar.getProgress() <= 0){ //si la vida es igual o inferior a 0, crida al mètode gameOver()
                            gameOver();
                        }
                    }
                    shape.destroyShape(); //destrueix la figura en qualsevol cas
                    Shape shape1 = new Shape(this, lista, constraintLayout, screenWidth,screenHeight, Commons.setPeriod(), progressBar, this, "");
            Shape shape2 = new Shape(this, lista, constraintLayout, screenWidth,screenHeight, Commons.setPeriod(), progressBar, this, "");
            lista.add(shape1);
            lista.add(shape2);
                } catch (Exception e){
                    //noError = false;
                }
            } while (!noError);

            scoreBar.bringToFront();
        }
    }

    /**
     * Actualitza la puntuació
     */
    private void updateScore() {
        score = score + 100;
        textView.setText("Score: " + String.valueOf(score));
    }

    /**
     * Acaba la partida
     */
    private void gameOver() {
        try{
            for(Shape a : lista){
                Shape shape = a;
                shape.destroyShape();
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        timer.cancel();
        timer.purge();

        Toast.makeText(this, "GAME OVER - " +String.valueOf(score)+ " points.", Toast.LENGTH_LONG).show();
        finish();
    }

    //getters i setters
    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public ConstraintLayout getConstraintLayout() {
        return constraintLayout;
    }

    public void setConstraintLayout(ConstraintLayout constraintLayout) {
        this.constraintLayout = constraintLayout;
    }

    public LinearLayout getScoreBar() {
        return scoreBar;
    }

    public void setScoreBar(LinearLayout scoreBar) {
        this.scoreBar = scoreBar;
    }

    public ArrayList<Shape> getLista() {
        return lista;
    }

    public void setLista(ArrayList<Shape> lista) {
        this.lista = lista;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }


}
