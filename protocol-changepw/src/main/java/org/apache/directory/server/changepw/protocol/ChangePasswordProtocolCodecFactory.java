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

package org.apache.directory.server.changepw.protocol;


import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;


/**
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
public class ChangePasswordProtocolCodecFactory implements ProtocolCodecFactory
{
    private static final ChangePasswordProtocolCodecFactory INSTANCE = new ChangePasswordProtocolCodecFactory();


    /**
     * Returns the singleton instance of {@link ChangePasswordProtocolCodecFactory}.
     *
     * @return The singleton instance of {@link ChangePasswordProtocolCodecFactory}.
     */
    public static ChangePasswordProtocolCodecFactory getInstance()
    {
        return INSTANCE;
    }


    private ChangePasswordProtocolCodecFactory()
    {
        // Private constructor prevents instantiation outside this class.
    }


    public ProtocolEncoder getEncoder()
    {
        // Create a new encoder.
        return new ChangePasswordEncoder();
    }


    public ProtocolDecoder getDecoder()
    {
        // Create a new decoder.
        return new ChangePasswordDecoder();
    }
}
