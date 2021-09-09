package com.example.auction.Model;

public class aucuserid {
    private Integer id;

    private Integer aucid;

    private String userstr;

    private String state;

    private String other;
    //公众号是否推送字段
    private String issend;

    public aucuserid(Integer id, Integer aucid, String userstr, String state, String other) {
        this.id = id;
        this.aucid = aucid;
        this.userstr = userstr;
        this.state = state;
        this.other = other;
    }

    public aucuserid() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAucid() {
        return aucid;
    }

    public void setAucid(Integer aucid) {
        this.aucid = aucid;
    }

    public String getUserstr() {
        return userstr;
    }

    public void setUserstr(String userstr) {
        this.userstr = userstr == null ? null : userstr.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other == null ? null : other.trim();
    }

    public String getIssend() {
        return issend;
    }

    public void setIssend(String issend) {
        this.issend = issend;
    }

    @Override
    public String toString() {
        return "aucuserid{" +
                "id=" + id +
                ", aucid=" + aucid +
                ", userstr='" + userstr + '\'' +
                ", state='" + state + '\'' +
                ", other='" + other + '\'' +
                ", issend='" + issend + '\'' +
                '}';
    }
}