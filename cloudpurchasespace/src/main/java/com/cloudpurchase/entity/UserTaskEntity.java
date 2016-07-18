package com.cloudpurchase.entity;

import android.widget.ImageView;

/**
 * 任务中下 下方Item的实体类
 */
public class UserTaskEntity {

    public String taskTitle;//任务名称
//    public String taskImg;//以后使用url形式进行网络请求加载图片
    public int taskImg;
    public String num;//任务奖励积分
    public boolean flag;//判断是否完成任务的标志
}
