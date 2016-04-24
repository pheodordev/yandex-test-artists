package pheodor.ru.yandextestartists;


import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class Artist {

    public long id;
    public String name;
    public int tracks;
    public int albums;
    public String link;
    public String description;
    public String coverSmall;
    public String coverBig;
    public ArrayList<String> genres;
    private String source;

    public Artist(JSONObject json) {
        // id и name считаем обязательными данными, остальные данные проверяем
        try {
            id = json.getLong("id");
            name = json.getString("name");
            tracks = json.has("tracks") && !json.isNull("tracks") ? json.getInt("tracks") : 0;
            albums = json.has("albums") && !json.isNull("albums") ? json.getInt("albums") : 0;
            link = json.has("link") ? json.getString("link") : null;
            description = json.has("description") ? json.getString("description") : null;

            coverSmall = null;
            coverBig = null;
            // Проверяем наличие ссылок на изображения
            if(json.has("cover") && !json.isNull("cover")) {
                JSONObject covers = json.getJSONObject("cover");
                coverSmall = covers.has("small") && !covers.isNull("small") ? covers.getString("small") : null;
                coverBig = covers.has("big") && !covers.isNull("big") ? covers.getString("big") : null;
            }

            if(json.has("genres")) {
                // Преобразовываем JSONArray в привычный ArrayList
                genres = new ArrayList<String>();
                JSONArray array = json.getJSONArray("genres");
                for(int i = 0; i < array.length(); i++) {
                    genres.add(array.getString(i));
                }
            }
            else {
                genres = null;
            }

            // Преобразуем объект в строку для простой передачи данных в другую активити
            source = json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getJSONString() {
        return source;
    }

    // Возвращает скленные в строку жанры
    public String getGenres() {
        return genres != null ? TextUtils.join(", ", genres) : "";
    }

    // Возвращает форматированную строку с информацией о песнях и альбомах
    public String getStats(Context context, int text)  {
        Resources res = context.getResources();
        String statsAlbums = plurar(albums, res.getStringArray(R.array.text_albums));
        String statsTracks = plurar(tracks, res.getStringArray(R.array.text_tracks));
        return String.format(res.getString(text), statsAlbums, statsTracks);
    }

    // Передаем функции число и формы склонения загруженные из strings.xml,
    // а в ответ получаем форматированную строку с правильным склонением
    private String plurar(int num, String[] forms) {
        int form = 3;
        int n = Math.abs(num) % 100;
        int n1 = n % 10;
        if(n > 10 && n < 20) form = 3;
        else if(n1 > 1 && n1 < 5) form = 2;
        else if(n1 == 1) form = 1;

        return String.format(forms[form], num);
    }

}
