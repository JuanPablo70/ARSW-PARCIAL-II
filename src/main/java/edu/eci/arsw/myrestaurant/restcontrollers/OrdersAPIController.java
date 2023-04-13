/*
 * Copyright (C) 2016 Pivotal Software, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.eci.arsw.myrestaurant.restcontrollers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import edu.eci.arsw.myrestaurant.model.Order;
import edu.eci.arsw.myrestaurant.model.ProductType;
import edu.eci.arsw.myrestaurant.model.RestaurantProduct;
import edu.eci.arsw.myrestaurant.services.RestaurantOrderServices;
import edu.eci.arsw.myrestaurant.services.RestaurantOrderServicesStub;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author hcadavid
 */

@RestController
@RequestMapping(value = "/orders")
public class OrdersAPIController {

    @Autowired
    RestaurantOrderServices service;

    @RequestMapping(method = RequestMethod.GET, produces="application/json")
    public ResponseEntity<?> handlerGetOrdersResource() {
        try {
            Set<Integer> orders = service.getTablesWithOrders();
            Gson gson = new Gson();
            JsonObject json = new JsonObject();
            JsonObject json1;
            for (int order : orders) {
                Set<String> products = service.getTableOrder(order).getOrderedDishes();
                json1 = new JsonObject();
                json1.addProperty("table", order);
                json1.addProperty("products", products.toString());
                json1.addProperty("total", service.calculateTableBill(order));
                json.add("order" + order,json1);
            }
            return new ResponseEntity<>(gson.toJson(json), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
}
