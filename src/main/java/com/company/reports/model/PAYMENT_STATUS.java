package com.company.reports.model;

public enum PAYMENT_STATUS {
    PENDING(1),
    PAID(2),
    OVERDUE(3),
    PARTIALLY_PAID(4),
    REFUNDED(5),
    FAILED(6),
    PROCESSING(7),
    CANCELLED(8);

    int statusId;

    private PAYMENT_STATUS(int statusId) {
        this.statusId = statusId;
    }

    public int getId() {
        return statusId;
    }
}
