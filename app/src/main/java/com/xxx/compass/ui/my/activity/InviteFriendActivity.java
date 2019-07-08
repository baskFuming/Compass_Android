package com.xxx.compass.ui.my.activity;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxx.compass.ConfigClass;
import com.xxx.compass.R;
import com.xxx.compass.base.activity.BaseTitleActivity;
import com.xxx.compass.model.sp.SharedConst;
import com.xxx.compass.model.sp.SharedPreferencesUtil;
import com.xxx.compass.model.utils.ImageUtil;
import com.xxx.compass.model.utils.KeyBoardUtil;
import com.xxx.compass.model.utils.ZXingUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Page 邀请好友页
 * @Author xxx
 */
public class InviteFriendActivity extends BaseTitleActivity {

    @BindView(R.id.invite_friend_image)
    ImageView mImage;
    @BindView(R.id.invite_friend_code)
    TextView mCode;

    private String content;
    private Bitmap bitmap;  //二维码

    @Override
    protected String initTitle() {
        return getString(R.string.invite_friend_title);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_invite_friend;
    }

    @Override
    protected void initData() {
        content = ConfigClass.INVITE_URL + SharedPreferencesUtil.getInstance().getString(SharedConst.VALUE_INVITE_CODE);
        bitmap = ZXingUtil.createQRCode(content, (int) getResources().getDimension(R.dimen.zxCode_size));
        mImage.setImageBitmap(bitmap);
        mCode.setText(content);
    }

    @OnClick({R.id.invite_friend_copy, R.id.invite_friend_save})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.invite_friend_copy:
                KeyBoardUtil.copy(this, content);
                break;
            case R.id.invite_friend_save:
                ImageUtil.saveImage(this, bitmap);
                break;
        }
    }
}
