package com.noida.just200.modaldata;

/**
 * Created by deepak on 11/21/2016.
 */

public class GetReceivehelpData {

  private String  LevelPlan;
    private int sno;
    private String LoginId;
    private String  HelpRemark;
    private String  AssignmentNo;
    private String   DrAmount;
    private String  CreatedDate;
    private String  AccountId;
    private String     MemberName;
    private String  CancelButtonView;
    private String        MemberMobile;
    private String   MemberAccNo;
    private String       MemberBankName;
    private String  MemberBranch;
    private String        IFSCCode;
    private String ApprovedDate;
    private String        SponsorName;
    private String UserType;
    private String   RemainingTime;
    private String PK_HelpDRCRID;
    private String Receipt;

    public GetReceivehelpData() {
    }

    public GetReceivehelpData(int sno, String levelPlan, String loginId,String pk_crcid, String helpRemark, String assignmentNo, String drAmount, String createdDate, String accountId, String memberName, String cancelButtonView, String memberMobile, String memberAccNo, String memberBankName, String memberBranch, String IFSCCode, String approvedDate, String sponsorName, String userType, String remainingTime,String receipt) {
         this.sno = sno;
        LevelPlan = levelPlan;
        LoginId = loginId;
        PK_HelpDRCRID = pk_crcid;
        HelpRemark = helpRemark;
        AssignmentNo = assignmentNo;
        DrAmount = drAmount;
        CreatedDate = createdDate;
        AccountId = accountId;
        MemberName = memberName;
        CancelButtonView = cancelButtonView;
        MemberMobile = memberMobile;
        MemberAccNo = memberAccNo;
        MemberBankName = memberBankName;
        MemberBranch = memberBranch;
        this.IFSCCode = IFSCCode;
        ApprovedDate = approvedDate;
        SponsorName = sponsorName;
        UserType = userType;
        RemainingTime = remainingTime;
        Receipt = receipt;
    }

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
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

    public String getPK_HelpDRCRID() {
        return PK_HelpDRCRID;
    }

    public void setPK_HelpDRCRID(String PK_HelpDRCRID) {
        this.PK_HelpDRCRID = PK_HelpDRCRID;
    }

    public String getHelpRemark() {
        return HelpRemark;
    }

    public void setHelpRemark(String helpRemark) {
        HelpRemark = helpRemark;
    }

    public String getAssignmentNo() {
        return AssignmentNo;
    }

    public void setAssignmentNo(String assignmentNo) {
        AssignmentNo = assignmentNo;
    }

    public String getDrAmount() {
        return DrAmount;
    }

    public void setDrAmount(String drAmount) {
        DrAmount = drAmount;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getAccountId() {
        return AccountId;
    }

    public void setAccountId(String accountId) {
        AccountId = accountId;
    }

    public String getMemberName() {
        return MemberName;
    }

    public void setMemberName(String memberName) {
        MemberName = memberName;
    }

    public String getCancelButtonView() {
        return CancelButtonView;
    }

    public void setCancelButtonView(String cancelButtonView) {
        CancelButtonView = cancelButtonView;
    }

    public String getMemberMobile() {
        return MemberMobile;
    }

    public void setMemberMobile(String memberMobile) {
        MemberMobile = memberMobile;
    }

    public String getMemberAccNo() {
        return MemberAccNo;
    }

    public void setMemberAccNo(String memberAccNo) {
        MemberAccNo = memberAccNo;
    }

    public String getMemberBankName() {
        return MemberBankName;
    }

    public void setMemberBankName(String memberBankName) {
        MemberBankName = memberBankName;
    }

    public String getMemberBranch() {
        return MemberBranch;
    }

    public void setMemberBranch(String memberBranch) {
        MemberBranch = memberBranch;
    }

    public String getIFSCCode() {
        return IFSCCode;
    }

    public void setIFSCCode(String IFSCCode) {
        this.IFSCCode = IFSCCode;
    }

    public String getApprovedDate() {
        return ApprovedDate;
    }

    public void setApprovedDate(String approvedDate) {
        ApprovedDate = approvedDate;
    }

    public String getSponsorName() {
        return SponsorName;
    }

    public void setSponsorName(String sponsorName) {
        SponsorName = sponsorName;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public String getRemainingTime() {
        return RemainingTime;
    }

    public void setRemainingTime(String remainingTime) {
        RemainingTime = remainingTime;
    }

    public String getReceipt() {
        return Receipt;
    }

    public void setReceipt(String receipt) {
        Receipt = receipt;
    }
}
