package shabtay.coupon.system.WebService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
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
import shabtay.coupon.system.exceptions.CouponAlreadyExistInCompanyDBException;
import shabtay.coupon.system.exceptions.NameOrIdNotExistException;
import shabtay.coupon.system.exceptions.WrongLoginInputException;
import shabtay.coupon.system.facade.CompanyFacade;

//after I upload the file into the server I need to remove the cross origin *
//since the html and the server are in the same domain + port
@RestController
@CrossOrigin("*")
public class CompanyWebService {

	
	private static Logger logger = LogManager.getLogger(AdminWebService.class);
	
	// private CompanyFacade getFacade(HttpServletRequest request) {
	// if (request.getSession().getAttribute("coompanyFacade") == null) {
	// // user bypass login
	// // decide what to do ....
	// return null;
	// }
	// CompanyFacade companyFacade = (CompanyFacade)
	// request.getSession().getAttribute("coompanyFacade");
	// return companyFacade;
	//
	// // CompanyFacade result = (new CompanyFacade()).login("TEVA", "12345",
	// "Company");
	// // return result;
	// }
	//
	// // @CrossOrigin(origins = "*")
	// @RequestMapping(value = "/getcoupon", method = RequestMethod.GET)
	// public Coupon getCoupon(HttpServletRequest request) {
	// CompanyFacade companyFacade = getFacade(request);
	// return companyFacade.getCoupon();
	// }

	@Autowired
	private CouponSystem couponSystem;

	private CompanyFacade getFacade(){
		// the line below is only for testing , what really sholud be is
		// AdminFacade af = (AdminFacade) couponSystem.login(username, password, type);
		CompanyFacade compf;
		try {
			compf = (CompanyFacade) couponSystem.login("asml", "1234", ClientType.COMPANY);
			return compf;
		} catch (WrongLoginInputException | InterruptedException e) {			
			return null;
		}
		
	}

	@RequestMapping(value = "/companyws/createcoupon", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity createCoupon(@RequestBody Coupon coupon){
		CompanyFacade compf = getFacade();		
		try {
			compf.createCoupon(coupon);	
			logger.info("createCoupon() executed");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body("Coupon Created successfully");
		} catch (CouponAlreadyExistInCompanyDBException | InterruptedException e) {		
			logger.error("createCoupon() " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}		
	}
	 
	@RequestMapping(value = "/companyws/deletecoupon/{id}", method = RequestMethod.DELETE)
	public  @ResponseBody ResponseEntity deleteCoupon(@PathVariable("id")long id) {
		CompanyFacade compf = getFacade();		
		Coupon coupon;
		try {
			coupon = compf.getCoupon(id);
			compf.removeCoupon(coupon);
			logger.info("removeCoupon() executed");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body("Coupon removed successfully");
		} catch (InterruptedException | NameOrIdNotExistException e) {	
			logger.error("removeCoupon() " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
		
	}  

	@RequestMapping(value = "/companyws/updatecoupon/{id}", method = RequestMethod.PUT)
	public @ResponseBody ResponseEntity updateCoupon(@RequestBody Coupon coupon, @PathVariable("id") int id) {
		CompanyFacade compf = getFacade();
		try {
			compf.updateCoupon(coupon);
			logger.info("updateCoupon() executed");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body("Coupon updated successfully");
		} catch (InterruptedException e) {	
			logger.error("updateCoupon() " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}		
	}

	@RequestMapping(value = "/companyws/getallcoupon", method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity getAllCoupon() {		
		CompanyFacade compf = getFacade();		
		try {			
			logger.info("getallcoupon() executed");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(compf.getAllCoupon());
		} catch (InterruptedException e) {		
			logger.error("getallcoupon() " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
	}

	@RequestMapping(value = "/companyws/getallcoupon/byid/{id}", method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity getCouponById(@PathVariable("id") int id) {
		CompanyFacade compf = getFacade();
		try {
			logger.info("getCouponById() executed");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(compf.getCoupon(id));
		} catch (InterruptedException | NameOrIdNotExistException e) {
			logger.error("getCouponById() " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
	}

	@RequestMapping(value = "/companyws/getallcoupon/byname/{name}", method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity getCouponByName(@PathVariable("name") String name) {
		CompanyFacade compf = getFacade();
		try {
			logger.info("getCouponByName() executed");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(compf.getCouponByName(name));
		} catch (InterruptedException | NameOrIdNotExistException e) {
			logger.error("getCouponByName() " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
	}

	@RequestMapping(value = "/companyws/getallcoupon/bytype/{type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity  getCouponByType(@PathVariable("type") String type) { 
		CompanyFacade compf = getFacade();
		CouponType fromEnum = CouponType.valueOf(type.toUpperCase());
		try {
			logger.info("getCouponByType() executed");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(compf.getCouponByType(fromEnum));
		} catch (InterruptedException e) {
			logger.error("getCouponByType() " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		} 
	}
 
	@RequestMapping(value = "/companyws/getallcoupon/byprice/{minPrice}/{maxPrice}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity getAllCouponsByPrice(@PathVariable("minPrice")double minPrice,@PathVariable("maxPrice")double maxPrice) {
		CompanyFacade compf = getFacade();
		try {
			logger.info("getAllCouponsByPrice() executed");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(compf.getAllCouponsByPrice(minPrice, maxPrice));
		} catch (InterruptedException e) {
			logger.error("getAllCouponsByPrice() " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
	}
 
	@RequestMapping(value = "/companyws/getallcoupon/betweendates/{startingDate}/{endingDate}", method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity  getCouponBetweenDates(
			@PathVariable("startingDate") @DateTimeFormat(iso=ISO.DATE) Date startingDate,
			@PathVariable("endingDate") @DateTimeFormat(iso=ISO.DATE) Date endingDate)					
	{
		CompanyFacade compf = getFacade();
		try {
			logger.info("getCouponBetweenDates() executed");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(compf.getCouponBetweenDates(startingDate, endingDate));
		} catch (InterruptedException e) {
			logger.error("getCouponBetweenDates() " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
	}	

}
