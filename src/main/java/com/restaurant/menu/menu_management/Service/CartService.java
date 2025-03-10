package com.restaurant.menu.menu_management.Service;

import com.restaurant.menu.menu_management.Domain.*;
import com.restaurant.menu.menu_management.Domain.DTO.CartDTO;
import com.restaurant.menu.menu_management.Domain.DTO.OrderDTO;
import com.restaurant.menu.menu_management.Domain.DTO.OrderDetailDTO;
import com.restaurant.menu.menu_management.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final DishRepository dishRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final AuthService authService;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
            DishRepository dishRepository, UserRepository userRepository, OrderRepository orderRepository,
            OrderDetailRepository orderDetailRepository, AuthService authService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.dishRepository = dishRepository;
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.authService = authService;
    }

    // Lấy giỏ hàng của người dùng (hoặc tạo mới nếu chưa có)
    public Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    // Lấy giỏ hàng dưới dạng DTO
    public CartDTO.CartResponse getCart(Long userId) {

        User user = this.authService.getAuthenticatedUser();

        Cart cart = getOrCreateCart(user);

        CartDTO.CartResponse cartDTO = new CartDTO.CartResponse();
        cartDTO.setId(cart.getId());
        cartDTO.setUser(new CartDTO.UserDTO(user.getId(), user.getName(), user.getEmail(), user.getRole().getName()));

        List<CartDTO.CartItemDTO> itemDTOs = cart.getCartItems().stream().map(item -> {
            CartDTO.CartItemDTO dto = new CartDTO.CartItemDTO();
            dto.setId(item.getId());
            dto.setDish(new CartDTO.DishDTO(item.getDish().getId(), item.getDish().getName(), item.getDish().getPrice(),
                    item.getDish().getImageUrl()));
            dto.setQuantity(item.getQuantity());
            dto.setUnitPrice(item.getUnitPrice());
            dto.setSubtotal(item.getSubtotal());
            dto.setNote(item.getNote());
            return dto;
        }).toList();

        cartDTO.setCartItems(itemDTOs);
        return cartDTO;
    }

    // Thêm món vào giỏ hàng
    @Transactional
    public CartDTO.CartResponse addToCart(Long userId, Long dishId, int quantity, String note) {
        User user = this.authService.getAuthenticatedUser();

        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new EntityNotFoundException("Dish not found"));

        Cart cart = getOrCreateCart(user);

        // Kiểm tra xem món đã có trong giỏ hàng chưa
        Optional<CartItem> existingItemOpt = cart.getCartItems().stream()
                .filter(item -> item.getDish().getId().equals(dishId))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem.calculateSubtotal();
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setDish(dish);
            newItem.setQuantity(quantity);
            newItem.setUnitPrice(dish.getPrice());
            newItem.setNote(note);
            newItem.calculateSubtotal();
            cart.getCartItems().add(cartItemRepository.save(newItem));
        }

        cartRepository.save(cart);
        return getCart(userId);
    }

    @Transactional
    public void removeFromCart(Long userId, Long cartItemId) {
        User user = this.authService.getAuthenticatedUser();

        Cart cart = getOrCreateCart(user);

        boolean removed = cart.getCartItems().removeIf(item -> item.getId().equals(cartItemId));

        if (!removed) {
            throw new EntityNotFoundException("Cart item not found in user's cart");
        }

        if (cart.getCartItems().isEmpty()) {
            this.cartRepository.delete(cart);
        } else {
            this.cartRepository.save(cart);
        }
    }

    @Transactional
    public OrderDTO checkout(Long cartId, Long userId, String orderType, String location, Integer tableNumber,
            String note, String phoneNumber) {

        User user = this.authService.getAuthenticatedUser();

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        if (!Objects.equals(cart.getUser().getId(), user.getId())) {
            throw new IllegalArgumentException("The cart does not belong to you");
        }

        if (cart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        if (!"DINE_IN".equals(orderType) && !"TAKEAWAY".equals(orderType)) {
            throw new IllegalArgumentException("Invalid order type");
        }

        double totalPrice = cart.getCartItems().stream()
                .mapToDouble(cartItem -> {
                    if (cartItem.getUnitPrice() == null || cartItem.getQuantity() == null) {
                        throw new IllegalStateException("Cart item unit price hoặc quantity không được để null");
                    }
                    return cartItem.getSubtotal();
                })
                .sum();

        Order order = new Order();
        order.setUser(user);
        order.setStatus("PENDING");
        order.setOrderType(orderType);
        order.setTotalAmount(totalPrice);
        order.setNote(note);
        order.setPhoneNumber(phoneNumber);

        if ("TAKEAWAY".equals(orderType)) {
            if (location == null || location.isBlank()) {
                throw new IllegalArgumentException("Location is required for TAKEAWAY orders");
            }
            order.setLocation(location);
        } else if ("DINE_IN".equals(orderType)) {
            if (tableNumber == null) {
                throw new IllegalArgumentException("Table number is required for DINE_IN orders");
            }
            order.setTableNumber(tableNumber);
        }

        final Order savedOrder = this.orderRepository.save(order);

        List<OrderDetailDTO.Item> orderDetailItems = cart.getCartItems().stream().map(cartItem -> {
            OrderDetail orderDetail = new OrderDetail();

            orderDetail.setOrder(savedOrder);
            orderDetail.setDish(cartItem.getDish());
            orderDetail.setQuantity(cartItem.getQuantity());

            orderDetail.setUnitPrice(cartItem.getUnitPrice());
            orderDetail.setPriceAtOrderTime(cartItem.getUnitPrice());
            orderDetail.calculateSubtotal();

            this.orderDetailRepository.save(orderDetail);

            OrderDetailDTO.Item itemDTO = new OrderDetailDTO.Item();
            itemDTO.setOrderDetail_id(orderDetail.getId());

            OrderDetailDTO.Dish dishDTO = new OrderDetailDTO.Dish();
            dishDTO.setId(cartItem.getDish().getId());
            dishDTO.setName(cartItem.getDish().getName());
            dishDTO.setPrice(cartItem.getDish().getPrice());
            dishDTO.setImageUrl(cartItem.getDish().getImageUrl());

            itemDTO.setDish(dishDTO);
            itemDTO.setQuantity(cartItem.getQuantity());
            itemDTO.setUnitPrice(cartItem.getUnitPrice());
            itemDTO.setSubtotal(cartItem.getSubtotal());
            itemDTO.setNote(cartItem.getNote());

            return itemDTO;
        }).toList();

        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        orderDetailDTO.setOrderId(savedOrder.getId());
        orderDetailDTO.setItems(orderDetailItems);
        orderDetailDTO.setSummary(new OrderDetailDTO.Summary(orderDetailItems.size(), totalPrice));

        this.cartItemRepository.deleteAll(cart.getCartItems());
        this.cartRepository.delete(cart);

        return new OrderDTO(savedOrder, orderDetailDTO);
    }

}
