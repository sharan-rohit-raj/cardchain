package com.example.cardchain;

import java.util.Objects;

public class EditProfileModel {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EditProfileModel that = (EditProfileModel) o;
        return Objects.equals(first_name, that.first_name) &&
                Objects.equals(last_name, that.last_name) &&
                Objects.equals(phone_num, that.phone_num);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first_name, last_name, phone_num);
    }

    private String first_name;
    private String last_name;
    private String phone_num;


    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }



    public EditProfileModel(String first_name, String last_name, String phone_num) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_num = phone_num;
    }


}
