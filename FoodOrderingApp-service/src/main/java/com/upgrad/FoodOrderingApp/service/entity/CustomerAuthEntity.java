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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

 

@Entity
@Table(name = "Customer_Auth", schema = "public")
@NamedQueries({
        @NamedQuery(name = "customerAuthByAccessToken", query = "select ut from CustomerAuthEntity ut where ut.accessToken = :accessToken ")
})
public class CustomerAuthEntity implements Serializable {
	    
		@Id
	    @Column(name = "ID")
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;

	    @ManyToOne
	    @JoinColumn(name = "CUSTOMER_ID")
	    private CustomerEntity customer;
	    
	    @Column(name = "UUID")
	    @Size(max = 64)
	    private String uuid;
	    
	    @Column(name = "ACCESS_TOKEN")
	    @NotNull
	    @Size(max = 500)
	    private String accessToken;

	    @Column(name = "LOGIN_AT")
	    @NotNull
	    private ZonedDateTime loginAt;

	    @Column(name = "EXPIRES_AT")
	    @NotNull
	    private ZonedDateTime expiresAt;

	    @Column(name = "LOGOUT_AT")
	    private ZonedDateTime logoutAt;
	    
	    public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public CustomerEntity getCustomer() {
			return customer;
		}

		public void setCustomer(CustomerEntity customer) {
			this.customer = customer;
		}

		public String getUuid() {
			return uuid;
		}

		public void setUuid(String uuid) {
			this.uuid = uuid;
		}

		public String getAccessToken() {
			return accessToken;
		}

		public void setAccessToken(String accessToken) {
			this.accessToken = accessToken;
		}

		public ZonedDateTime getLoginAt() {
			return loginAt;
		}

		public void setLoginAt(ZonedDateTime loginAt) {
			this.loginAt = loginAt;
		}

		public ZonedDateTime getExpiresAt() {
			return expiresAt;
		}

		public void setExpiresAt(ZonedDateTime expiresAt) {
			this.expiresAt = expiresAt;
		}

		public ZonedDateTime getLogoutAt() {
			return logoutAt;
		}

		public void setLogoutAt(ZonedDateTime logoutAt) {
			this.logoutAt = logoutAt;
		}

	    
	    @Override
	    public boolean equals(Object obj) {
	        return new EqualsBuilder().append(this, obj).isEquals();
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
