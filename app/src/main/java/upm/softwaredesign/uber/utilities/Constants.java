package upm.softwaredesign.uber.utilities;

/**
 * Created by Aleksandar on 22/03/2017.
 */

public class Constants {

    public static final String SERVER_URL = "https://uber-server.herokuapp.com";
    public static final String REQUEST_TRIP_URL = "https://uber-server.herokuapp.com/api/trip/request";
    public static final String TRIP_STATUS_URL = "https://uber-server.herokuapp.com/api/trip/status/"; // append [trip_id] to this
    public static final String Register_URL = "https://uber-server.herokuapp.com/api/user/registration/";
    public static final String Login_URL = "https://uber-server.herokuapp.com/api/user/login/";
    public static final String Logout_URL = "https://uber-server.herokuapp.com/api/user/logout/";

    public static final Integer TRIP_STATUS_INTENT_FLAG = 999;
    public static final String TRIP_ID = "tripID";
    public static final String TRIP_STATUS = "tripStatus";

}
