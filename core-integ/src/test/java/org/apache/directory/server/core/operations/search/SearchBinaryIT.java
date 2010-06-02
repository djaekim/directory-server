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
package org.apache.directory.server.core.operations.search;


import static org.junit.Assert.assertEquals;

import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.message.SearchResponse;
import org.apache.directory.server.core.annotations.ApplyLdifs;
import org.apache.directory.server.core.annotations.ContextEntry;
import org.apache.directory.server.core.annotations.CreateDS;
import org.apache.directory.server.core.annotations.CreateIndex;
import org.apache.directory.server.core.annotations.CreatePartition;
import org.apache.directory.server.core.integ.AbstractLdapTestUnit;
import org.apache.directory.server.core.integ.FrameworkRunner;
import org.apache.directory.server.core.integ.IntegrationUtils;
import org.apache.directory.shared.ldap.cursor.Cursor;
import org.apache.directory.shared.ldap.filter.SearchScope;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Test the add operation performances
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$
 */
@RunWith ( FrameworkRunner.class )
@CreateDS(
        name="AddPerfDS",
        partitions =
        {
            @CreatePartition(
                name = "example",
                suffix = "dc=example,dc=com",
                contextEntry = @ContextEntry( 
                    entryLdif =
                        "dn: dc=example,dc=com\n" +
                        "dc: example\n" +
                        "objectClass: top\n" +
                        "objectClass: domain\n\n" ),
                indexes = 
                {
                    @CreateIndex( attribute = "objectClass", cacheSize = 1000 ),
                    @CreateIndex( attribute = "sn", cacheSize = 1000 ),
                    @CreateIndex(attribute = "cn", cacheSize = 1000),
                    @CreateIndex( attribute = "userCertificate", cacheSize = 1000 )
                } )
                
        },
        enableChangeLog = false )
@ApplyLdifs(
    {
        "dn: cn=testing00,ou=system",
        "objectClass: top",
        "objectClass: person", 
        "objectClass: organizationalPerson",
        "objectClass: inetOrgPerson", 
        "cn: testing00", 
        "sn: Testing 0", 
        "userCertificate:: AQIDBA==",
        "",
        "dn: cn=testing01,ou=system",
        "objectClass: top",
        "objectClass: person", 
        "objectClass: organizationalPerson",
        "objectClass: inetOrgPerson", 
        "cn: testing01", 
        "sn: Testing 1", 
        "",
        "dn: cn=testing02,ou=system",
        "objectClass: top",
        "objectClass: person", 
        "objectClass: organizationalPerson",
        "objectClass: inetOrgPerson", 
        "cn: testing02", 
        "sn: Testing 2", 
        "userCertificate:: CQoLD==",
        "",
        "dn: cn=testing03,ou=system",
        "objectClass: top",
        "objectClass: person", 
        "objectClass: organizationalPerson",
        "objectClass: inetOrgPerson", 
        "cn: testing03", 
        "sn: Testing 3", 
        "userCertificate:: AQIDBA=="
    }
    )
public class SearchBinaryIT extends AbstractLdapTestUnit
{
    /**
     * Test an add operation performance
     */
    @Test
    public void testSearchWithIndexBinaryAttribute() throws Exception
    {
        LdapConnection connection = IntegrationUtils.getAdminConnection( service );

        // Do a search with a filter based on certificate, get back all the entries
        Cursor<SearchResponse> responses = connection.search( "ou=system", "(userCertificate=*)", SearchScope.SUBTREE,
            "*" );

        int i = 0;

        while ( responses.next() )
        {
            responses.get();
            ++i;
        }

        responses.close();

        // We should have 3 entries
        assertEquals( 4, i );

        // Now, filter the entry with a cn starting with testing, and a certificate 
        responses = connection.search( "ou=system", "(&(cn=testing*)(userCertificate=*))",
            SearchScope.SUBTREE, "*" );

        i = 0;

        while ( responses.next() )
        {
            responses.get();
            ++i;
        }

        responses.close();

        // Now, only 2 entries
        assertEquals( 3, i );

        // Now, just get back the entry with a certificate equals to 0x01 0x02 0x03 0x04
        responses = connection.search( "ou=system", "(userCertificate=\\01\\02\\03\\04)", SearchScope.SUBTREE, "*" );

        i = 0;

        while ( responses.next() )
        {
            responses.get();
            ++i;
        }

        responses.close();

        assertEquals( 2, i );

        // Last, check that searching for an entry using a SUBSTR filter does not work
        responses = connection.search( "ou=system", "(userCertificate=\\01\\02*)", SearchScope.SUBTREE, "*" );

        i = 0;

        while ( responses.next() )
        {
            responses.get();
            ++i;
        }

        responses.close();

        assertEquals( 0, i );
        connection.close();
    }
}
