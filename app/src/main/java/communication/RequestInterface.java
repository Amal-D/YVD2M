package communication;

/**
 * Created by devkkda on 10/22/2016.
 */
import me.amald.youtubedownloader.MainActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface RequestInterface  {

    String url = "";



  //  @GET("")
   // @GET("all-we-know-don-t-wanna-know-the-chainsmokers-maroon-5-mashup-cover-by-j-fla.mp3?id=R8YnpnL8nIM&title=All+We+Know+%26+Don%27t+Wanna+Know+-+The+Chainsmokers+%26+Maroon+5+%28+MASHUP+cover+by+J.Fla+%29&t=1477129868&extra=a&ip=202.88.237.233&h=a46cc02ddfb835661ea8846d1073ea1c139b6939")
   // @Streaming
   // Call<ResponseBody> downloadFile(@Path("user") String user);
 //   Call<ResponseBody> downloadFile();
    //Call<ResponseBody> downloadFile(@Url String url);


    @GET
    @Streaming
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);


}
