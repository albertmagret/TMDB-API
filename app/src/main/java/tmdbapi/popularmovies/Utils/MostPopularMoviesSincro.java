package tmdbapi.popularmovies.Utils;

/**
 * Created by albert on 09/02/17.
 */

import android.content.Context;
import android.os.AsyncTask;

import tmdbapi.popularmovies.Models.Movies;
import tmdbapi.popularmovies.Models.Page;


public class MostPopularMoviesSincro extends AsyncTask <Integer, Void, String>{

    Context context;
    public static final int CONNECTON_TIMEOUT_MILLISECONDS = 60000;
    public OnMoviesSincroListener onMoviesSincroListener;


    public MostPopularMoviesSincro(Context context, OnMoviesSincroListener onMoviesSincroListener){
        this.context = context;
        this.onMoviesSincroListener = onMoviesSincroListener;
    }


    @Override
    protected String doInBackground(Integer... params) {
        String messageResult = null;
        Page pageObj = new Page();
        int page = params[0];

        String url = ComunicationsUtils.GET_MOST_POPULAR_MOVIES+""+page;

        ComunicationsUtils comunicationsUtils = new ComunicationsUtils();

        String response = comunicationsUtils.getMovies(url);  //getMovies(page);

        if (response != null && !response.isEmpty()
                && !response.startsWith("1. ERROR : ")
                && !response.startsWith("2. ERROR : "))
            pageObj = comunicationsUtils.extractJSONMovies(response);
        else {
            messageResult = response;
            pageObj = null;
        }


        if  (pageObj != null) {
            messageResult = "SUCCESS";
            ComunicationsUtils.TOTAL_PAGES = pageObj.getTotal_pages();
            ComunicationsUtils.CURRENT_PAGES = pageObj.getPage();
            for (Movies movie : pageObj.getMoviesListFromPage()){
                Movies.addMovieToList(movie);
            }
        }


        return messageResult;
    }

    @Override
    protected void onPostExecute(String message) {
        super.onPostExecute(message);

        if (message == null){
            onMoviesSincroListener.OnMoviesSincroFailed(message);
        }else{
            onMoviesSincroListener.OnMoviesSincroSuccess(message);
        }


    }



    public interface OnMoviesSincroListener{
        public void OnMoviesSincroSuccess(String result);
        public void OnMoviesSincroFailed(String result);
    }


}
/*
    "page": 3,
      "results": [
        {
          "poster_path": "/adw6Lq9FiC9zjYEpOqfq03ituwp.jpg",
          "adult": false,
          "overview": "A ticking-time-bomb insomniac and a slippery soap salesman channel primal male aggression into a shocking new form of therapy. Their concept catches on, with underground \"fight clubs\" forming in every town, until an eccentric gets in the way and ignites an out-of-control spiral toward oblivion.",
          "release_date": "1999-10-15",
          "genre_ids": [
                18
              ],
          "id": 550,
          "original_title": "Fight Club",
          "original_language": "en",
          "title": "Fight Club",
          "backdrop_path": "/wSJPjqp2AZWQ6REaqkMuXsCIs64.jpg",
          "popularity": 13.83249,
          "vote_count": 6274,
          "video": false,
          "vote_average": 8.1
        },
    {},
    ...
    ],
    "total_results": 19564,
    "total_pages": 979




private String getMovies(int page){
        String stringResult = null;

        String url_str = ComunicationsUtils.GET_MOST_POPULAR_MOVIES+""+page;
        URL request_url = null;

        try {
            request_url = new URL(url_str);
            urlConnection = (HttpsURLConnection) request_url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(95 * 1000);
            urlConnection.setConnectTimeout(95 * 1000);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("X-Environment", "android");
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
            e.printStackTrace();
            stringResult = "1. ERROR : "+e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            stringResult = "2. ERROR : "+e.getMessage();
        }

        return stringResult;
    }


    private Page extractJSONMovies(String JSONstring){

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


*/