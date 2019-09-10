package com.xxx.compass.ui.my.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.xxx.compass.R;
import com.xxx.compass.base.activity.BaseTitleActivity;
import com.xxx.compass.base.dialog.LoadingDialog;
import com.xxx.compass.model.http.Api;
import com.xxx.compass.model.http.ApiCallback;
import com.xxx.compass.model.http.bean.base.BaseBean;
import com.xxx.compass.model.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @Page 问题反馈页
 * @Author xxx
 */
public class FeedBackActivity extends BaseTitleActivity {

    @BindView(R.id.feed_back_number)
    TextView mNumber;
    @BindView(R.id.feed_back_edit)
    EditText mContent;
    @BindView(R.id.feed_back_phone)
    EditText mPhone;

    private LoadingDialog mLoadingDialog;

    @Override
    protected String initTitle() {
        return getString(R.string.feedback_title);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feed_back;
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.feed_back_btn})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.feed_back_btn:
                submitFeedback();
                break;
        }
    }

    @OnTextChanged({R.id.feed_back_edit})
    public void OnTextChanged(CharSequence charSequence) {
        int length = charSequence.length();
        mNumber.setText(length + "/300");
    }

    /**
     * @Model 提交意见反馈
     */
    private void submitFeedback() {
        String content = mContent.getText().toString();
        String phone = mPhone.getText().toString();

        if (content.isEmpty()) {
            ToastUtil.showToast(R.string.feed_back_error_1);
            return;
        }
        if (phone.isEmpty()) {
            ToastUtil.showToast(R.string.feed_back_error_2);
            return;
        }

        Api.getInstance().submitFeedback(content, phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<Object>(this) {

                    @Override
                    public void onSuccess(BaseBean<Object> bean) {
                        if (bean != null) {
                            ToastUtil.showToast(bean.getMessage());
                            mContent.setText("");
                            mPhone.setText("");
                        }
                    }

                    @Override
                    public void onError(int errorCode, String errorMessage) {
                        ToastUtil.showToast(errorMessage);
                    }

                    @Override
                    public void onStart(Disposable d) {
                        super.onStart(d);
                        if (mLoadingDialog == null) {
                            mLoadingDialog = LoadingDialog.getInstance(FeedBackActivity.this);
                            mLoadingDialog.show();
                        }
                    }

                    @Override
                    public void onEnd() {
                        super.onEnd();
                        if (mLoadingDialog != null) {
                            mLoadingDialog.dismiss();
                            mLoadingDialog = null;
                        }
                    }
                });
    }
}
