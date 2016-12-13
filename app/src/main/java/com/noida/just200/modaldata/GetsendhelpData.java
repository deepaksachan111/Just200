package com.noida.just200.modaldata;

/**
 * Created by intex on 11/21/2016.
 */
public class GetsendhelpData {
    private  int sno;
    private String AccountName;
    private String  Amount;
    private String AssignmentNo;
    private String CreatedDate;
    private String MemberAccNo;
    private String LevelPlan;
    private String LoginId;
    private String MemberMobile;
    private String MemberName;
    private String Narration;
    private String PK_HelpDRCRID;
    private String PaytmID;
    private String Receipt;
    private String RemainingTime;
    private String HelpRemark;


    public GetsendhelpData() {
    }

    public GetsendhelpData(int sn,String accountName, String amount, String assignmentNo, String createdDate, String memberAccNo, String levelPlan, String loginId, String memberMobile, String memberName, String narration, String PK_HelpDRCRID, String paytmID, String receipt, String remainingTime,String helpRemark) {
         sno = sn;
        AccountName = accountName;
        Amount = amount;
        AssignmentNo = assignmentNo;
        CreatedDate = createdDate;
        MemberAccNo = memberAccNo;
        LevelPlan = levelPlan;
        LoginId = loginId;
        MemberMobile = memberMobile;
        MemberName = memberName;
        Narration = narration;
        this.PK_HelpDRCRID = PK_HelpDRCRID;
        PaytmID = paytmID;
        Receipt = receipt;
        RemainingTime = remainingTime;
        HelpRemark =helpRemark;
    }

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String accountName) {
        AccountName = accountName;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getAssignmentNo() {
        return AssignmentNo;
    }

    public void setAssignmentNo(String assignmentNo) {
        AssignmentNo = assignmentNo;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getMemberAccNo() {
        return MemberAccNo;
    }

    public void setMemberAccNo(String memberAccNo) {
        MemberAccNo = memberAccNo;
    }

    public String getLevelPlan() {
        return LevelPlan;
    }

    public void setLevelPlan(String levelPlan) {
        LevelPlan = levelPlan;
    }

    public String getLoginId() {
        return LoginId;
    }

    public void setLoginId(String loginId) {
        LoginId = loginId;
    }

    public String getMemberMobile() {
        return MemberMobile;
    }

    public void setMemberMobile(String memberMobile) {
        MemberMobile = memberMobile;
    }

    public String getMemberName() {
        return MemberName;
    }

    public void setMemberName(String memberName) {
        MemberName = memberName;
    }

    public String getNarration() {
        return Narration;
    }

    public void setNarration(String narration) {
        Narration = narration;
    }

    public String getPK_HelpDRCRID() {
        return PK_HelpDRCRID;
    }

    public void setPK_HelpDRCRID(String PK_HelpDRCRID) {
        this.PK_HelpDRCRID = PK_HelpDRCRID;
    }

    public String getPaytmID() {
        return PaytmID;
    }

    public void setPaytmID(String paytmID) {
        PaytmID = paytmID;
    }

    public String getReceipt() {
        return Receipt;
    }

    public void setReceipt(String receipt) {
        Receipt = receipt;
    }

    public String getRemainingTime() {
        return RemainingTime;
    }

    public void setRemainingTime(String remainingTime) {
        RemainingTime = remainingTime;
    }

    public String getHelpRemark() {
        return HelpRemark;
    }

    public void setHelpRemark(String helpRemark) {
        HelpRemark = helpRemark;
    }
}
