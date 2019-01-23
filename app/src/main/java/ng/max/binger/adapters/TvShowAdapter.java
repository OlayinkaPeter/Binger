package ng.max.binger.adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.max.binger.R;
import ng.max.binger.data.AppDatabase;
import ng.max.binger.data.TvShow;

import java.util.List;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.MovieViewHolder> {

    private List<TvShow> tvShowList;
    private int rowLayout;
    private Context context;

    private OnMovieItemClicked listener;

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.moviesLayout)
        ConstraintLayout moviesLayout;
        @BindView(R.id.videoTitle)
        TextView movieTitle;
        @BindView(R.id.productionYear)
        TextView date;
        @BindView(R.id.videoDescription)
        TextView movieDescription;
        @BindView(R.id.videoRating)
        TextView rating;
        @BindView(R.id.videoPoster)
        ImageView poster;
        @BindView(R.id.favoriteButton)
        ImageView favoriteButton;

        public MovieViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public TvShowAdapter(List<TvShow> tvShowList, int rowLayout, Context context) {
        this.tvShowList = tvShowList;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public TvShowAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        holder.movieTitle.setText(tvShowList.get(position).getName());
        holder.date.setText(tvShowList.get(position).getReleaseDate());
        holder.movieDescription.setText(tvShowList.get(position).getOverview());
        holder.rating.setText("Rating: " + tvShowList.get(position).getVoteAverage().toString());
        Glide.with(context).load("https://image.tmdb.org/t/p/w500/" + tvShowList.get(position).getPosterPath()).into(holder.poster);

        holder.moviesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.containerClicked(position);
                }
            }
        });

        holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.favoriteButtonClicked(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tvShowList.size();
    }

    public void setListener(OnMovieItemClicked onMovieItemClicked) {
        this.listener = onMovieItemClicked;
    }

    public interface OnMovieItemClicked {
        void favoriteButtonClicked(int position);
        void containerClicked(int position);
    }
}