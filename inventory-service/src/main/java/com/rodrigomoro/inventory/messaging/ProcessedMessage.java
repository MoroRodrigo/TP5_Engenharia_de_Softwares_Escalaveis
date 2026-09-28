package com.rodrigomoro.inventory.messaging;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="processed_messages", indexes={@Index(name="uk_inventory_processed_event",columnList="event_id",unique=true)})
public class ProcessedMessage {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="event_id",nullable=false,unique=true,length=36) private String eventId;
    @Column(name="processed_at",nullable=false) private LocalDateTime processedAt;
    protected ProcessedMessage(){}
    public ProcessedMessage(String eventId){this.eventId=eventId;this.processedAt=LocalDateTime.now();}
    public String getEventId(){return eventId;} public LocalDateTime getProcessedAt(){return processedAt;}
}
