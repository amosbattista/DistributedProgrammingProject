package com.example.deliveryAppServer.repository;

import com.example.deliveryAppServer.model.enumerations.OrderState;
import com.example.deliveryAppServer.model.order.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity,Long> {
    List<OrderEntity> findAllByOrderState(OrderState orderState);
    List<OrderEntity> findAllByCustomerId(Long customerId);
    List<OrderEntity> findAllByProviderIdAndOrderState(Long providerId, OrderState orderState);

}
