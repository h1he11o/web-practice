package com.example.jbnu.osj.demo.api.dto;

public class ItemDto {
    private Long id;
    private String sender;
    private String item;
    private String origin;
    private String receiver;
    private int quantity;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    public String getItem() { return item; }
    public void setItem(String item) { this.item = item; }
    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }
    public String getReceiver() { return receiver; }
    public void setReceiver(String receiver) { this.receiver = receiver; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}