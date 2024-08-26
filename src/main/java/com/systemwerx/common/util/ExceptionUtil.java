package com.systemwerx.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import com.systemwerx.common.event.Log;

public final class ExceptionUtil {
    public static void printStackTrace(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String sStackTrace = sw.toString(); 
        System.out.println(sStackTrace);
        Log.error(ex.getStackTrace().toString());
    }
    
}
