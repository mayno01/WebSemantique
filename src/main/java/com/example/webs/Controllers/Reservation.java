package com.example.webs.Controllers;



public class Reservation {
    private String reservationId;
    private String reservationType;
    private String reservationDate;
    private String reservationDescription;
    private String reservationFeedback;

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getReservationType() {
        return reservationType;
    }

    public void setReservationType(String reservationType) {
        this.reservationType = reservationType;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getReservationDescription() {
        return reservationDescription;
    }

    public void setReservationDescription(String reservationDescription) {
        this.reservationDescription = reservationDescription;
    }

    public String getReservationFeedback() {
        return reservationFeedback;
    }

    public void setReservationFeedback(String reservationFeedback) {
        this.reservationFeedback = reservationFeedback;
    }

    public Reservation() {
    }
}
