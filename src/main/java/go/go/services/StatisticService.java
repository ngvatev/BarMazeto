package go.go.services;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import go.go.dao.OrderDao;
import go.go.dao.OrderedProductsDao;
import go.go.dao.ProductDao;
import go.go.dao.UserDao;
import go.go.enums.OrderType;
import go.go.enums.ProductType;
import go.go.model.Order;
import go.go.model.OrderedProducts;
@Path("statistic")
@Stateless
public class StatisticService {
	private static final Response RESPONSE_OK = Response.ok().build();

	@Inject
	private OrderDao orderDAO;

	@Inject
	private OrderedProductsDao orderedDAO;

	@Inject
	private UserDao userDAO;
	@Inject
	private ProductDao productDAO;
	
	@GET
	@Path("{type}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<OrderedProducts> getAllOrderedProducts(@PathParam("type") OrderType type){
		List<Order> orders = orderDAO.getAllOrder(type);
		List<OrderedProducts> result = new ArrayList<OrderedProducts>();
		for (Order order : orders) {
			result.addAll(orderedDAO.getAllOrderedProducts(order));
		}
		return result;
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Double getOborot(){
		 NumberFormat formatter = new DecimalFormat("#0.00");
		return Double.valueOf(formatter.format(orderDAO.getOborot()));
	}

}
