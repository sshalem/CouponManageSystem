package shabtay.coupon.system.entry;




import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import shabtay.coupon.system.common.ClientType;
import shabtay.coupon.system.connection.ConnectionPool;
import shabtay.coupon.system.exceptions.WrongLoginInputException;
import shabtay.coupon.system.facade.AdminFacade;
import shabtay.coupon.system.facade.CompanyFacade;
import shabtay.coupon.system.facade.CouponClientFacade;
import shabtay.coupon.system.facade.CustomerFacade;
import shabtay.coupon.system.thread.DailyCouponExpirationTask;

/**
 * Class CouponSystem is entry point for nominees (ADMIN, COMPANY, CUSTOMER)
 * also initiates the Daily Coupon Expiration Task
 * 
 * @author Shabtay Shalem
 * 
 */

@Component
@Scope("singleton")
public class CouponSystem {

	Logger logger = LogManager.getLogger(CouponSystem.class);
	
	@Autowired
	AdminFacade adminFacade;

	@Autowired
	ApplicationContext ctx;

	
	private boolean startDailyThreadTask = true;

	public CouponSystem() {
		super();
	}

	/**
	 * Method returns Facade (AdminFacade, CopmanyFacade, CustomerFacade), 
	 * also invokes the Daily Coupon Expiration Task 
	 * @param name
	 *            enter the user name
	 * @param password
	 *            ender the user name password
	 * @param type
	 *            user type : ADMIN, COMPANY, CUSTOMER
	 * @return CouponClientFacade (can be : adminFacade, companyFacade ,customerFacade ) or null;
	 * @throws WrongLoginInputException
	 *             Exception is thrown in case Login input is Wrong
	 * @throws InterruptedException
	 */

	public CouponClientFacade login(String name, String password, ClientType type) throws WrongLoginInputException, InterruptedException {
		
		if (this.startDailyThreadTask) {
			DailyCouponExpirationTask dailyThread = new DailyCouponExpirationTask(ctx);
			Thread invokeDailyThreadTask = new Thread(dailyThread);
			invokeDailyThreadTask.start();
			this.startDailyThreadTask = false;
		}
		switch (type) {
		case ADMIN:
			logger.info(ctx.getBean(AdminFacade.class).hashCode());
			return adminFacade.login(name, password);
		case COMPANY:	
			CompanyFacade companyFacade = ctx.getBean(CompanyFacade.class);
			logger.info(companyFacade.hashCode()); 
			return companyFacade.login(name, password);
		case CUSTOMER:
			logger.info(ctx.getBean(CustomerFacade.class).hashCode());
			return ctx.getBean(CustomerFacade.class).login(name, password); 
		}
		return null;
	}

	/**
	 * This method stops the daily Coupon Expiration Task and terminates all
	 * connection and terminates the currently running program immidiately
	 */
	public void shutdown() {
		boolean terminate = ConnectionPool.getInstance().terminateDailyTaskAndCloseConnectionPool();
		if (terminate) {
			System.out.println("stoped daily task and closed connection pool");
			System.exit(0); // terminates the program immidiately
		}
	}

}
