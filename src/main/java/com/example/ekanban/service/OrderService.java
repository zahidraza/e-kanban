package com.example.ekanban.service;

import com.example.ekanban.dto.OrderDto;
import com.example.ekanban.entity.Inventory;
import com.example.ekanban.entity.Order;
import com.example.ekanban.entity.Product;
import com.example.ekanban.enums.BinState;
import com.example.ekanban.enums.OrderState;
import com.example.ekanban.respository.InventoryRepository;
import com.example.ekanban.respository.OrderRepository;
import com.example.ekanban.respository.ProductRepository;
import com.example.ekanban.respository.UserRepository;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private InventoryRepository inventoryRepository;
    private final Mapper mapper;

    @Autowired
    public OrderService(OrderRepository orderRepository, Mapper mapper) {
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    public OrderDto findOne(Long id) {
        logger.debug("findOne(): id = {}",id);
        Order order = orderRepository.findOne(id);
        if (order == null) return null;
        return mapper.map(order, OrderDto.class);
    }

    public List<OrderDto> findAll(Long after) {
        logger.debug("findAll()");
        List<Order> orders;
        if (after == 0L){
            orders = orderRepository.findByOrderState(OrderState.ORDERED.getValue());
        }else {
            Date date = new Date(after);
            orders = orderRepository.findByOrderStateAndLastUpdatedGreaterThan(OrderState.ORDERED.getValue(),date);
        }
        return orders.stream()
                .map(order -> mapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
    }

    public Boolean exists(Long id) {
        logger.debug("exists(): id = ",id);
        return orderRepository.exists(id);
    }

    public Long count(){
        logger.debug("count()");
        return orderRepository.count();
    }

    @Transactional
    public OrderDto save(OrderDto orderDto) {
        logger.debug("save()");
        if (orderDto == null) return null;

        Order order = mapper.map(orderDto,Order.class);
        Product product = productRepository.findOne(orderDto.getProductId());
        order.setProduct(product);
        order.setOrderedBy(userRepository.findOne(orderDto.getOrderedBy()));
        order.setOrderState(OrderState.ORDERED);
        order.setOrderedAt(new Date());
        order = orderRepository.save(order);
        //Modify state of Bins:
        String[] bins = orderDto.getBins().split(",");
        for (String bin: bins){
            if (bin.trim().length()> 0){
                Inventory inv = inventoryRepository.findByProductAndBinNo(product,Integer.parseInt(bin));
                inv.setBinState(BinState.ORDERED);
            }
        }
        return mapper.map(order,OrderDto.class);
    }

    /**
     * Only one Update Case. When order is completed
     * @param orderDto
     * @return
     */
    @Transactional
    public OrderDto update(OrderDto orderDto) {
        logger.debug("update()");
        if (orderDto == null){
            logger.error("null OrderDto cannot be updated.");
        }
        Order order = mapper.map(orderDto, Order.class);
        Order order2 = orderRepository.findOne(order.getId());
        if (order2 == null){
            logger.error("No Order found matching orderId = {}", order.getId());
            return null;
        }
        //if (order.getOrderedAt() != null)   order2.setOrderedAt(order.getOrderedAt());
        //if (order.getCompletedAt() != null) order2.setCompletedAt(order.getCompletedAt());
        order2.setCompletedAt(new Date());
        order2.setOrderState(OrderState.COMPLETED);
        //if (order.getOrderState() != null) order2.setOrderState(order.getOrderState());
        return mapper.map(order2,OrderDto.class);
    }

    @Transactional
    public void delete(Long id) {
        logger.debug("delete(): id = {}",id);
        orderRepository.delete(id);
    }
}
