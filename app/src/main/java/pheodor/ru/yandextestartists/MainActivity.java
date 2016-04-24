package pheodor.ru.yandextestartists;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    private ProgressDialog mProgressBar;
    private ListView mArtistsList;
    private TextView mTextError;
    private Animation mErrorAnimation;
    private ArtistAdapter mAdapter;
    private List<Artist> mArtists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextError = (TextView) findViewById(R.id.text_error);
        mArtistsList = (ListView) findViewById(R.id.list_artists);
        mArtistsList.setOnItemClickListener(artistsClickListener);
        mErrorAnimation = AnimationUtils.loadAnimation(this, R.anim.slide);
        mErrorAnimation.setDuration(500);

        // Создаем диалог информирующий о загрузке данных
        createProgressDialog();

        if(mArtists == null) {
            getData();
        }
        else {
            refreshViews();
        }
    }

    AdapterView.OnItemClickListener artistsClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Artist item = mArtists.get(position);
            Intent intent = new Intent(MainActivity.this, ArtistActivity.class);
            // Передаем данные артиста строкой в формате JSON
            intent.putExtra("artist_json", item.getJSONString());
            startActivity(intent);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                getData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createProgressDialog() {
        mProgressBar = new ProgressDialog(this);
        mProgressBar.setTitle(getString(R.string.title_loading));
        mProgressBar.setMessage(getString(R.string.text_loading));
        mProgressBar.setIndeterminate(true);
        mProgressBar.setCancelable(false);
    }

    private void getData() {
        mArtists = null;

        // Проверяем доступ в интернет
        if(!isNetworkConnected()) {
            mTextError.setText(getText(R.string.text_disconnected));
            refreshViews();
            return;
        }
        // Скрываем ошибку и предварительно меняем текст
        mTextError.setVisibility(View.GONE);
        mTextError.setText(getText(R.string.text_error));

        // Для получения JSON используем библиотеку http://loopj.com/android-async-http/
        ArtistClient.getAllArtists(new JsonHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                refreshViews();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                mArtists = new ArrayList<>();
                try {
                    for(int i = 0; i < response.length(); i++) {
                        Artist artist = new Artist(response.getJSONObject(i));
                        mArtists.add(artist);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                refreshViews();
            }

            @Override
            public void onStart() {
                mProgressBar.show();
            }

            @Override
            public void onFinish() {
                mProgressBar.dismiss();
            }

        });
    }

    private void refreshViews() {
        // Если данные есть, формируем список, иначе выводим ошибку
        if(mArtists != null && mArtists.size() > 0) {
            mTextError.setVisibility(View.GONE);
            mArtistsList.setVisibility(View.VISIBLE);
            mAdapter = new ArtistAdapter(this, mArtists);
            mArtistsList.setAdapter(mAdapter);
        }
        else {
            mArtistsList.setVisibility(View.GONE);
            mTextError.setVisibility(View.VISIBLE);
            mTextError.startAnimation(mErrorAnimation);
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}
