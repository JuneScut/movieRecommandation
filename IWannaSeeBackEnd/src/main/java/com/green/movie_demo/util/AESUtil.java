package com.green.movie_demo.util;

import com.alibaba.fastjson.JSON;
import com.green.movie_demo.entity.WXUserInfo;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Scanner;

/**
 * 调用python文件执行AES，获取返回值
 */
public class AESUtil
{
    
    public static WXUserInfo WX_UserInfo_decode(String appid, String sessionKey, Map<String, Object>userInfo) throws Exception
    {
        File srcDir = new File("");
        String scriptFilePath = srcDir.getAbsolutePath() +
                "\\src\\main\\java\\com\\green\\movie_demo" +
                "\\script\\aes\\decode_WXUserInfo.py";
        
        String jsonStr = "";
        String[] args = {"python", scriptFilePath, appid, sessionKey, (String)userInfo.get("encryptedData"), (String)userInfo.get("iv")};
        try{
            Process process = Runtime.getRuntime().exec(args);
            process.waitFor();
//            Scanner input = new Scanner(process.getInputStream(), "utf-8");
            Scanner input = new Scanner(process.getInputStream());
            while(input.hasNextLine())
            {
                String line = input.nextLine();
                if(line.startsWith("ERROR"))
                {
                    throw new Exception(line); // 注意不要被下面的catch语句捕捉到
                }
                
                jsonStr += line;
            }
            
            input.close();
        }catch (IOException ex)
        {
            ex.printStackTrace();
        }
    
        WXUserInfo wxUserInfo = JSON.parseObject(jsonStr, WXUserInfo.class);
        
        return wxUserInfo;
    }
}
