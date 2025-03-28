package edu.uoc.epcsd.notification.domain.service;

import edu.uoc.epcsd.notification.application.kafka.ProductMessage;

public interface NotificationService {
    void notifyProductAvailable(ProductMessage productMessage);
}
