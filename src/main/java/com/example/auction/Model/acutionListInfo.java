package com.example.auction.Model;

import java.util.List;

public class acutionListInfo {
    private Integer aucid;

    private String auctiontime;

    private String cycle;

    //private String carid;

    private String auctionfloor;

    private String auctionassessmentprice;
    private String addtime;
    private String auctionnumber;




    private List<car> carList;
    private List<BidderUser> bidderUserlist;

    public acutionListInfo() {
        super();
    }

    public acutionListInfo(Integer aucid, String auctiontime, String cycle, String auctionfloor, String auctionassessmentprice, String auctionnumber, String addtime, List<car> carList, List<BidderUser> bidderUserlist) {
        this.aucid = aucid;
        this.auctiontime = auctiontime;
        this.cycle = cycle;
        this.auctionfloor = auctionfloor;
        this.auctionassessmentprice = auctionassessmentprice;
        this.auctionnumber = auctionnumber;
        this.addtime = addtime;
        this.carList = carList;
        this.bidderUserlist = bidderUserlist;
    }

    public Integer getAucid() {
        return aucid;
    }

    public void setAucid(Integer aucid) {
        this.aucid = aucid;
    }

    public String getAuctiontime() {
        return auctiontime;
    }

    public void setAuctiontime(String auctiontime) {
        this.auctiontime = auctiontime;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getAuctionfloor() {
        return auctionfloor;
    }

    public void setAuctionfloor(String auctionfloor) {
        this.auctionfloor = auctionfloor;
    }

    public String getAuctionassessmentprice() {
        return auctionassessmentprice;
    }

    public void setAuctionassessmentprice(String auctionassessmentprice) {
        this.auctionassessmentprice = auctionassessmentprice;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getAuctionnumber() {
        return auctionnumber;
    }

    public void setAuctionnumber(String auctionnumber) {
        this.auctionnumber = auctionnumber;
    }

    public List<car> getCarList() {
        return carList;
    }

    public void setCarList(List<car> carList) {
        this.carList = carList;
    }

    public List<BidderUser> getBidderUserlist() {
        return bidderUserlist;
    }

    public void setBidderUserlist(List<BidderUser> bidderUserlist) {
        this.bidderUserlist = bidderUserlist;
    }

    /*public  class  BidderUser{
        private String paymentvoucher;

        private String paymentstate;

        private String applicationtime;

        private String auctionnumber;

        private String totalamount;

        private String auctionstate;
        private String ispaybond;
        private Integer userid;
        private String username;
        private String tel;
        private String certificates;
        private String address;


        public BidderUser(String paymentvoucher, String paymentstate, String applicationtime, String auctionnumber, String totalamount, String auctionstate, String ispaybond, Integer userid, String username, String tel, String certificates, String address) {
            this.paymentvoucher = paymentvoucher;
            this.paymentstate = paymentstate;
            this.applicationtime = applicationtime;
            this.auctionnumber = auctionnumber;
            this.totalamount = totalamount;
            this.auctionstate = auctionstate;
            this.ispaybond = ispaybond;
            this.userid = userid;
            this.username = username;
            this.tel = tel;
            this.certificates = certificates;
            this.address = address;
        }

        public String getPaymentvoucher() {
            return paymentvoucher;
        }

        public void setPaymentvoucher(String paymentvoucher) {
            this.paymentvoucher = paymentvoucher;
        }

        public String getPaymentstate() {
            return paymentstate;
        }

        public void setPaymentstate(String paymentstate) {
            this.paymentstate = paymentstate;
        }

        public String getApplicationtime() {
            return applicationtime;
        }

        public void setApplicationtime(String applicationtime) {
            this.applicationtime = applicationtime;
        }

        public String getAuctionnumber() {
            return auctionnumber;
        }

        public void setAuctionnumber(String auctionnumber) {
            this.auctionnumber = auctionnumber;
        }

        public String getOther() {
            return totalamount;
        }

        public void setOther(String other) {
            this.totalamount = other;
        }

        public String getAuctionstate() {
            return auctionstate;
        }

        public void setAuctionstate(String auctionstate) {
            this.auctionstate = auctionstate;
        }

        public String getIspaybond() {
            return ispaybond;
        }

        public void setIspaybond(String ispaybond) {
            this.ispaybond = ispaybond;
        }

        public Integer getUserid() {
            return userid;
        }

        public void setUserid(Integer userid) {
            this.userid = userid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getCertificates() {
            return certificates;
        }

        public void setCertificates(String certificates) {
            this.certificates = certificates;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }*/


}
