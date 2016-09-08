package go.go.dao;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import go.go.model.User;
import go.go.utils.DaoUtils;

import java.security.MessageDigest;
import java.util.List;

@Singleton
public class UserDao {

	@PersistenceContext
	private EntityManager manager = DaoUtils.getEmfactory();

	public void addUser(User user) {
		user.setPassword(getHashedPassword(user.getPassword()));
		manager.persist(user);
	}

	public boolean validateUserCredentials(String userName, String password) {
		System.out.println(userName + "..." + password);
		String txtQuery = "SELECT u FROM User u WHERE u.username = :userName AND u.password = :password";
		TypedQuery<User> query = manager.createQuery(txtQuery, User.class);
		query.setParameter("userName", userName);
		query.setParameter("password", getHashedPassword(password));
		return queryUser(query) != null;
//		return true;
	}

	public List<User> listUsers() {
		return manager.createQuery("SELECT u FROM User u ", User.class).getResultList();
	}

	public void deleteUser(User user) {
		manager.remove(user);
	}

	public User getUserById(Integer userId) {
		return manager.find(User.class, userId);
	}

	public User getUserByUsername(String uname) {
		String sql = "SELECT u FROM User u WHERE u.username = :uname";
		TypedQuery<User> query = manager.createQuery(sql, User.class);
//		return (User) manager.createQuery("SELECT u FROM User u WHERE u.username = \'" + uname + "\'")
//				.getSingleResult();
		query.setParameter("uname", uname);
		return queryUser(query);
	}

	public void updateUser(User user) {
		try {
			manager.getTransaction().begin();
			manager.merge(user);
			manager.getTransaction().commit();
		} finally {
			if (manager.getTransaction().isActive()) {
				manager.getTransaction().rollback();
			}
		}
	}

	private User queryUser(TypedQuery<User> query) {
		try {
			System.err.println("HERE QUERY");
			System.out.println(query.getSingleResult());
			return query.getSingleResult();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	private String getHashedPassword(String password) {
		try {
			MessageDigest mda = MessageDigest.getInstance("SHA-256");
			password = new String(mda.digest(password.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(password);
		return password;
	}
}
