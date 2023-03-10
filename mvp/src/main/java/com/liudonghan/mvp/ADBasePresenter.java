/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liudonghan.mvp;

/**
 * Description：presenter通用接口定义
 *
 * @author Created by: Li_Min
 * Time:2018/8/2
 */
public interface ADBasePresenter {

    /**
     * 初始化订阅数据
     */
     void onSubscribe();

    /**
     * 释放presenter资源
     */
    void onDestroy();

    /**
     * 获取网络列表数据
     * @param o 请求参数
     * @param page 请求页数
     * @param refreshType 刷新数据（true 刷新 false 加载）
     */
    void getNetworkListData(int page, boolean refreshType, Object... o);

    /**
     * 获取网络列表数据
     *
     * @param o          请求参数
     * @param page       请求页数
     * @param limit      分页数量
     * @param refreshType 刷新数据（true 刷新 false 加载）
     */
    void getNetworkListData(int page, int limit, boolean refreshType, Object... o);


}
