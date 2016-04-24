package pheodor.ru.yandextestartists;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;


public class ArtistClient {

    private static final String ARTISTS_URL = "http://download.cdn.yandex.net/mobilization-2016/artists.json";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void getAllArtists(AsyncHttpResponseHandler responseHandler) {
        client.get(ARTISTS_URL, null, responseHandler);
    }

}
