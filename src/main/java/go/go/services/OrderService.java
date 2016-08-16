package go.go.services;

import java.net.HttpURLConnection;
import java.util.Calendar;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import go.go.context.UserContext;
import go.go.dao.OrderDao;
import go.go.dao.OrderedProductsDao;
import go.go.dao.ProductDao;
import go.go.dao.UserDao;
import go.go.enums.OrderType;
import go.go.model.Order;
import go.go.model.OrderedProducts;

@Stateless
@Path("order")
public class OrderService {
	private static final Response RESPONSE_OK = Response.ok().build();

	@Inject
	private OrderDao orderDAO;

	@Inject
	private OrderedProductsDao orderedDAO;

	@Inject
	private UserDao userDAO;
	
	@Inject
	private ProductDao productDAO;

	@Inject
	private UserContext context;

	@POST
	@Path("make")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response makeOrder(Collection<OrderedProducts> products) {
		if (products.isEmpty()) {
			return Response.status(HttpURLConnection.HTTP_NO_CONTENT).build();
		}
		int id = orderDAO.addOrder(new Order(Calendar.getInstance().getTime(),
				userDAO.getUserByUsername(context.getCurrentUser().getUsername()), OrderType.WAITING));
		for (OrderedProducts product : products) {
			product.setProduct(productDAO.getProductByName(product.getProduct().getName()));
			product.setOrder(orderDAO.getOrderById(id));
			orderedDAO.addOrderProduct(product);
		}
		return RESPONSE_OK;
	}

	@GET
	@Produces("application/json")
	public Collection<Order> getOrders() {
		Collection<Order> orders = orderDAO.getAllOrders();
		return orders;
	}

	@PUT
	@Path("{idOrder}")
	public Response changeStatus(@PathParam("idOrder") int id, String type) {
		if (OrderType.POSTPONED.toString().equals(type) || OrderType.WAITING.toString().equals(type))
			return Response.status(HttpURLConnection.HTTP_NOT_ACCEPTABLE).build();
		System.out.println(type);
		Order order = orderDAO.getOrderById(id);
		System.err.println(order.getType());
		if ((order.getType().equals(OrderType.POSTPONED) || order.getType().equals(OrderType.ACCEPTED))
				&& OrderType.COMPLETED.toString().equals(type)) {
			order.setType(OrderType.COMPLETED);
			order.setTimeFinished(Calendar.getInstance().getTime());
		} else if (order.getType().equals(OrderType.WAITING) && OrderType.ACCEPTED.toString().equals(type)) {
			order.setType(OrderType.ACCEPTED);
			order.setBarman(userDAO.getUserByUsername(context.getCurrentUser().getUsername()));
		} else
			return Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).build();

		orderDAO.updateOrder(order);
		return RESPONSE_OK;
	}

	@GET
	@Path("details/{idOrder}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<OrderedProducts> getDetails(@PathParam("idOrder") int id) {
		return orderedDAO.getAllOrderedProducts(orderDAO.getOrderById(id));
	}

	@GET
	@Path("type")
	@Produces(MediaType.APPLICATION_JSON)
	public OrderType[] getAllOrderTypes() {
		OrderType[] rv = { OrderType.ACCEPTED, OrderType.COMPLETED };
		return rv;
	}
}
