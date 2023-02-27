package com.example.ruijiwaimai.dto;

import com.example.ruijiwaimai.entity.OrderDetail;
import com.example.ruijiwaimai.entity.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
