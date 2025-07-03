package com.example.Jewelry;

import com.example.Jewelry.Utility.Constant;
import com.example.Jewelry.entity.DeliveryAddress;
import com.example.Jewelry.entity.User;
import com.example.Jewelry.service.DeliveryAddressService;
import com.example.Jewelry.service.UserService;
import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableWebSecurity
public class JewelryApplication implements CommandLineRunner {
	private final Logger LOG = LoggerFactory.getLogger(JewelryApplication.class);

	@Autowired
	private UserService userService;
	@Autowired
	private DeliveryAddressService deliveryAddressService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {

		Dotenv dotenv = Dotenv.load();
		System.setProperty("GOOGLE_CLIENT_ID", dotenv.get("GOOGLE_CLIENT_ID"));
		System.setProperty("GOOGLE_CLIENT_SECRET", dotenv.get("GOOGLE_CLIENT_SECRET"));
		System.setProperty("FACEBOOK_CLIENT_ID", dotenv.get("FACEBOOK_CLIENT_ID"));
		System.setProperty("FACEBOOK_CLIENT_SECRET", dotenv.get("FACEBOOK_CLIENT_SECRET"));

		SpringApplication.run(JewelryApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		User admin = this.userService.getUserByEmailIdAndRoleAndStatus("demo.admin@demo.com",
				Constant.UserRole.ROLE_ADMIN.value(), Constant.ActiveStatus.ACTIVE.value());

		if (admin == null) {
			LOG.info("Admin not found in system, so adding default admin");

			User user = new User();
			user.setUsername("admin");
			user.setEmailId("demo.admin@demo.com");
			user.setPassword(passwordEncoder.encode("admin123"));
			user.setRole(Constant.UserRole.ROLE_ADMIN.value());
			user.setCreatedAt(LocalDateTime.now());
			user.setUpdateAt(LocalDateTime.now());
			user.setStatus(Constant.ActiveStatus.ACTIVE.value());
			this.userService.addUser(user);


			DeliveryAddress deliveryAddress = new DeliveryAddress();
			deliveryAddress.setUser(user);
			deliveryAddress.setBuildingAddress("25/12 KP11");
			deliveryAddress.setProvinceName("Đồng Nai");
			deliveryAddress.setDistrictName("Biên Hòa");
			deliveryAddress.setWardName("Tân Hòa");
			deliveryAddress.setContactNumber("0786355804");
			deliveryAddress.setWorkAddress(false);
			this.deliveryAddressService.addAddress(deliveryAddress);
		}

	}
}
