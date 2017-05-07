package com.example.ekanban.restcontroller;

import com.example.ekanban.assembler.OrderAssembler;
import com.example.ekanban.dto.FieldError;
import com.example.ekanban.dto.OrderDto;
import com.example.ekanban.service.OrderService;
import com.example.ekanban.service.ProductService;
import com.example.ekanban.service.UserService;
import com.example.ekanban.util.ApiUrls;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Created by razamd on 5/2/2017.
 */
@RestController
@RequestMapping(ApiUrls.ROOT_URL_ORDERS)
public class OrderRestController {

    private final Logger logger = LoggerFactory.getLogger(OrderRestController.class);

    private final OrderService orderService;
    @Autowired private ProductService productService;
    @Autowired private UserService userService;
    private final OrderAssembler orderAssembler;

    @Autowired
    public OrderRestController(OrderService orderService, OrderAssembler orderAssembler) {
        this.orderService = orderService;
        this.orderAssembler = orderAssembler;
    }

    @GetMapping
    public ResponseEntity<?> loadAllOrders(@RequestParam(value = "after", defaultValue = "0") Long after){
        logger.debug("loadAllOrders()");
        List<OrderDto> orders = orderService.findAll(after);
        Map<String, Object> resp = new HashedMap();
        resp.put("orderSync", System.currentTimeMillis());
        resp.put("orders",orderAssembler.toResources(orders));
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @GetMapping(ApiUrls.URL_ORDERS_ORDER)
    public ResponseEntity<?> loadOrder(@PathVariable("orderId") Long orderId){
        logger.debug("loadOrder(): orderId = {}",orderId);
        OrderDto order = orderService.findOne(orderId);
        if (order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orderAssembler.toResource(order), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderDto orderDto) {
        logger.debug("createOrder(): {}", orderDto);
        List<FieldError> errors = new ArrayList<>();
        if (orderDto.getProductId() == null || orderDto.getProductId() == 0L){
            errors.add(new FieldError("productId",null,"Product Id cannot be null or empty"));
        }else if (! productService.exists(orderDto.getProductId())){
            errors.add(new FieldError("productId",null,"Product with this id not found."));
        }
        if (orderDto.getBins() == null){
            errors.add(new FieldError("bins",null,"Bins for which Order will be created cannot be null"));
        }
        if (orderDto.getOrderedBy() == null || orderDto.getOrderedBy() == 0L){
            errors.add(new FieldError("orderedBy",null,"User creating Order cannot be null or empty"));
        }else if (! userService.exists(orderDto.getOrderedBy())){
            errors.add(new FieldError("orderedBy",null,"User with this id not found"));
        }
        if (errors.size() > 0){
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        orderDto = orderService.save(orderDto);
        Link selfLink = linkTo(OrderRestController.class).slash(orderDto.getId()).withSelfRel();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(selfLink.getHref()));
        return new ResponseEntity<>(orderAssembler.toResource(orderDto),headers,HttpStatus.CREATED);
    }

    @PutMapping(ApiUrls.URL_ORDERS_ORDER)
    public ResponseEntity<?> updateOrder(@PathVariable("orderId") long orderId,@Valid @RequestBody OrderDto order) {
        logger.debug("updateOrder(): orderId = {} \n {}",orderId,order);
        if (!orderService.exists(orderId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        order.setId(orderId);
        order = orderService.update(order);
        return new ResponseEntity<>(orderAssembler.toResource(order), HttpStatus.OK);
    }

    @DeleteMapping(ApiUrls.URL_ORDERS_ORDER)
    public ResponseEntity<Void> deleteOrder(@PathVariable("orderId") long orderId) {
        logger.debug("deleteOrder(): id = {}",orderId);
        if (!orderService.exists(orderId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        orderService.delete(orderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
