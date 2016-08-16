package go.go.dao;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import go.go.model.Product;
import go.go.model.Sales;
import go.go.utils.DaoUtils;

import java.util.ArrayList;
import java.util.List;

import java.math.BigDecimal;;

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

	public ArrayList<Sales> getSales (){
		
		String query = 	"SELECT p.name, p.idProduct, SUM(op.quantity) " +
						"FROM Ordered_Products op " +
						"JOIN Product p ON op.idProduct = p.idProduct " +
						"GROUP BY p.idProduct, p.name";
		ArrayList<Sales> sales = new ArrayList<Sales>();
		
		List<Object[]> rows = manager.createNativeQuery(query).getResultList();
		for (Object[] row: rows){
			Sales sale = new Sales ((String) row[0], (Integer) row[1], (BigDecimal) row[2]);
			sales.add(sale);
		}
		return sales;
	}
	
	@SuppressWarnings("unchecked")
	public List<Product> getAllProducts() {
		List<Product> p = manager.createQuery("SELECT p FROM Product p ").getResultList();
		return p;
	}
}
