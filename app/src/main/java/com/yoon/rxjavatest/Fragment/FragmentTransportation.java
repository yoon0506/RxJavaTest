package com.yoon.rxjavatest.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.yoon.rxjavatest.R;
import com.yoon.rxjavatest.databinding.FragmentTransportationBinding;

import java.util.HashMap;

public class FragmentTransportation extends Fragment {
    private FragmentTransportation This = this;
    private FragmentTransportationBinding mBinding;
    // request
//    private RequestTrafficList mRequestTrafficList;
//    // UI
//    private WebView mWebView;
//    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Listener mListener = null;

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_transportation, container, false);
        View mmView = mBinding.getRoot();
        return mmView;
    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        mWebView = getActivity().findViewById(R.id.webView_transportation);
//        RequestTrafficList();
//    }
//
//    private void RequestTrafficList() {
//        if (mListener != null) mListener.didRespond(This, Define.LOADING, null);
//
//        String mmUserId = AppData.GetInstance().GetUserID(getContext());
//
//        mRequestTrafficList = new RequestTrafficList();
//        mRequestTrafficList.request(mmUserId, new RequestTrafficList.Listener() {
//            @Override
//            public void didRespond(RequestRoot request, HashMap<String, String> properties, String error) {
//                if (error.equals("0")) {
//                    String mmTempBoardID = properties.get(Key.BOARD_ID);
//                    String mmTempBoardURL = properties.get(Key.BOARD_URL);
//                    AppData.GetInstance().SetBoardID(getContext(), mmTempBoardID);
//                    AppData.GetInstance().SetCurrentURL(getContext(), mmTempBoardURL);
//                    requestWebURL();
//                }
//            }
//        });
//    }
//
//    public void requestWebURL() {
//        String mmWebURL = AppData.GetInstance().GetCurrentURL(getContext());
//        if (mmWebURL.equals("")) {
//            if (mListener != null) {
//                mListener.didRespond(This, Define.LOADING_COMPLETE, null);
//            }
//        } else {
//            showWebView(mmWebURL);
//        }
//    }
//
//
//    @SuppressLint("SetJavaScriptEnabled")
//    public void showWebView(String webURL) {
//        mWebView.enableSlowWholeDocumentDraw();
//        mWebView.clearHistory();
//        mWebView.clearCache(true);
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.setWebChromeClient(new WebChromeClient());
//        mWebView.setWebViewClient(new WebViewClientClass());
//        mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        mWebView.loadUrl(webURL);
//    }
//
//    private class WebViewClientClass extends WebViewClient {//페이지 이동
//
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
//            // 세부 페이지 로딩
//            if (url.contains("toapp")) {
//                didOccurContentDetail(url);
//            } else {
//                String mmTempURL = url;
//                AppData.GetInstance().SetCurrentURL(getContext(), mmTempURL);
//
//            }
//            return true;
//        }
//
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest request) {
//            // 세부 페이지 로딩
//            if (request.getUrl().toString().contains("toapp")) {
//                didOccurContentDetail(request.getUrl().toString());
//            } else {
//                String mmTempURL = request.getUrl().toString();
//                AppData.GetInstance().SetCurrentURL(getContext(), mmTempURL);
//
//            }
//            return true;
//        }
//
//        @SuppressLint("RestrictedApi")
//        @Override
//        public void onPageFinished(WebView view, String url) {
//            super.onPageFinished(view, url);
//            if (mListener != null) {
//                mIsSuccLoading = true;
//                mListener.didRespond(This, Define.LOADING_COMPLETE, null);
//            }
//        }
//
//        String sslMessage = null;
//
//        @Override
//        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
//            if (AppData.GetInstance().GetSSLCheck(getContext())) {
//                handler.proceed();
//            } else {
//                if (error != null) {
//                    switch (error.getPrimaryError()) {
//                        case SslError.SSL_EXPIRED:
//                            sslMessage = "이 사이트의 보안 인증서가 만료되었습니다.\n";
//                            break;
//                        case SslError.SSL_IDMISMATCH:
//                            sslMessage = "이 사이트의 보안 인증서 ID가 일치하지 않습니다.\n";
//                            break;
//                        case SslError.SSL_NOTYETVALID:
//                            sslMessage = "이 사이트의 보안 인증서가 아직 유효하지 않습니다.\n";
//                            break;
//                        case SslError.SSL_UNTRUSTED:
//                            sslMessage = "이 사이트의 보안 인증서는 신뢰할 수 없습니다.\n";
//                            break;
//                        default:
//                            sslMessage = "보안 인증서에 오류가 있습니다.\n";
//                            break;
//                    }
//                }
//                _Popup.GetInstance().ShowBinaryPopup(getContext(), sslMessage + Define.SSL_INFORM, "확인", "취소", new _Popup.BinaryPopupListener() {
//                    @Override
//                    public void didSelectBinaryPopup(String mainMessage, String selectMessage) {
//                        if (selectMessage.equals("확인")) {
//                            AppData.GetInstance().SetSSLCheck(getContext(), true);
//                            handler.proceed();
//                        } else {
//                            _Popup.GetInstance().ShowConfirmPopup(getContext(), Define.NOTIFY_TITLE, Define.SSL_NAGATIVE, Define.CONFIRM_MSG, new _Popup.ConfirmPopupListener() {
//                                @Override
//                                public void didSelectConfirmPopup(String title, String message, String confirmMessage) {
//                                    if (confirmMessage.equals("확인")) {
//                                        return;
//                                    }
//                                }
//                            });
//                            handler.cancel();
//                        }
//                    }
//                });
//            }
//        }
//    }
//
//    private void didOccurContentDetail(String url) {
//        String[] mmSplitStr = url.split("::");
//        if (mmSplitStr[1].contains("sub_board_info")) {
//            String[] mmSplitStr1 = mmSplitStr[1].split("&");
//            String[] mmGetSecondStr = mmSplitStr1[1].split("=");
//            String mmBoardID = mmGetSecondStr[1];
//
//            AppData.GetInstance().SetBoardID(getContext(), mmBoardID);
//
//            if (mListener != null) {
//                mListener.didRespond(This, Define.FRAGMENT_TRANSPORTATION_DETAIL, null);
//            }
//        } else if (mmSplitStr[1].equals("click_advrts")) {
//            Bundle bundle = new Bundle();
//            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, AppData.GetInstance().GetUserID(getContext()));
//            //bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, AppData.GetInstance().GetUserName(This));
//            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "click_ad_transportation");
//            mFireBaseAnalytics.logEvent("click_ad_transportation", bundle);
//
//            String mmAdID = mmSplitStr[2];
//            String mmAdLinkURL = mmSplitStr[3];
//
//            AppData.GetInstance().SetAdID(getContext(), mmAdID);
//            AppData.GetInstance().SetAdLinkURL(getContext(), mmAdLinkURL);
//
//            if (mListener != null) mListener.didRespond(This, Define.CLICK_AD, null);
//            requestAdClick();
//        } else if (mmSplitStr[1].contains("search_keyword")) {
//            String mmSplitStr1 = mmSplitStr[2];
//            String mmSplitStr1_1 = null;
//            try {
//                mmSplitStr1_1 = URLDecoder.decode(mmSplitStr1, "UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            String mmKeyword = mmSplitStr1_1.replaceFirst("#", "");
//            AppData.GetInstance().SetSearchResult(getContext(), mmKeyword);
//
//            if (mListener != null) {
//                mListener.didRespond(This, Define.FRAGMENT_SEARCH, null);
//            }
//        }
//    }
//
//    private void requestAdClick() {
//        final String mmUserID = AppData.GetInstance().GetUserID(getContext());
//        String mmAdId = AppData.GetInstance().GetAdID(getContext());
//        mRequestAdClick = new RequestAdClick();
//        mRequestAdClick.request(mmUserID, mmAdId, new RequestAdClick.Listener() {
//            @Override
//            public void didRespond(RequestRoot request, HashMap<String, String> properties, String error) {
//                //request success
//            }
//        });
//    }

    public interface Listener {
        public void didRespond(Fragment fragment, String event, HashMap<String, String> data);

    }

}
