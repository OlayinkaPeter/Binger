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

import ng.max.binger.R;
import ng.max.binger.data.AppDatabase;
import ng.max.binger.data.TvShow;

import java.util.List;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.MovieViewHolder> {

    private List<TvShow> tvShowList;
    private int rowLayout;
    private Context context;

    private static final String DATABASE_NAME = "favorite_movies_db";
    private AppDatabase appDatabase;

    private OnMovieItemClicked listener;

    private ScaleAnimation scaleAnimation;
    private BounceInterpolator bounceInterpolator;


    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout moviesLayout;
        TextView movieTitle;
        TextView date;
        TextView movieDescription;
        TextView rating;
        ImageView poster;
        ImageView favoriteButton;

        public MovieViewHolder(View v) {
            super(v);
            moviesLayout = v.findViewById(R.id.moviesLayout);
            movieTitle = v.findViewById(R.id.videoTitle);
            date = v.findViewById(R.id.productionYear);
            movieDescription = v.findViewById(R.id.videoDescription);
            rating = v.findViewById(R.id.videoRating);
            poster = v.findViewById(R.id.videoPoster);
            favoriteButton = v.findViewById(R.id.favoriteButton);
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

//        animateFavButton();
//        holder.favoriteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                //animation
//                compoundButton.startAnimation(scaleAnimation);
//            }
//        });
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

    public void animateFavButton() {
        scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
        scaleAnimation.setDuration(500);
        bounceInterpolator = new BounceInterpolator();
        scaleAnimation.setInterpolator(bounceInterpolator);
    }
}





