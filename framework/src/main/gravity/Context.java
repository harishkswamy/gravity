// Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package gravity;

import gravity.util.ClassUtils;
import gravity.util.ReflectUtils;
import gravity.util.ThreadEvent;
import gravity.util.Utils;

import java.net.URL;

/**
 * This is the framework context. This context wraps the application context and intercepts all
 * access to the application context. Thereby if the application context does not have a requested
 * item, this context will search for any defaults defined here and return it if found.
 * <p>
 * Every component holds a reference to this context, although different components may hold
 * different instance references. It is guaranteed that the context of a component will be stable
 * throught out the life of the component.
 * <p>
 * This also acts as a parameterized factory for the framework APIs.
 * <p>
 * In addition this context has helper methods for the framework.
 * 
 * @author Harish Krishnaswamy
 * @version $Id: Context.java,v 1.2 2005-10-06 21:59:22 harishkswamy Exp $
 */
public interface Context extends ApplicationContext
{
    /**
     * @return Returns the {@link ExceptionWrapper} registred in this context.
     */
    ExceptionWrapper getExceptionWrapper();

    /**
     * @return Returns the {@link Utils} registred in this context.
     */
    Utils getUtils();

    /**
     * @return Returns the {@link ClassUtils} registred in this context.
     */
    ClassUtils getClassUtils();

    /**
     * @return Returns the {@link ReflectUtils} registered in this context.
     */
    ReflectUtils getReflectUtils();

    /**
     * This method finds the API implementation class name for the provided key from the framework
     * properties and returns a newly constructed instance of the implementation class.
     * <p>
     * NOTE: The implementation class must have a no args constructor.
     * 
     * @param implNameKey
     *            The name of the key used to find the implementation class name from the framework
     *            properties.
     * @return Object The new API instance.
     */
    Object newApiInstance(Object implKey);

    /**
     * @return Returns the container held in this context.
     */
    Container getContainer();

    /**
     * @return Returns the {@link ThreadEvent} registered in this context.
     */
    ThreadEvent getThreadEvent();

    /**
     * This method will first make a copy of this context and then it will create a new application
     * context with the provided name and properties and wrap the application context held in this
     * context with the newly created context. It will then swap the context of the contained
     * container to the copy of the context that now has the new application context.
     * 
     * @param name
     *            Name of the new context.
     * @param propertiesUrl
     *            The URL of the properties of the new context.
     * @return Copy of this context that has the new application context.
     */
    Context decorateApplicationContext(String name, URL propertiesUrl);

    /**
     * This method will strip the wrapped application context 1 level and then swap the context of
     * the contained container to the copy of the context that now has the stripped application
     * context.
     */
    void stripApplicationContext();
}