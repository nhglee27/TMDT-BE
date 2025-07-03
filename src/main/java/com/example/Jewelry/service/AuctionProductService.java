package com.example.Jewelry.service;

import com.example.Jewelry.dto.request.UpdateAuctionDetailDTO;
import com.example.Jewelry.entity.AuctionProduct;
import com.example.Jewelry.entity.AuctionRoom;
import com.example.Jewelry.entity.Product;

import java.util.Collection;
import java.util.List;

public interface AuctionProductService {
    AuctionProduct add(AuctionProduct auctionProduct);

    AuctionProduct update(AuctionProduct auctionProduct);

    List<AuctionProduct> updateAll(List<AuctionProduct> auctionProducts);

    AuctionProduct getById(int id);

    List<AuctionProduct> getAll();

    void deleteProduct(int auctionID);

    List<Product> fetchAllMyProductAuction(String value, int userID);

    /** Product ID */
    AuctionProduct getByProductId(int productID);
    /** Create registeration room */
    AuctionRoom addRoom(AuctionRoom auctionRoom);
    /** Get ROOM by UUID string */
    AuctionRoom getRoomByID(String auctionRequestID);

    List<AuctionRoom> getAuctionRoomsByAuctionID(int auctionID);
    AuctionRoom updateAuctionDetails(UpdateAuctionDetailDTO dto);
}
