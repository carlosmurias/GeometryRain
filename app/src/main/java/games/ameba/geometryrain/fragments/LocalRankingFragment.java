package games.ameba.geometryrain.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;

import games.ameba.geometryrain.Commons;
import games.ameba.geometryrain.R;
import games.ameba.geometryrain.RankedUser;
import games.ameba.geometryrain.adapters.AdaptadorLocal;
import games.ameba.geometryrain.controllers.RankingController;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocalRankingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocalRankingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocalRankingFragment extends Fragment implements Spinner.OnItemSelectedListener {
    ArrayList<RankedUser> usuaris;
    AdaptadorLocal adapter;
    RecyclerView recyclerView;
    Spinner spinner;
    String currentCountry;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public LocalRankingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocalRankingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocalRankingFragment newInstance(String param1, String param2) {
        LocalRankingFragment fragment = new LocalRankingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }
    //TODO: recargarRecicler local ranking
    private void recargarRecicler() {
        //Preparo l'adaptador
        adapter = new AdaptadorLocal(this.getContext(), usuaris);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_local_ranking, container, false);
        //Agafo el pais que llistarà per defecte:
        Bundle b = this.getArguments();
        if (b != null){
            currentCountry = b.getString("country");
        } else {
            currentCountry = "";
        }

        //Preparo la llista d'usuaris
        usuaris = new ArrayList<RankedUser>();
        //Referencio el RecyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.rView);
        //Referencio l'spinner i afegeixo el listener per al canvi d'element seleccionat
        spinner = (Spinner) view.findViewById(R.id.rankSpinner);
        countries();
        spinner.setOnItemSelectedListener(this);

        spinner.setSelection(getIndex(spinner, currentCountry));


        //afegim l'adaptador amb les dades i el LinearLayoutManager que pintarà les dades
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        //aquesta funció actualitza el recycler
        recargarRecicler();

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter.notifyDataSetChanged();


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        new RankingController().getCountryTop(currentCountry, 10).setOnSignInListener(new RankingController.onRankingResultListener() {
            @Override
            public void onRankingResult(ArrayList<RankedUser> rankedUsers) {
                // rankedUsers és el arraylist con los resultados
                for (RankedUser r : rankedUsers) {
                    usuaris.add(r);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e) {
                Log.d("BACKEND", "Error getting documents: ", e);
            }
        });
    }



    private void countries(){
        String[] paisos = Commons.getCountries();
        List<String> countries = new ArrayList<>();
        countries.add("");

        for (String pais : paisos) {
            countries.add(pais);
        }

        Collections.sort(countries);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_spinner_item, countries);
        spinner.setAdapter(adapter);
    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            currentCountry = spinner.getSelectedItem().toString();
            System.out.println(currentCountry);
        new RankingController().getCountryTop(currentCountry, 10).setOnSignInListener(new RankingController.onRankingResultListener() {
            @Override
            public void onRankingResult(ArrayList<RankedUser> rankedUsers) {
                // rankedUsers és el arraylist con los resultados
                try{
                    for(RankedUser ru : usuaris){
                        usuaris.clear();
                    }
                } catch (ConcurrentModificationException c){
                    //Aquest error apareix a vegades al canviar d'element
                }

                for (RankedUser r : rankedUsers) {
                    usuaris.add(r);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e) {
                Log.d("BACKEND", "Error getting documents: ", e);
            }
        });


        //spinner.setSelection(getIndex(spinner, currentCountry));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
