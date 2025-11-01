package com.logistic.reeasy.demo.users.service;

import com.logistic.reeasy.demo.common.exception.BaseApiException;
import com.logistic.reeasy.demo.users.dto.UserDto;
import com.logistic.reeasy.demo.users.iface.iUserDAO;
import org.springframework.stereotype.Service;

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
            throw new RuntimeException(e.getMessage());
        }

    }

    public void substractPoints(Long userId, int pointsToSubstract){
        try{
            findUserById(userId);
            userDAO.substractPoints(userId, pointsToSubstract);
        }
        catch (BaseApiException e){
            throw e;
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

}
