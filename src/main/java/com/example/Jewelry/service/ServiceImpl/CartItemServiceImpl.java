package com.example.Jewelry.service.ServiceImpl;

import com.example.Jewelry.dao.CartItemDAO;
import com.example.Jewelry.dao.ProductDAO;
import com.example.Jewelry.dao.UserDAO;
import com.example.Jewelry.dto.response.CommonApiResponse;
import com.example.Jewelry.entity.CartItem;
import com.example.Jewelry.entity.Product;
import com.example.Jewelry.entity.User;
import com.example.Jewelry.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private CartItemDAO cartItemDAO;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private UserDAO userDAO;

    @Override
    public CartItem addToCart(int userId, int productId, int quantity) {
        Optional<User> userOpt = userDAO.findById(userId);
        Optional<Product> productOpt = productDAO.findById(productId);

        if (userOpt.isEmpty() || productOpt.isEmpty()) {
            throw new RuntimeException("Người dùng hoặc sản phẩm không tồn tại");
        }

        User user = userOpt.get();
        Product product = productOpt.get();

        Optional<CartItem> existing = cartItemDAO.findByUserAndProduct(user, product);
        if (existing.isPresent()) {
            CartItem item = existing.get();
            if (item.isDeleted()) {
                item.setDeleted(false);
                item.setQuantity(quantity);
            } else {
                item.setQuantity(item.getQuantity() + quantity);
            }
            return cartItemDAO.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setUser(user);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setDeleted(false);
            return cartItemDAO.save(newItem);
        }
    }


    @Override
    public List<CartItem> getCartItems(int userId) {
        User user = userDAO.findById(userId).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        return cartItemDAO.findByUserAndDeletedFalse(user);
    }

    @Override
    public CommonApiResponse removeFromCart(int userId, int cartItemId) {
        CommonApiResponse response = new CommonApiResponse();
        Optional<User> userOpt = userDAO.findById(userId);
        Optional<CartItem> cartItemOpt = cartItemDAO.findById(cartItemId);

        if (userOpt.isPresent() && cartItemOpt.isPresent()) {
            CartItem cartItem = cartItemOpt.get();

            if (cartItem.getUser().getId() != userId) {
                response.setSuccess(false);
                response.setResponseMessage("Sản phẩm không thuộc người dùng");
                return response;
            }

            cartItem.setDeleted(true);
            cartItemDAO.save(cartItem);

            response.setSuccess(true);
            response.setResponseMessage("Đã xóa khỏi giỏ hàng");
        } else {
            response.setSuccess(false);
            response.setResponseMessage("Không tìm thấy người dùng hoặc sản phẩm trong giỏ hàng");
        }

        return response;
    }


    @Override
    public CommonApiResponse clearCart(int userId) {
        List<CartItem> cartList = cartItemDAO.findByUserAndDeletedFalse(userDAO.findById(userId).orElseThrow());
        for (CartItem cart : cartList) {
            cart.setDeleted(true);
        }
        cartItemDAO.saveAll(cartList);

        CommonApiResponse response = new CommonApiResponse();
        response.setSuccess(true);
        response.setResponseMessage("Đã xóa toàn bộ giỏ hàng");
        return response;
    }

    @Override
    public CommonApiResponse updateQuantity(int userId, int cartItemId, String action) {
        CommonApiResponse response = new CommonApiResponse();
        Optional<CartItem> cartItemOpt = cartItemDAO.findById(cartItemId);

        if (cartItemOpt.isPresent()) {
            CartItem cartItem = cartItemOpt.get();

            if (cartItem.getUser().getId() != userId) {
                response.setSuccess(false);
                response.setResponseMessage("Sản phẩm không thuộc người dùng");
                return response;
            }

            int currentQty = cartItem.getQuantity();
            if (action.equalsIgnoreCase("increment")) {
                cartItem.setQuantity(currentQty + 1);
            } else if (action.equalsIgnoreCase("decrement") && currentQty > 1) {
                cartItem.setQuantity(currentQty - 1);
            }

            cartItemDAO.save(cartItem);
            response.setSuccess(true);
            response.setResponseMessage("Đã cập nhật số lượng");

        } else {
            response.setSuccess(false);
            response.setResponseMessage("Không tìm thấy sản phẩm trong giỏ hàng");
        }

        return response;
    }

}
