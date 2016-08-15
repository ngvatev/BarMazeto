package go.go.services;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import go.go.dao.UserDao;
import go.go.enums.UserRole;
import go.go.model.User;

@Path("register")
@Stateless
public class RegisterService {

	@Inject
	private UserDao userDAO;

	private static final String USERNAME_REGEX = "^[a-z0-9_-]{3,15}$";
	private static final String PASSWORD_REGEX = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registerUser(User newUser) {

		User existingUser = userDAO.getUserByUsername(newUser.getUsername().trim());
		if(existingUser != null) {
			return Response.serverError().build();
		}

		if(!newUser.getUsername().trim().matches(USERNAME_REGEX)) {
			return Response.serverError().build();
		}

		if(!newUser.getPassword().trim().matches(PASSWORD_REGEX)) {
			return Response.serverError().build();
		}

		userDAO.addUser(newUser);
		return Response.ok().build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public UserRole[] getUserRole() {
		return UserRole.values();
	}
}
