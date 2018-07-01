package shabtay.coupon.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class CouponManageSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CouponManageSystemApplication.class, args);
	}
}
