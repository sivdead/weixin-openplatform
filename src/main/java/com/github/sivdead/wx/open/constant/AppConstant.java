package com.github.sivdead.wx.open.constant;

public interface AppConstant {

    String REDIS_KEY_PREFIX = "wx:";

    String result_code_success = "0";

    String result_code_error = "9999";

    String result_msg_success = "success";

    interface TargetType {

        int ALL = 0;
        int TAG = 1;
        int OPENID = 2;
    }

    interface SubscribeStatus{
        int YES = 1;
        int NO = 0;
    }

}
