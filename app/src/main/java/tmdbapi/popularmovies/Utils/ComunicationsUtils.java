package tmdbapi.popularmovies.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import tmdbapi.popularmovies.MainActivity;
import tmdbapi.popularmovies.Models.Movies;
import tmdbapi.popularmovies.Models.Page;

/**
 * Created by albert on 09/02/17.
 */

public class ComunicationsUtils {


    public static final String API_KEY = "93aea0c77bc168d8bbce3918cefefa45";
    public static String KEYWOORD_ID = "";

    public static String GET_MOST_POPULAR_MOVIES = "https://api.themoviedb.org/3/movie/popular?api_key="+API_KEY+"&language=en-US&page=";
    public static String GET_MOVIES_BY_KEYWORD = "https://api.themoviedb.org/3/keyword/";
    public static String GET_MOVIES_BY_KEYWORD_1 = "/movies?api_key="+API_KEY+"&language=en-US&include_adult=false&page=";
    public static String GET_IMAGE_MOVIES = "https://image.tmdb.org/t/p/w300/";


    public static int TOTAL_PAGES = 1;
    public static int CURRENT_PAGES = 1;

    private HttpsURLConnection urlConnection = null;
    private HttpsURLConnection urlConnection_img;

    public ComunicationsUtils(){}


    public String getMovies(String url){
        String stringResult = null;
        URL request_url = null;

        try {

            if (urlConnection != null) {
                urlConnection.disconnect();
                urlConnection = null;
            }

            Log.w(MainActivity.TAG,"getMovies url: "+url);
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

        } catch (MalformedURLException e) {
            //e.printStackTrace();
            stringResult = "1. ERROR : "+e.getMessage();
        } catch (IOException e) {
            //e.printStackTrace();
            stringResult = "2. ERROR : "+e.getMessage();
        }finally {
            urlConnection.disconnect();
        }

        return stringResult;
    }


    public Bitmap getImageMovies(String url){
        URL request_url = null;
        Bitmap bitmap = null;
        BufferedInputStream bufferedInputStream = null;


        try {
            request_url = new URL(url);

            if (urlConnection_img != null) {
                urlConnection_img.disconnect();
                urlConnection_img = null;
            }

            urlConnection_img = (HttpsURLConnection) request_url.openConnection();
            urlConnection_img.setRequestMethod("GET");
            urlConnection_img.setReadTimeout(95 * 1000);
            urlConnection_img.setConnectTimeout(95 * 1000);
            urlConnection_img.setDoInput(true);
            urlConnection_img.setRequestProperty("X-Environment", "android");
            urlConnection_img.setRequestProperty("Content-Type", "image/jpeg");
            urlConnection_img.setRequestProperty("Accept", "application/json");
            urlConnection_img.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection_img.getInputStream();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }

            bufferedInputStream = new BufferedInputStream(inputStream, 8192); // 8192
            bitmap = BitmapFactory.decodeStream(bufferedInputStream);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            bitmap = null;
        } catch (IOException e) {
            e.printStackTrace();
            bitmap = null;
        } finally {
            urlConnection_img.disconnect();
        }

        return bitmap;

    }





    public Page extractJSONMovies(String JSONstring){

        Page page = new Page();

        try{
            JSONObject jsonObject = new JSONObject(JSONstring);
            if (!jsonObject.isNull("page"))
                page.setPage(jsonObject.getInt("page"));
            if (!jsonObject.isNull("total_results"))
                page.setTotal_results(jsonObject.getInt("total_results"));
            if (!jsonObject.isNull("total_pages"))
                page.setTotal_pages(jsonObject.getInt("total_pages"));
            if (jsonObject.get("results") != null){

                List<Movies> moviesList = new ArrayList<>();
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                for (int pos = 0; pos < jsonArray.length(); pos++){
                    Movies movie = new Movies();
                    JSONObject jsonObject_movie = jsonArray.getJSONObject(pos);
                    if (jsonObject_movie.has("poster_path")){
                        movie.setPoster_path(jsonObject_movie.getString("poster_path"));
                    }
                    if (!jsonObject_movie.isNull("adult")){
                        jsonObject_movie.getBoolean("adult");
                    }
                    if (jsonObject_movie.has("overview")){
                        movie.setOverview(jsonObject_movie.getString("overview"));
                    }
                    if (jsonObject_movie.has("release_date")){
                        movie.setRelease_date(convertStringToDate(jsonObject_movie.getString("release_date")));
                    }
                    if (!jsonObject_movie.isNull("id")){
                        movie.setId(jsonObject_movie.getInt("id"));
                    }
                    if (jsonObject_movie.has("original_title")){
                        movie.setOriginal_title(jsonObject_movie.getString("original_title"));
                    }
                    if (jsonObject_movie.has("original_language")){
                        movie.setOriginal_language(jsonObject_movie.getString("original_language"));
                    }
                    if (jsonObject_movie.has("title")){
                        movie.setTitle(jsonObject_movie.getString("title"));
                    }
                    if (jsonObject_movie.has("backdrop_path")){
                        movie.setBackdrop_path(jsonObject_movie.getString("backdrop_path"));
                    }
                    if (!jsonObject_movie.isNull("popularity")){
                        movie.setPopularity(jsonObject_movie.getInt("popularity"));
                    }
                    if (!jsonObject_movie.isNull("vote_count")){
                        movie.setVote_count(jsonObject_movie.getInt("vote_count"));
                    }
                    if (jsonObject_movie.isNull("video")){
                        movie.setVideo(jsonObject_movie.getBoolean("video"));
                    }
                    if (jsonObject_movie.has("vote_average")){
                        movie.setVote_average(jsonObject_movie.getDouble("vote_average"));
                    }

                    moviesList.add(pos, movie);

                }

                page.setMoviesListToPage(moviesList);
            }


        }catch (JSONException e){
            e.printStackTrace();
            page = null;
        }

        return page;
    }

    private Date convertStringToDate(String dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return convertedDate;
    }

}
