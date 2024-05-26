package me.nslot.jcomp.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import me.nslot.jcomp.wrappers.Problem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

public class Utils {

    private static final String[] positions = {
            "1st","2nd","3rd", "4th", "5th", "6th", "7th", "8th", "9th", "10th", "11th", "12th", "13th", "14th", "15th", "16th", "17th", "18th", "19th", "20th", "21st", "22nd", "23rd", "24th", "25th", "26th", "27th", "28th", "29th", "30th"
    };


    public static String getLoginID(HttpServletRequest req) {
        for (Cookie cookie : req.getCookies())
            if (cookie.getName().equals("loginID"))
                return cookie.getValue();
        return null;
    }

    public static String formattedPosition(int position){
        if (position-1 > positions.length)
            return position +"th";
        return positions[position-1];
    }
}