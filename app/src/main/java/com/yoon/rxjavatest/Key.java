package com.yoon.rxjavatest;

public class Key {

    //    public static final String REQ_URL = "http://htmain.kro.kr/cc_transportation/AppClient";
    public static final String REQ_URL = "https://htmain.kro.kr/cc_transportation/AppClient";

    // 정류소별특정노선버스도착예정정보목록조회
    // http://openapi.tago.go.kr/openapi/service/ArvlInfoInqireService/getSttnAcctoSpcifyRouteBusArvlPrearngeInfoList?serviceKey=RD2oHWzZkZ7c3QBbBPT3jwG6IubKC1%2B9epIN0220PKywcyo4OFHlkuXDK8mF0q3Oi%2BXIf9J%2FZPQsaDN3ImJWJA%3D%3D&cityCode=32010&nodeId=CCB250001498&routeId=CCB250020001&_type=json
    public static final String SPECIFIC_BUS_ARVL_INFO_INQUIRE_SERVICE = "http://openapi.tago.go.kr/openapi/service/ArvlInfoInqireService/getSttnAcctoSpcifyRouteBusArvlPrearngeInfoList?";

    // 노선번호목록조회 : 버스 번호를 입력하면 모든 버스 번호를 검색하여 버스 정보 데이터를 가져오는 url
    // http://openapi.tago.go.kr/openapi/service/BusRouteInfoInqireService/getRouteNoList?serviceKey=RD2oHWzZkZ7c3QBbBPT3jwG6IubKC1%2B9epIN0220PKywcyo4OFHlkuXDK8mF0q3Oi%2BXIf9J%2FZPQsaDN3ImJWJA%3D%3D&cityCode=32010&routeNo=1&_type=json
    public static final String BUS_NUM_SEARCHING_URL = "http://openapi.tago.go.kr/openapi/service/BusRouteInfoInqireService/getRouteNoList?";

    // 노선별버스위치목록조회 : 노선별로 버스의 GPS위치정보의 목록을 조회한다.
    // http://openapi.tago.go.kr/openapi/service/BusLcInfoInqireService/getRouteAcctoBusLcList?serviceKey=rwASh3vbJxCw18PC7tn3LysYGa2rieM5ZvFimL5WpwG61xIIKNVyFz3bmoYChk/gL7hvuVikKo608PoukisWeA==&cityCode=32010&routeId=CCB250020001&_type=json
    public static final String GET_ROUTE_ARVL_BUS_LIST = "http://openapi.tago.go.kr/openapi/service/BusLcInfoInqireService/getRouteAcctoBusLcList?";

    public static final String SERVICE_KEY = "RD2oHWzZkZ7c3QBbBPT3jwG6IubKC1%2B9epIN0220PKywcyo4OFHlkuXDK8mF0q3Oi%2BXIf9J%2FZPQsaDN3ImJWJA%3D%3D";
    public static final String BASE_URI = "http://openapi.tago.go.kr/openapi/service/";
    public static final String CITY_CODE = "32010";
    public static final String TYPE_JSON = "json";
    public static final String ROWS = "300";

    public static final String EVENT_APP_EXIT = "exit";
    public static final String NOTIFICATION_URL = "notification_url";
    public static final String TOKEN_ID = "token_id";
    public static final String SSL_CHECK = "ssl_check";
    public static final String CURRENT_HOUR = "current_hour";
    public static final String CURRENT_MINUTE = "current_minute";
    public static final String PUSH_STATE = "push_state";
    public static final String CURRENT_URL = "current_url";
    public static final String RECEIVED_MSG = "received_msg";
    public static final String SELECTED_NUM = "selected_num";
    public static final String SEARCH_RESULT = "search_result";
    public static final String LOADING_TIMER = "loading_timer";

    // for notification
    public static final String NOTIFICATION_INFO = "notification_info";
    public static final String NOTI_COMMENT = "comment";
    public static final String NOTI_CONTENT = "content";
    public static final String NOTI_ID = "noti_id";
    public static final String NOTI_DATE = "noti_date";
    public static final String NOTI_ARRIVAL = "arrival";
    public static final String NOTI_NODE_ID = "node_id";
    public static final String NOTI_ROUTE_NO = "route_no";
    public static final String NOTI_ARRIVAL_COUNT = "arrival_cnt";
    public static final String NOTI_NOTICE = "notice";
    public static final String NOTI_EVENT = "event";
    public static final String NOTI_QNA = "qna";

    // for bus
    public static final String BUS_LATITUDE = "gpslati";
    public static final String BUS_LONGITUDE = "gpslong";
    public static final String BUS_NODE_ID = "nodeid";
    public static final String BUS_NODE_NAME = "nodenm";
    public static final String BUS_NODE_NO = "nodeno";
    public static final String BUS_ROUTE_ID = "routeid";
    public static final String BUS_ROUTE_NO = "routeno";
    public static final String BUS_ROUTE_TP = "routetp";
    public static final String BUS_START_STOP = "startnodenm";
    public static final String BUS_END_STOP = "endnodenm";
    public static final String BUS_FINAL_START_STOP = "startnodenm_final";
    public static final String BUS_FINAL_END_STOP = "endnodenm_final";
    public static final String BUS_NEXT_STOP = "nextnodenm";
    public static final String BUS_ARRIVAL_CNT = "arrprevstationcnt";
    public static final String BUS_NODE_ORD = "nodeord";
    public static final String NODE_LIST = "node_list";
    public static final String ROUTE_LIST = "route_list";
}