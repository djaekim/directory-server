/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */
package org.apache.directory.server.schema.registries;


import java.util.Iterator;

import javax.naming.NamingException;

import org.apache.directory.shared.ldap.schema.Syntax;


/**
 * Manages the lookup and registration of Syntaxes within the system by OID.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$
 */
public interface SyntaxRegistry extends SchemaObjectRegistry, Iterable<Syntax>
{
    /**
     * Looks up a Syntax by its unique Object Identifier or by name.
     * 
     * @param id the object identifier or name
     * @return the Syntax for the id
     * @throws NamingException if there is a backing store failure or the Syntax
     * does not exist.
     */
    Syntax lookup( String id ) throws NamingException;


    /**
     * Registers a Syntax with this registry.  
     * 
     * @param syntax the Syntax to register
     * @throws NamingException if the syntax is already registered or the 
     * registration operation is not supported
     */
    void register( Syntax syntax ) throws NamingException;


    /**
     * Checks to see if a Syntax exists.  Backing store failures simply return
     * false.
     * 
     * @param id the object identifier or name
     * @return true if a Syntax definition exists for the id, false otherwise
     */
    boolean hasSyntax( String id );


    /**
     * Lists all the Syntaxes within this registry.
     *
     * @return an Iterator over all the Syntaxes within this registry
     */
    Iterator<Syntax> iterator();
}
