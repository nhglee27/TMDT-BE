package com.example.Jewelry.Utility;

public class Constant {
    public enum UserRole {
        ROLE_USER("User"), ROLE_ADMIN("Admin"), ROLE_CTV("CTV");

        private String role;

        private UserRole(String role) {
            this.role = role;
        }

        public String value() {
            return this.role;
        }
    }

    public enum ActiveStatus {
        ACTIVE("Active"), DEACTIVATED("Deactivated"), DELETED("Deleted"), OPENAUCTION("Open-Auction");

        private String status;

        private ActiveStatus(String status) {
            this.status = status;
        }

        public String value() {
            return this.status;
        }
    }

    public enum BookingStatus {
        CONFIRMED("Confirmed"), CANCELLED("Cancelled");

        private String status;

        private BookingStatus(String status) {
            this.status = status;
        }

        public String value() {
            return this.status;
        }
    }

    public enum ProductType {
        NORMAL("Paid"), AUCTION("Auction");

        private String type;

        private ProductType(String type) {
            this.type = type;
        }

        public String value() {
            return this.type;
        }
    }
    public enum CtvStatus {
        PENDING("Pending"), APPROVED("Approved"), REJECTED("Rejected");
        private String status;

        private CtvStatus(String status) { this.status =status;}
        public String value() {return this.status;}
    }

    public enum AuctionMessageType {
        MESSAGE("MESSAGE"), ACCEPT("ACCEPT"), REJECT("REJECT"), PENDING("PENDING"), UPDATE("UPDATE");
        private String status;

        private AuctionMessageType(String status) { this.status =status;}
        public String value() {return this.status;}
    }

}
