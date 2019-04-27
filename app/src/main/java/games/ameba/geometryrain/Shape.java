package games.ameba.geometryrain;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Shape extends android.support.v7.widget.AppCompatImageView {

    //Agafa la mida de la pantalla per ubicar la figura
    private int screenWidth;
    private int screenHeight;

    //Eix de coordenades per indicar el punt d'inici i els posteriors desplaçaments
    private float x;
    private float y;

    //Un timer i un handler per renderitzar el desplaçament
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    //layout
    private ConstraintLayout constraintLayout;

    //un String anomenat tag, o etiqueta, que aportarà informació sobre la figura
    private String tag;
    private String tagReferencia;

    private ArrayList<Shape> lista;

    private ProgressBar progressBar;

    Activity activity;

    /**
     * El constructor de Shape. Cada Shape és independent de la resta, actuen com a fils.
     * @param context el context de l'objecte
     * @param lista la llista creada a GameActivity, que des d'aquí també s'afegeixen i eliminen objectes
     * @param constraintLayout el layout on es pintaran les figures
     * @param screenWidth l'amplada de la pantalla del dispositiu
     * @param screenHeight l'alçada de la pantalla del dispositiu
     * @param period la periodicitat amb que s'executa el timertask
     * @param progressBar la barra de vida, que des d'aquí també es controla parcialment
     * @param activity rep per paràmetre el GameActivity que genera aquest fil i, sota determinades circumstàncies, finalitza el joc.
     */
    public Shape(Context context, ArrayList<Shape> lista, ConstraintLayout constraintLayout, int screenWidth,
                 int screenHeight, int period, ProgressBar progressBar, @Nullable Activity activity, @Nullable String tagRef) {
        super(context);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        this.activity = activity;
        this.progressBar = progressBar;
        this.screenWidth = screenWidth; //rep per paràmetre la mida de la pantalla
        this.screenHeight = screenHeight;
        if (tagRef.isEmpty()){
            this.setImageResource(setRandomImage()); //estableix la figura i la seva etiqueta
        } else {
            this.setImageResource(forceImage(tagRef));
        }

        this.tag = tag;
        this.tagReferencia = tagReferencia;
        this.constraintLayout = constraintLayout;
        constraintLayout.addView(this);
        this.lista = lista;

        //com que les fletxes son rectangulars, de moment he fet que aquestes tinguin de mida una desena part del seu contenidor
        this.getLayoutParams().height = screenHeight / 10;
        this.getLayoutParams().width= screenWidth /10;
        //ubica la figura fora de la pantalla
        this.setX(-80.0f);
        this.setY(screenHeight + 80.f);
        //estableix un click listener per al Shape en el context on sigui creat, en aquest cas, el GameActivity
        this.setOnClickListener((OnClickListener) this.getContext());
        //posa en marxa el timer
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        changePos(this);
                    }
                });
            }
        }, 0, period); //observeu que la periodicitat es un valor passat per paràmetre, generat pel mètode estàtic Commons.setPeriod()
    }

    /**
     * Funciona de manera semblant al seu homònim del GameActivity: un numero aleatori de 5 possibles i a partir d'aquí s'assigna un valor
     * @return l'id del drawable assignat, per indicar al constructor quina imatge i quin tag ha de dur el shape
     */
    private int setRandomImage(){
        int i = Commons.randomInt(5);
        int ret = -1;
        switch (i) {
            case 0 : ret = R.drawable.flecha_amarilla;
                tag = "groga";
                break;
            case 1 : ret = R.drawable.flecha_azul;
                tag = "blava";
                break;
            case 2: ret = R.drawable.flecha_naranja;
                tag = "taronja";
                break;
            case 3: ret = R.drawable.flecha_roja;
                tag = "vermella";
                break;
            case 4: ret = R.drawable.flecha_verde;
                tag = "verda";
                break;
        }

        //figura per recuperar vida
        i = Commons.randomInt(100);
        System.out.print("valor aleatori: "+ i);
        if ((i < 10) && (progressBar.getProgress() < 50)){
            ret = R.drawable.heart;
            tag = "vida";
        }

        return ret;
    }

    private int forceImage(String tagRef){
        int ret = -1;
        tag = tagRef;
        switch (tagRef) {
            case "groga" : ret = R.drawable.flecha_amarilla;

                break;
            case "blava" : ret = R.drawable.flecha_azul;

                break;
            case "taronja": ret = R.drawable.flecha_naranja;

                break;
            case "vermella": ret = R.drawable.flecha_roja;

                break;
            case "verda": ret = R.drawable.flecha_verde;

                break;
        }
        return ret;
    }


    /**
     * amb cada crida a aquest mètode, el valor de Y del shape augmenta en 10. Mentres el fil segueixi actiu, en arribar al final de la pantalla,
     * aquest tornarà amunt de tot, a -100y
     * @param runnable
     */
    private void changePos(Runnable runnable) {
        y += 10;
        if (this.getY() > screenHeight){ //si el valor de Y es superior a l'alçada de la pantalla
            try {
                if(this.getTag().equals(this.tagReferencia)){ // si s'escapa una figura bona, resta vida
                    progressBar.setProgress(progressBar.getProgress() -1); //com que aquest mètode es crida amb una periodicitat aleatoria, la pèrdua també ho es, entre 3 i 6 punts aprox.
                }
                if (progressBar.getProgress() <= 0){ //si en aquest moment la vida es igual o inferior a 0, acaba l'activity
                    activity.finish();
                }
            } catch (Exception e){
                System.out.println(e.getMessage());

            }
            //reseteja la posició del shape, amb un valor aleatori per a X i amb un valor de Y inferior a 0, és a dir, per "d'amunt" de la pantalla
            x = (float)Math.floor(Math.random()* (screenWidth - this.getWidth()));
            y = -100.f;
        }
        this.setX(x);
        this.setY(y);
    }

    /**
     * Elimina el shape de la llista, del layout, i finalitza el timer
     */
    public void destroyShape() {
        try {
            constraintLayout.removeView(this);
            lista.remove(this);
            timer.cancel();
            timer.purge();

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    //getters i setters
    public String getTagReferencia() {
        return tagReferencia;
    }

    public void setTagReferencia(String tagReferencia) {
        this.tagReferencia = tagReferencia;
    }

    @Override
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


}
