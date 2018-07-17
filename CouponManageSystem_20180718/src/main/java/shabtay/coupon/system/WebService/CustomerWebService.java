package shabtay.coupon.system.WebService;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import shabtay.coupon.system.common.ClientType;
import shabtay.coupon.system.common.CouponType;
import shabtay.coupon.system.entities.Coupon;
import shabtay.coupon.system.entry.CouponSystem;
import shabtay.coupon.system.exceptions.AmountOfCouponsZeroException;
import shabtay.coupon.system.exceptions.CouponAlreadyPurchsedByCustomerException;
import shabtay.coupon.system.exceptions.CouponExpiredException;
import shabtay.coupon.system.exceptions.CouponNotExistException;
import shabtay.coupon.system.exceptions.WrongLoginInputException;
import shabtay.coupon.system.facade.CompanyFacade;
import shabtay.coupon.system.facade.CustomerFacade;

//after I upload the file into the server I need to remove the cross origin *
//since the html and the server are in the same domain + port
@RestController
@CrossOrigin("*")
public class CustomerWebService {

	private static Logger logger = LogManager.getLogger(CustomerWebService.class);
	
	private CustomerFacade getFacade(HttpServletRequest request) {		
		CustomerFacade customerFacade = (CustomerFacade) request.getSession().getAttribute("customerFacade");
		return customerFacade;
	}
	
	@RequestMapping(value = "/customerws/purchasecoupon", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity purchaseCoupon(@RequestBody Coupon coupon, HttpServletRequest req)	{
		CustomerFacade customerFacade = getFacade(req);
		try {
			customerFacade.purchaseCoupon(coupon);
			logger.info("purchaseCoupon() executed");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body("Coupon purchased");
		} catch (CouponAlreadyPurchsedByCustomerException | AmountOfCouponsZeroException | CouponExpiredException
				| InterruptedException | CouponNotExistException e) {	
			logger.error("purchaseCoupon() " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/customerws/showallcoupons", method = RequestMethod.GET ,  produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity showAllCoupons(HttpServletRequest req) {
		CustomerFacade customerFacade = getFacade(req);		
		try {
			Collection<Coupon> coupons = customerFacade.getAllCoupon();
			logger.info("showAllCoupons() executed");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(coupons);
		} catch (InterruptedException e) {
			logger.error("showAllCoupons() " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/customerws/getpurchsedcoupons", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity getAllPurchasedCoupons(HttpServletRequest req) {
		CustomerFacade customerFacade = getFacade(req);		
		try {
			Collection<Coupon> coupons = customerFacade.getAllPurchasedCoupons();
			logger.info("getAllPurchasedCoupons() executed");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(coupons);
		} catch (InterruptedException e) {
			logger.error("getAllPurchasedCoupons() " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
	}	
	
	@RequestMapping(value = "/customerws/getpurchsedcoupons/bytype/{type}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity getAllPurchasedCouponsByType(@PathVariable("type")String type, HttpServletRequest req) {
		CustomerFacade customerFacade = getFacade(req);
		CouponType couponType = CouponType.valueOf(type.toUpperCase());		
		try {
			List<Coupon> coupons = customerFacade.getAllPurchasedCouponsByType(couponType);
			logger.info("getAllPurchasedCouponsByType() executed");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(coupons);
		} catch (InterruptedException e) {
			logger.error("getAllPurchasedCouponsByType() " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		} 
	}
	
	@RequestMapping(value = "/customerws/getpurchsedcoupons/byprice/{minimumPrice}/{maximumPrice}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity getAllPurchasedCouponsByPrice(HttpServletRequest req,
			@PathVariable("minimumPrice")double minimumPrice, @PathVariable double maximumPrice) {
		CustomerFacade customerFacade = getFacade(req);		
		try {
			List<Coupon> coupons = customerFacade.getAllPurchasedCouponsByPrice(minimumPrice, maximumPrice);
			logger.info("getAllPurchasedCouponsByPrice() executed");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(coupons);
		} catch (InterruptedException e) {
			logger.error("getAllPurchasedCouponsByPrice() " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/customerws/getpurchsedcoupons/bytitle/{title}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody ResponseEntity getCouponbyTitle(@PathVariable("title")String title, HttpServletRequest req) {
		CustomerFacade customerFacade = getFacade(req);		
		try {
			Coupon coupon = customerFacade.getCouponFromCustomerDB(title);
			logger.info("getCouponbyTitle() executed");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(coupon);
		} catch (InterruptedException | CouponNotExistException e) {
			logger.error("getCouponbyTitle() " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
	}	
	
}
