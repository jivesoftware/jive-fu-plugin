package com.jivesoftware.sbs.plugins.foo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.jivesoftware.base.event.v2.BaseJiveEvent;
import com.jivesoftware.retention.ActionType;
import com.jivesoftware.retention.listeners.builders.filter.AbstractDefaultEventFilter;


public class FooRetentionEventFilter extends AbstractDefaultEventFilter {

	private static final Map<Integer, ActionType> actionTypeMap;

	static {
		actionTypeMap = new HashMap<Integer, ActionType>();

		actionTypeMap.put(0, ActionType.CREATE);
		actionTypeMap.put(1, ActionType.DELETE);
		actionTypeMap.put(2, ActionType.DELETING);
		actionTypeMap.put(3, ActionType.EDIT);
		actionTypeMap.put(4, ActionType.RATE);
		actionTypeMap.put(5, ActionType.VIEW);
		actionTypeMap.put(6, ActionType.MOVE);
		actionTypeMap.put(7, ActionType.BOOKMARK);
		actionTypeMap.put(8, ActionType.MODERATE);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public boolean shouldProcess(BaseJiveEvent baseJiveEvent) {
		if (!super.shouldProcess(baseJiveEvent)) {
			return false;
		}

		if (isWrongType(baseJiveEvent)) {
			return false;
		}

		int eventType = baseJiveEvent.getEventType();
		return eventType == 0
			|| eventType == 3;
	}

	@SuppressWarnings("rawtypes")
	private boolean isWrongType(BaseJiveEvent baseJiveEvent) {
		String className = baseJiveEvent.getClass().getName();
		return !className.endsWith("FooEvent");
	}

	@Override
	protected Map<Integer, ActionType> getEventTypeToActionTypeMap() {
		return Collections.unmodifiableMap(actionTypeMap);
	}

}
