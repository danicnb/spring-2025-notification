package edu.uoc.epcsd.notification.application.kafka;

import edu.uoc.epcsd.notification.domain.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class KafkaClassListener {

    private final NotificationService notificationService;

    @KafkaListener(topics = KafkaConstants.PRODUCT_TOPIC + KafkaConstants.SEPARATOR + KafkaConstants.UNIT_AVAILABLE, groupId = "group-1")
    void productAvailable(ProductMessage productMessage) {
        log.trace("productAvailable");

        notificationService.notifyProductAvailable(productMessage);
    }
}
