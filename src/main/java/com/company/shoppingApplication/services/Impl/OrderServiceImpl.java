package com.company.shoppingApplication.services.Impl;

import com.company.shoppingApplication.Enum.OrderStatus;
import com.company.shoppingApplication.Exceptions.ResourceNotFoundException;
import com.company.shoppingApplication.dto.OrderDto;
import com.company.shoppingApplication.entities.Cart;
import com.company.shoppingApplication.entities.Order;
import com.company.shoppingApplication.entities.OrderItem;
import com.company.shoppingApplication.entities.Product;
import com.company.shoppingApplication.repository.OrderRepository;
import com.company.shoppingApplication.repository.ProductRepository;
import com.company.shoppingApplication.services.CartService;
import com.company.shoppingApplication.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;

    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order, cart);
         order.setOrderItems(new HashSet<>(orderItemList));
         order.setTotalAmount(calculateTotalPrice(orderItemList));
         Order savedOrder = orderRepository.save(order);

         cartService.clearCart((cart.getId()));


        return savedOrder;
    }
    private Order createOrder(Cart cart){
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart){
        return cart.getItems().stream()
                .map(cartItem->{
                    Product product  = cartItem.getProduct();
                    product.setInventory((product.getInventory()  - cartItem.getQuantity()));
                  productRepository.save(product);
                  return new OrderItem(
                          order,
                          product,
                          cartItem.getQuantity(),
                          cartItem.getUnitPrice()
                  );
                }).toList();
    }

    private BigDecimal calculateTotalPrice(List<OrderItem> orderItemList){
         return orderItemList.stream()
                 .map(item-> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                 .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::convertToDto)
                .orElseThrow(()->new ResourceNotFoundException("order not found"));
        }


        @Override
        public List<OrderDto> getUserOrders(Long userId){
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(this::convertToDto).toList();
        }

        private OrderDto convertToDto(Order order){
        return modelMapper.map(order, OrderDto.class);
        }
    }

