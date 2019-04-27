package games.ameba.geometryrain.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import games.ameba.geometryrain.Commons;
import games.ameba.geometryrain.R;
import games.ameba.geometryrain.RankedUser;

public class AdaptadorLocal extends RecyclerView.Adapter<AdaptadorLocal.ElMeuViewHolder> implements View.OnClickListener {
    private ArrayList<RankedUser> items;
    private Context context;
    private View.OnClickListener listener;
    //Creem el constructor
    public AdaptadorLocal(Context context, ArrayList<RankedUser> items) {
        this.context = context;
        this.items= items;
    }
    //Crea noves files (l'invoca el layout manager). Aquí fem referència al layout fila.xml
    @Override
    public AdaptadorLocal.ElMeuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fila, null);
        //assignació del listener
        itemLayoutView.setOnClickListener(this);
        // create ViewHolder
        ElMeuViewHolder viewHolder = new ElMeuViewHolder(itemLayoutView);

        return viewHolder;
    }
    //Retorna la quantitat de les dades
    @Override
    public int getItemCount() {
        return items.size();
    }
    //Carreguem els widgets amb les dades (l'invoca el layout manager)
    @Override
    public void onBindViewHolder(ElMeuViewHolder viewHolder, int position) {
        /* *
         * position conté la posició de l'element actual a la llista. També l'utilitzarem
         * com a índex per a recòrrer les dades
         * */
        RankedUser user = (RankedUser) items.get(position);
        viewHolder.vPlayerName.setText(user.getUsername());
        viewHolder.vMaxScore.setText(String.valueOf(user.getScore()));
        viewHolder.vDate.setText(Commons.dateFormat(user.getDate()));
        viewHolder.vCountry.setText("");
        viewHolder.vPosition.setText(String.valueOf(position + 1));
    }
    //retorna un enter que és la posició d'un llibre dins la llista
    public RankedUser getItemAt(int position) {
        return items.get(position);
    }

    //Definim el nostre ViewHolder, és a dir, un element de la llista en qüestió
    public static class ElMeuViewHolder extends RecyclerView.ViewHolder {
        //L'exemple de l'enunciat tenia només un TextView, ara son dos
        protected TextView vPlayerName, vMaxScore, vDate, vCountry, vPosition;
        public ElMeuViewHolder(View v) {
            super(v);
            //Els referenciem al layout
            vPlayerName = (TextView) v.findViewById(R.id.playerName);
            vMaxScore = (TextView) v.findViewById(R.id.maxScore);
            vDate=(TextView)v.findViewById(R.id.scoreDate);
            vCountry=(TextView)v.findViewById(R.id.playerCountry);
            vPosition =(TextView)v.findViewById(R.id.txtPosition);
        }
    }
    //l'onClickListener de l'adaptador i, per extensió, del RecyclerView
    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }
    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }
}
