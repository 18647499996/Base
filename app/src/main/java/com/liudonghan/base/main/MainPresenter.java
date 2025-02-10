package com.liudonghan.base.main;


import com.liudonghan.base.ChatService;
import com.liudonghan.base.bean.RequestXhsBean;
import com.liudonghan.mvp.ADBaseExceptionManager;
import com.liudonghan.mvp.ADBaseLogInterceptor;
import com.liudonghan.mvp.ADBaseOkHttpClient;
import com.liudonghan.mvp.ADBaseRequestResult;
import com.liudonghan.mvp.ADBaseRetrofitManager;
import com.liudonghan.mvp.ADBaseSubscription;
import com.liudonghan.mvp.ADBaseTransformerManager;
import com.liudonghan.utils.ADParamsUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:
 */
public class MainPresenter extends ADBaseSubscription<MainContract.View> implements MainContract.Presenter {


    MainPresenter(MainContract.View view) {
        super(view);
    }

    @Override
    public void onSubscribe() {
        ADBaseRetrofitManager
                .getInstance()
                .baseHttpUrl("https://edith.xiaohongshu.com/")
                .baseOkHttpClient(ADBaseOkHttpClient.getInstance().getOkHttpClient(new ADBaseLogInterceptor()).build())
                .baseRetrofitManager(ChatService.class)
                .getXhsHomeFeed(ADParamsUtils.newParams()
                        .put("Referer", "https://www.xiaohongshu.com/")
                        .put("Cookie", "abRequestId=9a3771b6-f6e3-51b9-adb8-cd26d8f65ab3; a1=194159851000nwk62i8p8kk7bcwouscyfwc88f51m30000994136; webId=f655ef955f27a463ea14e79ef27d349b; gid=yj4y2jYK22iyyj4y2jY2yEi2888IETKJ3YUYSy6TTWDSEYq8l7MSvY888jj4yqK80j4fJY22; web_session=0400698dddbc790e903d9034b1354b73fbd64c; xsecappid=xhs-pc-web; webBuild=4.55.1; acw_tc=0ad59cfe17389805016562605e145336792c3c305f4f7730a0d2841b6f377f; websectiga=2845367ec3848418062e761c09db7caf0e8b79d132ccdd1a4f8e64a11d0cac0d; sec_poison_id=3500bef5-c802-449a-b9c6-d741953848fe; unread={%22ub%22:%2267a57bcf000000001902e1b2%22%2C%22ue%22:%22678bdd540000000017003eb0%22%2C%22uc%22:14}")
                        .put("x-b3-traceid", "1b5b34b4861e7c33")
                        .put("x-mns", "awePO6fun00hCyPxT7ixJOTjLMxeQ4hKtGfjguJLBJ5ETOlceLYpL14dMuxx7x6vnGvPFnu3SvcvlgFkyD/S8u8lcDmL1eZG6oW9HZLP64p3XgHxuC3cOyz1Pzfv6PxWxEWpNPawgDPFYEv7HY7y7eow23NJTJFa/4CWGceIpnIMmd2hjazDHL6SxBh9H2WWJ8FwtCP+6gcniXXNF3k1co9tm+9tWYcK5S5wiP2TQhCyjozeoa7ShwPHLWE+R3moHkGxcc911j4SQdCPaSh21wZwyxyTu7QhbcSkw+YN1RieajQL3di+ZK9Dj56zt9hxtWKKI8SBvhCFZZ8Z39aFu78TbfNJFu01ISW2W8cIzww5JlfnwT/Z90KZcvY5e07SCNJnYXcWpE1HRLc6z6STT2cXvJKZZNz155onJLtXyzLwCjvBB0xGm1Owhu091OGwY1mdfo5PnXNfmz9pXcdPKEeQnJDmyZo9G6d/Ow+i1lc9ZGR5Lc4LHywnt/5hewvyac")
                        .put("x-s", "XYW_eyJzaWduU3ZuIjoiNTYiLCJzaWduVHlwZSI6IngyIiwiYXBwSWQiOiJ4aHMtcGMtd2ViIiwic2lnblZlcnNpb24iOiIxIiwicGF5bG9hZCI6IjA3YjUwNmQyYTM5OTA1OGU5NmM3NWMxZjExMzQ2N2ZmNTgyYTI5ZjhhMTc2NDdlNTNmNDNiZDJhOGQ5MTc4Nzg3ZjViOWE0MzY2NGQ3Y2VjNmRlMzBhMWJkMjNlNzBhNDJlYjIxNmE2OGVhNDhjMDQ3Yjg0ZTc4YTRkNDViYjI3ODM2OWRhNzAwYjg1ZmNlNDE3ZmNkNTA2ZWNlMDAwMzBhZDljM2JiMTI4MDEyNGZlOTZiOWY0MDAxNGNmYTYzNWVjZGUzMzczNTE4OTM1OWM4NmE2MzJmNDg3YTA3Y2NmNTcxNWU4NmU4N2YzNzE1ZTM1ODk1NDA1OGZhMDQ5ZjQ3YTU0YTY0N2EyNzkzYjA5ZTY2ZTVhNDUxNjRlMTQ3Y2I1YjFhY2RlYzNhMmYyOTQyY2IxZmY4OGQwMDIyYTE2ZDY2NTI0ODI3ZTJmYTNjNmY5MGEwNmRhZDU1YzcxNzRmOTQ0N2U0ZmZlNzEwZDc2MmUzMWIxMTM5N2UwNjFjZDJjMTY1ZjUyYjlmZjliNjc4MWNmMjBlNjZjZDM4YjE2In0=")
                        .params(), ADBaseTransformerManager.transformJson(new RequestXhsBean()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new ADBaseRequestResult<Object>() {
                    @Override
                    protected void onCompletedListener() {

                    }

                    @Override
                    protected void onErrorListener(ADBaseExceptionManager.ApiException e) {

                    }

                    @Override
                    protected void onNextListener(Object o) {

                    }
                });
    }

}