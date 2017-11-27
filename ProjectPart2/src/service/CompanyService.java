package service;

import java.sql.SQLException;





import java.util.ArrayList;
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



import enums.CouponType;
import exeptions.FacadeExeptions;

import facade.CompanyFacade;

import javabeans.Coupon;





@Path("/companies")
public class CompanyService {



	@POST
	@Path("/coupons")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createCoupon(Long companyId,Coupon coupon) {
		try {
			CompanyFacade.createCoupon(coupon, companyId);
			return Response
					.status(Status.OK)
					.type(MediaType.APPLICATION_JSON)
					.build();
		} catch (Exception e) {
			throw new CouponSystemWebException("CompanyService: " + e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
	}

	@DELETE
	@Path("/coupons")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeCoupon(Long couponId,Coupon coupon) throws SQLException {
		try {
			CompanyFacade.removeCoupon(coupon);
			return Response.status(Status.OK)
					.type(MediaType.APPLICATION_JSON)
					.build();
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.BAD_REQUEST);
		}
	}

	@PUT
	@Path("/coupons")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCoupon(Coupon coupon) throws SQLException {
		try {
			CompanyFacade.updateCoupon(coupon);
			return Response
					.status(Status.OK)
					.type(MediaType.APPLICATION_JSON)
					.build();
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
			if (coupon.getTitle() == null)
				throw new SQLException();			
			return Response.status(Status.OK).entity(coupon).build();
		} catch (SQLException e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		} 


	}

	@POST
	@Path("/coupons")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCoupons(long companyId)   {
		Collection<Coupon> list = new ArrayList<>();
		try {
			list = CompanyFacade.getAllCoupons(companyId);
			return Response
					.status(Status.OK)
					.entity(list)
					.type(MediaType.APPLICATION_JSON)
					.build();
		} catch (FacadeExeptions e) {
			throw new CouponSystemWebException(e.getMessage(),Status.INTERNAL_SERVER_ERROR);
		}


	}

	@POST
	@Path("/coupons/type/{type}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getCouponByType(long companyId,@PathParam("type") String couponType)  {
		Collection<Coupon> couponsByType = new ArrayList<>();
		try {
			CouponType myType = CouponType.valueOf(couponType);
			couponsByType = CompanyFacade.getCouponByType(companyId,myType);
			if (couponsByType.size() == 0)
				return Response
						.status(Status.INTERNAL_SERVER_ERROR)
						.entity(couponsByType)
						.type(MediaType.APPLICATION_JSON)
						.build();
			return Response
					.status(Status.OK)
					.entity(couponsByType)
					.build();
		}  catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
	}

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(GenericUser company) {
		CompanyFacade facade = new CompanyFacade();
		try {
			facade = (CompanyFacade) facade.login(company.getName(), company.getPassword());

			return Response
					.status(Status.OK)
					.build();
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
	}


	@POST
	@Path("/logoutcompany")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response logOut(@Context HttpServletRequest req, @Context HttpServletResponse res) throws CouponSystemWebException {
		HttpSession session = (HttpSession) req.getSession(false);
		try {
			session.removeAttribute("facade");
			session.invalidate();
			return Response
					.status(Status.OK)
					.build();
					
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
	}
}

