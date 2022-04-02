package classes.it.hdp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReqAeps {

    @SerializedName("mobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("user_agent")
    @Expose
    private String userAgent;
    @SerializedName("request")
    @Expose
    private RequestRes request;

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public RequestRes getRequest() {
        return request;
    }

    public void setRequest(RequestRes request) {
        this.request = request;
    }


    public static class RequestRes {

        @SerializedName("outlet_id")
        @Expose
        private String outletId;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("aadhaar_uid")
        @Expose
        private String aadhaarUid;
        @SerializedName("bankiin")
        @Expose
        private String bankiin;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("agent_id")
        @Expose
        private String agentId;
        @SerializedName("sp_key")
        @Expose
        private String spKey;
        @SerializedName("pidDataType")
        @Expose
        private String pidDataType;
        @SerializedName("pidData")
        @Expose
        private String pidData;
        @SerializedName("ci")
        @Expose
        private String ci;
        @SerializedName("dc")
        @Expose
        private String dc;
        @SerializedName("dpId")
        @Expose
        private String dpId;
        @SerializedName("errCode")
        @Expose
        private String errCode;
        @SerializedName("errInfo")
        @Expose
        private String errInfo;
        @SerializedName("fCount")
        @Expose
        private String fCount;
        @SerializedName("tType")
        @Expose
        private String tType;
        @SerializedName("hmac")
        @Expose
        private String hmac;
        @SerializedName("iCount")
        @Expose
        private String iCount;
        @SerializedName("mc")
        @Expose
        private String mc;
        @SerializedName("mi")
        @Expose
        private String mi;
        @SerializedName("nmPoints")
        @Expose
        private String nmPoints;
        @SerializedName("pCount")
        @Expose
        private String pCount;
        @SerializedName("pType")
        @Expose
        private String pType;
        @SerializedName("qScore")
        @Expose
        private String qScore;
        @SerializedName("rdsId")
        @Expose
        private String rdsId;
        @SerializedName("rdsVer")
        @Expose
        private String rdsVer;
        @SerializedName("sessionKey")
        @Expose
        private String sessionKey;
        @SerializedName("srno")
        @Expose
        private String srno;

        public String getOutletId() {
            return outletId;
        }

        public void setOutletId(String outletId) {
            this.outletId = outletId;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getAadhaarUid() {
            return aadhaarUid;
        }

        public void setAadhaarUid(String aadhaarUid) {
            this.aadhaarUid = aadhaarUid;
        }

        public String getBankiin() {
            return bankiin;
        }

        public void setBankiin(String bankiin) {
            this.bankiin = bankiin;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAgentId() {
            return agentId;
        }

        public void setAgentId(String agentId) {
            this.agentId = agentId;
        }

        public String getSpKey() {
            return spKey;
        }

        public void setSpKey(String spKey) {
            this.spKey = spKey;
        }

        public String getPidDataType() {
            return pidDataType;
        }

        public void setPidDataType(String pidDataType) {
            this.pidDataType = pidDataType;
        }

        public String getPidData() {
            return pidData;
        }

        public void setPidData(String pidData) {
            this.pidData = pidData;
        }

        public String getCi() {
            return ci;
        }

        public void setCi(String ci) {
            this.ci = ci;
        }

        public String getDc() {
            return dc;
        }

        public void setDc(String dc) {
            this.dc = dc;
        }

        public String getDpId() {
            return dpId;
        }

        public void setDpId(String dpId) {
            this.dpId = dpId;
        }

        public String getErrCode() {
            return errCode;
        }

        public void setErrCode(String errCode) {
            this.errCode = errCode;
        }

        public String getErrInfo() {
            return errInfo;
        }

        public void setErrInfo(String errInfo) {
            this.errInfo = errInfo;
        }

        public String getFCount() {
            return fCount;
        }

        public void setFCount(String fCount) {
            this.fCount = fCount;
        }

        public String getTType() {
            return tType;
        }

        public void setTType(String tType) {
            this.tType = tType;
        }

        public String getHmac() {
            return hmac;
        }

        public void setHmac(String hmac) {
            this.hmac = hmac;
        }

        public String getICount() {
            return iCount;
        }

        public void setICount(String iCount) {
            this.iCount = iCount;
        }

        public String getMc() {
            return mc;
        }

        public void setMc(String mc) {
            this.mc = mc;
        }

        public String getMi() {
            return mi;
        }

        public void setMi(String mi) {
            this.mi = mi;
        }

        public String getNmPoints() {
            return nmPoints;
        }

        public void setNmPoints(String nmPoints) {
            this.nmPoints = nmPoints;
        }

        public String getPCount() {
            return pCount;
        }

        public void setPCount(String pCount) {
            this.pCount = pCount;
        }

        public String getPType() {
            return pType;
        }

        public void setPType(String pType) {
            this.pType = pType;
        }

        public String getQScore() {
            return qScore;
        }

        public void setQScore(String qScore) {
            this.qScore = qScore;
        }

        public String getRdsId() {
            return rdsId;
        }

        public void setRdsId(String rdsId) {
            this.rdsId = rdsId;
        }

        public String getRdsVer() {
            return rdsVer;
        }

        public void setRdsVer(String rdsVer) {
            this.rdsVer = rdsVer;
        }

        public String getSessionKey() {
            return sessionKey;
        }

        public void setSessionKey(String sessionKey) {
            this.sessionKey = sessionKey;
        }

        public String getSrno() {
            return srno;
        }

        public void setSrno(String srno) {
            this.srno = srno;
        }

    }

}
