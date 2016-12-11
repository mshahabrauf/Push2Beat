package com.attribes.push2beat.Utils;

/**
 * Created by android on 12/9/16.
 */

public class CommonUtils {
    private static CommonUtils Instance = null;



    private CommonUtils() {
    }

    public static CommonUtils getInstance()
    {
        if(Instance == null)
        {
            Instance = new CommonUtils();
        }
        return Instance;
    }




}
