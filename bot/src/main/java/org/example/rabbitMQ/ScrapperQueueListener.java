package org.example.rabbitMQ;

import org.example.dto.UpdateRequest;
import org.example.service.UpdateHandler;
import org.example.service.UpdateRequestHandler;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@RabbitListener(queues = "${app.rabbitMQConfig.queueName}")
public class ScrapperQueueListener implements UpdateHandler {
    private final UpdateRequestHandler updateRequestHandler;
    public ScrapperQueueListener(UpdateRequestHandler updateRequestHandler) {
        this.updateRequestHandler = updateRequestHandler;
    }

    @RabbitHandler
    @Override
    public void getUpdates(UpdateRequest updateRequest){
        updateRequestHandler.handleUpdateRequest(updateRequest);
    }
}
