package pheodor.ru.yandextestartists;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.loopj.android.image.SmartImageView;
import java.util.List;

public class ArtistAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflater;
    List<Artist> mItems;

    public ArtistAdapter(Context context, List<Artist> artists) {
        mItems = artists;
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            view = mInflater.inflate(R.layout.item_artist, parent, false);
        }

        Artist artist = getArtist(position);

        TextView name = (TextView) view.findViewById(R.id.name);
        TextView genres = (TextView) view.findViewById(R.id.genres);
        TextView tracks = (TextView) view.findViewById(R.id.tracks);
        SmartImageView cover = (SmartImageView) view.findViewById(R.id.cover);
        name.setText(artist.name);
        genres.setText(artist.getGenres());
        tracks.setText(artist.getStats(mContext, R.string.text_stats_list));

        // Если есть изображение, загружаем и автоматически кэшируем
        // с помощью SmartImage (http://loopj.com/android-smart-image-view/)
        if(artist.coverSmall != null) {
            cover.setImageUrl(artist.coverSmall, R.drawable.user);
        }
        else {
            // Иначе устанавливаем свое изображение по умолчанию
            cover.setImageResource(R.drawable.user);
        }

        // Добавляем анимацию для позиции списка
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade);
        animation.setDuration(500);
        view.startAnimation(animation);
        animation = null;

        return view;
    }

    Artist getArtist(int position) {
        return (Artist) getItem(position);
    }
}
