package games.ameba.geometryrain.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import games.ameba.geometryrain.R;
import games.ameba.geometryrain.User;
import games.ameba.geometryrain.adapters.Adaptador;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GlobalRankingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GlobalRankingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GlobalRankingFragment extends Fragment {
    ArrayList<User> usuaris;
    Adaptador adapter;
    RecyclerView recyclerView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public GlobalRankingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GlobalRankingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GlobalRankingFragment newInstance(String param1, String param2) {
        GlobalRankingFragment fragment = new GlobalRankingFragment();
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
    //TODO: recargarRecicler global ranking
    private void recargarRecicler() {
        //carrega a la llista els llibres guardats a la BD
        //llibres = mostrarTots();
        //Preparo l'adaptador
        adapter = new Adaptador(this.getContext(), usuaris);
        //estableixo l'onClickListener
        /*adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //agafa la posició del llibre que toquis dins el recyclerView
                int position = recyclerView.getChildAdapterPosition(v);
                //crea un nou objecte Llibre igual que el de la posició seleccionada
                Llibre llibre = adapter.getItemAt(position);
                //ho passa per paràmetre a la funció obrir detalls
                obrirDetalls(llibre);
            }
        });*/
        recyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_global_ranking, container, false);
        //Preparo la llista de llibres
        usuaris = new ArrayList<User>();

        //dades de prova
        usuaris.add(new User("Mobutu",19500));
        usuaris.add(new User("Hinata",1200));
        usuaris.add(new User("Lagertha",7900));
        usuaris.add(new User("Wilson Gonsales",4900));

        //Referencio el RecyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.rViewGlobal);

        //afegim l'adaptador amb les dades i el LinearLayoutManager que pintarà les dades
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        //aquesta funció actualitza el recycler, seguint indicacions del tauler
        recargarRecicler();

        //Això ho he preferit deixar com estava a l'enunciat, perque no em fa nosa
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //I això també
        adapter.notifyDataSetChanged();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    public ArrayList<User> mostrarTots(){
        /*ArrayList<Llibre> llistaLlibres = new ArrayList<Llibre>();
        //Obrir base de dades
        bd.obre();
        //Crida la BD per obtenir tots els llibres
        Cursor c = bd.obtenirTotsElsLlibres();

        //Situa el cursor a l'inici
        if (c.moveToFirst()) {
            do {
                //carrego els llibres a l'arrayList que és la font del RecyclerView, mentre hagi llibres que passar
                llistaLlibres.add(carregaLlibre(c));
            } while (c.moveToNext());
        }
        //Tanca la base de dades
        bd.tanca();
        return llistaLlibres;*/

        return usuaris;
    }
}
