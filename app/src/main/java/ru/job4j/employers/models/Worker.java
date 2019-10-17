package ru.job4j.employers.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Worker {
    @SerializedName("f_name")
    private String firstName;
    @SerializedName("l_name")
    private String lastName;
    private String birthday;
    @SerializedName("avatr_url")
    private String avatarUrl;
    @SerializedName("specialty")
    private List<Speciality> specialty = null;

    public Worker(String firstName, String lastName, String birthday, String avatarUrl, List<Speciality> specialty) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.avatarUrl = avatarUrl;
        this.specialty = specialty;
    }

    public String getFName() {
        return firstName;
    }

    public void setFName(String fName) {
        this.firstName = fName;
    }

    public String getLName() {
        return lastName;
    }

    public void setLName(String lName) {
        this.lastName = lName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAvatrUrl() {
        return avatarUrl;
    }

    public void setAvatrUrl(String avatrUrl) {
        this.avatarUrl = avatrUrl;
    }

    public List<Speciality> getSpecialty() {
        return specialty;
    }

    public void setSpecialty(List<Speciality> specialty) {
        this.specialty = specialty;
    }
}
