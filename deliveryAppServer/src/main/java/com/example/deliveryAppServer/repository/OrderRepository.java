    List<OrderEntity> findAllByRiderIdAndOrderStateIn(Long providerId, List<OrderState> orderStateList);package com.example.deliveryAppServer.repository;

import com.example.deliveryAppServer.model.enumerations.OrderState;
import com.example.deliveryAppServer.model.dao.order.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity,Long> {
    List<OrderEntity> findAllByOrderState(OrderState orderState);
    List<OrderEntity> findAllByCustomerId(Long customerId);
    List<OrderEntity> findAllByProviderIdAndOrderState(Long providerId, OrderState orderState);
    OrderEntity findByCustomerIdAndOrderStateNotIn(Long customerId, List<OrderState> orderStateList);
    Boolean existsByCustomerIdAndOrderStateNotIn(Long customerId, List<OrderState> orderStateList);
    List<OrderEntity> findAllByRiderIdAndOrderStateIn(Long providerId, List<OrderState> orderStateList);
    @Query(value ="SELECT orders "+
            "FROM OrderEntity orders " +
            "WHERE orders.orderState = 'PENDING' or orders.orderState = 'SEMIACCEPTED'")
    List<OrderEntity> findPendingAndSemiaccptedOrders();


    List<OrderEntity> findAllByRiderIdAndOrderStateIn(Long providerId, List<OrderState> orderStateList);

    @Query(value ="SELECT orders "+
            "FROM OrderEntity orders " +
            "WHERE orders.orderState = 'PENDING' or orders.orderState = 'SEMIACCEPTED'")
    List<OrderEntity> findPendingAndSemiaccptedOrders();
}
