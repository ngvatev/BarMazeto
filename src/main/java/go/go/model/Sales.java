package go.go.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import go.go.enums.ProductType;

public class Sales {
	
	private String productName;
	
	private Integer idProduct;
	
	private BigDecimal timesSold;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getIdProduct() {
		return idProduct;
	}

	public void setIdProduct(Integer idProduct) {
		this.idProduct = idProduct;
	}

	public BigDecimal getTimesSold() {
		return timesSold;
	}

	public void setTimesSold(BigDecimal timesSold) {
		this.timesSold = timesSold;
	}

	public Sales(String productName, Integer idProduct, BigDecimal timesSold) {
		super();
		this.productName = productName;
		this.idProduct = idProduct;
		this.timesSold = timesSold;
	}

	@Override
	public String toString() {
		return "Sales [productName=" + productName + ", idProduct=" + idProduct + ", timesSold=" + timesSold + "]";
	}
	
	
	
}
