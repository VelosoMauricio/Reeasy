package com.logistic.reeasy.demo.Coupons.dao;


import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import com.logistic.reeasy.demo.Coupons.models.CouponModel;
import com.logistic.reeasy.demo.Coupons.iface.AdminDAO;

@Repository
public class AdminDAOimpl implements AdminDAO{

    private Sql2o sql2o;

    public AdminDAOimpl(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public CouponModel addCoupon(CouponModel coupon){
        
        final String sql = "INSERT INTO Coupons (coupon_id, expiration_date, price, description, amount, link, image, Enterprise_cuit) VALUES (:coupon_id, :expiration_date, :price, :description, :amount, :link, :image, :Enterprise_cuit)";


        try(var con = sql2o.open()) {
            con.createQuery(sql)
                .addParameter("coupon_id", coupon.coupon_id)
                .addParameter("expiration_date", coupon.expiration_date)
                .addParameter("price", coupon.price)
                .addParameter("description", coupon.description)
                .addParameter("amount", coupon.amount)
                .addParameter("link", coupon.link)
                .addParameter("image", coupon.image)
                .addParameter("Enterprise_cuit", coupon.Enterprise_cuit)
                .executeUpdate();

                return coupon;
        } catch (Exception e) {
                e.printStackTrace();
                  throw new RuntimeException("No se pudo agregar el cupón a la base de datos", e);
        }
    }
    
}