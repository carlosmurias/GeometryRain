package games.ameba.geometryrain;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import games.ameba.geometryrain.fragments.GlobalRankingFragment;
import games.ameba.geometryrain.fragments.LocalRankingFragment;

public class RankingActivity extends AppCompatActivity implements View.OnClickListener,
        GlobalRankingFragment.OnFragmentInteractionListener, LocalRankingFragment.OnFragmentInteractionListener {

    Button btnLocal, btnGlobal;
    GlobalRankingFragment globalRankingFragment;
    LocalRankingFragment localRankingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        btnLocal = (Button) findViewById(R.id.btnLocal);
        btnGlobal = (Button) findViewById(R.id.btnGlobal);

        btnGlobal.setOnClickListener(this);
        btnLocal.setOnClickListener(this);
        //ara toca posar el fragment llanterna com a fragment per defecte dins el frameLayout
        localRankingFragment = new LocalRankingFragment();
        //mitjançant aquest mètode li puc dir que posi el fragment indicat dins el contenidor
        getSupportFragmentManager().beginTransaction().add(R.id.contenidor, localRankingFragment).commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) { //com que son set fragments, és millor fer servir un switch/case que no pas anidar if/elses
            case R.id.btnLocal:
                localRankingFragment = new LocalRankingFragment(); //declara nova instància
                //mitjançant un objecte de tipus FragmentTransaction es pot fer el canvi de fragment al contenidor
                FragmentTransaction transitionLLant = getSupportFragmentManager().beginTransaction();
                //remplaça el fragment antic pel nou
                transitionLLant.replace(R.id.contenidor, localRankingFragment);
                //acometeix els canvis
                transitionLLant.commit();
                break;
            case R.id.btnGlobal:
                globalRankingFragment = new GlobalRankingFragment();
                FragmentTransaction transitionCam = getSupportFragmentManager().beginTransaction();
                transitionCam.replace(R.id.contenidor, globalRankingFragment);
                transitionCam.commit();
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
