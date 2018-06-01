package eu.activstar.ursus.myapplication;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Vlastimil Brečka (www.vlastimilbrecka.sk)
 * on 25-Oct-17.
 */
public abstract class AdapterClickListener {
    public void click(int adapterPosition) {
        if (adapterPosition != RecyclerView.NO_POSITION) {
            onClick(adapterPosition);
        }
    }

    public abstract void onClick(int position);
}
