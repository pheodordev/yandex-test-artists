package pheodor.ru.yandextestartists;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.loopj.android.image.SmartImageView;
import org.json.JSONException;
import org.json.JSONObject;


public class ArtistActivity extends AppCompatActivity {

    Intent mIntent;
    Bundle mBundle;
    Artist mArtist;

    SmartImageView mImageCover;
    TextView mTextGenres;
    TextView mTextStats;
    TextView mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);

        mIntent = getIntent();
        mBundle = mIntent.getExtras();

        if(mBundle != null) {
            // Получаем переданную строку с JSON и создаем из неё объект
            try {
                JSONObject json = new JSONObject((String) mBundle.get("artist_json"));
                mArtist = new Artist(json);
            } catch (JSONException e) {
                e.printStackTrace();
                finish();
            }

            setTitle(mArtist.name);

            // Проверяем наличие фотографии, подгружаем ее с помощью
            // SmartImage (http://loopj.com/android-smart-image-view/)
            // и автоматически кэшируем
            mImageCover = (SmartImageView) findViewById(R.id.cover);
            if(mArtist.coverBig != null) {
                mImageCover.setImageUrl(mArtist.coverBig);
            }
            else {
                mImageCover.setVisibility(View.GONE);
            }

            mTextGenres = (TextView) findViewById(R.id.genres);
            mTextGenres.setText(mArtist.getGenres());

            mTextStats = (TextView) findViewById(R.id.stats);
            mTextStats.setText(mArtist.getStats(this, R.string.text_stats_single));

            mDescription = (TextView) findViewById(R.id.description);
            mDescription.setText(mArtist.description);

            // Добавляем кнопку "Назад" рядом с именем артиста
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
