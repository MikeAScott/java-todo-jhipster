package com.nttdata.ta.todo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A ToDo.
 */
@Entity
@Table(name = "to_do")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ToDo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "by_date")
    private LocalDate byDate;

    @Column(name = "done")
    private Boolean done;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public ToDo description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getByDate() {
        return byDate;
    }

    public ToDo byDate(LocalDate byDate) {
        this.byDate = byDate;
        return this;
    }

    public void setByDate(LocalDate byDate) {
        this.byDate = byDate;
    }

    public Boolean isDone() {
        return done;
    }

    public ToDo done(Boolean done) {
        this.done = done;
        return this;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ToDo)) {
            return false;
        }
        return id != null && id.equals(((ToDo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ToDo{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", byDate='" + getByDate() + "'" +
            ", done='" + isDone() + "'" +
            "}";
    }
}
