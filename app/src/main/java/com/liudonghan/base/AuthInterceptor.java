package com.liudonghan.base;


import android.text.TextUtils;

import com.liudonghan.mvp.GsonUtils;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:3/10/22
 */
public class AuthInterceptor implements Interceptor {

    private RequestBody body2;


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (request.method().equals("POST")) {
            RequestBody body = request.body();
            Map<String, Object> formMap = new HashMap<>();
            if (body instanceof FormBody) {
                FormBody formBody = (FormBody) body;
                // 从 formBody 中拿到请求参数，放入 formMap 中
                for (int i = 0; i < formBody.size(); i++) {
                    formMap.put(formBody.name(i), formBody.value(i));
                }
            }
            long currentTimeMillis = System.currentTimeMillis();

            formMap.put("Phone", "18647499996");
            formMap.put("DeviceType", "wifi");
            formMap.put("IP", "android");
            formMap.put("Time", currentTimeMillis);
            String signFmt = signParameter(currentTimeMillis, formMap.get("code"), formMap.get("Phone"));
            formMap.put("sign", signFmt);
            String stringEntity = GsonUtils.toJson(formMap);
            RequestBody body2 = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), stringEntity);
            if (body instanceof MultipartBody) {
                body2 = body;
            }
            return chain.proceed(request
                    .newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept-Encoding", "identity")
                    .addHeader("targetId", "user_2102961110")
                    .addHeader("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IjcyRENCNzE2RTE3NzAzMjQxQjM5QzU4NTlCQjNDNDI5IiwidHlwIjoiYXQrand0In0.eyJuYmYiOjE2NzI3MTc3MjIsImV4cCI6MTY3MzU4MTcyMiwiaXNzIjoiaHR0cHM6Ly9sb2dpbi5sYXd4cC5jb20iLCJjbGllbnRfaWQiOiJhcHAiLCJzdWIiOiI3MThfaXN3ZWJvYTpUcnVlX2lzd2VzYWxlOkZhbHNlX2lzYWdlbnQ6RmFsc2UiLCJhdXRoX3RpbWUiOjE2NzI3MTc3MjIsImlkcCI6ImxvY2FsIiwiVXNlcklkIjoiMjEwMjk2MTExMCIsIm5hbWUiOiIxODY0NzQ5OTk5NiIsImdpdmVuX25hbWUiOiLliJjlhqzmtrUiLCJlbWFpbCI6ImxpdWRvbmdoYW5AbGF3eHAuY29tIiwianRpIjoiREJEQUNFNzlBRDk1NTRCMThBMzAyNTkzMEM2N0I4MTEiLCJpYXQiOjE2NzI3MTc3MjIsInNjb3BlIjpbIm9wZW5pZCIsInByb2ZpbGUiLCJvZmZsaW5lX2FjY2VzcyJdLCJhbXIiOlsiY3VzdG9tIl19.jeJTt0xI0dJ9VHuvl8sBhAWiek0JdMeL7oDAQFMB_VN2S2KY_HGiSXFd_QdpAR9CNc0Uf9P-QLJFgZA86LvxPS2yvQjt6vPIiBkGk_BAuovKdu3cZ6qrBSCcdvJyD9gVaP4OUuH9FlzOMOf669h6fMkizKsRIwsGVFiUsvjIhOI37i2WZoM-HPvfIIvnzAtaO1aWyiL9ja8a6FIqO4aKj3STsMc2FYQ4WMWaY7LdrivJhrYohg0OLDKo8wWt4fwwwR7H0gU5f4F4S-_HYRMk3DZ-KZ-8GwwI_0440BFedIzFj7bQ1MlnPoIXdw4_ZOKjwShhxEKQuWulSIUtb1-DmA")
                    .post(body2)
                    .build());


        } else {
            return chain.proceed(request
                    .newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept-Encoding", "identity")
                    .addHeader("targetId", "user_2102961110")
                    .addHeader("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IjcyRENCNzE2RTE3NzAzMjQxQjM5QzU4NTlCQjNDNDI5IiwidHlwIjoiYXQrand0In0.eyJuYmYiOjE2NzI3MTc3MjIsImV4cCI6MTY3MzU4MTcyMiwiaXNzIjoiaHR0cHM6Ly9sb2dpbi5sYXd4cC5jb20iLCJjbGllbnRfaWQiOiJhcHAiLCJzdWIiOiI3MThfaXN3ZWJvYTpUcnVlX2lzd2VzYWxlOkZhbHNlX2lzYWdlbnQ6RmFsc2UiLCJhdXRoX3RpbWUiOjE2NzI3MTc3MjIsImlkcCI6ImxvY2FsIiwiVXNlcklkIjoiMjEwMjk2MTExMCIsIm5hbWUiOiIxODY0NzQ5OTk5NiIsImdpdmVuX25hbWUiOiLliJjlhqzmtrUiLCJlbWFpbCI6ImxpdWRvbmdoYW5AbGF3eHAuY29tIiwianRpIjoiREJEQUNFNzlBRDk1NTRCMThBMzAyNTkzMEM2N0I4MTEiLCJpYXQiOjE2NzI3MTc3MjIsInNjb3BlIjpbIm9wZW5pZCIsInByb2ZpbGUiLCJvZmZsaW5lX2FjY2VzcyJdLCJhbXIiOlsiY3VzdG9tIl19.jeJTt0xI0dJ9VHuvl8sBhAWiek0JdMeL7oDAQFMB_VN2S2KY_HGiSXFd_QdpAR9CNc0Uf9P-QLJFgZA86LvxPS2yvQjt6vPIiBkGk_BAuovKdu3cZ6qrBSCcdvJyD9gVaP4OUuH9FlzOMOf669h6fMkizKsRIwsGVFiUsvjIhOI37i2WZoM-HPvfIIvnzAtaO1aWyiL9ja8a6FIqO4aKj3STsMc2FYQ4WMWaY7LdrivJhrYohg0OLDKo8wWt4fwwwR7H0gU5f4F4S-_HYRMk3DZ-KZ-8GwwI_0440BFedIzFj7bQ1MlnPoIXdw4_ZOKjwShhxEKQuWulSIUtb1-DmA")
                    .build());
        }
    }

    private String signParameter(long currentTimeMillis, Object code, Object phone) {
        Map<String, Object> formMap = new HashMap<>();
        if ((String) phone == null) {
            formMap.put("Phone",  "");
        } else {
            formMap.put("Phone", "18647499996");
        }
        formMap.put("DeviceType", "wifi");
        formMap.put("Code", TextUtils.isEmpty((String) code) ? "" : (String) code);
        formMap.put("IP", "android");
        formMap.put("Time", String.valueOf(currentTimeMillis));

        String signStr;
        List<String> valueList = paramSortByValue(formMap);
        String firstPart = listToString(valueList);
        String signBefore = firstPart + "," + "JSU$E&H@F";
        try {
            signStr = md5Digest(signBefore);
        } catch (Exception var11) {
            var11.printStackTrace();
            signStr = "";
        }

        return signStr;
    }

    /**
     * 按参数名排序( 取value )
     *
     * @param formMap map集合
     * @return List<String>
     */
    private List<String> paramSortByValue(Map<String, Object> formMap) {
        Set<String> keySet = formMap.keySet();
        List<String> keyList = new ArrayList<>(keySet);
        Collections.sort(keyList, String::compareTo);
        List<String> valueList = new ArrayList<>();
        Iterator var9 = keyList.iterator();
        while (var9.hasNext()) {
            String value = ((String) formMap.get((String) var9.next()));
            valueList.add(value);
        }
        return valueList;
    }

    /**
     * 按参数名排序( 取key )
     *
     * @param formMap map集合
     * @return List<String>
     */
    private List<String> paramSortByKey(Map<String, Object> formMap) {
        Set<String> keySet = formMap.keySet();
        List<String> keyList = new ArrayList<>(keySet);
        Collections.sort(keyList, String::compareTo);

        return keyList;
    }

    private String listToString(List<String> list) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < list.size(); ++i) {
            sb.append(list.get(i)).append(",");
        }

        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    public static String md5Digest(String src) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] b = md.digest(src.getBytes("utf-8"));
        return byte2HexStr(b);
    }

    private static String byte2HexStr(byte[] b) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < b.length; ++i) {
            String s = Integer.toHexString(b[i] & 255);
            if (s.length() == 1) {
                sb.append("0");
            }

            sb.append(s);
        }

        return sb.toString();
    }
}
