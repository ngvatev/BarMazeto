package go.go.dao;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import go.go.model.Product;
import go.go.model.Sales;
import go.go.utils.DaoUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import java.math.BigDecimal;

@Singleton
public class ProductDao {
	@PersistenceContext
	private EntityManager manager = DaoUtils.getEmfactory();

	public void addProduct(Product product) {
		manager.persist(product);
	}

	public void deleteProduct(int id) {
		manager.remove(getProductById(id));
	}

	public Product getProductById(int producttId) {
		return manager.find(Product.class, producttId);
	}
	
	public Product getProductByName(String name){
		return manager.createQuery("SELECT p FROM Product p where p.name = :name", Product.class).setParameter("name", name).getResultList().get(0);
	}

	@SuppressWarnings("unchecked")
	public List<Sales> getSales (Date from, Date to){
		// query in JPQL format 
		String query = 	"SELECT NEW go.go.model.Sales (p.name, p.idProduct, SUM(op.quantity)) " +
						"FROM OrderedProducts op " +
						"JOIN op.product p " +
						"JOIN op.order o " +
						"WHERE o.timeFinished BETWEEN :from AND :to "+
						"GROUP BY p.idProduct, p.name";
		
		Query q = manager.createQuery(query, Sales.class);
		q.setParameter("from", from, TemporalType.TIMESTAMP);
		q.setParameter("to", to, TemporalType.TIMESTAMP);
		List<Sales> sales = q.getResultList();
		
		return sales;
	}
	
	@SuppressWarnings("unchecked")
	public List<Product> getAllProducts() {
		List<Product> p = manager.createQuery("SELECT p FROM Product p ").getResultList();
		return p;
	}
}
