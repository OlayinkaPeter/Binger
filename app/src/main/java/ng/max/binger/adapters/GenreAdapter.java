package ng.max.binger.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ng.max.binger.R;
import ng.max.binger.data.Genre;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {
    private List<Genre> genreList;
    private int rowLayout;
    private Context context;


public static class GenreViewHolder extends RecyclerView.ViewHolder {
    TextView genreName;

    public GenreViewHolder(View v) {
        super(v);
        genreName = (TextView) v.findViewById(R.id.genreName);
    }
}

    public GenreAdapter(List<Genre> genreList, int rowLayout, Context context) {
        this.genreList = genreList;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public GenreAdapter.GenreViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new GenreViewHolder(view);
    }


    @Override
    public void onBindViewHolder(GenreViewHolder holder, final int position) {
        holder.genreName.setText(genreList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }
}