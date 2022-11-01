package com.example.productservice.command;

import com.example.chapter9.core.command.ReserveProductCommand;
import com.example.chapter9.core.event.ProductReservedEvent;
import com.example.productservice.command.CreateProductCommand;
import com.example.productservice.core.event.ProductCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Aggregate
public class ProductAggregate {

    // State //
    @AggregateIdentifier
    private String productId;
    private String title;
    private BigDecimal price;
    private Integer quantity;

    // Command Handlers //
    @CommandHandler
    public ProductAggregate(CreateProductCommand command){
        // Business Logic (Validation) //
        if (command.getPrice().compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Price cannot be less than or equal to zero");
        }
        if (command.getTitle() == null || command.getTitle().isBlank()){
            throw new IllegalArgumentException("Title cannot be blank");
        }

        // Event //
        ProductCreatedEvent event = new ProductCreatedEvent();
        // ตัวที่ copy ทุกตัวให้เราเลย ไม่ต้องมาเขียนเหมือน Builder ทีละตัว
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);

    }

    @CommandHandler
    public void handler(ReserveProductCommand reserveProductCommand){
        if (quantity < reserveProductCommand.getQuantity()){
            throw new IllegalArgumentException("Insufficient umber of items in stock");
        }
        ProductReservedEvent productReservedEvent = ProductReservedEvent.builder()
                .orderId((reserveProductCommand.getOrderId()))
                .productId(reserveProductCommand.getProductId())
                .quantity(reserveProductCommand.getQuantity())
                .userId(reserveProductCommand.getUserId())
                .build();
        AggregateLifecycle.apply(productReservedEvent);
    }

    @EventSourcingHandler
    public void on(ProductCreatedEvent event){
        // Update State
        this.productId = event.getProductId();
        this.title = event.getTitle();
        this.price = event.getPrice();
        this.quantity = event.getQuantity();
        System.out.println("on product created : "+ this.productId);
    }

    @EventSourcingHandler
    public void on(ProductReservedEvent productReservedEvent){
        this.quantity -= productReservedEvent.getQuantity();
    }

}
