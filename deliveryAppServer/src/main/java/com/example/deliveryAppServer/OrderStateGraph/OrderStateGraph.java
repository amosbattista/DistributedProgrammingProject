package com.example.deliveryAppServer.OrderStateGraph;

import com.example.deliveryAppServer.exception.IllegalOrderType;
import com.example.deliveryAppServer.model.enumerations.OrderState;
import com.example.deliveryAppServer.model.enumerations.OrderType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * This is a utility class used to check if the status change of an order is correct.
 *
 * It makes use of three graphs, each for a specific type of order:
 *      - takeAwayGraph: is used for an Order whose delivery is a takeaway.
 *                       for this case we can have two different order status sequences:
 *                       1) PENDING->ACCEPTED->COMPLETED
 *                       2) PENDING->REFUSED
 *
 *      - deliveryNoRiderGraph: is used for an Order whose delivery is a home delivery, made by a provider's rider.
 *                              for this case we can have two different order status sequences:
 *                              1) PENDING->ACCEPTED->SHIPPED->COMPLETED
*                               2) PENDING->REFUSED
 *
 *      - deliveryRiderGraph is used for an Order whose delivery is a home delivery, made by a rider app
 *                              for this case we can have three different order status sequences:
 *                              1) PENDING->SEMI_ACCEPTED->ACCEPTED->SHIPPED->COMPLETED
 *                              2) PENDING->REFUSED
 *                              3) PENDING->SEMI_ACCEPTED->REFUSED
 *
 *
 */
public class OrderStateGraph {
    private HashMap<OrderState, List<OrderState>> takeAwayGraph;
    private HashMap<OrderState, List<OrderState>> deliveryNoRiderGraph;
    private HashMap<OrderState, List<OrderState>> deliveryRiderGraph;
    private static OrderStateGraph orderStateGraphInstance = null;

    private OrderStateGraph(){
        initTakeAwayGraph();
        initNoRiderGraph();
        initRiderGraph();
    }

    public static OrderStateGraph getOrderStateGraphInstance(){
        if(orderStateGraphInstance == null)
            orderStateGraphInstance = new OrderStateGraph();
        return orderStateGraphInstance;
    }

    private void initTakeAwayGraph(){
        takeAwayGraph = new HashMap<>();
        //Creazione sequenza di stati per l'asporto
        //Stato inziale Pending (In valatuzione)
        //Stato successivo possibile Accepted e Refused (dal provider)
        LinkedList<OrderState> nextStatesPending = new LinkedList<OrderState>();
        nextStatesPending.add(OrderState.ACCEPTED);
        nextStatesPending.add(OrderState.REFUSED);
        takeAwayGraph.put(OrderState.PENDING, nextStatesPending);
        //Stato Accepted
        //Stato successivo possibile Completed
        LinkedList<OrderState> nextStatesAccepted = new LinkedList<OrderState>();
        nextStatesAccepted.add(OrderState.COMPLETED);
        takeAwayGraph.put(OrderState.ACCEPTED, nextStatesAccepted);
        //Stato refused
        //Finale non ci sono stati successivi
        LinkedList<OrderState> nextStatesRefused = new LinkedList<OrderState>();
        takeAwayGraph.put(OrderState.REFUSED, nextStatesRefused);
        //Stato Completed
        //Finale non ci sono stati successivi
        LinkedList<OrderState> nextStatesCompleted = new LinkedList<OrderState>();
        takeAwayGraph.put(OrderState.COMPLETED, nextStatesCompleted);
    }



    private void initNoRiderGraph(){
        deliveryNoRiderGraph = new HashMap<>();
        //Creazione sequenza di stati per consegna senza rider
        //Stato inziale Pending (In valatuzione)
        //Stato successivo possibile Accepted e Refused (dal provider)
        LinkedList<OrderState> nextStatesPending = new LinkedList<OrderState>();
        nextStatesPending.add(OrderState.ACCEPTED);
        nextStatesPending.add(OrderState.REFUSED);
        deliveryNoRiderGraph.put(OrderState.PENDING, nextStatesPending);
        //Stato Accepted
        //Stato successivo possibile Shipped (Partito dal provider)
        LinkedList<OrderState> nextStatesAccepted = new LinkedList<OrderState>();
        nextStatesAccepted.add(OrderState.SHIPPED);
        deliveryNoRiderGraph.put(OrderState.ACCEPTED, nextStatesAccepted);
        //Stato Shipped
        //Stato successivo possibile Completed
        LinkedList<OrderState> nextStatesShipped = new LinkedList<OrderState>();
        nextStatesShipped.add(OrderState.COMPLETED);
        deliveryNoRiderGraph.put(OrderState.SHIPPED, nextStatesShipped);
        //Stato refused
        //Finale non ci sono stati successivi
        LinkedList<OrderState> nextStatesRefused = new LinkedList<OrderState>();
        deliveryNoRiderGraph.put(OrderState.REFUSED, nextStatesRefused);
        //Stato Completed
        //Finale non ci sono stati successivi
        LinkedList<OrderState> nextStatesCompleted = new LinkedList<OrderState>();
        deliveryNoRiderGraph.put(OrderState.COMPLETED, nextStatesCompleted);

    }

    private void initRiderGraph(){
        deliveryRiderGraph = new HashMap<>();
        //Creazione sequenza di stati per consegna con rider
        //Stato inziale Pending (In valatuzione)
        //Stato successivo possibile SemiAccepted e Refused (dal provider)
        LinkedList<OrderState> nextStatesPending = new LinkedList<OrderState>();
        nextStatesPending.add(OrderState.SEMI_ACCEPTED);
        nextStatesPending.add(OrderState.REFUSED);
        deliveryRiderGraph.put(OrderState.PENDING, nextStatesPending);
        //Stato Semi-Accepted
        //Stato successivo possibile Accepted e Refused (dai rider)
        LinkedList<OrderState> nextStatesSemiAccepted = new LinkedList<OrderState>();
        nextStatesSemiAccepted.add(OrderState.ACCEPTED);
        nextStatesSemiAccepted.add(OrderState.REFUSED);
        deliveryRiderGraph.put(OrderState.SEMI_ACCEPTED, nextStatesSemiAccepted);
        //Stato Accepted
        //Stato successivo possibile Shipped (Rider partito dal provider)
        LinkedList<OrderState> nextStatesAccepted = new LinkedList<OrderState>();
        nextStatesAccepted.add(OrderState.SHIPPED);
        deliveryRiderGraph.put(OrderState.ACCEPTED, nextStatesAccepted);
        //Stato Shipped
        //Stato successivo possibile Completed
        LinkedList<OrderState> nextStatesShipped = new LinkedList<OrderState>();
        nextStatesShipped.add(OrderState.COMPLETED);
        deliveryRiderGraph.put(OrderState.SHIPPED, nextStatesShipped);
        //Stato refused
        //Finale non ci sono stati successivi
        LinkedList<OrderState> nextStatesRefused = new LinkedList<OrderState>();
        deliveryRiderGraph.put(OrderState.REFUSED, nextStatesRefused);
        //Stato Completed
        //Finale non ci sono stati successivi
        LinkedList<OrderState> nextStatesCompleted = new LinkedList<OrderState>();
        deliveryRiderGraph.put(OrderState.COMPLETED, nextStatesCompleted);
    }

    public boolean checkNextState(OrderState prevState, OrderState nextState, OrderType orderType){
        HashMap<OrderState, List<OrderState>> stateGraph;
        switch (orderType){
            case TAKE_AWAY: {stateGraph = takeAwayGraph; break;}
            case DELIVERY_NORIDER: {stateGraph = deliveryNoRiderGraph; break;}
            case DELIVERY_RIDERS: {stateGraph = deliveryRiderGraph; break;}
            default: throw new IllegalOrderType();
        }
        //Controlla se negli stati possibili del prevstate è presente NextState
        return stateGraph.get(prevState).contains(nextState);
    }

    @Override
    public String toString() {
        return "OrderStateGraph{" +
                "\ntakeAwayGraph=" + takeAwayGraph +
                "\ndeliveryNoRiderGraph=" + deliveryNoRiderGraph +
                "\ndeliveryRiderGraph=" + deliveryRiderGraph +
                '}';
    }

}
