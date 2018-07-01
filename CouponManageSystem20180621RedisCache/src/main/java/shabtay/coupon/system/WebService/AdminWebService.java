package shabtay.coupon.system.WebService;


import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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


import shabtay.coupon.system.entities.Company;
import shabtay.coupon.system.entities.Customer;
import shabtay.coupon.system.exceptions.CompanyAlreadyExistException;
import shabtay.coupon.system.exceptions.CustomerAlreadyExistException;
import shabtay.coupon.system.exceptions.NameOrIdNotExistException;
import shabtay.coupon.system.facade.AdminFacade;

@RestController
@CrossOrigin("*")
public class AdminWebService {

	private static Logger logger = LogManager.getLogger(AdminWebService.class);
			
	private AdminFacade getFacade(HttpServletRequest request) {
		AdminFacade adminFacade = (AdminFacade) request.getSession().getAttribute("adminFacade");		
		return adminFacade;
	}
	
	
	@RequestMapping(value = "/adminws/company", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity createCompany(@RequestBody Company comp, HttpServletRequest req) 
	{
		AdminFacade adminFacade = getFacade(req);		
		try {
			adminFacade.createCompany(comp);
			logger.info("createCompany() executed ");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body("created successfully");
		} catch (CompanyAlreadyExistException | InterruptedException e) {
			logger.error("createCompany() failed ... " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/adminws/company", method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity getAllCompanies(HttpServletRequest req) {
		AdminFacade adminFacade = getFacade(req);
		try {
			Collection<Company> companies = adminFacade.getAllCompanies();
			logger.info("getAllCompanies() executed");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(companies);
		} catch (InterruptedException e) {
			logger.error("getAllCompanies() " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
	}	

	@RequestMapping(value = "/adminws/company/id/{id}", method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity getCompanyById(@PathVariable("id") int id, HttpServletRequest req) {
		AdminFacade adminFacade = getFacade(req);	
			try {
				Company company = adminFacade.getCompany(id);
				logger.info("getCompanyById() executed");
				return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(company);
			} catch (InterruptedException | NameOrIdNotExistException e) { 
				logger.error("getCompanyById() " + e.getMessage());
				return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
			} 
	} 

	@RequestMapping(value = "/adminws/company/name/{name}", method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity getCompanyByName(@PathVariable("name") String name, HttpServletRequest req) {
		AdminFacade adminFacade = getFacade(req);	
		try {
			Company company = adminFacade.getCompByName(name);
			logger.info("getCompanyByName() executed");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(company);
		} catch (InterruptedException | NameOrIdNotExistException e) { 
			logger.error("getCompanyByName() " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
	}
	

	@RequestMapping(value = "/adminws/company/update/{id}", method = RequestMethod.PUT , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity updateCompany(@RequestBody Company comp, @PathVariable("id") int id, HttpServletRequest req)
	{
		AdminFacade adminFacade = getFacade(req);
		try {
			adminFacade.updateCompany(comp);
			logger.info("updateCompany() invoked ");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body("Updated successfully");
		} catch (InterruptedException | NameOrIdNotExistException e) {
			logger.error("updateCompany() was not updated since " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
	} 

	@RequestMapping(value = "/adminws/company/delete/{id}", method = RequestMethod.DELETE , produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity deleteCompany(@PathVariable("id") int id, HttpServletRequest req)
	{
		AdminFacade adminFacade = getFacade(req);
		try {
			adminFacade.removeCompany(adminFacade.getCompany(id));
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
	public @ResponseBody ResponseEntity createCustomer(@RequestBody Customer customer , HttpServletRequest req){		
		AdminFacade adminFacade = getFacade(req);
		try {
			adminFacade.createCustomer(customer);
			logger.info("createCustomer() invoked ");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body("customer created");
		} catch (CustomerAlreadyExistException | InterruptedException e) {
			logger.error("createCustomer() was not performed since " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
	}

	@RequestMapping(value = "/adminws/customer", method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity getAllCustomers(HttpServletRequest req) {
		AdminFacade adminFacade = getFacade(req);
		try {
			Collection<Customer> customer = adminFacade.getAllCustomer();
			logger.info("getAllCustomers() invoked ");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(customer); 
		} catch (InterruptedException e) {		
			logger.error("getAllCustomers() was not performed since " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
	} 

	@RequestMapping(value = "/adminws/customer/id/{id}", method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity getCustomerById(@PathVariable("id") int id, HttpServletRequest req) {
		AdminFacade adminFacade = getFacade(req);
		try {
			Customer customer = adminFacade.getCustomer(id);
			logger.info("getCustomerById() invoked ");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(customer);
		} catch (InterruptedException | NameOrIdNotExistException e) {
			logger.error("getCustomerById() was not performed since " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
	}

	@RequestMapping(value = "/adminws/customer/name/{name}", method = RequestMethod.GET , produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity getCustomerByName(@PathVariable("name") String name, HttpServletRequest req) {
		AdminFacade adminFacade = getFacade(req);
		try {
			Customer customer = adminFacade.getCustomerByName(name);
			logger.info("getCustomerByName() invoked ");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(customer);
		} catch (InterruptedException | NameOrIdNotExistException e) {
			logger.error("getCustomerByName() was not performed since " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}	
	}

	@RequestMapping(value = "/adminws/customer/update/{id}", method = RequestMethod.PUT)
	public @ResponseBody ResponseEntity updateCustomer(@RequestBody Customer customer, @PathVariable("id") int id, HttpServletRequest req) {
		AdminFacade adminFacade = getFacade(req);		
		try {
			adminFacade.updateCustomer(customer);
			logger.info("updateCustomer() invoked ");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body("Updated successfully");			
		} catch (InterruptedException e) {	
			logger.error("updateCustomer() was not performed since " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}	 
	}

	@RequestMapping(value = "/adminws/customer/delete/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResponseEntity deleteCustomer(@PathVariable("id") int id , HttpServletRequest req) {
		AdminFacade adminFacade = getFacade(req);
		try {
			adminFacade.removeCustomer(adminFacade.getCustomer(id));
			logger.info("deleteCustomer() invoked ");
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_MARKDOWN).body(" Customer deleted successfully");
		} catch (InterruptedException | NameOrIdNotExistException e) {	
			logger.error("deleteCustomer() was not performed since " + e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).contentType(MediaType.TEXT_PLAIN).body(e.getMessage());
		}
	}

}
