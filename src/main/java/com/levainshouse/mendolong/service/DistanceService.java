package com.levainshouse.mendolong.service;

import com.levainshouse.mendolong.entity.Address;
import com.levainshouse.mendolong.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DistanceService {

    public List<User> getValidAddressesBetweenRange(
            List<Address> addresses, List<Address> oppositeAddresses, int range){
        List<User> validUsers = new ArrayList<>();
        for (Address address : addresses) {
            for (Address oppositeAddress : oppositeAddresses) {
                double distance = this.getDistance(
                        address.getLatitude(), address.getLongitude(),
                        oppositeAddress.getLatitude(), oppositeAddress.getLongitude());

                if(!validUsers.contains(oppositeAddress.getUser()) && distance <= range){
                    validUsers.add(oppositeAddress.getUser());
                }
            }
        }

        return validUsers;
    }

    private double getDistance(double lat1, double lon1, double lat2, double lon2){
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        // KM 단위이므로 단위를 맞춰준다
        dist = dist * 1.609344;

        return (dist);
    }

    // This function converts decimal degrees to radians
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
