package com.taotong.tuanfan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ro.xdroid.net.ok.OkKit;
import com.ro.xdroid.net.ok.taotong.TTOkListener;
import com.ro.xdroid.net.ok.taotong.TTResponse;
import com.ro.xdroid.view.dialog.ToastKit;
import com.taotong.tuanfan.Util.IPConstants;
import com.taotong.tuanfan.model.HomePageListModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TTOkListener {

    private TextView tv_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    private void initView() {
        tv_start = (TextView) findViewById(R.id.tv_start); //开始
        tv_start.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_start: //
                getData();
                break;
        }
    }

    private void getData() {

        OkKit.request(1, IPConstants.HOME_PAGE, HomePageListModel.class)
                .putParams("page", 1 + "")
                .putParams("rows", 20 + "")
                .execute(this);

    }


    @Override
    public void onTTOkResponse(TTResponse ttResponse) throws Throwable {
        if (!ttResponse.isSuccess) {
            return;
        }
        int key = (int) ttResponse.key;
        switch (key) {
            case 1:
                HomePageListModel mechanismInfoModel = (HomePageListModel) ttResponse.data;
                if (mechanismInfoModel.getStatus()==1) {
                    List<HomePageListModel.DataBean> dataBeen = mechanismInfoModel.getData();
                    String id = dataBeen.get(1).getId();
                    ToastKit.show("id:"+id);
                }
                break;
        }

    }


}
