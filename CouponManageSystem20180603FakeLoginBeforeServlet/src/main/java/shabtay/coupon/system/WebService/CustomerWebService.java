package shabtay.coupon.system.WebService;

import java.util.Collection;
import java.util.List;

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
import shabtay.coupon.system.facade.CustomerFacade;

//after I upload the file into the server I need to remove the cross origin *
//since the html and the server are in the same domain + port
@RestController
@CrossOrigin("*")
public class CustomerWebService {

	private static Logger logger = LogManager.getLogger(AdminWebService.class);
	
	@Autowired
	private CouponSystem couponSystem;
	
	private CustomerFacade getFacade() {
		CustomerFacade cf;
		try {
			cf = (CustomerFacade) couponSystem.login("odel", "1234", ClientType.CUSTOMER);
			return cf;
		} catch (WrongLoginInputException | InterruptedException e) {			
			return null;
		}
		
	}
	
	@RequestMapping(value = "/customerws/purchasecoupon", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity purchaseCoupon(@RequestBody Coupon coupon)	{
		CustomerFacade cf = getFacade();
		try {
			cf.purchaseCoupon(coupon);
			logger.info("purchaseCoupon() executed");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body("Coupon purchased");
		} catch (CouponAlreadyPurchsedByCustomerException | AmountOfCouponsZeroException | CouponExpiredException
				| InterruptedException | CouponNotExistException e) {	
			logger.error("purchaseCoupon() " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/customerws/showallcoupons", method = RequestMethod.GET ,  produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity showAllCoupons() {
		CustomerFacade cf = getFacade();		
		try {
			logger.info("showAllCoupons() executed");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(cf.getAllCoupon());
		} catch (InterruptedException e) {
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/customerws/getpurchsedcoupons", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity getAllPurchasedCoupons() {
		CustomerFacade cf = getFacade();		
		try {
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(cf.getAllPurchasedCoupons());
		} catch (InterruptedException e) {
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
	}	
	
	@RequestMapping(value = "/customerws/getpurchsedcoupons/bytype/{type}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity getAllPurchasedCouponsByType(@PathVariable("type")String type) {
		CustomerFacade cf = getFacade();
		CouponType couponType = CouponType.valueOf(type.toUpperCase());		
		try {
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(cf.getAllPurchasedCouponsByType(couponType));
		} catch (InterruptedException e) {
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		} 
	}
	
	@RequestMapping(value = "/customerws/getpurchsedcoupons/byprice/{minimumPrice}/{maximumPrice}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity getAllPurchasedCouponsByPrice(
			@PathVariable("minimumPrice")double minimumPrice, @PathVariable double maximumPrice) {
		CustomerFacade cf = getFacade();		
		try {
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(cf.getAllPurchasedCouponsByPrice(minimumPrice, maximumPrice));
		} catch (InterruptedException e) {
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/customerws/getpurchsedcoupons/bytitle/{title}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody ResponseEntity getCouponbyTitle(@PathVariable("title")String title) {
		CustomerFacade cf = getFacade();		
		try {
			logger.info("getCouponbyTitle() executed");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(cf.getCouponFromCustomerDB(title));
		} catch (InterruptedException | CouponNotExistException e) {
			logger.error("getCouponbyTitle() " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
	}	
	
}
