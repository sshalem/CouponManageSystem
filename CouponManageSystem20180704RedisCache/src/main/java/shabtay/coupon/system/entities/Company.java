package shabtay.coupon.system.entities;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore; 



/**
 * Represents Companies
 * 
 * @author Shabtay Shalem
 *
 */
@Entity(name = "COMPANY")
@XmlRootElement
public class Company implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "COMPANY_NAME")
	private String compName;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "EMAIL")
	private String email;

	// (1) this is a OneToMany association between Company Entity to Coupon Entity
	// (UniDirectional)
	// which creates new column in Table "COUPON" with a given name "company_id"
	// (2) Using Fetchtype.EAGER - the Data of Company to be loaded With all
	// of his Coupons.
	// (3) CascadeType.All - propagates all operations (persist, remove, merge...)
	// from Parent Object (Company) to Child object (Coupon)	
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH , CascadeType.REMOVE})
	@JoinColumn(name = "company_id")
	@JsonIgnore
	private Set<Coupon> coupons; 

	/**
	 * default constructor
	 */
	public Company() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param compName
	 *            company name
	 * @param password
	 *            company password
	 * @param email
	 *            company email
	 */
	public Company(String compName, String password, String email) {
		super();
		this.compName = compName;
		this.password = password;
		this.email = email;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(Set<Coupon> coupons) {
		this.coupons = coupons;
	}

	public void addCoupon(Coupon coupon) {
		this.coupons.add(coupon);
	}

	public void removeCoupon(Coupon coupon) {
		this.coupons.remove(coupon);
	}

	@Override
	public String toString() {
		return "\nCompany [id=" + id + ", compName=" + compName + ", password=" + password + ", email=" + email + "]";
	}

}
