package com.example.Jewelry.service.ServiceImpl;

import com.example.Jewelry.dao.AuctionProductDAO;
import com.example.Jewelry.dao.AuctionRoomDAO;
import com.example.Jewelry.dao.ProductDAO;
import com.example.Jewelry.dto.request.UpdateAuctionDetailDTO;
import com.example.Jewelry.entity.AuctionProduct;
import com.example.Jewelry.entity.AuctionRoom;
import com.example.Jewelry.entity.Product;
import com.example.Jewelry.exception.ResourceNotFoundException;
import com.example.Jewelry.service.AuctionProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuctionProductServiceImpl implements AuctionProductService {
    @Autowired
    private AuctionProductDAO auctionProductDAO;
    @Autowired
    private AuctionRoomDAO auctionRoomDAO;
    @Autowired
    private ProductDAO productDAO;

    @Override
    public AuctionProduct add(AuctionProduct auctionProduct) {
        return auctionProductDAO.save(auctionProduct);
    }

    @Override
    public AuctionProduct update(AuctionProduct auctionProduct) {
        return auctionProductDAO.save(auctionProduct);
    }

    @Override
    public List<AuctionProduct> updateAll(List<AuctionProduct> auctionProducts) {
        return auctionProductDAO.findAll();
    }

    @Override
    public AuctionProduct getById(int id) {
        Optional<AuctionProduct> optional = this.auctionProductDAO.findById(id);

        if (optional.isPresent()) {
            return optional.get();
        } else {
            return null;
        }
    }

    @Override
    public List<AuctionProduct> getAll() {
        return auctionProductDAO.findAll();
    }

    @Override
    public void deleteProduct(int auctionID) {
        this.auctionProductDAO.deleteById(auctionID);
    }

    @Override
    public List<Product> fetchAllMyProductAuction(String status, int userID) {
        return productDAO.findAllMyProductAuction(status, userID);
    }

    @Override
    public AuctionRoom addRoom(AuctionRoom auctionRoom) {
        return auctionRoomDAO.save(auctionRoom);
    }

    @Override
    public AuctionProduct getByProductId(int productID) {
        Product p = productDAO.findById(productID).orElse(null);
        if (p == null)
            return null;
        return auctionProductDAO.findByProduct(p).orElse(null);
    }

    @Override
    public AuctionRoom getRoomByID(String auctionRequestID) {
        UUID uuid = UUID.fromString(auctionRequestID);
        return auctionRoomDAO.findById(uuid).orElse(null);
    }

    @Override
    public List<AuctionRoom> getAuctionRoomsByAuctionID(int auctionID) {
        AuctionProduct product = getById(auctionID);
        if (product == null) return null;
        return auctionRoomDAO.findByCurrentAuction(product);
    }
    
    @Override
    @Transactional
    public AuctionRoom updateAuctionDetails(UpdateAuctionDetailDTO dto) {
        AuctionRoom room = getRoomByID(dto.getRoomID());
        if (room == null)
            return null;

        AuctionProduct auctionProduct = room.getCurrentAuction();
        Product product = auctionProduct.getProduct();

        if (dto.getName() != null) {
            product.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            product.setDescription(dto.getDescription());
        }
        if (dto.getMaterial() != null) {
            product.setProductMaterial(dto.getMaterial());
        }
        if (dto.getSize() != null) {
            product.setSize(dto.getSize());
        }
        if (dto.getOccasion() != null) {
            product.setOccasion(dto.getOccasion());
        }

        if (dto.getBudgetAuction() != null) {
            room.setProposingPrice(dto.getBudgetAuction());
        }

        productDAO.save(product);
        auctionRoomDAO.save(room);

        return room;
    }
}
