package com.example.physicswallah_assignment.Jsonvalues;

public class ExclusionsBean {
    int facility_id1,facility_id2,options_id2,options_id1;

    public int getFacility_id1() {
        return facility_id1;
    }

    public void setFacility_id1(int facility_id1) {
        this.facility_id1 = facility_id1;
    }

    public int getFacility_id2() {
        return facility_id2;
    }

    public void setFacility_id2(int facility_id2) {
        this.facility_id2 = facility_id2;
    }

    public int getOptions_id2() {
        return options_id2;
    }

    public void setOptions_id2(int options_id2) {
        this.options_id2 = options_id2;
    }

    public int getOptions_id1() {
        return options_id1;
    }

    public void setOptions_id1(int options_id1) {
        this.options_id1 = options_id1;
    }

    @Override
    public String toString() {
        return "ExclusionsBean{" +
                "facility_id1=" + facility_id1 +
                ", facility_id2=" + facility_id2 +
                ", options_id2=" + options_id2 +
                ", options_id1=" + options_id1 +
                '}';
    }
}
