package edu.unsj.fcefn.lcc.optimizacion.api.model.entities;

import edu.unsj.fcefn.lcc.optimizacion.api.model.domain.StopDTO;
import edu.unsj.fcefn.lcc.optimizacion.api.model.domain.TransportCompanyDTO;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalTime;

@Entity
@Table(name = "frames")
public class FrameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "id_transport_company")
    private TransportCompanyDTO id_transport_company;

    @Column(name = "id_stop_departure")
    private StopDTO id_stop_departure;

    @Column(name = "id_stop_arrival")
    private StopDTO id_stop_arrival;

    @Column(name = "price")
    private Float price;

    @Column(name = "category")
    private String category;

    @Column(name = "departure_datetime")
    private LocalTime departure_datetime;

    @Column(name = "arrival_datetime")
    private LocalTime arrival_datetime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TransportCompanyDTO getId_transport_company() {
        return id_transport_company;
    }

    public void setId_transport_company(TransportCompanyDTO id_transport_company) {
        this.id_transport_company = id_transport_company;
    }

    public StopDTO getId_stop_departure() {
        return id_stop_departure;
    }

    public void setId_stop_departure(StopDTO id_stop_departure) {
        this.id_stop_departure = id_stop_departure;
    }

    public StopDTO getId_stop_arrival() {
        return id_stop_arrival;
    }

    public void setId_stop_arrival(StopDTO id_stop_arrival) {
        this.id_stop_arrival = id_stop_arrival;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalTime getDeparture_datetime() {
        return departure_datetime;
    }

    public void setDeparture_datetime(LocalTime departure_datetime) {
        this.departure_datetime = departure_datetime;
    }

    public LocalTime getArrival_datetime() {
        return arrival_datetime;
    }

    public void setArrival_datetime(LocalTime arrival_datetime) {
        this.arrival_datetime = arrival_datetime;
    }

}
