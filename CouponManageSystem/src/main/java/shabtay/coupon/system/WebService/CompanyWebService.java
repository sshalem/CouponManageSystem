package shabtay.coupon.system.WebService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.Date;

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
import shabtay.coupon.system.exceptions.WrongLoginInputException;
import shabtay.coupon.system.facade.CompanyFacade;

//after I upload the file into the server I need to remove the cross origin *
//since the html and the server are in the same domain + port
@RestController
@CrossOrigin("*")
public class CompanyWebService {

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
			System.out.println(coupon.getEndDate());
			System.out.println(coupon.getStartDate());
			
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body("Coupon Created successfully");
		} catch (CouponAlreadyExistInCompanyDBException | InterruptedException e) {			
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}		
	}
	 
	@RequestMapping(value = "/companyws/deletecoupon/{id}", method = RequestMethod.DELETE)
	public void deleteCoupon(@PathVariable("id")long id) throws WrongLoginInputException, InterruptedException {
		CompanyFacade compf = getFacade();		
		Coupon coupon = compf.getCoupon(id);
		compf.removeCoupon(coupon);
	}  

	@RequestMapping(value = "/companyws/updatecoupon/{id}", method = RequestMethod.PUT)
	public void updateCoupon(@RequestBody Coupon coupon, @PathVariable("id") int id) throws WrongLoginInputException, InterruptedException {
		CompanyFacade compf = getFacade();
		Collection<Coupon> coupons = compf.getAllCoupon();
		for (Coupon c : coupons) {
			if (c.getId() == id) {
				if (coupon.getTitle() != null)
					c.setTitle(coupon.getTitle());
				if (coupon.getStartDate() != null)
					c.setStartDate(coupon.getStartDate());
				if (coupon.getEndDate() != null)
					c.setEndDate(coupon.getEndDate());
				if (coupon.getAmount() > 0)
					c.setAmount(coupon.getAmount());
				if (coupon.getType() != null)
					c.setType(coupon.getType());
				if (coupon.getMessage() != null)
					c.setMessage(coupon.getMessage());
				if (coupon.getPrice() > 0)
					c.setPrice(coupon.getPrice());
				if (coupon.getImage() != null)
					c.setImage(coupon.getImage());
				compf.updateCoupon(c);
			}
		}
	}

	@RequestMapping(value = "/companyws/getallcoupon", method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity getAllCoupon() {		
		CompanyFacade compf = getFacade();		
		try {			
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(compf.getAllCoupon());
		} catch (InterruptedException e) {			
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
	}

	@RequestMapping(value = "/companyws/getallcoupon/byid/{id}", method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
	public Coupon getCouponById(@PathVariable("id")  int id) throws WrongLoginInputException, InterruptedException {
		CompanyFacade compf = getFacade();
		System.out.println();
		System.out.println(compf.getCoupon(id).getEndDate());
		System.out.println(compf.getCoupon(id).getStartDate());
		return compf.getCoupon(id);
	}

	@RequestMapping(value = "/companyws/getallcoupon/byname/{name}", method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
	public Coupon getCouponByName(
			@PathVariable("name") String name) 
					throws WrongLoginInputException, InterruptedException 
	{
		CompanyFacade compf = getFacade();
		return compf.getCouponByName(name);
	}

	@RequestMapping(value = "/companyws/getallcoupon/bytype/{type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<Coupon> getCouponByType(
			@PathVariable("type") String type)
					throws WrongLoginInputException, InterruptedException 
	{
		CompanyFacade compf = getFacade();
		CouponType fromEnum = CouponType.valueOf(type.toUpperCase());
		System.out.println(compf.getCouponByType(fromEnum));
		return compf.getCouponByType(fromEnum); 
	}
 
	@RequestMapping(value = "/companyws/getallcoupon/byprice/{minPrice}/{maxPrice}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<Coupon> getAllCouponsByPrice(
			@PathVariable("minPrice")double minPrice, 
			@PathVariable("maxPrice")double maxPrice)
					throws WrongLoginInputException, InterruptedException
	{
		CompanyFacade compf = getFacade();
		System.out.println(compf.getAllCouponsByPrice(minPrice, maxPrice));
		return compf.getAllCouponsByPrice(minPrice, maxPrice);
	}
 
	@RequestMapping(value = "/companyws/getallcoupon/betweendates/{startingDate}/{endingDate}", method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<Coupon> getCouponBetweenDates(
			@PathVariable("startingDate") @DateTimeFormat(iso=ISO.DATE) Date startingDate,
			@PathVariable("endingDate") @DateTimeFormat(iso=ISO.DATE) Date endingDate) 
					throws WrongLoginInputException, InterruptedException 
	{
		CompanyFacade compf = getFacade();
		System.out.println(compf.getCouponBetweenDates(startingDate, endingDate));
		return compf.getCouponBetweenDates(startingDate, endingDate);
	}	

}