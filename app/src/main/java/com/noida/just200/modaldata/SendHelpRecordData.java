package com.noida.just200.modaldata;

/**
 * Created by intex on 12/7/2016.
 */

public class SendHelpRecordData {
    private String sno;
    private String associateid;
    private String associatename;
    private String sendamount;
    private String transactionid;
    private String senddate;
    private String status;

    public SendHelpRecordData(String sno, String associateid, String associatename, String sendamount, String transactionid, String senddate, String status) {
        this.sno = sno;
        this.associateid = associateid;
        this.associatename = associatename;
        this.sendamount = sendamount;
        this.transactionid = transactionid;
        this.senddate = senddate;
        this.status = status;
    }


    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getAssociateid() {
        return associateid;
    }

    public void setAssociateid(String associateid) {
        this.associateid = associateid;
    }

    public String getAssociatename() {
        return associatename;
    }

    public void setAssociatename(String associatename) {
        this.associatename = associatename;
    }

    public String getSendamount() {
        return sendamount;
    }

    public void setSendamount(String sendamount) {
        this.sendamount = sendamount;
    }

    public String getSenddate() {
        return senddate;
    }

    public void setSenddate(String senddate) {
        this.senddate = senddate;
    }

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
