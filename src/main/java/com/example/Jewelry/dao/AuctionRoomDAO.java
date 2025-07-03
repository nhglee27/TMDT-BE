package com.example.Jewelry.dao;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Jewelry.entity.AuctionProduct;
import com.example.Jewelry.entity.AuctionRoom;

public interface AuctionRoomDAO extends JpaRepository<AuctionRoom, UUID> {

    List<AuctionRoom> findByCurrentAuction(AuctionProduct currentAuction);

}
