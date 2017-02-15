package tmdbapi.popularmovies.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import tmdbapi.popularmovies.Models.Movies;
import tmdbapi.popularmovies.R;
import tmdbapi.popularmovies.Utils.ImageMovieSincro;

/**
 * Created by albert on 10/02/17.
 */

public class MoviesAdapter extends
        RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    ImageMovieSincro imageMovieSincro;
    private List<Movies> moviesList;
    private Context mContext;

    public MoviesAdapter(Context context, List<Movies> list) {
        this.moviesList = list;
        this.mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitle;
        public TextView textViewOverview;
        public ImageView imageView;
        public ProgressBar progressBar;


        public ViewHolder(View itemView) {
            super(itemView);

            textViewTitle = (TextView) itemView.findViewById(R.id.text_title_id);
            textViewOverview = (TextView) itemView.findViewById(R.id.text_overview_id);
            imageView = (ImageView) itemView.findViewById(R.id.image_row_id);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_id);

        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.movies_row, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        Movies movies = moviesList.get(position);
        Calendar cal = Calendar.getInstance();
        cal.setTime(movies.getRelease_date());

        getImage(viewHolder, movies);
        viewHolder.textViewTitle.setText(movies.getTitle()+" -- "+cal.get(Calendar.YEAR));
        viewHolder.textViewOverview.setText(movies.getOverview());

    }

    @Override
    public int getItemCount() {

        if (moviesList != null)
            return moviesList.size();
        else
            return 0;

    }

    public void getImage(ViewHolder viewHolder, Movies movie){

        if (!movie.getPoster_path().equals("null")
                && !movie.getPoster_path().isEmpty()){
            imageMovieSincro = new ImageMovieSincro(mContext, viewHolder, new ImageMovieSincro.OnImageMovieSincroListener() {
                @Override
                public void OnImageMovieSincroSuccess() {

                }

                @Override
                public void OnImageMovieSincroFailed() {

                }

            });
            imageMovieSincro.execute(movie.getPoster_path());
        }

    }


}