package com.yoon.rxjavatest;

public class Define {

    public static final String FRAGMENT_BUS = "FragmentBus";
    public static final String FRAGMENT_MAP = "FragmentMap";
    public static final String FRAGMENT_MAP_GET_BUS_DATA = "FragmentMapGetBusData";
    public static final String FRAGMENT_TRANSPORTATION = "FragmentTransportation";
    public static final String FRAGMENT_TRANSPORTATION_DETAIL = "FragmentTransportationDetail";
    public static final String FRAGMENT_SEARCH= "FragmentSearch";

    /** 이벤트 */
    public static final String EVENT_BACK = "back";
    public static final String EVENT_DONE = "done";
    public static final String EVENT_DELETE = "delete";
    public static final String EVENT_CLICK_ITEM = "click_item";
    public static final String EVENT_NOTI_ON = "noti_on";
    public static final String EVENT_NOTI_OFF = "noti_off";
    public static final String EVENT_CHROME = "chrome";
    public static final String EVENT_CERTIFY = "certify";
    public static final String LOAD_DATA = "load_data";
    public static final String RECEIVED_DATA = "received_data";
    public static final String RELOAD_LIST = "reload_list";
    public static final String TERMS_OF_USE = "terms_of_use";
    public static final String ABOUT = "about";
    public static final String SHOW_ALL= "showAll";
    public static final String RETRY = "retry";
    public static final String RESEND = "resend";
    public static final String CONFIRM = "confirm";
    public static final String EVENT_SET_LAYOUT = "event_set_layout";

    // 특정 정거장 전이면 버스 알림
    public final static int NOTI_BUS_ARRIVAL_CNT = 5;

    public static final String CC_TRANS_CHANNEL = "ccTransportationChannel";
    public static final String CC_TRANS_CHANNEL_NAME = "춘천 버스 알림";
    public static final String CC_TRANS_CHANNEL_DESC= "춘천 버스 알림 채널";

    public static final String LOADING = "loading";
    public static final String LOADING_COMPLETE = "loading_complete";
    public static final String LOADING_REMOVE = "loading_remove";
    public static final String EXIT_ROOT = "exit";

    /** 팝업 메세지 */
    public static final String NETWORK_INFORM = "인터넷 연결이 되어있지 않습니다.\n인터넷 연결 후 앱을 다시 실행하여 주십시오.\nWiFi 설정화면으로 이동합니다.";
    public static final String UPDATE_TITLE = "업데이트 요청";
    public static final String UPDATE_INFORM = "최신 버전으로 업데이트를 해주시기 바랍니다.\n업데이트 페이지로 이동합니다.";
    public static final String RESTART_INFORM = "앱을 재실행합니다.";
    public static final String PERMISSION_DENIED_INFORM = "권한이 거부되었습니다.\n앱 이용을 위하여 해당 권한을 허용하여 주시기 바랍니다.\n앱을 종료합니다.";
    public static final String PERMISSION_DENIED_WRITE_INFORM = "권한이 거부되었습니다.\n서비스 이용을 위하여 해당 권한을 허용하여 주시기 바랍니다.";
    public static final String REQUEST_INFORM = "서버에서 데이터를 가져오는데 실패 하였습니다.\n다시 요청 하시겠습니까?";
    public static final String FILE_INFORM = "서비스를 실행하기 위한 데이터를 가져오는데 문제가 발생하였습니다.\n네트워크 상태를 확인하시고 다시 실행하여 주시기 바랍니다.";
    public static final String SEARCH_INFORM = "검색어를 입력하여 주십시오.";
    public static final String BUS_SEARCH_INFORM = "버스정류장 이름을 입력하여 주십시오.";
    public static final String SSL_INFORM = "해당 사이트로 진입 하시겠습니까?";
    public static final String SSL_NAGATIVE = "확인 버튼을 눌러야 서비스 이용이 가능합니다.";

    public static final String NOTIFY_TITLE = "알림";
    public static final String CONFIRM_MSG = "확인";
    public static final String CANCEL_MSG = "취소";
    public static final String REQUEST = "재요청";
    public static final String FAIL_TITLE = "데이터 요청 실패";
    public static final String APP_EXIT = "앱 종료";
    public static final String VERIFICATION_MSG = "인증하기";
    public static final String REFUSAL = "거부";

    /** 로그캣 */
    public static final String TAG_HTTP = "HTTP_CONNECTION";
}
