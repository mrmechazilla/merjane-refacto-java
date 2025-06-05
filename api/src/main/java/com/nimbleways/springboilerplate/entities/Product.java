package com.nimbleways.springboilerplate.entities;

import com.nimbleways.springboilerplate.entities.enums.ProductType;
import lombok.*;

import java.time.LocalDate;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "lead_time")
    private Integer leadTime;

    @Column(name = "available")
    private Integer available;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ProductType type;

    @Column(name = "name")
    private String name;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "season_start_date")
    private LocalDate seasonStartDate;

    @Column(name = "season_end_date")
    private LocalDate seasonEndDate;

    public boolean isAvailable() {
        return this.available != null && this.available > 0;
    }

    public boolean isExpired() {
        return this.expiryDate != null && this.expiryDate.isBefore(LocalDate.now());
    }

    public boolean isInSeason(LocalDate date) {
        return this.seasonStartDate != null &&
                this.seasonEndDate != null &&
                !date.isBefore(seasonStartDate) &&
                !date.isAfter(seasonEndDate);
    }

    public void decrementStock() {
        if (this.isAvailable()) {
            this.available -= 1;
        }
    }

    public void markUnavailable() {
        this.available = 0;
    }
}
