package com.example.frank.jinding.Bean.OrderBean;

/**
 * Created by DELL on 2017/12/25.
 */

public class CheckType {
    private String check_type_id;
    private String check_type_code;
    private String check_type_name;

    public CheckType() {
    }

    public CheckType(String check_type_id, String check_type_code, String check_type_name) {
        this.check_type_id = check_type_id;
        this.check_type_code = check_type_code;
        this.check_type_name = check_type_name;
    }

    public String getCheck_type_id() {
        return check_type_id;
    }

    public String getCheck_type_name() {
        return check_type_name;
    }

    public String getCheck_type_code() {
        return check_type_code;
    }

    @Override
    public String toString() {
        return check_type_name;
    }
}
