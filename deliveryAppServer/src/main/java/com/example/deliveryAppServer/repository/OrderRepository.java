package com.example.deliveryAppServer.repository;

import com.example.deliveryAppServer.model.enumerations.OrderState;
import com.example.deliveryAppServer.model.dao.order.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * It allows the OrderEntity to persist in the database. It defines and builds all the standard CRUD DB queries for the OrderEntity.
 * It allows adding new custom queries.
 */
@Repository
public interface OrderRepository extends JpaRepository<OrderEntity,Long> {

    /**
     *
     * @param orderState
     * @return a list of OrderEntity where the field state is equal to the given param orderState
     */
    List<OrderEntity> findAllByOrderState(OrderState orderState);

    /**
     *
     * @param customerId
     * @return a list of OrderEntity where the field customerId is equal to the given param customerId
     */
    List<OrderEntity> findAllByCustomerId(Long customerId);

    /**
     *
     * @param providerId
     * @param orderState
     * @return a list of OrderEntity where the field state is equal to the given param orderState and the providerId field
     *          is equal to the given providerId
     */
    List<OrderEntity> findAllByProviderIdAndOrderState(Long providerId, OrderState orderState);

    /**
     *
     * @param customerId
     * @param orderStateList
     * @return the OrderEntity where the field state is not equal to one of the given param orderState and the customerId field
               is equal to the given customerId
     */
    OrderEntity findByCustomerIdAndOrderStateNotIn(Long customerId, List<OrderState> orderStateList);

    /**
     *
     * @param customerId
     * @param orderStateList
     * @return True if exists an order where the field state is not equal to one of the given param orderState and the customerId field
     *                is equal to the given customerId
     */
    Boolean existsByCustomerIdAndOrderStateNotIn(Long customerId, List<OrderState> orderStateList);

    /**
     *
     * @param riderId
     * @param orderStateList
     * @return a list of OrderEntity where the field state is equal to one of the given params orderState and the riderId field
     *          is equal to the given riderId
     */
    List<OrderEntity> findAllByRiderIdAndOrderStateIn(Long riderId, List<OrderState> orderStateList);

    /**
     *
     * @return a list of OrderEntity where the field state is equal to 'SEMIACCEPTED' or 'PENDING'
     */
    @Query(value ="SELECT orders "+
            "FROM OrderEntity orders " +
            "WHERE orders.orderState = 'PENDING' or orders.orderState = 'SEMIACCEPTED'")
    List<OrderEntity> findPendingAndSemiaccptedOrders();

}
