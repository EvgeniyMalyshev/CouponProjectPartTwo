package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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

import enums.CouponType;
import exeptions.FacadeExeptions;
import facade.CompanyFacade;
import javabeans.Coupon;

@Path("/companies")
public class CompanyService {

	private static final String FACADE_ATTRIBE_NAME = "company";

	@POST
	@Path("/coupons/create")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createCoupon(Coupon coupon, @Context HttpServletRequest req, @Context HttpServletResponse res) {
		try {
			Long companyId = getCompanyId(req);
			if (companyId != null) {
				CompanyFacade.createCoupon(coupon, companyId);
			}
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(coupon).build();
		} catch (Exception e) {
			throw new CouponSystemWebException("CompanyService: " + e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
	}

	@DELETE
	@Path("/coupons/remove/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeCoupon(@PathParam("id") long couponId) throws SQLException {
		try {
			System.out.println("deleting a coupon");
			Coupon coupon = CompanyFacade.getCoupon(couponId);
			CompanyFacade.removeCoupon(coupon);
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(coupon).build();
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.BAD_REQUEST);
		}
	}

	@PUT
	@Path("/coupons/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCoupon(Coupon coupon) throws SQLException {
		try {
			CompanyFacade.updateCoupon(coupon);
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(coupon).build();
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.NOT_MODIFIED);
		}
	}

	@GET
	@Path("/coupons/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCoupon(@PathParam("id") long id) throws FacadeExeptions {
		try {
			Coupon coupon = CompanyFacade.getCoupon(id);
			if (coupon == null)
				throw new SQLException();
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(coupon).build();
		} catch (SQLException e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}

	}

	
	@GET
	@Path("/coupons/getall")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCoupons(@Context HttpServletRequest req, @Context HttpServletResponse res)
			throws FacadeExeptions {
		Collection<Coupon> list = Collections.EMPTY_LIST;
		
		Long id = getCompanyId(req);
		if (id != null) {
			list = CompanyFacade.getAllCoupons(id);
		}
		return Response.status(Status.OK).entity(list).type(MediaType.APPLICATION_JSON).build();
	}

	
	@GET
	@Path("/coupons/type/{type}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getCouponsByType(@PathParam("type") String couponType, @Context HttpServletRequest req, @Context HttpServletResponse res) {
		Collection<Coupon> couponsByType = new ArrayList();
		try {
			System.out.println("get all coupons by type");
			CouponType myType = CouponType.valueOf(couponType);
			Long companyId = getCompanyId(req);
			if (companyId != null) {
				couponsByType = CompanyFacade.getCouponByType(companyId, myType);
			}
			if (couponsByType.size() == 0)
				return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON)
						.entity(couponsByType).build();
			return Response.status(Status.OK).entity(couponsByType).build();
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
	}

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(GenericUser company, @Context HttpServletRequest req, @Context HttpServletResponse res) {
		CompanyFacade facade = new CompanyFacade();
		try {
			facade = (CompanyFacade) facade.login(company.getName(), company.getPassword());
			req.getSession().setAttribute(FACADE_ATTRIBE_NAME, facade);
			return Response.status(Status.OK).build();
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
	}

	@POST
	@Path("/coupons/logoutcompany")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response logoutCompany(@Context HttpServletRequest req, @Context HttpServletResponse res)
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
	
	private static Long getCompanyId(HttpServletRequest req) {
		Object attribute = req.getSession().getAttribute(FACADE_ATTRIBE_NAME);
		if (attribute instanceof CompanyFacade) {
			return ((CompanyFacade) attribute).getId();
		}
		return null;
	}
}
