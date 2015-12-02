package innoworld.cyclingmaster.network;

import java.util.List;

import innoworld.cyclingmaster.model.LoginReponse;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by latifalbar on 12/3/2015.
 */
public interface NetworkService {

    @GET("/index.php")
    List<LoginReponse> loginReponses(@Query("tag") String tag);
}
