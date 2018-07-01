package shabtay.coupon.system.WebService;

import java.util.Collection;

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
import shabtay.coupon.system.entities.Company;
import shabtay.coupon.system.entities.Customer;
import shabtay.coupon.system.entry.CouponSystem;
import shabtay.coupon.system.exceptions.CompanyAlreadyExistException;
import shabtay.coupon.system.exceptions.CustomerAlreadyExistException;
import shabtay.coupon.system.exceptions.NameOrIdNotExistException;
import shabtay.coupon.system.exceptions.WrongLoginInputException;
import shabtay.coupon.system.facade.AdminFacade;

//after I upload the file into the server I need to remove the cross origin *
//since the html and the server are in the same domain + port
@RestController
@CrossOrigin("*")
public class AdminWebService {

	private static Logger logger = LogManager.getLogger(AdminWebService.class);
			
	@Autowired
	private CouponSystem couponSystem;

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
	//// @CrossOrigin(origins = "*")
	// @RequestMapping(value = "/getcoupon", method = RequestMethod.GET)
	// public Coupon getCoupon(HttpServletRequest request) {
	// CompanyFacade companyFacade = getFacade(request);
	// return companyFacade.getCoupon();
	// }

	private AdminFacade getFacade() {

		// the line below is only for testing , what really sholud be is
		// AdminFacade af = (AdminFacade) couponSystem.login(username, password, type);
		 
		try {
			AdminFacade af = (AdminFacade) couponSystem.login("admin", "1234", ClientType.ADMIN);
			return af;
		} catch (WrongLoginInputException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/adminws/company", method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity getAllCompanies() {
		AdminFacade af = getFacade();
		try {
			logger.info("getAllCompanies() executed");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(af.getAllCompanies());
		} catch (InterruptedException e) {
			logger.error("getAllCompanies() " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
	}	

	@RequestMapping(value = "/adminws/company/id/{id}", method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity getCompanyById(@PathVariable("id") int id) {
		AdminFacade af = getFacade();	
			try {
				logger.info("getCompanyById() executed");
				return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(af.getCompany(id));
			} catch (InterruptedException | NameOrIdNotExistException e) { 
				logger.error("getCompanyById() " + e.getMessage());
				return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
			} 
	} 

	@RequestMapping(value = "/adminws/company/name/{name}", method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity getCompanyByName(@PathVariable("name") String name) {
		AdminFacade af = getFacade();	
		try {
			logger.info("getCompanyByName() executed");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(af.getCompByName(name));
		} catch (InterruptedException | NameOrIdNotExistException e) { 
			logger.error("getCompanyByName() " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
	}

	@RequestMapping(value = "/adminws/company", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity createCompany(@RequestBody Company comp) 
	{
		AdminFacade af = getFacade();
		try {
			af.createCompany(comp);
			logger.info("createCompany() executed ");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body("created successfully");
		} catch (CompanyAlreadyExistException | InterruptedException e) {
			logger.error("createCompany() was not updated since " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
	}

	@RequestMapping(value = "/adminws/company/update/{id}", method = RequestMethod.PUT , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity updateCompany(@RequestBody Company comp, @PathVariable("id") int id)
	{
		AdminFacade af = getFacade();
		try {
			af.updateCompany(comp);
			logger.info("updateCompany() invoked ");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body("Updated successfully");
		} catch (InterruptedException | NameOrIdNotExistException e) {
			logger.error("updateCompany() was not updated since " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
	} 

	@RequestMapping(value = "/adminws/company/delete/{id}", method = RequestMethod.DELETE , produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity deleteCompany(@PathVariable("id") int id)
	{
		AdminFacade af = getFacade();
		try {
			af.removeCompany(af.getCompany(id));
			logger.info("deleteCompany() invoked ");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body("deleted successfully");
		} catch (InterruptedException | NameOrIdNotExistException e) {
			logger.error("deleteCompany() was not performed since " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
	}

	// --------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------

	/*
	 * Admin Facade - Customer methods
	 */

	@RequestMapping(value = "/adminws/customer", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity createCustomer(@RequestBody Customer customer){		
		AdminFacade af = getFacade();
		try {
			af.createCustomer(customer);
			logger.info("createCustomer() invoked ");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body("customer created");
		} catch (CustomerAlreadyExistException | InterruptedException e) {
			logger.error("createCustomer() was not performed since " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
	}

	@RequestMapping(value = "/adminws/customer", method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity getAllCustomers() {
		AdminFacade af = getFacade();
		try {
			logger.info("getAllCustomers() invoked ");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(af.getAllCustomer()); 
		} catch (InterruptedException e) {		
			logger.error("getAllCustomers() was not performed since " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
	} 

	@RequestMapping(value = "/adminws/customer/id/{id}", method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity getCustomerById(@PathVariable("id") int id) {
		AdminFacade af = getFacade();
		try {
			logger.info("getCustomerById() invoked ");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(af.getCustomer(id));
		} catch (InterruptedException | NameOrIdNotExistException e) {
			logger.error("getCustomerById() was not performed since " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
	}

	@RequestMapping(value = "/adminws/customer/name/{name}", method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity getCustomerByName(@PathVariable("name") String name) {
		AdminFacade af = getFacade();
		try {
			logger.info("getCustomerByName() invoked ");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(af.getCustomerByName(name));
		} catch (InterruptedException | NameOrIdNotExistException e) {
			logger.error("getCustomerByName() was not performed since " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}	
	}

	@RequestMapping(value = "/adminws/customer/update/{id}", method = RequestMethod.PUT)
	public @ResponseBody ResponseEntity updateCustomer(@RequestBody Customer customer, @PathVariable("id") int id) {
		AdminFacade af = getFacade();		
		try {
			af.updateCustomer(customer);
			logger.info("updateCustomer() invoked ");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body("Updated successfully");			
		} catch (InterruptedException e) {	
			logger.error("updateCustomer() was not performed since " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}	 
	}

	@RequestMapping(value = "/adminws/customer/delete/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResponseEntity deleteCustomer(@PathVariable("id") int id) {
		AdminFacade af = getFacade();
		try {
			af.removeCustomer(af.getCustomer(id));
			logger.info("deleteCustomer() invoked ");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(" Customer deleted successfully");
		} catch (InterruptedException | NameOrIdNotExistException e) {	
			logger.error("deleteCustomer() was not performed since " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
	}

}
