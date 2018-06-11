package com.example.frank.jinding.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DELL on 2017/12/13.
 */

public class Order implements Serializable{
    private OrderUser orderUser;
    private List<Consignment> consignmentList;

    public OrderUser getOrderUser() {
        return orderUser;
    }

    public void setOrderUser(OrderUser orderUser) {
        this.orderUser = orderUser;
    }


    public Order(OrderUser orderUser, List<Consignment> consignmentList) {
        this.orderUser = orderUser;
        this.consignmentList = consignmentList;
    }

    public List<Consignment> getConsignmentList() {
        return consignmentList;
    }

    public void setConsignmentList(List<Consignment> consignmentList) {
        this.consignmentList = consignmentList;
    }

    public Order() {
    }
}
