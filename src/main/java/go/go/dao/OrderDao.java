package go.go.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Singleton;
import javax.management.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import go.go.enums.OrderType;
import go.go.model.Order;
import go.go.utils.DaoUtils;

@Singleton
public class OrderDao {
	@PersistenceContext
	private EntityManager manager = DaoUtils.getEmfactory();

	public int addOrder(Order order) {
		manager.persist(order);
		System.out.println(order.getIdOrder());
		return order.getIdOrder();
	}

	public void updateOrder(Order order) {
		manager.merge(order);
	}

	public void deleteOrder(int id) {
		manager.remove(getOrderById(id));
	}

	public Order getOrderById(int orderId) {
		return manager.find(Order.class, orderId);
	}

	@SuppressWarnings("unchecked")
	public List<Order> getAllOrders() {
		List<Order> orders = manager.createQuery("SELECT p FROM Order p ").getResultList();
		refactorTypes(orders);
		return orders;
	}

	@SuppressWarnings("unchecked")
	public List<Order> getAllOrder(OrderType type) {
		List<Order> orders = manager.createQuery("SELECT b FROM Order b where b.type=:type ").setParameter("type", type)
				.getResultList();
		return orders;
	}

	public double getOborot(){
		@SuppressWarnings("unchecked")
		TypedQuery<Double> query = (TypedQuery<Double>) manager.createNativeQuery("SELECT sum(p.price*o.quantity) FROM go.BarOrder bo join Ordered_Products o on bo.idOrder = o.idOrder join Product p on o.idProduct = p.IDPRODUCT where time_finished like '2016-08%'");
		return query.getSingleResult();
	}

	@SuppressWarnings("deprecation")
	private void refactorTypes(List<Order> orders) {
		for (Order order : orders) {
			Date dNow = new Date();
			Calendar cal = Calendar.getInstance();
			Date date = cal.getTime();
			cal.setTime(order.getTimeStarted());
			cal.add(Calendar.MINUTE, 5);
			dNow = cal.getTime();
			if (dNow.getDay() <= date.getDay() && dNow.getHours() <= date.getHours()
					&& dNow.getMinutes() <= date.getMinutes()) {
				if (order.getType().equals(OrderType.WAITING) || order.getType().equals(OrderType.ACCEPTED)) {
					order.setType(OrderType.POSTPONED);
					updateOrder(order);
				}
			}
		}
	}
}
