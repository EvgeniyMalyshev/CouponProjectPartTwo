package service;





import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import exeptions.FacadeExeptions;
import facade.AdminFacade;
import javabeans.Company;
import javabeans.Customer;





@Path("/admin")
public class AdminService {


	private static final String FACADE_ATTRIBE_NAME = "admin";

	@POST
	@Path("/companies")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createCompany(Company company) {
		
		try {
			AdminFacade.createCompany(company);
			return Response
					.status(Status.CREATED)
					.type(MediaType.APPLICATION_JSON)
					.entity(company)
					.build();
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
	}

	@DELETE
	@Path("/companies/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeCompany(@PathParam("id") long id) {
		try {
			Company company = AdminFacade.getCompany(id);
			AdminFacade.removeCompany(company);
			return Response
					.status(Status.OK)
					.type(MediaType.APPLICATION_JSON)
					.entity(company)
					.build();
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
	}

	@PUT
	@Path("/companies")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCompany(Company company) {
		try {
			AdminFacade.updateCompany(company);
			return Response
					.status(Status.OK)
					.type(MediaType.APPLICATION_JSON)
					.entity(company)
					.build();
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
	}

	@GET
	@Path("/companies/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCompany(@PathParam("id") long id) throws FacadeExeptions {
		Company company = AdminFacade.getCompany(id);
		if (company == null)
			return Response.status(Status.NOT_FOUND)
					.build();
		return Response.status(Status.OK)
				.type(MediaType.APPLICATION_JSON)
				.entity(company).build();

	}

	@GET
	@Path("/companies")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCompanies()  {
		Collection<Company> list = null;
		try {
			list = AdminFacade.getAllCompanies();	
		} catch (FacadeExeptions e) {
			e.printStackTrace();
		}
		return Response
				.status(Status.OK)
				.entity(list)
				.build();
	}

	@POST
	@Path("/customers")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createCustomer(Customer customer) {
		try {
			AdminFacade.createCustomer(customer);
			return Response
					.status(Status.OK)
					.type(MediaType.APPLICATION_JSON)
					.entity(customer)
					.build();
		} catch ( FacadeExeptions e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}

	}
	
	@DELETE
	@Path("/customers/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeCustomer(@PathParam("id") long id) {
		try {
			Customer customer = AdminFacade.getCustomer(id);
			AdminFacade.removeCustomer(customer);
			return Response
					.status(Response.Status.OK)
					.type(MediaType.APPLICATION_JSON)
					.entity(customer)
					.build();
			
		} catch (FacadeExeptions e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
	}
	

	@PUT
	@Path("/customers")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCustomer(Customer customer) {
		try {
			
			AdminFacade.updateCustomer(customer);
			return Response
					.status(Status.OK)
					.type(MediaType.APPLICATION_JSON)
					.entity(customer)
					.build();
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
	}

	@GET
	@Path("/customers/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCustomer(@PathParam("id") long id) throws Exception {
		
		Customer customer = AdminFacade.getCustomer(id);
		if (customer == null)
			return Response
					.status(Status.NOT_FOUND)
					.type(MediaType.APPLICATION_JSON)
					.entity(customer)
					.build();
		return Response
				.status(Status.OK)
				.type(MediaType.APPLICATION_JSON)
				.entity(customer)
				.build();

	}

	@GET
	@Path("/customers")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAllCustomers() throws Exception {
		Collection<Customer> list = AdminFacade.getAllCustomers();
		return Response
				.status(Status.OK)
				.entity(list)
				.build();
	}


	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(GenericUser admin) {
		AdminFacade facade = new AdminFacade();
		try {
			facade = (AdminFacade) facade.login(admin.getName(), admin.getPassword());
			return Response
					.status(Status.OK)
					.build();
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
	}	
	
	@POST
	@Path("/logout")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response logout(@Context HttpServletRequest req, @Context HttpServletResponse res) throws CouponSystemWebException {
		HttpSession session = (HttpSession) req.getSession(false);
		try {
			if(session.getAttribute(FACADE_ATTRIBE_NAME) != null){
				session.removeAttribute(FACADE_ATTRIBE_NAME);
				session.invalidate();	
				
			}
			return Response
					.status(Status.OK)
					.build();
					
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
	}
}
