// Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package gravity;

import java.util.Properties;

/**
 * Implementations of this interface will help {@link gravity.Gravity}build the
 * {@link gravity.Container}. Gravity can accept any number of plugins and each plugin is
 * responsible for building its modules and adding them to the container.
 * <p>
 * Creating a new plugin is as simple as implementing this interface and dropping it in the
 * classpath and writing a plugin manifest file (<code>gravity-plugin.properties</code>) and
 * placing it under the <code>META-INF</code> directory in the classpath. The manifest file should
 * syndicate the plugin implementation class name if it is not the default plugin class (
 * {@link gravity.plugins.bsh.BshPlugin}).
 * 
 * @author Harish Krishnaswamy
 * @version $Id: Plugin.java,v 1.6 2004-09-02 04:04:47 harishkswamy Exp $
 */
public interface Plugin
{
    String LOCATION_URL_KEY = "$gravity.Plugin.locationUrlKey$";

    /**
     * This method will take the plugin properties and the container, and build its modules and
     * populate the provided container.
     */
    void startup(Properties props, MutableContainer container);
}