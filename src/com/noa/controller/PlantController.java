package com.noa.controller;

import java.net.URLEncoder;

import com.noa.test.Base64Util;
import com.noa.test.FileUtil;
import com.noa.test.HttpUtil;

public class PlantController {

    /**
    * ��Ҫ��ʾ���������蹤����
    * FileUtil,Base64Util,HttpUtil,GsonUtils���
    * https://ai.baidu.com/file/658A35ABAB2D404FBF903F64D47C1F72
    * https://ai.baidu.com/file/C8D81F3301E24D2892968F09AE1AD6E2
    * https://ai.baidu.com/file/544D677F5D4E4F17B4122FBD60DB82B3
    * https://ai.baidu.com/file/470B3ACCA3FE43788B5A963BF0B625F3
    * ����
    */
    public static String plant(String zhiwu) {
        // ����url
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/plant";
        try {
            // �����ļ�·��
            String filePath = "D:\\upload\\test.jpg";
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;

            // ע�������Ϊ�˼򻯱���ÿһ������ȥ��ȡaccess_token�����ϻ���access_token�й���ʱ�䣬 �ͻ��˿����л��棬���ں����»�ȡ��
            String accessToken = "[24.c0e4d43f6013e4979c5233ce474bfab1.2592000.1641692619.282335-25308532]";

            String result = HttpUtil.post(url, accessToken, param);
            //System.out.println(result);
            //return result;
            zhiwu=result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zhiwu;
    }
}
