package edu.uoc.epcsd.notification.domain.service;

import edu.uoc.epcsd.notification.application.kafka.ProductMessage;
import edu.uoc.epcsd.notification.application.rest.dtos.GetProductResponse;
import edu.uoc.epcsd.notification.application.rest.dtos.GetUserResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Log4j2
@Service
public class NotificationServiceImpl implements NotificationService {

    @Value("${userService.getUsersToAlert.url}")
    private String userServiceUrl;

    @Value("${productService.getProductDetails.url}")
    private String productServiceUrl;

    /**
     * Notifies users that a product item is now available by retrieving their alerts from the User microservice.
     *
     * @param productMessage contains the product ID of the product that has become available
     * @throws RuntimeException logged as an error if the HTTP request to the User microservice fails
     *
     * This method builds a GET request to the User microservice to obtain the list of users
     * who have alerts for the specified product and current date. If users are returned, it simulates
     * sending an email notification by logging the action with INFO level for each user.
     */
    @Override
    public void notifyProductAvailable(ProductMessage productMessage) {
        log.info("notifyProductAvailable - productId={}, date={}", productMessage.getProductId(), LocalDate.now());

        String url = userServiceUrl
                .replace("{productId}", String.valueOf(productMessage.getProductId()))
                .replace("{availableOnDate}", LocalDate.now().toString()); // format ISO

        try {
            RestTemplate restTemplate = new RestTemplate();

            GetUserResponse[] users = restTemplate.getForObject(url, GetUserResponse[].class);

            if (users != null && users.length > 0) {
                for (GetUserResponse user : users) {
                    log.info("Sending an email to user {}", user.getFullName());
                }
            } else {
                log.info("No users to notify for product {}", productMessage.getProductId());
            }

        } catch (Exception e) {
            log.error("Error notifying users for product {}", productMessage.getProductId(), e);
        }
    }
}