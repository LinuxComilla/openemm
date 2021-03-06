/*********************************************************************************
 * The contents of this file are subject to the Common Public Attribution
 * License Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.openemm.org/cpal1.html. The License is based on the Mozilla
 * Public License Version 1.1 but Sections 14 and 15 have been added to cover
 * use of software over a computer network and provide for limited attribution
 * for the Original Developer. In addition, Exhibit A has been modified to be
 * consistent with Exhibit B.
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is OpenEMM.
 * The Original Developer is the Initial Developer.
 * The Initial Developer of the Original Code is AGNITAS AG. All portions of
 * the code written by AGNITAS AG are Copyright (c) 2007 AGNITAS AG. All Rights
 * Reserved.
 * 
 * Contributor(s): AGNITAS AG. 
 ********************************************************************************/

package org.agnitas.util;

import java.util.Iterator;

import org.apache.commons.collections.map.LRUMap;

/**
 *
 * @author  mhe
 */
public class TimeoutLRUMap implements java.io.Serializable {

    private static final long serialVersionUID = -1755144418309829988L;

	/**
     * Holds value of property timeout.
     */
    private long timeout;

    private LRUMap internalMap;

    /**
     * Creates a new instance of TimeoutLRUMap
     */
    public TimeoutLRUMap() {
        timeout=5000; // Milliseconds
        internalMap=new LRUMap(1000);
    }

    /**
     * Creates a new instance of TimeoutLRUMap
     *
     * @param capacity
     * @param to
     */
    public TimeoutLRUMap(int capacity, long to) {
        this.setTimeout(to);
        internalMap=new LRUMap(capacity);
    }

    /**
     * Saves a key with value and timestamp.
     */
    public synchronized Object put(Object key, Object value) {
        synchronized(internalMap) {
            TimeoutObject aObject=null;
            if(!internalMap.containsKey(key)) {
                aObject=new TimeoutObject();
                aObject.aObject=value;
                aObject.timestamp=System.currentTimeMillis();
                internalMap.put(key, aObject);
            }
            return key;
        }
    }

    /**
     * Gets the value from a key
     */
    public synchronized Object get(Object key) {
        synchronized(internalMap) {
            TimeoutObject aObject=(TimeoutObject)internalMap.get(key);
            if(aObject!=null) {
                if(System.currentTimeMillis()<aObject.timestamp+this.timeout) {
                    return aObject.aObject;
                } else {
                    internalMap.remove(key);
                }
            }
            return null;
        }
    }

    /**
     * Removes unused objects.
     */
    public int cleanupGarbage() {
        int num=0;
        TimeoutObject aObject=null;
        String key=null;
        long time=System.currentTimeMillis();

        synchronized(internalMap) {
            Iterator aIt=internalMap.keySet().iterator();
            while(aIt.hasNext()) {
                key=(String)aIt.next();
                aObject=(TimeoutObject)internalMap.get(key);
                if(time>=aObject.timestamp+this.timeout) {
                    num++;
                }
            }
        }
        return num;
    }

    /**
     * Getter for property timeout.
     *
     * @return Value of property timeout.
     *
     */
    public long getTimeout() {
        return this.timeout;
    }

    /**
     * Setter for property timeout.
     *
     * @param timeout New value of property timeout.
     *
     */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    private class TimeoutObject {
        public Object aObject;
        public long timestamp;
    }

    public int size() {
        synchronized(internalMap) {
            return this.internalMap.size();
        }
    }

}
