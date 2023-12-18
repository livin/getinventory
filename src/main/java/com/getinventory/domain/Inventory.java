package com.getinventory.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Inventory.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inventory")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Inventory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Setter
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @JsonIgnoreProperties(value = { "inventory" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "inventory")
    private Reservation reservation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Inventory id(Long id) {
        this.setId(id);
        return this;
    }

    public Inventory name(String name) {
        this.setName(name);
        return this;
    }

    public void setReservation(Reservation reservation) {
        if (this.reservation != null) {
            this.reservation.setInventory(null);
        }
        if (reservation != null) {
            reservation.setInventory(this);
        }
        this.reservation = reservation;
    }

    public Inventory reservation(Reservation reservation) {
        this.setReservation(reservation);
        return this;
    }

    @JsonProperty("available")
    boolean isAvailable() {
        return getReservation() == null;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Inventory)) {
            return false;
        }
        return getId() != null && getId().equals(((Inventory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Inventory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
