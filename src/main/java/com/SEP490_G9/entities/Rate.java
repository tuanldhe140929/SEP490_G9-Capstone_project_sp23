package com.SEP490_G9.entities;

import java.io.Serializable;
import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "rates")
public class Rate {
    @EmbeddedId
    private RateId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("accountId")
    @JoinColumn(name = "account_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "stars")
    private int stars;

    @Column(name = "date")
    private Date date;

    public RateId getId() {
		return id;
	}

	public void setId(RateId id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
		this.stars = stars;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	
    public Rate() {}

    public Rate(User user, Product product, int stars, Date date) {
        this.user = user;
        this.product = product;
        this.stars = stars;
        this.date = date;
        this.id = new RateId(user.getId(), product.getId());
    }

   
    @Embeddable
    public static class RateId implements Serializable {
        @Column(name = "account_id")
        private Long accountId;

        @Column(name = "product_id")
        private Long productId;

        public RateId() {}

        public RateId(Long accountId, Long productId) {
            this.accountId = accountId;
            this.productId = productId;
        }

		public Long getAccountId() {
			return accountId;
		}

		public void setAccountId(Long accountId) {
			this.accountId = accountId;
		}

		public Long getProductId() {
			return productId;
		}

		public void setProductId(Long productId) {
			this.productId = productId;
		}
		

    }
}
