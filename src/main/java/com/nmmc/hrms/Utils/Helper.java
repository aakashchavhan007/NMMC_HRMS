package com.nmmc.hrms.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

public class Helper {
    public static String getLogDate() {
        // format current date in dd-mm-yyyy format and return
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
    }

    public static String getFileDate() {
        // format current date in dd-mm-yyyy format and return
        return new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss").format(new Date());
    }

    public static Date getCurrentDate() {
        // format current date in dd-mm-yyyy format and return
        return new Date();
    }

    public static Date getDateFromString(String date) {
        // convert string to date
        try {
            return new SimpleDateFormat("dd-MM-yyyy").parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static Date getDateFromStringWithSlash(String date) {
        // convert string to date
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static String getDateForReferenceNumber() {
        // convert string to date

        return new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());

    }

    /*
     * Generates a unique sequence number
     * from 1-999999999999
     * Sample Series: 000000000001
     */
    private static AtomicLong sequenceNumber = new AtomicLong(000000000001L);

    public static long getNextCode() {
        long code = sequenceNumber.getAndIncrement();
        if (code == 999999999999L) {
            sequenceNumber = new AtomicLong(000000000001L);
            code = sequenceNumber.getAndIncrement();
        }
        System.out.println("//*****Next Sequence Number*****// Code" + code);
        return code;
    }

    public static String getAffidavitDate() {
        // convert string to date

        return new SimpleDateFormat("dd MMM yyyy HH:mm:ss").format(new Date());

    }
}
