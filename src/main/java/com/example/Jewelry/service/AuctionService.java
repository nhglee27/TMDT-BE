//package com.example.Jewelry.service;
//
//import com.example.Jewelry.dao.AuctionDAO;
//import com.example.Jewelry.dto.AuctionDTO;
//import com.example.Jewelry.dto.response.CommonApiResponse;
//import com.example.Jewelry.entity.Auction;
//import com.example.Jewelry.service.ServiceImpl.AuctionServiceImpl;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//
//@Service
//public class AuctionService implements AuctionServiceImpl {
//
//    private final AuctionDAO auctionRepository;
//    private final CloudinaryService cloudinaryService;
//
//    public AuctionService(AuctionDAO auctionRepository, CloudinaryService cloudinaryService) {
//        this.auctionRepository = auctionRepository;
//        this.cloudinaryService = cloudinaryService;
//    }
//
//    @Override
//    public CommonApiResponse createAuction(AuctionDTO dto, List<MultipartFile> images) {
//        try {
//            List<String> imageUrls = images != null ? cloudinaryService.uploadFiles(images) : List.of();
//
//            Auction auction = new Auction();
//            auction.setDescription(dto.getDescription());
//            auction.setBudget(dto.getBudget());
//            auction.setImages(imageUrls);
//            auction.setJewelryType(dto.getJewelryType());
//            auction.setMaterial(dto.getMaterial());
//            auction.setSize(dto.getSize());
//            auction.setSpecialRequest(dto.getSpecialRequest());
//            auction.setDeadline(dto.getDeadline());
//
//            auctionRepository.save(auction);
//            return CommonApiResponse.success("Tạo phiên đấu giá thành công");
//        } catch (Exception e) {
//            return CommonApiResponse.fail("Lỗi khi upload ảnh hoặc lưu dữ liệu: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public Page<Auction> getAllAuctions(int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        return auctionRepository.findAll(pageable);
//    }
//}
