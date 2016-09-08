package go.go.services;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
import go.go.model.Sales;


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
	public Collection<OrderedProducts> getAllOrderedProducts(@PathParam("type") OrderType type) {
		List<Order> orders = orderDAO.getAllOrder(type);
		List<OrderedProducts> result = new ArrayList<OrderedProducts>();
		for (Order order : orders) {
			result.addAll(orderedDAO.getAllOrderedProducts(order));
		}
		return result;
	}

	@GET
	@Path("daily")
	@Produces(MediaType.TEXT_PLAIN)
	public Double getTurnoverDaily() {
		NumberFormat formatter = new DecimalFormat("#0.00");
		System.out.println(orderDAO.getOborotDaily());
		return Double.valueOf(formatter.format(orderDAO.getOborotDaily()));
//		return 0.0;
	}

	@GET
	@Path("monthly")
	@Produces(MediaType.TEXT_PLAIN)
	public Double getTurnoverMonthly() {
		NumberFormat formatter = new DecimalFormat("#0.00");
		return Double.valueOf(formatter.format(orderDAO.getOborotMonthly()));

	}

	@GET
	@Path("sales")
	@Produces("application/json")
	public Collection<Sales> getSales(@QueryParam("date_from")String from, @QueryParam("date_to")String to) {
		// /go/rest/statistics/sales?date_from=date1&date_to=date2
		// Tue Aug 02 2016 11:11:00 GMT 0300 (FLE Daylight Time)
		System.out.println("HERE Sales");
		SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss");
		System.out.println("XXXXXXXXXXXXXXXXXXX");
		System.out.println(from);
		System.out.println(to);
		Date dateFrom = new Date (), dateTo = new Date ();
		try {
			dateFrom = format.parse (from);
			dateTo = format.parse (to);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return productDAO.getSales(dateFrom, dateTo);
	}

}
