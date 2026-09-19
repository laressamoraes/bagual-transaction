package com.laressa.transaction.event;

import com.laressa.transaction.domain.Transaction;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TransactionEventPublisher {

    private static final String TOPIC = "transacoes";

    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;

    public TransactionEventPublisher(KafkaTemplate<String, TransactionEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(Transaction transaction) {
        TransactionEvent event = new TransactionEvent(
                transaction.getTransactionId(),
                transaction.getTransactionType().name(),
                transaction.getOriginAccountId(),
                transaction.getDestinationAccountId(),
                transaction.getAmount(),
                transaction.getTransactionStatus().name()
        );

        kafkaTemplate.send(TOPIC, transaction.getTransactionId().toString(), event);
    }
}