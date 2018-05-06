package dbs.db;

import javax.persistence.EntityManager;

public abstract class AbstractProvider {
    protected final EntityManager entityManager;

    public AbstractProvider(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
