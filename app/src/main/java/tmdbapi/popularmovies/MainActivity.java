package tmdbapi.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import tmdbapi.popularmovies.Adapters.EndlessRecyclerViewScrollListener;
import tmdbapi.popularmovies.Adapters.MoviesAdapter;
import tmdbapi.popularmovies.Models.Movies;
import tmdbapi.popularmovies.Utils.ComunicationsUtils;
import tmdbapi.popularmovies.Utils.MostPopularMoviesSincro;
import tmdbapi.popularmovies.Utils.SearchForKeywordSincro;

public class MainActivity extends AppCompatActivity {

    static int CURRENT_PAGE = 1;
    public static final String TAG = "PrivaliaTest";
    Boolean isSearchTime = false;
    CharSequence s_1 = null;

    EditText editText_search;
    RecyclerView rvMovies;

    private EndlessRecyclerViewScrollListener endlessScrollListener;
    MoviesAdapter adapter;
    MostPopularMoviesSincro mostPopularMoviesSincro;
    SearchForKeywordSincro searchForKeywordSincro;
    Context context;


    private MoviesAdapter getInstance(){

        if(adapter == null){
            adapter =  new MoviesAdapter(context, Movies.getMoviesList());
        }

        return adapter;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        adapter = null;
        this.searchForKeywordSincro = null;

        editText_search = (EditText) findViewById(R.id.editText_search);
        rvMovies = (RecyclerView) findViewById(R.id.recycleView_id);

        editText_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                s_1 = s;
                Log.w(TAG, "beforeTextChanged s_1: "+s_1.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.w(TAG, "onTextChanged");

                if (s.length()>0 && !TextUtils.isEmpty(s) && !s.equals(s_1)){
                    Log.w(TAG, "onTextChanged 1");
                    isSearchTime = true;
                    ComunicationsUtils.KEYWOORD_ID = s.toString();
                    CURRENT_PAGE = 1;

                    if (searchForKeywordSincro != null)
                        searchForKeywordSincro.cancel(true);

                    endlessScrollListener.resetState();
                    Movies.cleanList();
                    getSearchList(CURRENT_PAGE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.w(TAG, "afterTextChanged");

                if (TextUtils.isEmpty(s) && isSearchTime){
                    Log.w(TAG, "afterTextChanged 2");
                    isSearchTime = false;
                    getPageList(CURRENT_PAGE);
                }

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvMovies.setLayoutManager(linearLayoutManager);

        // Retain an instance so that you can call `resetState()` for fresh searches
        endlessScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount, RecyclerView view, int lastVisibleItemPosition) {
                if (isSearchTime)
                getSearchList(page);
                else
                getPageList(page);

                return true;
            }
        };
        rvMovies.addOnScrollListener(endlessScrollListener);

        getPageList(CURRENT_PAGE);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }



    private void getPageList(final int page){

        if (page <= ComunicationsUtils.TOTAL_PAGES) {
            Log.w(TAG,"getPageList page: "+String.valueOf(page));
            mostPopularMoviesSincro = new MostPopularMoviesSincro(context, new MostPopularMoviesSincro.OnMoviesSincroListener() {
                @Override
                public void OnMoviesSincroSuccess(String message) {
                    Log.w(TAG,message);
                    refreshList(page);

                }

                @Override
                public void OnMoviesSincroFailed(String message) {
                    Log.w(TAG,message);
                    endlessScrollListener.resetState();
                    Movies.cleanList();
                    refreshList(page);
                }
            });
            mostPopularMoviesSincro.execute(page);
        }

    }


    private void getSearchList(int page){

        if (page <= ComunicationsUtils.TOTAL_PAGES) {
            if (searchForKeywordSincro != null){
                searchForKeywordSincro.cancel(true);
                if (searchForKeywordSincro.isCancelled()){
                    Log.w(TAG,"getSearchList page: "+String.valueOf(page));
                    callSearForKewordSincro(page);
                }
            }else{
                callSearForKewordSincro(page);
            }

        }

    }


    private void callSearForKewordSincro(final int page){
        searchForKeywordSincro = new SearchForKeywordSincro(context, new SearchForKeywordSincro.OnSearchForKeywordListener() {
            @Override
            public void OnSearchForKeywordSincroSuccess(String result) {
                Log.w(TAG,result);
                refreshList(page);
            }

            @Override
            public void OnSearchForKeywordSincroFailed(String result) {
                endlessScrollListener.resetState();
                Movies.cleanList();
                refreshList(page);
            }
        });
        searchForKeywordSincro.execute(page);
    }


    private void refreshList(int page){
        if (page == 1){
            adapter = getInstance();
            rvMovies.setAdapter(adapter);
        }

        adapter.notifyDataSetChanged();
    }
}
