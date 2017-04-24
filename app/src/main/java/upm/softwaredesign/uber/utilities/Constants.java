package upm.softwaredesign.uber.utilities;

/**
 * Created by Aleksandar on 22/03/2017.
 */

public class Constants {

    public static final String SERVER_URL = "https://uber-server.herokuapp.com/";
    public static final String REQUEST_TRIP_URL = SERVER_URL + "api/trip/";
    public static final String TRIP_STATUS_URL = SERVER_URL + "api/trip/status/"; // append [trip_id] to this
    public static final String Register_URL = SERVER_URL+"api/user/register";
    public static final String Login_URL = SERVER_URL+"api/user/login";
    public static final String Logout_URL = SERVER_URL+ "api/user/logout";

    public static final Integer TRIP_STATUS_INTENT_FLAG = 999;
    public static final String TRIP_ID = "tripID";
    public static final String TRIP_STATUS = "tripStatus";


    public final static String ALBY_API_KEY = "YmhWtQ.EumZvQ:Mu32PO24pKaAUgDh";

}
