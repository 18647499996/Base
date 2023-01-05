package com.liudonghan.mvp;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Description
 *
 * @author Created by: Li_Min
 * Time:2018/8/4
 */
public class GsonUtils {
    static {
        mGson = new GsonBuilder().disableHtmlEscaping().create();
    }

    private static Gson mGson;

    public static String toJson(Object object) {
        if (object == null) {
            return null;
        }
        return mGson.toJson(object);

    }



    public static  <T> T fromJson(String json, Type t) throws RuntimeException {
        T t1=null;
        try {
             t1 = mGson.fromJson(json, t);

        }
        catch (RuntimeException e){
            Log.e("json解析异常：" , json);
            e.printStackTrace();
//            throw new JsonSyntaxFailException(" JsonSyntax fail: "+json,e);
            return null;
        }
        return t1 ;

    }

    public static  <T> T fromJson(String json, Class<T> t) throws RuntimeException {
        T t1=null;
        try {
            t1 = mGson.fromJson(json, t);

        }
        catch (RuntimeException e){

            e.printStackTrace();
//            throw new JsonSyntaxFailException(" JsonSyntax fail: "+json,e);
            return null;
        }
        return t1 ;

    }
    public static boolean isJsonArray(String json) {

        return json.startsWith("[");
    }

    public static <T> List<T> jsonArrayList(String launch, Class<T> tClass) {
        List<T> tList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(launch);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                T t = new Gson().fromJson(jsonObject.toString(),tClass);
                tList.add(t);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tList;
    }

    /**
     * 得到json文件中的内容
     *
     * @param context  上下文
     * @param fileName 资源文件名称
     * @return String
     */
    public static String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        //获得assets资源管理器
        AssetManager assetManager = context.getAssets();
        //使用IO流读取json文件内容
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName), "utf-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
