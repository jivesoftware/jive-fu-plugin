package com.jivesoftware.sbs.plugins.foo;

import java.util.Map;

import com.jivesoftware.base.event.ContentEvent;
import com.jivesoftware.community.ContainerAwareEntityDescriptor;
import com.jivesoftware.community.JiveContentObject.Status;

public class FooEvent extends ContentEvent<FooEvent.Type, ContainerAwareEntityDescriptor> {

    private Status status = null;

    public FooEvent() {
        super();
    }

    public FooEvent(Type eventType, ContainerAwareEntityDescriptor payload, long actorID, Status status) {
        super(eventType, payload, actorID);
        this.status = status;
    }

    public FooEvent(Type eventType, ContainerAwareEntityDescriptor payload, Map<String, ?> params, long actorID, Status status) {
        super(eventType, payload, params, actorID);
        this.status = status;
    }

    public FooEvent(Type eventType, ContainerAwareEntityDescriptor payload, Map<String, ?> params, Status status) {
        super(eventType, payload, params);
        this.status = status;
    }

    public FooEvent(Type eventType, ContainerAwareEntityDescriptor payload) {
        super(eventType, payload);
    }

    public enum Type {
        CREATED, DELETED, DELETING, MODIFIED, RATED, VIEWED, MOVED, MODERATED;
    }

    @Override
    public ModificationType getContentModificationType() {
        switch (this.getType()) {
        case CREATED:
            return ModificationType.Create;
        case DELETED:
            return ModificationType.Deleted;
        case DELETING:
            return ModificationType.Deleting;
        case MODIFIED:
            return ModificationType.Modify;
        case VIEWED:
            return ModificationType.View;
        case RATED:
            return ModificationType.Rate;
        case MOVED:
            return ModificationType.Move;
        case MODERATED:
            return ModificationType.Moderate;
        }

        throw new IllegalArgumentException("No content modification type registered for " + this.getType());
    }

    @Override
    public Status getStatus() {
        return status;
    }
}
