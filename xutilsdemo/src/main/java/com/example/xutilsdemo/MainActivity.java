package com.example.xutilsdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * xUtils中的HttpUtils的使用示例
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * get的方式请求网络字符串数据
     * 这里请求的是html百度网页的源码数据
     */
    public void btn1(View view) {
        //设置Url地址
        RequestParams entity = new RequestParams("https://www.baidu.com");
        //数据请求，这里先要设置回到的call接口对象,数据在接口对象的方法中获取
        x.http().get(entity, call);

    }

    /**
     * get的方式请求网络json字符串数据
     * 传入一些键值对的值
     * 这里演示的是ShowAPI网站中的示例
     */
    public void btn2(View view) {
        //设置Url地址
        RequestParams entity = new RequestParams("http://route.showapi.com/341-3?");
        entity.addQueryStringParameter("showapi_appid", "13074");
        entity.addQueryStringParameter("showapi_sign", "ea5b4bf2e140498bb772d1bf2a51a7a0");
        //数据请求，这里先要设置回到的call接口对象,数据在接口对象的方法中获取
        x.http().get(entity, call);
    }

    /**
     * get的方式请求网络数据
     * 这里使用的是有缓存的数据的请求
     */
    public void btn3(View view) {
        RequestParams entity = new RequestParams("https://www.baidu.com");
        // 默认缓存存活时间, 单位:毫秒.(如果服务没有返回有效的max-age或Expires)
        entity.setCacheMaxAge(1000 * 60);
        // 使用CacheCallback, xUtils将为该请求缓存数据.
        Callback.Cancelable cancelable = x.http().get(entity, new Callback.CacheCallback<String>() {

            private boolean hasError = false;
            private String result = null;

            @Override
            public boolean onCache(String result) {
                // 得到缓存数据, 缓存过期后不会进入这个方法.
                // * 如果信任该缓存返回 true, 将不再请求网络;
                //   返回 false 继续请求网络, 但会在请求头中加上ETag, Last-Modified等信息,
                //   如果服务端返回304, 则表示数据没有更新, 不继续加载数据.
                this.result = result;
                return false; // true: 信任缓存数据, 不在发起网络请求; false不信任缓存数据.
            }

            @Override
            public void onSuccess(String result) {
                // 注意: 如果服务返回304 或 onCache 选择了信任缓存, 这时result为null.
                if (result != null) {
                    this.result = result;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                hasError = true;
                Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {
                if (!hasError && result != null) {
                    // 成功获取数据
                    Toast.makeText(x.app(), "finish" + result, Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    /**
     * 网络请求时的callBack回调对象
     */
    Callback.CommonCallback call = new Callback.CommonCallback<String>() {
        @Override
        public void onSuccess(String result) {
            Toast.makeText(x.app(), result, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancelled(Callback.CancelledException cex) {
            Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFinished() {

        }
    };

}
