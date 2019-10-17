package ru.job4j.employers.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Speciality implements Comparable {

    @SerializedName("specialty_id")
    private Integer specialtyId;
    private String name;

    public Speciality(Integer specialtyId, String name) {
        this.specialtyId = specialtyId;
        this.name = name;
    }

    public Integer getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(Integer specialtyId) {
        this.specialtyId = specialtyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Speciality that = (Speciality) o;
        return Objects.equals(specialtyId, that.specialtyId) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(specialtyId, name);
    }


    @Override
    public int compareTo(Object o) {
        Speciality speciality = (Speciality)o;

        return getSpecialtyId()-speciality.getSpecialtyId();
    }
}
