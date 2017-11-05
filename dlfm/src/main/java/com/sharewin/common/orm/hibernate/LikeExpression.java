package com.sharewin.common.orm.hibernate;

import org.hibernate.criterion.MatchMode;

/**
 * QBC like查询表达式.
 */
@SuppressWarnings("serial")
public class LikeExpression extends org.hibernate.criterion.LikeExpression {

	protected LikeExpression(String propertyName, String value,
			MatchMode matchMode, Character escapeChar, boolean ignoreCase) {
		super(propertyName, value, matchMode, escapeChar, ignoreCase);
	}

}