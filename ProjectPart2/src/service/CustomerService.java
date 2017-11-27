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



	@POST
	@Path("/coupons")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response purchaseCoupon(long companyId,Coupon coupon) {

		try {
			CustomerFacade.purchaseCoupon(companyId,coupon);
			return Response
					.status(Status.OK)
					.type(MediaType.APPLICATION_JSON)
					.build();
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
	}

	@POST
	@Path("/coupons")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllPurchasedCoupons(long companyId)  {
		Collection<Coupon> list = new ArrayList<>();
		try {
			list = CustomerFacade.getAllPurchasedCoupons(companyId);
			return Response
					.status(Status.OK)
					.entity(list)
					.build();
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}

	}

	@POST
	@Path("/coupons/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllPurchasedCouponsByType(long companyId,@PathParam("type") String type)  {
		Collection<Coupon> list = new ArrayList<>();
		try {
			CouponType c = CouponType.valueOf(type);
			list = CustomerFacade.getAllPurchasedCouponsByType(companyId,c);
			return Response
					.status(Status.OK)
					.entity(list)
					.build();
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}

	}

	@POST
	@Path("/coupons/price/{price}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllPurchasedCouponsByPrice(long companyId,@PathParam("price") double couponPrice)  {
		Collection<Coupon> list = new ArrayList<>();
		try {
			list = CustomerFacade.getAllPurchasedCouponsByPrice(companyId,couponPrice);

			return Response
					.status(Status.OK)
					.entity(list)
					.build();
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
	}

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(GenericUser customer) {
		CustomerFacade facade = new CustomerFacade();
		try {
			facade = (CustomerFacade) facade.login(customer.getName(), customer.getPassword());
			return Response
					.status(Status.OK)
					.build();
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	@POST
	@Path("/logoutcustomer")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response logOutCustomer(@Context HttpServletRequest req, @Context HttpServletResponse res) throws CouponSystemWebException {
		HttpSession session = (HttpSession) req.getSession(false);
		try {
			session.removeAttribute("facade");
			session.invalidate();
			//redirect!!!
			return Response
					.status(Status.OK)
					.build();
					
		} catch (Exception e) {
			throw new CouponSystemWebException(e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
	}
}
