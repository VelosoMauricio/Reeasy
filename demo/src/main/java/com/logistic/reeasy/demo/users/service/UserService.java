package com.logistic.reeasy.demo.users.service;

import com.logistic.reeasy.demo.common.exception.BaseApiException;
import com.logistic.reeasy.demo.redeem.service.RedeemCouponService;
import com.logistic.reeasy.demo.users.dto.UserDto;
import com.logistic.reeasy.demo.users.iface.iUserDAO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {
    private iUserDAO userDAO;

    public UserService(iUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserDto findUserById(Long userId){

        try{
            var response = userDAO.findById(userId, "user_id");

            return new UserDto(
                    response.getUser_id(),
                    response.getName() + " " + response.getLastname(),
                    response.getEmail(),
                    response.getAddress(),
                    response.getPoints()
            );

        }
        catch (Exception e){
            log.error("ERROR ON findUserById {}: ", userId, e);
            throw new RuntimeException(e.getMessage());
        }

    }

    public void substractPoints(Long userId, int pointsToSubstract){
        try{
            log.info("Attempting to substract {} points from user with id {}", pointsToSubstract, userId);
            findUserById(userId);
            userDAO.substractPoints(userId, pointsToSubstract);
            log.info("Successfully substracted {} points from user with id {}", pointsToSubstract, userId);
        }
        catch (BaseApiException e){
            log.error("API Error substracting {} point on user {}: ", pointsToSubstract,  userId, e);
            throw e;
        }
        catch (Exception e){
            log.error("Error substracting {} point on user {}: ", pointsToSubstract,  userId, e);
            throw new RuntimeException(e.getMessage());
        }
    }

}
