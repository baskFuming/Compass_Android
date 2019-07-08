package com.xxx.compass.ui.login;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xxx.compass.ConfigClass;
import com.xxx.compass.R;
import com.xxx.compass.base.activity.BaseActivity;
import com.xxx.compass.model.http.Api;
import com.xxx.compass.model.http.ApiCallback;
import com.xxx.compass.model.http.bean.base.BaseBean;
import com.xxx.compass.model.http.bean.CountyBean;
import com.xxx.compass.model.sp.SharedConst;
import com.xxx.compass.model.sp.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @Page 选择国家页
 * @Author xxx
 */
public class SelectCountyActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String RESULT_CODE_KRY = "code";   //返回的Key
    public static final String RESULT_NAME_KRY = "name";   //返回的Key
    public static final String REQUEST_KRY = "return";   //返回的Key

    public static final int REGISTER_PAGE_CODE = 1;    //注册页面
    public static final int FORGET_PAGE_CODE = 2;   //忘记密码页面

    @BindView(R.id.select_county_recycler)
    RecyclerView mRecycler;
    @BindView(R.id.select_county_refresh)
    SwipeRefreshLayout mRefresh;

    private int code;   //页面状态
    private SelectCountyAdapter mAdapter;
    private List<CountyBean> mList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_county;
    }

    @Override
    protected void initData() {
        code = getIntent().getIntExtra(REQUEST_KRY, 0);

        mAdapter = new SelectCountyAdapter(mList);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mRefresh.setOnRefreshListener(this);

        loadData();
    }

    @OnClick({R.id.select_county_return, R.id.select_county_save})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_county_return:
                finish();
                break;
            case R.id.select_county_save:
                Intent intent = null;
                switch (code) {
                    case REGISTER_PAGE_CODE:
                        intent = new Intent(this, RegisterActivity.class);
                        break;
                    case FORGET_PAGE_CODE:
                        intent = new Intent(this, ForgetLoginPswActivity.class);
                        break;
                }
                if (intent != null) {
                    //记录选择
                    SharedPreferencesUtil instance = SharedPreferencesUtil.getInstance();
                    CountyBean countyBean = mList.get(mAdapter.getPosition());
                    instance.saveString(SharedConst.VALUE_COUNTY_CODE, countyBean.getAreaCode());
                    instance.saveString(SharedConst.VALUE_COUNTY_CITY, countyBean.getZhName());
                    intent.putExtra(SelectCountyActivity.RESULT_CODE_KRY, countyBean.getAreaCode());
                    intent.putExtra(SelectCountyActivity.RESULT_NAME_KRY, countyBean.getZhName());
                    setResult(ConfigClass.RESULT_CODE, intent);
                    finish();
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        mAdapter.setPosition(position);
        mAdapter.notifyDataSetChanged();
    }


    /**
     * @Model 获取城市编码
     */
    private void loadData() {
        Api.getInstance().getCounty()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<List<CountyBean>>(this) {

                    @Override
                    public void onSuccess(BaseBean<List<CountyBean>> bean) {
                        if (bean != null) {
                            List<CountyBean> list = bean.getData();
                            if (list != null && list.size() != 0) {
                                String saveCode = SharedPreferencesUtil.getInstance().getString(SharedConst.VALUE_COUNTY_CODE);
                                for (int i = 0; i < list.size(); i++) {
                                    if (list.get(i).getAreaCode().equals(saveCode)) {
                                        mAdapter.setPosition(i);
                                        break;
                                    }
                                }
                                mList.clear();
                                mList.addAll(list);
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onStart(Disposable d) {
                        super.onStart(d);
                        if (mRefresh != null) {
                            mRefresh.setRefreshing(true);
                        }
                    }

                    @Override
                    public void onEnd() {
                        super.onEnd();
                        if (mRefresh != null) {
                            mRefresh.setRefreshing(false);
                        }
                    }
                });
    }
}
