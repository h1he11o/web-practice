package com.example.jbnu.osj.demo.api.request;

public class ItemUpdateRequest {
    private String sender;
    private String item;
    private String origin;
    private String receiver;
    private Integer quantity;

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getItem() { return item; }
    public void setItem(String item) { this.item = item; }

    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }

    public String getReceiver() { return receiver; }
    public void setReceiver(String receiver) { this.receiver = receiver; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
