package com.canmeizhexue.common.views.viewflow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.canmeizhexue.common.R;

/**BannerView
 * Created by canmeizhexue on 2016-8-12.
 */
public class BannerView extends RelativeLayout{
    private ViewFlow viewFlow;
    private CircleFlowIndicator circleFlowIndicator;
    public BannerView(Context context) {
        super(context);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void init(int widthPx,int heightPx){
        View contentView = View.inflate(getContext(), R.layout.banner_view, this);
        viewFlow = (ViewFlow) contentView.findViewById(R.id.banner_viewflow);
        circleFlowIndicator = (CircleFlowIndicator) contentView.findViewById(R.id.banner_flow_indicator);
    }
}
