/*
 *   Copyright 2004 The Apache Software Foundation
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package org.apache.ldap.server.protocol;


import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.ldap.InitialLdapContext;

import org.apache.ldap.common.exception.LdapException;
import org.apache.ldap.common.message.LdapResultImpl;
import org.apache.ldap.common.message.ModifyRequest;
import org.apache.ldap.common.message.ModifyResponse;
import org.apache.ldap.common.message.ModifyResponseImpl;
import org.apache.ldap.common.message.ResultCodeEnum;
import org.apache.ldap.common.util.ExceptionUtils;
import org.apache.mina.protocol.ProtocolSession;


/**
 * A single reply handler for {@link org.apache.ldap.common.message.ModifyRequest}s.
 *
 * @author <a href="mailto:directory-dev@incubator.apache.org">Apache Directory Project</a>
 * @version $Rev$
 */
public class ModifyHandler implements CommandHandler
{
    private static final ModificationItem[] EMPTY = new ModificationItem[0];

    public void handle( ProtocolSession session, Object request )
    {
        ModifyRequest req = ( ModifyRequest ) request;
        ModifyResponse resp = new ModifyResponseImpl( req.getMessageId() );
        resp.setLdapResult( new LdapResultImpl( resp ) );

        try
        {
            InitialLdapContext ictx = SessionRegistry.getSingleton()
                    .getInitialLdapContext( session, null, true );
            DirContext ctx = ( DirContext ) ictx.lookup( "" );
            Object[] mods = req.getModificationItems().toArray( EMPTY );
            ctx.modifyAttributes( req.getName(), ( ModificationItem[] ) mods );
        }
        catch ( NamingException e )
        {
            String msg = "failed to add entry " + req.getName() + ":\n";
            msg += ExceptionUtils.getStackTrace( e );
            ResultCodeEnum code;

            if ( e instanceof LdapException )
            {
                code = ( ( LdapException ) e ).getResultCode() ;
            }
            else
            {
                code = ResultCodeEnum.getBestEstimate( e, req.getType() );
            }

            resp.getLdapResult().setResultCode( code );
            resp.getLdapResult().setErrorMessage( msg );

            if ( e.getResolvedName() != null )
            {
                resp.getLdapResult().setMatchedDn( e.getResolvedName().toString() );
            }

            session.write( resp );
            return;
        }

        resp.getLdapResult().setResultCode( ResultCodeEnum.SUCCESS );
        resp.getLdapResult().setMatchedDn( req.getName() );
        session.write( resp );
        return;
    }
}
