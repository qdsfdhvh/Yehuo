package com.dian.yunbo.sited;

import android.app.Application;

import com.eclipsesource.v8.JavaVoidCallback;
import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;

/**
 * Created by Seiko on 2017/4/15. Y
 */

public class JsEngine {

    private V8 engine;
    private YhSource source;

    public JsEngine(Application app, YhSource source) {
        this.source = source;
        this.engine = V8.createV8Runtime(null, app.getApplicationInfo().dataDir);

        JavaVoidCallback callback = new JavaVoidCallback() {
            @Override
            public void invoke(V8Object v8Object, V8Array v8Array) {
                if (v8Array.length() > 0) {
                    Object arg1 = v8Array.get(0);

                    Util.log("JsEngine.print", (String) arg1);
                }
            }
        };

        engine.registerJavaMethod(callback, "print");
    }


    public synchronized JsEngine loadJs(String funs) {
        try {
            engine.executeVoidScript(funs);
        } catch (Exception e) {
            e.printStackTrace();
            Util.log("JsEngine.loadJs", e.getMessage(), e);
            throw e;
        }
        return this;
    }

    public synchronized String callJs(String fun, String... args) {
        try {
            V8Array params = new V8Array(this.engine);
            for (String p : args) {
                params.push(p);
            }
            return engine.executeStringFunction(fun, params);
        } catch (Exception e) {
            e.printStackTrace();
            Util.log("JsEngine.callJs:" + fun, e.getMessage(), e);
            return null;
        }
    }

    public synchronized void release() {
        if (engine != null) {
            engine.getLocker().release();
            engine = null;
            source = null;
        }
    }

}
