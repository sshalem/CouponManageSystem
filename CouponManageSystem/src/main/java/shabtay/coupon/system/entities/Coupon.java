package shabtay.coupon.system.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import shabtay.coupon.system.DBDAO.JsonDateSerializer;
import shabtay.coupon.system.common.CouponType;

/**
 * Represents Coupons
 * 
 * @author Shabtay Shalem
 *
 */
@Entity(name = "COUPON")
@XmlRootElement
public class Coupon implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "START_DATE")
	private Date startDate;

	@Column(name = "END_DATE")
	private Date endDate;

	@Column(name = "AMOUNT")
	private int amount;

	@Enumerated(EnumType.STRING)
	@Column(name = "COUPON_TYPE")
	private CouponType type;

	@Column(name = "MESSAGE")
	private String message;

	@Column(name = "PRICE")
	private double price;

	@Column(name = "IMAGE")
	private String image;

	// (1) this is a ManyToMany association between Coupon Entity to Customer Entity
	// (BiDirectional)
	// which creates new table (Customer_Coupon) in DB with column names coupon_id &
	// customer_id
	// (2) Using Fetchtype.EAGER - the Data of Customer will be loaded With all
	// of his Coupons.
	// (3) Cascade Options: DETACH - detaches child object, REFRESH - refresh child
	// object
	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH, CascadeType.REFRESH })
	@JoinTable(name = "Customer_Coupon", joinColumns = @JoinColumn(name = "coupon_id"), inverseJoinColumns = @JoinColumn(name = "customer_id"))	
	private Collection<Customer> customers;

	/**
	 * Constructor
	 * 
	 * @param title
	 *            coupon title
	 * @param startDate
	 *            coupon start date
	 * @param endDate
	 *            coupo end date
	 * @param amount
	 *            coupon amount
	 * @param type
	 *            coupon type
	 * @param message
	 *            - general message
	 * @param price
	 *            coupon price
	 * @param image
	 *            - Image file
	 * @param customers
	 *            associated entity
	 */
	public Coupon(String title, Date startDate, Date endDate, int amount, CouponType type, String message, double price,
			String image, Collection<Customer> customers) {
		super();
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
		this.type = type;
		this.message = message;
		this.price = price;
		this.image = image;
		this.customers = customers;
	}

	/**
	 * Default constructor
	 */
	public Coupon() {
		super();
	}

	public Collection<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(Collection<Customer> customers) {
		this.customers = customers;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@JsonSerialize(using=JsonDateSerializer.class)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public CouponType getType() {
		return type;
	}

	public void setType(CouponType type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "\nCoupon [id=" + id + ", title=" + title + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", amount=" + amount + ", type=" + type + ", message=" + message + ", price=" + price + ", image="
				+ image + "]";
	}

}
