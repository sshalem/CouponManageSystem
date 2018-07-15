package shabtay.coupon.system.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Represents Customers
 * 
 * @author Shabtay Shalem
 *
 */
@Entity(name = "CUSTOMER")
@XmlRootElement
public class Customer implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "CUSTOMER_NAME")
	private String custName;

	@Column(name = "PASSWORD")
	private String password;

	// (1) this is a ManyToMany association between Customer Entity to Coupon Entity
	// (BiDirectional)
	// which creates new table (Customer_Coupon) in DB with column names coupon_id &
	// customer_id
	// (2) Using Fetchtype.EAGER - the Data of Customer will be loaded With all
	// of his Coupons.
	// (3) Cascade Options: MERGE - performs persist(insert) & update , DETACH -
	// detaches child object, REFRESH - refresh child object	
	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH }) //
	@JoinTable(name = "Customer_Coupon", joinColumns = @JoinColumn(name = "customer_id"), inverseJoinColumns = @JoinColumn(name = "coupon_id"))
	@JsonIgnore
	private Collection<Coupon> coupons;

	/**
	 * Constructor
	 * 
	 * @param custName
	 *            customer name
	 * @param password
	 *            customer password
	 */
	public Customer(String custName, String password) {
		super();
		this.custName = custName;
		this.password = password;
	}

	/**
	 * default constructor
	 */
	public Customer() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Collection<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(Collection<Coupon> coupons) {
		this.coupons = coupons;
	}

	@Override
	public String toString() {
		return "\nCustomer [id=" + id + ", custName=" + custName + ", password=" + password + "]";
	}

}
