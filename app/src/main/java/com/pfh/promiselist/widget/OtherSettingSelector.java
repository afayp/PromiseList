package com.pfh.promiselist.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.pfh.promiselist.R;
import com.pfh.promiselist.utils.DensityUtil;

/**
 * 新建任务里面的其他设置,
 * 选择之后 要有相应view变化来提醒用户
 */

public class OtherSettingSelector extends HorizontalScrollView {

    private String[] settings = {"优先级" , "提醒" , "标签" , "共享者"};
    private int[] icons = {R.drawable.ic_flag , R.drawable.ic_bell , R.drawable.ic_tag_grey , R.drawable.ic_add_people};
    private Context mContext;
    private LinearLayout.LayoutParams defaultParams;


    private int selectedImportance = 2;//默认正常

    public OtherSettingSelector(Context context) {
        this(context,null);
    }

    public OtherSettingSelector(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public OtherSettingSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        defaultParams = new LinearLayout.LayoutParams(DensityUtil.dp2px(mContext,100),DensityUtil.dp2px(mContext,100));
        initView();
    }

    private void initView() {
        removeAllViews();
        LinearLayout ll_root = new LinearLayout(mContext);
        ll_root.setOrientation(LinearLayout.HORIZONTAL);
        LayoutInflater inflater = LayoutInflater.from(mContext);

        for (int i = 0; i < settings.length; i++) {
            View item = inflater.inflate(R.layout.item_other_setting_selector, null);
            item.setId(i);
            ImageView iv_bg = (ImageView) item.findViewById(R.id.iv_bg);
            TextView tv_name = (TextView) item.findViewById(R.id.tv_name);
            tv_name.setText(settings[i]);
            iv_bg.setImageResource(icons[i]);
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,"click "+settings[v.getId()],Toast.LENGTH_SHORT).show();
                    switch (v.getId()) {
                        case 0:
                            showImportance();
                            break;
                        case 1:
                            showRemind();
                            break;
                        case 2:
                            showTag();
                            break;
                        case 3:
                            showCooperator();
                            break;
                    }
                }
            });
            ll_root.addView(item,defaultParams);
        }
        addView(ll_root);
    }


    private void showImportance() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_choose_importance, null);
        final MaterialDialog dialog = new MaterialDialog.Builder(mContext)
                .customView(view, false)
                .negativeText(R.string.cancel)
                .title(R.string.choose_importance)
                .show();

        LinearLayout ll_high = (LinearLayout) view.findViewById(R.id.ll_high);
        LinearLayout ll_nomal = (LinearLayout) view.findViewById(R.id.ll_nomal);
        LinearLayout ll_low = (LinearLayout) view.findViewById(R.id.ll_low);

        RadioButton rb_high = (RadioButton) view.findViewById(R.id.rb_high);
        RadioButton rb_nomal = (RadioButton) view.findViewById(R.id.rb_nomal);
        RadioButton rb_low = (RadioButton) view.findViewById(R.id.rb_low);
        switch (selectedImportance){
            case 1:
                rb_low.setChecked(true);
                break;
            case 2:
                rb_nomal.setChecked(true);
                break;
            case 3:
                rb_high.setChecked(true);
                break;
        }

        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ll_high:
                        selectedImportance = 3;
                        break;
                    case R.id.ll_nomal:
                        selectedImportance = 2;
                        break;
                    case R.id.ll_low:
                        selectedImportance = 1;
                        break;
                }
                dialog.dismiss();
            }
        };
        ll_high.setOnClickListener(listener);
        ll_nomal.setOnClickListener(listener);
        ll_low.setOnClickListener(listener);
    }
    private void showRemind() {

    }

    private void showTag() {

    }

    private void showCooperator() {

    }
}
