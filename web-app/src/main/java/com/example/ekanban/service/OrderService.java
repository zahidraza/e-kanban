package com.example.ekanban.service;

import com.example.ekanban.dto.OrderDto;
import com.example.ekanban.entity.Inventory;
import com.example.ekanban.entity.Order;
import com.example.ekanban.entity.Product;
import com.example.ekanban.enums.BinState;
import com.example.ekanban.enums.OrderState;
import com.example.ekanban.exception.ScanException;
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

import java.util.ArrayList;
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
        Product product = productRepository.findOne(order2.getProduct().getId());

        //Check whether InwardScan is possible
        if (!isInwardScanPossible(product)) {
            throw new ScanException("Inward Scan cannot be done. Maximum bins in stock cannot be greater than " + product.getNoOfBins());
        }

        //Check whether card is valid
        int binNo = Integer.parseInt(order.getBins().trim());
        Inventory inv = inventoryRepository.findByProductAndBinNo(product,binNo);
        if (inv == null) {
            throw new ScanException("Invalid card. It does not exist.");
        }
        else if (inv.getBinState() == BinState.FREEZED) {
            throw new ScanException("Invalid card. This card is freezed.");
        }

        //find all inventory for this product and update the inevntory matching incoming bins
        List<Inventory> inventoryList = inventoryRepository.findByProduct(order2.getProduct());
        inventoryList.forEach(inventory -> {
            if (inventory.getBinNo() == binNo) {
                inventory.setBinState(BinState.STORE);
                product.setStkOnFloor(product.getStkOnFloor()+product.getBinQty());
            }
        });

        //create a list of bins in this order
        String[] bins2 = order2.getBins().split(",");
        List<Integer> binList2 = new ArrayList<>();
        for (String bin: bins2) {
            if (bin.trim().length() != 0){
                binList2.add(Integer.parseInt(bin.trim()));
            }
        }
        //Check whether all bins of this order are in store
        boolean isOrderComplete = true;
        for (Inventory inv2: inventoryList){
            if (binList2.contains(inv2.getBinNo()) && inv2.getBinState() != BinState.STORE){
                isOrderComplete = false;
            }
        }
        if (isOrderComplete) {
            order2.setCompletedAt(new Date());
            order2.setOrderState(OrderState.COMPLETED);
        }
        return mapper.map(order2,OrderDto.class);
    }

    @Transactional
    public void delete(Long id) {
        logger.debug("delete(): id = {}",id);
        orderRepository.delete(id);
    }

    private boolean isInwardScanPossible(Product product) {
        int binsInStock = inventoryRepository.findByProduct(product).stream()
                .filter(inv -> inv.getBinState() == BinState.STORE)
                .collect(Collectors.toList()).size();
        if (binsInStock >= product.getNoOfBins()) {
            return false;
        }
        return true;
    }
}
