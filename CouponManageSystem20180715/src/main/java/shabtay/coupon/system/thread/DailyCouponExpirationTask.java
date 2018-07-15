package shabtay.coupon.system.thread;

import java.util.Date;
import org.springframework.context.ApplicationContext;
import shabtay.coupon.system.DBDAO.CouponDBDAO;
import shabtay.coupon.system.connection.ConnectionPool;

/**
 * Class DailyCouponExpirationTask implemets Runnable interface in this class,
 * Defines the logic of the thread and Every 24 hours Thread will check for
 * expired coupons and if there are such he will delete them from DB.
 * 
 * @author shabtay shalem
 *
 */
public class DailyCouponExpirationTask implements Runnable {

	private CouponDBDAO couponDBDAO;

	public DailyCouponExpirationTask(ApplicationContext ctx) {
		this.couponDBDAO = ctx.getBean(CouponDBDAO.class);
	}

	/**
	 * run the Daily Coupon Expiration Task deletes Coupons which there endDate is
	 * expired
	 */
	@Override
	public void run() {
		while (!ConnectionPool.getInstance().invokeStopTask()) {
			try {
				couponDBDAO.deleteExpiredCoupons(new Date());
				Thread.sleep(1000 * 60 * 60 * 24);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}