/*
 * UCL MSR Processor (c) by Blair Butterworth and Chenghui Fan
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.msr.data;

import cc.kave.commons.model.events.userprofiles.Educations;
import cc.kave.commons.model.events.userprofiles.Positions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Instances of this class contain information about a user.
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */
public class ProfileDetails
{
    private String identifier;
    private Collection<String> sessions;
    private Positions position;
    private Educations education;

    public ProfileDetails()
    {
        sessions = Collections.synchronizedCollection(new ArrayList<>());
    }

    public void addSession(String session)
    {
        sessions.add(session);
    }

    public String getIdentifier() {
        return identifier;
    }

    public Collection<String> getSessions() {
        return sessions;
    }

    public Educations getEducation() {
        return education;
    }

    public Positions getPosition() {
        return position;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setPosition(Positions position) {
        this.position = position;
    }

    public void setEducation(Educations education) {
        this.education = education;
    }
}
