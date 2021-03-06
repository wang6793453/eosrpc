package com.sd.eos.rpc;

import android.app.Application;

import com.sd.eos.rpc.model.AccountHolder;
import com.sd.eos.rpc.model.AccountModel;
import com.sd.eos.rpc.utils.AppRpcApiExecutor;
import com.sd.eos.rpc.utils.CacheObjectConverter;
import com.sd.lib.cache.CacheConfig;
import com.sd.lib.cache.FCache;
import com.sd.lib.eos.rpc.core.FEOSManager;

public class App extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        FCache.init(new CacheConfig.Builder()
                .setObjectConverter(new CacheObjectConverter())
                .build(this));

        AccountHolder.get().add(new AccountModel("liuliqin1234",
                "5KDnhagYqzM9mtZ64CqJtZ1pUmCapsdVPHSoxh1ovwNbA98XUAZ",
                "EOS6xAkdK2KKXyccBdjncB8om7iNDc41vmSuJgServRd9gAiWFSyw"));

        /**
         * 设置节点地址（必须）
         */
        FEOSManager.getInstance().setBaseUrl("https://jungle2.cryptolions.io:443");
        /**
         * 设置接口请求对象（非必须）
         */
        FEOSManager.getInstance().setApiExecutor(new AppRpcApiExecutor());
    }
}
