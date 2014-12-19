/*
 * Copyright 2004-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.groovy.grails.orm.hibernate.exceptions;


import org.grails.core.exceptions.GrailsException;

/**
 * Base exception class for errors related to Hibernate configuration in Grails.
 *
 * @author Steven Devijver
 */
public abstract class GrailsHibernateException extends GrailsException {

    private static final long serialVersionUID = -6019220941440364736L;

    public GrailsHibernateException() {
        super();
    }

    public GrailsHibernateException(String message) {
        super(message);
    }

    public GrailsHibernateException(String message, Throwable cause) {
        super(message, cause);
    }

    public GrailsHibernateException(Throwable cause) {
        super(cause);
    }
}