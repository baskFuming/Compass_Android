package com.xxx.compass.ui.my.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.xxx.compass.ConfigClass;
import com.xxx.compass.R;
import com.xxx.compass.base.activity.BaseTitleActivity;
import com.xxx.compass.model.utils.ImageUtil;
import com.xxx.compass.model.utils.ZXingUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class CallMeActivity extends BaseTitleActivity {

    @BindView(R.id.call_me_facebook_code)
    ImageView mFacebookCode;
    @BindView(R.id.call_me_we_chat_code)
    ImageView mWeChatCode;

    private Bitmap mFacebookBitmap;
    private Bitmap mWeChatBitmap;

    @Override
    protected String initTitle() {
        return getString(R.string.call_my_title);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_call_me;
    }

    @Override
    protected void initData() {
//        mFacebookBitmap = ZXingUtil.createQRCode(ConfigClass.CALL_ME_FACEBOOK, (int) getResources().getDimension(R.dimen.zxCode_call_me_size));
//        mWeChatBitmap = ZXingUtil.createQRCode(ConfigClass.CALL_ME_WE_CHAT, (int) getResources().getDimension(R.dimen.zxCode_call_me_size));
        mFacebookBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.facebook_code);
        mWeChatBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.we_chat_code);
        mFacebookCode.setImageBitmap(mFacebookBitmap);
        mWeChatCode.setImageBitmap(mWeChatBitmap);
    }

    @OnClick({R.id.call_me_facebook_save, R.id.call_me_we_chat_save})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.call_me_facebook_save:
                ImageUtil.saveImage(this, mFacebookBitmap);
                break;
            case R.id.call_me_we_chat_save:
                ImageUtil.saveImage(this, mWeChatBitmap);
                break;
        }
    }
}
