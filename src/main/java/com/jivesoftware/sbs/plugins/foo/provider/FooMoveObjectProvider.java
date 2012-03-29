package com.jivesoftware.sbs.plugins.foo.provider;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.jivesoftware.base.UnauthorizedException;
import com.jivesoftware.community.JiveContainer;
import com.jivesoftware.community.RejectedException;
import com.jivesoftware.community.objecttype.MoveObjectProvider;
import com.jivesoftware.sbs.plugins.foo.Foo;
import com.jivesoftware.sbs.plugins.foo.FooManager;

public class FooMoveObjectProvider implements MoveObjectProvider<Foo> {

    private static final Logger log = Logger.getLogger(FooMoveObjectProvider.class);

    private FooManager fooManager;

    @Override
    public MoveValidationResult canMove(Foo toMove, JiveContainer target) {
        return new MoveValidationResult(true, null);
    }

    @Override
    public Foo move(Foo toMove, JiveContainer target) throws UnauthorizedException, RejectedException {
        log.debug("Moving foo " + toMove.getID() + " to container " + target);
        return fooManager.moveFoo(toMove, target);
    }

    @Required
    public void setFooManager(FooManager fooManager) {
        this.fooManager = fooManager;
    }
}
