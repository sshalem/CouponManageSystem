package shabtay.coupon.system.facade;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import shabtay.coupon.system.DBDAO.CompanyDBDAO;
import shabtay.coupon.system.DBDAO.CouponDBDAO;
import shabtay.coupon.system.DBDAO.CustomerDBDAO;
import shabtay.coupon.system.common.ConstantList;
import shabtay.coupon.system.entities.Company;
import shabtay.coupon.system.entities.Coupon;
import shabtay.coupon.system.entities.Customer;
import shabtay.coupon.system.exceptions.CompanyAlreadyExistException;
import shabtay.coupon.system.exceptions.CustomerAlreadyExistException;
import shabtay.coupon.system.exceptions.NameOrIdNotExistException;
import shabtay.coupon.system.exceptions.WrongLoginInputException;

/**
 * Class AdminFacade gives the administrator access to all the data of Customers
 * and Companies able to create, remove,update, get coupon list, get company
 * list, get customer list get company by name, get customer by name, get
 * company by id, get customer by id
 * 
 * @author Shabtay Shalem
 *
 */
@Component
public class AdminFacade implements CouponClientFacade {

	private static Logger logger = LogManager.getLogger(AdminFacade.class);

	@Autowired
	CompanyDBDAO companyDBDAO;

	@Autowired
	CustomerDBDAO customerDBDAO;

	@Autowired
	CouponDBDAO couponDBDAO;

	public AdminFacade() {
		super();
	}

	@Override
	public CouponClientFacade login(String name, String password) throws WrongLoginInputException {

		if ((password.equals(ConstantList.ADMIN_PASSWORD)) && (name.equals(ConstantList.ADMIN_USER))) {
			return this;
		}
		logger.debug("login() executed for " + name + ", " +  password);
		throw new WrongLoginInputException(ConstantList.WRONG_LOGIN);
	}

	/**
	 * Creates new company
	 * 
	 * @param newCompany
	 *            is the company to add
	 * @throws CompanyAlreadyExistException
	 *             Exception is thrown in case company with same CompanyName exists
	 * @throws InterruptedException
	 */
	public void createCompany(Company newCompany) throws CompanyAlreadyExistException, InterruptedException {

		if (companyDBDAO.getCompanyByName(newCompany.getCompName()) != null)
			throw new CompanyAlreadyExistException(newCompany.getCompName() + ConstantList.NAME_EXIST);
		companyDBDAO.createCompany(newCompany);
		logger.debug("createCompany() executed for " + newCompany);
	}

	/**
	 * Removes a Company
	 * 
	 * @param company
	 *            The company to be removed
	 * @throws InterruptedException
	 */
	public void removeCompany(Company company) throws InterruptedException {
		companyDBDAO.removeCompany(company);
		logger.debug("removeCompany() executed for " + company);
	}

	/**
	 * Updates Company
	 * 
	 * @param company
	 *            The company to be updated
	 * @throws InterruptedException
	 * @throws NameOrIdNotExistException
	 */	
	public void updateCompany(Company company) throws InterruptedException, NameOrIdNotExistException {
		if (company == null)
			throw new NameOrIdNotExistException(ConstantList.NAME_OR_ID_NOT_EXIST);
		companyDBDAO.updateCompany(company);
		logger.debug("updateCompany() executed for " + company);
	}

	/**
	 * Get Company details by Company Id
	 * 
	 * @param id
	 *            company Id
	 * @return Company
	 * @throws InterruptedException
	 * @throws NameOrIdNotExistException
	 */	
	public Company getCompany(long id) throws InterruptedException, NameOrIdNotExistException {
		if (companyDBDAO.getCompany(id) == null)
			throw new NameOrIdNotExistException(ConstantList.NAME_OR_ID_NOT_EXIST);
		logger.debug("getCompany() executed to get company by Id");
		return companyDBDAO.getCompany(id);
	}

	/**
	 * Get Company details by Company name
	 * 
	 * @param name
	 *            Company name
	 * @return Company
	 * @throws InterruptedException
	 * @throws NameOrIdNotExistException
	 */
	public Company getCompByName(String name) throws InterruptedException, NameOrIdNotExistException {
		if (companyDBDAO.getCompanyByName(name) == null)
			throw new NameOrIdNotExistException(ConstantList.NAME_OR_ID_NOT_EXIST);
		logger.debug("getCompanyByName() executed ");
		return companyDBDAO.getCompanyByName(name);
	}

	/**
	 * Get all the Companies in Data Base
	 * 
	 * @return Collection of Company
	 * @throws InterruptedException
	 */

	public Collection<Company> getAllCompanies() throws InterruptedException {
		logger.debug("getAllCompanies() executed ");
		return companyDBDAO.getAllCompanies();
	}

	public Collection<Coupon> getAllCoupon() throws InterruptedException {
		logger.debug("Class AdminFacade getAllCoupon() executed ");
		return couponDBDAO.getAllCoupon();
	}

	/**
	 * Creates new Customer
	 * 
	 * @param newCustomer
	 *            is the Customer to add
	 * @throws CustomerAlreadyExistException
	 *             Exception is thrown in case Customer with same CustomerName
	 *             exists
	 * @throws InterruptedException
	 */
	public void createCustomer(Customer newCustomer) throws CustomerAlreadyExistException, InterruptedException {

		if (customerDBDAO.getCustomerByName(newCustomer.getCustName()) != null)
			throw new CustomerAlreadyExistException(newCustomer.getCustName() + ConstantList.NAME_EXIST);
		customerDBDAO.createCustomer(newCustomer);
		logger.debug("createCustomer() executed for " + newCustomer);
	}

	/**
	 * Remove Customer From DB
	 * 
	 * @param customer
	 *            The Customer to be removed
	 * @throws InterruptedException
	 */
	public void removeCustomer(Customer customer) throws InterruptedException {
		customerDBDAO.removeCustomer(customer);
		logger.debug("removeCustomer() executed for " + customer);
	}

	/**
	 * Update Customer From DB
	 * 
	 * @param customer
	 *            The customer to be updated
	 * @throws InterruptedException
	 */
	public void updateCustomer(Customer customer) throws InterruptedException {
		customerDBDAO.updateCustomer(customer);
		logger.debug("updateCustomer() executed for " + customer);
	}

	/**
	 * Get Customer details by Customer Id
	 * 
	 * @param id
	 *            customer ID
	 * @return Customer
	 * @throws InterruptedException
	 * @throws NameOrIdNotExistException
	 */
	public Customer getCustomer(long id) throws InterruptedException, NameOrIdNotExistException {
		if (customerDBDAO.getCustomer(id) == null)
			throw new NameOrIdNotExistException(ConstantList.NAME_OR_ID_NOT_EXIST);
		logger.debug("getCustomer() executed to get customer by Id");
		return customerDBDAO.getCustomer(id);
	}

	/**
	 * Get all customers from DB
	 * 
	 * @return Collection of Customer
	 * @throws InterruptedException
	 */
	public Collection<Customer> getAllCustomer() throws InterruptedException {
		logger.debug("getAllCustomer() executed ");
		return customerDBDAO.getAllCustomer();
	}

	/**
	 * Get Customer details by customer name
	 * 
	 * @param custName
	 *            Customer name
	 * @return Customer
	 * @throws InterruptedException
	 * @throws NameOrIdNotExistException
	 */
	public Customer getCustomerByName(String custName) throws InterruptedException, NameOrIdNotExistException {
		if (customerDBDAO.getCustomerByName(custName) == null)
			throw new NameOrIdNotExistException(ConstantList.NAME_OR_ID_NOT_EXIST);
		logger.debug("getCustomerByName() executed ");
		return customerDBDAO.getCustomerByName(custName);
	}
}
