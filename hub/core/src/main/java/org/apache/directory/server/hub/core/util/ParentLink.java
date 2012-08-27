/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */

package org.apache.directory.server.hub.core.util;


import org.apache.directory.server.hub.api.component.DirectoryComponent;


public class ParentLink
{
    private DirectoryComponent parent;
    private String linkPoint;


    public ParentLink( DirectoryComponent parent, String linkPoint )
    {
        this.parent = parent;
        this.linkPoint = linkPoint;
    }


    public DirectoryComponent getParent()
    {
        return parent;
    }


    public String getLinkPoint()
    {
        return linkPoint;
    }


    @Override
    public boolean equals( Object obj )
    {
        if ( !( obj instanceof ParentLink ) )
        {
            return false;
        }

        ParentLink pl = ( ParentLink ) obj;

        return parent.equals( pl.getParent() ) && linkPoint.equals( pl.getLinkPoint() );
    }


    @Override
    public int hashCode()
    {
        return ( parent.getComponentPID() + linkPoint ).hashCode();
    }
}