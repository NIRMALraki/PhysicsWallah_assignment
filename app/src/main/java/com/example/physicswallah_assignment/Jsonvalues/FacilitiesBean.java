package com.example.physicswallah_assignment.Jsonvalues;

public class FacilitiesBean implements Comparable<FacilitiesBean> {
    int facility_id;
    String Name;


    public int getFacility_id() {
        return facility_id;
    }

    public void setFacility_id(int facility_id) {
        this.facility_id = facility_id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }



    @Override
    public String toString() {
        return "FacilitiesBean{" +
                "facility_id=" + facility_id +
                ", Name='" + Name + '\'' +
                '}';
    }


    @Override
    public int compareTo(FacilitiesBean o) {
        return this.facility_id - o.facility_id;
    }
}
