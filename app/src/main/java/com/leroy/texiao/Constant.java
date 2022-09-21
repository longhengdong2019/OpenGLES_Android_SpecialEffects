package com.leroy.texiao;

import android.content.Context;

import com.leroy.texiao.filter.base.MBaseFilter;
import com.leroy.texiao.filter.base.MBsTransFilter;
import com.leroy.texiao.filter.image.GaussFilter;
import com.leroy.texiao.filter.image.GrayFilter;
import com.leroy.texiao.filter.image.MoveBlurFilter;
import com.leroy.texiao.filter.image.PipFilter;
import com.leroy.texiao.filter.image.ScaleFilter;
import com.leroy.texiao.filter.image.SplitFilter;
import com.leroy.texiao.filter.trans.BlurTransFilter;
import com.leroy.texiao.filter.trans.CutFilter;
import com.leroy.texiao.filter.trans.FlipFilter;
import com.leroy.texiao.filter.trans.MixTransFilter;
import com.leroy.texiao.filter.trans.MoveTransFilter;
import com.leroy.texiao.filter.trans.PageUpFilter;
import com.leroy.texiao.filter.trans.WipeTransFilter;
import com.leroy.texiao.utils.FilterBean;


public class Constant {

    public final static String EXTRA = "extra";
    public final static String EXTRA_IMAGE = "extra_image";
    public final static String EXTRA_TRANS = "extra_trans";

    public final static int orientation = 1;
    public final static int VERTICAL = 2;
    public final static int HORIZONTAL = 1;

    // 静态特效
    public final static String img_gauss = "高斯模糊";
    public final static String img_moveblur = "运动模糊";
    public final static String img_scale = "缩放特效";
    public final static String img_pip = "画中画";
    public final static String img_gray = "灰度滤镜";
    public final static String img_two = "两分屏";
    public final static String img_three = "三分屏";
    public final static String img_four = "四分屏";
    public final static String img_six = "六分屏";
    public final static String img_nine = "九分屏";

    // 动态特效
    public final static String trans_mix = "混合切换";
    public final static String trans_blue_trans = "模糊切换";
    public final static String trans_pageup_l = "向左翻页";
    public final static String trans_pageup_r = "向右翻页";
    public final static String cut_ud_1 = "左右切割(一)";
    public final static String cut_ud_2 = "左右切割(二)";
    public final static String cut_lr_1 = "上下切割(一)";
    public final static String cut_lr_2 = "上下切割(二)";

    public final static String trans_flip_hori = "水平翻转";
    public final static String trans_flip_verl = "垂直翻转";
    public final static String trans_move_up = "向上移动";
    public final static String trans_move_down = "向下移动";
    public final static String trans_move_left = "向左移动";
    public final static String trans_move_right = "向右移动";
    public final static String trans_move_lup = "左上移动";
    public final static String trans_move_rup = "右上移动";
    public final static String trans_move_ldown = "左下移动";
    public final static String trans_move_rdown = "右下移动";

    public final static String trans_wipe_up = "向上抹除";
    public final static String trans_wipe_down = "向下抹除";
    public final static String trans_wipe_left = "向左抹除";
    public final static String trans_wipe_right = "向右抹除";
    public final static String trans_wipe_rup = "右上抹除";
    public final static String trans_wipe_ldown = "左下抹除";
    public final static String trans_wipe_middle = "中间抹除";
    public final static String trans_wipe_circle = "圆心抹除";

    // 静态特效列表
    public static FilterBean[] img_filters = {
            new FilterBean(img_gauss),
            new FilterBean(img_moveblur),
            new FilterBean(img_scale),
            new FilterBean(img_pip),
            new FilterBean(img_gray),
            new FilterBean(img_two,"#00D689"),
            new FilterBean(img_three,"#00D689"),
            new FilterBean(img_four,"#00D689"),
            new FilterBean(img_six,"#00D689"),
            new FilterBean(img_nine,"#00D689"),
    };

    // 动态特效列表
    public static FilterBean[] trans_filters = {
            new FilterBean(trans_mix),
            new FilterBean(trans_blue_trans),
            new FilterBean(trans_pageup_l,"#F08080"),
            new FilterBean(trans_pageup_r,"#F08080"),
            new FilterBean(trans_flip_hori,"#00D689"),
            new FilterBean(trans_flip_verl,"#00D689"),
            new FilterBean(cut_ud_1,"#FF9933"),
            new FilterBean(cut_ud_2,"#FF9933"),
            new FilterBean(cut_lr_1,"#FF9933"),
            new FilterBean(cut_lr_2,"#FF9933"),
            new FilterBean(trans_move_up,"#0099CC"),
            new FilterBean(trans_move_down,"#0099CC"),
            new FilterBean(trans_move_left,"#0099CC"),
            new FilterBean(trans_move_right,"#0099CC"),
            new FilterBean(trans_move_lup,"#0099CC"),
            new FilterBean(trans_move_rup,"#0099CC"),
            new FilterBean(trans_move_ldown,"#0099CC"),
            new FilterBean(trans_move_rdown,"#0099CC"),
            new FilterBean(trans_wipe_up,"#CC99CC"),
            new FilterBean(trans_wipe_down,"#CC99CC"),
            new FilterBean(trans_wipe_left,"#CC99CC"),
            new FilterBean(trans_wipe_right,"#CC99CC"),
            new FilterBean(trans_wipe_rup,"#CC99CC"),
            new FilterBean(trans_wipe_ldown,"#CC99CC"),
            new FilterBean(trans_wipe_middle,"#CC99CC"),
            new FilterBean(trans_wipe_circle,"#CC99CC")
    };

    public static FilterBean[] getFilters(String filterType){
        if (EXTRA_IMAGE.equals(filterType))
            return img_filters;
        else if (EXTRA_TRANS.equals(filterType))
            return trans_filters;
        else
            return img_filters;
    }

    /**
     * 获取静态特效列表
     * @param context
     * @param filterName
     * @return
     */
    public static MBaseFilter getImageFilters(Context context, String filterName) {
        MBaseFilter filter = null;
        switch (filterName){
            case img_gauss:
                filter = new GaussFilter(context);
                break;
            case img_moveblur:
                filter = new MoveBlurFilter(context);
                break;
            case img_scale:
                filter = new ScaleFilter(context);
                break;
            case img_pip:
                filter = new PipFilter(context);
                break;
            case img_gray:
                filter = new GrayFilter(context);
                break;
            case img_two:
                filter = new SplitFilter(context, 2.0f);
                break;
            case img_three:
                filter = new SplitFilter(context, 3.0f);
                break;
            case img_four:
                filter = new SplitFilter(context, 4.0f);
                break;
            case img_six:
                filter = new SplitFilter(context, 6.0f);
                break;
            case img_nine:
                filter = new SplitFilter(context, 9.0f);
                break;
        }
        return filter;
    }

    /**
     * 获取动态特效列表
     * @param context
     * @param filterName
     * @return
     */
    public static MBsTransFilter getTransFilter(Context context, String filterName) {
        MBsTransFilter filter = null;
        switch (filterName){
            case trans_mix:
                filter = new MixTransFilter(context);
                break;
            case trans_blue_trans:
                filter = new BlurTransFilter(context);
                break;
            case trans_pageup_l:
                filter = new PageUpFilter(context,trans_pageup_l);
                break;
            case trans_pageup_r:
                filter = new PageUpFilter(context,trans_pageup_r);
                break;
            case cut_ud_1:
                filter = new CutFilter(context,cut_ud_1);
                break;
            case cut_ud_2:
                filter = new CutFilter(context,cut_ud_2);
                break;
            case cut_lr_1:
                filter = new CutFilter(context,cut_lr_1);
                break;
            case cut_lr_2:
                filter = new CutFilter(context,cut_lr_2);
                break;

            case trans_flip_hori:
                filter = new FlipFilter(context,HORIZONTAL);
                break;
            case trans_flip_verl:
                filter = new FlipFilter(context,VERTICAL);
                break;
            case trans_move_up:
                filter = new MoveTransFilter(context,trans_move_up);
                break;
            case trans_move_down:
                filter = new MoveTransFilter(context,trans_move_down);
                break;
            case trans_move_left:
                filter = new MoveTransFilter(context,trans_move_left);
                break;
            case trans_move_right:
                filter = new MoveTransFilter(context,trans_move_right);
                break;
            case trans_move_lup:
                filter = new MoveTransFilter(context,trans_move_lup);
                break;
            case trans_move_rup:
                filter = new MoveTransFilter(context,trans_move_rup);
                break;
            case trans_move_ldown:
                filter = new MoveTransFilter(context,trans_move_ldown);
                break;
            case trans_move_rdown:
                filter = new MoveTransFilter(context,trans_move_rdown);
                break;

            case trans_wipe_up:
                filter = new WipeTransFilter(context,trans_wipe_up);
                break;
            case trans_wipe_down:
                filter = new WipeTransFilter(context,trans_wipe_down);
                break;
            case trans_wipe_left:
                filter = new WipeTransFilter(context,trans_wipe_left);
                break;
            case trans_wipe_right:
                filter = new WipeTransFilter(context,trans_wipe_right);
                break;
            case trans_wipe_rup:
                filter = new WipeTransFilter(context, trans_wipe_rup);
                break;
            case trans_wipe_ldown:
                filter = new WipeTransFilter(context, trans_wipe_ldown);
                break;
            case trans_wipe_middle:
                filter = new WipeTransFilter(context,trans_wipe_middle);
                break;
            case trans_wipe_circle:
                filter = new WipeTransFilter(context,trans_wipe_circle);
                break;
        }
        return filter;
    }


}
