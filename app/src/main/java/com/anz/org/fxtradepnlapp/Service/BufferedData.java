package com.anz.org.fxtradepnlapp.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 8/22/2016.
 */
public class BufferedData {

    private static List<String> buf;

    public  static List<String> GetData()
    {
        return buf;
    }

    public static void SetData(List<String> sb)
    {
        buf= new ArrayList<String>(sb);
    }
}
