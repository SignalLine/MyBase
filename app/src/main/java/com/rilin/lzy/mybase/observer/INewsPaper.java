package com.rilin.lzy.mybase.observer;

/**
 * Created by lzy on 16/9/22.
 * 报纸接口
 */
public interface INewsPaper {
    //添加订阅者
    void RegisterSubscriber(ISubScribe subScribe);

    //取消订阅者
    void RemoveSubscriber(ISubScribe subScribe);

    //发送报纸
    void SendPaper();
}
