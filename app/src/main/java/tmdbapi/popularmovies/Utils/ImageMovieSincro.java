package tmdbapi.popularmovies.Utils;

/**
 * Created by albert on 11/02/17.
 */


import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;

import tmdbapi.popularmovies.Adapters.MoviesAdapter;

public class ImageMovieSincro extends AsyncTask<String, Void, Bitmap> {

    Context context;
    public static final int CONNECTON_TIMEOUT_MILLISECONDS = 60000;
    public OnImageMovieSincroListener onImageMovieSincroListener;
    MoviesAdapter.ViewHolder viewHolder;

    public ImageMovieSincro(Context context, MoviesAdapter.ViewHolder viewHolder, OnImageMovieSincroListener onImageMovieSincroListener){
        this.context = context;
        this.onImageMovieSincroListener = onImageMovieSincroListener;
        this.viewHolder = viewHolder;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        viewHolder.imageView.setImageBitmap(null);
        viewHolder.imageView.setVisibility(View.INVISIBLE);
        viewHolder.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap bitmap = null;
        ComunicationsUtils comunicationsUtils = new ComunicationsUtils();

        // params[]
        String path_image = params[0];

        // url
        String url = ComunicationsUtils.GET_IMAGE_MOVIES+""+path_image;

        // image bitmap
        bitmap = comunicationsUtils.getImageMovies(url);  //getMovies(page);


        return bitmap;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        if (bitmap == null){
            viewHolder.progressBar.setVisibility(View.VISIBLE);
            viewHolder.imageView.setVisibility(View.INVISIBLE);
            viewHolder.imageView.setImageBitmap(null);
            onImageMovieSincroListener.OnImageMovieSincroFailed();
        }else{
            viewHolder.progressBar.setVisibility(View.GONE);
            viewHolder.imageView.setVisibility(View.VISIBLE);
            viewHolder.imageView.setImageBitmap(bitmap);
            onImageMovieSincroListener.OnImageMovieSincroSuccess();
        }

    }


    public interface OnImageMovieSincroListener{
        public void OnImageMovieSincroSuccess();
        public void OnImageMovieSincroFailed();
    }


}