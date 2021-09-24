package com.yoon.rxjavatest._Library;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class _Popup {

    private String TAG = this.getClass().toString();

    private static _Popup mInstance = new _Popup();

    private _Popup() { }

    public static _Popup GetInstance() {
        return mInstance;
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    private ProgressDialog mIndicatorDialog = null;
    public IndicatorPopupListener waitIndicatorPopupListener = null;
    public interface IndicatorPopupListener {
        void didDismissIndicatorPopup();
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public ProgressDialog mProgressDialog = null;
    public WaitProgressPopupListener waitProgressPopupListener = null;
    public interface WaitProgressPopupListener {
        void didDismissWaitProgressPopup(String title, String message, Integer tagId);
    }

    String mWaitProgressPopupTitle = "";
    String mWaitProgressPopupMsg = "";
    public void ShowWaitProgressPopup(final Context context, String title, String message) {
        mWaitProgressPopupTitle = title;
        mWaitProgressPopupMsg = message;
        mProgressDialog = ProgressDialog.show(context, title, message, true);
    }

    public void DismissWaitProgressPopup(final Integer tagId) {
        if(mProgressDialog != null) {
            mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    String mmTitle = mWaitProgressPopupTitle;
                    String mmMessage = mWaitProgressPopupMsg;
                    if(waitProgressPopupListener != null) {
                        waitProgressPopupListener.didDismissWaitProgressPopup(mmTitle, mmMessage, tagId);

                    }
                    mProgressDialog = null;
                }
            });
            mProgressDialog.dismiss();
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public ConfirmPopupListener confirmPopupListener = null;
    public interface ConfirmPopupListener { void didSelectConfirmPopup(String title, String message, String confirmMessage); }

    public AlertDialog.Builder mConfirmAlt_Builder = null;
    public void ShowConfirmPopup(Context context, String title, String mainMessage, String confirmMessage) {
        final String mmTitle = title;
        final String mmMsg = mainMessage;
        final String mmConMsg = confirmMessage;
        
        mConfirmAlt_Builder = new AlertDialog.Builder(context);
        if(title != null) mConfirmAlt_Builder.setTitle(mmTitle);
        if(mainMessage != null) mConfirmAlt_Builder.setMessage(mmMsg);
        mConfirmAlt_Builder.setCancelable(false);
        mConfirmAlt_Builder.setPositiveButton(mmConMsg, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(confirmPopupListener != null) confirmPopupListener.didSelectConfirmPopup(mmTitle, mmMsg, mmConMsg);
                        binaryPopupListener = null;
                        mBinaryAlt_Builder = null;
                        dialog.cancel();
                    }
                });
        AlertDialog mmAlert = mConfirmAlt_Builder.create();
        mmAlert.setCanceledOnTouchOutside(true);
        mmAlert.show();
    }

    public void ShowConfirmPopup(Context context, String title, String mainMessage, String confirmMessage, final ConfirmPopupListener listener) {
        final String mmTitle = title;
        final String mmMsg = mainMessage;
        final String mmConMsg = confirmMessage;

        mConfirmAlt_Builder = new AlertDialog.Builder(context);
        if(title != null) mConfirmAlt_Builder.setTitle(mmTitle);
        if(mainMessage != null) mConfirmAlt_Builder.setMessage(mmMsg);
        mConfirmAlt_Builder.setCancelable(false);
        mConfirmAlt_Builder.setPositiveButton(mmConMsg, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(listener != null) listener.didSelectConfirmPopup(mmTitle, mmMsg, mmConMsg);
                mBinaryAlt_Builder = null;
                dialog.cancel();
            }
        });
        AlertDialog mmAlert = mConfirmAlt_Builder.create();
        mmAlert.show();
    }
    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public BinaryPopupListener binaryPopupListener = null;
    public interface BinaryPopupListener
    { void didSelectBinaryPopup(String mainMessage, String selectMessage); }

    public AlertDialog.Builder mBinaryAlt_Builder = null;
    public void ShowBinaryPopup(Context context, String mainMessage, String positiveMessage, String negitiveMessage) {
        final String mmMsg = mainMessage;
        final String mmPosMsg = positiveMessage;
        final String mmNegMsg = negitiveMessage;
        mBinaryAlt_Builder = new AlertDialog.Builder(context);
        mBinaryAlt_Builder.setMessage(mmMsg).setCancelable(
                false).setPositiveButton(mmPosMsg,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(binaryPopupListener != null) binaryPopupListener.didSelectBinaryPopup(mmMsg, mmPosMsg);
                        binaryPopupListener = null;
                        mBinaryAlt_Builder = null;
                        dialog.cancel();
                    }
                }).setNegativeButton(mmNegMsg, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(binaryPopupListener != null) binaryPopupListener.didSelectBinaryPopup(mmMsg, mmNegMsg);
                        binaryPopupListener = null;
                        mBinaryAlt_Builder = null;
                        dialog.cancel();
                    }
                });
        AlertDialog mmAlert = mBinaryAlt_Builder.create();
        mmAlert.show();
    }

    public void ShowBinaryPopup(Context context, String mainMessage, String positiveMessage, String negitiveMessage, final BinaryPopupListener listener) {
        final String mmMsg = mainMessage;
        final String mmPosMsg = positiveMessage;
        final String mmNegMsg = negitiveMessage;
        mBinaryAlt_Builder = new AlertDialog.Builder(context);
        mBinaryAlt_Builder.setMessage(mmMsg).setCancelable(
                false).setPositiveButton(mmPosMsg,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(listener != null) listener.didSelectBinaryPopup(mmMsg, mmPosMsg);
                        mBinaryAlt_Builder = null;
                        dialog.cancel();
                    }
                }).setNegativeButton(mmNegMsg, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(listener != null) listener.didSelectBinaryPopup(mmMsg, mmNegMsg);
                mBinaryAlt_Builder = null;
                dialog.cancel();
            }
        });
        AlertDialog mmAlert = mBinaryAlt_Builder.create();
        mmAlert.setCanceledOnTouchOutside(true);
        mmAlert.show();
    }

    public void ShowBinaryPopupVerification(Context context, String mainMessage, String positiveMessage, String negitiveMessage, final BinaryPopupListener listener) {
        final String mmMsg = mainMessage;
        final String mmPosMsg = positiveMessage;
        final String mmNegMsg = negitiveMessage;
        mBinaryAlt_Builder = new AlertDialog.Builder(context);
        mBinaryAlt_Builder.setMessage(mmMsg).setCancelable(
                false).setPositiveButton(mmPosMsg,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(listener != null) listener.didSelectBinaryPopup(mmMsg, mmPosMsg);
                        mBinaryAlt_Builder = null;
                        dialog.cancel();
                    }
                }).setNegativeButton(mmNegMsg, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(listener != null) listener.didSelectBinaryPopup(mmMsg, mmNegMsg);
                mBinaryAlt_Builder = null;
                dialog.cancel();
            }
        });
        AlertDialog mmAlert = mBinaryAlt_Builder.create();
        mmAlert.show();
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public TriplePopupListener tripleSelectPopupListener = null;
    public interface TriplePopupListener
    { void didSelectTripleSelectPopup(String mainMessage, String selectMessage); }

    private AlertDialog.Builder mTripleAlt_Builder = null;
    public void ShowTriplePopup(Context context, final String mainMessage, String positiveMessage, String neutralMessage, String negativeMessage) {
        final String mmMaiMsg = mainMessage;
        final String mmPosMsg = positiveMessage;
        final String mmNeuMsg = neutralMessage;
        final String mmNegMsg = negativeMessage;

        mTripleAlt_Builder = new AlertDialog.Builder(context);
        mTripleAlt_Builder.setMessage(mmMaiMsg);
        mTripleAlt_Builder.setCancelable(false);
        mTripleAlt_Builder.setPositiveButton(mmPosMsg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(tripleSelectPopupListener != null) tripleSelectPopupListener.didSelectTripleSelectPopup(mmMaiMsg, mmPosMsg);
            }
        });

        mTripleAlt_Builder.setNegativeButton(mmNeuMsg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(tripleSelectPopupListener != null) tripleSelectPopupListener.didSelectTripleSelectPopup(mmMaiMsg, mmNeuMsg);
            }
        });

        mTripleAlt_Builder.setNeutralButton(mmNegMsg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(tripleSelectPopupListener != null) tripleSelectPopupListener.didSelectTripleSelectPopup(mmMaiMsg, mmNegMsg);
            }
        });

        mTripleAlt_Builder.show();
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public SinlgeTextInputPopupListener singleTextInputPopupListener = null;
    public interface SinlgeTextInputPopupListener
    { void didSingleTextInputPopup(String mainMessage, String inputText, String selectMessage); }

    private AlertDialog.Builder mSingleTextInputAlt_Builder = null;
    public void ShowSingleTextInputPopup(Context context, int frameResourceId, int editViewResId, String titleMessage, String mainMessage, String hintMessage, String positiveMessage, String negativeMessage) {
        final String mmTiTMsg = titleMessage;
        final String mmMaiMsg = mainMessage;
        final String mmPosMsg = positiveMessage;
        final String mmNegMsg = negativeMessage;

        LayoutInflater mmLi = LayoutInflater.from(context);
        View mmFrameView = mmLi.inflate(frameResourceId, null);

        final EditText mmEditText = ((EditText)mmFrameView.findViewById(editViewResId));

        mmEditText.setText("");
        mmEditText.setHint(hintMessage);

        mSingleTextInputAlt_Builder = new AlertDialog.Builder(context);

        mSingleTextInputAlt_Builder.setView(mmFrameView);
        mSingleTextInputAlt_Builder.setTitle(mmTiTMsg);
        mSingleTextInputAlt_Builder.setMessage(mmMaiMsg);
        mSingleTextInputAlt_Builder.setCancelable(false);

        mSingleTextInputAlt_Builder.setPositiveButton(mmPosMsg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String mmInputStr = mmEditText.getText().toString();
                if(singleTextInputPopupListener != null) singleTextInputPopupListener.didSingleTextInputPopup(mmMaiMsg, mmInputStr, mmPosMsg);
            }
        });

        mSingleTextInputAlt_Builder.setNegativeButton(mmNegMsg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(singleTextInputPopupListener != null) singleTextInputPopupListener.didSingleTextInputPopup(mmMaiMsg, "", mmNegMsg);
            }
        });

        mSingleTextInputAlt_Builder.show();
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public DualTextInputPopupListener dualTextInputPopupListener = null;
    public interface DualTextInputPopupListener
    { void didDualTextInputPopup(String mainMessage, String firstInputText, String secondInputText, String selectMessage); }

    private AlertDialog.Builder mDualTextInputAlt_Builder = null;
    public void ShowDualTextInputPopup(Context context, int frameResourceId, int firstEditViewResId, int secondEditViewResId, String titleMessage, String mainMessage, String firstHintMessage, String secondHintMessage, String positiveMessage, String negativeMessage) {
        final String mmTiTMsg = titleMessage;
        final String mmMaiMsg = mainMessage;
        final String mmPosMsg = positiveMessage;
        final String mmNegMsg = negativeMessage;

        LayoutInflater mmLi = LayoutInflater.from(context);
        View mmFrameView = mmLi.inflate(frameResourceId, null);

        final EditText mmFirstEditText = ((EditText)mmFrameView.findViewById(firstEditViewResId));
        final EditText mmSecondEditText = ((EditText)mmFrameView.findViewById(secondEditViewResId));

        mmFirstEditText.setText("");
        mmFirstEditText.setHint(firstHintMessage);

        mmSecondEditText.setText("");
        mmSecondEditText.setHint(secondHintMessage);

        mDualTextInputAlt_Builder = new AlertDialog.Builder(context);

        mDualTextInputAlt_Builder.setView(mmFrameView);
        mDualTextInputAlt_Builder.setTitle(mmTiTMsg);
        mDualTextInputAlt_Builder.setMessage(mmMaiMsg);
        mDualTextInputAlt_Builder.setCancelable(false);

        mDualTextInputAlt_Builder.setPositiveButton(mmPosMsg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String mmFirstInputStr = mmFirstEditText.getText().toString();
                String mmSecondInputStr = mmSecondEditText.getText().toString();
                if(dualTextInputPopupListener != null) dualTextInputPopupListener.didDualTextInputPopup(mmMaiMsg, mmFirstInputStr, mmSecondInputStr, mmPosMsg);
            }
        });

        mDualTextInputAlt_Builder.setNegativeButton(mmNegMsg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(dualTextInputPopupListener != null) dualTextInputPopupListener.didDualTextInputPopup(mmMaiMsg, "", "", mmNegMsg);
            }
        });

        mDualTextInputAlt_Builder.show();
    }

    public ShowMultipleItemPopupListener multipleItemPopupListener = null;
    public interface ShowMultipleItemPopupListener
    { void didMultipleItemPopup(String mainMessage, String selectItem, Integer selectIndex); }

    private AlertDialog.Builder mMultipleItemAlt_Builder = null;
    public void ShowMultipleItemPopup(Context context, String titleMessage, String mainMessage, ArrayList<String> items, String negativeMessage) {
        final String mmTiTMsg = titleMessage;
        final String mmMaiMsg = mainMessage;

        final CharSequence mmItems[] = items.toArray(new CharSequence[items.size()]);

        mMultipleItemAlt_Builder = new AlertDialog.Builder(context);
        mMultipleItemAlt_Builder.setTitle(mmTiTMsg);
        //When use list. It can't use message alternatly.
        mMultipleItemAlt_Builder.setItems(mmItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String mmItem = mmItems[which].toString();
                Integer mmIndex = which;
                if(multipleItemPopupListener != null) multipleItemPopupListener.didMultipleItemPopup(mmMaiMsg, mmItem, mmIndex);
            }
        });
        mMultipleItemAlt_Builder.setNegativeButton(negativeMessage, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(multipleItemPopupListener != null) multipleItemPopupListener.didMultipleItemPopup(mmMaiMsg, "", -1);
            }
        });

        mMultipleItemAlt_Builder.show();
    }
}
