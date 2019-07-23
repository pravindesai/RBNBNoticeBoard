package com.pack.batman.rbnbnoticeboard;

public class Notice {
    private String id,user,usermail,dept,date,noticeTitle,noticetext;

    public Notice() {  }

    public Notice(String id, String user, String usermail, String dept, String date,String noticeTitle, String noticetext) {
        this.id = id;
        this.user = user;
        this.usermail = usermail;
        this.dept = dept;
        this.date = date;
        this.noticeTitle=noticeTitle;
        this.noticetext = noticetext;
    }

    public String getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getUsermail() {
        return usermail;
    }

    public String getDept() {
        return dept;
    }

    public String getDate() {
        return date;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public String getNoticetext() {
        return noticetext;
    }
}
