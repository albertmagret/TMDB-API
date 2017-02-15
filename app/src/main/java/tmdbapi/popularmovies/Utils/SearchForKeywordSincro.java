package tmdbapi.popularmovies.Utils;

/**
 * Created by albert on 09/02/17.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import tmdbapi.popularmovies.MainActivity;
import tmdbapi.popularmovies.Models.Movies;
import tmdbapi.popularmovies.Models.Page;


public class SearchForKeywordSincro extends AsyncTask<Integer, Void, String> {

    Context context;
    public static final int CONNECTON_TIMEOUT_MILLISECONDS = 60000;
    public OnSearchForKeywordListener onSearchForKeywordListener;
    private volatile boolean running = true;
    private HttpsURLConnection urlConnection = null;

    public SearchForKeywordSincro(Context context, OnSearchForKeywordListener onSearchForKeywordListener){
        this.context = context;
        this.onSearchForKeywordListener = onSearchForKeywordListener;
        running = true;
    }

    @Override
    protected String doInBackground(Integer... params) {
        String messageResult = null;
        while (running) {
            // does the hard work
            ComunicationsUtils comunicationsUtils = new ComunicationsUtils();
            Page pageObj = new Page();

            // params[]
            String page = String.valueOf(params[0]);

            // url
            String url = ComunicationsUtils.GET_MOVIES_BY_KEYWORD+""+ComunicationsUtils.KEYWOORD_ID+""+ComunicationsUtils.GET_MOVIES_BY_KEYWORD_1+""+page;
            Log.w(MainActivity.TAG, "url: "+url);

            // response
            String stringResult = null;
            URL request_url = null;

            try {

                if (urlConnection != null) {
                    urlConnection.disconnect();
                    urlConnection = null;
                }

                request_url = new URL(url);
                urlConnection = (HttpsURLConnection) request_url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(95 * 1000);
                urlConnection.setConnectTimeout(95 * 1000);
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("X-Environment", "android");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer buffer = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                stringResult = buffer.toString();

                if (stringResult != null && !stringResult.isEmpty()
                        && !stringResult.startsWith("1. ERROR : ")
                            && !stringResult.startsWith("2. ERROR : "))
                    pageObj = comunicationsUtils.extractJSONMovies(stringResult);
                else {
                    messageResult = null;
                    pageObj = null;
                }


                if  (pageObj != null) {
                    messageResult = "SUCCESS";

                    for (Movies movie : pageObj.getMoviesListFromPage()){
                        Movies.addMovieToList(movie);
                    }
                }

            } catch (MalformedURLException e) {
                //e.printStackTrace();
                stringResult = "1. ERROR : "+e.getMessage();
                Log.w(MainActivity.TAG, stringResult);
                messageResult = null;
            }catch (FileNotFoundException e){
                //e.printStackTrace();
                stringResult = "2. ERROR : "+e.getMessage();
                Log.w(MainActivity.TAG, stringResult);
                messageResult = null;
            }catch (InterruptedIOException e){
                //e.printStackTrace();
                stringResult = "3. ERROR : "+e.getMessage();
                Log.w(MainActivity.TAG, stringResult);
                messageResult = null;
            }catch (IOException e) {
                //e.printStackTrace();
                stringResult = "4. ERROR : "+e.getMessage();
                Log.w(MainActivity.TAG, stringResult);
                messageResult = null;
            }finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }

            Log.w(MainActivity.TAG,"running..");
            return messageResult;
        }

        return messageResult;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Log.w(MainActivity.TAG,"cancelled..");
        if (urlConnection != null)
            urlConnection.disconnect();
        running = false;
    }

    @Override
    protected void onPostExecute(String message) {
        super.onPostExecute(message);

        if (message == null && onSearchForKeywordListener != null){
            onSearchForKeywordListener.OnSearchForKeywordSincroFailed(message);
        }else if (onSearchForKeywordListener != null){
            onSearchForKeywordListener.OnSearchForKeywordSincroSuccess(message);
        }

    }


    public interface OnSearchForKeywordListener{
        public void OnSearchForKeywordSincroSuccess(String result);
        public void OnSearchForKeywordSincroFailed(String result);
    }


}