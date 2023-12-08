package de.koppy.economy.api;

import java.util.Date;

public class Log {
    private String sentfrom;
    private String sendto;
    private double amount;
    private String reason;
    private double moneybefore;
    private Date date;

    public Log(String sentfrom, String sendto, double amount, String reason, double moneybefore, Date date) {
        this.sentfrom = sentfrom;
        this.sendto = sendto;
        this.amount = amount;
        this.reason = reason.replace(";", ":");
        this.moneybefore = moneybefore;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public String getSentfrom() {
        return sentfrom;
    }

    public String getSendto() {
        return sendto;
    }

    public double getAmount() {
        return amount;
    }

    public String getReason() {
        return reason;
    }

    public double getMoneybefore() {
        return moneybefore;
    }

    @Override
    public String toString() {
        return this.sentfrom+";"+this.sendto+";"+this.reason+";"+this.amount+";"+this.moneybefore+";"+this.date.getTime();
    }

    public static Log fromString(String data) {
        String[] datas = data.split(";");
        String sentfrom = datas[0];
        String sentto = datas[1];
        String reason = datas[2];
        String amount = datas[3];
        String moneybefore = datas[4];
        String time = datas[5];
        return new Log(sentfrom, sentto, Double.parseDouble(amount), reason, Double.parseDouble(moneybefore), new Date(Long.parseLong(time)));
    }

}