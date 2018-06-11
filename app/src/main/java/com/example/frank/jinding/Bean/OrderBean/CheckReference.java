package com.example.frank.jinding.Bean.OrderBean;

import java.io.Serializable;

/**
 * Created by DELL on 2018/1/13.
 */

public class CheckReference implements Serializable {
    String reference_id;
    String reference_code;
    String reference_name;


    public CheckReference() {
        super();
    }


    public CheckReference(String reference_id, String reference_name, String reference_code) {
        this.reference_id = reference_id;
        this.reference_code = reference_code;
        this.reference_name = reference_name;
    }

    public String getReference_id() {
        return reference_id;
    }

    public void setReference_id(String reference_id) {
        this.reference_id = reference_id;
    }

    public String getReference_code() {
        return reference_code;
    }

    public void setReference_code(String reference_code) {
        this.reference_code = reference_code;
    }

    public String getReference_name() {
        return reference_name;
    }

    public void setReference_name(String reference_name) {
        this.reference_name = reference_name;
    }

    @Override
    public String toString() {
        return reference_name;
    }
}
