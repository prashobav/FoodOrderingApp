package com.upgrad.FoodOrderingApp.service.entity;

import java.io.Serializable;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
 
@Entity
@Table(name = "customer", schema = "public")
@NamedQueries(
        {
                @NamedQuery(name = "customerByUuid", query = "select u from CustomerEntity u where u.uuid = :uuid"),
                @NamedQuery(name = "customerByContactNumber", query = "select u from CustomerEntity u where u.contactNumber =:contactNumber")
        }
)
public class CustomerEntity implements Serializable{

	    @Id
	    @Column(name = "ID")
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;

	    @Column(name = "UUID")
	    @Size(max = 64)
	    private String uuid;

	    @Column(name = "EMAIL")
	    @Size(max = 200)
	    private String email;

	    //@ToStringExclude
	    @Column(name = "PASSWORD")
	    private String password;

	    @Column(name = "FIRSTNAME")
	    @NotNull
	    @Size(max = 200)
	    private String firstName;

	    @Column(name = "LASTNAME")
	    @Size(max = 200)
	    private String lastName;

		@Column(name = "CONTACT_NUMBER")
	    @NotNull
	    @Size(max = 50)
	    private String contactNumber;

 

	    @Column(name = "SALT")
	    @NotNull
	    @Size(max = 200)
	    //@ToStringExclude
	    private String salt;

 


	    @Override
	    public boolean equals(Object obj) {
	        return new EqualsBuilder().append(this, obj).isEquals();
	    }

	    public Integer getId() {
	        return id;
	    }

	    public void setId(Integer id) {
	        this.id = id;
	    }

	    public String getUuid() {
	        return uuid;
	    }

	    public void setUuid(String uuid) {
	        this.uuid = uuid;
	    }
 

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public String getPassword() {
	        return password;
	    }

	    public void setPassword(String password) {
	        this.password = password;
	    }

	    public String getFirstName() {
	        return firstName;
	    }

	    public void setFirstName(String firstName) {
	        this.firstName = firstName;
	    }

	    public String getLastName() {
	        return lastName;
	    }

	    public void setLastName(String lastName) {
	        this.lastName = lastName;
	    }

	    public String getContactNumber() {
			return contactNumber;
		}

		public void setContactNumber(String contactNumber) {
			this.contactNumber = contactNumber;
		}

	    
 

	    public String getSalt() {
	        return salt;
	    }

	    public void setSalt(String salt) {
	        this.salt = salt;
	    }
 
	 
 
	    @Override
	    public int hashCode() {
	        return new HashCodeBuilder().append(this).hashCode();
	    }

	    @Override
	    public String toString() {
	        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	    }




}
