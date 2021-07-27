package com.example.testproject;

import java.io.Serializable;

public class Users implements Serializable {
    String name, phone, email, password, domain;
    String groupDiscussion, technicalInterview, caseStudy, hrInterview, rating;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Users() { }

    public Users(String name, String phone, String email, String password, String domain, String caseStudy, String groupDiscussion, String rating) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.caseStudy=caseStudy;
        this.domain=domain;
        this.groupDiscussion=groupDiscussion;
        this.hrInterview="";
        this.technicalInterview="";
        this.rating=rating;
    }


    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
    public String getGroupDiscussion() { return groupDiscussion; }
    public void setGroupDiscussion(String groupDiscussion) { this.groupDiscussion = groupDiscussion; }
    public String getTechnicalInterview() { return technicalInterview; }
    public void setTechnicalInterview(String technicalInterview) { this.technicalInterview = technicalInterview; }
    public String getCaseStudy() { return caseStudy; }
    public void setCaseStudy(String caseStudy) { this.caseStudy = caseStudy; }
    public String getHrInterview() { return hrInterview; }
    public void setHrInterview(String hrInterview) { this.hrInterview = hrInterview; }
    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
