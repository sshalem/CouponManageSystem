package shabtay.coupon.system.WebService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import shabtay.coupon.system.common.CouponType;
import shabtay.coupon.system.entities.Coupon;
import shabtay.coupon.system.exceptions.CouponAlreadyExistInCompanyDBException;
import shabtay.coupon.system.exceptions.NameOrIdNotExistException;
import shabtay.coupon.system.facade.CompanyFacade;

//after I upload the file into the server I need to remove the cross origin *
//since the html and the server are in the same domain + port
@RestController
@CrossOrigin("*")
public class CompanyWebService {

	private static Logger logger = LogManager.getLogger(CompanyWebService.class);

	private CompanyFacade getFacade(HttpServletRequest request) {		
		CompanyFacade companyFacade = (CompanyFacade) request.getSession().getAttribute("companyFacade");
		return companyFacade;
	}	

	@RequestMapping(value = "/companyws/createcoupon", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity createCoupon(@RequestBody Coupon coupon , HttpServletRequest req ) {
		CompanyFacade companyFacade = getFacade(req);
		try {
			companyFacade.createCoupon(coupon);
			logger.info("createCoupon() executed");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN)
					.body("Coupon Created successfully");
		} catch (CouponAlreadyExistInCompanyDBException | InterruptedException e) {
			logger.error("createCoupon() " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN)
					.body(e.getMessage());
		}
	}

	@RequestMapping(value = "/companyws/deletecoupon/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResponseEntity deleteCoupon(@PathVariable("id") long id, HttpServletRequest req) {
		CompanyFacade companyFacade = getFacade(req);
		Coupon coupon;
		try {
			coupon = companyFacade.getCoupon(id);
			companyFacade.removeCoupon(coupon);
			logger.info("removeCoupon() executed");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN)
					.body("Coupon removed successfully");
		} catch (InterruptedException | NameOrIdNotExistException e) {
			logger.error("removeCoupon() " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN)
					.body(e.getMessage());
		}

	}

	@RequestMapping(value = "/companyws/updatecoupon/{id}", method = RequestMethod.PUT)
	public @ResponseBody ResponseEntity updateCoupon(@RequestBody Coupon coupon, @PathVariable("id") int id, HttpServletRequest req) {
		CompanyFacade companyFacade = getFacade(req);
		try {
			companyFacade.updateCoupon(coupon);
			logger.info("updateCoupon() executed");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN)
					.body("Coupon updated successfully");
		} catch (InterruptedException e) {
			logger.error("updateCoupon() " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN)
					.body(e.getMessage());
		}
	}

	@RequestMapping(value = "/companyws/getallcoupon", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity getAllCoupon(HttpServletRequest req) {
		CompanyFacade companyFacade = getFacade(req);
		try {
			Collection<Coupon> coupons = companyFacade.getAllCoupon();
			logger.info("getallcoupon() executed");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(coupons);
		} catch (InterruptedException e) {
			logger.error("getallcoupon() " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN)
					.body(e.getMessage());
		}
	}

	@RequestMapping(value = "/companyws/getallcoupon/byid/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity getCouponById(@PathVariable("id") int id, HttpServletRequest req) {
		CompanyFacade companyFacade = getFacade(req);
		try {
			Coupon coupon = companyFacade.getCoupon(id);
			logger.info("getCouponById() executed");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(coupon);
		} catch (InterruptedException | NameOrIdNotExistException e) {
			logger.error("getCouponById() " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN)
					.body(e.getMessage());
		}
	}

	@RequestMapping(value = "/companyws/getallcoupon/byname/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity getCouponByName(@PathVariable("name") String name, HttpServletRequest req) {
		CompanyFacade companyFacade = getFacade(req);
		try {
			Coupon coupon = companyFacade.getCouponByName(name);
			logger.info("getCouponByName() executed");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(coupon);
		} catch (InterruptedException | NameOrIdNotExistException e) {
			logger.error("getCouponByName() " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN)
					.body(e.getMessage());
		}
	}

	@RequestMapping(value = "/companyws/getallcoupon/bytype/{type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity getCouponByType(@PathVariable("type") String type, HttpServletRequest req) {
		CompanyFacade companyFacade = getFacade(req);
		CouponType fromEnum = CouponType.valueOf(type.toUpperCase());
		try {
			List<Coupon> coupons = companyFacade.getCouponByType(fromEnum);
			logger.info("getCouponByType() executed");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(coupons);
		} catch (InterruptedException e) {
			logger.error("getCouponByType() " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN)
					.body(e.getMessage());
		}
	}

	@RequestMapping(value = "/companyws/getallcoupon/byprice/{minPrice}/{maxPrice}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity getAllCouponsByPrice(@PathVariable("minPrice") double minPrice,
			@PathVariable("maxPrice") double maxPrice, HttpServletRequest req) {
		CompanyFacade companyFacade = getFacade(req);
		try {
			List<Coupon> coupons = companyFacade.getAllCouponsByPrice(minPrice, maxPrice);
			logger.info("getAllCouponsByPrice() executed");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(coupons);
		} catch (InterruptedException e) {
			logger.error("getAllCouponsByPrice() " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN)
					.body(e.getMessage());
		}
	}

	@RequestMapping(value = "/companyws/getallcoupon/betweendates/{startingDate}/{endingDate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity getCouponBetweenDates(HttpServletRequest req,
			@PathVariable("startingDate") @DateTimeFormat(iso = ISO.DATE) Date startingDate,
			@PathVariable("endingDate") @DateTimeFormat(iso = ISO.DATE) Date endingDate) {
		CompanyFacade companyFacade = getFacade(req);
		try {
			List<Coupon> coupons = companyFacade.getCouponBetweenDates(startingDate, endingDate);
			logger.info("getCouponBetweenDates() executed");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(coupons);
		} catch (InterruptedException e) {
			logger.error("getCouponBetweenDates() " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN)
					.body(e.getMessage());
		}
	}

}
