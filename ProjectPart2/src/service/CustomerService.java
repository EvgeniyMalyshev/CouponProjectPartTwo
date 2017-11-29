package service;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import enums.CouponType;
import facade.CustomerFacade;
import javabeans.Coupon;

@Path("/customers")
public class CustomerService {

	private static final String FACADE_ATTRIBE_NAME = "customer";

	@POST
	@Path("/coupons/purchase")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response purchaseCoupon(Coupon coupon, @Context HttpServletRequest req, @Context HttpServletResponse res) {
		try {
			Long customerId = getCustomerId(req);
			if (customerId != null) {
				CustomerFacade.purchaseCoupon(customerId, coupon);
			}
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
	}

	@POST
	@Path("/coupons/getall")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllPurchasedCoupons(@Context HttpServletRequest req, @Context HttpServletResponse res) {
		Collection<Coupon> list = new ArrayList<Coupon>();
		try {
			Long customerId = getCustomerId(req);
			if (customerId != null) {
				list = CustomerFacade.getAllPurchasedCoupons(customerId);
			}
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
		return Response.status(Status.OK).entity(list).build();
	}

	@POST
	@Path("/coupons/getbytype/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllPurchasedCouponsByType(@PathParam("type") String type, @Context HttpServletRequest req, @Context HttpServletResponse res) {
		Collection<Coupon> list = new ArrayList<Coupon>();
		try {
			
			Long customerId = getCustomerId(req);
			if (customerId != null) {
				CouponType c = CouponType.valueOf(type);
				list = CustomerFacade.getAllPurchasedCouponsByType(customerId, c);
			}
			return Response.status(Status.OK).entity(list).build();
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}

	}

	@POST
	@Path("/coupons/price/{price}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllPurchasedCouponsByPrice(@PathParam("price") double couponPrice, @Context HttpServletRequest req, @Context HttpServletResponse res) {
		Collection<Coupon> list = new ArrayList<Coupon>();
		try {
			Long customerId = getCustomerId(req);
			if (customerId != null) {
				list = CustomerFacade.getAllPurchasedCouponsByPrice(customerId, couponPrice);
			}
			return Response.status(Status.OK).entity(list).build();
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
	}

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(GenericUser customer, @Context HttpServletRequest req, @Context HttpServletResponse res) {
		CustomerFacade facade = new CustomerFacade();
		try {
			facade = (CustomerFacade) facade.login(customer.getName(), customer.getPassword());
			req.getSession().setAttribute(FACADE_ATTRIBE_NAME, facade);
			return Response.status(Status.OK).build();
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
	}

	@POST
	@Path("/coupons/logoutcustomer")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response logoutCustomer(@Context HttpServletRequest req, @Context HttpServletResponse res)
			throws CouponSystemWebException {
		HttpSession session = (HttpSession) req.getSession(false);
		try {
			if (session.getAttribute(FACADE_ATTRIBE_NAME) != null) {
				session.removeAttribute(FACADE_ATTRIBE_NAME);
				session.invalidate();
			}
			return Response.status(Status.OK).build();

		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	private static Long getCustomerId(HttpServletRequest req) {
		Object attribute = req.getSession().getAttribute(FACADE_ATTRIBE_NAME);
		if (attribute instanceof CustomerFacade) {
			return ((CustomerFacade) attribute).getId();
		}
		return null;
	}
	
}
