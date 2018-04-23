package com.thenorthw.tc.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by theNorthW on $date.
 * blog: thenorthw.com
 * 在本应用中，权限验证交给后面service
 * @autuor : theNorthW
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminAllowed {
}
