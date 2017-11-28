/*
 * UCL MSR Processor (c) by Blair Butterworth and Chenghui Fan
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.msr;

import cc.kave.commons.model.events.IDEEvent;

/**
 * Implementors of this interface provide a method that is called when
 * iterating through a collection of event data.
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */
public interface EventProcessor
{
    void process(IDEEvent event);
}
