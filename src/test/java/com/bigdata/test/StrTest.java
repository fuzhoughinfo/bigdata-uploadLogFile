package com.bigdata.test;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.net.NetUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StrTest {

    public static void main(String[] args) {
//        String s="api.2022-05-12.13.log";
//        String []fileDate=s.split("\\.");
//        for (int i=0;i<fileDate.length;i++){
//            System.out.println(fileDate[i]);
//        }
//        InetAddress localhost = NetUtil.getLocalhost();
//        Date date3 = DateUtil.date(System.currentTimeMillis());
//        System.out.println(((DateTime) date3).toTimestamp());

//        JSONObject param = new JSONObject();
//        param.put("account", "11");
//        param.put("password", "222");
//        JSONObject authCreateObj = JSONUtil.createObj().put("account","11").put("password","11");
//        System.out.println(authCreateObj.toString());

        String now = DateUtil.now();
        System.out.println(now);
    }


    public static List<String> readlines(File FileNames){
        List<String> resultStr = new ArrayList<String>();
        try {
            FileReader fileReader = new FileReader(FileNames);

            LineNumberReader reader = null;
            int [] lineNum = new int[5];
            reader = new LineNumberReader(fileReader.getReader());
            int lineNumber = lineNum.length;
//           int line = 1;
            int i = 0;
            String s = "";
            while((s = reader.readLine()) != null){
                if(i < lineNumber ){
                    i++;
                    System.out.println(s);
//                   resultStr.add(s);&& line ==lineNum[i]

                }
//               line++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultStr;
    }


    public static  void closeResource(FileReader in,LineNumberReader reader){
        try {
            if(reader != null){
                reader.close();
            }
//            if(in != null){
//                in.close();
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
