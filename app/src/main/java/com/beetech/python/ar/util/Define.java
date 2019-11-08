package com.beetech.python.ar.util;

public class Define {
    public static final String PREF_FILE_NAME = "beetech_pref";
    public static final String REALM_NAME = "beetech_db.realm";
    public static final String DAILY_API = "/daily";
    public static final String UNITS = "metric";
    public static final String LANG = "vi";
    public static final int CNT_TIME = 10;
    public static final int CNT_DAY = 7;
    public static final Float DEFAULT_TEMPERATE = 10000f;

    public static final long DEFAULT_TIMEOUT = 30L;
    public static final long CLICK_TIME_INTERVAL = 300L;
    public static final long TOUCH_TIME_INTERVAL = 200L;
    public static final int MAX_PAGE_SIZE = 20;
    //record
    public static final String MESSAGE_UNKNOW_RECORD = "<unknown>";

    // all event category id
    public static final int ALL_EVENT_CATEGORY_ID = 0;

    public static class Api {

        public static final String GET_LIST_TRAVELS_URL = "api/travels/";

        public static class BaseResponse {

        }

        public static class HttpCode {
            public static final Integer NETWORK_NOT_CONNECT = 1001;
            public static final Integer CANNOT_CONNECT_TO_SERVER = 1002;
            public static final Integer RESPONSE_CODE_ACCESS_TOKEN_EXPIRED = 403;
            public static final Integer NOT_FOUND = 404;
        }

    }

}
