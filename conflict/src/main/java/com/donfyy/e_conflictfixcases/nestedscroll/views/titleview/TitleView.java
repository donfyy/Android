package com.donfyy.e_conflictfixcases.nestedscroll.views.titleview;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.donfyy.e_conflictfixcases.nestedscroll.base.customview.BaseCustomView;
import com.donfyy.conflict.R;
import com.donfyy.conflict.databinding.TitleViewBinding;


/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public class TitleView extends BaseCustomView<TitleViewBinding, TitleViewViewModel> {
    public TitleView(Context context) {
        super(context);
    }

    @Override
    public int setViewLayoutId() {
        return R.layout.title_view;
    }

    @Override
    public void setDataToView(TitleViewViewModel data) {
        getDataBinding().setViewModel(data);
    }

    @Override
    public void onRootClick(View view) {
        Toast.makeText(getContext(), "clicked...", Toast.LENGTH_SHORT).show();
    }
}
