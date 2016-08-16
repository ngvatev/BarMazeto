package go.go.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import go.go.context.UserContext;
import go.go.dao.OrderDao;
import go.go.dao.OrderedProductsDao;
import go.go.dao.ProductDao;
import go.go.dao.UserDao;
import go.go.enums.OrderType;
import go.go.model.Order;
import go.go.model.OrderedProducts;
import go.go.model.Product;
import go.go.model.Sales;

@Stateless
@Path("statistics")
public class StatisticsService {
	
	
	
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
	
	@GET
	@Path("/sales")
	@Produces("application/json")
	public Collection<Sales> asdasd(@QueryParam("date_from")int from, @QueryParam("date_to")int to) {
		// /go/rest/statistics/sales?date_from=5&date_to=6
		System.out.printf("%d",from);
		return productDAO.getSales();
	}
}
