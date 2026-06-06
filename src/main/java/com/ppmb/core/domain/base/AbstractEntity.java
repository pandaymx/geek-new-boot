package com.ppmb.core.domain.base;

import jakarta.persistence.MappedSuperclass;
import java.util.Objects;
import org.hibernate.proxy.HibernateProxy;

/**
 * A thin base class for entities, strictly handling entity identity via equality and hash code. It
 * does NOT define any business properties, leaving that to composition (e.g., AuditInfo).
 */
@MappedSuperclass
public abstract class AbstractEntity<ID> implements Identifiable<ID> {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }

        // Handle Hibernate proxies correctly when comparing classes
        Class<?> oEffectiveClass =
                o instanceof HibernateProxy
                        ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
                        : o.getClass();
        Class<?> thisEffectiveClass =
                this instanceof HibernateProxy
                        ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
                        : this.getClass();

        if (thisEffectiveClass != oEffectiveClass) {
            return false;
        }

        AbstractEntity<?> that = (AbstractEntity<?>) o;

        // If both IDs are null, they are considered different entities unless they are the same
        // instance
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        // Return a constant hash code or use the effective class's hash code
        // to prevent hash code changes before and after entity persistence
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this)
                        .getHibernateLazyInitializer()
                        .getPersistentClass()
                        .hashCode()
                : getClass().hashCode();
    }
}
