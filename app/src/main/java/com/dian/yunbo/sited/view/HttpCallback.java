package com.dian.yunbo.sited.view;

import com.dian.yunbo.sited.HttpMessage;

/**
 * Created by Seiko on 2017/4/15. Y
 */

public interface HttpCallback {
    void run(Integer code, HttpMessage msg2, String text);
}
