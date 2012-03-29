package com.jivesoftware.sbs.plugins.foo;

import com.jivesoftware.retention.listeners.specialist.impl.AbstractSpecialist;

public class FooRetentionSpecialist extends AbstractSpecialist {

	@Override
	public int getTargetObjectType() {
		return Math.abs("foo".hashCode());
	}

	@Override
	protected String getContentType() {
		return "Foo";
	}

}
